#! /bin/bash
alphabet=(A B C D E F G H I J K L M N O P Q R S T U V W X Y Z 1 2 3 4 5 6 7 8 9 0)
c1=0
c2=0
c3=0
c4=0
numWhoIs=0
wrd=""
while [ $c1 -lt 26 ]; do
 while [ $c2 -lt 26 ]; do
   while [ $c3 -lt 26 ]; do
     while [ $c4 -lt 26 ]; do
       wrd="${alphabet[$c1]}${alphabet[$c2]}${alphabet[$c3]}${alphabet[$c4]}.COM"
#       wrd="${alphabet[$c1]}${alphabet[$c2]}${alphabet[$c3]}.COM"
       #wrd="GF${alphabet[$c1]}${alphabet[$c2]}.COM"
       digresults=`/usr/bin/dig $wrd | grep 'SECTION'`
       echo "$wrd" >> whois.progress
       if [ "$digresults" ]
       then
	  echo `/bin/date` " - $wrd TAKEN" 
       else
	  echo `/bin/date` " - $wrd CHECK"
	  /usr/bin/whois $wrd >> whois.$wrd.results
          resultssorry=`grep 'Sorry that it' whois.$wrd.results` 
          if [ "$resultssorry" ]
          then
             echo `/bin/date` " - $wrd TAKEN" 
          else
             resultsupdate=`grep 'Update' whois.$wrd.results`
             if [ "$resultsupdate" ]
             then
                echo `/bin/date` " - $wrd TAKEN"
             else
                resultsupdate=`grep 'expires on\|Expiration Date' whois.$wrd.results`
                if [ "$resultsupdate" ]
                then
                   echo `/bin/date` " - $wrd TAKEN"
                else
                   echo "----------" `/bin/date` " - $wrd AVAILABLE OR EXPIRING"
                   echo "$wrd" >> whois.available
                fi
             fi
          fi
	  echo "$wrd" >> whois.checkit
       fi
       c4=$((c4+1))
    done
    c4=0
    c3=$((c3+1))
   done
   c3=0
   c2=$((c2+1))
 done
 c2=0
 c1=$((c1+1))
done
#echo "DONE" >> whois.results;
