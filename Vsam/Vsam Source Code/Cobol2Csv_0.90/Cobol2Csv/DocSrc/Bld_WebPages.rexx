/* Rexx 

	Rexx Program to build documentation
*/

        logo='sourceforge'

	b2h = 'F:\Work\Rexx\B2H\B2H.REXX'  /* home */
	
	param = "LOG=webCbl2Csv.log AUTOSPLIT=2 SPLITLINK=NO TOCRET=NO QUIET FRAMEPOS=TOP FRAMETOCOPT='TAB LOGO="""logo""" " 


	regina b2h '"wCbl2Csv.dcf" ('param')'
	

