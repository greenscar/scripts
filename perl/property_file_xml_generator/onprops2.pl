#!ccperl
$file="C:\\working\\James.G.Sandlin_SYSCONFIG_int\\IE_sysconf\\TIERS\\build_script\\props\\online.properties";

my(%hash_table) = getVarsFromFile($file, $ARGV[0]);
#$size = getArraySize(%hash_table);
#displayMsg("size = " . $size);
#displayMsg(%hash_table);
clean_hash_of_subvars(%hash_table);
#display_hashtable(%hash_table);
sub getVarsFromFile
{
	my (@args) = @_;
   my ($file) = @args[0];
   my ($envt) = @args[1];
   my (%hash_table) = ();
   displayMsg("-----------------------------");
   open INF, "<$file" or die "$file $!";
   while( <INF> )
   {
     chop;
     $line = $_;
     if($line =~ m/^online$envt/)
     {
        (my($name),my($val))=split(/=/,$line);
        $hash_table{$name} = $val;
     }
   }
   close INF;
   return %hash_table;
}

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

sub clean_hash_of_subvars(\%)
{
   my(%table) = @_;
   my($hasVars) = 0;
   while ( ($key, $value) = each(%table) ) {
      $varCount = getStringVarCount($value);
      if($varCount)
      {
         replaceVarsInVar($value);
      }
   }
}
sub display_hashtable(\%)
{
   my(%table) = @_;
   my($hasVars) = 0;
   while ( ($key, $value) = each(%table) ) {
      displayMsg("$key => $value");
   }
}     

sub replaceString {
  my $strString = shift;
  my $strSearch = shift;
  my $strReplace = shift;
  #displayMsg("strString = s/$strSearch/$strReplace/ge;");
  $strString =~ s/$strSearch/$strReplace/ge;
  return $strString;
}

sub replaceVarsInVar
{
   my($full_var) = @_;
   #displayMsg("var = " . $full_var);
   while($full_var =~ m/(\$\{\w+\.\w+\})/g)
   {
      $origVar = $full_var;
      #displayMsg("origVar = $origVar");
      #displayMsg("1 = $1");
      $string_to_replace = $1;
      
      
      #displayMsg("string_to_replace = $string_to_replace");
      $len_one = length $string_to_replace;
      $sub_var = substr $string_to_replace, 2;
      $len = length $sub_var;
      $len = $len - 1;
      $key = substr $sub_var, 0, $len;
      $value = $hash_table{$key};
      #displayMsg("value = $value");
      
      
      $_ = $origVar;
      $replaceWith=$hash_table{$key};
      $key_with_parens = "\\\$\\\{" . $key . "\\\}";
      $full_var =~ s/$key_with_parens/$value/ge;
   } 
   $hash_table{$key} = $full_var;
   #displayMsg("hash_table[$key] = $full_var");
   #displayMsg("key => $key");
   #displayMsg("full_var => $full_var");
}
sub getHashValue {
   my ($var) = @_;
   displayMsg("var = $var");
   
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

sub getArraySize
{
   my @intArray = @_;
   $lastIndex = $#intArray; # index of the last element, $lastIndex == 2
   $length = $lastIndex + 1; # size of the array
   return($length);
}
