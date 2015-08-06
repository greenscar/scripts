#!/opt/rational/common/sun5/bin/ratlperl

#
# This script creates views on the build box for the given release.
#
use Getopt::Long;

@STREAM_LEVELS = ("LOCAL", "DEV", "SIT", "UAT", "TST");
@APPS = ("SSP", "STP", "TLM");
$HOME = "/home/wasadmin/";
$STG_LOC = "clearcase_home_ccstg";
$PVOB = "DC_Projects";

%LOAD_RULES = (
   "SSP" => ["IES_arch", "IES_ssportal"],
   "STP" => ["IES_arch", "IES_ssportal", "IES_stportal"],
   "TLM" => ["IES_arch", "IES_TLM"]
);


# Print provided args following each with a new line char.
sub println {
   my (@msg) = @_;
	foreach (@msg) {
      print($_ . "\n");
	}
}


# Display required syntax for calling this script.
sub display_syntax
{
   println("Syntax create_views.pl ARGS");
   println("Arguments:");
   println("   --rel_num <RELEASE_NUM>     Release number in format XX.X.X.X");
}


########################### PROCEDURE BELOW ####################################

# Get arguments
GetOptions('rel_num=s' => \$rel_num);

# Ensure args are defined in correct syntax
if((!defined($rel_num)) || ($rel_num !~ /\d\d\.\d\.\d\.\d/))
{
   display_syntax();
   exit(1);
}

# Go Home
chdir($HOME);

# Create all views & set config specs.
foreach $app (@APPS)
{
   foreach $stream_lvl (@STREAM_LEVELS)
   {
      chdir($HOME);
      $streamname = $app . "_" . $rel_num . "_" . $stream_lvl;  
      if(-d $HOME . $streamname)
      {
         println("view already exists");
      }
      else
      {
         println("cleartool mkview -snap -stgloc $STG_LOC -stream $streamname\@/$PVOB $streamname");
         system("cleartool mkview -snap -stgloc $STG_LOC -stream $streamname\@/$PVOB $streamname");
      }
      println("cd $HOME$streamname");
      chdir($HOME .$streamname);
      @app_load_rules = @{$LOAD_RULES{$app}};
      my($rules) = "";
      my($rule) = undef;
      foreach $rule (@{$LOAD_RULES{$app}})
      {
         $rules .= "\\$rule ";
      }        
      println("cleartool update -add_loadrules $rules");
      system("cleartool update -add_loadrules $rules"); 
   }
}
