
date=`date +%Y-%m-%d_%H:%m:%S`
src_dir="/mnt/nasmemories"
backup_dir="/media/memories"
log_dir="/home/c8h10n4o2/backup_log"
`mount -t smbfs -o  username=james,password=Zoolu4 //nas/memories /mnt/nasmemories`
rsync -rv ${src_dir} ${backup_dir} >> /ldap_home/c8h10n4o2/logs/memories_${date}.log
`umount /mnt/nasmemories`
