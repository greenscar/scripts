#!/bin/sh
# Author: Barry Searle
#
# (C) Copyright IBM Corp. 2004,2005 - All Rights Reserved.
# DISCLAIMER:
# The following source code is sample code created by IBM Corporation.
# This sample code is not part of any standard IBM product and is provided
# to you solely for the purpose of assisting you in the development of your
# applications. The code is provided 'AS IS', without warranty or condition
# of any kind. IBM shall not be liable for any damages arising out of your
# use of the sample code, even if IBM has been advised of the possibility of
# such damages.
#
# Change History:
# 1.3 (22apr2005) API: PARAM-1 is JACLdistDir, eliminated D_WAS param
# 1.2 (14jan2005) use automatically determined D_XXXX envars
# 1.1 (17nov2004) initial version
#

binDir=`dirname "$0"`
pwd=`pwd`
if [ $binDir = "." ]
then
  binDir=$pwd
fi

# ########## ONLY 'WAS_ROOT' MUST BE SET ########
WAS_ROOT=/opt/IBM/WAS60/AppServer/profiles/ND6

# ########## NO LONGER USED ############
JACLWASROOT=$WAS_ROOT
JACLbaseDir=$binDir
distDir=$JACLbaseDir/dist

D_ROOT=$binDir
D_DIST=$binDir/dist
D_WAS=$WAS_ROOT
D_STAGE=-pilot
D_FAIL=true
D_ACTION=install
script=$D_ROOT/automatedDeploy.jacl

if [ "$1" = "ant" ]; then
  ../../runAnt.sh -buildfile ./automatedDeploy.xml
else
#                                          action    stage   fail  "$distDir"
# FIRST install ...
  echo FIRST install ...
  "$WAS_ROOT/bin/wsadmin.sh" -f "$script"  install   -pilot  true  "$distDir"

  echo SECOND update ...
  "$WAS_ROOT/bin/wsadmin.sh" -f "$script"  update    -pilot  false "$distDir"

  echo .
  echo THIRD confirm ...
  "$WAS_ROOT/bin/wsadmin.sh" -f "$script"  confirm   -pilot  true  "$distDir"

  echo FOURTH  uninstall ...
  "$WAS_ROOT/bin/wsadmin.sh" -f "$script"  uninstall -pilot  true  "$distDir"

  echo DONE.
fi
