#!/usr/bin/ksh
#
# This script is made to go into an environemnt and give batchop ONLY the permissions required to run a batch job.
#
# @author: James Sandlin
# created: 2006-11-30
# company: Accenture
#
#
if [ $# -ne 1 ]; then
   echo 1>&2 Usage: changepermissions.ksh ENV_NAME
   exit 127
fi
READWRITEEXECUTE="fw/java/patch"
READWRITE="*/temp */logs */data */report co/print tierslogs ftp ftp/receive"
ALL="${READWRITE} ${READWRITEEXECUTE}"

echo "Change permissions of $p and its contents to 755"
chmod -R 755 $1

for p in $ALL
do
   echo "Processing $p directory..."
   echo "Set /TIERS/$1/$p so anything written in it inherits dir's group privileges."
   echo "chmod g+s /TIERS/$1/$p"
   `chmod g+s /TIERS/$1/$p`
   echo "Set /TIERS/$1/$p & its contents to be writable by group"
   echo "chmod -R g+w /TIERS/$1/$p"
   `chmod -R g+w /TIERS/$1/$p`
   echo "Change group of /TIERS/$1/$p contents to batchtaa"
   echo "chgrp -R batchtaa /TIERS/$1/$p"
   `chgrp -R batchtaa /TIERS/$1/$p`
   echo "Always must have execute permissions to the directory /TIERS/$1/$p."
   echo "chmod g+x /TIERS/$1/$p"
   `chmod g+x /TIERS/$1/$p`
done

for p in $READWRITEEXECUTE
do
  echo "We want the group to have execute permission to the contents of /TIERS/$1/$p."
  echo "chmod -R g+x /TIERS/$1/$p"
  `chmod -R g+x /TIERS/$1/$p`
done

