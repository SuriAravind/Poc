rem  Convert cobol data (text) file to xml.
rem  Dat2Xml only handles text files. It can not handle Cobol binary files
rem you should look at Data2Xml in the JRecord Project
java -cp ../../lib/cb2xml.jar;cb2xmlTest.jar tstBigCopybook.ReadFile
pause 