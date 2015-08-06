#!/usr/bin/perl
#my($str) = "ls -l /var/mail/virtual/james/cur/1257872382.M561834P24913V0000000000000811I000B8D11_118.server.sa*";
my($str) = "ls -ltS /var/mail/virtual/james/cur/*.*";
my(@lines) = `$str`;
my($prevsize);
my($prevdate);
my($prevtime);
foreach $line (@lines)
{
        my(@props) = split(" ", $line);
        my($thissize) = @props[4];
        my($thisdate) = @props[5];
        my($thistime) = @props[6];
        my($thisfilename) = @props[7];
#        print("THIS = $thissize - $thisdate - $thistime - $thisfilename\n");
#	print("LAST = $prevsize - $prevdate - $prevtime\n");
        if(($prevsize == $thissize) && ("$prevdate" eq "$thisdate") && ("$prevtime" eq "$thistime"))
        {
#print("rm /var/mail/virtual/james/cur/$thisfilename\n");
            `rm /var/mail/virtual/james/cur/$thisfilename`;
        }
        else
        {
#            print("SAVE $thisfilename\n");
            $prevsize = $thissize;
            $prevdate = $thisdate;
            $prevtime = $thistime;
        }
}

