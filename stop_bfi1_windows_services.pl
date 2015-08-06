use strict;
use warnings;

my($str1) = "BFI1";
my(@clients) = ("AP", "CBS", "CMC", "CSTV", "FOX", "IND", "NDM", "PBS", "TP");
my($client) = undef;

foreach $client (@clients)
{
   for($i=1; $i < 15; $i++)
   {
      my($box) = $str1 . $client . "FEEDS";
      if($i < 10)
      {
         $box = $box . "0";
      }
      $box = $box . $i;
      print($box . "\n");
   }
}
