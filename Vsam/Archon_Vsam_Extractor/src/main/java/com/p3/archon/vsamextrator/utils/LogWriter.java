package com.p3.archon.vsamextrator.utils;

import java.io.*;
import java.sql.Timestamp;

public class LogWriter {

    private Writer out;
    private Common common;

    public LogWriter(String outputPath, String filename) throws IOException {
        outputPath += outputPath.endsWith(File.separator) ? "" : File.separator;
        outputPath += filename + ".log";
        this.out = new OutputStreamWriter(new FileOutputStream(outputPath));
        this.common = new Common();
        this.write("-----------------" + new Timestamp(System.currentTimeMillis()) + "-----------------" + "\n");
        this.out.flush();
    }

    public void write(Exception e) throws IOException {
        out.write(common.getExceptionString(e) + "\n");
        out.flush();
    }

    public void write(String logText) throws IOException {
        out.write(logText + "\n");
        out.flush();
    }

    public void end() throws IOException {
        out.flush();
        out.close();
    }
}
