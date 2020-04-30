package com.p3.archon.rdbms_extrator.utils;


import com.sun.org.apache.xml.internal.utils.XMLChar;

/*
 * $$HeadURL$$
 * $$Id$$
 *
 * CCopyright (c) 2015, P3Solutions . All Rights Reserved.
 * This code may not be used without the express written permission
 * of the copyright holder, P3Solutions.
 */

/**
 * Class: GeneralUtil
 * <p>
 * This class is a General Util class.
 *
 * @author Malik
 * @version 1.0
 */
public class GeneralUtil {


    /**
     * Does this string have a value. true or false. Checks for null or empty
     * string
     *
     * @param s the string to check for a value
     * @return true or false.
     */
    public static boolean hasValue(String s) {

        return (s != null) && (s.trim().length() > 0);
    }


    /**
     * Get the File Extension from a filename
     *
     * @param filename the filename
     * @return the file extension
     */
    public static String getFileExtension(String filename) {
        if (!GeneralUtil.hasValue(filename)) return null;
        int index = filename.lastIndexOf('.');
        if (index == -1) return null;
        return filename.substring(index + 1, filename.length()).toLowerCase();
    }

    public static String getXmlValidData(String data) {
        return stripNonValidXMLCharacters(data.replace("&", "&amp;").replace(">", "&gt;").replace("<", "&lt;")
                .replace("'", "&apos;").replace("\"", "&quot;"));
    }

    public static String rightPadding(String str, int num) {
        return String.format("%1$-" + num + "s", str);
    }

    public static String leftPadding(String str, int num) {
        return "                                                         ".substring(0, (num - str.length())) + str;
    }

    public static String timeDiff(long diff) {
        int diffDays = (int) (diff / (24 * 60 * 60 * 1000));
        String dateFormat = "";
        if (diffDays > 0) {
            dateFormat += diffDays + " day ";
        }
        diff -= diffDays * (24 * 60 * 60 * 1000);

        int diffhours = (int) (diff / (60 * 60 * 1000));
        if (diffhours > 0) {
            dateFormat += leftNumPadding(diffhours, 2) + " hour ";
        } else if (dateFormat.length() > 0) {
            dateFormat += "00 hour ";
        }
        diff -= diffhours * (60 * 60 * 1000);

        int diffmin = (int) (diff / (60 * 1000));
        if (diffmin > 0) {
            dateFormat += leftNumPadding(diffmin, 2) + " min ";
        } else if (dateFormat.length() > 0) {
            dateFormat += "00 min ";
        }

        diff -= diffmin * (60 * 1000);

        int diffsec = (int) (diff / (1000));
        if (diffsec > 0) {
            dateFormat += leftNumPadding(diffsec, 2) + " sec ";
        } else if (dateFormat.length() > 0) {
            dateFormat += "00 sec ";
        }

        int diffmsec = (int) (diff % (1000));
        dateFormat += leftNumPadding(diffmsec, 3) + " ms";
        return dateFormat;
    }

    private static String leftNumPadding(int str, int num) {
        return String.format("%0" + num + "d", str);
    }

    private static String stripNonValidXMLCharacters(String in) {
        if (in == null || ("".equals(in)))
            return null;
        StringBuffer out = new StringBuffer();
        for (int i = 0; i < in.length(); i++) {
            char c = in.charAt(i);
            if (XMLChar.isValid(c))
                out.append(c);
        }
        return (out.toString().trim());
    }
}
