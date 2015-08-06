date=`date +%Y-%m-%d_%H:%M:%S`
src_dir="/media/photographs"
backup_dir="/media/backup"
log_dir="/home/c8h10n4o2/backup_log"
log_file=${log_dir}/photos_all_${date}.log
email_address="james@h8n.com"

echo "Backing up ${src_dir}" > ${log_file}
echo "dar -v -R ${src_dir} -c ${backup_dir}/photos_all -m 256 -y -s 4000M -D >> ${log_file}"
dar -v -R ${src_dir} -c ${backup_dir}/photos_all -m 256 -y -s 4000M -D >> ${log_file}
echo "DONE Backing up ${src_dir}" >> ${log_file}

#EMAIL COMPLETION
more ${log_file} | mail -s "${subdir_to_backup} photos backup completed" ${email_address}


