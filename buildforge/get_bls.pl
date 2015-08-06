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
$DEBUG = 0;
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
   println("   --srcstream <STREAM_NAME>     Name of stream you wish to build from");
}


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
      #my($modcomp) = get_modifiable_component_list("$BFROOT/$dev_stream");
      #println("modcomp DEV => $modcomp");
      my(@bllist) = get_latest_baselines("$BFROOT/$dev_stream");
      #println("bllist DEV => ");
      #println(@bllist);
		#println("------------------");
      foreach $blname (@bllist)
      {  
         println("cleartool lsbl -fmt %[depends_on]p $blname");
         @dependencies = `cd $BFROOT/$dev_stream; cleartool lsbl -fmt %[depends_on]p $blname`;
         if(@dependencies > 0)
         {
            println("set bldev = " . $blname);
            $bldev = $blname;
         }
      }
   }
   $sit_stream =~ s/$test_lvl/$SIT/g;
   if(-d "$BFROOT/$sit_stream")
   {
      my(@bllist) = get_latest_baselines("$BFROOT/$sit_stream");
      foreach $blname (@bllist)
      {  
         println("cleartool lsbl -fmt %[depends_on]p $blname");
         @dependencies = `cd $BFROOT/$sit_stream; cleartool lsbl -fmt %[depends_on]p $blname`;
         if(@dependencies > 0)
         {
            println("set blsit = " . $blname);
            $blsit = $blname;
         }
      }
   }
   
   
   $uat_stream =~ s/$test_lvl/$UAT/g;
   if(-d "$BFROOT/$uat_stream")
   {
      my(@bllist) = get_latest_baselines("$BFROOT/$uat_stream");
      foreach $blname (@bllist)
      {  
         println("cleartool lsbl -fmt %[depends_on]p $blname");
         @dependencies = `cd $BFROOT/$uat_stream; cleartool lsbl -fmt %[depends_on]p $blname`;
         if(@dependencies > 0)
         {
            println("set bluat = " . $blname);
            $bluat = $blname;
         }
      }
   }
   if($bldev)
   {
      return($bldev, $blsit, $bluat);
   }
   else
   {
      return($bluat);
   }
}

#########################################################################################################
#########################################################################################################
############################################# END OF METHODS ############################################
######################################## BEGIN ACTUAL PROCESSING. #######################################
#########################################################################################################
#########################################################################################################
show_all_env_vars;

# Parse command-line options and assign to appropriate variables
GetOptions( 'srcstream=s' => \$src_stream);

# Make sure required args are set.
if(! defined($src_stream))
{
   print("src_stream not defined\n");
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

# Figure out stream structure
$stream_structure = get_stream_structure($test_level, $src_stream);
@streams = @{$STREAM_STRUCTURES{$stream_structure}};
println("-------STREAMS--------");
println(@streams);

# Get the baselines of all streams.
if($stream_structure eq $STREAM_STRUCTURE_EMER)
{
   ($bl_uat) = query_bls($src_stream, $test_level);
   print("bl_uat=$bl_uat\n");
}
else # $stream_structure == $STREAM_STRUCTURE_NORM
{
   ($bl_dev, $bl_sit, $bl_uat) = query_bls($src_stream, $test_level);   
   print("bl_dev=$bl_dev\nbl_sit=$bl_sit\nbl_uat=$bl_uat\n");   
}

