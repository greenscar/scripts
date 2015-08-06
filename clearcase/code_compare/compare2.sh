processLine(){
	#get the path from the file & convert it to cygwin path
#	line="$@"
#	linelength=`expr length $line`
   #get the index of the comma
#	comma_index=`expr index "$line" ,`
	#extract before comma to a string
#	filename=`expr substr $line 1 $(($comma_index-1))`
	#extract after comma to a string
#	path=`expr substr $line $(($comma_index+1)) $linelength`"/"
   
   #append strings for full path
#	fullpath=`cygpath -w "M:/Jordan.M.Klein_TIERS_Project_APT_dyn/"${path}${filename}`
#   activity=`cleartool describe "$fullpath" | grep TAA`
#   created=`cleartool describe "$fullpath" | grep created`
   activity=`cleartool describe -fmt '%[activity]p\n' "$fullpath"`
#   owner=`cleartool describe -fmt '%[owner]p\n' activity:${activity}@\\TAA_projects`
#   desc=`cleartool describe -fmt '%[headline]p\n' activity:${activity}@\\TAA_projects`
   echo -n "${file},${path}," | tee -a output.csv
   cleartool desc -fmt '%[name]p,%[owner]p,%[headline]p\n' activity:${activity}@\\TAA_projects | tee -a output.csv
   #cleartool=`cleartool describe "$fullpath"`
   #echo $cleartool
   #echo $filename","$path","$activity","$cleartool","$created >> "output.csv"
   #echo $filename
}

numLines=`tail +4 dif.csv | wc -l`
counter=1
echo "FILE, PATH, ACTIVITY,OWNER, DESCRIPTION" >> "output.csv"
for i in `tail +4 dif.csv | cut -d, -f1-2`
do
#  echo $numLines
#  echo $counter
#  numLines=$(($numLines-1))
#  if [ $counter -gt 3 ]; then
   #echo $i
   file=`echo $i | awk -F, '{print $1}'`
   path=`echo $i | awk -F, '{print $2}'`
   stream="M:\\Jordan.M.Klein_TIERS_Project_APT_dyn"
   fullpath="${stream}\\${path}\\${file}"
#   echo $fullpath
#   processLine $line
   unixpath=`cygpath -u $fullpath`
   if [ -e $unixpath ]
   then
     processLine $fullpath
   fi
#  fi
  counter=$(($counter+1)) 
done
exit 0
