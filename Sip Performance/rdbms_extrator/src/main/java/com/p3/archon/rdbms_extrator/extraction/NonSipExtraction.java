package com.p3.archon.rdbms_extrator.extraction;

import com.p3.archon.rdbms_extrator.beans.InputArgs;
import com.p3.archon.rdbms_extrator.report.ReportGeneration;
import com.p3solutions.writer.options.ColumnInfo;
import com.p3solutions.writer.options.Options;
import com.p3solutions.writer.writeOperation.WriteEngineExecutable;
import com.p3solutions.writer.writeOperation.WriteEngineHandler;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import static com.p3.archon.rdbms_extrator.utils.GeneralUtil.timeDiff;
import static com.p3solutions.writer.utility.others.Utility.getTextFormatted;

/**
 * Created by Suriyanarayanan K
 * on 29/02/20 4:19 PM.
 */
public class NonSipExtraction extends ExtractorCore {

    double totalprocessingtime;
    double totalWritingtime;
    double totalProcessingRecord;
    double totalWritingRecords;
    private Logger LOGGER = Logger.getLogger(this.getClass().getName());
    ReportGeneration reportGeneration = null;

    public NonSipExtraction(InputArgs inputBean, String schemaName, List<String> tableList, Map<String, String> queryMap) {
        super(schemaName, tableList, queryMap, inputBean);
        reportGeneration = new ReportGeneration();
    }

    public void startQueryNonSipExtraction(Connection connection) {
        try {
            customThreadPool.submit(
                    () -> queryMap
                            .entrySet().parallelStream()
                            .forEach(queryMapitem ->
                                    startQueryExtractionProcess(queryMapitem.getKey(), queryMapitem.getValue(), connection))
            ).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
            LOGGER.error("While retrieving  the schema  (" + schemaName + ")");
            LOGGER.error("Exception :" + e.getMessage());
        } catch (ExecutionException e) {
            e.printStackTrace();
            LOGGER.error("While retrieving  the schema  (" + schemaName + ")");
            LOGGER.error("Exception :" + e.getMessage());
        }

        reportGeneration.printAverage();
    }

    public void startNonSipExtraction(Connection connection) {
        try {
            customThreadPool.submit(
                    () -> tableList
                            .parallelStream()
                            .forEach(tableName ->
                                    startTableExtractionProcess(tableName, connection))
            ).get();


        } catch (InterruptedException e) {
            e.printStackTrace();
            LOGGER.error("While retrieving  the schema  (" + schemaName + ")");
            LOGGER.error("Exception :" + e.getMessage());
        } catch (ExecutionException e) {
            e.printStackTrace();
            LOGGER.error("While retrieving  the schema  (" + schemaName + ")");
            LOGGER.error("Exception :" + e.getMessage());
        }

        reportGeneration.printAverage();
    }

    private void startQueryExtractionProcess(String titleKey, String query, Connection connection) {
        long processStartTime = new Date().getTime();
        long sourceCount = 0;
        List<Object> values = new ArrayList<>();
        String title = schemaName + "-" + getTextFormatted(titleKey).toUpperCase();

        try {
            ResultSet resultSet;
            Statement statement;
            statement = connection.createStatement();
            System.out.println("Starting execution for query " + titleKey + " : " + new Date());

            ResultSet resultSetCount1 = statement.executeQuery("select count(1)  " + query.substring(query.toLowerCase().indexOf(" from")));
            resultSetCount1.next();
            sourceCount = resultSetCount1.getLong(1);

            resultSet = statement.executeQuery(query);

            if (!resultSet.next()) {
                LOGGER.debug("No record found for query with title " + titleKey);
                System.out.println("No records found for query with title " + titleKey);
                reportGeneration.reportInfo(titleKey
                        , timeDiff(new Date().getTime() - processStartTime),
                        "000ms", sourceCount, 0, Boolean.toString(0 == sourceCount),
                        "Nothing to process", new Date().getTime() - processStartTime, 0);
            } else {

                List<ColumnInfo> columnsInfo = getColumnInfo(resultSet.getMetaData());
                Options options = getOptionsForWriter(false, title);
                WriteEngineExecutable oe = new WriteEngineExecutable(options, title, columnsInfo);
                WriteEngineHandler eh = oe.getWriteEngineHandler();
                do {
                    values.clear();
                    for (int i = 0; i < columnsInfo.size(); i++) {
                        values.add(getColumnData(resultSet, i + 1, columnsInfo.get(i).getJavaDataType(), columnsInfo.get(i).getDataType()));
                    }
                    eh.iterateRows(values);
                } while (resultSet.next());
                eh.disposeWriteEngineHandler();
                // oe.generateReport();
                try {
                    resultSet.close();
                    statement.close();
                } catch (Exception e) {
                }
                options.setProcessEndTime(new Date().getTime());
                reportGeneration.reportInfo(titleKey, timeDiff(options.getProcessEndTime() - options.getProcessStartTime()),
                        timeDiff(options.getWriteEndTime() - options.getWriteStartTime()), sourceCount, options.getRecordsProcessed(),
                        Boolean.toString(options.getRecordsProcessed() == sourceCount), "",
                        options.getProcessEndTime() - options.getProcessStartTime(), options.getWriteEndTime() - options.getWriteStartTime());
            }
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("While retrieving the data for query : " + titleKey);
            LOGGER.error("Exception :" + e.getMessage());
            reportGeneration.reportInfo(titleKey, timeDiff(new Date().getTime() - processStartTime), "NA",
                    sourceCount, 0, "ERROR", e.getMessage(), new Date().getTime() - processStartTime, 0);
        }


    }

    private void startTableExtractionProcess(String tableName, Connection connection) {
        long processStartTime = new Date().getTime();
        long sourceCount = 0;
        List<Object> values = new ArrayList<>();
        String title = schemaName + "-" + tableName;
        try {
            ResultSet resultSet;
            Statement statement;
            statement = connection.createStatement();
            System.out.println("Starting execution for table " + tableName + " : " + new Date());

            ResultSet resultSet1 = statement.executeQuery("select count(1) from  " + schemaName + "." + tableName);
            resultSet1.next();
            sourceCount = resultSet1.getLong(1);

            resultSet = statement.executeQuery("select * from  " + schemaName + "." + tableName);

            if (!resultSet.next()) {                            //if rs.next() returns false
                LOGGER.debug("No record found for this table " + (schemaName + "." + tableName));
                System.out.println("No records found" + (schemaName + "." + tableName));
                reportGeneration.reportInfo(schemaName + "." + tableName
                        , timeDiff(new Date().getTime() - processStartTime),
                        "000ms", sourceCount, 0, Boolean.toString(0 == sourceCount),
                        "Nothing to process", new Date().getTime() - processStartTime, 0);
            } else {
               /* List<ColumnInfo> columnsInfo = getColumnInfo(resultSet.getMetaData());
                Options options =
                        new Options().builder()
                                .FileSplitByRecords(inputBean.getSplitByRecord())
                                .fileSplitBySize(inputBean.getSplitBySize() * 1024 * 1024)
                                .thresholdSize(1 * 1024)
                                .inputEncodingCharset(StandardCharsets.UTF_8)
                                .outputEncodingCharset(StandardCharsets.UTF_8).splitDate(false)
                                .outputFormatValue(TextOutputFormat.valueOfFromString(inputBean.getOutputType())).build();
                options.setOutputFile(Paths.get(inputBean.getOutputPath() + File.separator + schemaName + "-" + tableName));
                options.setProcessStartTime(processStartTime);*/

                List<ColumnInfo> columnsInfo = getColumnInfo(resultSet.getMetaData());
                Options options = getOptionsForWriter(false, title);
                createNonSipHandler(options, title, columnsInfo);
                /*WriteEngineExecutable oe = new WriteEngineExecutable(options, title, columnsInfo);
                WriteEngineHandler eh = oe.getWriteEngineHandler();*/
                do {
                    values.clear();
                    for (int i = 0; i < columnsInfo.size(); i++) {
                        values.add(getColumnData(resultSet, i + 1, columnsInfo.get(i).getJavaDataType(), columnsInfo.get(i).getDataType()));
                    }
                    writeEngineHandler.iterateRows(values);
                } while (resultSet.next());
                writeEngineHandler.disposeWriteEngineHandler();
                writeEngineExecutable.generateReport();
                try {
                    resultSet.close();
                    statement.close();
                } catch (Exception e) {
                }
                options.setProcessEndTime(new Date().getTime());
                reportGeneration.reportInfo(schemaName + "." + tableName, timeDiff(options.getProcessEndTime() - options.getProcessStartTime()),
                        timeDiff(options.getWriteEndTime() - options.getWriteStartTime()), sourceCount, options.getRecordsProcessed(),
                        Boolean.toString(options.getRecordsProcessed() == sourceCount), "",
                        options.getProcessEndTime() - options.getProcessStartTime(), options.getWriteEndTime() - options.getWriteStartTime());
            }
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("While retrieving  the table  (" + schemaName + "." + tableName + ")");
            LOGGER.error("Exception :" + e.getMessage());
            reportGeneration.reportInfo(schemaName + "." + tableName, timeDiff(new Date().getTime() - processStartTime), "NA",
                    sourceCount, 0, "ERROR", e.getMessage(), new Date().getTime() - processStartTime, 0);
        }


    }



    /*private void reportInfo(String table, String timeDiff, String writeTime, long sourceCount, long destinationCount,
                            String match, String message, long processingTime, long writingTime) {
        if (message.isEmpty()) {
            totalprocessingtime += processingTime;
            totalWritingtime += writingTime;
            totalProcessingRecord += 1;
            totalWritingRecords += destinationCount;
        } else {
            totalprocessingtime += processingTime;
            totalProcessingRecord += 1;
        }

        System.out.println(table +
                "\n\tProcess Time : " + timeDiff +
                "\n\tWrite Time : " + writeTime +
                "\n\tSource Records Count : " + sourceCount +
                "\n\tTotal Records Processed : " + destinationCount +
                "\n\tMatch : " + match +
                "\n\tMessage : " + message);
    }

    private void printAverage() {
        System.out.println("Average" +
                "\n\tTotal Processing Time : " + totalprocessingtime +
                "\n\tTotal Writing Time : " + totalWritingtime +
                "\n\tTotal Processes executed : " + totalProcessingRecord +
                "\n\tTotal records written : " + totalWritingRecords +
                "\n\tAverage Processing time per process : " + timeDiff((long) (totalprocessingtime / (totalProcessingRecord == 0 ? 1 : totalProcessingRecord))) +
                "\n\tAverage Writing time per record : " + timeDiff((long) (totalWritingtime / (totalWritingRecords == 0 ? 1 : totalWritingRecords))));

    }*/
}
