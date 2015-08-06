if(!defined(@ARGV[0]))
{
	println("USAGE: dc.pl <LENGTH>");
	exit();
}


# Specify Perl modules to include here
use Time::localtime;
#use Getopt::Long;
#use NET::SMTP;
#use NET::hostent;
use File::stat;

%MONTHS = ("jan"=>1,"feb"=>2, "mar"=>3, "apr"=>4, "may"=>5, "jun"=>6, "jul"=>7, "aug"=>8, "sep"=>9, "oct"=>10, "nov"=>11, "dec"=>12);
@CHARS = ("A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z");#, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9);
$strlen = @ARGV[0];
@domains = ();
($START_YEAR, $START_MONTH, $START_DAY) = getCurrentDate();
($START_HR, $START_MIN, $START_SEC) = getCurrentTime();
$DATE_STRING = $START_YEAR . "-" . $START_MONTH . "-" . $START_DAY . "_" . $START_HR . "-" . $START_MIN . "-" . $START_SEC;
$DC_DIR = "/home/c8h10n4o2/scripts/domain_check/";
$WHOIS_DIR = $DC_DIR . "whois_results/";
$NO_MATCH_FILE = $DC_DIR . $DATE_STRING . "_no_match.results";
$EXPIRES_ON_FILE = $DC_DIR . $DATE_STRING . "_expires_on.results";
$CHECK_WHOIS_FILE = $DC_DIR . $DATE_STRING . "_check_whois.results";
$PROGRESS_FILE = $DC_DIR . $DATE_STRING . "_status.results";
$NO_MATCH_FOR_STR = "No match for";
$EXPIRATION_DATE_STR = "Expiration Date:";
$EMAIL_FROM = "james\@h8n.com";
$EMAIL_TO = "james\@h8n.com";


file_append($PROGRESS_FILE, "$START_YEAR-$START_MONTH-$START_DAY $START_HR:$START_MIN:$START_SEC START\n");


for($counter = 1; $counter <= $strlen; $counter++)
{
	if(getArraySize(@domains) == 0)
   {
      foreach $char (@CHARS)
      {
         $new_domain = $char;
         push(@domains, $new_domain);
      }
   }
   else
   {
      @orig_list = @domains;
		open PROGRESSFILE, ">>$PROGRESS_FILE";
		
      foreach $domain (@orig_list)
      {
			file_append($PROGRESS_FILE, "$START_YEAR-$START_MONTH-$START_DAY $START_HR:$START_MIN:$START_SEC - Processing $domain* BEGIN\n");
         foreach $char (@CHARS)
         {
            $new_domain = $domain . $char;
            push(@domains, $new_domain);
				if(length($new_domain) == $strlen)
				{
					$to_check = $new_domain . ".COM";
					print PROGRESSFILE "$to_check\n";
					println($to_check);
					if(dig_dns($to_check) == 0)
					{				
						if(!whois_file_exists($domain))
						{
							get_whois_file($domain);
						}
						check_whois_file($domain);
					}
				}
         }
			file_append($PROGRESS_FILE, "$START_YEAR-$START_MONTH-$START_DAY $START_HR:$START_MIN:$START_SEC - Processing $domain* COMPLETE\n");
      }
		close PROGRESSFILE;
   }
}


file_append($PROGRESS_FILE, "$START_YEAR-$START_MONTH-$START_DAY $START_HR:$START_MIN:$START_SEC END\n");


# ARG: $domain to check
# Parse the whois file that was created and get the status (AVAILABLE, PROBATION, EXPIRES ON)
	# If expiration date is this month, make sure the whois.DOMAIN.results is from today.
#    If it is not from today, update it.
# If domain expires this month OR is available OR is probation, send email.

sub check_whois_file
{
	my $domain = ($to_check);
	println("check_whois_file($domain)");
	$filename = $WHOIS_DIR . "whois." . $domain . ".results";
	my($result_found) = 0;
	my($filemodyear, $filemodmonth, $filemodday)=get_file_mod_time($filename);
	#println("file mod date => $filemodyear-$filemodmonth-$filemodday");
	open WHOISFILE, "$filename";
	# Look through existing whois file for details
	while ( <WHOISFILE> )
	{
		my($line) = $_;
		if(my($subline) = m/($NO_MATCH_FOR_STR)/o)
		{
			# PRINT DOMAIN TO A FILE
			send_email($domain, "NO MATCH");
			file_append($NO_MATCH_FILE, $domain . "\n");
			$result_found++;
		}
		elsif(my($subline) = m/($EXPIRATION_DATE_STR)/o)
		{
			#println($line);
			# If expiring this month & whois file was not updated today, update it.
			if((expires_this_month($line)) && (($filemodyear<$START_YEAR) || ($filemodmonth < $START_MONTH) || (($filemodmonth == $START_MONTH) && ($filemodday < $START_DAY)))) 
			{
				#println("update whois file");
				close WHOISFILE;
				get_whois_file($domain);
				check_whois_file($domain);
			}
			else
			{
				if(expires_this_month($line))
				{
					my($expYear, $expMonth, $expDay)=get_expiration_date($line);
					send_email($domain, " expires $expYear-$expMonth-$expDay");
					file_append($EXPIRES_ON_FILE, $domain . " => " . $line);
				}
				close WHOISFILE;
				$result_found++;
			}
		}
	}
	close WHOISFILE;
	if($result_found == 0)
	{
		#ALSO PRINT DOMAIN TO A FILE
		send_email($domain, " CHECKIT");
		file_append($CHECK_WHOIS_FILE, $domain . " CHECK WHOIS\n");
		println($domain . " CHECK WHOIS FILE");
	}
}


#
# ARG: $domain, $expiration line
# Send email to james@h8n.com with domain information.
sub send_email
{
	my($domain, $subject) = @_;
	my $send_to = "To: $EMAIL_TO\n";
	my $reply_to = "Reply-to: $EMAIL_FROM\n"; 
	my $from = "From: $EMAIL_FROM\n";
	my $subject = "Subject: " . $domain . $subject . "\n"; 
	my $body = "";
	open WHOISFILE, get_file_name($domain);	
	while(<WHOISFILE>)
	{
		$body = $body . $_;
	}
	unless(open (MAIL, "|/usr/sbin/sendmail james\@h8n.com")) {
		print "error.\n";
		warn "Error starting sendmail: $!";
	}
	else{
		print MAIL $from;
		print MAIL $reply_to;
		print MAIL $subject;
		print MAIL $send_to;
		print MAIL "Content-type: text/plain\n\n";
		print MAIL $body; 
		close(MAIL) || warn "Error closing mail: $!";
		print "Mail sent\n";
	}
}
	
# ARG: The domain
# Return the file name of the domain results file.
sub get_file_name
{
	my($domain) = @_;
	$filename = $WHOIS_DIR . "whois." . $domain . ".results";
	return $filename;
}

# ARG: The domain to check
# Call whois and write the results into the correct file.
sub get_whois_file
{
	my $domain = ($to_check);
	$filename = get_file_name($domain);
	$whoisresults=`/usr/bin/whois $domain`;
	#println("writing results to $filename");
	file_write($filename, $whoisresults);
	#open WHOISFILE, ">$filename";	
	#print WHOISFILE $whoisresults . "\n";
}


# ARG: filename to write to, string to write.
# Write the provided string to the provided file name, append it to anything already there.
sub file_append
{
	my($filename, $str) = @_;
	open FILEPOINTER,  ">>$filename";
	print FILEPOINTER $str;
	close FILEPOINTER;
}

# ARG: filename to write to, string to write.
# Write the provided string to the provided file name, overwriting anything already there.
sub file_write
{
	my($filename, $str) = @_;
	open FILEPOINTER, ">$filename";
	print FILEPOINTER $str;
	close FILEPOINTER;
}

# ARG: The domain to check
# Look in the files and see if we already have a whois file for this DNS
# IF YES, return 1
# IF NO, return 0;
sub whois_file_exists
{
	my $domain = ($to_check);
	$filename = get_file_name($domain);
	if(-e $filename)
	{
		return(1);
	}
	else
	{
		return(0);
	}
}
	
	
# ARG: The filename you want the mod time on.
# Look at the file and get the modification date.
# RETURN: (modyear, modmonth, modday)
sub get_file_mod_time
{
	my($filename) =($filename);	
	$ft = stat($filename)->mtime;
	my $year  = localtime($ft)->year() + 1900;
	my $month = ((localtime($ft)->mon() + 1) < 10) ? ("0" . (localtime($ft)->mon() + 1)) : (localtime($ft)->mon() + 1);
	my $day   = (localtime($ft)->mday() < 10) ? ("0" . localtime($ft)->mday()) : localtime($ft)->mday();
	return($year, $month, $day);
}


# ARG: The domain you want to check.
sub dig_dns
{
	my $domain = ($to_check);
	my @domain = @_;
	#println("checking $domain");
	$digresults=`/usr/bin/dig $domain | grep 'ANSWER SECTION'`;
	if(length $digresults > 0)
	{
		return(1);
	}
	else
	{
		return(0);
	}
}

# ARG: What you want to print
# Goes through the array and prints each element.
sub println
{
	my @toprint = @_;
	$n = 0;
	while ($toprint[$n]) {
		print $toprint[$n] . "\n";
		$n++;
	}
}

# ARG: The line from the whois results containing the expiration date
# Calls the get_expiration_date method and compares the results to
#     the current date. 
# RETURN: true if year & month are same.
#         false if they are differnet
sub expires_this_month
{
	my $expire_line = (@_[0]);
	my ($expYear,$expMonth,$expDay)=get_expiration_date($expire_line);
	my($currentYear, $currentMonth, $currentDay)=getCurrentDate();
	if(($expYear <= $currentYear) && ($expMonth <= $currentMonth))
	{
		return(1);
	}
	else
	{
		return(0);
	}
}



# ARG: The line from the whois results containing the expiration date
# Parse the line returning the 
# RETURN: expiration date in the format YYYY-MM-DD
sub get_expiration_date
{
	my $full_line = (@_[0]);
	if(my($the_date) = m/(([12][0-9]|3[01])-[a-z][a-z][a-z]-20\d\d)/o)
	{
		($day,$month_str,$year)=split(/-/,$the_date,3);
		$month_num = $START_MONTHS{$month_str};
		return($year,$month_num,$day);
	}
}

# ARG: An Array
# RETURN: Size of array ARG
sub getArraySize
{
   my @intArray = @_;
   $lastIndex = $#intArray; # index of the last element, $lastIndex == 2
   $length = $lastIndex + 1; # size of the array
   return($length);
}
# Get current date.  Proceed single digit dates with a 0 for readability.
sub getCurrentDate {
        my $year  = localtime->year() + 1900;
        my $month = ((localtime->mon() + 1) < 10) ? ("0" . (localtime->mon() + 1)) : (localtime->mon() + 1);
        my $day   = (localtime->mday() < 10) ? ("0" . localtime->mday()) : localtime->mday();
        return ($year, $month, $day);
}

# Get current time.  Preceed single digit times with a 0 for readability.
sub getCurrentTime {
        my $hour   = (localtime->hour() < 10) ? ("0" . localtime->hour()) : localtime->hour();
        my $minute = (localtime->min() < 10) ? ("0" . localtime->min()) : localtime->min();
        my $second = (localtime->sec() < 10) ? ("0" . localtime->sec()) : localtime->sec();
        return ($hour, $minute, $second);
}

