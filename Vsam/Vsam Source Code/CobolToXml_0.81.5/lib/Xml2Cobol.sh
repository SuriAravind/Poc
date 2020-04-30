#! /bin/sh

## * -------------------------------------------------------------------
## *
## *  Purpose:  Convert a  Xml file to a COBOL Data file (using Cobol Copybook)
## *   Author:  Bruce Martin (CobolToCsv project)
## *
## * -------------------------------------------------------------------

ScriptDIR="$(dirname "$(readlink -f "$0")")"

java -jar ${ScriptDIR}/Xml2Cobol.jar "$@"
