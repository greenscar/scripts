#! /bin/bash
if [ $# -ne 1 ]; then
   echo 1>&2 Usage: dar_photos DIR
   exit 127
fi

date=`date +%Y-%m-%d_%H:%m:%S`
src_dir="/media/sdb1/photographs"
backup_dir="/media/backup"
log_dir="/home/c8h10n4o2/backup_log"
subdir_to_backup=$1
log_file=${log_dir}/photos_${subdir_to_backup}_${date}.log
email_address="james@h8n.com"

echo "Backing up ${src_dir}/${subdir_to_backup}" > ${log_file}  
dar -v -R ${src_dir}/${subdir_to_backup} -c ${backup_dir}/photos_${subdir_to_backup} -m 256 -y -s 4000M -D >> ${log_file}  
echo "DONE Backing up ${src_dir}/${subdir_to_backup}" >> ${log_file}

#EMAIL COMPLETION
more ${log_file} | mail -s "${subdir_to_backup} photos backup completed" ${email_address}
