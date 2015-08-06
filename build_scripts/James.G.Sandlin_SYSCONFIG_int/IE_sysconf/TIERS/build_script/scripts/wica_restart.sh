#!/usr/bin/bash

. $HOME/.bashrc
#home_dir="/home/wasadmin"
home_dir=$HOME
tiersapp_dir="$home_dir/tiersbuild/harmony/tiersapps"
WICA_HOME="/export/WICA"

if [ $# -ne 3 ]  ; then
  echo "Usage: WICA.sh <p1> <p2> <p3>"
  echo "Requires all 3 parameters"
  echo "1st parameter is the Server name"
  echo "2nd parameter is the Cluster name"
  echo "3rd parameter is the Application name"
  exit 1
fi

if [ "${WICA_HOME}" = "" ]; then
        echo "Please set WICA_HOME in your environment before executing $0"
        exit 1
fi 

. ${WICA_HOME}/lib/shell/bash/shellLib.bash
echo "Installing App"
${WICA_HOME}/bin/WICA.sh -e TIERS -c tiers_aging_Network -u $1 -a $2 -t $3 install_app
checkReturnCode

sleep 300
echo "Starting Cluster"
${WICA_HOME}/bin/WICA.sh -e TIERS -c tiers_aging_Network -t start_clusters
checkReturnCode

exit 0
