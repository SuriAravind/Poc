package com.p3solutions.writer.writeOperation;

import com.p3solutions.writer.options.Options;

import java.util.logging.Level;
import java.util.logging.Logger;

public final class SipWriteEngineExecutable {

    private final Logger LOGGER = Logger.getLogger(this.getClass().getName());

    private final Options options;
    private SipWriteEngineHandler createdHandler;
    private String title;

    public SipWriteEngineExecutable(Options options, String title) {
        this.options = options;
        this.title = title;
    }
    
    public SipWriteEngineHandler getSipWriteEngineHandler() throws Exception {
        final SipWriteEngineHandler sipHandler = getSipExecutionHandler();
        createdHandler = sipHandler;
        return sipHandler;
    }
    
    private SipWriteEngineHandler getSipExecutionHandler() throws Exception {
        return new SipFormatterEngine(this.options, title);
    }
    
    public void generateReport() {
        LOGGER.log(Level.INFO, "Records Processed : " + title + " : " + options.getRecordsProcessed());
    }
}
