#!/usr/bin/perl -w
use strict;
my %users;
my $who = `who`;
my $hl = 0;
foreach $_ (split(/\n/, $who)) {
    next unless m/^.*?\s+(\S*).*\((.*)\)$/;
    #print "$1 -> $2\n";
    $users{$1} = $2;
    my $l = length($2);
    $hl = $l if $l > $hl;
}
my $ul = 0;
my $pl = 0;
my $cl = 0;
my $ps = `ps -a -o tty,user,pid,pcpu,args`;
foreach $_ (split(/\n/, $ps)) {
    next unless m/^(\S+)\s+(\S+)\s+(\S+)\s+(\S+)\s+(.*)$/;
    next unless $users{$1};
    my $l = length($2);
    $ul = $l if $l > $ul;
    $l = length($3);
    $pl = $l if $l > $pl;
    $l = length($4);
    $cl = $l if $l > $cl;
}
foreach $_ (split(/\n/, $ps)) {
    next unless m/^(\S+)\s+(\S+)\s+(\S+)\s+(\S+)\s+(.*)$/;
    next unless $users{$1};
    my $h = sprintf("%${hl}s", $users{$1});
    my $u = sprintf("%${ul}s", $2);
    my $p = sprintf("%${pl}s", $3);
    my $c = sprintf("%${cl}s", $4);
    print "$h $u $p $c $5\n";
}

