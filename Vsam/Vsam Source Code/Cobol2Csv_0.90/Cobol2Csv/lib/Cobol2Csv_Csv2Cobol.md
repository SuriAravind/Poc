##Cobol2Csv, Csv2Cobol programs

With JRecord version 0.80.6, I have added 2 example programs Cobol2Csv and Csv2Cobol.
As you might expect these 2 programs convert Cobol data files to/from Csv using a Cobol copybook.
Program arguments are identical for both programs.

At this stage these are example programs, so if you decide to use them, please **test** them **thoroughly !!!**. 

###Program arguments:
<pre>     
  <font color="blue"><b>-I </b></font> or <font color="blue"><b>-InputFile </b></font>         : Input file
  <font color="blue"><b>-O </b></font> or <font color="blue"><b>-OutputFile</b></font>         : Output file
  <font color="blue"><b>-C </b></font> or <font color="blue"><b>-Copybook</b></font>           : Cobol Copybook file
  <font color="blue"><b>-D </b></font> or <font color="blue"><b>-Delimiter </b></font>         : Field Seperator (space, tab</b></font> or value)
  <font color="blue"><b>-Q </b></font> or <font color="blue"><b>-Quote </b></font>             : Quote, you can use DoubleQuote SingleQuote as well as an a the char
  <font color="blue"><b>-IC</b></font> or <font color="blue"><b>-InputCharacterSet</b></font>  : Input  font or character set
  <font color="blue"><b>-OC</b></font> or <font color="blue"><b>-OutputCharacterSet</b></font> : Output font or character set
  <font color="blue"><b>-FS</b></font> or <font color="blue"><b>-FileStructure</b></font>      : File Structure:
      <b>Default      </b>: Determine by Copybook  
      <b>Text         </b>: Use Standard Text IO 
      <b>Fixed_Length </b>: Fixed record Length binary 
      <b>Mainframe_VB </b>: Mainframe VB File 
      <b>Mainframe_VB_As_RECFMU</b> : Mainframe VB File including BDW (block descriptor word)
      <b>FUJITSU_VB   </b>: Fujitsu Cobol VB File 
      <b>Open_Cobol_VB</b>: Gnu Cobol VB File 
  <font color="blue"><b>-B</b></font> : Cobol Dialect
      <b>0</b> or <b>Intel</b>        : Intel little endian 
      <b>1</b> or <b>Mainframe</b>    : Mainframe big endian (Default) 
      <b>4</b> or <b>Open_Cobol_Little_Endian_(Intel)</b>  : Gnu:Cobol 
      <b>2</b> or <b>Fujitsu</b>      : Fujitsu Cobol 
  <font color="blue"><b>-Rename</b></font>         : How to update cobol variable names
      <b>0</b> or <b>Leave_Asis</b>                : Use the COBOL name
      <b>1</b> or <b>Change_Minus_To_Underscore</b>: Change '-(),' to '_'
      <b>2</b> or <b>Drop_Minus</b>                : Drop minus ('-') from the name
  <font color="blue"><b>-CsvParser</b></font> : Controls how Csv fields are written / parsed
      <b>0</b> or <b>Basic_Parser</b>                          : Parse Csv - when a field starts with " look for "&lt;FieldSeparator&gt;or "&lt;eol&gt 
      <b>1</b> or <b>Standard_Parser</b>                       : Parse CSV matching Quotes
      <b>2</b> or <b>Standard_Parse_Quote_4_Char_Fields</b>    : Standard Parser, add Quotes to all Char fields
      <b>3</b> or <b>Basic_Parser_Column_names_in_quotes</b>   : Basic Parser, Field (Column) names in Quotes
      <b>4</b> or <b>Standard_Parser_Column_names_in_quotes</b>: Standard Parser, Field (Column) names in Quotes
      <b>5</b> or <b>Standard_Parser_Quote_4_Char_Fields_Column_names_in_quotes</b>        : Standard Parser, Char fields in Quotes,  Field (Column) names in Quotes
      <b>6</b> or <b>Basic_Parser_Delimiter_all_fields</b>     : Basic Parser, Field Separator for all fields
      <b>7</b> or <b>Basic_Parser_Delimiter_all_fields+1</b>   : Basic Parser, Field Separator for all fields + extra seperator at the End-of-Line

</pre>

