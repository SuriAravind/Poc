package com.p3.archon.rdbms_extrator;

import com.p3.archon.rdbms_extrator.fun.AsciiGen;
import com.p3.archon.rdbms_extrator.processor.Processor;
import org.apache.log4j.PropertyConfigurator;

import java.util.Date;

import static com.p3.archon.rdbms_extrator.utils.GeneralUtil.timeDiff;

/**
 * Created by Suriyanarayanan K
 * on 29/02/20 1:05 PM.
 */
public class RdbmsExtractorMain {

    public static void main(String[] args) {

        PropertyConfigurator.configure("log4j.properties");
        System.out.println("Start Time : " + new Date());
        long start = new Date().getTime();
        AsciiGen.start();

        if (args.length == 0) {
            System.err.println("No arguments specified.\nTerminating ... ");
            System.err.println("Job Terminated = " + new Date());
            System.exit(1);
        }

        Processor processor = new Processor(args);
        processor.setValuesIntoBean();
        processor.testConnection();
        processor.startExtraction();

        System.out.println("End Time : " + new Date());
        long end = new Date().getTime();
        System.out.println("Total Execution Time :" + timeDiff(end - start));
    }
}
