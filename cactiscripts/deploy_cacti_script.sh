#!/bin/bash


file=$1
shift
boxes=$@

tempdir=/home/SEA1/james.sandlin
destdir=/root

tempuser=james.sandlin
user=root


for destbox in $boxes
do
   `scp $file $tempuser:$destbox$tempdir/$file`;
   `ssh $destbox;sudo mv $tempdir/$file $destdir/$file`; 
done;


exit 0
