      *
       01  REDEFINES-RECORD                Pic X(300).
      *                                                       001 - 400 00260003
       01  FIRST-TYPE         REDEFINES   REDEFINES-RECORD.             00270012
         02  REDEFINES-RECORD-KEY.                                      00110000
      *                                                       001 - 002 00120000
           04  REDEFINES-RECORD-TYPE       PIC  X(02).                  00130000
      *                                                       003 - 009 00140000
           04  SOME-FIELD-NUMBER           PIC  X(07).                  00150000
      *                                                       010 - 015 00160000
           04  SOME-FIELD-NUMBER-2         PIC  X(06).                  00170000
      *                                                       016 - 019 00180000
           04  SOME-FIELD-NUMBER-3         PIC  9(04).                  00190000
      *                                                       020 - 027 00200000
           04  CREATION-DATE  PIC  X(08).                               00210000
      *                                                       028 - 033 00300001
         02  SOME-FIELD1                   PIC  X(06).                  00310001
      *                                                       034 - 034 00320001
         02  SOME-FIELD2                   PIC  X(01).                  00330001
      *                                                       035 - 035 00340001
         02  SOME-FIELD3                   PIC  X(01).                  00350001
      *                                                       036 - 036 00360001
         02  SOME-FIELD4                   PIC  X(01).                  00370001
      *                                                       037 - 053 00380001
         02  SOME-FIELD5                   PIC  X(17).                  00390001
      *                                                       054 - 059 00400001
         02  SOME-FIELD6                   PIC  X(06).                  00410001
      *                                                       059 - 063 00420001
         02  SOME-FIELD7                   PIC  X(04).                  00430001
      *                                                       064 - 071 00440001
         02  SOME-FIELD8                   PIC  X(08).                  00450001
      *                                                       072 - 079 00460001
         02  SOME-FIELD9                   PIC  X(08).                  00470001
      *                                                       080 - 087 00480001
         02  SOME-FIELD10                  PIC  X(08).                  00490007
      *                                                       088 - 095 00500001
         02  SOME-FIELD11                  PIC  X(08).                  00510007
      *                                                       096 - 102 00520001
         02  SOME-FIELD12                  PIC  9(07).                  00530007
      *                                                       103 - 108 00540001
         02  SOME-FIELD13                  PIC  X(06).                  00550007
      *                                                       109 - 112 00560001
         02  SOME-FIELD14                  PIC  X(04).                  00570007
      *                                                       113 - 114 00580001
         02  SOME-FIELD15                  PIC  X(02).                  00590007
      *                                                       115 - 117 00600001
         02  SOME-FIELD16                  PIC  X(03).                  00610007
      *                                                       118 - 118 00620001
         02  SOME-FIELD17                  PIC  X(01).                  00630007
      *                                                       119 - 143 00640001
         02  SOME-FIELD18                  PIC  X(25).                  00650007
      *                                                       144 - 154 00660001
         02  SOME-FIELD19                  PIC  X(11).                  00670007
      *                                                       155 - 184 00680001
         02  SOME-FIELD20                  PIC  X(30).                  00690007
      *                                                       185 - 185 00700016
         02  SOME-FIELD21                  PIC  X(01).                  00710016
      *                                                       186 - 401 00720016
         02  FILLER                        PIC X(215).                  00730016
                                                                        00740001
                                                                        00750001
      *                                                       001 - 400 03120004
       01  SECOND-TYPE                REDEFINES   REDEFINES-RECORD.     03130009
      *                                                       001 - 027 03140001
         02  FILLER                        PIC  X(27).                  03150001
      *                                                       028 - 031 03160001
         02  ANOTHER-FIELD1                PIC  9(04).                  03170015
      *                                                       032 - 032 03180001
         02  ANOTHER-FIELD2                PIC  X(01).                  03190004
      *                                                       033 - 033 03200001
         02  ANOTHER-FIELD3                PIC  X(01).                  03210009
      *                                                       034 - 034 03220001
         02  ANOTHER-FIELD4                PIC  X(01).                  03230009
      *                                                       035 - 042 03240001
         02  ANOTHER-FIELD5                PIC  X(08).                  03250009
      *                                                       043 - 047 03260001
         02  ANOTHER-FIELD6                PIC  9(05).                  03270004
      *                                                       048 - 052 03280001
         02  ANOTHER-FIELD7                PIC  9(05).                  03290004
      *                                                       053 - 064 03300010
         02  ANOTHER-FIELD8                PIC  X(12).                  03310011
      *                                                       065 - 076 03320010
         02  ANOTHER-FIELD9                PIC  X(12).                  03330011
      *                                                       077 - 088 03340010
         02  ANOTHER-FIELD10               PIC  X(12).                  03350011
      *                                                       089 - 100 03360010
         02  ANOTHER-FIELD11               PIC  X(12).                  03370011
      *                                                       101 - 170 03380010
         02  ANOTHER-FIELD12               PIC  X(70).                  03390004
      *                                                       171 - 400 03400010
         02  FILLER                        PIC X(230).                  03410010