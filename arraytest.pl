
use strict;
use warnings;

my @array = (2, 5, 2, 4, 6, 8, 10, 12);
my @rs2 = (1, 3, 2, 4, 5, 2, 3, 3, 5);
#print(@array . "\n");
#delete $array[2];
#print(@array . "\n");
#splice(@array, 3, 1);
#print(@array . "\n");

println("--------------------------------------");
foreach(@array)
{
   print($_ . ", ");
}
println("\n-------------------------------------");
foreach (@rs2)
{
   print($_ . ", ");
}

for(my($loc1)=0; $loc1 < @array; $loc1++)
{
   if(defined $array[$loc1])
   {
      my($temp1) = $array[$loc1];
      for(my($loc2)=0; $loc2 < @rs2; $loc2++)
      {
         if(defined $rs2[$loc2])
         {
            my($temp2) = $rs2[$loc2];
            if($temp1 == $temp2)
            {
               println("delete " . @array[$loc1] . " and " . @rs2[$loc2]);
               @array = deletefromarray($temp1, @array);
               @rs2 = deletefromarray($temp2, @rs2);
               $loc1--;
               $loc2--;
            }
         }
      }
   }
}
println("\n--------------------------------------");
foreach(@array)
{
   print($_ . ", ");
}
println("\n--------------------------------------");
foreach (@rs2)
{
   print($_ . ", ");
}

println("\n--------------------------------------");
sub println 
{
   my (@msg) = @_;
   foreach (@msg) 
   {
      print("$_ \n")
   }
}

sub deletefromarray
{
   my($val, @array) = @_;
   for(my($x) = 0; $x < @array; $x++)
   {
      my($elem) = $array[$x];
      if ($elem == $val)
      {
         splice(@array, $x, 1);
         $x--;
      }
   }
   return(@array);
}

#for(my($loc1)=0; $loc1 < @array; $loc1++)
#{
#   for(my($loc2)=0; $loc2 < @rs2; $loc2++)
#   {
#      print($array[$loc1] . " = " . $rs2[$loc2] . "\n");
#   }
#}


#print(@array . "\n");
