#! /bin/sh

## * -------------------------------------------------------------------
## *
## *  Purpose:  Convert a COBOL Data file to a Xml file (using Cobol Copybook)
## *   Author:  Bruce Martin (CobolToCsv project)
## *
## * -------------------------------------------------------------------

ScriptDIR="$(dirname "$(readlink -f "$0")")"

java -jar ${ScriptDIR}/Cobol2Xml.jar "$@"

