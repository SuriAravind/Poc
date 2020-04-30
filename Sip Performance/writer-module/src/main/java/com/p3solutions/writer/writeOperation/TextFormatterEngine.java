package com.p3solutions.writer.writeOperation;

import com.p3solutions.writer.base.BaseFormatter;
import com.p3solutions.writer.exceptions.WriterException;
import com.p3solutions.writer.options.ColumnInfo;
import com.p3solutions.writer.options.Options;
import com.p3solutions.writer.formattingHelper.TextFormattingHelper;
import com.p3solutions.writer.utility.others.Color;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;

import static com.p3solutions.writer.utility.others.Utility.numberFormatter;

final class TextFormatterEngine extends BaseFormatter implements WriteEngineHandler {
    String filePath;
    FileChannel fileLooker;
    
    TextFormatterEngine(Options options, String title, List<ColumnInfo> columnsInfo) throws WriterException {
        super(options, columnsInfo, title);
        filePath = options.getOutputResource().toString();
    }
    
    @Override
    public TextFormattingHelper getFormatter() {
        return formattingHelper;
    }
    
    public void iterateRows(final List<Object> receivedRow) throws Exception {
        if (options.getFileCounter() == 0) {
            formattingHelper.flush();
            new File(filePath).delete();
            openNewOutputFile();
        }
        
        if (fileLooker.size() >= (options.getFileSplitBySize() - options.getThresholdSize()) || (
                options.getRecordsProcessed() > 0 &&
                options.getRecordsProcessed() % options.getFileSplitByRecords() == 0)) {
            closeOutputFile();
            openNewOutputFile();
        }
    
        List<Object> currentRow = processRow(receivedRow);
        final Object[] columnData = currentRow.toArray(new Object[currentRow.size()]);
        formattingHelper.writeRow(columnData);
        formattingHelper.flush();
        options.setRecordsProcessed(options.getRecordsProcessed() + 1);
        writerProgressReport(title);
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
        formattingHelper.close();
    }
    
    @Override
    public void handleDataStart() {
        formattingHelper.writeDocumentStart();
        formattingHelper.writeObjectStart();
        formattingHelper.writeObjectNameRow("", title, "", Color.white);
        formattingHelper.writeRowHeader(columnsInfo.stream().map(ColumnInfo::getColumn).toArray(String[]::new));
        formattingHelper.flush();
    }
    
    @Override
    public void handleDataEnd() {
        formattingHelper.writeObjectEnd();
        formattingHelper.writeDocumentEnd();
        formattingHelper.flush();
    }
    
    @Override
    public void disposeWriteEngineHandler() {
        closeOutputFile();
    }
}
