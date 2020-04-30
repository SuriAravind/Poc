package test.writeOperation;

import test.beans.SipRecordData;
import test.exceptions.WriterException;

import java.io.IOException;
import java.util.List;

public interface SipWriteEngineHandler {

    void createBatchAssembler() throws WriterException;

    void iterateRows(final SipRecordData sipRecordData) throws Exception;

    void closeBatchAssembler() throws WriterException, IOException;

}
