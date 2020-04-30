package com.p3.archon.sample_vsam;

import java.io.BufferedReader;

import com.ibm.jzos.ZFile;
@SuppressWarnings("unused")
public class Ksds_file_reader {
	
	static BufferedReader bf;
	
	public static void main(String args[]) throws Exception
	{

	        args=new String[]{"/Users/apple/Documents/Projects/VSAM/cobrix-master/examples/example_data/companies_data/COMP.DETAILS.SEP30.DATA.dat"};
	        // input is expected to a key for KSDS file
	        String key = args[0];
	        // update records in the file
	       readRecords(key);
	}

	public static void readRecords(String ksds_key) throws Exception {
    int nRead;
    int lrecl =242;
    int keyLen = 10;

    System.out.println("fileName=" + "MATEDKD.AVINASH.KSDSFILE" );
    
    ZFile zfile = new ZFile("//DD:KSDSFILE", "r");
    byte[] recBuf = new byte[lrecl];

    // record keys
    byte[] key = ksds_key.getBytes(ZFile.DEFAULT_EBCDIC_CODE_PAGE);
		
    try {
        // check if the file is empty
        zfile.rewind();
        nRead = zfile.read(recBuf);

        if (nRead != -1) {
            // position the record
            boolean located = zfile.locate(key, 0, keyLen, ZFile.LOCATE_KEY_EQ);
            System.out.println("located=" + located);

            // read the record
            nRead = zfile.read(recBuf);
            
            String ksds_row = new String(recBuf);
            System.out.println("nRead=" + nRead + " Record with Primary Key "+ ksds_key + ksds_row);
            employee ee = new employee(recBuf);
    		System.out.println(ee.toString());	
    		
//            // build data to update
//            String pk = record50000005.substring(0, 8);
//            byte[] updateRecord = padToLength(pk + "This record is modified", lrecl).getBytes(ZFile.DEFAULT_EBCDIC_CODE_PAGE);
//
//            // update the record with the new data
//            zfile.update(updateRecord);
//            System.out.println("Record 50000005 updated to=" + new String(updateRecord));
        }
        else {
            System.out.println("nRead=" + nRead);
        }
    }
    finally {
        zfile.close();
    }
}
	private static void usage() {
    System.out.println("ReadKsdsRecords - Demonstrates how to read records in a VSAM dataset.");
    System.out.println("Usage:");
    System.out.println("\tjava com.ibm.jzos.sample.vsam.file.ReadKsdsRecords file");
    System.out.println("\tfile: The name of the VSAM dataset to read");
    System.out.println("\t\tExample file");
    System.out.println("\t\tVSAM Dataset & Key: ");
    System.out.println();
    System.exit(0);
	}
}

