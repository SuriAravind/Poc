package com.p3.archon.sample_vsam;

import com.ibm.jzos.ZFile;
import za.co.absa.cobrix.cobol.parser.common.DataExtractors;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Scanner;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;

/**
 * Created by Suriyanarayanan K
 * on 25/02/20 10:50 AM.
 */
public class sam {
    public static void main(String[] args) throws IOException {

        String srcFilePath = "/Users/apple/Documents/Projects/VSAM/COMP.DETAILS.SEP30.DATA.dat";
        Scanner sc = new Scanner(new File(srcFilePath));
        Pattern p = Pattern.compile("([\w\d]*\s*)");
        while (sc.findWithinHorizon(p, 0) != null)
        {
            MatchResult m = sc.match();
            System.out.println(m.group(1));
        }
    }
}
