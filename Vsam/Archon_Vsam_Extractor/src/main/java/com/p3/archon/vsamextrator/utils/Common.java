package com.p3.archon.vsamextrator.utils;

import java.io.PrintWriter;
import java.io.StringWriter;

public class Common {

    public String getExceptionString(Exception e){
        StringWriter sw = new StringWriter();
        e.printStackTrace(new PrintWriter(sw));
        return sw.toString();
    }

}
