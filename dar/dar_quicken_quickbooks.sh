#! /bin/bash

date=`date +%Y-%m-%d_%H:%m:%S`
src_dir="/media/raid/backup"
backup_dir="/mnt/greenbox_backup"
log_dir="/ldap_home/c8h10n4o2/backup_log"
subdir_to_backup="Quicken_Backup"
log_file=${log_dir}/${subdir_to_backup}_${date}.log
email_address="james@sandlininc.com"

echo "Backing up ${src_dir}/${subdir_to_backup}" > ${log_file}  
echo "dar -v -R ${src_dir}/${subdir_to_backup} -c ${backup_dir}/${subdir_to_backup}_${date} -m 256 -y -s 4000M -D"
mount ${backup_dir}
dar -v -R ${src_dir}/${subdir_to_backup} -c ${backup_dir}/${subdir_to_backup}_${date} -m 256 -y -s 4000M -D >> ${log_file}  
umount ${backup_dir}
echo "DONE Backing up ${src_dir}/${subdir_to_backup}" >> ${log_file}

#EMAIL COMPLETION
more ${log_file} | mail -s "${subdir_to_backup} backup completed" ${email_address}
