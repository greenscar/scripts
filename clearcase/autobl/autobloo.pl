################################################################################
# Script:      autobloo.pl
# Version:     3.0
# Author:      James Sandlin <james.g.sandlin@hhsc.state.tx.us>
# Date:        2009-02-05
# Purpose:     This set of scripts is used to automate tasks in the stream 
#                 structure defined as the HHSC EA Standard.
#              Tasks automated by this package include:
#                 1) Baseline creation
#                 2) Stream rebasing
#                 3) Rebase Reporting
# Syntax:      This package is designed to work with IBM BuildForge.
#              In order to use without BuildForge, one must first
#                 define all required environment variables.
#              The driver script to this package is autobloo.pl
################################################################################

use Getopt::Long;
use Time::localtime;
use Ratladmin;
use Testadmin;
use Helper;
use BFJob;
use CCView;
$ratladmin = new Ratladmin->new();

################################################################################
# Create Helper Object
################################################################################
$helper = new Helper->new();
if($ENV{"DEBUG_AUTOBL"} == 0 || $ENV{"DEBUG_AUTOBL"} == 1)
{   
   $helper->debug($ENV{"DEBUG_AUTOBL"});
}
else
{
   $helper->debug(0);  
}
$helper->cqdbname("TAA");
################################################################################
################################################################################

################################################################################
# Create BFJob Object
# Populate with Environment Vars
################################################################################
$bfjob = new BFJob->new();
$bfjob->cleartool_path($ENV{"CLEARTOOL_PATH"});
$bfjob->pvob($ENV{"PVOB"});
$bfjob->unixid($ENV{"LOGNAME"});
$bfjob->bfroot($ENV{"BF_SERVER_ROOT"});
$bfjob->start_time($ENV{"CUR_DATE"});
$bfjob->bftag($ENV{"BF_TAG"});
$bfjob->b($ENV{"B"});
$bfjob->bfbid($ENV{"BF_BID"});
$bfjob->app($ENV{"APP"});
$bfjob->source_stream($ENV{"SOURCE_STREAM"});
$bfjob->projectname($ENV{"BF_PROJECTNAME_PHYS"});
$bfjob->rebasebl($helper->trim($ENV{"REBASE_BL"}));
$bfjob->force_rebase($ENV{"FORCE_REBASE"});
if(defined($ENV{"MAIL_TO"}))
{
   my($maillist) = $ENV{"MAIL_TO"};
   my(@mailto) = split(/,\s?/, $maillist);
   $bfjob->mailto(@mailto);
   $helper->println("mailto = " . $bfjob->mailto());
}
$helper->autobllog($bfjob->bfroot() . "/" . $bfjob->projectname() . "/" . $bfjob->bftag() . "/" . $bfjob->bfbid() . "_autobl.log");
$helper->println("autobllog = " . $helper->autobllog());
################################################################################
################################################################################

################################################################################
# Read in cmd line args
################################################################################
# Parse command-line options and assign to appropriate variables
GetOptions('view_name=s' => \$view_name);
# Make sure required args are set.
if(! defined($view_name))
{
   print("ERROR: Source Stream is not defined\n");
   exit(1);
}
################################################################################
################################################################################

################################################################################
# Create CCView Object
################################################################################
$ccview = new CCView->new();
$ccview->view_name($view_name);
$ccview->base_dir($bfjob->bfroot());
$ccview->owner($bfjob->unixid());
$ccview->pvob($bfjob->pvob());
################################################################################
################################################################################

# Check provided view name & ensure it meets naming conventions
if(!$ccview->parse_view_name($helper))
{
   printf("ERROR: Source stream %s does not meet stream naming conventions!\n", $ccview->view_name());
   exit 1;
}

# Check provided view name & ensure it exists on the build box.
if(!$ccview->view_exists($helper, $bfjob))
{
   printf("ERROR: The view " . $bfjob->bfroot() . "/" . $ccview->view_name() . " does not exist!\n\n");
   exit 1;
}

# Using views, get the stream structure
if($ratladmin->set_stream_structure($helper, $ccview, $bfjob) == 0)
{
   print("ERROR: Not all views exist\n");
   exit 1;
}   

# Now that we have a list of views, get the baselines for each test level.
$ratladmin->load_current_baselines($helper, $ccview);
################################################################################
################################################################################

################################################################################
# If test level is LOCAL, we don't need to do a rebase.
# Update view, generate report, exit.
################################################################################
if($ccview->test_level() eq $helper->LOCAL())
{
   $message = "MESSAGE: LOCAL - NO REBASE NEEDED";
   printf("$message\n\n");
   $helper->message($message);
   $ccview->update_view($helper);
   exit(1);
}
################################################################################
################################################################################

################################################################################
# Figure out what the previous testing level was.
# This will be used to determine if we should create a baseline or where
# to look to determine what baseline to use.
################################################################################
$ccview_prev = $ratladmin->get_prev_ccview($helper, $ccview);
if($ccview_prev == 0)
{
   print("ERROR: Cannot get previous view information\n");
   exit(1);
}
################################################################################
################################################################################

################################################################################
# DEBUG Prints
################################################################################
$helper->println("ccview->tstlvl = " . $ccview->test_level());
$helper->println("ccview->current_bl = " . $ccview->current_bl());
$helper->println("ccview->app_name -> " . $ccview->app_name());
$helper->println("ccview->test_level -> " . $ccview->test_level());
$helper->println("ccview->rel_num -> " . $ccview->rel_num());
$helper->println("ccview->rel_name -> " . $ccview->rel_name());
$helper->println("ccview->view_name -> " . $ccview->view_name());
$helper->println("ccview_prev->app_name -> " . $ccview_prev->app_name());
$helper->println("ccview_prev->test_level -> " . $ccview_prev->test_level());
$helper->println("ccview_prev->rel_num -> " . $ccview_prev->rel_num());
$helper->println("ccview_prev->rel_name -> " . $ccview_prev->rel_name());
$helper->println("ccview_prev->view_name -> " . $ccview_prev->view_name());
$helper->println("pvob -> " . $ccview->pvob());
################################################################################
################################################################################

################################################################################
# Make sure prev streams have been rebased before rebasing SIT or UAT
################################################################################
if($ccview_prev->test_level() ne $helper->LOCAL())
{
   if(($ccview->test_level() eq $helper->SIT()) && ($ratladmin->ccview_SIT->current_bl() =~ /^\$?[0-9]$/))
   {
      printf("ERROR: " . $helper->DEV() . " MUST BE REBASED BEFORE " . $helper->SIT());
      exit 1;
   }
   if(($ccview->test_level() eq $helper->UAT()) && ($ratladmin->ccview_UAT->current_bl() =~ /^\$?[0-9]$/))
   {
      printf("ERROR: " . $helper->SIT() . " MUST BE REBASED BEFORE " . $helper->UAT());
      exit 1;
   }
}
################################################################################
################################################################################

################################################################################
# Figure out what the rebase baseline is. This is done through the logic of
#    the stream structure.
# If the rebasebl was provided via environment var, set that as the rebase baseline.
################################################################################
my $retrebase = "";

if((defined($bfjob->rebasebl())) && ($bfjob->rebasebl()))
{
   $retrebase = $ratladmin->set_rebase_bl($helper, $bfjob, $ccview, $ccview_prev, $bfjob->rebasebl());
   $helper->println($retrebase);
   
}
else

{
   $retrebase = $ratladmin->set_rebase_bl($helper, $bfjob, $ccview, $ccview_prev);
   $helper->println($retrebase);
   
}

#to check for 1 or string

if ($retrebase ne 1)
{
  
#$helper->print_report($ccview, $bfjob, $ratladmin);
$helper->email_report($ccview, $bfjob, $ratladmin, $retrebase);
printf("$retrebase\n");
exit 1;
}


$helper->println("rebase_bl => " . $ccview->rebase_bl()->id());
$helper->println("current_bl => " . $ccview->current_bl()->id());


if(!((defined $ccview->rebase_bl()) && (defined $ccview->current_bl())))
{
   printf("ERROR: ccview->current_bl & ccview->rebase_bl must both be defined.\n");
   exit 1;
}
################################################################################
################################################################################


################################################################################
# Now that we know our current baseline & the baseline we wish to use for
#     rebasing, get a count on the number of differences.
################################################################################
$rc = $ccview->diff_bl_load($helper);
if($rc =~ /Error/)
{
   printf("ERROR: $rc");
   exit(1);
}
################################################################################
################################################################################


################################################################################
# Do the actual rebase
# First, check the bl diff count. 
# If differences == 0, do not do the rebase. Generate the report & exit.
# If differences > 0, do the rebase.
################################################################################
my($rebase_return_code) = undef;
if($ccview->rebase_bl()->get_num_activities() > 0)
{
   $rebase_return_code = $ccview->rebase_stream($helper, $bfjob);
   # Now that we have rebased the stream in this view, we need to replace
   # the view currently held by ratladmin with this one.
   $ratladmin->add_view($ccview);
   $helper->print_report($ccview, $bfjob, $ratladmin);
   $helper->email_report($ccview, $bfjob, $ratladmin);
   $helper->store_html_report($ccview, $bfjob, $ratladmin);
}
else
{
   $helper->message("MESSAGE: NO DIFF IN BASELINES -- NOT REBASING\n<br>\nMESSAGE: " . $ccview->rebase_bl()->id() . " SAME AS " . $ccview->current_bl()->id() . "\n");
   $helper->print_report($ccview, $bfjob, $ratladmin);
   $helper->email_report($ccview, $bfjob, $ratladmin);
   $helper->store_html_report($ccview, $bfjob, $ratladmin);
   printf($helper->message() . "\n");
   exit 1;
}
################################################################################
################################################################################

################################################################################
# Rebase is completed. 
# If failure, exit out.
################################################################################
if($rebase_return_code != 0)
{
   printf("ERROR: Rebase failed with RC=$rebase_return_code\n");
   exit($rebase_return_code);
}

################################################################################
################################################################################



