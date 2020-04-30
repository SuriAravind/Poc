package com.p3.archon;

public class ExceltoDBMain {
    public static void main(String[] args) throws Exception {


        //GleneaglesGlobal
        //miot
        args = new String[]{"D:\\Excel to Db\\SYSTEM3.xlsx", "GleneaglesGlobal_Clinical", "root", "root"};
        String srcFilePath = "";

        String schemaName = "";
        String username = "";
        String password = "";
        if (args.length == 4) {
            srcFilePath = args[0];
            schemaName = args[1];
            username = args[2];
            password = args[3];
        } else {
            throw new Exception("Args length not match");
        }
        Processor processor = new Processor(srcFilePath, schemaName, username, password);
        processor.start();
    }
}
