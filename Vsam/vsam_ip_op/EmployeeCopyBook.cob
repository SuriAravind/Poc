      ****************************************************************************
      *                                                                          *
      * Copyright 2018 ABSA Group Limited                                        *
      *                                                                          *
      * Licensed under the Apache License, Version 2.0 (the "License");          *
      * you may not use this file except in compliance with the License.         *
      * You may obtain a copy of the License at                                  *
      *                                                                          *
      *     http//www.apache.org/licenses/LICENSE-2.0                           *
      *                                                                          *
      * Unless required by applicable law or agreed to in writing, software      *
      * distributed under the License is distributed on an "AS IS" BASIS,        *
      * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. *
      * See the License for the specific language governing permissions and      *
      * limitations under the License.                                           *
      *                                                                          *
      ****************************************************************************

       01  EMPLOYEE.
           05  ID                       PIC 9(9).
           05  COMPANY.
               10  COMPANY-NAME         PIC X(20).
               10  SHORT_NAME           PIC X(3).
           05  PERSONAL.
               10  FIRST-NAME           PIC A(15).
               10  LAST-NAME            PIC A(15).
               10  DATE-OF-BIRTH        PIC 9(8).
               10  EXPERIENCE           PIC 9(2).
               10  SALARY               PIC S9(10).
               10  NUMBER-OF-PROJECTS   PIC 9(3).
               10  PROJECTS-DETAIL      OCCURS 4
                                        DEPENDING ON NUMBER-OF-PROJECTS.
                      12  PROJECT-NAME    PIC A(15).
                      12  START-DATE      PIC 9(8).
                      12  END-DATE        PIC 9(8).   
    