package com.p3.archon.rdbms_extrator.processor;

import com.p3.archon.rdbms_extrator.beans.CompleteTableInfo;
import com.p3.archon.rdbms_extrator.beans.InputArgs;
import com.p3.archon.rdbms_extrator.core.ConnectionChecker;
import com.p3.archon.rdbms_extrator.core.ParseJsonFileIntoBean;
import com.p3.archon.rdbms_extrator.extraction.ErtSipExtraction;
import com.p3.archon.rdbms_extrator.extraction.NonSipExtraction;
import com.p3.archon.rdbms_extrator.extraction.SipExtraction;
import com.p3.archon.rdbms_extrator.utils.FileUtil;
import lombok.SneakyThrows;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.apache.wink.json4j.JSONObject;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * Created by Suriyanarayanan K
 * on 29/02/20 1:05 PM.
 */
public class Processor {

    //input arguments
    private final String[] args;
    String schemaName;
    Connection connection;

    private Logger LOGGER = Logger.getLogger(this.getClass().getName());
    private InputArgs inputBean;
    //connection
    private ConnectionChecker connectionChecker = new ConnectionChecker();

    /*Set<String> checkedTablesList = new LinkedHashSet<>();
     */
    public Processor(String[] args) {
        this.args = args;
        this.inputBean = InputArgs.builder().build();
    }

    public void setValuesIntoBean() {
        CmdLineParser parser = new CmdLineParser(inputBean);
        try {
            parser.parseArgument(args);
            validateInputs(inputBean);
        } catch (CmdLineException e) {
            parser.printUsage(System.err);
            LOGGER.error("Please check arguments specified. \n" + e.getMessage() + "\nTerminating ... ");
            System.exit(1);
        }
    }

    private void validateInputs(InputArgs inputBean) {
        String outputPath = inputBean.getOutputPath();
        try {
            if (!FileUtil.checkForDirectory(outputPath)) {
                LOGGER.error("Wrong Output Path " + outputPath);
                System.exit(2);
            } else {
                outputPath = outputPath + File.separator + UUID.randomUUID().toString();
                FileUtil.checkCreateDirectory(outputPath);
                inputBean.setOutputPath(outputPath);
                LOGGER.debug("Path to generate output " + outputPath);
            }
            if (inputBean.getSplitByRecord() <= 0) {
                inputBean.setSplitByRecord(10000);
            }
            if (inputBean.getSplitBySize() <= 0) {
                inputBean.setSplitBySize(100);
            }
            if (inputBean.getSplitwisethread() <= 0) {
                inputBean.setSplitwisethread(25);
            }
            if (inputBean.getThreadCount() <= 0) {
                inputBean.setThreadCount(10);
            }
            if(inputBean.getWaitTime()<=1){
                inputBean.setWaitTime(1000);
            }
        } catch (Exception e) {
            LOGGER.error("Wrong Output Path " + outputPath);
            LOGGER.error(e.getMessage());
        }
        try {
            if (inputBean.isErt()) {
                if (!FileUtil.checkForFile(inputBean.getSipInputFile())) {
                    LOGGER.error("Wrong File Path for SIP Input Json File " + outputPath);
                    System.exit(2);
                } else {
                    String content = FileUtils.readFileToString(new File(inputBean.getSipInputFile()), StandardCharsets.UTF_8);
                    JSONObject jsonObject = new JSONObject(content);
                    if (inputBean.getDatabaseServer().equalsIgnoreCase("as400noport") ||
                            inputBean.getDatabaseServer().equalsIgnoreCase("as400")) {
                        content = content
                                .replace("\'\'", "\'")
                                .replace(inputBean.getSchemaName() + "/", "");
                    }
                    JSONObject jsonObject1 = new JSONObject(content);
                    LOGGER.debug("Input Json :" + jsonObject.toString());
                    inputBean.setInputJson(content);
                }
            }
        } catch (Exception e) {
            LOGGER.error("Exception " + e.getStackTrace());
        }
    }

    public void testConnection() {
        connection = connectionChecker.checkConnection(inputBean);
        if (connection == null) {
            System.exit(2);
        }
        schemaName = inputBean.getSchemaName();
    }

    @SneakyThrows
    public void startExtraction() {
        if (inputBean.isErt()) {
            doErt();
        } else if (inputBean.isQuery())
            doQuery();
        else
            doNonQuery();
    }


    private void doErt() {
        ParseJsonFileIntoBean parseJsonFileIntoBean = new ParseJsonFileIntoBean(inputBean);
        CompleteTableInfo resultantCompleteTableInfo = parseJsonFileIntoBean.parseInputJsonIntoCompletetableDetails();
        ErtSipExtraction ertSipExtraction = new ErtSipExtraction(inputBean, schemaName, resultantCompleteTableInfo.maintable, resultantCompleteTableInfo);
        ertSipExtraction.startExtraction();
    }

    /*private void parseInputJsonIntoCompletetableDetails() {
        String currentTable = "";
        try {
            Set<String> checkedTable = new LinkedHashSet<>();
            completeTableInfo = new CompleteTableInfo();
            JSONObject obj = new JSONObject(inputBean.getInputJson());
            JSONObject jsontables = new JSONObject(obj.get("tables"));
            tableList.addAll(jsontables.keySet());
            completedKeyColumnUUid = new LinkedHashMap<>();

            for (String table : tableList) {
                currentTable = table;
                checkedTable.add(table);
                tableComp = new TableDetailsInfo();
                JSONObject tableDetails = obj.getJSONObject("tables").getJSONObject(table);
                tableComp.setSelected(Boolean.valueOf(tableDetails.getString("selected")));
                tableComp.setDeleteData(Boolean.valueOf(tableDetails.getString("deleteData")));
                tableComp.setWhereOrderClause(tableDetails.getString("whereOrderClause"));

                //tableComp.setQueryClause();

                tableComp.setKeyColumns(new JSONArray(tableDetails.getString("KeyColumns").replace("\"", "")));
                if (tableDetails.getString("aliasTableName") != null
                        || !tableDetails.getString("aliasTableName").equals("null"))
                    tableComp.setAliasName(tableDetails.getString("aliasTableName"));
                else
                    tableComp.setAliasName(table);
                tableComp.setOriginalName(table);
                tableComp.setMainTable(tableDetails.getBoolean("mainTable"));
                if (tableComp.isMainTable()) {
                    mainTable = tableComp.getOriginalName();
                }
                TreeMap<String, String> tableJoinRelCond = new TreeMap<String, String>();
                JSONObject relationCond = new JSONObject(tableDetails.getString("TableJoinRelCond").trim());
                Iterator<String> keys = relationCond.keys();
                while (keys.hasNext()) {
                    String key = keys.next();
                    tableJoinRelCond.put(key, relationCond.getString(key));
                }
                tableComp.setTableJoinRelCond(tableJoinRelCond);
                JSONArray colArray = new JSONArray(tableDetails.getString("column"));
                columnsList = new ArrayList<>();
                ColumnBean column = null;
                Map<String, String> columnType = new LinkedHashMap<>();
                for (int i = 0; i < colArray.length(); i++) {
                    JSONObject columnJson = colArray.getJSONObject(i);
                    column = new ColumnBean();
                    column.setOriginalColName(columnJson.getString("originalColName"));
                    column.setExpColName(columnJson.getString("expectedColName"));
                    column.setColType(columnJson.getString("colType"));
                    columnType.put(columnJson.getString("originalColName"), columnJson.getString("colType"));
                    columnsList.add(column);
                }

                tableComp.setColumns(columnsList);
                tableComp.setColumnType(columnType);
                completedKeyColumnUUid.putAll(getKeyColumnUUid(tableComp.getKeyColumns(), tableComp.getOriginalName()));
                tableComp.setWholeExecuteQuery(getWholeQueryFrammer(tableComp));
                tableComp.setTableJoinRelCond(getTableRelCond(tableComp, tableComp.getOriginalName(), columnType));
                tableComp.setQueryClause(tableDetails.getString("queryClause"));
                tableComp.setQueryforArchonKeyColumnId(getIdKeyColumnQuery(tableComp));
                completeTableInfo.getTableDetails().put(table, tableComp);
            }
            completeTableInfo.setRpx(obj.getString("rpx"));
            completeTableInfo.setAppName(obj.getString("appName"));
            completeTableInfo.setHolding(obj.getString("holding"));
            completeTableInfo.setMaintable(obj.getString("mainTable"));
            completeTableInfo.setCompletedKeyColumnUUid(completedKeyColumnUUid);

            checkedTablesList.add(mainTable);
            Set<String> listOfCondition = new LinkedHashSet<>();
            Set<String> maintableWholeFilterQuery = getTableWholeForMainTable("", completeTableInfo.getTableDetails().get(mainTable), listOfCondition);
            Set<String> checkedTableListWithSchema = new LinkedHashSet<>();
            for (String table : checkedTablesList) {
                checkedTableListWithSchema.add(schemaName + "." + table);
            }
            checkedTable.clear();
            completeTableInfo.setMainTableWholeFilterQuery(" from " +
                    String.join(",", checkedTableListWithSchema) + " where " +
                    String.join(" AND ", maintableWholeFilterQuery));
        } catch (JSONException e) {
            LOGGER.error("While retriving the table :" + schemaName + "." + currentTable);
            LOGGER.error("Exception :" + e.getMessage());
        }
    }

    private Set<String> getTableWholeForMainTable(String wholeFilterQueryString, TableDetailsInfo tableDetailsInfo, Set<String> listOfCondition) {
        for (Map.Entry<String, String> filterEntry : tableDetailsInfo.getTableJoinRelCond().entrySet()) {
            if (!checkedTablesList.contains(filterEntry.getKey())) {
                checkedTablesList.add(filterEntry.getKey());
                listOfCondition.add(filterEntry.getValue().replace("'", ""));
                String whereOrder = tableDetailsInfo.getWhereOrderClause().replaceFirst("where", "").replaceFirst("WHERE", "");
                String where = whereOrder.toLowerCase().contains("order by") ? whereOrder.substring(0, whereOrder.toLowerCase().indexOf("order by")) : whereOrder;
                if (!where.trim().isEmpty())
                    listOfCondition.add(where);
                listOfCondition.addAll(getTableWholeForMainTable(wholeFilterQueryString, completeTableInfo.getTableDetails().get(filterEntry.getKey()), listOfCondition));
            }
        }
        return listOfCondition;
    }


    private String getIdKeyColumnQuery(TableDetailsInfo tableComp) {
        StringBuffer keyColumnQuery = new StringBuffer();
        for (String keyColumn : tableComp.getKeyColumns()) {
            String tableColumnIdentifier = tableComp.getOriginalName() + "." + keyColumn;
            keyColumnQuery.append(",").append(inputBean.getSchemaName() + "." + tableColumnIdentifier).append(" as \"").append(completedKeyColumnUUid.get(tableColumnIdentifier)).append("\"");
        }
        return keyColumnQuery.length() > 0 ? keyColumnQuery.toString().substring(1) : keyColumnQuery.toString();
    }


    private Map<String, String> getKeyColumnUUid(List<String> keyColumnsList, String originalTableName) {
        Map<String, String> keyColumnUUid = new LinkedHashMap<>();
        for (String keyColumns : keyColumnsList)
            keyColumnUUid.put(originalTableName + "." + keyColumns, ("_ARCHON" + UUID.randomUUID().toString()).substring(0, 28) + "_");
        return keyColumnUUid;
    }

    private TreeMap<String, String> getTableRelCond(TableDetailsInfo tableComp, String originalTableName, Map<String, String> columnType) {
        TreeMap<String, String> tableRelatedJoinCond = new TreeMap<>();
        for (Map.Entry<String, Set<String>> relatedJoinCond : tableComp.getWholeExecuteQuery().entrySet()) {
            String filterQuery = tableComp.getTableJoinRelCond().get(relatedJoinCond.getKey());
            filterQuery = filterQuery.replace("= " + schemaName + ".", "=");
            for (String keyColumn : relatedJoinCond.getValue()) {
                String colType = columnType.get(keyColumn.trim());
                if (colType.equalsIgnoreCase("VARCHAR")
                        || colType.equalsIgnoreCase("TIMESTAMP")
                        || colType.equalsIgnoreCase("CHAR")) {
                    filterQuery = filterQuery.replace(originalTableName + "." + keyColumn, "\'" + originalTableName + "." + keyColumn.trim() + "\' ");
                } else if (colType.equalsIgnoreCase("INTEGER")) {
                    filterQuery = filterQuery.replace(originalTableName + "." + keyColumn, "" + originalTableName + "." + keyColumn.trim() + "");
                } else {
                    filterQuery = filterQuery.replace(originalTableName + "." + keyColumn, "\'" + originalTableName + "." + keyColumn.trim() + "\' ");
                }
            }
            tableRelatedJoinCond.put(relatedJoinCond.getKey(), filterQuery);
        }
        LOGGER.debug("tableRelatedJoinCond :" + tableRelatedJoinCond);
        return tableRelatedJoinCond;
    }

    private Map<String, Set<String>> getWholeQueryFrammer(TableDetailsInfo tableComp) {
        Map<String, Set<String>> wholeExecuteQuery = new LinkedHashMap<>();
        for (Map.Entry<String, String> tableJoinRelCond : tableComp.getTableJoinRelCond().entrySet()) {
            String relationTableName = tableJoinRelCond.getKey();
            Set<String> keyColumnList = getKeyColumnList(tableJoinRelCond.getValue());
            wholeExecuteQuery.put(relationTableName, keyColumnList);
        }
        return wholeExecuteQuery;
    }

    private Set<String> getKeyColumnList(String tableJoinRelCondValue) {

        String[] orFilterCondition = tableJoinRelCondValue.split("or");
        int orfilterCount = orFilterCondition.length - 1;
        int count = 0;
        String query = "";
        Set<String> keyColumn = new LinkedHashSet<>();
        for (String eachOrCondition : orFilterCondition) {
            //  query += " ( ";
            eachOrCondition = eachOrCondition.replace("(", "").replace(")", "");
            String[] andFilterCondition = eachOrCondition.split("and");
            for (int i = 0; i < andFilterCondition.length; i++) {
                String condition = andFilterCondition[i];
                String[] keycolumn = condition.split("=")[1].split("\\.");
                keyColumn.add(keycolumn[keycolumn.length - 1]);
                if (i != andFilterCondition.length - 1) {
                    // query += " and ";
                }
            }
            // query += " ) ";
            if (count != orfilterCount) {
                //query += " or ";
            }
            count++;
        }
        return keyColumn;
    }*/


    //queryTitle-:-QueryText;
    private void doQuery() {
        Map<String, String> queryMap = new TreeMap<>();

//        String querys = "te-:-select A.* from PMS043.F0911 A, PMS043.F0006_NN B  , PMS043.F0010_NN C, PMS043.DOCTYPEDES_NN D , PMS043.BUSUNITDES_NN E , PMS043.REPUNITDES_NN F where A.GLICUT='D' AND B.MCRP18='US' AND A.GLFY=4 AND A.GLCO=B.MCCO AND A.GLMCU=B.MCMCU  AND A.GLCO=C.CCCO   AND A.GLDCT=D.DOCTYPE AND B.MCRP16=E.BUSUNIT AND B.MCRP21=F.REPUNIT;" +
//                "te1-:-select A.* from PMS043.F0911 A, PMS043.F0006_NN B  , PMS043.F0010_NN C, PMS043.DOCTYPEDES_NN D , PMS043.BUSUNITDES_NN E , PMS043.REPUNITDES_NN F where A.GLICUT='D' AND B.MCRP18='US' AND A.GLFY=4 AND A.GLCO=B.MCCO AND A.GLMCU=B.MCMCU  AND A.GLCO=C.CCCO   AND A.GLDCT=D.DOCTYPE AND B.MCRP16=E.BUSUNIT AND B.MCRP21=F.REPUNIT;";
        String[] queryList = inputBean.getQueryListInput().split(";");
        for (int i = 0; i < queryList.length; i++) {
            String[] keyValue = queryList[i].split("-:-");
            queryMap.put(keyValue[0], keyValue[1]);
        }
        if (queryMap.size() == 0) {
            LOGGER.info("No queries exist to process.");
            return;
        }
        if (inputBean.getOutputType().equalsIgnoreCase("sip")) {

            SipExtraction sipExtraction = new SipExtraction(inputBean, schemaName, null, queryMap);
            sipExtraction.startQuerySipExtraction(connection);
        } else {
            NonSipExtraction nonSipExtraction = new NonSipExtraction(inputBean, schemaName, null, queryMap);
            nonSipExtraction.startQueryNonSipExtraction(connection);
        }
    }

    private void doNonQuery() throws SQLException {
        List<String> tableList = new ArrayList<String>();
        ResultSet resultSet = null;

        List<String> selectiveTables = new ArrayList<>();
        if (inputBean.getTableList() != null && !inputBean.getTableList().isEmpty()) {
            selectiveTables.addAll(Arrays.asList(inputBean.getTableList().split(";")));
        }
        try {
            resultSet = connection.getMetaData().getTables(connection.getCatalog(), schemaName, null, new String[]{"TABLE"});
            while (resultSet.next()) {
                if (selectiveTables.isEmpty())
                    tableList.add(resultSet.getString("TABLE_NAME"));
                else if (selectiveTables.contains(resultSet.getString("TABLE_NAME"))) {
                    tableList.add(resultSet.getString("TABLE_NAME"));
                }
            }
            LOGGER.debug("Table List for a schema(" + schemaName + ")-->" + tableList);
            if (inputBean.getOutputType().equalsIgnoreCase("sip")) {
                SipExtraction sipExtraction = new SipExtraction(inputBean, schemaName, tableList, null);
                sipExtraction.startSipExtraction(connection);
            } else {
                NonSipExtraction nonSipExtraction = new NonSipExtraction(inputBean, schemaName, tableList, null);
                nonSipExtraction.startNonSipExtraction(connection);
            }
        } catch (SQLException e) {
            LOGGER.error("Exception :" + e.getMessage());
            e.printStackTrace();
        } finally {
            if (resultSet != null)
                resultSet.close();
        }
    }


}
