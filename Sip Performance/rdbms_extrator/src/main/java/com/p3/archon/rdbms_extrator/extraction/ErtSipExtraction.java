package com.p3.archon.rdbms_extrator.extraction;

import com.google.code.externalsorting.ExternalSort;
import com.p3.archon.rdbms_extrator.beans.*;
import com.p3.archon.rdbms_extrator.core.ConnectionChecker;
import com.p3solutions.writer.beans.SipRecordData;
import com.p3solutions.writer.options.ColumnInfo;
import com.p3solutions.writer.options.Options;
import com.p3solutions.writer.writeOperation.SipWriteEngineExecutable;
import com.p3solutions.writer.writeOperation.SipWriteEngineHandler;
import lombok.SneakyThrows;
import org.apache.log4j.Logger;

import java.io.*;
import java.sql.*;
import java.util.Date;
import java.util.*;
import java.util.concurrent.ExecutionException;

import static com.p3.archon.rdbms_extrator.utils.GeneralUtil.getXmlValidData;
import static com.p3.archon.rdbms_extrator.utils.GeneralUtil.timeDiff;
import static com.p3solutions.writer.utility.others.Utility.getTextFormatted;

public class ErtSipExtraction extends ExtractorCore {
    public int runningthread = 0;
    CompleteTableInfo completeTableInfo;
    String keyFileName;
    String keyFileOrConditionFile;
    List<ConnectionPool> connectionPool;
    Map<String, List<ColumnInfo>> tableColumnMapping = new LinkedHashMap<>();
    Set<String> checkedTable = new LinkedHashSet<>();
    int level = 0;
    int recordCount = 0;
    private Logger LOGGER = Logger.getLogger(this.getClass().getName());
    private String mainTable;


    public ErtSipExtraction(InputArgs inputBean, String schemaName, String mainTable, CompleteTableInfo completeTableInfo) {
        super(schemaName, null, null, inputBean);
        this.mainTable = mainTable;
        this.completeTableInfo = completeTableInfo;
        connectionPool = new ArrayList<>();
        keyFileName = inputBean.getOutputPath() + File.separator + "temp_keyFile" + ".txt ";
        keyFileOrConditionFile = inputBean.getOutputPath() + File.separator + "temp_keyOrConditionFile" + ".txt ";
    }

    public void startExtraction() {
        if (preProcessing()) {
            splitIntoKeyOrCondition();
            forkAndJoinProcess();
            postProcessing();
        }
    }

    private void splitIntoKeyOrCondition() {

        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(new File(keyFileOrConditionFile)));
            BufferedReader br = new BufferedReader(new FileReader(new File(keyFileName)));

            List<String> orCOnditonBuffer = new ArrayList<>();
            String contentLine = br.readLine();
            while (contentLine != null && !contentLine.isEmpty() && !contentLine.equalsIgnoreCase("")) {
                orCOnditonBuffer.add(contentLine);
                if (orCOnditonBuffer.size() == inputBean.getSplitwisethread()) {
                    write(String.join(" OR ", orCOnditonBuffer) + "\n", bw);
                    orCOnditonBuffer.clear();
                }
                contentLine = br.readLine();
            }
            if (orCOnditonBuffer.size() > 1) {
                write(String.join(" OR ", orCOnditonBuffer), bw);
            }
            bw.flush();
            bw.close();
            br.close();

        } catch (IOException e) {
            LOGGER.error("Failed to split the files " + e.getStackTrace());
        }
    }

    private void postProcessing() {
        printReport();
        printAverage();
    }

    private void printAverage() {
        System.out.println("Report" +
                "\n\tTotal Queries : " + StatusProcessor.totalQueries +
                "\n\tTotal Queries execution Time : " + timeDiff(StatusProcessor.totalQueriesTime) +
                "\n\tTotal Process : " + StatusProcessor.totalProcess +
                "\n\tTotal Processing Time : " + timeDiff(StatusProcessor.totalProcessingTime) +
                "\n\tTotal Batch Adds : " + StatusProcessor.totalAdds +
                "\n\tTotal Batch Addition Time : " + timeDiff(StatusProcessor.totalAddTime)
        );
    }

    private void printReport() {
        System.out.println("Average" +
                "\n\tAverage Queries Time :" + ((double) StatusProcessor.totalQueriesTime / (double) StatusProcessor.totalQueries) + " ms" +
                "\n\tAverage Processing Time : " + ((double) StatusProcessor.totalProcessingTime / (double) StatusProcessor.totalProcess) + " ms" +
                "\n\tAverage Batch Addition Time : " + ((double) StatusProcessor.totalAddTime / (double) StatusProcessor.totalAdds) + " ms"
        );
    }

    private void forkAndJoinProcess() {

        establishConnectionPool();

        Map<Integer, String> conditionList = new LinkedHashMap<>();
        try {
            BufferedReader br = new BufferedReader(new FileReader(new File(keyFileOrConditionFile)));
            String conditonStatement = br.readLine();
            int i = 0;
            while (conditonStatement != null && !conditonStatement.isEmpty() && !conditonStatement.equalsIgnoreCase("")) {
                conditionList.put(i, conditonStatement);
                i++;
                conditonStatement = br.readLine();
            }
        } catch (FileNotFoundException e) {
            LOGGER.error("FileNotFoundException :" + e.getStackTrace());
        } catch (IOException e) {
            LOGGER.error("IOException :" + e.getStackTrace());
        }
        try {

            final TableDetailsInfo mainTableDetailsInfo = completeTableInfo.getTableDetails().get(mainTable);

            Thread.sleep(inputBean.getWaitTime());
            customThreadPool.submit(() -> {
                conditionList.entrySet().parallelStream().forEach(
                        condition -> {
                            try {
                                Thread.currentThread().setName("Process-" + runningthread++);
                                String title = schemaName + "-" + mainTable + "-" + condition.getKey() + Thread.currentThread().getName();
                                Options options = getOptionsForWriter(true, title);
                                SipWriteEngineExecutable sipWriteEngineExecutable = new SipWriteEngineExecutable(options, title);
                                SipWriteEngineHandler sipWriteEngineHandler = sipWriteEngineExecutable.getSipWriteEngineHandler();
                                sipWriteEngineHandler.createBatchAssembler();
                                startExtractionForMainTable(condition.getKey(), condition.getValue(), mainTableDetailsInfo, sipWriteEngineHandler);
                                LOGGER.info("Batch " + Thread.currentThread().getName() + " closing");
                                sipWriteEngineHandler.closeBatchAssembler();
                                LOGGER.info("Batch " + Thread.currentThread().getName() + " closed");

                            } catch (Exception e) {
                                LOGGER.error("InterruptedException :" + e.getStackTrace());
                            }
                        }
                );
            }).get();
            LOGGER.info("END");
        } catch (
                InterruptedException e) {
            LOGGER.error("InterruptedException :" + e.getStackTrace());
        } catch (ExecutionException e) {
            LOGGER.error("ExecutionException :" + e.getStackTrace());
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                demolishConnectionPool();
            }
        }).start();
    }

    private void startExtractionForMainTable(Integer key, String conditonStatement, TableDetailsInfo mainTableDetailsInfo, SipWriteEngineHandler sipWriteEngineHandler) {
        int whichConnection = 0;
        try {
            Connection connection = null;
            while (connection == null) {
                for (int i = 0; i < connectionPool.size(); i++) {
                    if (!connectionPool.get(i).isInUse()) {
                        whichConnection = i;
                        connection = connectionPool.get(i).getConnection();
                        connectionPool.get(i).setInUse(true);
                        break;
                    }
                }
            }
            Set<String> checkedTable = new LinkedHashSet<>();
            getRecordsForEachTable(key, connection, sipWriteEngineHandler, mainTableDetailsInfo, queryFrammer(mainTableDetailsInfo, mainTable, conditonStatement), new StringBuffer(), checkedTable);

        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("While creating option  the table  (" + schemaName + "." + mainTable + ")");
            LOGGER.error("Exception :" + e.getMessage());
        }
        connectionPool.get(whichConnection).reset(inputBean);
    }


    private String queryFrammer(TableDetailsInfo tableInfo, String tableName, String filterQuery) {
        StringBuffer sb = new StringBuffer();
        for (String string : tableInfo.getRelatedTables()) {
            sb.append("," + schemaName + "." + string);
        }
        if (tableName.equalsIgnoreCase(this.mainTable))
            return "select distinct" + tableInfo.getQueryClause()
                    + (tableInfo.getQueryforArchonKeyColumnId().isEmpty() ? ""
                    : " ," + tableInfo.getQueryforArchonKeyColumnId())

                    + " "+getMainTableFromWhereFilter();
                    /*+ " from " + schemaName + "." + tableName + sb.toString()
                    + (tableInfo.getWhereOrderClause().isEmpty() ? "" : tableInfo.getWhereOrderClause())
                    + (filterQuery.isEmpty() ? " "
                    : ((tableInfo.getWhereOrderClause().isEmpty() ? " where " : " and ") + " ( " + filterQuery
                    + " ) "));*/
        return "select distinct" + tableInfo.getQueryClause()
                + (tableInfo.getQueryforArchonKeyColumnId().isEmpty() ? ""
                : " ," + tableInfo.getQueryforArchonKeyColumnId())
                + " from " + schemaName + "." + tableInfo.getOriginalName() + sb.toString()
                + (tableInfo.getWhereOrderClause().isEmpty() ? " where " + filterQuery
                : " " + tableInfo.getWhereOrderClause() + " and " + " ( " + filterQuery + " ) ");
    }

    private void getRecordsForEachTable(Integer key, Connection connection, SipWriteEngineHandler seh, TableDetailsInfo
            tableInfo, String query, StringBuffer stringBuffer, Set<String> checkedTable) {

        Date startProcessingTime = null;
        if (checkedTable.contains(tableInfo.getOriginalName()))
            return;
        Date startQueryTime;
        startQueryTime = new Date();

        Statement statement = null;
        ResultSet resultSet = null;
        try {
            statement = connection.createStatement();
            LOGGER.fatal("Query :" + query);
            resultSet = statement.executeQuery(query);
            StatusProcessor.updateTotalQueries();
            StatusProcessor.updateTotalQueriesTime(startQueryTime);
            List<ColumnInfo> columnsInfo = tableColumnMapping.get(tableInfo.getOriginalName());
            if (columnsInfo == null) {
                columnsInfo = getColumnInfo(resultSet.getMetaData());
                tableColumnMapping.put(tableInfo.getOriginalName(), columnsInfo);
            }
            level++;
            checkedTable.add(tableInfo.getOriginalName());
            if(!tableInfo.getOriginalName().equalsIgnoreCase(mainTable)){
                stringBuffer.append(indentLevel(level) + "\n");
                stringBuffer.append("<TABLE_" + tableInfo.getAliasName() + ">" + "\n");
            }
            while (resultSet.next()) {
                if(tableInfo.getOriginalName().equalsIgnoreCase(mainTable)){
                    stringBuffer.append(indentLevel(level) + "\n");
                    stringBuffer.append("<TABLE_" + tableInfo.getAliasName() + ">" + "\n");
                }
                startProcessingTime = new Date();

                stringBuffer.append(indentLevel(level++) + "<" + tableInfo.getAliasName() + "_ROW>" + "\n");
                Map<String, String> archonKeyIdString = new LinkedHashMap<>();
                for (int i = 1; i <= columnsInfo.size(); i++) {
                    String columnName = columnsInfo.get(i - 1).getColumn();
                    String columnValue = "";
                    Object columnData;
                    int javaSqlType = columnsInfo.get(i - 1).getJavaDataType();

                    if (columnName.startsWith("_ARCHON") && columnName.endsWith("_")) {
                        archonKeyIdString.put(columnName, resultSet.getString(columnName));
                        continue;
                    }
                    columnName = getTextFormatted(columnName);
                    Object columnNewValue = getColumnData(resultSet, i, columnsInfo.get(i - 1).getJavaDataType(), columnsInfo.get(i - 1).getDataType());
                    if (columnNewValue == null) {
                        columnValue = "";
                    } else {
                        if (javaSqlType == Types.DOUBLE) {
                            columnValue = String.format("%.2f", columnNewValue);
                        } else {
                            columnValue = columnNewValue.toString();
                        }
                    }
                    stringBuffer.append(indentLevel(level)).append(getSipColumnString(columnName, columnValue)).append("\n");
                }
                for (String relatedTableCond : tableInfo.getTableJoinRelCond().keySet()) {
                    if (!checkedTable.contains(relatedTableCond)) {
                        TableDetailsInfo relationShipTableInfo = completeTableInfo.tableDetails.get(relatedTableCond);
                        String relationCondQuery = tableInfo.getTableJoinRelCond().get(relatedTableCond);
                        String filterQuery = replaceRelationCondQuery(relationCondQuery, tableInfo.getOriginalName(), archonKeyIdString, tableInfo.getWholeExecuteQuery().get(relatedTableCond));
                        getRecordsForEachTable(key, connection, seh, relationShipTableInfo,
                                queryFrammer(relationShipTableInfo, relatedTableCond, filterQuery), stringBuffer, checkedTable);
                    }
                }
                level--;
                stringBuffer.append(indentLevel(level) + "</" + tableInfo.getAliasName() + "_ROW>" + "\n");
                if (tableInfo.getOriginalName().equalsIgnoreCase(mainTable)) {
                    stringBuffer.append(indentLevel(level--) + "</TABLE_" + tableInfo.getAliasName() + ">");
                    Date startAddTime = new Date();
                    seh.iterateRows(new SipRecordData(stringBuffer.toString(), new ArrayList<>()));
                    StatusProcessor.updateTotalAdds();
                    StatusProcessor.updateTotalAddTime(startAddTime);
                    stringBuffer.delete(0, stringBuffer.length());
                    checkedTable.clear();
                    checkedTable.add(mainTable);
                    StatusProcessor.updateTotalProcess();
                    StatusProcessor.updateTotalProcessingTime(startProcessingTime);
                    updateRecordCount();
                    LOGGER.info(Thread.currentThread().getName() + " : Total Time Taken for record : " + recordCount + "  : "
                            + timeDiff(new Date().getTime() - startProcessingTime.getTime()));
                    /*if (recordCount > 100) {
                        break;
                    }*/
                }
            }
            stringBuffer.append(indentLevel(level--) + "</TABLE_" + tableInfo.getAliasName() + ">");
            try {
                statement.close();
                resultSet.close();
            } catch (SQLException e) {
                LOGGER.error("Failed to close Statemet or Resultset :" + e.getStackTrace());
                LOGGER.error("Exception :" + e.getStackTrace());
            }
            checkedTable.remove(tableInfo.getOriginalName());
        } catch (Exception e) {
            LOGGER.error("While retrieving  the table  (" + schemaName + "." + tableInfo.getAliasName() + ")");
            LOGGER.error("Exception :" + e.getMessage());
            LOGGER.error("Exception :" + e.getStackTrace());
            e.printStackTrace();
        } finally {

        }
    }

    private synchronized void updateRecordCount() {
        recordCount++;
    }

    /*private void checkConnectionValidOrNot(String originalName) {
        boolean status = false;
        Statement statement = null;
        try {
            statement = connection.createStatement();
        } catch (SQLException e) {
            LOGGER.error("While retrieving  the table  (" + schemaName + ". " + originalName + ")");
            LOGGER.error("SQLException :" + e.getStackTrace());
            for (int i = 0; i < connectionPool.size(); i++) {
                if (!connectionPool.get(i).isInUse()) {
                    connection = connectionPool.get(i).getConnection();
                    LOGGER.debug("Connection Recreated with " + connectionPool.get(i) + " for :(" + schemaName + ". " + originalName + ")");
                    connectionPool.get(i).setInUse(true);
                    status = true;
                    break;
                }
            }
            if (!status) {
                connection = new ConnectionChecker().checkConnection(inputBean);
                LOGGER.debug("Connection Recreated  with Inputs  for :(" + schemaName + ". " + originalName + ")");
            }

        } finally {
            try {
                statement.close();
            } catch (SQLException e) {
                LOGGER.error("Failed to close the statement :" + e.getStackTrace());
                LOGGER.error("Exception :" + e.getStackTrace());
            }
        }
    }*/

    private String replaceRelationCondQuery(String relationCondQuery, String
            tableName, Map<String, String> archonKeyIdString, Set<String> keyColumnsList) {
        for (String keyColumn : keyColumnsList) {
            String columnUUID = completeTableInfo.getCompletedKeyColumnUUid().get(tableName + "." + keyColumn.trim());
            String replaceValue = archonKeyIdString.get(columnUUID);
            relationCondQuery = relationCondQuery.replace(tableName + "." + keyColumn.trim(), replaceValue);
        }
        return relationCondQuery;
    }

    private String indentLevel(int n) {
        return "";
    }

    private void demolishConnectionPool() {

        for (int i = 0; i < connectionPool.size(); i++) {
            try {
                LOGGER.info("Closing connection " + i);
                if (connectionPool.get(i).isInUse()) {
                    connectionPool.get(i).getConnection().close();
                    LOGGER.info("Closed connection " + i);
                }
            } catch (Exception e) {
                LOGGER.error("Failed to close connection " + i);
            }
        }
    }


    private void establishConnectionPool() {
        for (int i = 0; i < THREAD_CAPACITY; i++) {
            Connection connection = new ConnectionChecker().checkConnection(inputBean);
            if (connection != null) {
                ConnectionPool cp = new ConnectionPool();
                cp.setConnection(connection);
                cp.setInUse(false);
                connectionPool.add(cp);
                LOGGER.trace("Connection" + " " + i + " - > " + cp.getConnection().toString());
            }
        }
    }

    @SneakyThrows
    private boolean preProcessing() {
        boolean status = false;
        Connection connection = connectionChecker.checkConnection(inputBean);
        ResultSet resultSet = null;
        try {
            resultSet = connection.createStatement().executeQuery("select count(1) from  " + schemaName + "." + mainTable);
            resultSet.next();
            if (resultSet.getLong(1) == 0) {
                //if rs.next() returns false
                LOGGER.debug("No record found for this root table " + (schemaName + "." + mainTable));
                status = false;
            } else {
                LOGGER.info("Result Count :" + resultSet.getLong(1));
                prepareKeyInputs(connection);
                status = true;
            }
        } catch (SQLException | IOException e) {
            e.printStackTrace();
            LOGGER.error("While retrieving  the table  (" + schemaName + "." + this.mainTable + ")");
            LOGGER.error("Exception :" + e.getMessage());
            status = false;
        } finally {
            if (resultSet != null)
                resultSet.close();
            if (connection != null)
                connection.close();
        }
        return status;
    }

    private void prepareKeyInputs(Connection connection) throws SQLException, IOException {
        String keyFileNamePre = keyFileName.replace(".txt", "_pre.txt");
        BufferedWriter bw = new BufferedWriter(new FileWriter(new File(keyFileNamePre)));
        String query = "select " +
                keyQueryFramer(completeTableInfo.getTableDetails().get(mainTable).getKeyColumns())
                + getMainTableFromWhereFilter();
        LOGGER.info("Main Key file Query : " + query);
        String queryCount = "select count(1) "
                + getMainTableFromWhereFilter();
        ResultSet queryCountRs = connection.createStatement().executeQuery(queryCount);
        queryCountRs.next();
        LOGGER.info("Main key file Query Count  :"+queryCount);
        LOGGER.info("Main Key file Query Count : " + queryCountRs.getLong(1));

        ResultSet keyValues = connection.createStatement().executeQuery(query);
        boolean processed = false;
        String queryFiller = "";
        ResultSetMetaData x = keyValues.getMetaData();
        String y;
        while (keyValues.next()) {
            if (!processed) {
                StringBuffer sb = new StringBuffer();
                for (int i = 1; i <= x.getColumnCount(); i++) {
                    String sym = (x.getColumnTypeName(i).equalsIgnoreCase("INTEGER") || x.getColumnTypeName(i).equalsIgnoreCase("DECIMAL") || x.getColumnTypeName(i).equalsIgnoreCase("NUMBER")) ? "" : "\'";
                    sb.append("and ").append(schemaName + "." + mainTable + ".").append(x.getColumnName(i)).append(" = ").append(sym).append(":::::").append(x.getColumnName(i)).append(":::::").append(sym).append(" ");
                }
                queryFiller = sb.toString().length() > 0 ? "(" + sb.toString().substring(4) + ")" : sb.toString();
                y = queryFiller;
                for (int i = 1; i <= x.getColumnCount(); i++) {
                    // y = y.replace(":::::" + x.getColumnName(i) + ":::::", keyValues.getString(i).trim());
                    y = y.replace(":::::" + x.getColumnName(i) + ":::::", keyValues.getString(i));
                }
                write(y + "\n", bw);
            }
        }
        keyValues.close();
        bw.flush();
        bw.close();
        List<File> l = ExternalSort.sortInBatch(new File(keyFileNamePre));
        ExternalSort.mergeSortedFiles(l, new File(keyFileName), ExternalSort.defaultcomparator, true);
    }

    private String getMainTableFromWhereFilter() {
        if (inputBean.isExtaConditon())
            return " " + completeTableInfo.getMainTableWholeFilterQuery();
        return " from " + schemaName + "." + mainTable;
    }

    private String keyQueryFramer(List<String> keyColumns) {
        StringBuffer sb = new StringBuffer();
        for (String key : keyColumns)
            sb.append(",").append(schemaName + "." + mainTable + "." + key);
        return sb.length() > 0 ? sb.toString().substring(1) : sb.toString();
    }
    /*private String keyQueryFramer(List<String> keyColumns) {
        StringBuffer sb = new StringBuffer();
        for (String key : keyColumns)
            sb.append(",").append(key);
        return sb.length() > 0 ? sb.toString().substring(1) : sb.toString();
    }*/

    private void write(String replace, BufferedWriter bufferedWriter) throws IOException {
        bufferedWriter.write(replace);
        bufferedWriter.flush();
    }

    private String getSipColumnString(String columnName, String columnValue) {
        return "<" + columnName.trim() + ">" + (getXmlValidData(columnValue)).trim() + "</" + columnName.trim() + ">";
    }
}
