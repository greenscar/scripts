#!/opt/rational/clearquest/bin/cqperl

# Script:      streamread.pl
# Version:     2.0
# Author:      James Sandlin <james.g.sandlin@hhsc.state.tx.us>
# Date:        2008-02-05
# Purpose:     Generates a report of all ClearCase activities in a build of type:
#              "BaseCMActivity", "ChangeActivity", "UCMUtilityActivity", "Defect"
#              A stream name is provided. The script looks at the date of the last 
#              build & the current build. It gets dates from those files and finds
#              All deliveries to the build stream in that time range. It then
#              recursively traces those activites back to their initial contributing
#              activity.

# Syntax:      streamread.pl ARGS
# Arguments:   --stream    n     Name of stream to find initial activities

sub display_hashtable(\\%)
{
   my(%table) = @_;
   my($hasVars) = 0;
   while ( ($key, $value) = each(%table) ) {
      $self->displayMsg("$key => $value");
   }
}   
sub displayMsg {
	my (@msg) = @_;
	foreach (@msg) {
      print($_ . "\n");
		#printf("%s\n", $_);
		#printf ERRLOG ("%s\n", $_);
	}
}
1;
