#!/usr/bin/ksh
find ./* -type f -name "*.ksh" -exec perl -pi -e "s/ENV/$1/g" {} \; 
find ./* -type f -name "*.properties" -exec perl -pi -e "s/ENV/$1/g" {} \; 
find ./* -type f -name "*.xml" -exec perl -pi -e "s/ENV/$1/g" {} \; 
