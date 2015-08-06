#!/usr/bin/perl -w
 
### test.pl
# James Sandlin <james.sandlin@theplatform.com>
#
# Simply a test to learn Nagios / Zenoss scripting 
#
# Nagios plugin using the Nagios::Plugin module.  
#
# checks number of running processes
#
# License: GPL
#
# Changelog:
#  0.1 - try 1
# 
##############################################################################
use strict;
use warnings;
use Data::Dumper; 
use Nagios::Plugin ;
 
use vars qw($VERSION $PROGNAME  $verbose $warn $critical $timeout $result);
$VERSION = 0.1;
$|++;
$PROGNAME = "test";
 
# ipmi privilege level
my $PRIV = "USER";
 
print "Creating Nagios::Plugin object...\n";
# instantiate Nagios::Plugin
my $p = Nagios::Plugin->new(
	usage => "Usage: %s [ -v|--verbose ]  [-t <timeout>]
	[ -H|--Host <ipaddr> ] [ -u|--user <username> ] [ -p|--pass <password> ]",
	version => $VERSION,
	blurb => 'This plugin returns the number of running processes', 
 
	extra => ""
);
 
print "Calling add_arg-1\n";
# add all arguments 
$p->add_arg(
	spec => 'Host|H=s',
	help =>
	qq{-H, --Host=STRING
	Specify the remote station on the command line.},
	required => 0,
);
 
print "Calling add_arg-2\n";
$p->add_arg(
	spec => 'user|u=s',
	help =>
	qq{-u, --user=STRING
	Specify the IPMI user on the command line.},
	required => 0,
);
 
print "Calling add_arg-3\n";
$p->add_arg(
	spec => 'pass|p=s',
	help =>
	qq{-p, --pass=STRING
	Specify the IPMI password on the command line.},
	required => 0,
);
 
print "Getting options\n";
# parse arguments
$p->getopts;
 
print "Checking command line\n";
# perform checking on command line options
if ( ( (defined $p->opts->Host) || (defined $p->opts->user) || (defined $p->opts->pass) )
	&& !( (defined $p->opts->Host) && (defined $p->opts->user) && (defined $p->opts->pass) ) ) {
	$p->nagios_exit(
		return_code => UNKNOWN,
		message	=> "Specify username, password and host on the command line."
	)
}
 
# helper
sub trim ($) {
	my ($v) = @_;
	$v =~ s/^ +//;
	$v =~ s/ +$//;
	return $v;
}
 
 
my $result=OK;
my $message="";
my %goodresults;
my %badresults;
 
print "Checking ipmitool\n";
# open ipmitool with remote connection if host, user and password exists
if ( (defined $p->opts->Host) && (defined $p->opts->user) && (defined $p->opts->pass) ) {
    print "Using long command line\n";
	my $host = $p->opts->Host;
	my $user = $p->opts->user;
	my $pass = $p->opts->pass;
	open IPMI, "$IPMITOOL -L $PRIV -H $host -U $user -P $pass $SDR |" or 
	$p->nagios_exit( 
		return_code => UNKNOWN, 
		message => "ipmitool: $!" 
	);
}
else {
	print "Using short command line\n";
	# fall back to local execution (for use with nrpe)
	open IPMI, "$IPMITOOL -L $PRIV $SDR |" or 
	$p->nagios_exit( 
		return_code => UNKNOWN, 
		message => "ipmitool: $!" 
	);
}
 
# parse ipmitool output
# mainly based on Chris Wilson's code
while ( my $line = <IPMI> ) {
	chomp $line;
	print "Got line: $line\n";
	print "$line\n" if ( $p->opts->verbose );
	unless ($line =~ m/^(.*) \| (.*) \| (\w+)$/)
	{
		$p->nagios_exit(
			return_code => UNKNOWN,
			message => "Bad format in ipmitool output: $line"
		);
	}
	my $name  = trim $1;
	my $value = trim $2;
	my $state = trim $3;
 
	# $uname is used as key, check for doublets 
	my $counter = 1;
	my $uname = "$name";
	while ($goodresults{$uname}) {
		$uname = $name . $counter++;
	}
 
	$counter = 1;
	$uname = "$name";
	while ($badresults{$uname}) {
		$uname = $name . $counter++;
	}
 
	# skip not readable entries
	next if $state eq "ns";
 
	# put bad entries in %badresults
	if ($state ne "ok") {
		$badresults{$uname} = $state;
	}
 
	# rest is good
	$goodresults{$uname} = $value;
 
}
 
close IPMI or 
print "Done reading from ipmi\n";
$p->nagios_exit( 
	return_code => UNKNOWN, 
	message => "ipmitool: $! $?" 
);
 
print Data::Dumper->Dump([\%badresults, \%goodresults], ['badresults', 'goodresults']);
 
# check results an build a readable output 
if (keys %badresults) {
	print "Processing badresults\n";
	$result = CRITICAL;
	foreach my $name (sort keys %badresults) {
		print "  name=$name\n";
		if ( $message ne "" ) {
			$message = $message."; ";
		}
		$message = $message.$name.": ".$badresults{$name};
	}
}
else {
	print "Processing goodresults\n";
	foreach my $name (sort keys %goodresults) {
		print "  name=$name\n";
		next unless $name =~ m/(fan)|(temp)/i or $goodresults{$name} =~ m/(degrees)|(rpm)/i;
		my $value = $goodresults{$name};
		$value =~ s/degrees C//;
		$value =~ s/RPM//;
		$value = trim $value;
		next if $value eq 'nr';
		if ( $message ne "" ) {
			$message = $message."; ";
		}
		$message = $message.$name.": ".$value;
	}
}
 
print "mesage=$message\n";
print "result=$result\n";
 
print "result $result\n " if $p->opts->verbose;
 
$p->nagios_exit( 
	return_code => $result, 
	message => "$message" 
);
