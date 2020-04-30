package com.p3.archon.sample_vsam;

import org.mozilla.universalchardet.UniversalDetector;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class CsvFileWriter {
    private static final String COMMA_DELIMITER = ",";
    private static final String NEW_LINE_SEPARATOR = "\n";

    public static void writeCsvFile(String fileName, ArrayList<String> data) {
        ArrayList<String> students = data;
        FileWriter fileWriter = null;
        try {
            fileWriter = new FileWriter(fileName);
            for (String student111 : students) {

                String datafinal[] = student111.split("#");
                for (String dat1 : datafinal) {
                    fileWriter.append(dat1);
                    fileWriter.append(COMMA_DELIMITER);
                }
                fileWriter.append(NEW_LINE_SEPARATOR);
            }
            System.out.println("CSV file was created successfully !!!");

        } catch (Exception e) {
            System.out.println("Error in CsvFileWriter !!!");
            e.printStackTrace();
        } finally {

            try {
                fileWriter.flush();
                fileWriter.close();
            } catch (IOException e) {
                System.out.println("Error while flushing/closing fileWriter !!!");
                e.printStackTrace();
            }

        }
    }

    public static String getEncoding(File file) {
        String encoding = null;
        FileInputStream fis = null;
        UniversalDetector detector;
        try {
            byte[] buf = new byte[4096];
            fis = new java.io.FileInputStream(file);
            detector = new UniversalDetector(null);
            int nread;
            while ((nread = fis.read(buf)) > 0 && !detector.isDone()) {
                detector.handleData(buf, 0, nread);
            }
            detector.dataEnd();
            encoding = detector.getDetectedCharset();
            if (encoding == null)
                throw new Exception("Unable to determine File Encoding");
            detector.reset();
        } catch (Exception e) {
            encoding = "UTF-8";
        } finally {
            if (fis != null)
                try {
                    fis.close();
                } catch (IOException e) {
                }
        }
        return encoding;
    }

    public static void main(String[] args) throws IOException {

//"WINDOWS-1253"
        String srcFilePath = "/Users/apple/Documents/Projects/VSAM/c.txt" +
				"";
        System.out.println("Encoding  :" + getEncoding(new File(srcFilePath)));
        BufferedReader in = new BufferedReader(
                new InputStreamReader(new FileInputStream(new File(srcFilePath)),StandardCharsets.ISO_8859_1));

        String str;

        while ((str = in.readLine()) != null) {
           System.out.println(str);
            //System.out.println(new String(str.getBytes(Charset.forName("UTF-8"))));
            //System.out.println(new String(new String(str.getBytes(StandardCharsets.ISO_8859_1)).getBytes(StandardCharsets.ISO_8859_1),StandardCharsets.UTF_8));
        }



    }
}
