#!/usr/bin/ksh
#
# This script is made to change permissions of log files. Run as user who owns files.
#
# @author: James Sandlin
# created: 2006-11-30
# company: Accenture
#
#
if [ $# -ne 1 ]; then
   echo 1>&2 Usage: changecontentspermissions.ksh ENV_NAME
   exit 127
fi
READWRITEEXECUTE="fw/java/patch"
READWRITE="tierslog */temp */logs */data */report co/print tierslogs ftp ftp/receive"
ALL="${READWRITE} ${READWRITEEXECUTE}"

for p in $ALL
do
   echo "Processing $p directory..."
   echo "Set /TIERS/$1/$p/* & its contents to be writable by group"
   echo "chmod g+w /TIERS/$1/$p/* 2>/dev/null"
   `chmod -R g+w /TIERS/$1/$p/* 2>/dev/null`
done


