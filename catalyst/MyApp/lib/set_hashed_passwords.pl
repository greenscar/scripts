#!/opt/perl-5.10.0/bin/perl
    
use strict;
use warnings;

use MyApp::Schema;

my $schema = MyApp::Schema->connect('dbi:mysql:dbname=books', 'root', 'pwd');

my @users = $schema->resultset('User')->all;

foreach my $user (@users) {
  $user->password('password');
  $user->update;
}
