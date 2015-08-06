#!/usr/bin/perl
%evts = (
  "AST01" => "<a href=\"http://TIERS-AST01/SELoginAccess.jsp\">TIERS-AST01</a>",
  "AST02" => "<a href=\"http://TIERS-AST02/SELoginAccess.jsp\">TIERS-AST02</a>",
  "AST03" =>  "<a href=\"http://TIERS-AST03/SELoginAccess.jsp\">TIERS-AST03</a>",
  "APT01" => "<a href=\"http://TIERS-APT01/SELoginAccess.jsp\">TIERS-APT01</a>",
  "APT02" => "<a href=\"http://TIERS-APT02/SELoginAccess.jsp\">TIERS-APT02</a>",
  "AGING01" => "<a href=\"http://TIERS-AGING01/SELoginAccess.jsp\">TIERS-AGING01</a>",
  "AGING02" => "<a href=\"http://TIERS-AGING02/SELoginAccess.jsp\">TIERS-AGING02</a>",
  "AGING03" => "<a href=\"http://TIERS-AGING03/SELoginAccess.jsp\">TIERS-AGING03</a>",
  "MISC01" =>  "<a href=\"http://TIERS-MISC01/SELoginAccess.jsp\">TIERS-MISC01</a>",
  "MISC02" => "<a href=\"http://TIERS-MISC02/SELoginAccess.jsp\">TIERS-MISC02</a>",
  "IPT" => "<a href=\"https://ipt.txaccess.net/ipt-tiers/LoginServlet?ACTION=LOGIN\">TIERS-IPT</a>"
);
%build_types = (
  "bothdeploy" => "Deploy Batch & Online",
  "buildboth" => "Build & Deploy Batch & Online",
  "buildonlineonly" => "Build & Deploy Online",
  "buildbatchonly" => "Build & Deploy Batch",
  "deployboth" => "Deploy Batch & Online",
  "deployonlineonly" => "Deploy Online",
  "deploybatchonly" => "Deploy Batch",
  "javaDocGen" => "Generate JavaDocs",
  "batchdeploy" => "Deploy Batch"
);
@build_boxes = ("ietadu001", "iedaau019");
$time = localtime;
$numLines=`crontab -l | wc -l`;
$numLines=$numLines - 3;
$output = "<html><head><title>TIERS Build Schedule - $time</title></head><body>\n";
$output .= "<div style=\"text-align:center\">\n";
$output .= "<h1 align=center>TIERS Build Schedule</h1>\n";
$output .= "<h3 align=center>Last Updated On: " . $time . "</h3>\n";
$output .= "<table border=1>";
$output .= "<tr><th>TIME</th><th>Stream</th><th>Environment</th><th>Release</th><th>Build Box</th><th>Build Type</th></tr>";
foreach $build_box (@build_boxes)
{
   @cron = `ssh ccbuild\@$build_box "crontab -l"`;
   foreach $line (@cron)
   {
      #print($line);
      @daLine = split(/\s+/, $line);
      if(($daLine[0] =~ /^[0-9]+/)  && (index($daLine[5],"/home/ccbuild/tiersbuild/harmony/scripts") == 0))
       {
         $output .= "<tr>\n";
         $hr = $daLine[1];
         $min = $daLine[0];
         $day = $daLine[2];
         $month = $daLine[3];
         $day_of_week = $daLine[4];
         $script = $daLine[5];
         if(length($hr) == 1)
         {
            $hr = "0" . $hr;
         }
         if(length($min) == 1)
         {
            $min = $min . "0";
         }
   
         @descr = split(/\"/, $line);
         $description = $descr[1];
         $output .= "<td>" . $hr . "" . $min . "</td>\n";
         @ar = split(/\s+/, `cat $script`);
         $scr = @ar[0];
         if($scr =~ /build.sh$/)
         {
            # Old sh script
            $scr = $ar[0];
            $envid = $ar[1];
            $stream = $ar[2];
            $release = $ar[3];
            $bt = $ar[4];
            $output .= "<td>" . $stream . "</td>\n<td>" . $evts{$envid} . "</td>\n<td>" . $release . "</td>\n<td>$build_box</td>\n<td>" . $build_types{$bt} . "</td>\n";
         }
         if($scr !~ /build.sh$/)
         {
            # New pl script
            #/home/ccbuild/tiersbuild/harmony/scripts/autobuild.pl --stream TIERS_60.1.0.0_TST --bldname APT --target buildboth_ws --deploy APT02 --rel 60.1
            $stream = @ar[2];
            $stream_level = @ar[4];
            $target = @ar[6];
            $deploy_to = @ar[8];
            $release = @ar[10];
            $output .= "<td>" . $stream . "</td>\n<td>" . $evts{$deploy_to} . "</td>\n<td>" . $release . "</td>\n<td>$build_box</td>\n<td>" . $build_types{$target} . "</td>\n";   
         }
         $output .= "</tr>\n";
       }
   }
}
$output .= "</table>\n</div>\n";
$output .= "\n</body>\n</html>\n";
$rc = system("rm -f /home/ccbuild/james/build_schedule.htm");
if ($rc) {
   @errMsg = ("FATAL:   Could not remove the current build_schedule.htm!");
   print(@errMsg);
   exit($rc);
}
#`rm -f /home/ccbuild/james/build_schedule.htm`;
open(DAT,">/home/ccbuild/james/build_schedule.htm") || die("Cannot Open /home/ccbuild/james/build_schedule.htm to Write!");
print DAT "$output\n"; 
close(DAT);
$rc = system("ssh wasadmin\@iedaau019 \"cp -f /home/ccbuild/james/build_schedule.htm /var/apache/htdocs/build_schedule.htm; chmod 744 /var/apache/htdocs/build_schedule.htm\"");
if ($rc) {
   @errMsg = ("FATAL:   Could not copy the file to htdocs!");
   print(@errMsg);
   exit($rc);
}



