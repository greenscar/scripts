#!/usr/bin/perl
$t =  $ARGV[0];
print "\nt = $t\n";
if("$t" =~ /LOCAL/)
         { 
            print("NO\n");
         }
         else
         {
            print("YES\n");
         }
