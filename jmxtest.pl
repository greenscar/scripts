#!/usr/bin/perl

use strict;
use warnings;
use Getopt::Long;
my($box);
my($output) = 0;

GetOptions('box=s' => \$box);
#print("box = $box\n");
#print("hostname = " . $ENV{"HOSTNAME"} . "\n");
#$output = `java -jar JmxInvoker.jar -host $box -port 8006 -bean app.btn.storefront:group=services,role=siteHelper -operation getDefault 2>/dev/null`;
$output = "export CLASSPATH=/opt/zenoss/libexec; java -jar /opt/zenoss/libexec/JmxInvoker.jar -host $box -port 8006 -bean app.btn.storefront:group=services,role=siteHelper -operation getDefault 2>/dev/null";
#print("output = $output\n");
$output = `$output`;
print("domestic:");
if($output =~ /^DOMESTIC$/)
{
   print("1\n");
}
else
{
   print("0\n");
}

