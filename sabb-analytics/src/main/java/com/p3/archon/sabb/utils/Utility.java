package com.p3.archon.sabb.utils;

/**
 * Created by Suriyanarayanan K
 * on 26/02/20 5:51 PM.
 */
public class Utility {
    public static  String excepeSpaceinPath(String path) {
        if (OSIdentifier.checkOS()) {
            if (path.contains(" "))
                return "\"" + path + "\"";
        }
        return path.replace(" ", "\\ ");
    }
}
