package com.p3.archon;


import com.p3.archon.analysis_core.processor.AnalysisProcessor;

/**
 * Created by Suriyanarayanan K
 * on 27/02/20 10:53 AM.
 */

public class SabbAnalysisCoreMain {

    public static void main(String[] args) throws Exception {
        if (args.length == 13) {
            AnalysisProcessor processor = new AnalysisProcessor(args);
            processor.start();
        } else {
            throw new Exception("Please provide all the  input");
        }
    }
}
