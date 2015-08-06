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

###############################################################################
# Baseline Object 
###############################################################################
package Baseline;
sub new
{
   my $self = {};
   $self->{id} = undef;
   $self->{date} = undef;
   bless $self;
   return $self;
}
sub id
{    
   my ( $self, $id ) = @_;                         
   $self->{id} = $id if defined($id);
   return $self->{id};
}
sub date
{    
   my ( $self, $date ) = @_;                         
   $self->{date} = $date if defined($date);
   return $self->{date};
}
###############################################################################
# END Baseline Object 
###############################################################################
1;
