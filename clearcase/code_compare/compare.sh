processLine(){
	#get the path from the file & convert it to cygwin path
	line="$@"
	linelength=`expr length $line`
   #get the index of the comma
	comma_index=`expr index "$line" ,`
	#extract before comma to a string
	filename=`expr substr $line 1 $(($comma_index-1))`
	#extract after comma to a string
	path=`expr substr $line $(($comma_index+1)) $linelength`"/"
   
   #append strings for full path
	fullpath=`cygpath -w "M:/Jordan.M.Klein_TIERS_Project_APT_dyn/"${path}${filename}`
   activity=`cleartool describe "$fullpath" | grep TAA`
   created=`cleartool describe "$fullpath" | grep created`
   #cleartool=`cleartool describe "$fullpath"`
   #echo $cleartool
   #echo $filename","$path","$activity","$cleartool","$created >> "output.csv"
   #echo $filename
}

# Make sure we get file name as command line argument 
# Else read it from standard input device
if [ "$1" == "" ]; then
   FILE="./dif.csv"
else
   FILE="$1"	
   # make sure file exist and readable
   if [ ! -f $FILE ]; then 
  	echo "$FILE : does not exists"
  	exit 1
   elif [ ! -r $FILE ]; then
  	echo "$FILE: can not read"
  	exit 2
   fi
fi
`sed 's/\//\\/g' <dif.csv >dif_new.csv`
# read $FILE using the file descriptors
exec 3<&0
exec 0<$FILE
numLines=`wc -l dif.csv`
while read line
do
   echo $numLines
   numLines=$(($numLines-1))
   # use $line variable to process line in processLine() function
	processLine $line
done
exec 0<&3
exit 0
