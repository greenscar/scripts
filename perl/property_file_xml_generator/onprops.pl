#!ccperl

$file="C:\\working\\James.G.Sandlin_SYSCONFIG_int\\IE_sysconf\\TIERS\\build_script\\props\\online.properties";

open INF, "<$file" or die "$file $!";
#print "My name is $0 \n";
#print "First arg is: $ARGV[0] \n";
#print "Second arg is: $ARGV[1] \n";
%hashTable = ();
while( <INF> ){
  # /^SDI_(TOPIC|DATE)\s+(.+)/;
  chop;
  $line = $_;
  if($line =~ m/^online$ARGV[0]/)
  {
     # $line=$word  if /^online$ARGV[0]/;
     #print $line . "\n" if /^online$ARGV[0]/;
     ($name,$val)=split(/=/,$line);
     #print $name . " => " . $val . "\n";
     $hashTable{$name} = $val;
     if($val =~ m/\$\{/)
     {
        #$_ = $val;
        $count=0;
        #print($val . "\n");
        while($val =~ m/(\$\{\w+\.\w+\})/g)
        {
           #print "1 = " . $1 . "\n";
           $len_one = length $1;
           
           
           $var = substr $1, 2;
           $len = length $var;
           $len = $len - 1;
           print "index = " . index($val, $1) . "\n";
           print $val . "\n";
           $var = substr $var, 0, $len; 
           $value = $hashTable{$var};
           #print $val . " -- " . $var . " = " . $value . "\n";
           
           $_ = $val;
           $replaceWith=$hashTable{$var};
           print "strreplace(\$\{" . $var . "\}, " . $replaceWith . ")\n";
           $x =~ s/$value/$replaceWith/g . "\n";
           print $x . "\n";
           #print ++$count . " -> " . $var . "\n";
        }
        
        #print("------------------------------\n");
        #print("count = " . $count . "\n");
        #print($count . "[" . $name . "] val => " . $hashTable{$name} . "\n");
        #print $name . " => " . $hashTable{$name} . "\n";
        my($text) = $hashTable{$name};
        $text =~ m/\$\{.*\}/g;
        $res = $1;
     }
     else
     {
        #print " -- " . $name . " => " . $hashTable{$name} . "\n";
     }
  }
}
close INF;


sub findVariable
{
	my @var = @_;
   
   if($val =~ m/^\$\{\}/)
   {
      return (findVariable(@var));
   }
   else
   {
      return @var
   }
}
