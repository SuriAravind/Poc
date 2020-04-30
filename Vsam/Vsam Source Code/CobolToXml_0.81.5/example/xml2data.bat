rem create input file
java -jar ../lib/Cobol2Xml.jar -cobol DTAR020.cbl -font cp037 -fileOrganisation FixedWidth -input in/DTAR020.bin -output in/DTAR020.xml

rem convert xml back to Cobol Data file
java -jar ../lib/Xml2Cobol.jar ^
          -cobol DTAR020.cbl -font cp037 -fileOrganisation FixedWidth ^
          -input in/DTAR020.xml ^
          -output out/DTAR020_A.bin
pause           
