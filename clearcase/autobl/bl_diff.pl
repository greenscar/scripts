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
$helper->debug(1);  
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
$bfjob->bfroot($ENV{"BF_ROOT"});
$bfjob->start_time($ENV{"CUR_DATE"});
$bfjob->bftag($ENV{"B"});
$bfjob->bfbid($ENV{"BF_BID"});
$bfjob->app($ENV{"APP"});
$bfjob->source_stream($ENV{"SOURCE_STREAM"});
$bfjob->projectname($ENV{"BF_PROJECTNAME_PHYS"});
$bfjob->rebasebl($helper->trim($ENV{"REBASE_BL"}));
if(defined($ENV{"MAIL_TO"}))
{
   my($maillist) = $ENV{"MAIL_TO"};
   my(@mailto) = split(/,\s?/, $maillist);
   $bfjob->mailto(@mailto);
   $helper->println("mailto = " . $bfjob->mailto());
}
################################################################################
################################################################################

################################################################################
# Read in cmd line args
################################################################################
# Parse command-line options and assign to appropriate variables
# Parse command-line options and assign to appropriate variables
#sGetOptions('stream=s' => \$stream);
## ENSURE STREAM NAME ARG IS IN CORRECT FORMAT
#if(!defined($stream))
#{
#   $helper->display_syntax();
#   exit();
#}
GetOptions('bl_prev=s' => \$bl_prev,
           'bl_post=s' => \$bl_post,
           'view_name=s' => \$view_name);
#$helper->println("view_name = $view_name");
#$helper->println("bl_prev = $bl_prev\nbl_post=$bl_post");
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
#$b = new Baseline->new();
#$b->id($bl_prev);
#$b->pvob($bfjob->pvob());
$ccview->current_bl->id($bl_prev);
#$b = undef;
#$ccview_prev->current_bl($b);
#$ccview->rebase_bl->id($bl_post);

################################################################################
################################################################################

################################################################################
# If test level is LOCAL, we don't need to do a rebase.
# Update view, generate report, exit.
################################################################################
#if($ccview->test_level() eq $helper->LOCAL())
#{
#   $message = "MESSAGE: LOCAL - NO REBASE NEEDED";
#   printf("$message\n\n");
#   $helper->message($message);
#   $ccview->update_view($helper);
#   exit(1);
#}
################################################################################
################################################################################

################################################################################
# Figure out what the previous testing level was.
# This will be used to determine if we should create a baseline or where
# to look to determine what baseline to use.
################################################################################
$ccview_prev = $ratladmin->get_prev_ccview($helper, $ccview);
$helper->println("prev = " . $ccview_prev->view_name());
$b = new Baseline->new();
$b->pvob($bfjob->pvob());
$b->id($bl_post);
$ccview_prev->current_bl($b);
$ccview->rebase_bl($b);
$b = undef;
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
$helper->email_bl_diff_report($ccview,$bfjob,$ratladmin);
   #$helper->print_report($ccview, $bfjob, $ratladmin);
   #$helper->email_report($ccview, $bfjob, $ratladmin);
################################################################################
################################################################################



