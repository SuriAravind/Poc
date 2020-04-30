package test;

import test.beans.BinaryData;
import test.beans.SipRecordData;
import test.options.*;
import test.writeOperation.SipWriteEngineExecutable;
import test.writeOperation.SipWriteEngineHandler;
import test.writeOperation.WriteEngineExecutable;
import test.writeOperation.WriteEngineHandler;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.*;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static test.utility.others.Utility.readFully;

public class testProgram {

    private static final String SIP = "sip";

    public static void main(String[] args) throws Exception {
        System.out.println("Start - " + new Date());
        String outputType = "sip";
        String outputDir = "/Users/apple/Downloads/" + outputType + "/" + new Date().getTime();
        new File(outputDir).mkdirs();
        testProgram t = new testProgram();
        String title = "SCH-TAB_VALUE";

        if (outputType.equalsIgnoreCase(SIP))
            t.SipExample(outputDir, outputType, title);
            //t.SipHelperMysql(outputDir, outputType, title);
        else
            t.nonSipHelperMysql(outputDir, outputType, title);

        System.out.println("End - " + new Date());
    }

    private void SipExample(String outputDir, String outputType, String title) throws Exception {
        Options options =
                new Options().builder().FileSplitByRecords(100).fileSplitBySize(2 * 1024 * 1024).thresholdSize(1 * 1024)
                        .inputEncodingCharset(StandardCharsets.UTF_8)
                        .outputEncodingCharset(StandardCharsets.UTF_8)
                        .outputFormatValue(TextOutputFormat.valueOfFromString(outputType)).build();

        options.setOutputFile(Paths.get(outputDir + File.separator + title));

        SipConfigOptions.builder().application("iaapp").entity("ent").holding("hold").producer("arc")
                .recordElement("row").rootElement("root").build();

        // setting valid schema from holding
        options.getSipConfigOptions().generateNameSpaceFromHolding();

        String details = "\t<field>one</field>\n\t<field2>two</field2>\n\t<field3>\n\t\t<subthree>three</subthree>\n\t</field3>";

        SipWriteEngineExecutable oe = new SipWriteEngineExecutable(options, title);
        SipWriteEngineHandler seh = oe.getSipWriteEngineHandler();
        seh.createBatchAssembler();
        int i = 0;
        while (i < 10) {
            List<String> a = new ArrayList<>();
            for (int j = 0; (j + i) % 3 != 0; j++) {
                String f = outputDir + File.separator + "test_" + i + "_" + j + ".txt";
                Path p = Paths.get(f);
                BufferedWriter bw = new BufferedWriter(new FileWriter(p.toFile()));
                bw.write("test");
                bw.flush();
                bw.close();
                a.add(f);
            }
            seh.iterateRows(new SipRecordData(details, a));
            i++;
        }
        seh.closeBatchAssembler();
        oe.generateReport();
    }


    private void SipHelperMysql(String outputDir, String outputType, String title) throws Exception {
        Options options =
                new Options().builder().FileSplitByRecords(100).fileSplitBySize(2 * 1024 * 1024).thresholdSize(1 * 1024)
                        .inputEncodingCharset(StandardCharsets.UTF_8)
                        .outputEncodingCharset(StandardCharsets.UTF_8)
                        .outputFormatValue(TextOutputFormat.valueOfFromString(outputType)).build();

        options.setOutputFile(Paths.get(outputDir + File.separator + title));

        SipConfigOptions.builder().application("iaapp").entity("ent").holding("hold").producer("arc")
                .recordElement("row").rootElement("root").build();

        // setting valid schema from holding
        options.getSipConfigOptions().generateNameSpaceFromHolding();

        Connection con = null;
        Statement stmt = null;
        ResultSet rs = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/sakila", "root", "root");
            stmt = con.createStatement();
            rs = stmt.executeQuery("select * from actor");
        } catch (Exception e) {
            System.out.println(e);
        }


        if (rs == null) {
            System.exit(1);
        }
        List<ColumnInfo> columnsInfo = getColInfo(rs);
        WriteEngineExecutable oe = new WriteEngineExecutable(options, title, columnsInfo);
        WriteEngineHandler eh = oe.getWriteEngineHandler();
        while (rs.next()) {
            List<Object> values = new ArrayList<>();
            for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
                values.add(getColumnData(rs, i, rs.getMetaData().getColumnType(i), rs.getMetaData().getColumnTypeName(i)));
            }
            eh.iterateRows(values);
        }
        eh.disposeWriteEngineHandler();
        oe.generateReport();
        // yet to code

        try {
            rs.close();
            stmt.close();
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void nonSipExample(String outputDir, String outputType, String title) throws Exception {


        Options options =
                new Options().builder().FileSplitByRecords(100).fileSplitBySize(2 * 1024 * 1024).thresholdSize(1 * 1024)
                        .inputEncodingCharset(StandardCharsets.UTF_8)
                        .outputEncodingCharset(StandardCharsets.UTF_8).splitDate(true)
                        .outputFormatValue(TextOutputFormat.valueOfFromString(outputType)).build();

        options.setOutputFile(Paths.get(outputDir + File.separator + title));
        // Columns
        List<ColumnInfo> columnsInfo = new ArrayList<>();
        columnsInfo.add(new ColumnInfo("SF", "STRING", JavaDataType.NORMAL));
        columnsInfo.add(new ColumnInfo("LF", "STRING", JavaDataType.NORMAL));
        columnsInfo.add(new ColumnInfo("NF", "NUMBER", JavaDataType.NORMAL));
        columnsInfo.add(new ColumnInfo("CF", "DOUBLE", JavaDataType.NORMAL));
        columnsInfo.add(new ColumnInfo("DF", "DATE", JavaDataType.DATE));
        columnsInfo.add(new ColumnInfo("TF", "TIME", JavaDataType.TIME));
        columnsInfo.add(new ColumnInfo("DTF", "DATETIME", JavaDataType.DATETIME));
        columnsInfo.add(new ColumnInfo("CLF", "CLOB", JavaDataType.CLOB));
        columnsInfo.add(new ColumnInfo("BLF", "BLOB", JavaDataType.BLOB));
        columnsInfo.add(new ColumnInfo("CLF1", "CLOB", JavaDataType.CLOB));
        columnsInfo.add(new ColumnInfo("BLF1", "BLOB", JavaDataType.BLOB));

        // Data Values
        List<Object> values = new ArrayList<>();
        values.add("HI");
        values.add("This is a  multi line comment\n spanning across 2 lines");
        values.add(123);
        values.add(12.004);
        values.add("2010-01-01");
        values.add("01:01:01");
        values.add("2010-01-01 01:01:01");
        values.add("CLOB & VARCHARMAX: This is a  multi line comment\n spanning across 2 lines");
        values.add(new BinaryData("asasa", null));
        values.add("CLOB & VARCHARMAX: This is a  multi line comment\n spanning across 2 lines");
        values.add(new BinaryData("asasa", null));

        List<Object> values1 = new ArrayList<>();
        values1.add("1HI");
        values1.add("1This is a  multi line comment\n spanning across 2 lines");
        values1.add(1123);
        values1.add(112.004);
        values1.add("2010-01-01");
        values1.add("01:01:01");
        values1.add("2010-01-01 01:01:01");
        values1.add("1CLOB & <VARCHARMAX>: This is a  multi line comment\n spanning across 2 lines");
        values1.add(new BinaryData("asasa", null));
        values1.add("CLOB & VARCHARMAX: This is a  multi line comment\n spanning across 2 lines");
        values1.add(new BinaryData("asasa", null));


        WriteEngineExecutable oe = new WriteEngineExecutable(options, title, columnsInfo);
        WriteEngineHandler eh = oe.getWriteEngineHandler();
        int i = 0;
        while (i < 200) {
            eh.iterateRows(values);
            eh.iterateRows(values1);
            i++;
        }
        eh.disposeWriteEngineHandler();
        oe.generateReport();
    }


    private void nonSipHelperMysql(String outputDir, String outputType, String title) throws Exception {

        Options options =
                new Options().builder().FileSplitByRecords(100).fileSplitBySize(2 * 1024 * 1024).thresholdSize(1 * 1024)
                        .inputEncodingCharset(StandardCharsets.UTF_8)
                        .outputEncodingCharset(StandardCharsets.UTF_8).splitDate(true)
                        .outputFormatValue(TextOutputFormat.valueOfFromString(outputType)).build();

        options.setOutputFile(Paths.get(outputDir + File.separator + title));

        Connection con = null;
        Statement stmt = null;
        ResultSet rs = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/ArchonDb", "root", "imaninshu1@");
            stmt = con.createStatement();
            rs = stmt.executeQuery("select * from JOB_DETAILS");
        } catch (Exception e) {
            System.out.println(e);
        }


        if (rs == null) {
            System.exit(1);
        }

        List<ColumnInfo> columnsInfo = getColInfo(rs);
        WriteEngineExecutable oe = new WriteEngineExecutable(options, title, columnsInfo);
        WriteEngineHandler eh = oe.getWriteEngineHandler();
        while (rs.next()) {
            List<Object> values = new ArrayList<>();
            for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
                values.add(getColumnData(rs, i, rs.getMetaData().getColumnType(i), rs.getMetaData().getColumnTypeName(i)));
            }
            eh.iterateRows(values);
        }
        eh.disposeWriteEngineHandler();
        oe.generateReport();

        try {
            rs.close();
            stmt.close();
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private List<ColumnInfo> getColInfo(ResultSet rs) throws SQLException {
        List<ColumnInfo> columnInfo = new ArrayList<ColumnInfo>();
        for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
            ColumnInfo column = new ColumnInfo();
            column.setColumn(rs.getMetaData().getColumnName(i));
            column.setCategory(getTypeCategory(rs.getMetaData().getColumnTypeName(i)));
            column.setDataType(rs.getMetaData().getColumnTypeName(i).toUpperCase());
            columnInfo.add(column);
        }
        System.out.println(columnInfo);
        return columnInfo;
    }


    public JavaDataType getTypeCategory(String type) {
        System.out.println(type);
        switch (type) {
            case "DATE":
                return JavaDataType.DATE;
            case "TIME":
                return JavaDataType.TIME;
            case "DATETIME":
            case "DATETIME2":
            case "TIMESTAMP":
            case "DATE TIME":
                return JavaDataType.DATETIME;
            case "BLOB":
            case "LONGBLOB":
            case "SHORTBLOB":
                return JavaDataType.BLOB;
            case "CLOB":
                return JavaDataType.CLOB;
            case "BOOLEAN":
            case "BIT":
                return JavaDataType.BOOLEAN;
            case "INT":
            case "INTEGER":
            case "LONG":
            case "AUTONUMBER":
            case "SMALLINT":
            case "BIGINT":
            case "NUMERIC":
            case "REAL":
            case "TINYINT":
                return JavaDataType.INTEGER;
            case "MONEY":
            case "DEC":
            case "DECIMAL":
            case "FLOAT":
            case "DOUBLE":
            case "SMALLMONEY":
                return JavaDataType.DECIMAL;
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
                return JavaDataType.NORMAL;
        }

    }


    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
    private final SimpleDateFormat dateOnlyFormat = new SimpleDateFormat("yyyy-MM-dd");

    DecimalFormat formatter;

    private Object getColumnData(ResultSet rows, int i, int javaSqlType, String resultsColumnsTypeName) throws SQLException {
        // System.out.println(i + ". " + resultsColumns.get(i).getName() + "-" +
        // javaSqlType);
        // System.out.println(javaSqlType + " --- " +
        // resultsColumns.get(i).getType().getName().toUpperCase() + " --- "
        // + ((ResultsColumn) this.resultsColumns.get(i)).getColumnDataType().getName()
        // + " --- "
        // + (this.rows.getObject(i) != null ? this.rows.getObject(i).toString()
        // : "null"));

        // if (this.rows.getObject(i) != null)
        // this.rows.getObject(i).toString();
        Object columnData;
        if (javaSqlType == Types.CLOB) {
            final Clob clob = rows.getClob(i);
            if (rows.wasNull() || clob == null) {
                columnData = null;
            } else {
                columnData = readClob(clob, null);
            }
        } else if (javaSqlType == Types.NCLOB) {
            final NClob nClob = rows.getNClob(i);
            if (rows.wasNull() || nClob == null) {
                columnData = null;
            } else {
                columnData = readClob(nClob, null);
            }
        } else if (javaSqlType == Types.BLOB) {
            final Blob blob = rows.getBlob(i);
            if (rows.wasNull() || blob == null) {
                columnData = null;
            } else {
                columnData = readBlob(blob);
            }
        } else if (javaSqlType == Types.LONGVARBINARY || javaSqlType == Types.VARBINARY) {

            if (resultsColumnsTypeName.equalsIgnoreCase("RAW")) {
                try {
                    final InputStream stream = rows.getBinaryStream(i);
                    final Blob blob = rows.getBlob(i);
                    if (rows.wasNull() || stream == null) {
                        columnData = null;
                    } else {
                        columnData = readStream(stream, blob);
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
                final Blob blob = rows.getBlob(i);
                if (rows.wasNull() || stream == null) {
                    columnData = null;
                } else {
                    columnData = readStream(stream, blob);
                }

            }
        } else if (javaSqlType == Types.LONGNVARCHAR || javaSqlType == Types.LONGVARCHAR) {
            final InputStream stream = rows.getAsciiStream(i);
            if (rows.wasNull() || stream == null) {
                columnData = null;
            } else {
                columnData = readStream(stream, null);
            }
        } else if (javaSqlType == Types.DATE || resultsColumnsTypeName.equalsIgnoreCase("DATE")) {
            final Date datevalue = rows.getDate(i);
            if (rows.wasNull() || datevalue == null) {
                columnData = null;
            } else {
                try {
                    // System.out.println("DATE VALUE : " + rows.getString(i));
                    java.sql.Date ts = rows.getDate(i);
                    Date date = new Date();
                    date.setTime(ts.getTime());
                    String formattedDate = dateOnlyFormat.format(date);
                    columnData = formattedDate;
                } catch (Exception e) {
                    // System.out.println("Date excpetion");
                    columnData = rows.getString(i);
                }
            }
        } else if (javaSqlType == Types.TIMESTAMP || javaSqlType == Types.TIMESTAMP_WITH_TIMEZONE
                || resultsColumnsTypeName.equalsIgnoreCase("TIMESTAMP WITH TIME ZONE")) {
            final Timestamp timestampValue = rows.getTimestamp(i);
            if (rows.wasNull() || timestampValue == null) {
                columnData = null;
            } else {
                try {
                    // System.out.println("TIMESTAMP VALUE : " + rows.getString(i) + " / " +
                    // rows.getTimestamp(i));
                    Timestamp ts = rows.getTimestamp(i);
                    Date date = new Date();
                    date.setTime(ts.getTime());
                    String formattedDate = dateFormat.format(date);
                    columnData = formattedDate;
                } catch (Exception e) {
                    // System.out.println("Timestamp excpetion");
                    columnData = rows.getTimestamp(i);
                }
            }
            // } else if (javaSqlType == Types.BIGINT) {
            // final long bigIntValue = rows.getLong(i);
            // if (rows.wasNull()) {
            // columnData = null;
            // } else {
            // columnData = bigIntValue;
            // }
            // } else if (javaSqlType == Types.TINYINT) {
            // final byte byteValue = rows.getByte(i);
            // if (rows.wasNull()) {
            // columnData = null;
            // } else {
            // columnData = byteValue;
            // }
            // } else if (javaSqlType == Types.SMALLINT) {
            // final short shortValue = rows.getShort(i);
            // if (rows.wasNull()) {
            // columnData = null;
            // } else {
            // columnData = shortValue;
            // }
        } else if (javaSqlType == Types.BIT) {
            final boolean booleanValue = rows.getBoolean(i);
            final String stringValue = rows.getString(i);
            if (rows.wasNull()) {
                columnData = null;
            } else {
                columnData = stringValue.equalsIgnoreCase(Boolean.toString(booleanValue)) ? booleanValue : stringValue;
            }
            // } else if (javaSqlType == Types.INTEGER) {
            // final int intValue = rows.getInt(i);
            // if (rows.wasNull()) {
            // columnData = null;
            // } else {
            // columnData = intValue;
            // }
        } else if (javaSqlType == Types.FLOAT /* || javaSqlType == Types.REAL */) {
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
        } else if (javaSqlType == Types.DOUBLE) {
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
        } else {
            final Object objectValue = rows.getObject(i);
            if (rows.wasNull() || objectValue == null)
                columnData = null;
            else
                columnData = objectValue;
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

    private BinaryData readStream(final InputStream stream, final Blob blob) {
        if (stream == null) {
            return null;
        } else {
            final BufferedInputStream in = new BufferedInputStream(stream);
            final BinaryData lobData = new BinaryData(readFully(in), blob);
            return lobData;
        }
    }
}
