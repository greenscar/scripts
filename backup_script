#!/bin/sh 

# Backup important training data to the backup drive
#usageQuit()
#{
#cat << "EOF" >&2
#Usage: $0 [-o output] [-i|-f] [-n]
#  -o lets you specify an alternative backup file/device.
#  -i is an incremental or -f is a full backup.
#  -n prevents updating the timestamp if incremental is done.
#EOF
#  exit 1
#}
compress="gzip"
output_dir="/mnt/backup"
#output_dir="/tmp"
output_web="$(date +%Y-%m-%d)-wwwroot.tgz"
output_data="$(date +%Y-%m-%d)-mysql-data.tgz"
WEBHOME="/wwwroot/trainweb"
DBDATA="/wwwroot/mysql/data/"

tsfile="$WEBHOME/.backuptimestamp"
btype="full"		# default to full backup
noinc=0			# and an update of the timestamp
trap "/bin/rm -f $inclist" EXIT

while getopts "o:ifn" arg; do
   case "$arg" in
	i ) btype="incremental";	;;
	f ) btype="full";		;;
	n ) noinc=1;			;;
#	? ) usageQuit			;;
   esac
done

shift $(( $OPTIND - 1 ))
#echo "Doing $btype backup, saving output to $output_web"

#rm -fr $output_dir/$output_web
#rm -fr $output_dir/$output_data
# MOUNT THE NETWORK DRIVE (JUST IN CASE IT IS NOT MOUNTED ALREADY)
/bin/mount /mnt/backup
#tar -cz --file=$output_dir/$output_web $WEBHOME
#tar -cz --file=$output_dir/$output_data $DBDATA

timestamp="$(date +%m%d%I%M)"

if [ "$btype" = "incremental" ] ; then
    if [ ! -f $tsfile ] ; then
	echo "ERROR: Can't do an incremental backup: no timestamp file" >&2
	exit 1
    fi
    find $HOME -depth -type f -newer $tsfile | \
	pax -w -x tar | $compress > $output_dir/$output_web
    failure_web="$?"
    find $DBDATA -depth -type f -newer $tsfile | \
	pax -w -x tar | $compress > $output_dir/$output_data
    failure_data="$?"
else
    find $WEBHOME -depth -type f | \
	pax -w -x tar | $compress > $output_dir/$output_web
    failure_web="$?"
    find $DBDATA -depth -type f | \
	pax -w -x tar | $compress > $output_dir/$output_data
    failure_data="$?"
fi

if [ "$noinc" = "0" -a "$failure_web" = "0" -a "$failure_data" = "0" ] ; then
	touch -t $timestamp $tsfile
else
	echo "ERROR WITH TIMESTAMP"
fi

exit 0 
