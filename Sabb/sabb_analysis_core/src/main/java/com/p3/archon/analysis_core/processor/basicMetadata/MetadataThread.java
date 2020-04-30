package com.p3.archon.analysis_core.processor.basicMetadata;

/**
 * @author saumyaguptaa
 */


import com.p3.archon.analysis_core.beans.Column;
import com.p3.archon.analysis_core.beans.Metadata;
import com.p3.archon.analysis_core.enums.Type;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;


public class MetadataThread implements Runnable {
    private Connection con;
    private String countQuery;
    private ArrayList<String> colNames;
    private ArrayList<String> colTypes;
    private String tempTableName;
    private String tempSchema;
    private String tempDb;
    private int pkRelaxation;
    private PrintWriter pw;
    private Type dbType;
    private PrintWriter pwFailed;
//    private List<String> unstructuredColsinTable;
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    public MetadataThread(Connection con, String countQuery, ArrayList<String> colNames, ArrayList<String> colTypes,
                          String tempTableName, String tempSchema, String tempDb, int pkRelaxation, PrintWriter pw, PrintWriter pwFailed, Type dbType) {
        this.colNames = colNames;
        this.colTypes = colTypes;
        this.countQuery = countQuery;
        this.tempTableName = tempTableName;
        this.tempSchema = tempSchema;
        this.tempDb = tempDb;
        this.con = con;
        this.pkRelaxation = pkRelaxation;
        this.pw = pw;
        this.pwFailed = pwFailed;
        this.dbType = dbType;
//        this.unstructuredColsinTable = unstructuredColsinTable;
    }

    @Override
    public void run() {
        ArrayList<Integer> colValues = new ArrayList<Integer>();
        Column col = new Column();
        ResultSet drc;
        /*try (PreparedStatement drcstmt = con.prepareStatement(countQuery, ResultSet.TYPE_FORWARD_ONLY,
                ResultSet.CONCUR_READ_ONLY)) {*/

        try {
//            System.out.println(countQuery);
                PreparedStatement drcstmt = con.prepareStatement(countQuery, ResultSet.TYPE_FORWARD_ONLY,
                        ResultSet.CONCUR_READ_ONLY);
                drc = drcstmt.executeQuery();
                drc.next();
                for (int i = 1; i <= drc.getMetaData().getColumnCount(); i++) {
                    colValues.add(drc.getInt(i));
                }


            for (int i = 0; i < colNames.size(); i++) {
                Metadata md = new Metadata();
                md.tableName = tempTableName;
                col.colName = colNames.get(i);
                col.colType = colTypes.get(i);
                md.column = col;
                md.distinctRowCount = colValues.get(i);
                md.totalRowCount = colValues.get(colValues.size() - 1);
                md.schemaName = tempSchema;
                md.dbName = tempDb;

                md.isDateColumn = colTypes.get(i).toUpperCase().contains("DATE");

                int acceptedRelaxation = pkRelaxation * md.totalRowCount / 100;
                int numOfNulls = 0;
                if(md.totalRowCount > 0) {
                    if (md.totalRowCount - md.distinctRowCount <= acceptedRelaxation) {
                        String nullCheckQuery = "select count(1) as count from " + tempSchema + "." + getQuoteIdentifier(con, dbType) + tempTableName + getQuoteIdentifier(con, dbType) + " where "
                                + getQuoteIdentifier(con, dbType) + colNames.get(i) + getQuoteIdentifier(con, dbType) + " is null";

//                    try(
//                        PreparedStatement nullCheckStmt = con.prepareStatement(nullCheckQuery);
//                        ResultSet nullcheckResult = nullCheckStmt.executeQuery();)
//                        nullcheckResult.next();
//                        numOfNulls = nullcheckResult.getInt(1);


                        try (PreparedStatement nullCheckStmt = con.prepareStatement(nullCheckQuery)) {
                            ResultSet nullcheckResult = nullCheckStmt.executeQuery();
                            nullcheckResult.next();
                            numOfNulls = nullcheckResult.getInt(1);
                        } catch (Exception e) {
                            LOGGER.info(">>>>>" + nullCheckQuery + ":" + md.totalRowCount);
                            e.printStackTrace();
                        }

                        if (numOfNulls <= acceptedRelaxation)
                            md.probablePrimary = true;
                        else
                            md.probablePrimary = false;
                    } else {
                        md.probablePrimary = false;
                    }
                }
                else {
                    md.probablePrimary = false;
                }
                String toSave = tempTableName + "," + colNames.get(i) + "," + colTypes.get(i) + "," +
                        md.distinctRowCount + "," + md.totalRowCount + "," + md.probablePrimary + "\n";
                pw.write(toSave);
                pw.flush();
//                for(String unstructuredCol : unstructuredColsinTable) {
//                    String toSaveUnstructured = tempTableName + "," + unstructuredCol.split("\\|")[0] + "," + unstructuredCol.split("\\|")[1] + "," +
//                            -1 + "," + md.totalRowCount + "," + false + "\n";
//                    pw.write(toSaveUnstructured);
//                    pw.flush();
//                }
            }
        } catch (Exception e) {
//			System.out.println("Failed query-->" + countQuery);
//			System.out.println("e.getMessage-->" + e.getMessage());
//            e.printStackTrace();
//            System.out.println(">>>" + countQuery);
            pwFailed.write(countQuery+"\n");
            pwFailed.flush();
        }
    }

    private String getQuoteIdentifier(Connection con, Type dbType) {
        try {
            return con.getMetaData().getIdentifierQuoteString();
        } catch (Exception e) {
            switch (dbType) {
                case SQL:
                    return "\"";
                case MYSQL:
                    return "`";
                case ORACLE:
                case ORACLE_SERVICE:
                    return "\"";
                case AS400:
                    return "\"";
                default:
                    return "";
            }
        }
    }
}
