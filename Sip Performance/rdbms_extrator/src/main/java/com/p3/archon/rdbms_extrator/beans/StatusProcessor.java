package com.p3.archon.rdbms_extrator.beans;

import lombok.Data;

import java.util.Date;

@Data
public class StatusProcessor {


    public static long totalProcessingTime;
    public static long totalAddTime;
    public static long totalQueriesTime;
    public static long totalProcess;
    public static long totalAdds;
    public static long totalQueries;

    public static synchronized void updateTotalProcessingTime(Date startTime) {
        totalProcessingTime += getLong(startTime);
    }

    public static synchronized void updateTotalAddTime(Date startTime) {
        totalAddTime += getLong(startTime);
    }

    public static synchronized void updateTotalQueriesTime(Date startTime) {
        totalQueriesTime += getLong(startTime);
    }

    private static synchronized long getLong(Date startTime) {
        return new Date().getTime() - startTime.getTime();
    }

    public static synchronized void updateTotalProcess() {
        totalProcess++;
    }

    public static synchronized void updateTotalAdds() {
        totalAdds++;
    }

    public static synchronized void updateTotalQueries() {
        totalQueries++;
    }
}
