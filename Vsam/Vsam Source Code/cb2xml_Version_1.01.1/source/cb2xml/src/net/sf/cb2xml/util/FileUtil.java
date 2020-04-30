package net.sf.cb2xml.util;

import java.io.*;

/**
 * Created by Suriyanarayanan K
 * on 01/03/20 9:54 AM.
 */
public class FileUtil {


    public static InputStream openFile(String fileName) throws FileNotFoundException {
        InputStream stream = ClassLoader.getSystemClassLoader().getResourceAsStream(fileName);
        if (stream == null) stream = new FileInputStream(fileName);
        if (stream == null) throw new FileNotFoundException("resources not found: " + fileName);
        return stream;
    }

    public static StringBuffer readFile(String fileName) throws FileNotFoundException {
        InputStream fis = openFile(fileName);
        BufferedReader buffer = null;
        StringBuffer sb = new StringBuffer();
        String s = null;
        try {
            buffer = new BufferedReader(new InputStreamReader(fis));
            while ((s = buffer.readLine()) != null) {
                sb.append(s);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    ;
                }
            }
        }
        return sb;
    }

    public static void writeFile(String content, String fileName, boolean append) {
        FileWriter writer = null;
        try {
            writer = new FileWriter(fileName, append);
            writer.write(content);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (Exception e) {
                }
                ;
            }
        }
    }

}
