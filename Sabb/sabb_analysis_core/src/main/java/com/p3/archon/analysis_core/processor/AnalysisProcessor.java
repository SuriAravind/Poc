package com.p3.archon.analysis_core.processor;


import com.p3.archon.analysis_core.beans.ConnectionParameter;
import com.p3.archon.analysis_core.enums.Type;
import com.p3.archon.analysis_core.processor.basicMetadata.GenerateMetadataJob;
import com.p3.archon.analysis_core.response.TableResponseAge;
import com.p3.archon.analysis_core.utils.FileUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Created by Suriyanarayanan K
 * on 27/02/20 11:08 AM.
 */
public class AnalysisProcessor {

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
    String appName;
    Type dbType;
    String host;
    int port;
    String dbName;
    String schema;
    String user;
    String pwd;
    String ageByDate;
    String outputLocation;
    String metaDataRequired;
    String relationshipRequired;
    String ageRequired;

    public AnalysisProcessor(String[] args) {

        this.appName = args[0];
        this.dbType = Type.valueOf(args[1]);
        this.host = args[2];
        this.port = Integer.parseInt(args[3]);
        this.dbName = args[4];
        this.schema = args[5];
        this.user = args[6];
        this.pwd = args[7];
        this.ageByDate = args[8];
        this.outputLocation = args[9];
        this.metaDataRequired = args[10];
        this.relationshipRequired = args[11];
        this.ageRequired = args[12];
    }


    public void start() {

//        System.out.println("Process Started");

        List<String> schemaList = Arrays.asList(schema.split("\\|"));


        ConnectionParameter params = new ConnectionParameter();
        params.setAppName(appName);
        params.setDbName(dbName);
        params.setSchema(schemaList.get(0));
        params.setUserName(user);
        params.setPassword(pwd);
        params.setHostName(host);
        params.setPort(port);
        params.setCategories("RDBMS");
        params.setType(dbType);
        params.setAgeByDate(ageByDate);
        params.setOutputLocation(outputLocation);


        DSConnectionManager dsConnectionManager = new DSConnectionManager();
        try {
            Connection connection;

            outputLocation = outputLocation.endsWith(File.separator) ? outputLocation : outputLocation + File.separator;
            String outputFolderLocation = outputLocation + "OUTPUT" + File.separator + appName + "_" + dbName + "_" + schema;
            FileUtil.checkCreateDirectory(outputFolderLocation);
            try {
                if (metaDataRequired.equalsIgnoreCase("yes")) {
//                    System.out.println("Metadata Generation Started");
                    connection = dsConnectionManager.RDBMSConnection(params);
                    GenerateMetadataJob generateMetaData = new GenerateMetadataJob();
                    generateMetaData.populateMetadata(connection, params, outputFolderLocation);
//                    System.out.println("Metadata generated");
                    LOGGER.debug("Metadata formed for " + params.getAppName() + "-" + params.getDbName() + "-" + params.getSchema());
                }
            } catch (Exception e) {
//                e.printStackTrace();
                LOGGER.error("Metadata generation encountered issue for " + params.getAppName() + "-" + params.getDbName() + "-" + params.getSchema());
            }
            try {
                if (relationshipRequired.equalsIgnoreCase("yes")) {
                    connection = dsConnectionManager.RDBMSConnection(params);
                    RelationsShipProcess relationsShipProcess = new RelationsShipProcess(connection, outputFolderLocation, params.getSchema());
                    relationsShipProcess.start();
                    LOGGER.debug("Relationship formed for " + params.getAppName() + "-" + params.getDbName() + "-" + params.getSchema());
                }
            } catch (Exception e) {
//                e.printStackTrace();
                LOGGER.error("Relationship generation encountered issue for " + params.getAppName() + "-" + params.getDbName() + "-" + params.getSchema());
            }
            try {
                if (!ageRequired.equalsIgnoreCase("no")) {
                    connection = dsConnectionManager.RDBMSConnection(params);
                    AgeProcess ageProcess = new AgeProcess(outputFolderLocation);
                    Map<String, TableResponseAge> responseMap = ageProcess.startAgeProcess(params, connection, ageRequired);   // ageRequired is the path
                    ageProcess.writeAgeResults(responseMap);
                    LOGGER.debug("Ageing done for " + params.getAppName() + "-" + params.getDbName() + "-" + params.getSchema());
                }
            } catch (Exception e) {
                e.printStackTrace();
                LOGGER.error("Ageing done encountered issue for " + params.getAppName() + "-" + params.getDbName() + "-" + params.getSchema());
            }
        } catch (SQLException e) {
            LOGGER.error("SQLException", e.getMessage());
//             e.printStackTrace();
        } catch (ClassNotFoundException e) {
            LOGGER.error("ClassNotFoundException", e.getMessage());
            //e.printStackTrace();
        } catch (InstantiationException e) {
            LOGGER.error("InstantiationException", e.getMessage());
            //e.printStackTrace();
        } catch (IllegalAccessException e) {
            LOGGER.error("IllegalAccessException", e.getMessage());
            //e.printStackTrace();
        } catch (Exception e) {
            LOGGER.error("Exception", e.getMessage());
        }
        // LOGGER.debug("Check the directory for Output :" + outputLocation + File.separator + "OUTPUT");

    }
}
