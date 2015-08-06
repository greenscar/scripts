for i in `ls /mnt`; do `sudo mount /mnt/$i > /dev/null`; echo $i" = "`du -sh /mnt/$i` >> /home/c8h10n4o2/%d_dir_size.log; done
