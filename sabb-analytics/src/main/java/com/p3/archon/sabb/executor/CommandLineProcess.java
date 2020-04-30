package com.p3.archon.sabb.executor;

/*
 * $$HeadURL$$
 * $$Id$$
 *
 * CCopyright (c) 2015, P3Solutions . All Rights Reserved.
 * This code may not be used without the express written permission
 * of the copyright holder, P3Solutions.
 */

import com.p3.archon.sabb.utils.FileUtil;
import com.p3.archon.sabb.utils.OSIdentifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.TreeSet;


/**
 * @author Malik
 * <p>
 * General Utility to run a command line process
 */
public class CommandLineProcess {
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
    public TreeSet<String> logFiles = new TreeSet<String>();

    String errorlog;
    String outputlog;
    String toolPrefix;
    String tempOutputPath;

    public CommandLineProcess(String identifier, String jobId, String tempOutputPath) {
        this.toolPrefix = identifier;
        this.tempOutputPath = tempOutputPath;
        errorlog = tempOutputPath + File.separator + "log" + File.separator + "error_"
                + identifier + "_" + jobId + ".log";
        outputlog = tempOutputPath + File.separator + "log" + File.separator + "output_"
                + identifier + "_" + jobId + ".log";

    }

    public TreeSet<String> getLogFiles() {
        return logFiles;
    }

    public void setLogFiles(TreeSet<String> logFiles) {
        this.logFiles = logFiles;
    }

    public String getErrorlog() {
        return errorlog;
    }

    public void setErrorlog(String errorlog) {
        this.errorlog = errorlog;
    }

    public String getOutputlog() {
        return outputlog;
    }

    public void setOutputlog(String outputlog) {
        this.outputlog = outputlog;
    }

    /**
     * runs the command
     *
     * @param command the command
     * @return returns true if the script ran successfully
     * @throws Exception if there is an error
     */
    public boolean run(String[] command) throws Exception {
        return run(command, null);
    }

    /**
     * runs the command
     *
     * @param command                   the command
     * @param fileDirectoryToRunCommand the directory on the file system to where the command is to be run
     * @return returns true if the script ran successfully
     * @throws Exception if there is an error
     */
    public boolean run(String[] command, File fileDirectoryToRunCommand) throws Exception {
        try {
            LOGGER.debug("Command ", command);
            // do this for debugging to print out the command to be run
            int len = command.length;
            String newCommand = "";
            String sep = "";
            for (int i = 0; i < len; i++) {
                newCommand += sep + command[i];
                sep = " ";
            }
            //System.out.println("Running command =  ('" + newCommand + "')");

            // Now run the process
            Process p;
            if (fileDirectoryToRunCommand != null) {
                // System.out.println("Running command at File Directory = '" + fileDirectoryToRunCommand.getAbsolutePath() + "'");
                p = Runtime.getRuntime().exec(command, null, fileDirectoryToRunCommand);
            } else {
                p = Runtime.getRuntime().exec(command);
            }
            // System.out.println("Process successfully created.");
            logFiles.add(errorlog);
            logFiles.add(outputlog);
            // any error or output messages
            CommandLineStream errorGobbler = new CommandLineStream(p.getErrorStream(), "ERROR", errorlog, tempOutputPath);
            CommandLineStream outputGobbler = new CommandLineStream(p.getInputStream(), "OUTPUT", outputlog, tempOutputPath);
            errorGobbler.start();
            outputGobbler.start();
            int exitVal = p.waitFor();
            if (exitVal != 0) {
                LOGGER.debug("Return value error occurred. Value = " + exitVal);
                LOGGER.error("Error occured. For details refer file --> " + new File(checkReturnLogFileName()).getAbsolutePath());
                //System.out.println("Return value error occurred. Value = " + exitVal);
                // throw new Exception("Error occured. For details refer file --> \n" + new File(checkReturnLogFileName()).getAbsolutePath());
            } else {
                LOGGER.debug("Return value of process is " + exitVal);
            }
            //System.out.println("Return value of process is " + exitVal);
            return true;

        } catch (Exception e) {
            LOGGER.error("An Error has occurred = " + e.getMessage());
            // System.out.println("An Error has occurred = " + e.getMessage());
            // throw new Exception("An Error has occurred: " + e.getMessage(), e);
        }
        return true;

    }

    /**
     * runs the command
     *
     * @param command                   the command
     * @param fileDirectoryToRunCommand the directory on the file system to where the command is to be run
     * @param message
     * @return returns true if the script ran successfully
     * @throws Exception if there is an error
     */
    public boolean run(String command, File fileDirectoryToRunCommand, String message) throws Exception {
        try {
            // LOGGER.debug("Command ", command);
            // Now run the process
            Process p;
            if (fileDirectoryToRunCommand != null) {
                // System.out.println("Running command at File Directory = '" + fileDirectoryToRunCommand.getAbsolutePath() + "'");
                p = Runtime.getRuntime().exec(command, null, fileDirectoryToRunCommand);
            } else {
                p = Runtime.getRuntime().exec(command);
            }
            LOGGER.debug("Analysis Process Initiated for " + message);
            // System.out.println(message);
            logFiles.add(errorlog);
            logFiles.add(outputlog);

            CommandLineStream errorGobbler = new CommandLineStream(p.getErrorStream(), "ERROR", errorlog, tempOutputPath);
            CommandLineStream outputGobbler = new CommandLineStream(p.getInputStream(), "OUTPUT", outputlog, tempOutputPath);


            errorGobbler.start();
            outputGobbler.start();
            int exitVal = p.waitFor();
            if (exitVal != 0) {
                LOGGER.debug("Return value error occurred. Value = " + exitVal);
                LOGGER.error("Error occured. For details refer file -->" + new File(checkReturnLogFileName()).getAbsolutePath());
                //System.out.println("Return value error occurred. Value = " + exitVal);
                // throw new Exception("Error occured. For details refer file --> \n" + new File(checkReturnLogFileName()).getAbsolutePath());
            } else {
                LOGGER.debug("Analysis Completed for " + message);
                // FileUtil.deleteFile(errorlog);
            }
            // System.out.println("Return value of process is " + exitVal);

            return true;

        } catch (Exception e) {
            LOGGER.error("An Error has occurred = " + e.getMessage());
            //  System.out.println("An Error has occurred = " + e.getMessage());
            // throw new Exception("An Error has occurred: " + e.getMessage(), e);
        }
        return true;
    }

    private String checkReturnLogFileName() {
        switch (toolPrefix) {
            case "SABB":
                return outputlog;
            default:
                return errorlog;
        }
    }

}

/**
 * @author Malik
 * <p>
 * CommandLineStream - thread class to read the input and error message
 * streams from the command line
 */
class CommandLineStream extends Thread {
    InputStream is;
    String type;
    String file;
    Writer out;
    public final static char CR = (char) 0x0D;
    public final static char LF = (char) 0x0A;

    public final static String CRLF = "" + CR + LF;

    CommandLineStream(InputStream is, String type, String file, String tempOutputFolder) {
        this.is = is;
        this.type = type;
        this.file = file;
        try {
            FileUtil.checkCreateDirectory(tempOutputFolder + File.separator + "log");
            this.out = new OutputStreamWriter(new FileOutputStream(file, true));
        } catch (Exception e) {
        }
    }

    public void run() {
        try {
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            String line = null;
            int counter = 0;
            if (this.out != null) {
                if (OSIdentifier.checkOS()) {
                    while (type.equals("OUTPUT") && (line = br.readLine()) != null && ++counter < 4) {
                    }
                }
                while ((line = br.readLine()) != null) {
                    this.out.write(line);
                    this.out.write(CRLF);
                    out.flush();
                }
                out.flush();
                out.close();
            } else
                while ((line = br.readLine()) != null)
                    System.out.println(type + ">" + line);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
}
