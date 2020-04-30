package com.p3.archon.sabb.utils;

/**
 * Created by Suriyanarayanan K
 * on 25/02/20 5:35 PM.
 */
public class OSIdentifier {

    public static final String WINDOWS_OS = "Windows";
    public static String osName = System.getProperty("os.name");

    public static boolean checkOS() {
        if (osName.startsWith(WINDOWS_OS))
            return true;
        else
            return false;
    }
}
