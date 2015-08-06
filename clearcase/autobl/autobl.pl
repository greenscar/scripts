#!/opt/rational/clearquest/bin/cqperl

use Getopt::Long;
use Time::localtime;
use Ratladmin;
use Testadmin;
use Helper;
use BFJob;
use CCView;

#$numArgs = $#ARGV + 1;
#$numArgs = scaler @ARGV;
$ratladmin = new Ratladmin->new();
$helper = new Helper->new();
$bfjob = new BFJob->new();
$ccview = new CCView->new();

$helper->debug(1);

$bfjob->cleartool_path($ENV{"CLEARTOOL_PATH"});
#$bfjob->pvob($ENV{"PVOB"});
$bfjob->pvob("snbx_project");
$bfjob->unixid($ENV{"LOGNAME"});
#$bfjob->bfroot($ENV{"BF_ROOT"});
$bfjob->bfroot("/home/wasadmin");
#$bfjob->start_time($ENV{"CUR_DATE"});
$bfjob->start_time("2008-01-01_1010");
#$bfjob->bftag($ENV{"BF_TAG"});
$bfjob->bftag(33);
#$bfjob->bfid($ENV{"BF_BID"});
$bfjob->jobid("12345");
$bfjob->projectname($ENV{"BF_PROJECTNAME_PHYS"});

#$ccview->view_name($ENV{"VIEW_NAME"});
$ccview->view_name("SNBX_89.0.0.0_DEV");
$ccview->base_dir($bfjob->bfroot());
$ccview->owner($bfjob->unixid());
$ccview->pvob("snbx_project");

if(!$ccview->parse_view_name($helper))
{
   printf("Source stream %s does not meet stream naming conventions!\n", $ccview->view_name());
   exit 1;
}

if(!$ccview->view_exists($helper, $bfjob))
{
   printf("The view " . $bfjob->bfroot() . $ccview->view_name() . " does not exist!\n\n");
   exit 1;
}

if($ccview->test_level() eq $helper->LOCAL())
{
   printf("LOCAL - NO REBASE NEEDED\n\n");
   exit 1;
}

if($ratladmin->set_stream_structure($helper, $ccview, $bfjob) == 0)
{
   print("ERROR: Not all views exist\n");
   exit 1;
}   
$ratladmin->load_current_baselines($helper, $ccview);
$helper->println("ccview->tstlvl = " . $ccview->test_level());
$helper->println("ccview->current_bl = " . $ccview->current_bl());
$helper->println("ccview->rel_num => " . $ccview->rel_num());
$ccview_prev = $ratladmin->get_prev_ccview($helper, $ccview);
if($ccview_prev == 0)
{
   print("ERROR: Cannot get previous view information\n");
   exit(1);
}

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

#if($ratladmin->load_latest_baselines($helper) == 0)
#{
#   print("ERROR Loading latest baselines");
#   exit(1);
#}

# Make sure prev streams have been rebased before rebasing SIT or UAT
if($ccview_prev->test_level() ne $helper->LOCAL())
{
   if(($ccview->test_level() eq $helper->SIT()) && ($ratladmin->ccview_SIT->current_bl() =~ /^\$?[0-9]$/))
   {
      printf($helper->DEV() . " MUST BE REBASED BEFORE " . $helper->SIT());
      exit 1;
   }
   if(($ccview->test_level() eq $helper->UAT()) && ($ratladmin->ccview_UAT->current_bl() =~ /^\$?[0-9]$/))
   {
      printf($helper->SIT() . " MUST BE REBASED BEFORE " . $helper->UAT());
      exit 1;
   }
}

$ratladmin->set_rebase_bl($helper, $bfjob, $ccview, $ccview_prev);
$helper->println("rebase_bl => " . $ccview->rebase_bl()->id());
$helper->println("current_bl => " . $ccview->current_bl()->id());
if(!((defined $ccview->rebase_bl()) && (defined $ccview->current_bl())))
{
   printf("ERROR: ccview->current_bl & ccview->rebase_bl must both be defined.\n");
   exit 1;
}

$ccview->diff_bl_load($helper);
exit(1);
# Do the actual rebase
my($rebase_return_code) = $ccview->rebase_stream($helper, $bfjob);

$helper->println("rebase_return_code = $rebase_return_code");


#################################################################################
# Everything from this point forward has been done in the BF adaptor in the past. 
# Moving functionality to this script.
#################################################################################

if(defined $ccview->prev_bl())
{
   $helper->println("prev_bl is defined");
}
if($ccview->curr_bl()->id() eq $ccview->new_bl()->id())
{
   $helper->println("curr_bl == newbl");
}



# We have the current test level based on stream name. 
# Using current test level & @streams, determine prev_tst_lvl
#$prev_tst_lvl = get_prev_tst_lvl($test_level, @streams);
#println("prev_tst_lvl = $prev_tst_lvl");

# Set the previous source stream based on provided arg of this source stream.
#$prev_src_stream = $src_stream;
#$prev_src_stream =~ s/([a-zA-Z0-9]+)_(\d\d\.\d\.\d\.\d)_([a-zA-Z0-9]{3,5})/$1_$2_$prev_tst_lvl/;





