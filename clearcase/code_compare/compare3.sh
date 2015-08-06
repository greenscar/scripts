processLine(){
   activity=`cleartool describe -fmt '%[activity]p\n' "$fullpath"`
   echo -n "${file},${path}," | tee -a output.csv
   cleartool desc -fmt '%[name]p,%[owner]p,%[headline]p\n' activity:${activity}@\\TAA_projects | tee -a output.csv
}

if [ -s $1 ]
then
   echo "$1 exists and is not empty"
else
   exit 0
fi

numLines=`tail +4 $1 | wc -l`
counter=1
echo "FILE, PATH, ACTIVITY,OWNER, DESCRIPTION" >> "output.csv"
for i in `tail +4 $1 | cut -d, -f1-2`
do
   echo $counter,"of",$numLines
   file=`echo $i | awk -F, '{print $1}'`
   path=`echo $i | awk -F, '{print $2}'`
   stream="M:\\Jordan.M.Klein_TIERS_Project_APT_dyn"
   fullpath="${stream}\\${path}\\${file}"
   unixpath=`cygpath -u $fullpath`
   if [ -e $unixpath ]
   then
     processLine $fullpath
   fi
  counter=$(($counter+1)) 
done
exit 0
