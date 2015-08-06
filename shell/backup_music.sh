
date=`date +%Y-%m-%d_%H:%m:%S`
src_dir="/mnt/music"
backup_dir="/mnt/music_backup"
log_dir="/home/c8h10n4o2/backup_log"

rsync -rv ${src_dir} ${backup_dir} >> /home/c8h10n4o2/backup_log/music_${date}.log
