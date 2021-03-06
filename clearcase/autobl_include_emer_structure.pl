#!/opt/rational/common/sun5/bin/ratlperl


# This script is based on autobl.pl written by Jordan Klein.
# Changes have been made to accomidate for a new stream structure
#     & integration with IBM Rational Build Forge.

# Specify Perl modules to include here
use Time::localtime;
use Getopt::Long;

$DEV = "DEV";
$SIT = "SIT";
$UAT = "UAT";
$LOCAL = "LOCAL";
$INT = "Tst";
$COMPOSITE = "COMPOSITE";
$CLEARTOOL_PATH = $ENV{"CLEARTOOL_PATH"};
$CLEARTOOL = "$CLEARTOOL_PATH/cleartool";
$PVOB = $ENV{"PVOB"};
$USER = $ENV{"LOGNAME"};
$BFROOT = $ENV{"BF_ROOT"};
$DEBUG = 0;


# Get current date.  Proceed single digit dates with a 0 for readability.
sub get_current_date {
	my $year  = localtime->year() + 1900;
	my $month = ((localtime->mon() + 1) < 10) ? ("0" . (localtime->mon() + 1)) : (localtime->mon() + 1);
	my $day   = (localtime->mday() < 10) ? ("0" . localtime->mday()) : localtime->mday();
	return ($year, $month, $day);
}

# Get current time.  Preceed single digit times with a 0 for readability.
sub get_current_time {
	my $hour   = (localtime->hour() < 10) ? ("0" . localtime->hour()) : localtime->hour();
	my $minute = (localtime->min() < 10) ? ("0" . localtime->min()) : localtime->min();
	my $second = (localtime->sec() < 10) ? ("0" . localtime->sec()) : localtime->sec();
	return ($hour, $minute, $second);
}

# Define the previous source stream based on the source stream provided.
# Some projects (i.e. EMER, OCR) will not have a DEV or SIT stream. 
# In order to determine what the previous stream was for UAT, we must 
#     first check if SIT exists.
# If it does, SIT is previous to UAT. If it doesn't, LOCAL is previous to UAT.
sub set_prev_tst_lvl
{
   my($test_lvl, $src_stream) = @_;
   my($prev_stream) = $src_stream;
   if($test_lvl eq $DEV)
   {
      $prev_tst_lvl = $LOCAL;
   }
   if($test_lvl eq $SIT)
   {
      $prev_tst_lvl = $DEV;
   }
   elsif($test_lvl eq $UAT)
   {
      $prev_stream =~ s/$UAT/$SIT/g;
      println("cleartool lsview | grep $prev_stream");
      my($prev_view) = `cleartool lsview | grep $prev_stream`;
      if(defined($prev_view))
      {
         $prev_tst_lvl = $SIT;
      }
      else
      {
         $prev_tst_lvl = $LOCAL;
      }
   }
   else
   {
      $prev_tst_lvl = $DEV;
   }
   return $prev_tst_lvl;
}

# Print provided args following each with a new line char.
sub println {
   my (@msg) = @_;
	foreach (@msg) {
      print($_ . "\n") if($DEBUG);
	}
}

# Display all available environment variables.
# Used for development / debugging
sub show_all_env_vars
{
   foreach $id (keys(%ENV))
   {
      println("$id = " . $ENV{$id});
   }
}

# Display required syntax for calling this script.
sub display_syntax
{
   println("Syntax autobl.pl ARGS");
   println("Arguments:");
   println("   --bftag <ENVIRONMENT_NAME>  The Build Forge tag of the project calling the script.");
   println("   --srcstream <STREAM_NAME>     Name of stream you wish to build from");
}

# Query the baseline stream and return a list of available baselines
#sub get_baseline_list
#{
#   my($bl_stream) = @_;
#   println("get_baseline_list($bl_stream)");
#   @bl_list = `cd $BFROOT; $CLEARTOOL lsbl -s -stream $bl_stream\@/$PVOB`;
#   println("bl_list => ");
#   println(@bl_list);
#   println("END get_baseline_list($bl_stream)\n");
#   return(@bl_list);
#}

# Using app name & release number, generate the name of the baseline stream.
sub get_baseline_stream
{
   my($app_name, $rel_num) = @_;
   $bl_stream = $app_name . "_" . $rel_num . "_" . $INT;
   return($bl_stream);
}

# Get the name of the latest baseline in the provide stream.
#sub get_baseline_name
#{
#   my($streamdir) = @_;
#   println("get_baseline_name($streamdir)");
#   println("cd $streamdir; $CLEARTOOL lsstream -fmt $BLFMT 2>&1; cd ..;");
#   $_ = `cd $streamdir; $CLEARTOOL lsstream -fmt $BLFMT 2>&1; cd ..;`;
#   /^(baseline:)(.+)(@\/${PVOB})$/;
#   my($baseline_name) = $2;
#   println("END get_baseline_name($streamdir) => $baseline_name\n");
#   return($baseline_name);
#}

# TIERS & PORTAL are configured as each stream has multiple components (VOBs).
# TIERS uses a single composite baseline for all components. 
# PORTAL uses a component baseline for each component.
# In PORTAL, a view for a particular stream will contain multiple components, 
#     only one of which is writable. 
# For instance, to develop SSP, we have a SSP view which contains IES_ssportal & IES_arch.
#     Only IES_ssportal is writable from SSP view.
# When rebasing PORTAL, we need to get the list of modifiable components to determine which
#     component will be rebased.
sub get_modifiable_component_list
{
   my($streamdir) = @_;
   println("get_modifiable_component_list($streamdir)");
   # 1) Figure out what the modifiable components are for this stream.
   println("cd $streamdir; $CLEARTOOL lsproject -cview -fmt %[mod_comps]Cxp");
   my($mod_comps) = `cd $streamdir; $CLEARTOOL lsproject -cview -fmt %[mod_comps]Cxp`;
   println("mod_comps = $mod_comps");
   @modifiable_components = split(",",$mod_comps);
   chomp(@modifiable_components);
   my($size) = scalar(@modifiable_components);
   println("$size modifiable components =>");
   println($modifiable_components);
   if($size > 1)
   {
      $to_return = $COMPOSITE;
   }
   else
   {
      $to_return = $mod_comps;
   }
   println("END get_modifiable_component_list => $to_return");
   return($to_return);
}

# Create a baseline in the provided bl_stream named new_bl_name.
# Return 1 if a baseline was created or 0 if a baseline was not created.
sub create_baseline
{
   my($src_stream, $bl_stream, $new_bl_name, $component) = @_;
   println("create_baseline($src_stream, $bl_stream, $new_bl_name, $component)");
   my($bl_created) = 0;
   $mkbl_options = "-incr -ident";
   my($rc) = undef;
   if($component eq $COMPOSITE)
   {
      $rc = `cd $BFROOT; $CLEARTOOL mkbl -all $mkbl_options -view $bl_stream $new_bl_name 2>&1`;
      println("cd $BFROOT; $CLEARTOOL mkbl -all $mkbl_options -view $bl_stream $new_bl_name 2>&1");
   }
   else
   {
      $rc = `cd $src_stream; $CLEARTOOL mkbl -com $component\@/$PVOB $mkbl_options -view $bl_stream $new_bl_name 2>&1`;
      println("cd $src_stream; $CLEARTOOL mkbl -com $component\@/$PVOB $mkbl_options -view $bl_stream $new_bl_name 2>&1");
   }
   println($rc);
   if ($rc =~ /Created\ baseline/) {
      $bl_created = 1;
   }
   println("bl_created = $bl_created");
   return($bl_created);
}

# Get the foundation baseline name for the specified stream.  Return the name
# if there is a foundation baseline, or undef if not.
sub get_current_foundation_baseline_from_stream {
	my ($streamName, $component) = @_;
   println("get_current_foundation_baseline_from_stream($streamName, $component)");
   $rawBlList = `cd $BFROOT; $CLEARTOOL lsstream -fmt \"%[found_bls]p\" $streamName\@/$PVOB`;
   println("cd $BFROOT; $CLEARTOOL lsstream -fmt \"%[found_bls]p\" $streamName\@/$PVOB");
   println("rawBLList = $rawBlList");
	@rawBlList = split(/\s/, $rawBlList);
   my($bl) = undef;
	if (scalar @rawBlList) {
		foreach $rawBl (@rawBlList) {
			next if ($rawBl =~ /^.*\.\d+$/);
			if ($rawBl !~ /^.*\.\d+]$/) {
            println("bl = $rawBl");
            if($component ne $COMPOSITE)
            {
               println("cd $BFROOT/$streamName; $CLEARTOOL lsbl -fmt \"%[component]Cxp\" $rawBl\@/$PVOB");
               $tempComp = `cd $BFROOT/$streamName; $CLEARTOOL lsbl -fmt \"%[component]Cxp\" $rawBl\@/$PVOB`;
               println("tempComp = $tempComp\ncomponent = $component");
               if($tempComp eq $component)
               {
                  println("tempComp eq component");
                  $bl = $rawBl;
               }
            }
            else
            {
               $bl = $rawBl;
            }
			}
		}
	} else {
      println("END get_current_foundation_baseline_from_stream => undef");
		return undef;
	}
   println("END get_current_foundation_baseline_from_stream => $bl\n");
   return $bl;
}

# Rebase specified stream with specified baseline, using specified view.
sub rebase_stream
{
   my($src_stream, $rebase_bl_name, $src_stream_view) = @_;
   println("rebase_stream($src_stream, $rebase_bl_name, $src_stream_view)");
   my $current_bl_name = `cd $BFROOT; $CLEARTOOL lsstream -fmt \"%[found_bls]p\" -view $src_stream_view 2>&1`;
   my($rc) = undef;
   println("current_bl_name = $current_bl_name\nrebase_bl_name = $rebase_bl_name");
   if($current_bl_name ne $rebase_bl_name)
   {
      println("----------------------- REBASING --------------------------------");
      println ("cd $BFROOT/$src_stream; $CLEARTOOL rebase -view $src_stream_view -baseline $rebase_bl_name\@/$PVOB > /dev/null 2>&1");
      $rc = system("cd $BFROOT/$src_stream; $CLEARTOOL rebase -view $src_stream_view -baseline $rebase_bl_name\@/$PVOB > /dev/null 2>&1");
      println("-----------rc-----------\n$rc\n-------------rc-------------");
      if($rc)
      {
         system("cd $BFROOT/$src_stream; $CLEARTOOL rebase -view $src_stream_view -cancel -force > /dev/null 2>&1");
         println("cd $BFROOT/$src_stream; $CLEARTOOL rebase -view $src_stream_view -cancel -force");
	      return("RC=" . $rc);
      }
      println("cd $BFROOT/$src_stream; $CLEARTOOL rebase -view $src_stream_view -complete -force");
      system("cd $BFROOT/$src_stream; $CLEARTOOL rebase -view $src_stream_view -complete -force > /dev/null 2>&1");
      println("rc = $rc");
      println("------------------------- END REBASING ---------------------------");
      return($rc);
   }
   else  
   {
      println("------------------- NOT REBASING --------------------------------");
      $rc = "current_bl = rebase_bl, so not rebasing stream.";
      println("current_bl = rebase_bl, so not rebasing stream.");
   }
   println("END rebase_stream($src_stream, $rebase_bl_name, $src_stream_view) => $rc\n");
   return($rc);
}
#########################################################################################################
#########################################################################################################
############################################# END OF METHODS ############################################
######################################## BEGIN ACTUAL PROCESSING. #######################################
#########################################################################################################
#########################################################################################################
show_all_env_vars;

# Parse command-line options and assign to appropriate variables
GetOptions( 'bftag=s' => \$bf_tag,
            'srcstream=s' => \$src_stream,
            'bfroot=s' => \$bf_root,
            'bldev=s' => \$bl_dev,
            'blsit=s' => \$bl_sit,
            'bluat=s' => \$bl_uat
          );
# Get arguments
if(! defined($src_stream) || ! defined($bf_tag) || ! defined($bf_root))
{
   print("not defined\n");
   display_syntax();
   exit(1);
}


# Verify that srcStreamName is in the correct format:
# projectName_ww.x.y.z_buildName
if($src_stream !~ /^([a-zA-Z]+)_([\d|x][\d|x]\.[\d|x]\.[\d|x]\.[\d|x])(_\w*)?_([a-zA-Z]+)$/)
{
#if ($src_stream !~ /^([a-zA-Z]+)_(\d\d\.\d\.\d\.\d)_([a-zA-Z]{3,5})$/) {
	printf("Source stream %s does not meet stream naming conventions!\n", $src_stream);
	exit 1;
}

# Verify the srcStreamName exists.
if(! -d "$bf_root$src_stream")
{
   printf("The view $bf_root$src_stream does not exist!\n\n");
   exit 1;
}

my($app_name, $rel_num, $rel_name, $test_level) = undef;

# Parse src_stream to determine app, release, stream level
$src_stream =~ /^([a-zA-Z]+)_([\d|x][\d|x]\.[\d|x]\.[\d|x]\.[\d|x])(_\w*)?_([a-zA-Z]+)$/;
if(defined($4))
{
   ($app_name, $rel_num, $streampurpose, $test_level) = ($1, $2, $3, $4);
}
else
{
   ($app_name, $rel_num, $test_level) = ($1, $2, $3);
}


# Define previous test level based on provided arg of this source stream.
$prev_tst_lvl = set_prev_tst_lvl($test_level, $src_stream);

# Set the previous source stream based on provided arg of this source stream.
$prev_src_stream = $src_stream;
$prev_src_stream =~ s/([a-zA-Z]+)_(\d\d\.\d\.\d\.\d)_([a-zA-Z]{3,5})/$1_$2_$prev_tst_lvl/;

# If test level == LOCAL (The Dev stream), don't do anything. Exit.
if($test_level eq $LOCAL)
{
   printf("LOCAL - NO REBASE NEEDED\n\n");
   exit 1;
}
elsif(($test_level eq $SIT) && ($bl_sit =~ /^\$?[0-9]$/))
{
   printf("DEV MUST BE REBASED BEFORE SIT");
   exit 1;
}
elsif($prev_tst_lvl eq $LOCAL)
{
   println("test lvl eq local.... continue");
}
elsif(($test_level eq $UAT) && ($bl_uat =~ /^\$?[0-9]$/))
{
   printf("SIT MUST BE REBASED BEFORE UAT");
   exit 1;
}

# Set date format to YYYY-MM-DD and time format to HH:MM
($year, $month, $day) = get_current_date();
$curr_date = $year . "-" . $month . "-" . $day;
($hour, $minute, $second) = get_current_time();
$curr_time = $hour . "." . $minute . "." . $second;

# Define int stream view name
$int_stream_view = $USER . "_" . get_baseline_stream($app_name, $rel_num);

# Define src stream view name
$src_stream_view = $USER . "_" . $src_stream;

# Get a list of baselines from the baseline stream
#@bl_list = get_baseline_list($bl_stream);
# Get the # of baselines found
#$number_of_baselines = scalar(@bl_list);

$short_rel_num = $rel_num;
$short_rel_num =~ s/\.//g;

# Get list of what components are modifiable.
my($component) = get_modifiable_component_list($src_stream);

# This will need modified to work with apps other than Portal.
if(!defined($component))
{
   exit 1;
}
my($foundation_bl_name) = undef;
# Create the baseline only for DEV and set baseline name for upcoming rebase
# For SIT, get previous successful DEV baseline
# For UAT, get previous SIT baseline
if($prev_tst_lvl eq $LOCAL)
{  
   # 1) Construct baseline name
   $new_bl_name = $curr_date . "_" . $curr_time . "_" . $app_name . "_" . $short_rel_num . "_" . $bf_tag;  
   # 2) Create new baseline with this name
   $bl_created = create_baseline($src_stream, $int_stream_view, $new_bl_name, $component);
   if($bl_created)
   {
      println("new_bl_name=#$new_bl_name#");
   }
   else
   {
      exit 1;
   }
   # 3) Set baseline name for rebase baseline
   $rebase_bl_name = $new_bl_name;
   if(defined($bl_dev))
   {
      $foundation_bl_name = $bl_dev;
   }
}
elsif($test_level eq $SIT)
{
   $rebase_bl_name = $bl_dev;
   if(defined($bl_sit))
   {
      $foundation_bl_name = $bl_sit;
   }
}
elsif($test_level eq $UAT)
{
   $rebase_bl_name = $bl_sit;
   if(defined($bl_uat))
   {
      $foundation_bl_name = $bl_uat;
   }
}

# Get foundation baseline from source stream
if(!defined($foundation_bl_name))
{
   $foundation_bl_name = get_current_foundation_baseline_from_stream($src_stream, $component);
}
if (defined $foundation_bl_name) {
	println("NOTICE:  The current foundation baseline of stream $src_stream is $foundation_bl_name");
}

# Rebase build stream only if new baseline is different from foundation baseline,
#  OR if build is for DEV.
println("foundation_bl_name = $foundation_bl_name\nrebase_bl_name = $rebase_bl_name\nsrc_stream = $src_stream");
if(($foundation_bl_name ne $rebase_bl_name) || ($prev_tst_lvl eq $LOCAL))
{
   $rebased = rebase_stream($src_stream, $rebase_bl_name, $src_stream_view);
   if($rebased > 0)
   {
        exit $rebased;
   }
   println("$rebased");
   #print("OLDBL=$foundation_bl_name\nNEWBL=$rebase_bl_name\n");
   print($test_level . "_OLDBL=$foundation_bl_name\n" . $test_level . "_NEWBL=$rebase_bl_name\n");
}
else
{
   print("NO REBASE NEEDED\n");
}

