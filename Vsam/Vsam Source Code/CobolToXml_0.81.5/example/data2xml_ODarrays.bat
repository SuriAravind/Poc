
java -jar ../lib/Cobol2Xml.jar -cobol in/ODArray01.cbl -fileOrganisation text ^
                             -input in/ODArray01.txt   ^
                             -output out/ODArray01.xml
java -jar ../lib/Cobol2Xml.jar -cobol in/invoice.cbl -fileOrganisation text ^
                             -input in/invoice.txt   ^
                             -output out/invoice.xml

java -jar ../lib/Cobol2Xml.jar -cobol in/ODArray61.cbl -fileOrganisation text ^
                             -input in/ODArray61.txt   ^
                             -output out/ODArray61.xml
java -jar ../lib/Cobol2Xml.jar -cobol in/ODArray61a.cbl -fileOrganisation text ^
                             -input in/ODArray61.txt   ^
                             -output out/ODArray61a.xml

pause                             

