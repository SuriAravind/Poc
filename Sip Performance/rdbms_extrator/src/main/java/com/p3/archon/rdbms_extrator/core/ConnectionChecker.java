package com.p3.archon.rdbms_extrator.core;

import com.p3.archon.rdbms_extrator.beans.InputArgs;
import org.apache.log4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.text.MessageFormat;

/**
 * Created by Suriyanarayanan K
 * on 29/02/20 12:48 PM.
 */
public class ConnectionChecker {

    Connection con;
    private Logger LOGGER = Logger.getLogger(this.getClass().getName());

    public String getConnectionString(InputArgs inputArgs) {
        Object[] replaceValues = new Object[]{inputArgs.getHost(), inputArgs.getPort(), inputArgs.getDatabase()};
        return MessageFormat.format(getConnectionURL(inputArgs.getDatabaseServer().toLowerCase()), replaceValues);
    }

    public String getConnectionURL(String type) {
        switch (type.toLowerCase()) {
            case "teradata":
                return "jdbc:teradata://{0}/{2}";
            case "sqlwinauth":
                return "jdbc:jtds:sqlserver://localhost/{2};appName=SchemaCrawler;useCursors=true;useNTLMv2=true;domain={0};";
            case "sql":
                return "jdbc:jtds:sqlserver://{0}:{1}/{2};appName=SchemaCrawler;useCursors=true;";
            case "oracle":
                return "jdbc:oracle:thin:@{0}:{1}:{2}";
            case "oracleservice":
                return "jdbc:oracle:thin:@//{0}:{1}/{2}";
            case "mysql":
                return "jdbc:mysql://{0}:{1}/{2}?nullNamePatternMatchesAll=true&logger=Jdk14Logger&dumpQueriesOnException=true&dumpMetadataOnColumnNotFound=true&maxQuerySizeToLog=4096";
            case "db2":
                return "jdbc:db2://{0}:{1}/{2};retrieveMessagesFromServerOnGetMessage=true;";
            case "sybase":
                return "jdbc:jtds:sybase://{0}:{1}/{2}";
            case "postgresql":
                return "jdbc:postgresql://{0}:{1}/{2}?ApplicationName=SchemaCrawler";
            case "as400":
                return "jdbc:as400://{0}:{1}/{2};translate hex=character;translate binary=true";
            case "as400noport":
                return "jdbc:as400://{0}/{2};translate hex=character;translate binary=true";
            default:
                return null;
        }
    }

    public String getForName(String serverType) {
        String driverClass = "";
        switch (serverType.toLowerCase()) {
            case "mysql":
                driverClass = "com.mysql.jdbc.Driver";
                break;
            case "sql":
                driverClass = "net.sourceforge.jtds.jdbc.Driver";
                break;
            case "teradata":
                driverClass = "com.teradata.jdbc.teradriver";
                break;
            case "oracle":
            case "oracleservice":
                driverClass = "oracle.jdbc.driver.OracleDriver";
                break;
            case "db2":
                driverClass = "com.db2.jdbc.driver";
                break;
            case "sybase":
                driverClass = "com.sybase.jdbc4.jdbc.sybdriver";
                break;
            case "postgresql":
                driverClass = "com.postgresql.jdbc.driver";
                break;
            case "as400":
            case "as400noport":
                driverClass = "com.ibm.as400.access.AS400JDBCDriver";
                break;
        }
        if (driverClass.equalsIgnoreCase("")) {

            LOGGER.error("Server Type not matched: " + serverType);

            System.exit(2);
        }
        return driverClass;
    }

    public Connection checkConnection(InputArgs inputArgs) {

        try {
            Class.forName(getForName(inputArgs.getDatabaseServer().toLowerCase()));
            con = DriverManager.getConnection(getConnectionString(inputArgs), inputArgs.getUser(), inputArgs.getPass());
        } catch (SQLException e) {
            LOGGER.error("SQLException:" + e.getMessage());
        } catch (ClassNotFoundException e) {
            LOGGER.error("ClassNotFoundException:" + e.getMessage());
        }
        return con;
    }
}
