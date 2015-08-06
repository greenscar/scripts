#!/usr/bin/perl
#
# Application developed by James Sandlin
# This script reads the online.properties files and generates
# a XML file with the environments' properties
# This XML file will then be read by the TAA Workbench's files
#    & inserted into the DBb
#

# Specify Perl modules to include here
use Time::localtime;
use File::Copy;
use File::Compare;
#require HTTP::Request;
 
# SET STATIC VARS
$SOURCE_FILE="/home/ccbuild/tiersbuild/harmony/props/online.properties";
$DEST_XML="envts_online.xml";
$DEST_DIR="/var/apache/htdocs";
@ENVT_LIST = ("AST01", "AST02", "AST03", "APT01", "APT02", "AGING01", "AGING02", "AGING03", "MISC01", "MISC02", "IPT", "CONV1", "CONV2", "GENX", "PSR", "PRODFIX", "DAR", "COLA");
$PHP_ADDRESS = "http://workbench.txaccess.net/workbench/sections/content/environments/xml_process.php";

# Read properties file into XML 
my(%hash_table) = getVarsFromFile($SOURCE_FILE);
clean_hash_of_subvars(%hash_table);
$xml = generateXML(@ENVT_LIST);

# Archive the XML file so we can compare after creating.
moveOldXMLToArchive($DEST_DIR, $DEST_XML);

# Write properties file to new XML file
writeToFile($xml, $DEST_DIR, $DEST_XML, $dateTime);

# Compare the XML to its archive.
$compareResults = compareOldToNewXML($DEST_DIR, $DEST_XML);

#displayMsg("Compare Results => " . $compareResults);
if($compareResults)
{
#   $request = HTTP::Request->new(GET => '$PHP_ADDRESS');
}

sub moveOldXMLToArchive
{
   my ($dest_dir, $dest_file) = @_;
   my($new_file) = $dest_dir . "/" . $dest_file;
   my($archive_file) = $dest_dir . "/OLD_" . $dest_file;
   move($new_file, $archive_file);
}

sub compareOldToNewXML
{
   my($dest_dir, $dest_file) = @_;
   my($new_file) = $dest_dir . "/" . $dest_file;
   my($archive_file) = $dest_dir . "/OLD_" . $dest_file;
   my($to_return) = 0;
   # COMPARE THE 2 FILES. IF THEY ARE IDENTICAL, 
   return(compare($archive_file, $new_file));
   #if(compare($archive_file, $new_file) == 0)
   #{
   #   $to_return = 1;
   #}
   #else
   #{
   #   $to_return = 0;
   #}  
}
sub generateXML
{
   my(@envts) = @_;
   $xml = "<envts>\n";
   foreach(@envts)
   {
      $xml .= "\t<envt>\n";
      my($envt) = $_;
      $xml .= "\t\t<name>$envt</name>\n";
      my($hasVars) = 0;
      while ( ($key, $value) = each(%hash_table) ) {
         if($key =~ m/^online$envt/)
         {
            $value =~ s/\\cM//ge;
            $value =~ s/ //ge;
            $value =~ s/\\\\012//ge;
            $xml .= "\t\t<$key>" . $value . "</$key>\n";
         }
      }
      #displayMsg($_);
      $xml .= "\t</envt>\n";
   }
   $xml .= "</envts>\n";
   #displayMsg($xml);   
}

#
# writeToFile first backs up the original xml file with datetime stamp.
# It then writes the xml generated to the xml.
#
sub writeToFile
{
   my ($xml, $dest_dir, $dest_file, $date_time) = @_;
   # WRITE XML FOR ARCHIVE PURPOSES
   #my($archive_file) = $dest_dir . "/orig_" . $dest_file;
   #open(FILEWRITE, "> $archive_file");
   #print FILEWRITE $xml;
   #close FILEWRITE;
   # WRITE XML FOR UPDATE PURPOSES
   my($target_file) = $dest_dir . "/" . $dest_file;
   #my($target_file) = $dest_file;
   #`ssh wasadmin@iedaau019; cp /home/ccbuild/james/$dest_file $dest_dir/$dest_file; chmod 755 $dest_dir/$dest_file`
   open(FILEWRITE, "> $target_file");
   print FILEWRITE $xml;
   close FILEWRITE;

   #`su wasadmin@iedaau019; cp /home/ccbuild/james/$dest_file $dest_dir/$dest_file; chmod 755 $dest_dir/$dest_file`

}
# Read the properties file and get a hashtable of variables for the
# environment provided as an argument
sub getVarsFromFile
{
   my (@args) = @_;
   my ($file) = @args[0];
   #my ($envt) = @args[1];
   my (%hash_table) = ();
   open INF, "<$file" or die "$file $!";
   while( <INF> )
   {
     chop;
     $line = $_;
     if($line =~ m/^online/)
     {
        (my($name),my($val))=split(/=/,$line);
        $hash_table{$name} = $val;
     }
   }
   close INF;
   return %hash_table;
}

# Get number of subvars in a var
sub getStringVarCount
{
   my($var) = @_;
   my($toReturn) = 0;
   while($var =~ m/\\$\\{/g)
   {
      $toReturn++;
   }      
   return $toReturn;
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

# Go through the hashtable and remove subvars from the values.
sub clean_hash_of_subvars(\\%)
{
   my(%table) = @_;
   my($hasVars) = 0;
   while ( ($key, $value) = each(%table) ) {
      $varCount = getStringVarCount($value);
      if($varCount)
      {
	   $hash_table{$key} = replaceVarsInVar($value);
      }
   }
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

#
# String replace function. This is not being used in this app but is good to
# have in the collection.
#
sub replaceString {
  my $strString = shift;
  my $strSearch = shift;
  my $strReplace = shift;
  $strString =~ s/$strSearch/$strReplace/ge;
  return $strString;
}

#
# Clean up subvariables in a ANT variable.
# ANT variables look like: ${___}
# Replace each sub variable with the value.
#

sub replaceVarsInVar
{
   my($full_var) = @_;
   my($key);
   while($full_var =~ m/(\\$\\{\\w+\\.\\w+\\})/g)
   {
      $origVar = $full_var;
      $string_to_replace = $1;
      $len_one = length $string_to_replace;
      $sub_var = substr $string_to_replace, 2;
      $len = length $sub_var;
      $len = $len - 1;
      $key = substr $sub_var, 0, $len;
      $value = $hash_table{$key};
      $_ = $origVar;
      $replaceWith=$hash_table{$key};
      $key_with_parens = "\\\\\\$\\\\\\{" . $key . "\\\\\\}";
      $full_var =~ s/$key_with_parens/$value/ge;
   }
   return($full_var);
}


# Display any notification messages and write to logfile
sub displayMsg {
	my (@msg) = @_;
	foreach (@msg) {
      print($_ . "n");
		#printf("%s\n", $_);
		#printf ERRLOG ("%s\n", $_);
	}
}

# Get size of array
sub getArraySize
{
   my @intArray = @_;
   $lastIndex = $#intArray; # index of the last element, $lastIndex == 2
   $length = $lastIndex + 1; # size of the array
   return($length);
}

