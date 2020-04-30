package com.p3solutions.writer.writeOperation;

import com.p3solutions.writer.base.BaseFormatter;
import com.p3solutions.writer.beans.BinaryData;
import com.p3solutions.writer.exceptions.WriterException;
import com.p3solutions.writer.formattingHelper.TextFormattingHelper;
import com.p3solutions.writer.options.ColumnInfo;
import com.p3solutions.writer.options.Options;
import com.p3solutions.writer.utility.blobbean.BlobInfo;
import com.p3solutions.writer.utility.blobbean.FileInfo;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import static com.p3solutions.writer.utility.others.Utility.numberFormatter;

@SuppressWarnings("unused")
final class XmlFormatterEngine extends BaseFormatter implements WriteEngineHandler {

    final Path path;
    private final Logger LOGGER = Logger.getLogger(this.getClass().getName());
    String filePath;
    FileChannel fileLooker;

    XmlFormatterEngine(Options options, String title, List<ColumnInfo> columnsInfo) throws WriterException {
        super(options, columnsInfo, title);
        filePath = options.getOutputResource().toString();
        path = options.getOutputFile();
    }

    @Override
    public TextFormattingHelper getFormatter() {
        return formattingHelper;
    }

    public void iterateRows(final List<Object> receivedRow) throws Exception {
        switch (options.getFileCounter()) {
            case 0:
                formattingHelper.flush();
                new File(filePath).delete();
                openNewOutputFile();
                options.setWriteStartTime(new Date().getTime());
                break;
            default:
                if (fileLooker.size() >= (options.getFileSplitBySize() - options.getThresholdSize()) || (
                        options.getRecordsProcessed() > 0 &&
                                options.getRecordsProcessed() % options.getFileSplitByRecords() == 0)) {
                    closeOutputFile();
                    openNewOutputFile();
                }
        }

        List<Object> currentRow = processRowXml(receivedRow);
        final Object[] columnData = currentRow.toArray(new Object[currentRow.size()]);
        writeXMLRow(columnData);
        formattingHelper.flush();
        options.setRecordsProcessed(options.getRecordsProcessed() + 1);
        writerProgressReport(schema + "-" + table);
    }

    private void writeXMLRow(Object[] columnData) {
        formattingHelper.writeRecordStart("ROW", "");
        formattingHelper.writeAttribute("REC_ID", Long.toString(options.getRecordsProcessed() + 1));
        for (int i = 0; i < columnData.length; i++) {
            formattingHelper.writeElementStart(columnsInfo.get(i).getColumn());
            if (columnData[i] == null) {
                formattingHelper.writeAttribute("null", "true");
            }
            String columnName = columnsInfo.get(i).getColumn();
            switch (columnsInfo.get(i).getCategory()) {
                case CLOB:
                    formattingHelper.writeAttribute("type", "CLOB");
                    formattingHelper.writeCData(columnData[i] == null ? null : columnData[i].toString());
                    break;
                case BLOB:
                    formattingHelper.writeAttribute("type", "BLOB");
                    if (((BinaryData) columnData[i]).getBlob() != null) {
                        if (!options.getSrcColumnBlobCount().containsKey(columnName)) {
                            options.getSrcColumnBlobCount().put(columnName, (long) 0);
                            options.getDestColumnBlobCount().put(columnName, (long) 0);
                        }
                        options.getSrcColumnBlobCount().put(columnName,
                                options.getSrcColumnBlobCount()
                                        .get(columnName) + 1);
                        BlobInfo blobinfo = ((BinaryData) columnData[i]).processBlob(table,
                                columnName,
                                options.getRecordsProcessed(),
                                path.getParent().toString());
                        if (blobinfo.getStatus().equalsIgnoreCase("Success")) {
                            options.getDestColumnBlobCount()
                                    .put(columnName, options.getDestColumnBlobCount().get(columnName) + 1);
                            blobinfo.setPath(blobinfo.getPath().replace(path.getParent().toString() + (
                                    path.getParent().toString().endsWith(File.separator) ? "" : File.separator), "")
                                    .replace("\\", "/"));
                        }
                        formattingHelper.writeAttribute("ref", blobinfo.getPath());
                        formattingHelper.writeAttribute("size", blobinfo.getSize());
                        formattingHelper.writeAttribute("status", blobinfo.getStatus());
                        formattingHelper.writeValue(blobinfo.getName());
                    }
                    break;
                case FILE_BLOB:
                    formattingHelper.writeAttribute("type", "BLOB");
                    FileInfo fileInfo = ((FileInfo) columnData[i]);
                    if (!options.getSrcColumnBlobCount().containsKey(columnName)) {
                        options.getSrcColumnBlobCount().put(columnName, (long) 0);
                        options.getDestColumnBlobCount().put(columnName, (long) 0);
                    }
                    options.getSrcColumnBlobCount().put(columnsInfo.get(i).getColumn(),
                            options.getSrcColumnBlobCount()
                                    .get(columnName) + 1);
                    if (fileInfo.getStatus().equalsIgnoreCase("Success"))
                        options.getDestColumnBlobCount().put(columnsInfo.get(i).getColumn(),
                                options.getSrcColumnBlobCount()
                                        .get(columnName) + 1);
                    fileInfo.setPath(fileInfo.getPath().replace(path.getParent().toString() + (
                            path.getParent().toString().endsWith(File.separator) ? "" : File.separator), "")
                            .replace("\\", "/"));
                    formattingHelper.writeAttribute("ref", fileInfo.getPath());
                    formattingHelper.writeAttribute("size", fileInfo.getSize());
                    formattingHelper.writeAttribute("status", fileInfo.getStatus());
                    formattingHelper.writeValue(fileInfo.getName());
                    break;
                case ARCHON_DATE:
                case ARCHON_DATE_TIME:
                case ARCHON_DATE_TIME_XML:
                    formattingHelper.writeAttribute("gen", "y");
                case BOOLEAN:
                case NUMBER:
                case DECIMAL:
                case DATE:
                case DATETIME:
                case TIME:
                case STRING:
                default:
                    formattingHelper.writeValue(columnData[i] == null ? "" : columnData[i] + "");
            }
            formattingHelper.writeElementEnd();
        }
        formattingHelper.writeRecordEnd();
    }

    @Override
    public void openNewOutputFile() throws IOException {
        String newPath = filePath + "-" + numberFormatter(options.getFileCounter() + 1) + "." +
                options.getOutputFormatValue().name();
        Path pathloc = Paths.get(newPath);
        if (fileLooker != null && fileLooker.isOpen()) {
            fileLooker.close();
        }
        formattingHelper.close();

        options.setOutputFile(pathloc);
        out = new PrintWriter(options.openNewOutputWriter(options.isAppendOutput()), true);
        setFormattingHelper();
        options.setFileCounter(options.getFileCounter() + 1);
        handleDataStart();
        fileLooker = FileChannel.open(pathloc, StandardOpenOption.READ);
    }

    @Override
    public void closeOutputFile() {
        handleDataEnd();
        options.setWriteEndTime(new Date().getTime());
        formattingHelper.close();
    }

    @Override
    public void handleDataStart() {
        formattingHelper.writeDocumentStart();
        formattingHelper.writeRootElementStart(schema);
        formattingHelper.writeRootElementStart(table);
        formattingHelper.flush();
    }

    @Override
    public void handleDataEnd() {
        formattingHelper.writeRootElementEnd();
        formattingHelper.writeRootElementEnd();
        formattingHelper.writeDocumentEnd();
        formattingHelper.flush();
    }

    @Override
    public void disposeWriteEngineHandler() {
        closeOutputFile();
    }
}
