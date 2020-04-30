package com.p3.archon.rdbms_extrator.extraction;

import com.p3.archon.rdbms_extrator.beans.InputArgs;
import com.p3.archon.rdbms_extrator.core.ConnectionChecker;
import com.p3solutions.writer.beans.BinaryData;
import com.p3solutions.writer.options.*;
import com.p3solutions.writer.writeOperation.SipWriteEngineExecutable;
import com.p3solutions.writer.writeOperation.SipWriteEngineHandler;
import com.p3solutions.writer.writeOperation.WriteEngineExecutable;
import com.p3solutions.writer.writeOperation.WriteEngineHandler;
import org.apache.log4j.Logger;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.sql.*;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ForkJoinPool;

import static com.p3solutions.writer.utility.others.Utility.readFully;

/**
 * Created by Suriyanarayanan K
 * on 29/02/20 4:24 PM.
 */
public class ExtractorCore {

    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
    private final SimpleDateFormat dateOnlyFormat = new SimpleDateFormat("yyyy-MM-dd");
    DatabaseMetaData databaseMetaData;

    //inputs
    String schemaName;
    List<String> tableList;
    Map<String, String> queryMap;
    InputArgs inputBean;
    ConnectionChecker connectionChecker = new ConnectionChecker();
    DecimalFormat formatter;

    //threads
    int THREAD_CAPACITY;
    ForkJoinPool customThreadPool;

    //non sip
    WriteEngineExecutable writeEngineExecutable = null;
    WriteEngineHandler writeEngineHandler = null;
    //sip
    SipWriteEngineExecutable sipWriteEngineExecutable = null;
    SipWriteEngineHandler sipWriteEngineHandler = null;
    private Logger LOGGER = Logger.getLogger(this.getClass().getName());

    public ExtractorCore(String schemaName, List<String> tableList, Map<String, String> queryMap, InputArgs inputBean) {
        this.THREAD_CAPACITY = inputBean.getThreadCount();
        this.schemaName = schemaName;
        this.tableList = tableList;
        this.queryMap = queryMap;
        this.inputBean = inputBean;
        customThreadPool = new ForkJoinPool(THREAD_CAPACITY);
    }

    public Object getColumnData(ResultSet rows, int i, int javaSqlType, String resultsColumnsTypeName) throws SQLException {

        Object columnData;
        switch (resultsColumnsTypeName) {
            case "DATE":
                javaSqlType = Types.DATE;
                break;
            case "TIMESTAMP WITH TIME ZONE":
                javaSqlType = Types.TIMESTAMP_WITH_TIMEZONE;
        }
        switch (javaSqlType) {
            case Types.CLOB:
                final Clob clob = rows.getClob(i);
                if (rows.wasNull() || clob == null) {
                    columnData = null;
                } else {
                    columnData = readClob(clob, null);
                }
                break;
            case Types.NCLOB:
                final NClob nClob = rows.getNClob(i);
                if (rows.wasNull() || nClob == null) {
                    columnData = null;
                } else {
                    columnData = readClob(nClob, null);
                }
                break;
            case Types.BLOB:
                final Blob blob = rows.getBlob(i);
                if (rows.wasNull() || blob == null) {
                    columnData = null;
                } else {
                    columnData = readBlob(blob);
                }
                break;
            case Types.LONGVARBINARY:
            case Types.VARBINARY:
                if (resultsColumnsTypeName.equalsIgnoreCase("RAW")) {
                    try {
                        final InputStream stream = rows.getBinaryStream(i);
                        final Blob blobs = rows.getBlob(i);
                        if (rows.wasNull() || stream == null) {
                            columnData = null;
                        } else {
                            columnData = readStream(stream, blobs);
                        }
                    } catch (Exception e) {
                        final InputStream stream = rows.getAsciiStream(i);
                        if (rows.wasNull() || stream == null) {
                            columnData = null;
                        } else {
                            columnData = readStream(stream, null);
                        }
                    }

                } else {
                    final InputStream stream = rows.getBinaryStream(i);
                    final Blob blob1 = rows.getBlob(i);
                    if (rows.wasNull() || stream == null) {
                        columnData = null;
                    } else {
                        columnData = readStream(stream, blob1);
                    }

                }
                break;
            case Types.LONGNVARCHAR:
            case Types.LONGVARCHAR:
                final InputStream stream = rows.getAsciiStream(i);
                if (rows.wasNull() || stream == null) {
                    columnData = null;
                } else {
                    columnData = readStream(stream, null);
                }
                break;

            case Types.DATE:
                final java.util.Date datevalue = rows.getDate(i);
                if (rows.wasNull() || datevalue == null) {
                    columnData = null;
                } else {
                    try {
                        java.sql.Date ts = rows.getDate(i);
                        java.util.Date date = new java.util.Date();
                        date.setTime(ts.getTime());
                        String formattedDate = dateOnlyFormat.format(date);
                        columnData = formattedDate;
                    } catch (Exception e) {
                        columnData = rows.getString(i);
                    }
                }
                break;
            case Types.TIMESTAMP:
            case Types.TIMESTAMP_WITH_TIMEZONE:
                final Timestamp timestampValue = rows.getTimestamp(i);
                if (rows.wasNull() || timestampValue == null) {
                    columnData = null;
                } else {
                    try {
                        Timestamp ts = rows.getTimestamp(i);
                        java.util.Date date = new java.util.Date();
                        date.setTime(ts.getTime());
                        String formattedDate = dateFormat.format(date);
                        columnData = formattedDate;
                    } catch (Exception e) {
                        columnData = rows.getTimestamp(i);
                    }
                }
                break;
            case Types.BIT:
                final boolean booleanValue = rows.getBoolean(i);
                final String stringValue = rows.getString(i);
                if (rows.wasNull()) {
                    columnData = null;
                } else {
                    columnData = stringValue.equalsIgnoreCase(Boolean.toString(booleanValue)) ? booleanValue : stringValue;
                }
                break;

            case Types.FLOAT:
            case Types.REAL:
                final float floatValue = rows.getFloat(i);
                if (rows.wasNull()) {
                    columnData = null;
                } else {
                    float value = floatValue;
                    if (value - (int) value > 0.0)
                        formatter = new DecimalFormat(
                                "#.##########################################################################################################################################################################################################################");
                    else
                        formatter = new DecimalFormat("#");
                    columnData = formatter.format(value);
                }
                break;
            case Types.DOUBLE:
                final double doubleValue = rows.getDouble(i);
                if (rows.wasNull()) {
                    columnData = null;
                } else {
                    double value = doubleValue;
                    if (value - (int) value > 0.0)
                        formatter = new DecimalFormat(
                                "#.##########################################################################################################################################################################################################################");
                    else
                        formatter = new DecimalFormat("#");
                    columnData = formatter.format(value);
                }
                break;
            default:
                final Object objectValue = rows.getObject(i);
                if (rows.wasNull() || objectValue == null)
                    columnData = null;
                else
                    columnData = objectValue;
                break;
        }
        return columnData;
    }

    public BinaryData readBlob(final Blob blob) {
        if (blob == null) {
            return null;
        } else {
            InputStream in = null;
            BinaryData lobData;
            try {
                try {
                    in = blob.getBinaryStream();
                } catch (final SQLFeatureNotSupportedException e) {
                    in = null;
                }

                if (in != null) {
                    lobData = new BinaryData(readFully(in), blob);
                } else {
                    lobData = new BinaryData();
                }
            } catch (final SQLException e) {
                lobData = new BinaryData();
            }
            return lobData;
        }
    }

    public BinaryData readClob(final Clob clob, final Blob blob) {
        if (clob == null) {
            return null;
        } else {
            Reader rdr = null;
            BinaryData lobData;
            try {
                try {
                    rdr = clob.getCharacterStream();
                } catch (final SQLFeatureNotSupportedException e) {
                    rdr = null;
                }
                if (rdr == null) {
                    try {
                        rdr = new InputStreamReader(clob.getAsciiStream());
                    } catch (final SQLFeatureNotSupportedException e) {
                        rdr = null;
                    }
                }

                if (rdr != null) {
                    String lobDataString = readFully(rdr);
                    if (lobDataString.isEmpty()) {
                        // Attempt yet another read
                        final long clobLength = clob.length();
                        lobDataString = clob.getSubString(1, (int) clobLength);
                    }
                    lobData = new BinaryData(lobDataString, blob);
                } else {
                    lobData = new BinaryData();
                }
            } catch (final SQLException e) {
                lobData = new BinaryData();
            }
            return lobData;
        }
    }

    public BinaryData readStream(final InputStream stream, final Blob blob) {
        if (stream == null) {
            return null;
        } else {
            final BufferedInputStream in = new BufferedInputStream(stream);
            final BinaryData lobData = new BinaryData(readFully(in), blob);
            return lobData;
        }
    }

    protected void createNonSipHandler(Options options, String title, List<ColumnInfo> columnsInfo) {

        try {
            writeEngineExecutable = new WriteEngineExecutable(options, title, columnsInfo);
            writeEngineHandler = writeEngineExecutable.getWriteEngineHandler();
        } catch (Exception e) {
            LOGGER.error("While creating title :" + title);
            LOGGER.error("Error :" + e.getStackTrace());
        }
    }

    protected void createSipHandler(Options options, String title) {
        try {
            sipWriteEngineExecutable = new SipWriteEngineExecutable(options, title);
            sipWriteEngineHandler = sipWriteEngineExecutable.getSipWriteEngineHandler();
            sipWriteEngineHandler.createBatchAssembler();
        } catch (Exception e) {
            LOGGER.error("While creating title :" + title);
            LOGGER.error("Error :" + e.getStackTrace());
        }
    }

    public Options getOptionsForWriter(boolean isSip, String title) {
        Options options = null;
        options = new Options().builder()
                .FileSplitByRecords(inputBean.getSplitByRecord())
                .fileSplitBySize(inputBean.getSplitBySize() * 1024 * 1024)
                .thresholdSize(1 * 1024)
                .inputEncodingCharset(StandardCharsets.UTF_8)
                .outputEncodingCharset(StandardCharsets.UTF_8)
                .build();

        if (isSip) {
            options.setOutputFormatValue(TextOutputFormat.valueOfFromString("sip"));
            options.setOutputFile(Paths.get(inputBean.getOutputPath() + File.separator + title));
            options.setSipConfigOptions(
                    SipConfigOptions.builder().application("iaapp").entity("ent").holding("hold").producer("arc")
                            .recordElement("row").rootElement("root").build()
            );
            options.getSipConfigOptions().generateNameSpaceFromHolding();
        } else {
            options.setSplitDate(false);
            options.setOutputFormatValue(TextOutputFormat.valueOfFromString(inputBean.getOutputType()));
            options.setOutputFile(Paths.get(inputBean.getOutputPath() + File.separator + title));
            options.setProcessStartTime(new java.util.Date().getTime());
        }
        return options;
    }

    public List<ColumnInfo> getColumnInfo(ResultSetMetaData tableColumns) {
        List<ColumnInfo> columnInfo = new ArrayList<>();
        try {
            for (int i = 1; i <= tableColumns.getColumnCount(); i++) {
                ColumnInfo column = new ColumnInfo();
               // column.setColumn(tableColumns.getColumnName(i));
                column.setColumn( tableColumns.getColumnLabel(i));
                column.setCategory(getTypeCategory(tableColumns.getColumnTypeName(i)));
                column.setDataType(tableColumns.getColumnTypeName(i));
                column.setJavaDataType(tableColumns.getColumnType(i));
                columnInfo.add(column);
            }
        } catch (SQLException e) {
            LOGGER.error("Exception :" + e.getMessage());
        }
        return columnInfo;
    }

    public ArchonDataType getTypeCategory(String type) {
        switch (type) {
            case "DATE":
                return ArchonDataType.DATE;
            case "TIME":
                return ArchonDataType.TIME;
            case "DATETIME":
            case "DATETIME2":
            case "TIMESTAMP":
            case "DATE TIME":
                return ArchonDataType.DATETIME;
            case "BLOB":
            case "LONGBLOB":
            case "SHORTBLOB":
                return ArchonDataType.BLOB;
            case "CLOB":
                return ArchonDataType.CLOB;
            case "BOOLEAN":
            case "BIT":
                return ArchonDataType.BOOLEAN;
            case "INT":
            case "INTEGER":
            case "LONG":
            case "AUTONUMBER":
            case "SMALLINT":
            case "BIGINT":
            case "NUMERIC":
            case "REAL":
            case "TINYINT":
                return ArchonDataType.NUMBER;
            case "MONEY":
            case "DEC":
            case "DECIMAL":
            case "FLOAT":
            case "DOUBLE":
            case "SMALLMONEY":
                return ArchonDataType.DECIMAL;
            case "TEXT":
            case "NTEXT":
            case "CHAR":
            case "NCHAR":
            case "VARCHAR":
            case "NVARCHAR":
            case "NVARBINARY":
            case "VARBINARY":
            case "PHOTO":
            case "LONGVARBINARY":
            case "LONGNVARBINARY":
            case "VARCHAR2":
            case "RAW":
            case "LONG RAW":
            case "CHARACTER":
            case "CHARACTER VARYING":
            case "BINARY VARYING":
            case "INTERVAL":
            case "TIMESTAMP WITH LOCAL TIME ZONE":
            case "TIMESTAMP WITH TIME ZONE":
            case "SMALLDATETIME":
            case "DATETIMEOFFSET":
            default:
                return ArchonDataType.STRING;
        }
    }
}
