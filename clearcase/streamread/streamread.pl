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

use Getopt::Long;
use Time::localtime;
use Activity;
use ClearCase;
use Helper;
$numArgs = $#ARGV + 1;
$numArgs = scaler @ARGV;
$ccadmin = new ClearCase->new();
$helper = new Helper->new();
$helper->debug(1);
# Parse command-line options and assign to appropriate variables
GetOptions('stream=s' => \$stream);

# Set the view name based on the provided argument
$helper->ucm_stream($stream);

# Look at build files and view update files and determine what dates to load
$helper->set_build_dates_via_files();

# ENSURE STREAM NAME ARG IS IN CORRECT FORMAT
if(!defined($stream))
{
   $helper->display_syntax();
   exit();
}

# BEGIN HTML GENERATION

# GET A LIST OF ALL ACTIVITIES IN THIS STREAM
$ccadmin->load_acts_since_prior_build_w_contribs($helper);
# INSERT INTO DB
$ccadmin->insert_acts_into_db($helper);
#GENERATE HTML
$final_string = $helper->generate_html_report($ccadmin);
$helper->write_to_file($final_string);
#$helper->println("helper->dest_file_name = " . $helper->dest_file_name());
print $helper->dest_file_name();

################################################################################
# write_to_file writes the string provided to the file provided
# @args:
#  $what_to_write => the String to write to the file.
################################################################################






