#!/usr/bin/perl
use Time::localtime;
use Getopt::Long;
use Activity;
use Baseline;
use ClearCase;


$numArgs = $#ARGV + 1;
$TARGET_FILE = "c:\\webserver\\htdocs\\streamread.htm";
$ccadmin = new ClearCase->new();

# Parse command-line options and assign to appropriate variables
GetOptions('stream=s' => \$stream,
           'days=s' => \$days);

# ENSURE STREAM NAME ARG IS IN CORRECT FORMAT
if((!defined($stream)) || ((defined($stream)) && ($stream !~ m/TIERS\S/)))
{
   display_syntax();
   exit();
}
# ENSURE NUM DAYS BACK ARG IS A NUMBER (IF PROVIDED)
if(defined($days) && ($days !~ m/\d+/))
{
   display_syntax();
   exit();
}
if(!defined($days))
{
   $days = 0;
}

#displayMsg("stream = $stream");

# GET A LIST OF DATES BASED ON THE NUMBER OF DAYS WE WANT TO GO BACK
@dates_to_load = get_dates($days);


# BEGIN HTML GENERATION
my($final_string) = "";
$final_string = "<html>\n<head><title>$stream activites since " . @dates_to_load[0] . "</title><h1 align=\"center\">Activites since " . @dates_to_load[0] . "</h1></head>\n<body><table border=1>\n";
$final_string .= "<tr><th width=\"170px\">DATE</th><th width=\"150px\">USER</th><th width=\"100px\">ACT #</th><th width=\"600px\">DESCRIPTION</th></tr>\n";

#displayMsg("----------------------------------------------------");
# GET A LIST OF ALL ACTIVITIES IN THIS STREAM
#displayMsg("$stream");
#displayMsg("$ccadmin");
my(%all_activities) = $ccadmin->get_all_acts_of_stream($stream); 

displayMsg("all_activities = " . getArraySize(%all_activities));


# STRIP OLD ACTIVITIES FROM THE LIST
my(%recent_acts) = $ccadmin->pull_acts_from_date(%all_activities, @dates_to_load);

#displayMsg("recent_acts = " . getArraySize(%recent_acts));
# GO THROUGH OLD ACTIVITIES & RETRIEVE DETAILS OF EACH.
while ( ($date, $activity) = each(%recent_acts) ) {
   #displayMsg("$date  => " . $activity->user);
#   get_dtls_of_activity($date, $activity_num);
   $final_string .= "\t<tr>"
                 . "<td>" . $activity->date . "</td>"
                 . "<td>" . $activity->user . "</td>"
                 . "<td>" . $activity->id . "</td>"
                 . "<td>" . $activity->title . "</td></tr>\n";
}
$final_string .= "</table>\n</body>\n</html>";

write_to_file($final_string);

###############################################################################
# SUB METHODS
###############################################################################

# Display program syntax
sub display_syntax {
	printf("Syntax:  ccperl streamread.pl ARGS\n\n");
	printf("Required Arguments:\n");
	printf("    --stream  n   Name of stream to look for activities\n");
	printf("Optional Arguments:\n");
	printf("    --days  n   Number of days to look back for activites\n");
}



# Display the hastable with its keys and values
sub display_hashtable(\\%)
{
   my(%table) = @_;
   my($hasVars) = 0;
   while ( ($key, $value) = each(%table) ) {
      displayMsg("$key => $value");
   }
}     
sub displayMsg {
	my (@msg) = @_;
	foreach (@msg) {
      print($_ . "\n");
		#printf("%s\n", $_);
		#printf ERRLOG ("%s\n", $_);
	}
}


################################################################################
# write_to_file writes the string provided to the file provided
# @args:
#  $what_to_write => the String to write to the file.
################################################################################
sub write_to_file
{
   my ($what_to_write) = @_;
   #displayMsg("write_to_file($what_to_write)");
   open(FILEWRITE, "> $TARGET_FILE");
   print FILEWRITE $what_to_write;
   close FILEWRITE;
}


# Get current date.  Proceed single digit dates with a 0 for readability.
sub getCurrentDate {
	my $year  = localtime->year() + 1900;
	my $month = ((localtime->mon() + 1) < 10) ? ("0" . (localtime->mon() + 1)) : (localtime->mon() + 1);
	my $day   = (localtime->mday() < 10) ? ("0" . localtime->mday()) : localtime->mday();
	return ($year, $month, $day);
}

# Get all dates from ARG days back to today
# RETURN: Array of Date Strings Format: "YYYY-MM-DD"
sub get_dates
{
   my($days) = @_;
   my($now) = time();
   $hrs_per_day = 24;
   $min_per_hr = 60;
   $sec_per_min = 60;
   $sec_per_day = $hrs_per_day * $min_per_hr * $sec_per_min;
   my(@dates_to_load) = ();
   # DEFINE TODAY'S DATE
   my($year, $month, $day) = getCurrentDate();
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
   return (@dates_to_load);
}

# Get current time.  Preceed single digit times with a 0 for readability.
sub getCurrentTime {
	my $hour   = (localtime->hour() < 10) ? ("0" . localtime->hour()) : localtime->hour();
	my $minute = (localtime->min() < 10) ? ("0" . localtime->min()) : localtime->min();
	my $second = (localtime->sec() < 10) ? ("0" . localtime->sec()) : localtime->sec();
	return ($hour, $minute, $second);
}
# Perl trim function to remove whitespace from the start and end of the string
sub trim($)
{
	my $string = shift;
	$string =~ s/^\s+//;
   $string =~ s/\s+$//;
	return $string;
}
sub getArraySize
{
   my @intArray = @_;
   $lastIndex = $#intArray; # index of the last element, $lastIndex == 2
   $length = $lastIndex + 1; # size of the array
   return($length);
}
###############################################################################
# END SUB METHODS
###############################################################################




