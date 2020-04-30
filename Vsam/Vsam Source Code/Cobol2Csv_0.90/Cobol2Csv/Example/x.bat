java -jar ../lib/Cobol2Csv.jar ^
                  -C DTAR020.cbl  -FS Fixed_Length    -IC CP037 ^
                  -Q DoubleQuote  -D , ^
                  -I in/DTAR020.bin  -O out/o_DTAR020_a.csv
pause