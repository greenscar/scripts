#! /bin/bash

date=`date +%Y-%m-%d_%H:%m:%S`
src_dir="/var/mail"
#backup_dir="/media/raid/backup/mail_dar"
backup_dir="/mnt/software/mail_backup"
log_dir=${backup_dir}
subdir_to_backup="virtual"
log_file=${log_dir}/${subdir_to_backup}_${date}.log
email_address="james@sandlininc.com"

echo "Backing up ${src_dir}/${subdir_to_backup}" > ${log_file}  
echo "dar -v -R ${src_dir}/${subdir_to_backup} -c ${backup_dir}/email_${date} -m 256 -y -s 4000M -D"
dar -v -R ${src_dir}/${subdir_to_backup} -c ${backup_dir}/email_${date} -m 256 -y -s 4000M -D >> ${log_file}  
echo "DONE Backing up ${src_dir}/${subdir_to_backup}" >> ${log_file}

#EMAIL COMPLETION
more ${log_file} | mail -s "Email backup completed" ${email_address}
