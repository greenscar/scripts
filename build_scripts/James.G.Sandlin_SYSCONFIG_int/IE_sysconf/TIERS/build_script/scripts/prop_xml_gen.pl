#!/opt/rational/common/sun5/bin/ratlperl
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
require HTTP::Request;

$HOME_DIR = "/home/ccbuild";
@ENVT_LIST = ("AST01", "AST02", "AST03", "APT01", "APT02", "AGING01", "AGING02", "AGING03", "MISC01", "MISC02", "IPT", "COLA", "CONV1", "CONV2", "GENX", "PSR", "PRODFIX", "DAR");
$HTML_DEST_DIR="/var/apache/htdocs";
$XML_TEMP_DIR = $HOME_DIR . "/james/xmls";
$STREAM_DIR = $HOME_DIR . "/SYSCONFIG_Int/IE_sysconf/TIERS";

$DEST_XML_BUILD_PROPS="envts_online.xml";
$DEST_XML_APP_PROPS="app_properties.xml";
$PHP_ADDRESS = "http://workbench.txaccess.net/workbench/sections/content/environments/xml_process.php";
$BUILD_ONLINE_PROP_FILE=$HOME_DIR . "/tiersbuild/harmony/props/online.properties";
@APP_LIST = ("online", "batch");
@PROP_FILE_LIST = ("TIERS.properties", "messaging.properties", "webservice.properties");
$DEST_XML_PERMISSIONS = 0755;
################################################################################
# LOAD INFORMATION FROM BUILD SCRIPT PROPERTIES FILES
################################################################################
# Read properties file into XML 
my(%hash_table) = getVarsFromFile($BUILD_ONLINE_PROP_FILE);
cleanHashOfSubvars(%hash_table);
$xml = generateXML(@ENVT_LIST);

# Archive the XML file so we can compare after creating.
move_old_xml_to_archive($XML_TEMP_DIR, $DEST_XML_BUILD_PROPS);

# Write properties file to new XML file
write_to_file($xml, $XML_TEMP_DIR, $DEST_XML_BUILD_PROPS, $dateTime);

# Compare the XML to its archive.
$compare_results_build_props= compare_old_to_new_xml($XML_TEMP_DIR, $DEST_XML_BUILD_PROPS);

################################################################################
# END LOAD INFORMATION FROM BUILD SCRIPT PROPERTIES FILES
################################################################################


#
# LOAD INFORMATION FROM ENVIRONMENT'S PROPERTIES FILES
#

# Read properties file into XML
$xml = generate_xml();

# Archive the XML file so we can compare after creating.
move_old_xml_to_archive($XML_TEMP_DIR, $DEST_XML_APP_PROPS);

# Write properties file to new XML file
write_to_file($xml, $XML_TEMP_DIR, $DEST_XML_APP_PROPS, $dateTime);

# Compare the XML to its archive.
$compare_results_app_props = compare_old_to_new_xml($XML_TEMP_DIR, $DEST_XML_APP_PROPS);

if(($compare_results_build_props) || ($compare_results_app_props))
{
   displayMsg("scp -p $XML_TEMP_DIR/*.xml wasadmin\@iedaau019:$HTML_DEST_DIR");
   `scp $XML_TEMP_DIR/*.xml wasadmin\@iedaau019:$HTML_DEST_DIR`;
   displayMsg("making request");
   $request = HTTP::Request->new(GET => '$PHP_ADDRESS');
}



################################################################################
# Generate XML
################################################################################
sub generate_xml
{
   $xml = "<ENVTS>\n";
   foreach(@APP_LIST)
   {
      my($app) = $_;
      $xml .= "\t<$app>\n";
      foreach(@ENVT_LIST)
      {
         my($envt) = $_;
         $xml .= "\t<$envt>\n";
         foreach(@PROP_FILE_LIST)
         {
            my($file_name) = $_;
            my($file_to_parse) = $STREAM_DIR . "\\" . $app . "\\" . $envt . "\\" . $file_name;
            %prop_list = ();
            %prop_list = get_app_vars_from_file($file_to_parse);
            while ( ($key, $value) = each(%prop_list) ) 
            {
               if((length($key)) > 0 && (length($value) > 0))
               {
                  $xml .= "\t\t<$key>" . $value . "</$key>\n";
               }
            }
         }
         $xml .= "\t</$envt>\n";
      }
      # CLOSE XML WITH APP NAME HERE
      $xml .= "\t</$app>\n";
   }
   $xml .= "</ENVTS>\n";
   return($xml);
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

################################################################################
# Read the properties file and get a hashtable of variables for the
# environment provided as an argument
# This method is designed for app properties files (TIERS.properties, etc)
################################################################################
sub get_app_vars_from_file
{
   my ($file) = @_;
   my(%hash_table) = ();
   open(FILEHANDLE, "<", $file); #or die "$file $!";
   @filedata = <FILEHANDLE>;
   foreach(@filedata)
   {
      chop;
      my($line) = $_;
      if($line !~ m/^#.*/)
      {
         $equal_loc = index($line, "=");
         my($name) = substr($line, 0, $equal_loc);
         my($val) = substr($line, ($equal_loc+1));
         $hash_table{trim_whitespace($name)} = trim_whitespace($val);
      }
        
   }
   close FILEHANDLE;
   return %hash_table;
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


################################################################################
# Trim white spaces from beginning & end of a string
################################################################################
sub trim_whitespace {
  my $string = shift;
  for ($string) {
    s/^\s+//;
    s/\s+$//;
  }
  return $string;
}

sub move_old_xml_to_archive
{
   my ($dest_dir, $dest_file) = @_;
   my($new_file) = $dest_dir . "/" . $dest_file;
   my($archive_file) = $dest_dir . "/OLD_" . $dest_file;
   move($new_file, $archive_file);
}

sub compare_old_to_new_xml
{
   my($dest_dir, $dest_file) = @_;
   my($new_file) = $dest_dir . "/" . $dest_file;
   my($archive_file) = $dest_dir . "/OLD_" . $dest_file;
   # COMPARE THE 2 FILES. IF THEY ARE IDENTICAL, 
   return(compare($archive_file, $new_file));
}

################################################################################
# write_to_file writes the string provided to the file provided
# @args:
#  $xml => the String to write to the file.
#  $dest_dir => the directory to write the file.
#  $dest_file => the name of the file to write
#  $date_time => currently unused. To be used for writing date_time in the file name.
################################################################################
sub write_to_file
{
   my ($xml, $dest_dir, $dest_file, $date_time) = @_;
   my($target_file) = $dest_dir . "/" . $dest_file;
   open(FILEWRITE, "> $target_file");
   print FILEWRITE $xml;
   close FILEWRITE;
   chmod($DEST_XML_PERMISSIONS,$target_file);
}


sub merge_hash_tables
{
   my(%big_hash, %sub_hash) = @_;
   while( ($key, $value) = each(%sub_hash) )
   {
      $big_hash{$key} = $value;
   }
   return(%big_hash);
}
################################################################################
# Go through the hashtable and remove subvars from the values.
# A subvar is a string that starts with ${ and ends with }
################################################################################
sub cleanHashOfSubvars(\\%)
{
   my(%table) = @_;
   my($hasVars) = 0;
   while ( ($key, $value) = each(%table) ) {
      $varCount = ant_get_sub_var_count($value);
      if($varCount)
      {
         $hash_table{$key} = ant_sub_vars_replace($value);
      }
   }
}

################################################################################
# Get number of subvars in a var
# A subvar is a string that starts with ${ and ends with }
# EX: hello${joe}and${steve}goodbye has returns 2.
################################################################################
sub ant_get_sub_var_count
{
   my($var) = @_;
   my($toReturn) = 0;
   while($var =~ m/\\$\\{/g)
   {
      $toReturn++;
   }      
   return $toReturn;
}

################################################################################
# Clean up subvariables in a ANT variable.
# ANT variables look like: ${___}
# Replace each sub variable with the value.
################################################################################
sub ant_sub_vars_replace
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

################################################################################
# Get current date.  Proceed single digit dates with a 0 for readability.
################################################################################
sub getCurrentDate {
	my $year  = localtime->year() + 1900;
	my $month = ((localtime->mon() + 1) < 10) ? ("0" . (localtime->mon() + 1)) : (localtime->mon() + 1);
	my $day   = (localtime->mday() < 10) ? ("0" . localtime->mday()) : localtime->mday();
	return ($year, $month, $day);
}

################################################################################
# Get current time.  Preceed single digit times with a 0 for readability.
################################################################################
sub getCurrentTime {
	my $hour   = (localtime->hour() < 10) ? ("0" . localtime->hour()) : localtime->hour();
	my $minute = (localtime->min() < 10) ? ("0" . localtime->min()) : localtime->min();
	my $second = (localtime->sec() < 10) ? ("0" . localtime->sec()) : localtime->sec();
	return ($hour, $minute, $second);
}

################################################################################
# Display the hastable with its keys and values
################################################################################
sub display_hashtable(\\%)
{
   displayMsg("display_hashtable");
   my(%table) = @_;
   my($hasVars) = 0;
   while ( ($key, $value) = each(%table) ) {
      displayMsg("$key => $value");
   }
}     

################################################################################
# String replace function. This is not being used in this app but is good to
# have in the collection.
################################################################################
sub replaceString {
  my $strString = shift;
  my $strSearch = shift;
  my $strReplace = shift;
  $strString =~ s/$strSearch/$strReplace/ge;
  return $strString;
}


################################################################################
# Display any notification messages and write to logfile
################################################################################
sub displayMsg {
	my (@msg) = @_;
	foreach (@msg) {
      print($_ . "\n");
		#printf("%s\\n", $_);
		#printf ERRLOG ("%s\\n", $_);
	}
}
################################################################################
# Get size of array
################################################################################
sub getArraySize
{
   my @intArray = @_;
   $lastIndex = $#intArray; # index of the last element, $lastIndex == 2
   $length = $lastIndex + 1; # size of the array
   return($length);
}