package com.p3solutions.writer.writeOperation;

import com.opentext.ia.sdk.sip.BatchSipAssembler;
import com.p3solutions.writer.beans.SipRecordData;
import com.p3solutions.writer.exceptions.WriterException;

import java.io.IOException;

public interface SipWriteEngineHandler {

    void createBatchAssembler() throws WriterException;

    void iterateRows(final SipRecordData sipRecordData) throws Exception;

    void closeBatchAssembler() throws WriterException, IOException;

}
