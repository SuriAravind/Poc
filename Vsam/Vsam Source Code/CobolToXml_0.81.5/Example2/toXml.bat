java -jar ../lib/Cobol2Xml.jar -cobol Xmpl.cbl -fileOrganisation text  ^
                                 -split 01 ^
                                 -recordSelection FIRST-TYPE       REDEFINES-RECORD-TYPE=T1 ^
                                 -recordSelection SECOND-TYPE      REDEFINES-RECORD-TYPE=T2 ^
                             -input  In.txt   ^
                             -output out1.xml 

java -jar ../lib/Cobol2Xml.jar -cobol Xmpl.cbl -fileOrganisation text  ^
                                 -split 01 ^
                                 -recordSelection FIRST-TYPE       REDEFINES-RECORD-TYPE=T1 ^
                                 -recordSelection SECOND-TYPE      REDEFINES-RECORD-TYPE=T2  -recordParent SECOND-TYPE  FIRST-TYPE ^
                             -input  In.txt   ^
                             -output out2.xml 
java -jar ../lib/Cobol2Xml.jar -cobol Xmpl2.cbl -fileOrganisation text  ^
                                 -split 01 ^
                                 -recordSelection FIRST-TYPE       REDEFINES-RECORD-TYPE=T1 ^
                                 -recordSelection SECOND-TYPE      REDEFINES-RECORD-TYPE=T2  -recordParent SECOND-TYPE  FIRST-TYPE ^
                             -input  In.txt   ^
                             -output out3.xml 
                             
pause