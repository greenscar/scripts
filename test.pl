#!/usr/bin/perl

@nums = (100 .. 5599);
print scalar(@nums . "\n");
$numsubs = int(@nums / 1000);
if(@nums % 1000 > 0)
{
   $numsubs++;
}
for($x=0; $x<$numsubs; $x++)
{
   
   $start = 1000 * $x;
   $end = (($x+1) * 1000) - 1;
   if($end > $nums[-1])
   {
      $end = $nums[-1];
   }
   print("start = $start\nend = $end\n");
   @subarray = @nums[$start..$end];
   print("Array $x =>\n");
   print("@subarray\n");
   print("\n---------------------\n");
   
}

print("$numsubs\n");
