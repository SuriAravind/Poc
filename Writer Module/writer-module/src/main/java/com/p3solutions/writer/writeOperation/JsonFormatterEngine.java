package com.p3solutions.writer.writeOperation;

import com.p3solutions.writer.base.BaseFormatter;
import com.p3solutions.writer.exceptions.WriterException;
import com.p3solutions.writer.formattingHelper.JsonFormattingHelper;
import com.p3solutions.writer.formattingHelper.TextFormattingHelper;
import com.p3solutions.writer.options.ColumnInfo;
import com.p3solutions.writer.options.Options;
import com.p3solutions.writer.utility.json.JSONArray;
import com.p3solutions.writer.utility.json.JSONException;
import com.p3solutions.writer.utility.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.logging.Logger;

import static com.p3solutions.writer.utility.others.Utility.numberFormatter;

final class JsonFormatterEngine extends BaseFormatter implements WriteEngineHandler {
    
    final Path path;
    
    private final Logger LOGGER = Logger.getLogger(this.getClass().getName());
    private String filePath;
    private JSONObject jsonData;
    private JSONArray jsonRows;
    private JSONArray jsonDataArray;
    
    private long bytesize;
    private JSONArray ja;
    
    JsonFormatterEngine(Options options, String title, List<ColumnInfo> columnsInfo) throws WriterException {
        super(options, columnsInfo,title);
        filePath = options.getOutputResource().toString();
        path = options.getOutputFile();
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
        
        if (bytesize >= (options.getFileSplitBySize() - options.getThresholdSize()) || (
                options.getRecordsProcessed() > 0 &&
                options.getRecordsProcessed() % options.getFileSplitByRecords() == 0)) {
            closeOutputFile();
            openNewOutputFile();
        }
        
        List<Object> currentRow = processRow(receivedRow);
        JSONArray ja = new JSONArray(currentRow);
        jsonRows.put(ja);
        bytesize += ja.toString().getBytes().length * 1.8; // 1.8 is approximation size factor
        
        formattingHelper.flush();
        options.setRecordsProcessed(options.getRecordsProcessed() + 1);
        writerProgressReport(schema + "-" + table);
        
    }
    
    @Override
    public void openNewOutputFile() throws IOException, WriterException {
        String newPath = filePath + "-" + numberFormatter(options.getFileCounter() + 1) + "." +
                         options.getOutputFormatValue().name();
        Path pathloc = Paths.get(newPath);
        formattingHelper.close();
        
        jsonDataArray = new JSONArray();
        options.setOutputFile(pathloc);
        out = new PrintWriter(options.openNewOutputWriter(options.isAppendOutput()), true);
        setFormattingHelper();
        options.setFileCounter(options.getFileCounter() + 1);
        handleDataStart();
        bytesize = 0;
    }
    
    @Override
    public void closeOutputFile() throws WriterException {
        handleDataEnd();
        formattingHelper.close();
    }
    
    @Override
    public void handleDataStart() throws WriterException {
        try {
            jsonData = new JSONObject();
            jsonData.put("schema", schema);
            jsonData.put("table", table);
            jsonData.put("columnNames", columnsInfo.stream().map(ColumnInfo::getColumn).toArray());
        } catch (JSONException e) {
            throw new WriterException("Could not convert data to JSON", e);
        }
        jsonRows = new JSONArray();
    }
    
    @Override
    public void handleDataEnd() throws WriterException {
        try {
            jsonData.put("rows", jsonRows);
        } catch (JSONException e) {
            throw new WriterException("Could not convert data to JSON", e);
        }
        ((JsonFormattingHelper) (formattingHelper)).write(jsonData);
        formattingHelper.flush();
    }
    
    @Override
    public void disposeWriteEngineHandler() throws WriterException {
        closeOutputFile();
    }
}
