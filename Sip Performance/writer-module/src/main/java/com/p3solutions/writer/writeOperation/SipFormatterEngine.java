package com.p3solutions.writer.writeOperation;

import com.opentext.ia.sdk.sip.*;
import com.opentext.ia.sdk.support.io.FileSupplier;
import com.opentext.ia.sip.assembly.stringtemplate.StringTemplate;
import com.p3solutions.writer.beans.SipRecordData;
import com.p3solutions.writer.exceptions.WriterException;
import com.p3solutions.writer.options.Options;
import com.p3solutions.writer.utility.FilesToDigitalObjects;

import java.io.IOException;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.p3solutions.writer.utility.others.Utility.timeDiff;

public class SipFormatterEngine implements SipWriteEngineHandler {

    private static final long LOGGER_LIMIT = 500;
    private final Logger LOGGER = Logger.getLogger(this.getClass().getName());
    protected Options options;
    protected String title;
    BatchSipAssembler<SipRecordData> batchAssembler = null;
    private Date START_TIME = new Date();

    public SipFormatterEngine(Options options, String title) {
        this.options = options;
        this.title = title;
    }

    @Override
    public void createBatchAssembler() throws WriterException {
        batchAssembler = getBatchAssembler();
        LOGGER.log(Level.INFO, "Batch Assembler initialization complete");
    }

    private BatchSipAssembler<SipRecordData> getBatchAssembler() throws WriterException {
        PackagingInformation prototype = PackagingInformation.builder().dss()
                .application(options.getSipConfigOptions().getApplication())
                .holding(options.getSipConfigOptions().getHolding())
                .producer(options.getSipConfigOptions().getProducer())
                .entity(options.getSipConfigOptions().getEntity())
                .schema(options.getSipConfigOptions().getSchema()).end().build();

        PackagingInformationFactory factory = new OneSipPerDssPackagingInformationFactory(
                new DefaultPackagingInformationFactory(prototype), new SequentialDssIdSupplier(
                "ex6dss", 1));

        String sipHeader =
                "<" + options.getSipConfigOptions().getRootElement() + " xmlns=\"" + options.getSipConfigOptions().getSchema() + "\">\n";
        String sipFooter = "\n" + "</" + options.getSipConfigOptions().getRootElement() + ">\n";

        String rowTemplate = getRowTemplate();
        PdiAssembler<SipRecordData> pdiAssembler =

                new TemplatePdiAssembler<>(
                        new StringTemplate<>(sipHeader, sipFooter,
                                rowTemplate));
        SipAssembler<SipRecordData> sipAssembler = SipAssembler.forPdiAndContent(factory,
                pdiAssembler,
                new FilesToDigitalObjects());

        return batchAssembler = new BatchSipAssembler<>(
                sipAssembler,
                SipSegmentationStrategy.combining(
                        new SipSegmentationStrategy[]
                                {
                                        SipSegmentationStrategy.byMaxAius(options.getFileSplitByRecords()),
                                        SipSegmentationStrategy.byMaxSipSize(options.getFileSplitBySize())
                                }),
                FileSupplier.fromDirectory(options.getOutputFile().toFile(),
                        "SIP-" + title + "-" + options.getSipConfigOptions().getHolding() + "-",
                        ".zip")
        );


    }

    private String getRowTemplate() {
        return options.getSipConfigOptions().getOverrideRecordElement() ? "$model" +
                ".data$" : "<" + options.getSipConfigOptions().getRecordElement() +
                ">$model" +
                ".data$</" + options.getSipConfigOptions().getRecordElement() + ">" + "\n";
    }


    @Override
    public void iterateRows(SipRecordData sipRecordData) throws Exception {
        if (options.getWriteStartTime() == 0)
            options.setWriteStartTime(new Date().getTime());
        batchAssembler.add(sipRecordData);
        options.setRecordsProcessed(options.getRecordsProcessed() + 1);
        if (options.getRecordsProcessed() % LOGGER_LIMIT == 0) {
            LOGGER.log(Level.INFO,
                    "Records Processed : " + title + " : " + options.getRecordsProcessed() + " : " + timeDiff(new Date().getTime() - START_TIME.getTime()));
        }
    }

    @Override
    public void closeBatchAssembler() throws WriterException, IOException {
        options.setWriteEndTime(new Date().getTime());
        batchAssembler.end();
        LOGGER.log(Level.INFO, "Closing Batch Assembler");
    }
}
