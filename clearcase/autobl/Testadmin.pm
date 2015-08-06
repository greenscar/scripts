#!/opt/rational/clearquest/bin/cqperl

package Testadmin;
use File::Listing;
use Time::localtime;

sub new
{
   my $self = {};
   
   return $self;
}


##########################################################################
# END OBJECT VAR METHODS
##########################################################################


sub get_prev_tst_lvl
{
   my($self, $helper, $test_lvl, @streams) = @_;
   $helper->println("get_prev_tst_lvl(self, helper, $test_lvl, @streams)");
   my($prev_tst_lvl) = undef;
   my($tmp) = undef;
   foreach(@streams)
   {
      if($_ eq $test_lvl)
      {
         $prev_tst_lvl = $tmp;
      }
      else
      {
         $tmp = $_;
      }
   }
   if($prev_tst_lvl)
   {
      return($prev_tst_lvl);
   }
   return(0);
}


###############################################################################
# END SUB METHODS
###############################################################################
1;
