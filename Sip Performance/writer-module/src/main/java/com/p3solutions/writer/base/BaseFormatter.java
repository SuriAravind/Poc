package com.p3solutions.writer.base;

import com.p3solutions.writer.beans.BinaryData;
import com.p3solutions.writer.exceptions.WriterException;
import com.p3solutions.writer.formattingHelper.*;
import com.p3solutions.writer.options.ArchonDataType;
import com.p3solutions.writer.options.ColumnInfo;
import com.p3solutions.writer.options.Options;
import com.p3solutions.writer.options.TextOutputFormat;
import com.p3solutions.writer.utility.blobbean.FileInfo;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.p3solutions.writer.utility.others.Utility.timeDiff;
import static java.util.Objects.requireNonNull;

public abstract class BaseFormatter {

    private static final Logger LOGGER = Logger.getLogger(BaseFormatter.class.getName());
    private static final long LOGGER_LIMIT = 500;
    protected TextFormattingHelper formattingHelper;
    protected Options options;
    protected PrintWriter out;
    protected List<ColumnInfo> columnsInfo;
    protected String title;
    protected String schema;
    protected String table;
    private Date START_TIME = new Date();


    public BaseFormatter(Options outputOptions, List<ColumnInfo> columnsInfo, String title) throws WriterException {
        this.options = requireNonNull(outputOptions, "Options not provided");
        this.title = title;
        this.schema = title.split("-")[0];
        this.table = title.split("-")[1];
        this.columnsInfo = columnsInfo;
        try {
            out = new PrintWriter(options.openNewOutputWriter(options.isAppendOutput()), true);
        } catch (final IOException e) {
            throw new WriterException("Cannot open output writer", e);
        }

        setFormattingHelper();

    }

    protected void setFormattingHelper() {
        final TextOutputFormat outputFormat = options.getOutputFormatValue();
        switch (outputFormat) {
            case xml:
                formattingHelper = new XmlFormattingHelper(out, outputFormat);
                break;
            case json:
                formattingHelper = new JsonFormattingHelper(out, outputFormat);
                break;
            case html:
                formattingHelper = new HtmlFormattingHelper(out, outputFormat);
                break;
            default:
                formattingHelper = new PlainTextFormattingHelper(out, outputFormat);
                break;
        }
    }

    protected void writerProgressReport(String title) {
        if (options.getRecordsProcessed() % LOGGER_LIMIT == 0) {
            LOGGER.log(Level.INFO,
                    "Records Processed : " + title + " : " + options.getRecordsProcessed() + " : " + timeDiff(new Date().getTime() - START_TIME.getTime()));
        }
    }

    protected List<Object> processRow(List<Object> receivedRow) {
        /*     if (options.isSplitDate()) {*/
        List<Object> currentRow = new ArrayList<>();
        int lookUpIndex = -1;
        for (ColumnInfo colums : columnsInfo) {
            switch (colums.getCategory()) {
                case ARCHON_DATE:
                case ARCHON_DATE_TIME:
                case ARCHON_DATE_TIME_XML:
                    currentRow.add(SplitDateProcessor(receivedRow.get(lookUpIndex),
                            colums.getCategory()));
                    break;
                case BLOB:
                    ++lookUpIndex;
                    currentRow.add(BlobProcessor(columnsInfo.get(lookUpIndex).getColumn(),
                            receivedRow.get(lookUpIndex)));
                    break;
                case FILE_BLOB:
                    ++lookUpIndex;
                    currentRow.add(FileProcessor(columnsInfo.get(lookUpIndex).getColumn(),
                            receivedRow.get(lookUpIndex)));
                    break;
                default:
                    currentRow.add(receivedRow.get(++lookUpIndex));
            }
        }
        return currentRow;
/*        } else {
            return receivedRow;
        }*/
    }

    protected List<Object> processRowXml(List<Object> receivedRow) {
        if (options.isSplitDate()) {
            List<Object> currentRow = new ArrayList<>();
            int lookUpIndex = -1;
            for (ColumnInfo colums : columnsInfo) {
                switch (colums.getCategory()) {
                    case ARCHON_DATE:
                    case ARCHON_DATE_TIME:
                    case ARCHON_DATE_TIME_XML:
                        currentRow.add(SplitDateProcessor(receivedRow.get(lookUpIndex),
                                colums.getCategory()));
                        break;
                    default:
                        currentRow.add(receivedRow.get(++lookUpIndex));
                }
            }
            return currentRow;
        } else {
            return receivedRow;
        }
    }

    private Object BlobProcessor(String column, Object blobdata) {
        if (blobdata == null)
            return null;
        BinaryData binaryData = null;
        try {
            binaryData = (BinaryData) blobdata;
        } catch (ClassCastException e) {
            LOGGER.log(Level.SEVERE, "Unable to cast Blob data to BinaryData");
        }
        if (binaryData.getBlob() == null)
            return null;
        return binaryData.processBlob(table, column, options.getRecordsProcessed(),
                options.getOutputFile().getParent().toString()).getPath();
    }

    private Object SplitDateProcessor(Object dateValue, ArchonDataType javaDataType) {
        if (dateValue == null && dateValue.toString().isEmpty()) {
            return dateValue;
        }

        String part1 = dateValue.toString().split(" ")[0];
        String part2;
        try {
            part2 = dateValue.toString().split(" ")[1];
        } catch (Exception e) {
            part2 = "00:00:00";
        }

        switch (javaDataType) {
            case ARCHON_DATE:
                return part1;
            case ARCHON_DATE_TIME:
                return part1 + " " + part2;
            case ARCHON_DATE_TIME_XML:
                return part1 + "T" + part2;
            default:
                return dateValue;
        }
    }

    protected Object FileProcessor(String column, Object blobdata) {
        if (blobdata == null)
            return null;
        FileInfo fileInfo = null;
        try {
            fileInfo = (FileInfo) blobdata;
            fileInfo.setPath(fileInfo.getPath().replace(options.getOutputFile().getParent().toString(), ".."));
        } catch (ClassCastException e) {
            LOGGER.log(Level.SEVERE, "Unable to cast Blob data to BinaryData");
            return null;
        }
        return fileInfo;
    }
}
