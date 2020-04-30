## Cobol To Csv 0.81.1

This project will convert a **Cobol Data** File To/From a **Csv** file.  This project
grew out of the [JRecord](https://sourceforge.net/projects/jrecord/) project.


### Downloads

* CobolToCsv_0.81.1.zip
                     
### Usage

    ../lib/Cobol2Csv.bat  -Copybook DTAR020.cbl ^
                          -Delimiter , ^
                          -Quote doublequote ^
                          -InputFileStructure Fixed_Length ^
                          -InputCharacterSet  cp037 ^                                                          
                          -I in/DTAR020.bin   ^
                          -O out/DTAR020.bin.csv

### Changes

#### Version 0.81.1

* Initial version