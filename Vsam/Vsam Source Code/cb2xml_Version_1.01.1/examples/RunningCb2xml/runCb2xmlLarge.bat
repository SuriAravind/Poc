rem java -jar ../../lib/cb2xml.jar -cobol LargeCopybookX.cbl -xml LargeCopybookX.cbl.xml  -stacksize normal > error.txt 2>&1
rem java -jar ../../lib/cb2xml.jar -cobol LargeCopybookX.cbl -xml LargeCopybookX.cbl.xml -stacksize 30   > error.txt 2>&1
java -jar ../../lib/cb2xml.jar -cobol LargeCopybookX.cbl -xml LargeCopybookX.cbl.xml   > error.txt 2>&1
pause