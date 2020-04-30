package com.p3.archon.analysis_core.processor.basicMetadata;


import com.p3.archon.analysis_core.beans.ConnectionParameter;
import com.p3.archon.analysis_core.enums.Type;
import com.p3.archon.analysis_core.processor.DSConnectionManager;
import com.p3.archon.analysis_core.processor.basicMetadata.queries.Queries;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;

public class GenerateMetadataJob {

    private ConnectionParameter dbConfig;

    private Type dbType;
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    public void populateMetadata(Connection con, ConnectionParameter params, String metaOutputFileLocation)
            throws SQLException, IOException, InterruptedException, InstantiationException, IllegalAccessException, ClassNotFoundException {

        this.dbConfig = params;
        Long startTime = System.currentTimeMillis();
        ExecutorService executorService = Executors.newFixedThreadPool(ConstantsYml.metadataThreads);
        PrintWriter pw = new PrintWriter(new FileWriter(new File(metaOutputFileLocation + File.separator + "MetaFiles.csv")));
        PrintWriter pwFailed = new PrintWriter(new FileWriter(new File(metaOutputFileLocation + File.separator + "FailedCount.txt")));
        PreparedStatement sql = null;
        dbType = params.getType();
        switch (dbType) {
            case ORACLE:
            case ORACLE_SERVICE:
            case DB2:
                //LOGGER.debug(getMetadataQuery(params.getSchema().get(0)));
                sql = con.prepareStatement(getMetadataQuery(params.getSchema()), ResultSet.TYPE_FORWARD_ONLY,
                        ResultSet.CONCUR_READ_ONLY);
                break;

            case SQL:
                //LOGGER.debug(getMetadataQuery(params.getDbName()));
                sql = con.prepareStatement(getMetadataQuery(params.getSchema()), ResultSet.TYPE_FORWARD_ONLY,
                        ResultSet.CONCUR_READ_ONLY);
                break;
			/*case DB2:
                System.out.println(getMetadataQuery(params.getSchema().get(0)));
                sql = con.prepareStatement(getMetadataQuery(params.getSchema().get(0)), ResultSet.TYPE_FORWARD_ONLY,
                        ResultSet.CONCUR_READ_ONLY);
                break;*/

            case MYSQL:
                sql = con.prepareStatement(getMetadataQuery(params.getDbName()), ResultSet.TYPE_FORWARD_ONLY,
                        ResultSet.CONCUR_READ_ONLY);
                break;
            default:
                LOGGER.error("DB type not supported");

        }
        boolean exit = false;
//        if(con.isClosed()) {
//            System.out.println("Connection Closed");
//            DSConnectionManager dsConnectionManager = new DSConnectionManager();
//            con = dsConnectionManager.RDBMSConnection(params);
//        }
        ResultSet rs = sql.executeQuery();
        String tempTableName = null, tempSchema = null, tempDb = null;
        rs.next();
        do {
            tempTableName = rs.getString("TABLE_NAME");
            switch (dbType) {
                case ORACLE:
                case ORACLE_SERVICE:
                    tempSchema = rs.getString("OWNER");
                    tempDb = rs.getString("OWNER");
                    break;
                case MYSQL:
                    tempSchema = rs.getString("TABLE_SCHEMA");
                    tempDb = rs.getString("TABLE_CATALOG");
                    break;
                case SQL:
                    tempSchema = params.getSchema();
                    tempDb = rs.getString("TABLE_CATALOG");
                    break;
                default:
                    tempSchema = params.getSchema();
                    tempDb = params.getDbName();
            }

            ArrayList<String> colNames = new ArrayList<String>();
            ArrayList<String> colTypes = new ArrayList<String>();
            ArrayList<String> colNamesForQuery = new ArrayList<String>();
            do {
                colNames.add(rs.getString("COL_NAME"));
                colNamesForQuery.add("");
                colTypes.add(rs.getString("COL_TYPE"));
                if (rs.next() == false) {
                    exit = true;
                }
            } while (!exit && rs.getString("TABLE_NAME").trim().equalsIgnoreCase(tempTableName) && tempTableName != null);
//            List<String> unstructuredColsinTable = new LinkedList<>();
            StringBuffer sb = new StringBuffer();
            sb.append("select ");
            for (int k = 0; k < colNames.size(); k++) {
                if(colTypes.get(k).toUpperCase().contains("LOB") /*|| "IMAGE"*/) {
//                    unstructuredColsinTable.add(colNames.get(k) + "|" + colTypes.get(k));
                    sb.append("'-1' as " + colNames.get(k) + ",");
                }
                else {
                    sb.append("count(distinct(" + getQuoteIdentifier(con, params.getType()) + colNames.get(k) + getQuoteIdentifier(con, params.getType()) + "))" + ",");
                }
            }
            sb.append("count(1) ");
            sb.append("from " + tempSchema + "." + tempTableName);

            MetadataThread worker = new MetadataThread(con, sb.toString(), colNames, colTypes,
                    tempTableName,
                    tempSchema, tempDb,
                    ConstantsYml.pkRelaxation, pw, pwFailed, dbType);
            executorService.execute(worker);
        } while (exit == false);
        executorService.shutdown();
        executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
//        while (!executorService.isTerminated()) {
//        }
        // LOGGER.debug("Metadata formed for " + params.getDbName() + "-" + params.getSchema());
        pw.close();
        pwFailed.close();

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

    private String getMetadataQuery(String schema) {
        Queries query = new Queries(dbConfig, dbType);
        dbConfig.setSchema(schema);
        return query.getMetadataQuery();
    }
}
