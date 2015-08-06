#Updated:                 snbx_batch/Tst_fldr_1/test-r1-a.txt /main/Test_snbx_2_Mainline_Int/SNBX_89.0.0.0_Tst/6 /main/Test_snbx_2_Mainline_Int/SNBX_89.0.0.0_Tst/1
#New:                     snbx_bo/another1/text123 /main/Test_prj1_85.0_Tst/SNBX_89.0.0.0_Tst/2
#UnloadDeleted:           snbx_bo/another1/text123.txt
#if($line =~ /(Updating|Loading|Making\sdir|Unloaded)\s\"(.*)\"/)
#$line = "Updated:                 snbx_batch/Tst_fldr_1/test-r1-a.txt /main/Test_snbx_2_Mainline_Int/SNBX_89.0.0.0_Tst/6 /main/Test_snbx_2_Mainline_Int/SNBX_89.0.0.0_Tst/1";
#$line = "New:                     snbx_bo/another1/text123 /main/Test_prj1_85.0_Tst/SNBX_89.0.0.0_Tst/2";
#$line = "UnloadDeleted:           snbx_bo/another1/text123.txt";
#if($line =~ /^(Updated|New|UnloadDeleted):\s+([\w*\/?\.?]+)/

if($line =~ /^(Updated|New|UnloadDeleted):\s+(\S+)(\s(\S+)\/(\d))?(\s(\S+))?/)
{
   $action = $1;
   $filename = $2;
   $version = $5;
   printf("$1 - $2 -$3 - $4 - $5\n");
}
exit(1);

$test = "QC=11745, 11722, 11712, 11711, 11705, 11685, 11677, 11673, 11662, 11648, 11644, 11638, 11637, 11635, 11610, 10936, 10640,";
$test =~ /((\d+,\s?)+)/;

print("1 = " . $1 . "\n");
print("2 = " . $2 . "\n");

$a = "hello";
$a =~ /((hel)lo)/;
print("1 = " . $1 . "\n");
print("2 = " . $2 . "\n");

$x = "or 1 or ";
$y = "or 1";


print("----------------------\n");
if($x =~ /or\s(\d*)\s?(or$)?/)
{
   print($x . "\n");
}
print("----------------------\n");
if($y =~ /or\s(\d*)\s?(or)?\s?$/)
{
   print($y . "\n");
}
print("----------------------\n");
if($x =~ /^or\s(\d*)\s(or\s)$/)
{
   print("'$x' DOES end in or\n");
}
else
{
   print("'$x' DOES NOT end in or\n");
}
print("----------------------\n");
if($y =~ /^or (\d*) or$/)
{
   print("$y DOES end in or\n");
}
else
{
   print("$y DOES NOT end in or\n");
}
print("----------------------\n");
if($x =~ /^or (\d*)$/)
{
   print("$x does NOT end in or\n");
}
else
{
   print("$x DOES end in or\n");
}
print("----------------------\n");
if($y =~ /^or (\d*)$/)
{
   print("$y does NOT end in or\n");
}
else
{
   print("$y DOES end in or\n");
}

