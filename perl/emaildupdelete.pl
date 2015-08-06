#!/usr/bin/perl
#my($str) = "ls -l /var/mail/virtual/james/cur/1257872382.M561834P24913V0000000000000811I000B8D11_118.server.sa*";
my($str) = "ls -ltS /var/mail/virtual/james/cur";
#my($str) = "ls -ltS /Users/jsandlin/scripts/mail";
my(@filelist1) = `$str`;
my(@filelist2) = @filelist1;

my($prevsize);
my($prevdate);
my($prevtime);
my($x);
my($y);
for($x = 0; $x < @filelist1; $x++)
{
   #print("filelist1[$x] => " . @filelist1[$x] . "\n");
  my(@props1) = split(" ", @filelist1[$x]);
  my($size1) = @props1[4];
  my($date1) = @props1[5];
  my($time1) = @props1[6];
  my($filename1) = @props1[7];
  #print("size = $size1\ndate = $date1\ntime = $time1\nfilename = $filename1\n");
  for($y = 0; $y < @filelist2; $y++)
  {
      print("filelist2[$y] => " . @filelist2[$y] . "\n");
      my(@props2) = split(" ", @filelist2[$y]);
      print("size2 = " . @props2[4] . "\n");
      print("date2 = " . @props2[5] . "\n");
      print("time2 = " . @props2[6] . "\n");
      print("filename2 = " . @props2[7] . "\n");
      if($size1 == @props2[4] && $date1 == @props2[5] && $time1 == @props2[6] && $filename1 != @props2[7])
      {
          print("delete " . @file2[7] . "\n");
          # remove this element from array 1 & 2
      }
  }
#        if(($prevsize == $thissize) && ("$prevdate" eq "$thisdate") && ("$prevtime" eq "$thistime"))
#        {
#            `rm /var/mail/virtual/james/cur/$thisfilename`;
#        }
#        else
#        {
##            print("SAVE $thisfilename\n");
#            $prevsize = $thissize;
#            $prevdate = $thisdate;
#            $prevtime = $thistime;
#        }

}

