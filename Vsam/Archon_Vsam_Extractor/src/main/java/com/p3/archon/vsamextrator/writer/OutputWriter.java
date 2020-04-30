package com.p3.archon.vsamextrator.writer;

import com.p3.archon.vsamextrator.utils.LogWriter;
import com.sun.org.apache.xml.internal.utils.XMLChar;

import java.io.*;

public class OutputWriter {

    private String oneTab = "\t";
    private String twoTab = "\t\t";
    private String newLine = "\n";
    private Writer out;
    private int runningFileNumber = 0;
    private int rpx;
    private String outputPath;
    private File outputFilePath;
    private String copybookName;
    private LogWriter logWriter;

    public OutputWriter(String outputPath, String copybookName, int rpx, LogWriter logWriter) throws IOException {
        this.outputPath = outputPath;
        this.copybookName = copybookName;
        this.logWriter = logWriter;
        createFileWriter();
        if (rpx < 100)
            this.rpx = 100 * 1024 * 1024;
        else
            this.rpx = rpx * 1024 * 1024;
    }

    private String getRunningFileNumber() {
        return "-" + String.format("%05d", runningFileNumber++);
    }

    public static String getTextFormatted(String string) {
        string = string.trim().replaceAll("[^_^\\p{Alnum}.]", "_").replace("^", "_").replaceAll("\\s+", "_");
        string = ((string.startsWith("_") && string.endsWith("_") && string.length() > 2)
                ? string.substring(1).substring(0, string.length() - 2)
                : string);
        return (string.length() > 0)
                ? (((string.charAt(0) >= '0' && string.charAt(0) <= '9') ? "_" : "") + string).toUpperCase()
                : string;
    }

    public String getXmlValidData(String data) {
        if (data == null)
            return null;
        return stripNonValidXMLCharacters(data.replace("&", "&amp;").replace(">", "&gt;").replace("<", "&lt;")
                .replace("'", "&apos;").replace("\"", "&quot;"));
    }

    private String stripNonValidXMLCharacters(String in) {
        if (in == null || ("".equals(in)))
            return null;
        StringBuffer out = new StringBuffer();
        for (int i = 0; i < in.length(); i++) {
            char c = in.charAt(i);
            if (XMLChar.isValid(c))
                out.append(c);
            else
                out.append("");
//                out.append(checkReplace(in.codePointAt(i)));
        }
        return (out.toString().trim());
    }

    public void writeElement(String columnName, String columnValue) throws IOException {
        columnValue = getXmlValidData(columnValue);
        columnName = getTextFormatted(columnName);
        if (columnValue != null)
            this.out.write(twoTab + "<" + columnName + ">" + columnValue + "</" + columnName + ">" + newLine);
    }

    public void writeRowStart() throws IOException {
        checkCreateNewFile();
        this.out.write(oneTab + "<" + getTextFormatted(copybookName) + "_ROW" + ">" + newLine);
    }

    public void writeRowEnd() throws IOException {
        this.out.write(oneTab + "</" + getTextFormatted(copybookName) + "_ROW" + ">" + newLine);
        this.out.flush();
    }

    public void writeTableStart() throws IOException {
        this.out.write("<" + getTextFormatted(copybookName) + ">" + newLine);
    }

    public void writeTableEnd() throws IOException {
        this.out.write("</" + getTextFormatted(copybookName) + ">" + newLine);
        this.out.flush();
    }

    public void writeXmlTagInfo() throws IOException {
        this.out.write("<?xml version=\"1.0\" encoding=\"utf-8\"?>" + newLine);
    }

    private void checkCreateNewFile() throws IOException {
        if (outputFilePath.length() > rpx)
            createFileWriter();
    }

    private void createFileWriter() throws IOException {
        if (out != null) {
            writeTableEnd();
            out.flush();
            out.close();
        }
        String newOutputPath = outputPath + (outputPath.endsWith(File.separator) ? "" : File.separator);
        newOutputPath += getTextFormatted(copybookName) + getRunningFileNumber() + ".xml";
        this.outputFilePath = new File(newOutputPath);
        this.out = new OutputStreamWriter(new FileOutputStream(newOutputPath));
        writeXmlTagInfo();
        this.writeTableStart();
        logWriter.write("Writing output to file =>" + newOutputPath);
    }

    public void endWriter() throws IOException {
        writeTableEnd();
        this.out.flush();
        this.out.close();
    }


}
