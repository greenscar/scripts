#!/usr/bin/ksh
#
# This script is used for removing '^M' characters from all the files in a directory.
#
# @author: Rajesh Sambhaktula
# created: 08/23/2002
# company: Deloitte Consulting
#
#
if [ $# -ne 1 ]
then
    echo "Usage: removeCtrlCharsFromFile.ksh <file name with absolute path name> "
    echo "ex: /TIERS/TRDEVCOLA2/ab/data/corruptedFile.txt"
    exit 1
fi

INP_FILE_NAME=$1
OUT_FILE_NAME=$1.temp
LOG_FILE_NAME=$1.log

        #echo $1
        tr -d "\015" <$INP_FILE_NAME > $OUT_FILE_NAME
        chmod 775 $OUT_FILE_NAME 
        if [ $? -eq 0 ]
        then
           mv -f $OUT_FILE_NAME $INP_FILE_NAME
           # Changing geoup ownership to tiersbat
           chgrp batchtaa $1
           #echo "$d/tempRemCtrlM $1"
        else
           echo $1 >> $LOG_FILE_NAME
        fi

