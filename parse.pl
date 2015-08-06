#!/opt/perl/bin/perl

use LWP::Simple;
use File::Listing;
use Time::localtime;

my $url = 'http://sea1tpsup01/Monitor/tools/apiLongThreadCheck/Default.aspx';
    
my $content = get $url or die "Couldn't load " . $url;

$count = 0;


my $year  = localtime->year() + 1900;
my $month = ((localtime->mon() + 1) < 10) ? ("0" . (localtime->mon() + 1)) : (localtime->mon() + 1);
my $day   = (localtime->mday() < 10) ? ("0" . localtime->mday()) : localtime->mday();

my $hour   = (localtime->hour() < 10) ? ("0" . localtime->hour()) : localtime->hour();
my $minute = (localtime->min() < 10) ? ("0" . localtime->min()) : localtime->min();
my $second = (localtime->sec() < 10) ? ("0" . localtime->sec()) : localtime->sec();


if($content =~ /(<b>SERVER:<\/b>\s<a\shref="http:\/\/SEA1CNBCAPI01\/thread.status"\starget="_blank">)((.(?!<hr\/>))*)/)
{
   print($2 . "\n");
   $searchme = $2;
   while ($searchme =~ /\(\d{2,3}\.\d{2,3}\.\d{2,3}\.\d{2,3}\)<br/g)
   {
      $count++;
   }
   print("count = $count\n");
   
}
else
{
   print("NOTHING\n");
}
print("-------------------\n");



$file_name = "C:/apilog.csv";
$file_name = "/Users/james.sandlin/apilog.csv";

open(FILEWRITE, ">> $file_name");
print FILEWRITE $year . "-" .  $month . "-" .  $day . "-" . $hour . ":" .  $minute . ":" .  $second . "," . $count . "\n";
close FILEWRITE;

