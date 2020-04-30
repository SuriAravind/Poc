package com.p3.archon.sabb.processor;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;
import com.p3.archon.sabb.beans.ConnectionParameter;
import com.p3.archon.sabb.executor.CommandLineProcess;
import com.p3.archon.sabb.utils.CoreSettings;
import com.p3.archon.sabb.utils.FileUtil;
import com.p3.archon.sabb.utils.OSIdentifier;
import com.p3.archon.sabb.utils.Utility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Stream;

/**
 * Created by Suriyanarayanan K
 * on 25/02/20 5:35 PM.
 */
public class Processor {

    private String csvFileAbsolutePath;
    private static String SPACE = " ";
    private static String QUOTE = "\"";
    private String outputLocation;
    private String jarFileLocation;
    private String jarFileName;
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
    private String uuid;
    private String tempFolderPath;
    private int metadataThread;


    public Processor(String absolutePath) {
        this.csvFileAbsolutePath = absolutePath;
    }

    public void start() {
        List<ConnectionParameter> connectionParameterList = initiateConnectionParameterwithCsvFile();
        int THREAD_CAPACITY = getThreadCapacityCount();
        ForkJoinPool customThreadPool = new ForkJoinPool(THREAD_CAPACITY);
        List<ConnectionParameter> testedConnectionParameterList = testConnectionParameterList(connectionParameterList, customThreadPool);
        executeAnalsyisJarWithConnectionInput(testedConnectionParameterList, customThreadPool, connectionParameterList.size());
        deleteUnwantedFiles(tempFolderPath);

    }

    private void deleteUnwantedFiles(String tempFolderPath) {
        //File folder = new File(tempFolderPath );


        FileUtil.deleteDirectory(tempFolderPath);
    }

    private boolean executeAnalsyisJarWithConnectionInput(List<ConnectionParameter> testedConnectionParameterList, ForkJoinPool customThreadPool, int totalConnectionSize) {
        AtomicBoolean connectStatus = new AtomicBoolean(true);
        LOGGER.debug("Total Number of Valid Connection-->" + testedConnectionParameterList.size() + "/" + totalConnectionSize);
        try {
            customThreadPool.submit(
                    () -> testedConnectionParameterList.parallelStream()
                            .forEach(connectionParameter -> {


//                                String[] args = inputFrammerFoJAR(connectionParameter).split("\\s+");
//                                AnalysisProcessor processor = new AnalysisProcessor(args);
//                                processor.start();
                                uuid = UUID.randomUUID().toString();
                                String runfile = tempFolderPath + File.separator + uuid + (OSIdentifier.checkOS() ? ".bat" : ".sh");
                                CommandLineProcess clp = null;
                                try {
                                    commandFramerForJar(runfile, connectionParameter);
                                    clp = new CommandLineProcess("SABB", uuid, outputLocation + File.separator + "OUTPUT" + File.separator + connectionParameter.getAppName() + "_" + connectionParameter.getDbName() + "_" + connectionParameter.getSchemaName());
                                    clp.run((OSIdentifier.checkOS() ? "" : "sh ") + runfile, new File(tempFolderPath),
                                             connectionParameter.getAppName() + "_" + connectionParameter.getDbName() + "_" + connectionParameter.getSchemaName());
                                } catch (Exception e) {
                                    LOGGER.error("Exception ", e.getMessage());
                                }


                            })).get();
        } catch (InterruptedException e) {
            LOGGER.error("InterruptedException ", e.getMessage());
        } catch (ExecutionException e) {

            LOGGER.error("Execution Exception ", e.getStackTrace());

        }
        LOGGER.debug("Check this Output Folder :" + outputLocation + File.separator + "OUTPUT");
        return connectStatus.get();
    }


    private void commandFramerForJar(String runfile, ConnectionParameter connectionParameter)
            throws IOException {

        Writer cmd = new OutputStreamWriter(new FileOutputStream(new File(runfile)));
        cmd.write("cd "
                + Utility.excepeSpaceinPath(jarFileLocation + File.separator)
                + "\n");
        cmd.write("java -jar " + jarFileName + ".jar" + SPACE);
        cmd.write(inputFrammerFoJAR(connectionParameter));
        //cmd.write(SPACE + outputLocation);
        cmd.flush();
        cmd.close();

    }


    private List<ConnectionParameter> testConnectionParameterList(List<ConnectionParameter> connectionParameterList, ForkJoinPool customThreadPool) {
        List<ConnectionParameter> testedConnectionParameterList = new ArrayList<>();

        connectionParameterList.forEach(connectionParameter -> {
            if (connectionChecker(connectionParameter)) {
                testedConnectionParameterList.add(connectionParameter);

            }
        });
        return testedConnectionParameterList;
    }

    private List<ConnectionParameter> initiateConnectionParameterwithCsvFile() {
        List<ConnectionParameter> connectionParameterList = new ArrayList<>();
        try (Stream<String> stream = Files.lines(Paths.get(csvFileAbsolutePath))) {
            stream.skip(1).forEach(connectionParamterLine -> {
                String[] values = connectionParamterLine.split(",");
                if (values.length == 12) {
                    connectionParameterList.add(ConnectionParameter.builder()
                            .appName(values[0])
                            .dbType(values[1])
                            .hostName(values[2])
                            .port(Long.parseLong(values[3].trim().replace("\"", "")))
                            .dbName(values[4])
                            .schemaName(values[5])
                            .userName(values[6])
                            .password(values[7])
                            .ageByDate(values[8])
                            .metadataRequired(values[9])
                            .relationShipRequired(values[10])
                            .ageRequired(values[11])
                            .build());
                } else {
                    connectionParameterList.add(ConnectionParameter.builder().build());
                }
            });
        } catch (IOException e) {
            LOGGER.error("Exception" + e.getMessage());
        }
        return connectionParameterList;
    }

    private int getThreadCapacityCount() {
        int capacity = 0;
        CoreSettings props = new CoreSettings("config.properties");
        capacity = props.getIntegerValue("processCapacity", 1);
        outputLocation = props.getStringValue("outputLocation", null);
        tempFolderPath = outputLocation + File.separator + "TEMP";
        try {
            FileUtil.createDir(tempFolderPath);
        } catch (IOException e) {
            LOGGER.error("Exception ", e.getMessage());
        }
        jarFileLocation = props.getStringValue("jarFileLocation", null);
        jarFileName = props.getStringValue("jarName", null);
        metadataThread = props.getIntegerValue("metadataThread", 1);
        trimValues();
        int NUMBER_OF_CORES = Runtime.getRuntime().availableProcessors();

        /*try (InputStream input = Processor.class.getClassLoader().getResourceAsStream("config.properties")) {
            Properties prop = new Properties();
            if (input == null) {
                LOGGER.error("Sorry, unable to find config.properties");
            } else {
                prop.load(input);
                capacity = Integer.parseInt(prop.getProperty("processCapacity"));
                outputLocation = prop.getProperty("outputLocation");
                tempFolderPath = outputLocation + File.separator + "SABB";
                try {
                    FileUtil.createDir(tempFolderPath);
                } catch (IOException e) {
                    LOGGER.error("Exception ", e.getMessage());
                }
                //jarFileLocation = prop.getProperty("jarFileLocation");
                //jarFileName = prop.getProperty("jarName");
                //metadataThread = prop.getProperty("metadataThread");
                trimValues();
                int NUMBER_OF_CORES = Runtime.getRuntime().availableProcessors();
                if (capacity > NUMBER_OF_CORES) {
                    LOGGER.error("Thread capacity value(" + capacity + ") specified in config.properties is greater than system core processor(" + NUMBER_OF_CORES + ")");
                    LOGGER.info("We will set thread capacity as " + NUMBER_OF_CORES);
                    capacity = NUMBER_OF_CORES;
                }
            }
        } catch (IOException ex) {
            LOGGER.error("Exception", ex.getMessage());
        }*/
        return capacity;
    }

    private void trimValues() {
        outputLocation = outputLocation.trim();
        tempFolderPath = tempFolderPath.trim();
        jarFileLocation = jarFileLocation.trim();
        jarFileName = jarFileName.trim();

    }

    private String inputFrammerFoJAR(ConnectionParameter connectionParameter) {

        return QUOTE + connectionParameter.getAppName() + QUOTE +
                // SPACE + QUOTE + connectionParameter.getDbType() + QUOTE +
                SPACE + connectionParameter.getDbType() +
                // SPACE + QUOTE + connectionParameter.getHostName() + QUOTE +
                SPACE + connectionParameter.getHostName() +
                //SPACE + QUOTE + connectionParameter.getPort() + QUOTE +
                SPACE + connectionParameter.getPort() +
                //SPACE + QUOTE + connectionParameter.getDbName() + QUOTE +
                SPACE + connectionParameter.getDbName() +
                // SPACE + QUOTE + connectionParameter.getSchemaName() + QUOTE +
                SPACE + connectionParameter.getSchemaName() +
                // SPACE + QUOTE + connectionParameter.getUserName() + QUOTE +
                SPACE + connectionParameter.getUserName() +
                // SPACE + QUOTE + connectionParameter.getPassword() + QUOTE +
                SPACE + connectionParameter.getPassword() +
                // SPACE + QUOTE + connectionParameter.getAgeByDate() + QUOTE +
                SPACE + connectionParameter.getAgeByDate() +
                // SPACE + QUOTE + outputLocation + QUOTE +
                SPACE + outputLocation +
                SPACE + connectionParameter.getMetadataRequired() +
                SPACE + connectionParameter.getRelationShipRequired() +
                SPACE + connectionParameter.getAgeRequired();
    }

    private boolean connectionChecker(ConnectionParameter connectionParameter) {
        Connection connection;
        boolean status = false;
        String urlResult = "";
        try {
            if (connectionParameter.getDbType() != null) {
                switch (connectionParameter.getDbType().toString()) {
                    case "SQL":
                        Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver").newInstance();
                        String url = "jdbc:sqlserver://" + connectionParameter.getHostName() + ":" + connectionParameter.getPort() + ";databaseName=" + connectionParameter.getDbName() + ";user=" + connectionParameter.getUserName()
                                + ";password=" + connectionParameter.getPassword();
                        connection = DriverManager.getConnection(url);
                        status = (connection != null) ? true : false;
                        break;
                    case "ORACLE":
                        Class.forName("oracle.jdbc.driver.OracleDriver");
                        urlResult = "jdbc:oracle:thin:@" + connectionParameter.getHostName() + ":" + connectionParameter.getPort() + ":" + connectionParameter.getDbName();
                        connection = DriverManager.getConnection(urlResult, connectionParameter.getUserName(), connectionParameter.getPassword());
                        status = (connection != null) ? true : false;
                        break;
                    case "MYSQL":
                        MysqlDataSource sourceMysql = new MysqlDataSource();
                        Class.forName("com.mysql.jdbc.Driver");
                        sourceMysql.setDatabaseName(connectionParameter.getDbName());
                        sourceMysql.setPort(Integer.parseInt(String.valueOf(connectionParameter.getPort())));
                        sourceMysql.setUser(connectionParameter.getUserName());
                        sourceMysql.setPassword(connectionParameter.getPassword());
                        sourceMysql.setServerName(connectionParameter.getHostName());
                        connection = sourceMysql.getConnection();
                        status = (connection != null) ? true : false;
                        break;
                    default:
                        break;
                }
            } else {
                status = false;
            }
        } catch (InstantiationException e) {
            LOGGER.error("Exception" + e.getMessage());

        } catch (IllegalAccessException e) {
            LOGGER.error("Exception" + e.getMessage());

        } catch (ClassNotFoundException e) {
            LOGGER.error("Exception" + e.getMessage());

        } catch (SQLException e) {
            LOGGER.error("Exception" + e.getMessage());

        }
        return status;
    }
}
