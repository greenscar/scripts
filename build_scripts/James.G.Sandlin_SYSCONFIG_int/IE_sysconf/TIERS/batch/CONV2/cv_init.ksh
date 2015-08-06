#!/bin/ksh
##################################################################### 
#Name: cv_init.ksh                                                  #
#Usage: cv_init.ksh                                                 #
#Arguments:                                                         #
#None                                                               #
#Version: 1                                                         #
#Date: 02/14/2002                                                   #
#Purpose:                                                           #
#Initialization file for the conversion process                     #
#Author: SR                                                         #
#Revision History:                                                  #
#Date      Who                         Reason                       #
#                                                                   #
#                                                                   #
#####################################################################
#. /TIERS/ENV2/cv/script/set_oracle_env.ksh
set +vx
CV_HOME="/TIERS/ENV2/cv/script/ldr/bc"
CV_BIN="$CV_HOME/bin"
CV_SQL="$CV_HOME/sql"
CV_DATA="$CV_HOME/data"
CV_CTL="$CV_HOME/ctl"
CV_BAD="$CV_HOME/bad"
CV_DSC="$CV_HOME/dsc"
CV_LDRLOG="$CV_HOME/ldrlog"
CV_PAR="$CV_HOME/par"
CV_STAT="$CV_HOME/status"
CV_SQLLOG="$CV_HOME/sqllog"
CV_WORK="$CV_HOME/work"
CV_ASQL="$CV_HOME/asql"
CV_DMP="$CV_HOME/dmp"
CV_EXPLOG="$CV_HOME/explog"
CV_IMPLOG="$CV_HOME/implog"
CV_LDR="sqlldr"

CV_USR="TIERS2CON"
CV_PWD="H39HD04MVHWOSL5@TRTSTC2"
CV_OWN_USR="TIERS2OWN"
CV_OWN_PWD="P9J37DHHDI57NAGEUT0@TRTSTC2"
CV_ERR_USR="TIERS2CON"
CV_ERR_PWD="H39HD04MVHWOSL5@TRTSTC2"
SP="sqlplus -silent"
SSP="sqlplus -silent"

echo "define cv_usr = $CV_USR;"                > $CV_SQL/init_conn.sql 
echo "define cv_pwd = $CV_PWD;"               >> $CV_SQL/init_conn.sql
set -vx
