package test.writeOperation;

import test.exceptions.WriterException;
import test.formattingHelper.TextFormattingHelper;
import test.utility.json.JSONException;

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
