package com.p3.archon.analysis_core.processor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Suriyanarayanan K
 * on 27/02/20 3:48 PM.
 */
public class RelationsShipProcess {

    private Connection connection;
    private String OutputFolderLocation;
    ResultSet resultSet;
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
    private StringBuffer relationShipFileContent;
    private String HEADER = "\t\t\t\t\t";
    private String NEW_LINE = "\n";
    private String schema;

    public RelationsShipProcess(Connection connection, String OutputFolderLocation, String schema) {
        this.connection = connection;
        this.OutputFolderLocation = OutputFolderLocation;
        this.relationShipFileContent = new StringBuffer();
        this.schema = schema;
    }

    public void start() {
        try {
            DatabaseMetaData databaseMetaData = connection.getMetaData();
            String catelog = connection.getCatalog();
            //for (String schemaName : schemaList) {
            resultSet = databaseMetaData.getTables(catelog, schema, null, new String[]{"TABLE"});
            relationShipFileContent.append(HEADER + schema + NEW_LINE);
            List<String> tableList = new ArrayList<String>();
            while (resultSet.next()) {
                tableList.add(resultSet.getString("TABLE_NAME"));
            }
            for (String tableName : tableList) {
                relationShipFileContent.append(NEW_LINE);
                getForeignKeyColumns(catelog, databaseMetaData, schema, tableName);
            }
            createOuputFile(schema);
            // }

        } catch (SQLException e) {
            e.printStackTrace();
            LOGGER.error("SQLException", e.getMessage());
        }
    }

    private void createOuputFile(String schemaName) {
        String fileContent = relationShipFileContent.toString();
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(OutputFolderLocation + File.separator + "Relation_Ship_Details_" + schemaName + ".txt"));
            writer.write(fileContent);
            writer.close();
        } catch (IOException e) {
            LOGGER.error("IOException", e.getMessage());
        }

    }

    private void getForeignKeyColumns(String catelog, DatabaseMetaData databaseMetaData, String schemaName, String tableName) {

        ResultSet exportedKeys = null;
        ResultSet importedKeys = null;
        try {
            exportedKeys = databaseMetaData.getExportedKeys(catelog, schemaName, tableName);
            writeContentIntoStringBuffer(exportedKeys);
            importedKeys = databaseMetaData.getImportedKeys(catelog, schemaName, tableName);
            writeContentIntoStringBuffer(importedKeys);
        } catch (SQLException e) {
            LOGGER.error("SQLException", e.getMessage());
        }
    }

    private void writeContentIntoStringBuffer(ResultSet foreignKeys) {

        try {
            while (foreignKeys.next()) {
                relationShipFileContent.append(foreignKeys.getString("PKTABLE_NAME") + "." + foreignKeys.getString("PKCOLUMN_NAME"));
                relationShipFileContent.append("<>");
                relationShipFileContent.append(foreignKeys.getString("FKTABLE_NAME") + "." + foreignKeys.getString("FKCOLUMN_NAME"));
                relationShipFileContent.append(NEW_LINE);
            }
            foreignKeys.close();
        } catch (SQLException e) {
            LOGGER.error("SQLException", e.getMessage());
        }
    }
}
