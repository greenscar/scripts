#!/opt/rational/clearquest/bin/cqperl

package CCProject;
use Helper;
use BFJob;
use CCView;

sub new
{
   my $self = {};
   
   
   return $self;
}


##########################################################################
# END OBJECT VAR METHODS
##########################################################################
# Check the bls & return the diff count
# This is going to be used to ensure we don't rebase if there are no code differences. 
# The only place this needs to be used is going from TST to DEV (NORMAL structure) 
#     or TST to UAT (EMER structure).



###############################################################################
# END SUB METHODS
###############################################################################
1;
