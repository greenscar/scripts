#!/bin/ksh
boxes=( radccpdep01 radccpmps01 radccpmps02 radccpwebutil01 radccpauthadm01 radccpauthadm02 radccplicense01 radccplicense02)
for box in ${boxes[@]}
{
#print $box
ssh $box 'perl -pi -e "s/Defaults\s*requiretty/#Defaults\trequiretty/g" /etc/sudoers'
}
