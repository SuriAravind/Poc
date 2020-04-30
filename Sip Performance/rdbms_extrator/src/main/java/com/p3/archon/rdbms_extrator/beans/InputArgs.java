package com.p3.archon.rdbms_extrator.beans;

import lombok.*;
import org.apache.log4j.Logger;
import org.kohsuke.args4j.Option;

/**
 * Created by Suriyanarayanan K
 * on 29/02/20 12:57 PM.
 */

@Builder
@Data
@Getter
@Setter
@ToString
public class InputArgs {


    private Logger LOGGER = Logger.getLogger(this.getClass().getName());
    @Option(name = "-h", aliases = {"--host"}, usage = "Host name for connection", required = true)
    private String host;
    @Option(name = "-p", aliases = {"--port"}, usage = "Port no", required = false)
    private String port;
    @Option(name = "-d", aliases = {"--database"}, usage = "Database name for connection", required = true)
    private String database;
    @Option(name = "-s", aliases = {"--schemaName"}, usage = "Schema name for connection", required = true)
    private String schemaName;
    @Option(name = "-u", aliases = {"--userName"}, usage = "Username for connection", required = true)
    private String user;
    @Option(name = "-pw", aliases = {"--password"}, usage = "Password for connection", required = true)
    private String pass;
    @Option(name = "-ds", aliases = {"--databaseServer"}, usage = "Database server for connection", required = true)
    private String databaseServer;
    @Option(name = "-o", aliases = {"--output"}, usage = "Path to generate output", required = true)
    private String outputPath;
    @Option(name = "-ot", aliases = {"--outputType"}, usage = "Extension for output files", required = true)
    private String outputType;
    @Option(name = "-sip", aliases = {"--sipInputParams"}, usage = "Input params for sip files", required = false)
    private String sipInputFile;

    @Option(name = "-tl", aliases = {"--tablelistInputParams"}, usage = "Input params for table filtering", required = false)
    private String tableList;

    @Option(name = "-q", aliases = {"--isQuery"}, usage = "include if passing query", depends = "-ql")
    private boolean query;

    @Option(name = "-ql", aliases = {"--queryList"}, usage = "query list")
    private String queryListInput;

    @Option(name = "-ert", aliases = {"--isErt"}, usage = "include if ert flow is needed", depends = "-sip")
    private boolean ert;

    private String inputJson;

    @Option(name = "-sps", aliases = {"--splitBy Size"}, usage = "include if split by size needed in (MB)", required = false)
    private int splitBySize = 100;

    @Option(name = "-spr", aliases = {"--splitByRecord"}, usage = "include if split by record ", required = false)
    private int splitByRecord = 10000;

    @Option(name = "-tc", aliases = {"--threadCount"}, usage = "input for span the  thead  ", required = true)
    private int threadCount;

    @Option(name = "-swt", aliases = {"--splitwiseRecordSpanInThread"}, usage = "split wise record span in thread", required = true)
    private int splitwisethread;

    @Option(name = "-ex", aliases = {"--isextra"}, usage = "include if select query for main table must include sub child filtering")
    private boolean extaConditon = false;

    @Option(name = "-rc", aliases = {"--isResetConnection"}, usage = "include if reset the connection")
    public boolean isResetConnection = false;

    @Option(name = "-wt", aliases = {"--waitTime"}, usage = "wait Time for thread(in sec ) ",required = true)
    public long waitTime=1000;

}
