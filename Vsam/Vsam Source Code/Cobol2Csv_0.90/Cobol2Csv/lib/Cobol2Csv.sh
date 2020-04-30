#! /bin/sh

## * -------------------------------------------------------------------
## *
## *  Purpose:  Convert a COBOL Data file to a Csv file (using Cobol Copybook)
## *   Author:  Bruce Martin (CobolToCsv project)
## *
## * -------------------------------------------------------------------

ScriptDIR="$(dirname "$(readlink -f "$0")")"

java -jar ${ScriptDIR}/Cobol2Csv.jar "$@"

