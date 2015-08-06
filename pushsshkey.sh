#!/bin/ksh
# Call this script via:
# pushsshkey.sh <DESTBOX>
box=$1
user=$2
SSHKEY='ssh-dss AAAAB3NzaC1kc3MAAACBAMjQ578v8CPox6HBPcOcjw6NEhZHvIkz9dubzfAFdGZ7pbojHRQlKCVvLaMbNJ5hrFgOPVWqOzPovhMc6Qyr660SXup1aolP4v6fVTytRjuqwxFWATeKAFcnYCc86L7n3ziF/ZxhJEouWqKvUHzfqS3Rlp+PTi3YgGxaxcHa/cC9AAAAFQC5N2pvzCskDMFGOa7FTbsWw+hJ4wAAAIBct//PEnZph0LBNoIQrdQHLsZtfmbBOvLTqOuVujIAwpfqIUfJtv033jvfYbca0mitAuOsdkegvc3ANnwOnvOLy6R8xCz9jaOz1KPNAyFt4qbAg0pO2Tu1f3jmM1AXBuL1s4KcficUTEMoyo7PSKN8k1dpffMjWrKnZTqFR9B1wgAAAIBZyEbibgwr7hMdKJyOb23ZFdELnDlWGgSpazpBgjH0+poASJzddfZS/AQIEwcvhMk8EdYNo/0coIVvBziWGPDVCGqfMMB+3b8x+kLFA/YzFpIOZudFEjwVaL8UaDEEJzqvNu1Q0DD/EuicdKQrACuywOFwDmazKHpJ0JOWgZejZg== james.sandlin@lt-jsandlin-mac.local'

if [ -z "$box" ]
then
   boxes=( SEA1TPCONF01 SEA1TPCONF02 SEA1TPAPITPAPI02 SEA1TPAPI03 SEA1TPAPI04 SEA1TPAPI05 SEA1TPMPX01 SEA1TPMPX02 SEA1TPMPX91 sea1tpfacade01 sea1tpfacade02 sea1tpfacsry01 sea1tpfacsry02 sea1tpfacade03 sea1tpfacade04 SEA1TPPUBLISH01 SEA1TPPUBLISH02 SEA1TPPUBWORK01 SEA1TPPUBWORK02 SEA1TPPUBLISH91 SEA1TPPUBWORK91 SEA1TPAPICH01 SEA1TPAPICH02 SEA1CNBCAPI01 SEA1SKYTIDE01 SEA1TPCON01 SEA1TPCON02 SEA1TPLIC01 sea1tpdelcfg01 sea1tpdelcfg02 sea1tpdelcfg03 sea1tpdelcfg91 sea1tpdelcfg92 SEA1TPPLYRADM01 SEA1TPPLYRADM02 SEA1TPPLYRLNK01 SEA1TPPLYRLNK02 SEA1TPPLYRSRY01 SEA1TPRPT01 SEA1TPRPT02 sea1foxfeeds01 sea1foxfeeds02 sea1foxfcms01 SEA1STZAPI01 SEA1STZAPI02 SEA1TPSEL01 SEA1TPSEL02 SEA1TPSEL03 SEA1TPSEL04 SEA1TPSEL05 sea1tpmetrics01 sea1tpmetrics02 SEA1TPSORRY01 SEA1TPSORRY02 SEA1TPSORRY03 SEA1TPSORRY04 SEA1TPSORRY05 SEA1TPTASK01 SEA1TPTASK02 SEA1TPTASKSVC01 SEA1TPTASKSVC02 SEA1TPTASKSVC91 SEA1TPSLA01 SEA1TPAPIFEED01 SEA1TPAPIFEED02 SEA1TPAPISLA01 SEA1TPMQ01 SEA1TPMQ02 SEA1TPMQ03 SEA1TPMQ04 sea1tpingest01 sea1tpingest02 Sea1tpingest91 SEA1TPAUTHADM02 SEA1TPAUTHADM03 SEA1TPAUTHADM04 SEA1TPAUTHADM05 SEA1TPAUTHADM06 SEA1TPAUTHADM07 SEA1TPAUTHADM08 sea1tpwf01 sea1tpwf02 sea1tpwf03 SEA1TPAUTHUSR01 SEA1TPAUTHUSR02 SEA1TPAUTHUSR03 SEA1TPAUTHUSR04 SEA1TPAUTHUSR05 SEA1TPAUTHUSR06 SEA1TPAUTHUSR07 SEA1TPAUTHUSR08 SEA1TPCMTY01 SEA1TPCMTY02 SEA1TPCMTY03 SEA1TPCMTY04 SEA1TPCMTY91 SEA1TPCMTY92 SEA1TPCMTY05 SEA1TPMGMT01 SEA1TPMGMT02 SEA1TPMGMT03 sea1tprights01 sea1tprights02 sea1tprights03 sea1tprights91 sea1tpcmrc01 sea1tpcmrc02 sea1tpcmrc91 sea1tpchkout01 sea1tpchkout02 sea1tpfeedrdr01 sea1tpfeedrdr02)
   
   for box in ${boxes[@]}
   {   
      ssh $box 'if [ -d ~/.ssh ]; then echo ' $box '; else mkdir ~/.ssh; echo ' $SSHKEY ' > ~/.ssh/authorized_keys; chmod -R 700 ~/.ssh; echo 0; fi;'
   }

else
   if [ -z "$user" ]
   then
      ssh $box 'if [ -d ~/.ssh ]; then echo ' $box ' = 1; else mkdir ~/.ssh; echo ' $SSHKEY ' > ~/.ssh/authorized_keys; chmod -R 700 ~/.ssh; echo 0; fi;'
   else
      ssh $user\@$box 'if [ -d ~/.ssh ]; then echo ' $box ' = 1; else mkdir ~/.ssh; echo ' $SSHKEY ' > ~/.ssh/authorized_keys; chmod -R 700 ~/.ssh; echo 0; fi;'
   fi
fi

#ssh $box 'if [ -d ~/.ssh ]; then echo 1; else mkdir ~/.ssh; echo ' $SSHKEY ' > ~/.ssh/authorized_keys; chmod -R 700 ~/.ssh; echo 0; fi;'


exit 1


