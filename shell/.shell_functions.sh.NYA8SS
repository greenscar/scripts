Simple Date and Time functions:


#===================================================================
s_timestamp()	# (c) RHReepe. Returns string (YYYY-Mon-DD@HH:MM:SS)
#===================================================================
{
    date "+%Y-%h-%d@%H:%M:%S"
}
#===================================================================
s_now()	# (c) RHReepe. Returns string (YYYYMMDDHHMMSS)
#===================================================================
{
    date +%Y%m%d%H%M%S
}
#===================================================================
s_time()	# (c) RHReepe. Returns string (HHMMSS)
#===================================================================
{
    date +%H%M%S
}
====================================================================
s_date()	# (c) RHReepe. Returns string (YYYYMMDD)
#===================================================================
{
    date +%Y%m%d
}
#=====================================================================
s_month_length()	# (c) RHReepe. Returns number of days in MONTH (INT)
#=====================================================================
# Arg_1 = MONTH_NUMBER
{
    if [ $1 -lt 1 ] || [ $1 -gt 12 ]
    then
	echo "$0:s_month_length(): [$1] \
	     is not between 1 and 12"
	exit
    fi
    lengths="312831303130313130313031"
    cut2=`expr $1 + $1`
    cut1=`expr $cut2 - 1`
    echo $lengths | cut -c$cut1-$cut2
}
#=====================================================================
s_back_date	# (c) RHReepe. Returns a date string DAYS back
#=====================================================================
# Arg_1 = [DAYS:-1]
{
    days=${1:-1}
    date_d=`date +%d`
    date_m=`date +%m`
    date_y=`date +%Y`
	#--------------------------------
	# Days Back Size Test
	#--------------------------------
	if [ $days -lt $date_d ]
	then
		date_d=`expr $date_d - $days`
	else
		days=`expr $days - $date_d`
		date_m=`expr $date_m - 1`
		month_length=`s_month_length $date_m`
		while [ $days -gt $month_length ]
		do
			days=`expr $days - month_length`
			date_m=`expr $date_m - 1`
			month_length=`s_month_length $date_m`
		done
		date_d=`expr $month_length - $days`
	fi
	#--------------------------------
	# Date	String Padding
	#--------------------------------
	if [ $date_d -lt 10 ]
	then
		date_d="0"$date_d
	fi
	if [ $date_m -lt 10 ]
	then
		date_m="0"$date_m
	fi
	echo $date_y $date_m $date_d
}

Simple File Set-up Functions:

This next section deals with functions related to file set-up. This includes temporary files, status flags, log-files and many other related functions. Some of these may seem simple and rudimentary, but that is less important than easing the readability of the calling scripts and reducing the need to re-code the same thing several times.


#===================================================================
s_prog()	# (c) RHReepe. Returns the calling script name
#===================================================================
{
	basename $0
}
#===================================================================
s_file()	# (c) RHReepe. Creates a file name
#===================================================================
# Arg_1 = file extension
# Arg_2 = unique identifier
{
	temp_file="/tmp/db_`s_prog`_$$_$2.$1"
	rm -f $temp_file
	touch $temp_file
	echo $temp_file
}
#===================================================================
s_rmfile()	# (c) RHReepe. Removes file names
#===================================================================
# Arg_1 = [file extension]
# Arg_2 = [unique ientifier]
{
	rm -f /tmp/db_`s_prog`_$$_${2:-""}*.${1:-""}*
}
#===================================================================
s_tmp()		# (c) RHReepe. Creates a TMP filename
#===================================================================
# Arg_1 [unique identifier]
{
	s_file tmp ${1:-$$}
}
#===================================================================
s_rmtmp()	# (c) RHReepe. Removes a TMP filename
#===================================================================
# Arg_1 = [unique identifier]
{
	s_rmfile tmp ${1:-$$}
}
#===================================================================
s_sql()		# (c) RHReepe. Creates a SQL filename
#===================================================================
# Arg_1 [unique identifier]
{
	s_file sql ${1:-$$}
}
#===================================================================
s_rmsql()	# (c) RHReepe. Removes a SQL filename
#===================================================================
# Arg_1 = [unique identifier]
{
	s_rmfile sql ${1:-$$}
}
#===================================================================
s_log()		# (c) RHReepe. Creates a LOG filename
#===================================================================
# Arg_1 [unique identifier]
{
	s_file log ${1:-$$}
}
#===================================================================
s_rmlog()	# (c) RHReepe. Removes a LOG filename
#===================================================================
# Arg_1 = [unique identifier]
{
	s_rmfile log ${1:-$$}
}
#===================================================================
s_cleanup()	# (c) RHReepe. Cleans up ALL files for this script
#===================================================================
# Arg_1 = [unique identifier]
{
	s_rmfile ??? ${1:-""}
}
#===================================================================
s_truncate()	# (c) RHReepe. Truncates extension from a filename
#===================================================================
# Arg_1 - FILENAME
{
	dot=`s_in_string $1 "."`
	dot=`expr $dot - 1`
	echo $1 | cut -c1-$dot
}
#===================================================================
s_running()	# (c) RHReepe. Checks if script is running
#===================================================================
{
	if [ -f /tmp/db_`s_prog`_running.lock ]
	then
		echo "Script [$0] is already running!"
		exit -1
	else
		touch /tmp/db_`s_prog`_running.lock
	fi
}
#===================================================================
s_stopping()	# (c) RHReepe. Removes running lock file
#===================================================================
{
	rm -f /tmp/db_`s_prog`_running.lock
}

Simple String Functions:

This next batch all relate to string functions and text handling. Starting simply with join strings and case changers, we move on through padding into character and string searches.


#===================================================================
s_join()	# (c)RHReepe. Creates single string from several
#===================================================================
# Arg_n = Input Strings
{
	echo "$@" | tr ' ' '_'
}
#===================================================================
s_upper()	# (c) RHReepe. Returns input string in upper case
#===================================================================
{
	echo $@ | tr '[a-z]' '[A-Z]'
}
#===================================================================
s_lower()	# (c) RHReepe. Returns input string in lower case
#===================================================================
{
	echo $@ | tr '[A-Z]' '[a-z]'
}
#===================================================================
s_count_args()	# (c)RHReepe. Returns count of input ARG_LIST
#===================================================================
# Arg_n = ARG_LIST
{
	echo $#
}
#===================================================================
s_count_lines()	# (c)RHReepe. Returns count of lines in FILENAME
#===================================================================
# Arg_1 = FILENAME
{
	line_count=`wc -l $1 | cut -c1-8`
	expr $line_count + 0
}
#===================================================================
s_length()	# (c)RHReepe. Returns length of string
#===================================================================
# Arg_1 = string
{
	length=`echo "$@" | wc -c | cut -c1-8`
	length=`expr $length -1`
}
#===================================================================
s_right_pad()	# (c)RHReepe. Right pads a string to width
#===================================================================
# Arg_1 = string
# Arg_2 = width
{
	string="$1                                         "
	echo "$string" | cut -c1-$2
}
#===================================================================
s_left_pad()	# (c) RHReepe. Left pads a string to width
#===================================================================
# Arg_1 = string
# Arg_2 = width
{
	string="                                         $1"
	length=`s_length "$string"`
	echo "$string" | cut -c`expr $length - $2`-$length
}
#===================================================================
s_find_char_all() # (c) RHReepe. Finds all positions of CHAR in LIST
#===================================================================
# Arg_1 = CHAR
# Arg_n = LIST
{
	echo "$@" | cut -f2- -d\ | sed -e 's/./&\
	/g' | grep -n $1 | cut -f1 -d:
}
#===================================================================
s_find_char() # (c) RHReepe. Finds first position of CHAR in LIST
#===================================================================
# Arg_1 = CHAR
# Arg_n = LIST
{
		s_find_word_all "$@" | head -1
}
#===================================================================
s_get_char()	# (c) RHReepe. Returns the CHAR at POSITION in LIST
#===================================================================
# Arg_1 = POSITION
# Arg_n = LIST
{
	echo "$@" | cut -f2- -d\ | cut -c$1 -d\
}
#===================================================================
s_find_word_all() # (c) RHReepe. Finds all positions of word in list
#===================================================================
# Arg_1 = word
# Arg_n = list
{
	echo $@ | cut -f2- -d\ | tr ' ' '\n' \
		| grep -n $1 | cut -f1 -d:

#===================================================================
s_find_word() # (c) RHReepe. Finds first position of word in list
#===================================================================
# Arg_1 = word
# Arg_n = list
{
	s_find_word_all $@ | head -1
}
#===================================================================
s_get_word()	# (c) RHReepe. Returns the word at position in list
#===================================================================
# Arg_1 = position
# Arg_n = list
{
	echo $@ | cut -f2- -d\ | cut -f$1 -d\
}

imple Utility Functions

This next group do not fit in any other category, so I have lumped them all together for now. You have met two already from this group, but these are the final versions.


#===================================================================
s_version()	# (c) RHReepe. Returns 1 (cron) or 0 (not cron)
#===================================================================
# Arg_1 = [SCRIPT_NAME]
{
	head -3 ${1:-$0} | grep "(c)" | grep Version | cut -f9 -d\ 
}
#===================================================================
s_is_cron()	# (c) RHReepe. Returns 1 (cron) or 0 (not cron)
#===================================================================
{
	tty | grep -ci not
}
#===================================================================
s_syntax()	# (c) RHReepe. Checks Script Syntax Usage
#===================================================================
# Arg_1 = $# (Literally $# in the calling script)
# Arg_n = REQUIRED_ARGS
{
	arg_list=`echo $@ | cut -f2- -d\ `
	if [ "$arg_list" = "" ]
	then
		required_args=0
	else
		required_args=`s_count_args $arg_list`
	fi
	if [ $1 != $required_args ]
	then
		echo ""
		echo "Syntax Error in $0"
		echo "Usage: $0 $arg_list"
		echo ""
		exit
	else
		break
	fi
}

SQL*Net 1.0 Functions

This next group of functions were created to help with distributed database connections. They are all based on the Oracle SQL*Net 1.0 protocol. There is a matching set for SQL*Net 2.1 later in this section. To understand these functions fully, you may need to make reference to some other files listed in the last section - Putting It All Together. I will highlight this again nearer the time.


#===================================================================
s_sid_list()	# (c) RHReepe. Returns list of DB SID's on HOST
#===================================================================
# Arg_1 = [HOSTNAME:-THISHOST]
{
	tcpctl stat @${1:-$THISHOST} 2> /dev/null	| \
	grep "ORACLE SID"			| \
	cut -f2 -d:					| \
	tr ',' ' '
}
#===================================================================
s_connect_list()	# (c) RHReepe. Returns list of Global Connect Strings
#===================================================================
{
	connect_strings=""
	for next_host in $REFERENCE_DBSERVER
	do
	   list_of_database_sids=`s_sid_list $next_host`
	   for next_sid in $list_of_database_sids
	   do
	      connect_strings="$connect_strings @t:$next_host:$next_sid"
	   done
	done
	echo $connect_strings
}
#===================================================================
s_select_db()	# (c) RHReepe. Returns list DB's of TYPE
#===================================================================
# Arg_1 = DATABASE_TYPE
# Arg_2 = [DATABASE_SERVER:-THISHOST]
{
	type_list=""
	wanted_type=`s_lower $1 | cut -c1`
	database_list=`s_sid_list ${2:-$THISHOST}`
	for database in $database_list
	do
		db_type=`echo $database | grep -c "_$wanted_type"`
		if [ $db_type -gt 0 ]
		then
			type_list="$type_list $database"
		fi
	done
	echo $type_list
}
