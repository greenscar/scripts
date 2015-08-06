#!/usr/bin/perl
#
# We have no documentation regarding what process is running on what box. Therefore,
# this script will ssh to each box & get a report of what java processes are running.
#
use strict;
use warnings;
#use Net::SSH::Perl;

my(@BOXLIST) = ("SEA1TPCONF01", "SEA1TPCONF02", "SEA1TPMPX01", "SEA1TPMPX02", "SEA1TPMPX91", "SEA1TPPUBLISH01", "SEA1TPPUBLISH02", "SEA1TPPUBWORK01", "SEA1TPPUBWORK02", "SEA1TPPUBLISH91", "SEA1TPPUBWORK91", "sea1tpdelcfg01", "sea1tpdelcfg02", "sea1tpdelcfg03", "sea1tpdelcfg91", "sea1tpdelcfg92", "SEA1TPRPT01", "SEA1TPRPT02", "sea1tpmetrics01", "sea1tpmetrics02", "SEA1TPTASKSVC01", "SEA1TPTASKSVC02", "SEA1TPTASKSVC91", "SEA1TPMQ03", "SEA1TPMQ04", "sea1tpingest01", "sea1tpingest02", "Sea1tpingest91", "SEA1TPAUTHADM02", "SEA1TPAUTHADM03", "SEA1TPAUTHADM04", "SEA1TPAUTHADM05", "SEA1TPAUTHADM06", "SEA1TPAUTHADM07", "SEA1TPAUTHADM08", "sea1tpwf01", "sea1tpwf02", "sea1tpwf03", "SEA1TPAUTHUSR01", "SEA1TPAUTHUSR02", "SEA1TPAUTHUSR03", "SEA1TPAUTHUSR04", "SEA1TPAUTHUSR05", "SEA1TPAUTHUSR06", "SEA1TPAUTHUSR07", "SEA1TPAUTHUSR08", "SEA1TPCMTY01", "SEA1TPCMTY02", "SEA1TPCMTY03", "SEA1TPCMTY04", "SEA1TPCMTY91", "SEA1TPCMTY92", "SEA1TPCMTY05", "SEA1TPMGMT01", "SEA1TPMGMT02", "SEA1TPMGMT03", "sea1tprights01", "sea1tprights02", "sea1tprights03", "sea1tprights91", "sea1tpcmrc01", "sea1tpcmrc02", "sea1tpcmrc91", "sea1tpchkout01", "sea1tpchkout02", "sea1tpfeedrdr01", "sea1tpfeedrdr02");
#my(@BOXLIST) = ("sea1tpwf01");
my($TMPFILE) = "./javaprocesses.csv";

my($cmd) = undef;
open(FILE, ">", "$TMPFILE") or die $!;
my($dontmatch) = 0;
for my $box (@BOXLIST)
{
   $cmd = "ssh $box 'ps -fu jetty 2>/dev/null' 2>/dev/null";
   #println($box);
   my(@output) = `$cmd`;

   #print(FILE "\n-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-\n$box\n");
   foreach(@output)
   {
      my($port) = "___";
      my($app) = "___";
      my($jettyapp) = 0;
      my($appname);
      if(/^jetty\s+\d+\s+\d+\s+\d+\s+\w{3}\d{2}\s*\?\s+\d{2}\:\d{2}\:\d{2}\s*(.*)$/)
      {
         $jettyapp = $1;
         $appname = $1;
         #println($2);
      }
      elsif(/^jetty\s+\d+\s+\d+\s+\d+\s+\w{3}\d{2}\s*\?\s+(\d\-)\d{2}\:\d{2}\:\d{2}\s*(.*)$/)
      {
         $jettyapp = $2;
         $appname = $2;
         #println($2);
      }
      elsif(/^jetty/)
      {
         $dontmatch++;
      }
      #println("jettyapp = $jettyapp");
      if($jettyapp)
      {
         if(/\-Djetty\.port\=(\d\d\d\d)/)
         {
            $port = $1;
         }
         elsif(/\-Dcom\.sun.management\.jmxremote\.port=(\d\d\d\d)/)
         {
            $port = $1;
         }
         
         if(/\-Djava\.library\.path=\/app\/(jetty-\w*jar)\//)
         {
            $app = $1;
         }
         elsif(/\-jar\s\/app\/(jetty-\S*)\//)
         {
            $app = $1;
         }
         elsif(/\-classpath\s(\S+.jar)/)
         {
            $app = $1;
         }
         elsif(/\/usr\/java\/latest\/bin\/java.*\s(.*\.jar)$/)
         {
            $app = $1;
         }
         elsif(/java\s\-cp\s(\S*)\s/)
         {
            $app = $1;
         }
         #if($jettyapp)
         #{
            print(FILE "$box,$port,$app,$jettyapp\n");
            #println("$box,$port,$app,$_\n");
         #}  
      }
   }
   
}
close(FILE);
#println("dontmatch = $dontmatch");
sub println {
	my (@msg) = @_;
	foreach (@msg) {
      print($_ . "\n");
	}
}
