#! /bin/bash
alphabet=(A B C D E F G H I J K L M N O P Q R S T U V W X Y Z)
c1=0
c3=0
wrd=""
echo "START" > whois.results;
while [ $c1 -lt 26 ]; do
   c2=0
   while [ $c2 -lt 26 ]; do
      c3=0
      while [ $c3 -lt 26 ]; do
         c4=0
         while [ $c4 -lt 26 ]; do
            wrd="${alphabet[$c1]}${alphabet[$c2]}${alphabet[$c3]}${alphabet[$c4]}.COM"
            #    wrd="CAFFEIN${alphabet[$c4]}.com"
            #echo $wrd;
            digresults=`/usr/bin/dig $wrd | grep 'ANSWER SECTION'`    
            if [ -n "$digresults" ]
            then
               #echo "$wrd TAKEN"
               echo "$wrd TAKEN" >> whois.progress
            else
               #echo "$wrd NULL - checking whois"
               echo "$wrd NULL - checking whois" >> whois.progress
               whoisresults=`/usr/bin/whois $wrd | grep 'No match'`
               if [ -n $whoisresults ]
               then
                  echo "$wrd TAKEN" >> whois.progress
               else
                  echo $whoisresults >> whois.results
                  echo "$wrd NO MATCH!!!!" >> whois.progress
               fi
            fi
            c4=$((c4+1))
         done
         c3=$((c3+1))
      done
      c2=$((c2 + 1))
   done
   c1=$((c1 + 1))
done
echo "DONE" >> whois.results;


if [ $# -ne 1 ]; then
   echo 1>&2 Usage: dc.ksh DOMAIN_LENGTH
   exit 127
fi
while [ $strlen -lt $1 ]
do
done


get_first_char()
{
}