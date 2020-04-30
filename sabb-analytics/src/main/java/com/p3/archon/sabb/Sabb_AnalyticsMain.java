package com.p3.archon.sabb;

import com.p3.archon.sabb.processor.Processor;
import com.p3.archon.sabb.utils.FileUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

/**
 * Created by Suriyanarayanan K
 * on 25/02/20 5:17 PM.
 */
public class Sabb_AnalyticsMain {


    public static void main(String[] args) throws Exception {
        Logger LOGGER = LoggerFactory.getLogger(Sabb_AnalyticsMain.class);
        if (args.length == 1) {

            if (FileUtil.checkForFile(args[0])) {
                File inputFile = new File(args[0]);
                Processor processor = new Processor(inputFile.getAbsolutePath());
                processor.start();
            } else {
                LOGGER.error("File Not Found");
            }

        } else {
            LOGGER.error("Provide connection input file(.csv) as Argument");
        }
    }
}
