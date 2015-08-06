#!/opt/rational/common/sun5/bin/ratlperl
# ratlperl streamread.pl --days=1 --stream=DEV
# USE ratlperl -> RATIONAL PERL
use Getopt::Long;
use Time::localtime;
use Activity;
use ClearCase;
use Helper;
$numArgs = $#ARGV + 1;
$ccadmin = new ClearCase->new();
$helper = new Helper->new();
$helper->debug(1);
# Parse command-line options and assign to appropriate variables
GetOptions('stream=s' => \$stream);
$helper->println("stream = $stream");
$helper->ucm_stream($stream);

# ENSURE STREAM NAME ARG IS IN CORRECT FORMAT
if(!defined($stream))
{
   $helper->display_syntax();
   exit();
}

$helper->datestring_this_build("2008-08-01 16:32:00");
$helper->datestring_prior_view_update("2008-07-31 16:51:00");
$helper->datestring_this_view_update("2008-08-01 15:32:00");
$helper->println("dates are set");


# BEGIN HTML GENERATION

# GET A LIST OF ALL ACTIVITIES IN THIS STREAM
$helper->println("about to load");
$ccadmin->load_acts_since_prior_build_w_contribs($helper);
#GENERATE HTML
$final_string = $helper->generate_html_report($ccadmin);
$toreturn = $helper->write_to_file($final_string);
$helper->println("helper->dest_file_name = " . $helper->dest_file_name());
print $helper->dest_file_name() . "\n";
#}
################################################################################
# write_to_file writes the string provided to the file provided
# @args:
#  $what_to_write => the String to write to the file.
################################################################################






