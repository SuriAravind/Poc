package test.writeOperation;

import com.opentext.ia.sdk.sip.*;
import com.opentext.ia.sdk.support.io.FileSupplier;
import com.opentext.ia.sip.assembly.stringtemplate.StringTemplate;
import test.beans.SipRecordData;
import test.exceptions.WriterException;
import test.options.Options;
import test.utility.FilesToDigitalObjects;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SipFormatterEngine implements SipWriteEngineHandler {

    private static final long LOGGER_LIMIT = 500;
    private final Logger LOGGER = Logger.getLogger(this.getClass().getName());
    protected Options options;
    protected String title;

    BatchSipAssembler<SipRecordData> batchAssembler = null;

    public SipFormatterEngine(Options options, String title) {
        this.options = options;
        this.title = title;
    }

    @Override
    public void createBatchAssembler() throws WriterException {
        PackagingInformation prototype = PackagingInformation.builder().dss()
                .application(options.getSipConfigOptions().getApplication())
                .holding(options.getSipConfigOptions().getHolding())
                .producer(options.getSipConfigOptions().getProducer())
                .entity(options.getSipConfigOptions().getEntity())
                .schema(options.getSipConfigOptions().getSchema()).end().build();

        PackagingInformationFactory factory = new OneSipPerDssPackagingInformationFactory(
                new DefaultPackagingInformationFactory(prototype), new SequentialDssIdSupplier("ex6dss", 1));

        String sipHeader = "<" + options.getSipConfigOptions().getRootElement() + " xmlns=\"" + options.getSipConfigOptions().getSchema() + "\">\n";
        String sipFooter = "\n" + "</" + options.getSipConfigOptions().getRootElement() + ">\n";

        PdiAssembler<SipRecordData> pdiAssembler = new TemplatePdiAssembler<>(
                new StringTemplate<>(sipHeader, sipFooter, "<" + options.getSipConfigOptions().getRecordElement() + ">$model.data$</" + options.getSipConfigOptions().getRecordElement() + ">" + "\n"));
        SipAssembler<SipRecordData> sipAssembler = SipAssembler.forPdiAndContent(factory, pdiAssembler,
                new FilesToDigitalObjects());

        batchAssembler = new BatchSipAssembler<>(sipAssembler, SipSegmentationStrategy.byMaxSipSize(options.getFileSplitBySize()),
                FileSupplier.fromDirectory(options.getOutputFile().toFile(), "SIP-" + title + "-" + options.getSipConfigOptions().getHolding() + "-", ".zip"));
        LOGGER.log(Level.INFO, "Batch Assembler initialization complete");
    }

    @Override
    public void iterateRows(SipRecordData sipRecordData) throws Exception {
        batchAssembler.add(sipRecordData);
        options.setRecordsProcessed(options.getRecordsProcessed() + 1);
        if (options.getRecordsProcessed() % LOGGER_LIMIT == 0)
            LOGGER.log(Level.INFO, options.getRecordsProcessed() + " Record Processed");
    }

    @Override
    public void closeBatchAssembler() throws WriterException, IOException {
        batchAssembler.end();
        LOGGER.log(Level.INFO, "Closing Batch Assembler");
    }
}
