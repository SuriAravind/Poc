## Cobol To Csv 0.81.4

This project will convert a **Cobol Data** File To/From a **Csv** file.  This project
grew out of the [JRecord](https://sourceforge.net/projects/jrecord/) project.

There are no changes to the functionality or CobolToCsv, It has been changes to work with
JRecord 0.81.4.


### Downloads

* CobolToCsv_0.81.4.7z
                     
### Usage

    ../lib/Cobol2Csv.bat  -Copybook DTAR020.cbl ^
                          -Delimiter , ^
                          -Quote doublequote ^
                          -InputFileStructure Fixed_Length ^
                          -InputCharacterSet  cp037 ^                                                          
                          -I in/DTAR020.bin   ^
                          -O out/DTAR020.bin.csv

### Changes

#### Version 0.81.4

* Support for specifying field seperator in a \u0001 format
* Support for complex Occurs depending
* pickup JRecord 0.81.4


#### Version 0.81.2

* Updated for changes in JRecord 0.81.2



#### Version 0.81.1

* Initial version