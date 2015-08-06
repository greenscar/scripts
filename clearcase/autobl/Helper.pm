#!/opt/rational/clearquest/bin/cqperl

# Script:      streamread.pl
# Version:     2.0
# Author:      James Sandlin <james.g.sandlin@hhsc.state.tx.us>
# Date:        2008-02-05
# Purpose:     Generates a report of all ClearCase activities in a build of type:
#              "BaseCMActivity", "ChangeActivity", "UCMUtilityActivity", "Defect"
#              A stream name is provided. The script looks at the date of the last 
#              build & the current build. It gets dates from those files and finds
#              All deliveries to the build stream in that time range. It then
#              recursively traces those activites back to their initial contributing
#              activity.

# Syntax:      streamread.pl ARGS
# Arguments:   --stream    n     Name of stream to find initial activities

package Helper;
use File::Listing;
use Time::localtime;

sub new
{
   my $self = {};
   my $debug = undef;
   #my $os = undef;
   #my $ucm_stream = undef;
   my $view_name = undef;
   my $DEV, $SIT, $UAT, $LOCAL, $INT = undef;
   my $STREAM_STRUCTURE_NORM, $STREAM_STRUCTURE_EMER = undef;
   my $STREAM_STRUCTURES = undef;
   
   my $COMPOSITE = undef;
   
   #my $datestring_prior_build = undef;
   #my $datestring_this_build = undef;
   #my $datestring_prior_view_update = undef;
   #my $timestamp_prior_build = undef;
   #my $timestamp_prior_view_update = undef;
   #my $timestamp_this_build = undef;
   #my $timestamp_this_view_update = undef;
   
   #my $dest_file_name = undef;
   #my $dest_dir = undef;
   my $cqlogin = undef;
   my $cqpwd = undef;
   my $cqdbname = undef;
   my $cqdbset = undef;
   my $blfmt = undef;
   my $mkbloptions = undef;
   #my $sub_dir = "";
   my $entity_defs = ();
   
   
   bless $self;
   $self->entity_defs("BaseCMActivity", "ChangeActivity", "UCMUtilityActivity");
   $self->cqlogin("ccbuild");
   $self->cqpwd("ClearQuest");   
   #$self->cqdbname("TAA");
   $self->cqdbname("PRCT3");
   
   $self->DEV("DEV");
   $self->SIT("SIT");
   $self->UAT("UAT");
   $self->LOCAL("LOCAL");
   $self->TST("TST");
   $self->STREAM_STRUCTURE_NORM("NORM");
   $self->STREAM_STRUCTURE_EMER("EMER");
   $self->COMPOSITE("COMPOSITE");
   
   $self->{STREAM_STRUCTURES} = (
      $self->STREAM_STRUCTURE_NORM() => [$self->LOCAL(), $self->DEV(), $self->SIT(), $self->UAT()],
      $self->STREAM_STRUCTURE_EMER() => [$self->LOCAL(), $self->UAT()]
      );
   
   $self->blfmt("\%[found_bls]CXp");
   $self->mkbloptions("-full -ident");

   #if($self->os_is_windows())
   #{
   #   #$self->bfroot("c:\\views\\");
   #   $self->cqdbset("2003.06.00");
   #   #$self->pvob("\@\\DC_Projects");
   #
   #}
   #else
   #{
      #$self->bfroot("/home/ccbuild/");
      #/home/ccbuild/tiersbuild/harmony/tiersapps/SIT/online/ear
      $self->cqdbset("7.0.0");
      #$self->pvob("\@/DC_Projects");
   
   #}
   return $self;
}

sub blfmt
{
   my( $self, $blfmt) = @_;
   $self->{blfmt} = $blfmt if defined($blfmt);
   return $self->{blfmt};
}


sub COMPOSITE
{
   my( $self, $COMPOSITE) = @_;
   $self->{COMPOSITE} = $COMPOSITE if defined($COMPOSITE);
   return $self->{COMPOSITE};
}
sub LOCAL
{
   my( $self, $LOCAL) = @_;
   $self->{LOCAL} = $LOCAL if defined($LOCAL);
   return $self->{LOCAL};
}
sub DEV
{
   my( $self, $DEV) = @_;
   $self->{DEV} = $DEV if defined($DEV);
   return $self->{DEV};
}
sub SIT
{
   my( $self, $SIT) = @_;
   $self->{SIT} = $SIT if defined($SIT);
   return $self->{SIT};
}
sub UAT
{
   my( $self, $UAT) = @_;
   $self->{UAT} = $UAT if defined($UAT);
   return $self->{UAT};
}
sub TST
{
   my( $self, $TST) = @_;
   $self->{TST} = $TST if defined($TST);
   return $self->{TST};
}
sub STREAM_STRUCTURE_NORM
{
   my( $self, $STREAM_STRUCTURE_NORM) = @_;
   $self->{STREAM_STRUCTURE_NORM} = $STREAM_STRUCTURE_NORM if defined($STREAM_STRUCTURE_NORM);
   return $self->{STREAM_STRUCTURE_NORM};
}
sub STREAM_STRUCTURE_EMER
{
   my( $self, $STREAM_STRUCTURE_EMER) = @_;
   $self->{STREAM_STRUCTURE_EMER} = $STREAM_STRUCTURE_EMER if defined($STREAM_STRUCTURE_EMER);
   return $self->{STREAM_STRUCTURE_EMER};
}

sub STREAM_STRUCTURES
{    
   my($self) = shift;
   if(@_){%{$self->{STREAM_STRUCTURES}} = @_};
   return $self->{STREAM_STRUCTURES};
}

sub mkbloptions
{
   my( $self, $mkbloptions) = @_;
   $self->{mkbloptions} = $mkbloptions if defined($mkbloptions);
   return $self->{mkbloptions};
}

sub build_id
{
   my($self) = @_;
   return $self->timestamp_this_build();
}
sub os
{
   my($self) = @_;
   $self->{os} = $^O if(!($self->{os}));
   #if(!($self->{os})){ $self->{os} = $^O; }
   return $self->{os};
}
sub view_dir
{
   my( $self, $view_dir) = @_;
   if(!($self->{view_dir}))
   {
      if($self->os_is_windows())
      {
         $self->{view_dir} = $self->bfroot . "TIERS_" . $self->{ucm_stream};
      }
      else
      {
         $self->{view_dir} = $self->bfroot . $self->{ucm_stream};
      }
   }
   return $self->{view_dir};
}
sub entity_defs
{
   my($self, @defs) = @_;
   @{$self->{entity_defs}} = @defs if(!($self->{entity_defs}));
   #if(!($self->{entity_defs})){ $self->{entity_defs} = $^O; }
   return @{$self->{entity_defs}};
}
sub cqlogin
{
   my( $self, $cqlogin) = @_;
   $self->{cqlogin} = $cqlogin if defined($cqlogin);
   return $self->{cqlogin};
}
sub cqpwd
{
   my( $self, $cqpwd) = @_;
   $self->{cqpwd} = $cqpwd if defined($cqpwd);
   return $self->{cqpwd};
}
sub cqdbname
{
   my( $self, $cqdbname) = @_;
   $self->{cqdbname} = $cqdbname if defined($cqdbname);
   return $self->{cqdbname};
}

sub unixid
{
   my( $self, $unixid) = @_;
   $self->{unixid} = $unixid if defined($unixid);
   return $self->{unixid};
}
sub cqdbset
{
   my( $self, $cqdbset) = @_;
   $self->{cqdbset} = $cqdbset if defined($cqdbset);
   return $self->{cqdbset};
}
sub debug
{
   my( $self, $debug) = @_;
   $self->{debug} = $debug if defined($debug);
   return $self->{debug};
}
#sub max_date
#{
#   my( $self, $max_date) = @_;
#   $self->{max_date} = $max_date if defined($max_date);
#   return $self->{max_date};
#}

sub get_stream_list
{
   my($self, $stream_structure) = @_;
   %{$self->{STREAM_STRUCTURES}->$stream_structure};
}

sub datestring_prior_build
{
   my( $self, $datestring_prior_build) = @_;
   $self->{datestring_prior_build} = $datestring_prior_build if defined($datestring_prior_build);
   if(!defined($self->{datestring_prior_build}))
   {
      my($year, $month, $day, $hour, $minute, $second) = $self->convert_timestamp_to_array($self->timestamp_prior_build());
      #$self->println("$year, $month, $day, $hour, $minute, $second");
      $self->datestring_prior_build("$year-$month-$day " . $hour . ":" . $minute . ":" . $second);
   }
   return $self->{datestring_prior_build};
}
sub datestring_this_build
{
   my( $self, $datestring_this_build) = @_;
   $self->{datestring_this_build} = $datestring_this_build if defined($datestring_this_build);
   if(!defined($self->{datestring_this_build}))
   {
      my($year, $month, $day, $hour, $minute, $second) = $self->convert_timestamp_to_array($self->timestamp_this_build());
      #$self->println("$year, $month, $day, $hour, $minute, $second");
      $self->datestring_this_build("$year-$month-$day " . $hour . ":" . $minute . ":" . $second);
   }
   return $self->{datestring_this_build};
}
sub datestring_prior_view_update
{
   my( $self, $datestring_prior_view_update) = @_;
   $self->{datestring_prior_view_update} = $datestring_prior_view_update if defined($datestring_prior_view_update);
   if(!defined($self->{datestring_prior_view_update}))
   {
      my($year, $month, $day, $hour, $minute, $second) = $self->convert_timestamp_to_array($self->timestamp_prior_view_update());
      #$self->println("datestring prior view update = $year, $month, $day, $hour, $minute, $second");
      $self->datestring_prior_view_update("$year-$month-$day " . $hour . ":" . $minute . ":" . $second);
   }
   return $self->{datestring_prior_view_update};
}

sub datestring_this_view_update
{
   my( $self, $datestring_this_view_update) = @_;
   $self->{datestring_this_view_update} = $datestring_this_view_update if defined($datestring_this_view_update);
   if(!defined($self->{datestring_this_view_update}))
   {
      my($year, $month, $day, $hour, $minute, $second) = $self->convert_timestamp_to_array($self->timestamp_this_view_update());
      #$self->println("datestring this view update = $year, $month, $day, $hour, $minute, $second");
      $self->datestring_this_view_update("$year-$month-$day " . $hour . ":" . $minute . ":" . $second);
   }
   return $self->{datestring_this_view_update};
}
#sub days_back
#{
#   my( $self, $days_back) = @_;
#   $self->{days_back} = $days_back if defined($days_back);
#   return $self->{days_back};
#}
sub dest_dir
{
   my( $self, $dest_dir) = @_;
   $self->{dest_dir} = $dest_dir if defined($dest_dir);
   return $self->{dest_dir};
}
sub dest_file_name
{
   my($self, $tempfilename) = @_;
   if($self->ucm_stream() && $self->datestring_this_build())
   {
      my($tempdate) = $self->datestring_this_build();
      #$tempdate = substr($tempdate, 0, (length($tempdate) - 2));
      $tempfilename = $self->ucm_stream() . "_" . $tempdate . ".htm";
      $tempfilename =~ s/\ /_/g;
      $tempfilename =~ s/://g;
      $self->{dest_file_name} = $tempfilename;
   }
   return $self->{dest_file_name};
}
sub sub_dir
{
   my( $self, $sub_dir) = @_;
   $self->{sub_dir} = $sub_dir if defined($sub_dir);
   return $self->{sub_dir};
}
sub bfroot
{
   my( $self, $bfroot) = @_;
   $self->{bfroot} = $bfroot if defined($bfroot);
   return $self->{bfroot};
}
sub dest_ear_dir
{
   my( $self, $dest_ear_dir) = @_;
   $self->{dest_ear_dir} = $dest_ear_dir if defined($dest_ear_dir);
   return $self->{dest_ear_dir};
}
sub ucm_stream
{
   my( $self, $ucm_stream) = @_;
   if(!($self->{ucm_stream}))
   {
      $self->{ucm_stream} = $ucm_stream;
      if($self->os_is_windows())
      {
         $self->dest_dir("c:\\webserver\\htdocs\\buildreports\\" . $self->ucm_stream . "\\");
         $self->view_name("jsandlin_" . $ucm_stream);
         $self->dest_ear_dir($self->bfroot() . "tiersbuild\\harmony\\tiersapps\\" . $self->ucm_stream() . "\\online\\ear\\");
         #$self->dest_ear_dir("C:\\webserver\\htdocs\\buildreports\\");
      }
      else
      {
         $self->dest_dir("/var/apache/htdocs/harMony/build_reports/" . $self->ucm_stream . "/");
         $self->view_name("ccbuild_" . $ucm_stream);
         $self->dest_ear_dir($self->bfroot() . "tiersbuild/harmony/tiersapps/" . $self->ucm_stream() . "/online/ear/");
      }
      $self->view_dir($ucm_stream);
   }
   return $self->{ucm_stream};
}
sub view_name
{
   my( $self, $view_name) = @_;
   $self->{view_name} = $view_name if defined($view_name);
   return $self->{view_name};
}
sub os_is_windows
{
   my($self) = shift;
   if($self->os() =~ /^MSWin32/)
   {
      return(1);
   }
   return(0);
}


##########################################################################
# END OBJECT VAR METHODS
##########################################################################

# Get current date.  Proceed single digit dates with a 0 for readability.
sub get_curr_date {
	my $year  = localtime->year() + 1900;
	my $month = ((localtime->mon() + 1) < 10) ? ("0" . (localtime->mon() + 1)) : (localtime->mon() + 1);
	my $day   = (localtime->mday() < 10) ? ("0" . localtime->mday()) : localtime->mday();
	return ($year, $month, $day);
}

# Get current time.  Preceed single digit times with a 0 for readability.
sub get_curr_time {
	my $hour   = (localtime->hour() < 10) ? ("0" . localtime->hour()) : localtime->hour();
	my $minute = (localtime->min() < 10) ? ("0" . localtime->min()) : localtime->min();
	my $second = (localtime->sec() < 10) ? ("0" . localtime->sec()) : localtime->sec();
	return ($hour, $minute, $second);
}

sub convert_timestamp_to_array
{
   my($self, $timestamp) = @_;
   #$self->println("timestamp => " . $timestamp);
   #$self->println("year => " . localtime($timestamp)->year());
   #$self->println("month => " . localtime($timestamp)->mon());
   #$self->println("day => " . localtime($timestamp)->mday());
   #$self->println("hour => " . localtime($timestamp)->hour());
   #$self->println("min => " . localtime($timestamp)->min());
   #$self->println("sec => " . localtime($timestamp)->sec());
   
	my $year  = localtime($timestamp)->year() + 1900;
	my $month = ((localtime($timestamp)->mon() + 1) < 10) ? ("0" . (localtime($timestamp)->mon() + 1)) : (localtime($timestamp)->mon() + 1);
	my $day   = (localtime($timestamp)->mday() < 10) ? ("0" . localtime($timestamp)->mday()) : localtime($timestamp)->mday();
	my $hour   = (localtime($timestamp)->hour() < 10) ? ("0" . localtime($timestamp)->hour()) : localtime($timestamp)->hour();
	my $minute = (localtime($timestamp)->min() < 10) ? ("0" . localtime($timestamp)->min()) : localtime($timestamp)->min();
	my $second = (localtime($timestamp)->sec() < 10) ? ("0" . localtime($timestamp)->sec()) : localtime($timestamp)->sec();
	#$self->println("$year, $month, $day, $hour, $minute, $second");
   return ($year, $month, $day, $hour, $minute, $second);
}

# Display the hastable with its keys and values
sub display_hashtable
{
   my($self) = shift;
   my(%ht) = %{$_[0]};
   my($hasVars) = 0;
   foreach my $k ( keys(%ht) ) {
      print "$k : $ht{$k}\n";
   }
}  
sub get_hash_size
{
   my($self) = shift;
   my(%ht) = %{$_[0]};
   return keys(%ht);
}

sub println {
   my($self) = shift;
	my (@msg) = @_;
	foreach (@msg) {
      print($_ . "\n") if $self->debug;
	}
}

# Perl trim function to remove whitespace from the start and end of the string
sub trim($)
{
   my($self) = shift;
	my $string = shift;
	$string =~ s/^\s+//;
   $string =~ s/\s+$//;
	return $string;
}
# Return the number of elements in the provided array
sub get_array_size
{
   my($self) = shift;
   my @intArray = @_;
   $lastIndex = $#intArray; # index of the last element, $lastIndex == 2
   $length = $lastIndex + 1; # size of the array
   return($length);
}


# Remove all white spaces from string
sub remove_spaces($)
{
   my($self) = shift;
   my $string = shift;
   $string =~ s/\s+//;
   return $string;
}
# The following 3 functions pulled from http://www.somacon.com/p114.php
# Perl trim function to remove whitespace from the start and end of the string
sub trim($)
{
   my($self) = shift;
	my $string = shift;
	$string =~ s/^\s+//;
	$string =~ s/\s+$//;
	return $string;
}
# Left trim function to remove leading whitespace
sub ltrim($)
{
   my($self) = shift;
	my $string = shift;
	$string =~ s/^\s+//;
	return $string;
}
# Right trim function to remove trailing whitespace
sub rtrim($)
{
   my($self) = shift;
	my $string = shift;
	$string =~ s/\s+$//;
	return $string;
}

#  THIS METHOD IS NO LONGER USED
# Get all dates from ARG days back to today
# RETURN: Array of Date Strings Format: "YYYY-MM-DD"
sub get_dates_arg_days_back
{
   my($self, $days) = @_;
   $self->days_back($days);
   my($now) = time();
   $hrs_per_day = 24;
   $min_per_hr = 60;
   $sec_per_min = 60;
   $sec_per_day = $hrs_per_day * $min_per_hr * $sec_per_min;
   my(@dates_to_load) = ();
   # DEFINE TODAY'S DATE
   my($year, $month, $day) = $self->getCurrentDate();
   @dates_to_load[0] = "$year-$month-$day";
   for($i=1;$i<=$days;$i++)
   {
      my($secs_to_go_back) = $i * $sec_per_day;
      $time_to_get=$now - $secs_to_go_back;
      $year  = localtime($time_to_get)->year() + 1900;
      $month = ((localtime($time_to_get)->mon() + 1) < 10) ? ("0" . (localtime($time_to_get)->mon() + 1)) : (localtime($time_to_get)->mon() + 1);
      $day   = (localtime($time_to_get)->mday() < 10) ? ("0" . localtime($time_to_get)->mday()) : localtime($time_to_get)->mday();
      @dates_to_load[$i] = "$year-$month-$day";
   }
   $self->datestring_prior_build(@dates_to_load[$array_size - 1]);
   return (@dates_to_load);
}

# WRITE THE PROVIDED STRING TO THE DEST FILE
sub write_to_file
{
   my ($self, $what_to_write) = @_;
   $self->println("write_to_file(helper)");
   $file_name = $self->dest_dir() . $self->dest_file_name();
   $self->println("file => $file_name");
   open(FILEWRITE, "> $file_name");
   print FILEWRITE $what_to_write;
   close FILEWRITE;
   $mod = 0444;
   chmod $mod, '$file_name';
   return($file_name);
}

###############################################################################
# END SUB METHODS
###############################################################################
1;
