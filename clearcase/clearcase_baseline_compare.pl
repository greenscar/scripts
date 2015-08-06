#!ccperl

#############################################################################
#
# Script:        blcompare.pl
# Author:        Jordan Klein <jordan.m.klein@accenture.com>
# Company:       Accenture
# Project:       Texas ACCESS Alliance
# Date written:  08/08/2006
# Scope:         Use from anywhere
# Version:       1.0
# Purpose:       Compares two specified baselines.  Displays list of activites
#                that exist in one but not the other.  Baselines can be
#                specified as the full baseline name or as a baseline
#                name and -pred for the baseline's predecessor.

# Notes:    Date           Version    Note
#           08/08/2006     1.0        Original release

# Some static variables
$PROG   = "blcompare.pl";
$VER    = "1.0";
$AUTHOR = "Jordan Klein <jordan.m.klein\@txaccess.com>";
$PVOB = "\\TAA_projects";

# Header.  Gotta take credit.
printf("\n%s v%s - By %s\n\n", $PROG, $VER, $AUTHOR);

# Display if not enough arguments are passed to the script, then exit
if (scalar @ARGV < 2) {
	printf("Syntax:   ccperl [drive:\\][path\\]%s <baseline1> <baseline2>\n", $PROG);
	printf("          [ .. ] is optional.  < .. > is not.\n");
	printf("\nUse -pred in place of baseline1 or baseline2 to compare the specified baseline to it's predecessor\n");
	printf("\nThe order in which baselines are provided is unimportant, as the examples below show\n");
	printf("\nThe following four examples show how to compare a baseline to it's predecessor:\n");
	printf("Example:  ccperl c:\\scripts\\%s 2006-04-28_TIERS_55.0.0.1 2006-04-25_TIERS_55.0.0.0\n", $PROG);
	printf("Example:  ccperl c:\\scripts\\%s 2006-04-25_TIERS_55.0.0.0 2006-04-28_TIERS_55.0.0.1\n", $PROG);
	printf("Example:  ccperl c:\\scripts\\%s 2006-04-28_TIERS_55.0.0.1 -pred\n", $PROG);
	printf("Example:  ccperl c:\\scripts\\%s -pred 2006-04-28_TIERS_55.0.0.1\n", $PROG);
	printf("\nThe following two examples show how to compare two baselines that aren't sequential:\n");
	printf("Example:  ccperl c:\\scripts\\%s 2006-04-28_TIERS_55.0.0.1 2006-03-28_TIERS_54.2.0.0\n", $PROG);
	printf("Example:  ccperl c:\\scripts\\%s 2006-03-28_TIERS_54.2.0.0 2006-04-28_TIERS_55.0.0.1\n", $PROG);	
	exit 1;
}

# Assign $bl1 and $bl2 variables depending on how the script was invoked
# Always make sure $bl2 is "-pred" if "-pred" was specified
if ($ARGV[0] eq "-pred") {
	$bl1 = $ARGV[1];
	$bl2 = $ARGV[0];
} elsif ($ARGV[1] eq "-pred") {
	$bl1 = $ARGV[0];
	$bl2 = $ARGV[1];
} else {
	$bl1 = $ARGV[0];
	$bl2 = $ARGV[1];
}

# Exit if same baseline was specified for both parameters
die ("FATAL:  Can't compare identical baselines\n") if ($bl1 eq $bl2);

# Check that baselines specified are valid
$rc = system("cleartool lsbl $bl1\@$PVOB > NUL 2>&1");
die ("FATAL:  $bl1 is not a valid baseline\n") if $rc;
if ($bl2 ne "-pred") {
	$rc = system("cleartool lsbl $bl2\@$PVOB > NUL 2>&1");
	die ("FATAL:  $bl2 is not a valid baseline\n") if $rc;
}

# Print what we're doing
printf("Comparing activities between the following baselines:\n");
printf("\tBaseline 1:  %s\n", $bl1);
printf("\tBaseline 2:  ");
if ($bl2 ne "-pred") {
	printf("%s\n", $bl2);
} else {
	printf("Baseline 1's predecessor\n");
}
printf("\nThis could take a long time depending upon how far apart in time the specified\n");
printf("baselines are, so please be patient\n");

# Get full list of activities that differ between $bl1 and $bl2
if ($bl2 eq "-pred") {
	@rawBl = `cleartool diffbl -pred $bl1\@$PVOB`;
} else {
	@rawBl = `cleartool diffbl $bl1\@$PVOB $bl2\@$PVOB`;
}

# Strip out just activity number from the activity list
foreach $line (@rawBl) {
	chomp($line);
	($junk,$act) = split(/[\s\@]/, $line);
	push(@activities, $act);
}

# Build list of non-integration activities based on the activity's type
# Include any activity that isn't a UCMUtility activity
# A non-integration activity is one created by a developer and not by
# a rebase or deliver operation
# Those types are called integration activities
foreach $act (@activities) {
#	if (`cleartool lsact -fmt \"%[crm_record_type]p\" $act\@$PVOB` ne "UCMUtilityActivity") {
	$rc = system("cleartool lsact -contrib $act\@$PVOB > NUL 2>&1");
	if ($rc) {
		push(@nonIntegrationActivities, $act);
	}
}

#######################################################################
# Modify this section if you want to change the output formatting
#######################################################################

# Print sorted list of non-integration activities
$count = 0;
printf("\nNon-integration activities:\n");
foreach $act (sort @nonIntegrationActivities) {
	$owner = `cleartool lsact -fmt \"%[owner]p\" $act\@$PVOB`;
	$headline = `cleartool lsact -fmt \"%[headline]p\" $act\@$PVOB`;
	printf("%s\t%20s\t%s\n", $act, $owner, $headline);
	$count++
}

# Print count of total number of non-integration activities
printf("\nThere are a total of %d non-integration activities\n", $count);

exit 0;