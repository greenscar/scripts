#!ccperl
#
# Application developed by James Sandlin
# This script reads the online.properties files and generates
# a XML file with the environments' properties
# This XML file will then be read by the TAA Workbench's files
#    & inserted into the DB
#

$file="C:\\views\\James.G.Sandlin_SYSCONFIG_laptop\\IE_sysconf\\TIERS\\build_script\\props\\online.properties";
@envts = ("AST01", "AST02", "AST03", "APT01", "APT02", "AGING01", "AGING02", "AGING03", "MISC01", "MISC02", "IPT");
my(%hash_table) = getVarsFromFile($file);

clean_hash_of_subvars(%hash_table);
$xml = generateXML(@envts);
writeToFile($xml);

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
            $xml .= "\t\t<$key>" . $value . "</$key>\n";
         }
      }
      #displayMsg($_);
      $xml .= "\t</envt>\n";
   }
   $xml .= "</envts>\n";
   #displayMsg($xml);   
}
sub writeToFile
{
   my($xml) = @_;
   open(FILEWRITE, "> c:\\webserver\\htdocs\\envts.xml");
   print FILEWRITE $xml;
   close FILEWRITE;
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
   while($var =~ m/\$\{/g)
   {
      $toReturn++;
   }      
   return $toReturn;
}

# Go through the hashtable and remove subvars from the values.
sub clean_hash_of_subvars(\%)
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
sub display_hashtable(\%)
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
   while($full_var =~ m/(\$\{\w+\.\w+\})/g)
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
      $key_with_parens = "\\\$\\\{" . $key . "\\\}";
      $full_var =~ s/$key_with_parens/$value/ge;
   }
   return($full_var);
}


# Display any notification messages and write to logfile
sub displayMsg {
	my (@msg) = @_;
	foreach (@msg) {
      print($_ . "\n");
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
