#!/bin/ksh

. $HOME/.bashrc

date=`date '+%Y_%m_%d-%H:%M'`
#home_dir="/home/wasadmin"
home_dir=$HOME
tiersapp_dir="$home_dir/tiersbuild/harmony/tiersapps"

if [ $# -ne 4 ]  ; then
  echo "Usage: build.sh <p1> <p2> <p3> <p4>"
  echo "Requires all 4 parameters"
  echo "1st parameter is the environment name"
  echo "2nd parameter is the clearcase view name"
  echo "3rd parameter is the Build Number"
  echo "4th parameter is the ant target"
  exit 1
fi

# Get path to build properties directory - used by autobuild.pl wrapper script
bldDir=`echo $2 | cut -d_ -f1-2`
bldProps="$home_dir/tiersbuild/harmony/autobuild/$bldDir"
if [ -e $bldProps/BUILD_WAS_SUCCESSFUL ]; then
        rm -f $bldProps/BUILD_WAS_SUCCESSFUL
fi

echo "Parameters are:"
echo "env_name = $1"
echo "view_name = $2"
echo "build_number = $3"
echo "target = $4"
echo

if [ ! -d $home_dir/$2 ] ; then
   echo "Snapshot view $2 located at $home_dir/$2 does not exist!!!"
   exit 1
fi

if [ ! -d $tiersapp_dir/$2/logs ] ; then
    mkdir -p $tiersapp_dir/$2/logs
    mkdir -p $tiersapp_dir/$2/online/ear
fi

# Update SYSCONFIG_Int stream
cd /home/ccbuild/SYSCONFIG_Int
/opt/rational/clearcase/bin/cleartool update -log /dev/null
# Copy properties files to build directories
cp -f /home/ccbuild/SYSCONFIG_Int/IE_sysconf/TIERS/build_script/props/* ~/tiersbuild/harmony/props
cp -f /home/ccbuild/SYSCONFIG_Int/IE_sysconf/TIERS/build_script/xml/* ~/tiersbuild/harmony/xml
cd $home_dir/tiersbuild/harmony/xml

ant -Denv_name="$1" \
    -Dview_name="$2" \
    -Dbuild_number="$3" \
    -logfile $tiersapp_dir/$2/logs/build$1-$date.log \
    -buildfile $home_dir/tiersbuild/harmony/xml/build.xml \
    "$4"

# Touch file if build was successful based on ant's return code - used by autobuild.pl wrapper script
# Get return code from previous ant command - This must immediately follow the ant command
rc=$?
echo "`date '+%Y-%m-%d : %H:%M'` return_code => $rc" >> $bldProps/ant_return.txt
# Ensure that release specific build properties directory exists
# Create it if it doesn't
if [ ! -d $bldProps ]; then
        mkdir -p $bldProps
fi

# Create 0 byte status file if ant build was successful
# Determined by $rc (return code from ant) being 0
if [ $rc == 0 ]; then
        touch $bldProps/BUILD_WAS_SUCCESSFUL
fi

echo "Executing ant command:"
echo "ant -Denv_name=$1 -Dview_name=$2 -Dbuild_number=$3 $4"
echo "Log file $tiersapp_dir/$2/logs/build$1-$date.log is created!!!"
echo "Tail it to see the output"

exit 0

