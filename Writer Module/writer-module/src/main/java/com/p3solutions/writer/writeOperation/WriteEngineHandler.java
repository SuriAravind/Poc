package com.p3solutions.writer.writeOperation;

import com.p3solutions.writer.exceptions.WriterException;
import com.p3solutions.writer.formattingHelper.TextFormattingHelper;

import java.io.IOException;
import java.util.List;

public interface WriteEngineHandler {
    TextFormattingHelper getFormatter();
    
    void iterateRows(final List<Object> currentRow) throws Exception;
    
    void openNewOutputFile() throws IOException, WriterException;
    
    void closeOutputFile() throws WriterException;
    
    void handleDataStart() throws WriterException;
    
    void handleDataEnd() throws WriterException;
    
    void disposeWriteEngineHandler() throws WriterException;
}
