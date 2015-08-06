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
  "buildboth_ws" => "Build & Deploy Batch & Online",
  "buildboth_ws_trackwise" => "Build & Deploy Batch & Online & Generate Track JARs",
  "buildonline_ws" => "Build & Deploy Online",
  "javaDocGen" => "Generate JavaDocs",
  "batchdeploy" => "Deploy Batch",
  "buildbatchonly" => "Build & Deploy Batch Only"
);
@build_boxes = ("ietadu001", "iedaau019");
@builds = ();
$time = localtime;
$numLines=`crontab -l | wc -l`;
$numLines=$numLines - 3;
foreach $build_box (@build_boxes)
{
   @cron = `ssh ccbuild\@$build_box "crontab -l"`;
   foreach $line (@cron)
   {
      #print($line);
      @daLine = split(/\s+/, $line);
      if(($daLine[0] =~ /^[0-9]+/)  && (index($daLine[5],"/home/ccbuild/tiersbuild/harmony/scripts") == 0))
      {
         $b = new Build->new();
         #print("arraysize = " . $b->getArraySize(@daLine) . "\n");
         $b->hr($daLine[1]);
         $b->min($daLine[0]);
         $b->day($daLine[2]);
         $b->month($daLine[3]);
         $b->day_of_week($daLine[4]);
         $b->script($daLine[5]);
         $b->build_box($build_box);
         $b->processscript();
         push(@builds, $b);
       }
       #print("------------------------------------------------\n");
   }
}

for($OL = 0; $OL <= 10; $OL++)
{
   for($IL = $OL; $IL <= 10; $IL++)
   {
      #
      # ascending <
      # decending >
      #
      if($builds[$IL]->hr < $builds[$OL]->hr)
      {
         $Temp = $builds[$IL];
         $builds[$IL] = $builds[$OL];
         $builds[$OL] = $Temp;
      }
      elsif(($builds[$IL]->hr == $builds[$OL]->hr) && ($builds[$IL]->min < $builds[$OL]->min))
      {
         $Temp = $builds[$IL];
         $builds[$IL] = $builds[$OL];
         $builds[$OL] = $Temp;
      }
   }
}

# AT THIS POINT WE HAVE AN ARRAY OF Build OBJECTS CALLED @builds.
# USE THIS ARRAY TO GENERATE THE PAGE.
$output = "<html><head><title>TIERS Build Schedule - $time</title></head><body>\n";
$output .= "<div style=\"text-align:center\">\n";
$output .= "<h1 align=center>TIERS Build Schedule</h1>\n";
$output .= "<h3 align=center>Last Updated On: " . $time . "</h3>\n";
$output .= "<table border=1>";
$output .= "<tr><th>TIME</th><th>Stream</th><th>Environment</th><th>Build Box</th><th>Build Type</th></tr>";
foreach(@builds)
{
   my($a_build) = $_;
   $output .= "<tr>\n";
   $output .= "\t<td>" . $a_build->hr . $a_build->min . "</td>\n";
   $output .= "\t<td>" . $a_build->srcstream. "</td>\n";
   $output .= "\t<td>" . $evts{$a_build->deployto()}. "</td>\n";
   $output .= "\t<td>" . $a_build->build_box. "</td>\n";
   $output .= "\t<td>" . $build_types{$a_build->antcall()}. "</td>\n";
   $output .= "</tr>\n";
}
$output .= "</table>\n</div>\n";
$output .= "\n</body>\n</html>\n";

#REMOVE EXISTING BUILD SCHEDULE
$rc = system("rm -f /home/ccbuild/build_schedule.htm");
if ($rc) {
   @errMsg = ("FATAL:   Could not remove the current build_schedule.htm!");
   print(@errMsg);
   exit($rc);
}
# WRITE BUILD SCHEDULE IN MEMORY TO FILE.
open(DAT,">/home/ccbuild/build_schedule.htm") || die("Cannot Open /home/ccbuild/build_schedule.htm to Write!");
print DAT "$output\n"; 
close(DAT);

# COPY THE BUILD SCHEDULE TO THE HTTP SERVER
$rc = system("ssh wasadmin\@iedaau019 \"cp -f /home/ccbuild/build_schedule.htm /var/apache/htdocs/build_schedule.htm; chmod 744 /var/apache/htdocs/build_schedule.htm\"");
if ($rc) {
   @errMsg = ("FATAL:   Could not copy the file to htdocs!");
   print(@errMsg);
   exit($rc);
}


sub getArraySize
{
   my @intArray = @_;
   $lastIndex = $#intArray; # index of the last element, $lastIndex == 2
   $length = $lastIndex + 1; # size of the array
   return($length);
}



###############################################################################
# Activity Object 
###############################################################################
package Build;
#constructor
sub new
{
   my $self = {};
   $self->{hr} = undef;
   $self->{min} = undef;
   $self->{day} = undef;
   $self->{month} = undef;
   $self->{day_of_week} = undef;
   $self->{script} = undef;
   $self->{srcstream} = undef;
   $self->{antcall} = undef;
   $self->{deployto} = undef;
   $self->{skipbl} = 0;
   $self->{force} = 0;
   bless $self;
   return $self;
}


sub hr
{    
   my ( $self, $hr ) = @_;
   if(defined($hr))
   {
      #print("hr($hr)\n");
      if(length($hr) == 1)
      {
         $hr = "0" . $hr;
      }   
      $self->{hr} = $hr;
   }
   #print($self->{hr});
   return $self->{hr};
}
sub min
{    
   my ( $self, $min ) = @_;
   if(defined($min))
   {   
      #print("min($min)\n");
      if(length($min) == 1)
      {
         $min = $min . "0";
      }
      $self->{min} = $min;
   }
   return $self->{min};
}
sub day
{    
   my ( $self, $day ) = @_; 
   if(defined($day))
   {
      #print("day($day)\n");                        
      $self->{day} = $day;
   }
   return $self->{day};
}
sub month
{    
   
   my ( $self, $month ) = @_;
   if(defined($month))
   {
      #print("month($month)\n");
      $self->{month} = $month;
   }
   return $self->{month};
}
sub day_of_week
{    
   my ( $self, $day_of_week ) = @_;   
   if(defined($day_of_week))
   {
      #print("day_of_week($day_of_week)\n");
      $self->{day_of_week} = $day_of_week;
   }
   return $self->{day_of_week};
}
sub script
{    
   my ( $self, $script ) = @_;
   if(defined($script))
   {
      #print("script($script)\n");
      $self->{script} = $script;
   }
   return $self->{script};
}
sub srcstream
{    
   my ( $self, $srcstream ) = @_;   
   if(defined($srcstream))
   {
      #print("srcstream()\n");
      $self->{srcstream} = $srcstream;
   }
   return $self->{srcstream};
}
sub antcall
{    
   #print("antcall()\n");
   my ( $self, $antcall ) = @_;   
   if(defined($antcall))
   {
      #print("antcall()\n");
      $self->{antcall} = $antcall;
   }                
   return $self->{antcall};
}
sub deployto
{    
   #print("deployto()\n");
   my ( $self, $deployto ) = @_;   
   if(defined($deployto))
   {
      #print("deployto()\n");
      $self->{deployto} = $deployto;
   }                                       
   #$self->{deployto} = $deployto if defined($deployto);
   return $self->{deployto};
}
sub force
{
   my ( $self, $force ) = @_;                                 
   $self->{force} = $force if defined($force);
   return $self->{force};
}
sub skipbl
{
   my ( $self, $skipbl ) = @_;                                 
   $self->{skipbl} = $skipbl if defined($skipbl);
   return $self->{skipbl};
}
sub build_box
{
   my ( $self, $build_box ) = @_;                                 
   $self->{build_box} = $build_box if defined($build_box);
   return $self->{build_box};
}

sub getArraySize
{
   my @intArray = @_;
   $lastIndex = $#intArray; # index of the last element, $lastIndex == 2
   $length = $lastIndex + 1; # size of the array
   return($length);
}

sub process_cron_line(@daLine)
{
   # 30 13 * * * /home/ccbuild/tiersbuild/harmony/scripts/61.0_APT01_buildbothws.sh
   
}
sub processscript
{
   my ( $self ) = @_;  
   $my_script = $self->script;
   @ar = split(/\s+/, `cat $my_script`);
   $ar_size = $self->getArraySize(@ar);
   for($i = 0; $i < $ar_size; $i++)
   {
      $val = @ar[$i];
      if($val =~ m/\-+/)
      {
         # THIS VAL IS ONE OF THE OPTIONAL ARGS OF THE BUILD SCRIPT.
         # force & skipbl ARE OPTIONAL ARGS THAT DO NOT REQUIRE AN ADDITIONAL
         # VALUE. THUS, IF THESE ARE DEFINED, SIMPLY SET THE VALUE TO TRUE.
         $val = substr($val, 2);
         if($val =~ m/force/)
         {
            $self->force(1);
         }
         elsif($val =~ m/skipbl/)
         {
            $self->skipbl(1);
         }
         # IF THE VAL IS ANYTHING OTHER THAN force OR skipbl, THERE WILL BE AN
         # ARG NEXT. DEFINE THE VALUE.
         else
         {
            $self->$val(@ar[$i+1]);
         }
         #print("self->$val = " . $self->$val . "\n");
      }
   }
#         $scr = @ar[0];
#         # New pl script
#         #/home/ccbuild/tiersbuild/harmony/scripts/autobuild.pl --stream TIERS_60.1.0.0_TST --bldname APT --antcall buildboth_ws --deploy APT02 --rel 60.1
#         $stream = @ar[2];
#         $stream_level = @ar[4];
#         $antcall = @ar[6];
#         $deployto = @ar[8];
#         $release = @ar[10];
#         $output .= "<td>" . $stream . "</td>\n<td>" . $evts{$deployto} . "</td>\n<td>" . $release . "</td>\n<td>$build_box</td>\n<td>" . $build_types{$antcall} . "</td>\n";
#         $output .= "</tr>\n";
}
