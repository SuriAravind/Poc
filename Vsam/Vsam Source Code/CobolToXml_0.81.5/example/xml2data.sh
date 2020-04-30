#! /bin/sh
#set ($dhash = '##')
$dhash   Convert Xml to Cobol
##
##   Author Bruce Martin

# create input Xml file
java -jar ../lib/Cobol2Xml.jar  -cobol DTAR020.cbl -font cp037 -fileOrganisation FixedWidth -input in/DTAR020.bin -output in/DTAR020.xml

# Convert Xml to Cobol Data File
java -jar ../lib/Xml2Cobol.jar   \
                        -cobol DTAR020.cbl -font cp037 -fileOrganisation FixedWidth  \
                        -input in/DTAR020.xml \
                        -output out/DTAR020_A.bin
          
