#! /bin/sh
##   Convert cobol data diles to Xml
##
##   Author Bruce Martin

java -jar ../lib/Cobol2Xml.jar -? 
java -jar ../lib/Cobol2Xml.jar  \
        -cobol DTAR020.cbl -font cp037 -fileOrganisation FixedWidth \
        -input in/DTAR020.bin \
        -output out/DTAR020_A.xml
java -jar ../lib/Cobol2Xml.jar   \
        -cobol DTAR020.cbl -font cp037 -fileOrganisation FixedWidth -dropCopybookName true \
        -input in/DTAR020.bin \
        -output out/DTAR020_B.xml 
        
## mainframe VB, Occurs depending
java -jar ../lib/Cobol2Xml.jar -cobol FCUSDAT.cbl -font cp037 -fileOrganisation Mainframe_VB  \
                             -input  in/ZOS.FCUSTDAT_150.vb.bin   \
                             -output out/ZOS.FCUSTDAT_150_Bat.xml 

##   Multi Record File 
java -jar ../lib/Cobol2Xml.jar -cobol amsPoDownload.cbl -fileOrganisation text  \
                                 -split 01 \
                                 -recordSelection PO-Record       Record-Type=H1 \
                                 -recordSelection Product-Record  Record-Type=D1 \
                                 -recordSelection Location-Record Record-Type=S1 \
                             -input  in/Ams_PODownload_20041231.txt   \
                             -output out/Ams_PODownload_20041231_bat_Flat.xml 

##   Multi Record File, Define the Record-Hierarchy to get a proper Tree layout of the Xml
java -jar ../lib/Cobol2Xml.jar -cobol amsPoDownload.cbl -fileOrganisation text  \
                                 -split 01 \
                                 -recordSelection PO-Record        Record-Type=H1 \
                                 -recordSelection Product-Record   Record-Type=D1  -recordParent Product-Record  PO-Record \
                                 -recordSelection Location-Record  Record-Type=S1  -recordParent Location-Record Product-Record  \
                             -input  in/Ams_PODownload_20041231.txt   \
                             -output out/Ams_PODownload_20041231_bat_Tree.xml 

        
           
##           /* use a 'cb2xml' copybook  */
java -jar ../lib/Cobol2Xml.jar -cb2xml DTAR020.cbxml -font cp037 -fileOrganisation FixedWidth \
                           -input in/DTAR020.bin  \
                           -output out/DTAR020_A.xml -mainXmlTag copybook          
java -jar ../lib/Cobol2Xml.jar -cobol AmsLocation.cbl  \
                          -input in/Ams_LocDownload_20041228_Extract.txt \
                          -output out/Ams_LocDownload_20041228_Extract.xml

## use the  -tagFormat to format the Xml-Tags using Camel-Case or Underscore (instead of Cobols -)
java -jar ../lib/Cobol2Xml.jar -cobol DTAR020.cbl -font cp037 -fileOrganisation FixedWidth \
                         -input in/DTAR020.bin \
                         -output out/DTAR020_CC.xml             -tagFormat CamelCase
java -jar ../lib/Cobol2Xml.jar -cobol DTAR020.cbl -font cp037 -fileOrganisation FixedWidth \
                         -input in/DTAR020.bin \
                         -output out/DTAR020_UnderScore.xml    -tagFormat _

           