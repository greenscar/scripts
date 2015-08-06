home_dir=$HOME
# Update SYSCONFIG_Int stream
#ssh ccbuild@iedaau019 "cd SYSCONFIG_Int; /opt/rational/clearcase/bin/cleartool update -log /dev/null"
cd /home/ccbuild/SYSCONFIG_Int; /opt/rational/clearcase/bin/cleartool update -log /dev/null
# Copy properties files to build directories
cp -f /home/ccbuild/SYSCONFIG_Int/IE_sysconf/TIERS/build_script/props/* ~/tiersbuild/harmony/props
cp -f /home/ccbuild/SYSCONFIG_Int/IE_sysconf/TIERS/build_script/xml/* ~/tiersbuild/harmony/xml
cd $home_dir/tiersbuild/harmony/xml
