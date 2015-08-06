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

$helper->ucm_stream($stream);

@this_array = ("2008-07-26 00:10:00", "2008-07-28 15:48:00", "2008-07-30 20:20:00", "2008-07-31 17:51:00");
@prior_view_array = ("2008-07-24 18:00:00", "2008-07-25 23:10:00", "2008-07-28 14:48:00", "2008-07-30 19:20:00");
@this_view_array = ("2008-07-25 23:10:00", "2008-07-28 14:48:00", "2008-07-30 19:20:00", "2008-07-31 16:51:00");

for ($timestampcounter=0; $timestampcounter<4; $timestampcounter++)
{
   print("beginning of $timestampcounter\n");
   print("Processing build from " . $this_array[$timestampcounter] . "\n");
   $helper->datestring_this_build($this_array[$timestampcounter]);
   $helper->datestring_prior_view_update($prior_view_array[$timestampcounter]);
   $helper->datestring_this_view_update($this_view_array[$timestampcounter]);
   
   #$helper->datestring_this_build("2008-07-26 00:10:00")
   #$helper->datestring_prior_view_update("2008-07-24 18:00:00");
   #$helper->datestring_this_view_update("2008-07-26 23:00:00");
   # ENSURE STREAM NAME ARG IS IN CORRECT FORMAT
   if(!defined($stream))
   {
      $helper->display_syntax();
      exit();
   }
   
   # BEGIN HTML GENERATION
   
   # GET A LIST OF ALL ACTIVITIES IN THIS STREAM
   $ccadmin->load_acts_since_prior_build_w_contribs($helper);
   #GENERATE HTML
   $final_string = $helper->generate_html_report($ccadmin);
   $toreturn = $helper->write_to_file($final_string);
   $helper->println("helper->dest_file_name = " . $helper->dest_file_name());
   print $helper->dest_file_name();
   print("end of $timestampcounter\n");
}
################################################################################
# write_to_file writes the string provided to the file provided
# @args:
#  $what_to_write => the String to write to the file.
################################################################################






