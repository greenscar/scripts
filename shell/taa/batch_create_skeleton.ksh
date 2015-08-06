#/bin/ksh
###############################################################################
# create_skeleton.ksh creates the skeleton structure for the batch environment
# This script should be run at the start of each deployment to ensure the directory
#  structure is all defined.
###############################################################################

# Make sure arg was provided
if [ $# -ne 1 ]; then
   echo 1>&2 Usage: create_skeleton.ksh ENV_NAME
   exit 127
fi

# Set Environment variable
if [ $1 = "CONV1" ]; then
   tiers_env_dir="/TIERS/ENV"
else
   if test $1 = "CONV2"; then
      tiers_env_dir="/TIERS/ENV2"
   else
      tiers_env_dir="/TIERS/$1"
   fi
fi

# Set Variables
# Dirs to Create
dirs="al cv dc hm mo qc rp bi se BUILDSCRIPTS in co sh tierslog fw mu tm ftp functional props xml BUILDSCRIPTS/cleanup fw/lib fw/java/xml/config fw/java/xml/rules props/framework co/template/pdf co/template/Fonts cv/script/ldr/bc/ldrlog"
# Sub Dirs to create in all dirs_with_subs
subdirs="data java lib logs print report script sort temp template java/patch"
# Dirs to create subdirs in.
dirs_with_subs="fw mu al qc rp co bi hm mo sh in cv dc functional"
dirs_ftp_receive="EBT TWC SAVERR OAG ENROL_BRKR ARTS COMPTROLLER FMIS TDH SSA HRMIS LSIS DHS-MF HHSC NHIC TDH-CHIP TDH-EPSDT TDHS-HRMIS DHS-TS SDX SAS-MG MHMR DHS-MW ftpconv"
tiers_ftp_receive_dir="/TIERS/batch/ftp/receive"
   

# Create all DIRs
for fs in $dirs
do
   echo "Making $tiers_env_dir/$fs"
   #mkdir $tiers_env_dir/$fs
done

# Create all SUB DIRs
for fs in $dirs_with_subs
do
    for dir in $subdirs
    do
        echo "Making $tiers_env_dir/$fs/$dir"
        #mkdir -p $tiers_env_dir/$fs/$dir
    done
done

# If IPT, make sure FTP receive directories all exist.
if test $1 = "IPT" ; then
   echo "Creating FTP directories for IPT"
   # Create all FTP receive Directories.
   for fs in $dirs_ftp_receive
   do
      echo "Making $tiers_ftp_receive_dir/$fs"
     #mkdir -p $tiers_ftp_receive_dir/$fs
   done
fi
echo "ABOUT TO DO CONV1"
# If CONV1, make sure FTP receive directories all exist. 
if test $1 = "CONV1" ; then
   tiers_ftp_receive_dir="$tiers_ftp_receive_dir/ftpconv"
   echo "Creating FTP directories for CONV1"
   #mkdir -p $tiers_ftp_receive_dir
fi



#touch $tiers_env_dir/fw/java/patch/testfile
#touch $tiers_env_dir/BUILDSCRIPTS/cleanupScripts${1}.ksh
#chmod 755 $tiers_env_dir/BUILDSCRIPTS/cleanupScripts${1}.ksh


