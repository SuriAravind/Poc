package com.p3.archon.analysis_core.processor;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;
import com.p3.archon.analysis_core.beans.ConnectionParameter;
import com.p3.archon.analysis_core.enums.Type;
import org.apache.commons.lang.StringUtils;
import org.postgresql.ds.PGPoolingDataSource;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DSConnectionManager {


    public Connection RDBMSConnection(ConnectionParameter dto)
            throws SQLException, ClassNotFoundException, InstantiationException, IllegalAccessException {

        Connection connection = null;

        String userName = null;
        String password = null;

        if ("RDBMS".equals(dto.getCategories())) {//			RDBMSConnectionInfoDTO rdbmsConnectionDetails = dto.getRdbmsConnectionDetails();

            String host = dto.getHostName();
            int port = dto.getPort();
            String database = dto.getDbName();
            Type type = dto.getType();
            userName = dto.getUserName();
            password = dto.getPassword();

            /**
             * TODO:password encryption..
             */
            switch (type) {

                case MYSQL:
                    MysqlDataSource sourceMysql = new MysqlDataSource();
                    Class.forName("com.mysql.jdbc.Driver");
                    sourceMysql.setDatabaseName(database);
                    sourceMysql.setPort(port);
                    sourceMysql.setUser(userName);
                    sourceMysql.setPassword(password);
                    sourceMysql.setServerName(host);
                    connection = sourceMysql.getConnection();
                    break;
                case SQL:
                    Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver").newInstance();
                    String url = "jdbc:sqlserver://" + host + ":" + port + ";databaseName=" + database + ";user=" + userName
                            + ";password=" + password;
                    connection = DriverManager.getConnection(url);
                    break;

                case ORACLE:
                    Class.forName("oracle.jdbc.driver.OracleDriver");
                    String urlResult = "jdbc:oracle:thin:@" + host + ":" + port + ":" + database;
                    connection = DriverManager.getConnection(urlResult, userName, password);

//                    PoolDataSource pds = PoolDataSourceFactory.getPoolDataSource();
//                    pds.setConnectionFactoryClassName("oracle.jdbc.pool.OracleDataSource");
//                    pds.setURL(urlResult);
//                    pds.setUser(userName);
//                    pds.setPassword(password);
//                    pds.setValidateConnectionOnBorrow(true);
//                    connection = pds.getConnection();
                    break;
                case ORACLE_SERVICE:
                    Class.forName("oracle.jdbc.driver.OracleDriver");
                    String urlOracleService = "jdbc:oracle:thin:@//" + host + ":" + port + "/" + database;
                    connection = DriverManager.getConnection(urlOracleService, userName, password);
                    break;
                case DB2:

                    Class.forName("com.ibm.db2.jcc.DB2Driver");
                    connection = DriverManager.getConnection("jdbc:db2://" + host + ":" + port + "/" + database, userName,
                            password);
                    break;
                case POSTGRESQL:
                    PGPoolingDataSource sourcepostgresql = new PGPoolingDataSource();
                    Class.forName("org.postgresql.Driver");
                    sourcepostgresql.setDatabaseName(database);
                    sourcepostgresql.setPortNumber(port);
                    if (!StringUtils.isEmpty(userName)) {
                        sourcepostgresql.setUser(userName);
                    }
                    if (!StringUtils.isEmpty(password)) {
                        sourcepostgresql.setPassword(password);
                    }
                    sourcepostgresql.setServerName(host);
                    connection = sourcepostgresql.getConnection();

                    break;

                case SYBASE:
                    Class.forName("net.sourceforge.jtds.jdbc.Driver");
                    connection = DriverManager.getConnection(
                            "jdbc:jtds:sybase://" + host + ":" + port + ";DatabaseName=" + database, userName, password);
                    break;
                case AS400:
                    Class.forName("com.ibm.as400.access.AS400JDBCDriver");
                    String urlAS400 = "jdbc:as400://" + host + ":" + port + "/" + database;
                    connection = DriverManager.getConnection(urlAS400);
                    break;
                default:
                    break;
            }
        }
        return connection;
    }
}
