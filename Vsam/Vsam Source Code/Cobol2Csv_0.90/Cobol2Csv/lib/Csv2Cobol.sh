#! /bin/sh

## * -------------------------------------------------------------------
## *
## *  Purpose:  Convert a Csv file to a COBOL Data file (using Cobol Copybook)
## *   Author:  Bruce Martin (CobolToCsv project)
## *
## * -------------------------------------------------------------------

ScriptDIR="$(dirname "$(readlink -f "$0")")"

java -jar ${ScriptDIR}/Csv2Cobol.jar "$@"

