package com.p3.archon.rdbms_extrator.extraction;

import com.p3.archon.rdbms_extrator.beans.InputArgs;
import com.p3.archon.rdbms_extrator.report.ReportGeneration;
import com.p3solutions.writer.beans.BinaryData;
import com.p3solutions.writer.beans.SipRecordData;
import com.p3solutions.writer.options.ColumnInfo;
import com.p3solutions.writer.options.Options;
import com.p3solutions.writer.writeOperation.SipWriteEngineExecutable;
import com.p3solutions.writer.writeOperation.SipWriteEngineHandler;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.InputStream;
import java.io.StringWriter;
import java.sql.*;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import static com.p3.archon.rdbms_extrator.utils.GeneralUtil.getXmlValidData;
import static com.p3.archon.rdbms_extrator.utils.GeneralUtil.timeDiff;
import static com.p3solutions.writer.utility.others.Utility.getTextFormatted;

/**
 * Created by Suriyanarayanan K
 * on 29/02/20 4:19 PM.
 */
public class SipExtraction extends ExtractorCore {

    int recordCount = 0;
    double totalprocessingtime;
    double totalWritingtime;
    double totalProcessingRecord;
    double totalWritingRecords;
    private Logger LOGGER = Logger.getLogger(this.getClass().getName());

    ReportGeneration reportGeneration = null;

    public SipExtraction(InputArgs inputBean, String schemaName, List<String> tableList, Map<String, String> queryMap) {
        super(schemaName, tableList, queryMap, inputBean);
        reportGeneration = new ReportGeneration();
    }

    public void startQuerySipExtraction(Connection connection) {
        try {
            customThreadPool.submit(
                    () -> queryMap
                            .entrySet().parallelStream()
                            .forEach(queryMapitem ->
                                    startQuerySipExtraction(queryMapitem.getKey(), queryMapitem.getValue(), connection))
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

    public void startSipExtraction(Connection connection) {
        try {
            customThreadPool.submit(
                    () -> tableList
                            .parallelStream()
                            .forEach(tableName ->
                                    startSipTableExtractionProcess(tableName, connection))
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

    public void startQuerySipExtraction(String titleKey, String query, Connection connection) {
        long processStartTime = new Date().getTime();
        long sourceCount = 0;
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

            if (!resultSet.next()) {                            //if rs.next() returns false
                LOGGER.debug("No record found for query with title " + titleKey);
                System.out.println("No records found for query with title " + titleKey);
                reportGeneration.reportInfo(titleKey
                        , timeDiff(new Date().getTime() - processStartTime),
                        "000ms", sourceCount, 0, Boolean.toString(0 == sourceCount),
                        "Nothing to process", new Date().getTime() - processStartTime, 0);
            } else {

                /*Options options =
                        new Options().builder()
                                .FileSplitByRecords(inputBean.getSplitByRecord())
                                .fileSplitBySize(inputBean.getSplitBySize() * 1024 * 1024)
                                .thresholdSize(1 * 1024)
                                .inputEncodingCharset(StandardCharsets.UTF_8)
                                .outputEncodingCharset(StandardCharsets.UTF_8)
                                .outputFormatValue(TextOutputFormat.valueOfFromString("sip")).build();

                options.setOutputFile(Paths.get(inputBean.getOutputPath() + File.separator + title));
                options.setSipConfigOptions(
                        SipConfigOptions.builder().application("iaapp").entity("ent").holding("hold").producer("arc")
                                .recordElement("row").rootElement("root").build()
                );
                options.getSipConfigOptions().generateNameSpaceFromHolding();*/
                Options options = getOptionsForWriter(true, title);
                SipWriteEngineExecutable oe = new SipWriteEngineExecutable(options, title);
                SipWriteEngineHandler seh = oe.getSipWriteEngineHandler();
                seh.createBatchAssembler();
                StringBuffer stringBuffer = new StringBuffer();

                options.setProcessStartTime(processStartTime);

                List<ColumnInfo> columnsInfo = getColumnInfo(resultSet.getMetaData());

                do {
                    for (int i = 1; i <= columnsInfo.size(); i++) {
                        String columnName = getTextFormatted(columnsInfo.get(i - 1).getColumn());
                        String columnValue = "";
                        Object columnData;
                        int javaSqlType = columnsInfo.get(i - 1).getJavaDataType();


                        if (javaSqlType == Types.CLOB) {
                            final Clob clob = resultSet.getClob(i);
                            if (resultSet.wasNull() || clob == null) {
                                columnValue = "";
                                stringBuffer.append(getSipColumnString(columnName, columnValue));
                            } else {
                                columnData = readClob(clob, null);
                                stringBuffer.append(writeValueClob(columnName, columnData));
                            }
                        } else if (javaSqlType == Types.NCLOB) {
                            final NClob nClob = resultSet.getNClob(i);
                            if (resultSet.wasNull() || nClob == null) {
                                columnValue = "";
                                stringBuffer.append(getSipColumnString(columnName, columnValue));
                            } else {
                                columnData = readClob(nClob, null);
                                stringBuffer.append(writeValueClob(columnName, columnData));
                            }
                        } else if (javaSqlType == Types.BLOB) {
                            final Blob blob = resultSet.getBlob(i);
                            if (resultSet.wasNull() || blob == null) {
                                columnValue = "";
                                stringBuffer.append(getSipColumnString(columnName, columnValue));
                            } else {
                                //columnData = readBlob(blob);
                                stringBuffer.append(getSipColumnString(columnName, "<binary>"));
                            }
                        } else if (javaSqlType == Types.LONGVARBINARY || javaSqlType == Types.VARBINARY) {
                            if (columnsInfo.get(i - 1).getDataType().equalsIgnoreCase("RAW")) {
                                try {
                                    final InputStream stream = resultSet.getBinaryStream(i);
                                    final Blob blob = resultSet.getBlob(i);
                                    if (resultSet.wasNull() || stream == null) {
                                        columnValue = "";
                                        stringBuffer.append(getSipColumnString(columnName, columnValue));
                                    } else {
                                        //columnData = readStream(stream, blob);
                                        stringBuffer.append(getSipColumnString(columnName, "<binary>"));
                                    }
                                } catch (Exception e) {
                                    final InputStream stream1 = resultSet.getAsciiStream(i);
                                    if (resultSet.wasNull() || stream1 == null) {
                                        columnValue = "";
                                        stringBuffer.append(getSipColumnString(columnName, columnValue));
                                    } else {
                                        columnData = readStream(stream1, null);
                                        stringBuffer.append(writeValueClob(columnName, columnData));
                                    }
                                }
                            } else {
                                final InputStream stream = resultSet.getBinaryStream(i);
                                final Blob blob = resultSet.getBlob(i);
                                if (resultSet.wasNull() || stream == null) {
                                    columnValue = "";
                                    stringBuffer.append(getSipColumnString(columnName, columnValue));
                                } else {
                                    columnData = readStream(stream, blob);
                                    stringBuffer.append(writeValueClob(columnName, columnData));
                                }
                            }
                        } else if (javaSqlType == Types.LONGNVARCHAR || javaSqlType == Types.LONGVARCHAR) {
                            final InputStream stream = resultSet.getAsciiStream(i);
                            if (resultSet.wasNull() || stream == null) {
                                columnValue = "";
                                stringBuffer.append(getSipColumnString(columnName, columnValue));
                            } else {
                                columnData = readStream(stream, null);
                                stringBuffer.append(writeValueClob(columnName, columnData));
                            }
                        } else if (javaSqlType == Types.DATE || columnsInfo.get(i - 1).getDataType().equalsIgnoreCase("DATE")) {
                            final Date datevalue = resultSet.getDate(i);
                            if (resultSet.wasNull() || datevalue == null) {
                                columnValue = "";
                            } else {
                                try {
                                    java.sql.Date ts = resultSet.getDate(i);
                                    Date date = new Date();
                                    date.setTime(ts.getTime());
                                    String formattedDate = new SimpleDateFormat("yyyy-MM-dd").format(date);
                                    columnValue = formattedDate;
                                } catch (Exception e) {
                                    columnValue = resultSet.getString(i);
                                }
                            }
                            stringBuffer.append(getSipColumnString(columnName, columnValue));
                        } else if (javaSqlType == Types.TIMESTAMP || javaSqlType == Types.TIMESTAMP_WITH_TIMEZONE
                                || columnsInfo.get(i - 1).getDataType().equalsIgnoreCase("TIMESTAMP WITH TIME ZONE")) {
                            final Timestamp timestampValue = resultSet.getTimestamp(i);
                            if (resultSet.wasNull() || timestampValue == null) {
                                columnValue = "";
                            } else {
                                try {
                                    // System.out.println("TIMESTAMP VALUE : " + rows.getString(i) + " / " +
                                    // rows.getTimestamp(i));
                                    Timestamp ts = resultSet.getTimestamp(i);
                                    Date date = new Date();
                                    date.setTime(ts.getTime());
                                    String formattedDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(date);
                                    columnValue = formattedDate;
                                } catch (Exception e) {
                                    // System.out.println("Timestamp excpetion");
                                    columnValue = resultSet.getTimestamp(i) + "";
                                }
                            }
                            stringBuffer.append(getSipColumnString(columnName, columnValue));
                        } else if (javaSqlType == Types.BIT) {
                            final boolean booleanValue = resultSet.getBoolean(i);
                            final String stringValue = resultSet.getString(i);
                            if (resultSet.wasNull()) {
                                columnValue = "";
                            } else {
                                columnValue = stringValue.equalsIgnoreCase(Boolean.toString(booleanValue)) ? String.valueOf(booleanValue) : stringValue;
                            }
                            stringBuffer.append(getSipColumnString(columnName, columnValue));
                        } else if (javaSqlType == Types.FLOAT
                                || javaSqlType == Types.REAL
                        ) {
                            final float floatValue = resultSet.getFloat(i);
                            if (resultSet.wasNull()) {
                                columnValue = "";
                            } else {
                                float value = floatValue;
                                if (value - (int) value > 0.0)
                                    formatter = new DecimalFormat(
                                            "#.##########################################################################################################################################################################################################################");
                                else
                                    formatter = new DecimalFormat("#");
                                columnValue = formatter.format(value);
                            }
                            stringBuffer.append(getSipColumnString(columnName, columnValue));
                        } else if (javaSqlType == Types.DOUBLE) {
                            final double doubleValue = resultSet.getDouble(i);
                            if (resultSet.wasNull()) {
                                columnValue = "";
                            } else {
                                double value = doubleValue;
                                if (value - (int) value > 0.0)
                                    formatter = new DecimalFormat(
                                            "#.##########################################################################################################################################################################################################################");
                                else
                                    formatter = new DecimalFormat("#");
                                columnValue = formatter.format(value);
                            }
                            stringBuffer.append(getSipColumnString(columnName, columnValue));
                        } else {
                            final Object objectValue = resultSet.getObject(i);
                            if (resultSet.wasNull() || objectValue == null)
                                columnValue = "";
                            else
                                columnValue = objectValue + "";
                            stringBuffer.append(getSipColumnString(columnName, columnValue));
                        }
                    }

                    seh.iterateRows(new SipRecordData(stringBuffer.toString(), new ArrayList<>()));
                    stringBuffer.delete(0, stringBuffer.length());
                } while (resultSet.next());

                seh.closeBatchAssembler();
                oe.generateReport();
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
            LOGGER.error("While retrieving the data for query : " + titleKey);
            LOGGER.error("Exception :" + e.getMessage());
            reportGeneration.reportInfo(titleKey, timeDiff(new Date().getTime() - processStartTime), "NA",
                    sourceCount, 0, "ERROR", e.getMessage(), new Date().getTime() - processStartTime, 0);
        }
    }

    private void startSipTableExtractionProcess(String tableName, Connection connection) {
        long processStartTime = new Date().getTime();
        long sourceCount = 0;
        String title = schemaName + "-" + getTextFormatted(tableName).toUpperCase();
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

                /*Options options =
                        new Options().builder()
                                .FileSplitByRecords(inputBean.getSplitByRecord())
                                .fileSplitBySize(inputBean.getSplitBySize() * 1024 * 1024)
                                .thresholdSize(1 * 1024)
                                .inputEncodingCharset(StandardCharsets.UTF_8)
                                .outputEncodingCharset(StandardCharsets.UTF_8)
                                .outputFormatValue(TextOutputFormat.valueOfFromString("sip")).build();

                options.setOutputFile(Paths.get(inputBean.getOutputPath() + File.separator + title));
                options.setSipConfigOptions(
                        SipConfigOptions.builder().application("iaapp").entity("ent").holding("hold").producer("arc")
                                .recordElement("row").rootElement("root").build()
                );
                options.getSipConfigOptions().generateNameSpaceFromHolding();*/
                Options options = getOptionsForWriter(true, title);

                createSipHandler(options, title);
                /*SipWriteEngineExecutable oe = new SipWriteEngineExecutable(options, title);
                SipWriteEngineHandler seh = oe.getSipWriteEngineHandler();
                seh.createBatchAssembler();*/
                StringBuffer stringBuffer = new StringBuffer();
                options.setProcessStartTime(processStartTime);
                List<ColumnInfo> columnsInfo = getColumnInfo(resultSet.getMetaData());

                do {
                    for (int i = 1; i <= columnsInfo.size(); i++) {
                        String columnName = getTextFormatted(columnsInfo.get(i - 1).getColumn());
                        String columnValue = "";
                        Object columnData;
                        int javaSqlType = columnsInfo.get(i - 1).getJavaDataType();


                        if (javaSqlType == Types.CLOB) {
                            final Clob clob = resultSet.getClob(i);
                            if (resultSet.wasNull() || clob == null) {
                                columnValue = "";
                                stringBuffer.append(getSipColumnString(columnName, columnValue));
                            } else {
                                columnData = readClob(clob, null);
                                stringBuffer.append(writeValueClob(columnName, columnData));
                            }
                        } else if (javaSqlType == Types.NCLOB) {
                            final NClob nClob = resultSet.getNClob(i);
                            if (resultSet.wasNull() || nClob == null) {
                                columnValue = "";
                                stringBuffer.append(getSipColumnString(columnName, columnValue));
                            } else {
                                columnData = readClob(nClob, null);
                                stringBuffer.append(writeValueClob(columnName, columnData));
                            }
                        } else if (javaSqlType == Types.BLOB) {
                            final Blob blob = resultSet.getBlob(i);
                            if (resultSet.wasNull() || blob == null) {
                                columnValue = "";
                                stringBuffer.append(getSipColumnString(columnName, columnValue));
                            } else {
                                //columnData = readBlob(blob);
                                stringBuffer.append(getSipColumnString(columnName, "<binary>"));
                            }
                        } else if (javaSqlType == Types.LONGVARBINARY || javaSqlType == Types.VARBINARY) {
                            if (columnsInfo.get(i - 1).getDataType().equalsIgnoreCase("RAW")) {
                                try {
                                    final InputStream stream = resultSet.getBinaryStream(i);
                                    final Blob blob = resultSet.getBlob(i);
                                    if (resultSet.wasNull() || stream == null) {
                                        columnValue = "";
                                        stringBuffer.append(getSipColumnString(columnName, columnValue));
                                    } else {
                                        //columnData = readStream(stream, blob);
                                        stringBuffer.append(getSipColumnString(columnName, "<binary>"));
                                    }
                                } catch (Exception e) {
                                    final InputStream stream1 = resultSet.getAsciiStream(i);
                                    if (resultSet.wasNull() || stream1 == null) {
                                        columnValue = "";
                                        stringBuffer.append(getSipColumnString(columnName, columnValue));
                                    } else {
                                        columnData = readStream(stream1, null);
                                        stringBuffer.append(writeValueClob(columnName, columnData));
                                    }
                                }
                            } else {
                                final InputStream stream = resultSet.getBinaryStream(i);
                                final Blob blob = resultSet.getBlob(i);
                                if (resultSet.wasNull() || stream == null) {
                                    columnValue = "";
                                    stringBuffer.append(getSipColumnString(columnName, columnValue));
                                } else {
                                    columnData = readStream(stream, blob);
                                    stringBuffer.append(writeValueClob(columnName, columnData));
                                }
                            }
                        } else if (javaSqlType == Types.LONGNVARCHAR || javaSqlType == Types.LONGVARCHAR) {
                            final InputStream stream = resultSet.getAsciiStream(i);
                            if (resultSet.wasNull() || stream == null) {
                                columnValue = "";
                                stringBuffer.append(getSipColumnString(columnName, columnValue));
                            } else {
                                columnData = readStream(stream, null);
                                stringBuffer.append(writeValueClob(columnName, columnData));
                            }
                        } else if (javaSqlType == Types.DATE || columnsInfo.get(i - 1).getDataType().equalsIgnoreCase("DATE")) {
                            final Date datevalue = resultSet.getDate(i);
                            if (resultSet.wasNull() || datevalue == null) {
                                columnValue = "";
                            } else {
                                try {
                                    java.sql.Date ts = resultSet.getDate(i);
                                    Date date = new Date();
                                    date.setTime(ts.getTime());
                                    String formattedDate = new SimpleDateFormat("yyyy-MM-dd").format(date);
                                    columnValue = formattedDate;
                                } catch (Exception e) {
                                    columnValue = resultSet.getString(i);
                                }
                            }
                            stringBuffer.append(getSipColumnString(columnName, columnValue));
                        } else if (javaSqlType == Types.TIMESTAMP || javaSqlType == Types.TIMESTAMP_WITH_TIMEZONE
                                || columnsInfo.get(i - 1).getDataType().equalsIgnoreCase("TIMESTAMP WITH TIME ZONE")) {
                            final Timestamp timestampValue = resultSet.getTimestamp(i);
                            if (resultSet.wasNull() || timestampValue == null) {
                                columnValue = "";
                            } else {
                                try {
                                    // System.out.println("TIMESTAMP VALUE : " + rows.getString(i) + " / " +
                                    // rows.getTimestamp(i));
                                    Timestamp ts = resultSet.getTimestamp(i);
                                    Date date = new Date();
                                    date.setTime(ts.getTime());
                                    String formattedDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(date);
                                    columnValue = formattedDate;
                                } catch (Exception e) {
                                    // System.out.println("Timestamp excpetion");
                                    columnValue = resultSet.getTimestamp(i) + "";
                                }
                            }
                            stringBuffer.append(getSipColumnString(columnName, columnValue));
                        } else if (javaSqlType == Types.BIT) {
                            final boolean booleanValue = resultSet.getBoolean(i);
                            final String stringValue = resultSet.getString(i);
                            if (resultSet.wasNull()) {
                                columnValue = "";
                            } else {
                                columnValue = stringValue.equalsIgnoreCase(Boolean.toString(booleanValue)) ? String.valueOf(booleanValue) : stringValue;
                            }
                            stringBuffer.append(getSipColumnString(columnName, columnValue));
                        } else if (javaSqlType == Types.FLOAT
                                || javaSqlType == Types.REAL
                        ) {
                            final float floatValue = resultSet.getFloat(i);
                            if (resultSet.wasNull()) {
                                columnValue = "";
                            } else {
                                float value = floatValue;
                                if (value - (int) value > 0.0)
                                    formatter = new DecimalFormat(
                                            "#.##########################################################################################################################################################################################################################");
                                else
                                    formatter = new DecimalFormat("#");
                                columnValue = formatter.format(value);
                            }
                            stringBuffer.append(getSipColumnString(columnName, columnValue));
                        } else if (javaSqlType == Types.DOUBLE) {
                            final double doubleValue = resultSet.getDouble(i);
                            if (resultSet.wasNull()) {
                                columnValue = "";
                            } else {
                                double value = doubleValue;
                                if (value - (int) value > 0.0)
                                    formatter = new DecimalFormat(
                                            "#.##########################################################################################################################################################################################################################");
                                else
                                    formatter = new DecimalFormat("#");
                                columnValue = formatter.format(value);
                            }
                            stringBuffer.append(getSipColumnString(columnName, columnValue));
                        } else {
                            final Object objectValue = resultSet.getObject(i);
                            if (resultSet.wasNull() || objectValue == null)
                                columnValue = "";
                            else
                                columnValue = objectValue + "";
                            stringBuffer.append(getSipColumnString(columnName, columnValue));
                        }
                    }

                    sipWriteEngineHandler.iterateRows(new SipRecordData(stringBuffer.toString(), new ArrayList<>()));
                    stringBuffer.delete(0, stringBuffer.length());
                } while (resultSet.next());

                sipWriteEngineHandler.closeBatchAssembler();
                sipWriteEngineExecutable.generateReport();
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
            LOGGER.error("While retrieving  the table  (" + schemaName + "." + tableName + ")");
            LOGGER.error("Exception :" + e.getMessage());
            reportGeneration.reportInfo(schemaName + "." + tableName, timeDiff(new Date().getTime() - processStartTime), "NA",
                    sourceCount, 0, "ERROR", e.getMessage(), new Date().getTime() - processStartTime, 0);
        }
    }

    private String getSipColumnString(String columnName, String columnValue) {
        return "\n<" + columnName.trim() + ">" + getXmlValidData(columnValue) + "</" + columnName.trim() + ">";
    }

    public String writeValueClob(String columnName, Object columnData) {
        return "\n<" + columnName + ">"
                + ((columnData == null) ? "" : getCdataString(columnData)) + "</" + columnName + ">";
    }

    public String getCdataString(Object columnData) {
        String data = "<![CDATA[]]>";
        try {
            if (columnData == null)
                data = "<![CDATA[]]>";
            else if (columnData instanceof BinaryData)
                columnData = ((BinaryData) columnData).toString();

            if (((String) columnData).equals(""))
                data = "<![CDATA[]]>";
            else {
                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                DocumentBuilder builder = factory.newDocumentBuilder();
                Document document = builder.newDocument();
                Element rootElement = (Element) document.createElement("_");
                document.appendChild(rootElement);
                rootElement.appendChild(document.createCDATASection((String) columnData));
                String xmlString = getXMLFromDocument(document);
                data = xmlString.substring(0, xmlString.length() - 4).substring(3);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }

    public String getXMLFromDocument(Document document) throws TransformerException {
        Transformer transformer = TransformerFactory.newInstance().newTransformer();
        transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
        StreamResult streamResult = new StreamResult(new StringWriter());
        DOMSource source = new DOMSource(document);
        transformer.transform(source, streamResult);
        return streamResult.getWriter().toString();
    }


    /*private void reportInfo(String table, String timeDiff, String writeTime, long sourceCount,
                            long destinationCount,
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
