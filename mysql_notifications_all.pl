#!/usr/bin/perl
BEGIN {
   unless ($ENV{BEGIN_BLOCK}) {
       $ENV{LD_LIBRARY_PATH} = "/opt/mysql/client/lib/mysql";
       $ENV{BEGIN_BLOCK} = 1;
       exec 'env',$0,@ARGV;
   
   }
}

use strict;
use warnings;
use DBI;
use FileHandle;
use Getopt::Long;

my($server) = undef;
my($db) = undef;
my($user) = undef;
my($password) = "o7R6og2ia7";
my($value) = undef;
my($field) = undef;


GetOptions('server=s' => \$server,
           'db=s' => \$db,
           'user=s' => \$user,
           'field=s' => \$field,
           'value=s' => \$value);


if(!defined($server) || !defined($db) || !defined($user) || !defined($field) || !defined($value))
{
   println("Syntax: mysql_notifications_all.pl ARGS");
   println("Arguments:");
   println("   --server <DB SERVER> The name of the server where mysql is hosted");
   println("   --db <DB NAME> The name of the database you want to query");
   println("   --user <DB USER> The name of the user to connect to the database");
   println("   --field <DD FIELD> The stored procedure field");
   println("   --value <DB VALUE> The stored procedure value");
}
my($dbconn) = DBI->connect("dbi:mysql:host=$server;database=$db", $user, $password) or die("Error connecting to DB $DBI::errstr");

my($check_sql) = $dbconn->prepare("call netops.notification_monitor('$field', '$value','R',null)") or die ("DBI::err => $DBI::errstr");

$check_sql->execute or die ("DBI::err => $DBI::errstr");

print("Result:");

while (my(@results) = $check_sql->fetchrow_array())
{
     print(@results);
}

$check_sql->finish or die DBI::err.": ".$DBI::errstr;

$dbconn->disconnect;

# Print provided args following each with a new line char.
sub println {
   my (@msg) = @_;
        foreach (@msg) {
      print($_ . "\n");
        }
}

