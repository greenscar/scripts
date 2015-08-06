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
$BLFMT = "\%[found_bls]CXp";
$DEBUG = 1;
$STREAM_STRUCTURE_NORM = "NORM";
$STREAM_STRUCTURE_EMER = "EMER";
%STREAM_STRUCTURES = (
   $STREAM_STRUCTURE_NORM => [$LOCAL, $DEV, $SIT, $UAT],
   $STREAM_STRUCTURE_EMER => [$LOCAL, $UAT]
   );

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
# Some projects (i.e. EMER, OCR) will not have a SIT stream. 
# In order to determine what the previous stream was for UAT, we must 
#     first check if SIT exists.
# If it does, SIT is previous to UAT. If it doesn't, DEV is previous to UAT.
#sub set_prev_tst_lvl
#{
#   my($test_lvl, $src_stream) = @_;
#	println("set_prev_tst_lvl($test_lvl, $src_stream)");
#   my($prev_stream) = $src_stream;
#   if($test_lvl eq $SIT)
#   {
#      $prev_tst_lvl = $DEV;
#   }
#   elsif($test_lvl eq $UAT)
#   {
#      $prev_stream =~ s/$UAT/$SIT/g;
#      println("cleartool lsview | grep $prev_stream");
#      my($prev_view) = `cleartool lsview | grep $prev_stream`;
#      if(defined($prev_view))
#      {
#         $prev_tst_lvl = $SIT;
#      }
#      else
#      {
#         $prev_tst_lvl = $DEV;
#      }
#   }
#   else
#   {
#      $prev_tst_lvl = $DEV;
#   }
#   return $prev_tst_lvl;
#}

sub get_prev_tst_lvl
{
   my($test_lvl, @streams) = @_;
   println("get_prev_tst_lvl($test_lvl, @streams)");
   my($prev_tst_lvl) = undef;
   my($tmp) = undef;
   foreach(@streams)
   {
      #println("var = " . $_);
      if($_ eq $test_lvl)
      {
         $prev_tst_lvl = $tmp;
         #println("prev_tst_lvl = $prev_tst_lvl");
      }
      else
      {
         $tmp = $_;
         #println("tmp = $tmp");
      }
   }
   if($prev_tst_lvl)
   {
      return($prev_tst_lvl);
   }
   exit 1;
}

# There are 2 stream strucures based on the type of release.
# Determine the structure being used.
# This method is dependent on views already existing. 
# If we don't want to depend on views existing, 
#    replace -d $stream with `cleartool lsview | grep $prev_stream`
sub get_stream_structure
{
   my($test_lvl, $src_stream) = @_;
   println("get_stream_structure($test_lvl, $src_stream)");
   my($int_stream) = $src_stream;
   my($dev_stream) = $src_stream;
   my($sit_stream) = $src_stream;
   my($uat_stream) = $src_stream;
   $int_stream =~ s/$test_lvl/$INT/g;
   $dev_stream =~ s/$test_lvl/$DEV/g;
   $sit_stream =~ s/$test_lvl/$SIT/g;
   $uat_stream =~ s/$test_lvl/$UAT/g;
   my($stream_structure) = undef;
   println("$BFROOT/$int_stream");
   println("$BFROOT/$dev_stream");
   println("$BFROOT/$sit_stream");
   println("$BFROOT/$uat_stream");
   if((-d "$BFROOT/$int_stream") && (-d "$BFROOT/$dev_stream") && (-d "$BFROOT/$sit_stream") && (-d "$BFROOT/$uat_stream"))
   {
      println("structure = $STREAM_STRUCTURE_NORM");
      $stream_structure = $STREAM_STRUCTURE_NORM;
   }
   elsif((-d "$BFROOT/$int_stream") && (-d "$BFROOT/$uat_stream"))
   {
      println("structure = $STREAM_STRUCTURE_EMER");
      $stream_structure = $STREAM_STRUCTURE_EMER;
   }
   else
   {
      print("ERROR: Not all views exist\n");
      exit(1);
   }
   return($stream_structure);
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

sub get_latest_baselines
{
   my($streamdir) = @_;
	println("get_latest_baselines($streamdir)");
    $blstring = `cd $streamdir; $CLEARTOOL lsstream -fmt '%[latest_bls]Xp'`;
	#println("cd $streamdir; $CLEARTOOL lsstream -fmt '%[latest_bls]Xp'");
	my(@bl_list) = split(/\ /, $blstring);
	#println("----bl_list-----");
	#println(@bl_list);
	#println("--end bl_list--");
   return(@bl_list);
}


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
#   println("cd $streamdir; $CLEARTOOL lsstream -fmt \%[found_bls]CXp 2>&1; cd ..;");
#   $_ = `cd $streamdir; $CLEARTOOL lsstream -fmt \%[found_bls]CXp 2>&1; cd ..;`;
#   /^(baseline:)(.+)(@\/${PVOB})$/;
#   my($baseline_name) = $2;
#   println("END get_baseline_name($streamdir) => $baseline_name\n");
#   return($baseline_name);
#}

# Portal has components which are not readwrite. Thus, we must figure out what baseline to use 
# from the list of baselines returned. This method takes place of Build Forge storing baselines
# in each environment.
sub query_bls
{
   my($src_stream, $test_lvl) = @_;
   println("query_bls($src_stream, $test_lvl)");
   my($dev_stream) = $src_stream;
   my($sit_stream) = $src_stream;
   my($uat_stream) = $src_stream;
   my($bldev, $blsit, $bluat) = undef;
   #println("BFROOT = $BFROOT"); 
   #println("dev_stream = $dev_stream");
   $dev_stream =~ s/$test_lvl/$DEV/g;
   #println("dev_stream = $dev_stream");
   #println("$BFROOT/$dev_stream");
   #println(`ls -l $BFROOT/$dev_stream`);
   if(-d "$BFROOT/$dev_stream")
   {
      #println("---------DEV--------------");
      my($modcomp) = get_modifiable_component_list("$BFROOT/$dev_stream");
      #println("modcomp DEV => $modcomp");
      my(@bllist) = get_latest_baselines("$BFROOT/$dev_stream");
      #println("bllist DEV => ");
      #println(@bllist);
		#println("------------------");
      foreach $blname (@bllist)
      {  
     #    println("cd $BFROOT/$dev_stream; $CLEARTOOL describe -fmt %[component]p $blname");
         $thiscomp = `cd $BFROOT/$dev_stream; $CLEARTOOL describe -fmt %[component]p $blname`;
     #    println("thiscomp = $thiscomp vs modcomp = $modcomp");
         if($thiscomp eq $modcomp)
         {
            $bldev = $blname;
         }
      }
   }
   
   $sit_stream =~ s/$test_lvl/$SIT/g;
   if(-d "$BFROOT/$sit_stream")
   {
      #println("---------SIT--------------");
      my($modcomp) = get_modifiable_component_list("$BFROOT/$sit_stream");
      #println("modcomp SIT => $modcomp");
      my(@bllist) = get_latest_baselines("$BFROOT/$sit_stream");
      #println("bllist SIT => ");
      #println(@bllist);
      foreach $blname (@bllist)
      {  
         #println("cd $BFROOT/$sit_stream; $CLEARTOOL describe -fmt %[component]p $blname");
         $thiscomp = `cd $BFROOT/$sit_stream; $CLEARTOOL describe -fmt %[component]p $blname`;
         #println("thiscomp = $thiscomp vs modcomp = $modcomp");
         if($thiscomp eq $modcomp)
         {
            $blsit = $blname;
         }
      }
   }
   
   
   $uat_stream =~ s/$test_lvl/$UAT/g;
   if(-d "$BFROOT/$uat_stream")
   {
     #println("---------UAT--------------");
      my($modcomp) = get_modifiable_component_list("$BFROOT/$uat_stream");
      #println("modcomp UAT => $modcomp");
      my(@bllist) = get_latest_baselines("$BFROOT/$uat_stream");
      #println("bllist UAT => ");
      #println(@bllist);
      foreach $blname (@bllist)
      {  
         #println("cd $BFROOT/$uat_stream; $CLEARTOOL describe -fmt %[component]p $blname");
         $thiscomp = `cd $BFROOT/$uat_stream; $CLEARTOOL describe -fmt %[component]p $blname`;
         #println("thiscomp = $thiscomp vs modcomp = $modcomp");
         if($thiscomp eq $modcomp)
         {
            $bluat = $blname;
         }
      }
   }
   if($bldev && $blsit)
   {
      return($bldev, $blsit, $bluat);
   }
   else
   {
      return($bluat);
   }
}

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
   #println("cd $streamdir; $CLEARTOOL lsproject -cview -fmt %[mod_comps]Cxp");
   my($mod_comps) = `cd $streamdir; $CLEARTOOL lsproject -cview -fmt %[mod_comps]Cxp`;
   #println("mod_comps = $mod_comps");
   @modifiable_components = split(",",$mod_comps);
   chomp(@modifiable_components);
   my($size) = scalar(@modifiable_components);
   #println("$size modifiable components =>");
   #println($modifiable_components);
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
      println ("cd $BFROOT/$src_stream; $CLEARTOOL rebase -view $src_stream_view -baseline $rebase_bl_name > /dev/null 2>&1");
      #$rc = system("cd $BFROOT/$src_stream; $CLEARTOOL rebase -view $src_stream_view -baseline $rebase_bl_name\@/$PVOB > /dev/null 2>&1");
$rc = system("cd $BFROOT/$src_stream; $CLEARTOOL rebase -view $src_stream_view -baseline $rebase_bl_name > /dev/null 2>&1");
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
            'srcstream=s' => \$src_stream
          );

# Make sure required args are set.
if(! defined($src_stream) || ! defined($bf_tag))
{
   print("not defined\n");
   display_syntax();
   exit(1);
}

my($app_name, $rel_num, $rel_name, $test_level) = undef;

# Verify that src_stream is in the correct format
# projectName_ww.x.y.z_buildName
# Then pull important values from it.
if($src_stream =~ /^([a-zA-Z]+)_([\d|x][\d|x]\.[\d|x]\.[\d|x]\.[\d|x])_?(\w*)?_([a-zA-Z]+)$/)
{
   println("1 = $1\n2 = $2\n3 = $3\n4 = $4");
   if(defined($4))
   {
      ($app_name, $rel_num, $streampurpose, $test_level) = ($1, $2, $3, $4);
   }
   else
   {
      ($app_name, $rel_num, $test_level) = ($1, $2, $3);
   }
   
}
else
{
	printf("Source stream %s does not meet stream naming conventions!\n", $src_stream);
	exit 1;
}


# Verify the src_sream view exists on teh build box.
if(! -d "$bf_root$src_stream")
{
   printf("The view $bf_root$src_stream does not exist!\n\n");
   exit 1;
}


# If test level == LOCAL (The Dev stream), don't do anything. Exit.
if($test_level eq $LOCAL)
{
   printf("LOCAL - NO REBASE NEEDED\n\n");
   exit 1;
}

# Figure out stream structure
$stream_structure = get_stream_structure($test_level, $src_stream);
@streams = @{$STREAM_STRUCTURES{$stream_structure}};
println("-------STREAMS--------");
println(@streams);


# We have the current test level based on stream name. 
# Using current test level & @streams, determine prev_tst_lvl
$prev_tst_lvl = get_prev_tst_lvl($test_level, @streams);
println("prev_tst_lvl = $prev_tst_lvl");

# Set the previous source stream based on provided arg of this source stream.
$prev_src_stream = $src_stream;
$prev_src_stream =~ s/([a-zA-Z]+)_(\d\d\.\d\.\d\.\d)_([a-zA-Z]{3,5})/$1_$2_$prev_tst_lvl/;

# Define previous test level based on provided arg of this source stream.
# This method has been replaced with get_prev_tst_lvl above.
# $prev_tst_lvl = set_prev_tst_lvl($test_level, $src_stream);


# Get the baselines of all streams.
if($stream_structure eq $STREAM_STRUCTURE_EMER)
{
   ($bl_uat) = query_bls($src_stream, $test_level);
   println("bl_uat=$bl_uat");
}
else # $stream_structure == $STREAM_STRUCTURE_NORM
{
   ($bl_dev, $bl_sit, $bl_uat) = query_bls($src_stream, $test_level);   
   println("bl_dev=$bl_dev\nbl_sit=$bl_sit\nbl_uat=$bl_uat");   
}

# Make sure prev streams have been rebased before rebasing SIT or UAT
if($prev_tst_lvl ne $LOCAL)
{
   
   if(($test_level eq $SIT) && ($bl_sit =~ /^\$?[0-9]$/))
   {
      printf("DEV MUST BE REBASED BEFORE $SIT");
      exit 1;
   }
   if(($test_level eq $UAT) && ($bl_uat =~ /^\$?[0-9]$/))
   {
      printf("SIT MUST BE REBASED BEFORE $UAT");
      exit 1;
   }
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
println("test_level = $test_level");
if($test_level eq $DEV)
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
   println("test_level = $SIT");
   $rebase_bl_name = $bl_dev;
   println("rebase_bl_name = $rebase_bl_name");
   if($rebase_bl_name =~ /(baseline:)(.*)(@\/DC_Projects)/)
   {
	   $rebase_bl_name = $2;
	   println("rebase_bl_name = " . $rebase_bl_name);
   }
   if(defined($bl_sit))
   {
      $foundation_bl_name = $bl_sit;
   }
}
elsif($test_level eq $UAT)
{
   $rebase_bl_name = $bl_sit;
   if($rebase_bl_name =~ /(baseline:)(.*)(@\/DC_Projects)/)
   {
	   $rebase_bl_name = $2;
	   println("rebase_bl_name = " . $rebase_bl_name);
   }
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
if(($foundation_bl_name ne $rebase_bl_name) || ($src_stream eq $DEV))
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

