package com.p3.archon.sample_vsam;

import com.ibm.jzos.fields.CobolDatatypeFactory;

@SuppressWarnings("unused")
public class employee {
	final CobolDatatypeFactory ksds_coboldatafactory = new CobolDatatypeFactory();
//	01 WS-EMPLOYEE.                                           
//	**********************************************************
//	    EMPLOYEE KEY                                          
//	**********************************************************
//	  02 EMP-KEY.                                             
	     String  WS_EMP_ID;   //      PIC X(10).                      
//	**********************************************************
//	    EMPLOYEE DETAILS                                      
//	**********************************************************
//	  02 EMP-DTL.                                             
	     String EMP_NAME  ;        //PIC X(20).                      
	     String EMP_ADDRESS;       //PIC X(30).                                          
	     String EMP_CITY   ;       //PIC X(20).                      
	     String EMP_ST     ;       //PIC X(20).                      
	     String EMP_COUNTRY;       //PIC X(20).                      
	     int EMP_ZIP    ;          //PIC 9(06) COMP-3.               
	     int EMP_DOB    ;          //PIC 9(08).                      
//	**********************************************************
//	   EMPLOYEE POLICY DETAILS                                
//	**********************************************************
//	  02 EMP-PLCY-PLAN.                                       
	     String[][] EMP_COVERAGE =  new String[5][5];       // OCCURS 5 TIMES.
//	     String EMP_PLCY;          // PIC X(06).                      
//	     String EMP_DIV;           //PIC 9(04) BINARY.               
//	     String EMP_CLS;           //PIC 9(04) BINARY.               
//	     String EMP_ACTIVE_DATE;   //PIC 9(08) COMP-3.               
//	     String EMP_CANCEL_DATE;   //PIC 9(08) COMP-3.               
//	  02 FILLER               PIC X(10).                      
	public employee(byte[] recBuf) {
	super();
	System.out.println(recBuf.length);
	
//	WS_EMP_ID = String. valueOf(recBuf).substring(0, 10);
	WS_EMP_ID 	= (ksds_coboldatafactory.getStringField(10)).getString(recBuf);
	EMP_NAME 	= (ksds_coboldatafactory.getStringField(20)).getString(recBuf);
	EMP_ADDRESS = (ksds_coboldatafactory.getStringField(30)).getString(recBuf);
	EMP_CITY 	= (ksds_coboldatafactory.getStringField(20)).getString(recBuf);
	EMP_ST 		= (ksds_coboldatafactory.getStringField(20)).getString(recBuf);
	EMP_COUNTRY = (ksds_coboldatafactory.getStringField(20)).getString(recBuf);
	EMP_ZIP 	= (ksds_coboldatafactory.getPackedDecimalAsIntField(6,false)).getInt(recBuf);
	EMP_DOB     = (ksds_coboldatafactory.getExternalDecimalAsIntField(8, false).getInt(recBuf));
	for(int i=0;i<5;i++)
	{ 
		EMP_COVERAGE[i][0] = (ksds_coboldatafactory.getStringField(6)).getString(recBuf);
		EMP_COVERAGE[i][1] = String.valueOf((ksds_coboldatafactory.getBinaryAsIntField(4,false)).getInt(recBuf));
		EMP_COVERAGE[i][2] = String.valueOf((ksds_coboldatafactory.getBinaryAsIntField(4,false)).getInt(recBuf));
		EMP_COVERAGE[i][3] = String.valueOf((ksds_coboldatafactory.getPackedDecimalAsIntField(8,false).getInt(recBuf)));
		EMP_COVERAGE[i][4] = String.valueOf((ksds_coboldatafactory.getPackedDecimalAsIntField(8,false).getInt(recBuf)));
	}
}
	@Override
	public String toString() {
		return "employee [WS_EMP_ID=" + WS_EMP_ID + ", EMP_NAME=" + EMP_NAME + ", EMP_ADDRESS=" + EMP_ADDRESS
				+ ", EMP_CITY=" + EMP_CITY + ", EMP_ST=" + EMP_ST + ", EMP_COUNTRY=" + EMP_COUNTRY + ", EMP_ZIP="
				+ EMP_ZIP + ", EMP_DOB=" + EMP_DOB + "EMP-PLCY=" + EMP_COVERAGE[0][0] +
				"EMP-DIV=" + EMP_COVERAGE[0][1] + "EMP-CLS" + EMP_COVERAGE[0][2] + 
				"EMP-ACTIVE-DATE="+ EMP_COVERAGE[0][3] + "EMP-CANCEL-DATE=" + EMP_COVERAGE[0][4] + "]";
	}

	    
}
