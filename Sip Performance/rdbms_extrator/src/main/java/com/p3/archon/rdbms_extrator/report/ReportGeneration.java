package com.p3.archon.rdbms_extrator.report;

import com.p3.archon.rdbms_extrator.beans.InputArgs;
import com.p3.archon.rdbms_extrator.extraction.NonSipExtraction;
import org.apache.log4j.Logger;

import java.util.List;
import java.util.Map;

import static com.p3.archon.rdbms_extrator.utils.GeneralUtil.timeDiff;

/**
 * Created by Suriyanarayanan K
 * on 06/03/20 1:18 AM.
 */
public class ReportGeneration  {

    double totalprocessingtime;
    double totalWritingtime;
    double totalProcessingRecord;
    double totalWritingRecords;
    private Logger LOGGER = Logger.getLogger(this.getClass().getName());


    public void reportInfo(String table, String timeDiff, String writeTime, long sourceCount, long destinationCount,
                           String match, String message, long processingTime, long writingTime) {
        if (message.isEmpty()) {
            totalprocessingtime += processingTime;
            totalWritingtime += writingTime;
            totalProcessingRecord += 1;
            totalWritingRecords += destinationCount;
        } else {
            totalprocessingtime += processingTime;
            totalProcessingRecord += 1;
        }

        LOGGER.debug(table +
                "\n\tProcess Time : " + timeDiff +
                "\n\tWrite Time : " + writeTime +
                "\n\tSource Records Count : " + sourceCount +
                "\n\tTotal Records Processed : " + destinationCount +
                "\n\tMatch : " + match +
                "\n\tMessage : " + message);
    }

    public void printAverage() {
        LOGGER.debug("Average" +
                "\n\tTotal Processing Time : " + totalprocessingtime +
                "\n\tTotal Writing Time : " + totalWritingtime +
                "\n\tTotal Processes executed : " + totalProcessingRecord +
                "\n\tTotal records written : " + totalWritingRecords +
                "\n\tAverage Processing time per process : " + timeDiff((long) (totalprocessingtime / (totalProcessingRecord == 0 ? 1 : totalProcessingRecord))) +
                "\n\tAverage Writing time per record : " + timeDiff((long) (totalWritingtime / (totalWritingRecords == 0 ? 1 : totalWritingRecords))));

    }
}
