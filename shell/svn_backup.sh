#! /bin/bash

if [ $# -ne 1 ]; then
   echo 1>&2 Usage: svn_backup.sh STREAM_NAME
   exit 127
fi

stream_name=$1
date=`date +%Y-%m-%d_%H_%m%S`
svn_dir="/var/svn"
#svn_backup_dir="/mnt/backup/svn_backup"
svn_backup_dir="/media/raid/backup/svn_backup"

svnadmin dump ${svn_dir}/${stream_name} > /tmp/${stream_name}_${date}.dump 
cp /tmp/${stream_name}_${date}.dump ${svn_backup_dir}/${stream_name}_${date}.dump
rm -f  /tmp/${stream_name}_${date}.dump
