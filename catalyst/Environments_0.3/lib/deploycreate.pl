use lib '/home/jsandlin/catalyst/Environments/lib';
#use Environments::Schema;
use Getopt::Long;
use Time::localtime;
use LWP;
require HTTP::Request;

#use strict;
#use warnings;

################################################################################
# Read in cmd line args
################################################################################
# Parse command-line options and assign to appropriate variables

my($bfjobid);
my($app);
my($env);
my($baseline);


# Get vars from ENV as defined via BF
$bfjobid = $ENV{"BF_BID"} if(defined($ENV{"BF_BID"}));
$app = $ENV{"APP"} if(defined($ENV{"APP"}));
$env = $ENV{"ENV_NAME"} if(defined($ENV{"ENV_NAME"}));
$baseline = $ENV{"newbl"} if(defined($ENV{"newbl"}));

# Get vars from args in case user wishes to provide.
GetOptions('app=s' => \$app,
           'env=s' => \$env,
           'baseline=s' => \$baseline,
           'bfjobid=s' => \$bfjobid
          );

# For TIERS, a baseline will always be provided.
# However, in PORTAL, sometimes the autobl steps will be skipped.
# When this happens, we need to look @ the stream to get the current
#  baseline.
if(! defined($baseline))
{
   # Load baseline from CC
   $source_stream = $ENV{"VIEW_NAME"} if(defined($ENV{"VIEW_NAME"}));
   $base_dir = $ENV{"BF_ROOT"} if(defined($ENV{"BF_ROOT"}));
   
   # If a source stream is not defined, we cannot load a baseline. Thus, error out.   
   if(! defined($source_stream))
   {
      print("ERROR: You must provide either the baseline or the BF_ROOT + VIEW_NAME\n");
      exit(1);
   }
   $baseline = load_current_baseline($base_dir, $source_stream);
   print("current baseline = $baseline\n");
}

# Make sure required args are set.
if(! defined($app) || !defined($env) || !defined($baseline) || !defined($bfjobid))
{
   print("ERROR: Missing some required arguments:\n");
   print("       --app\n");
   print("       --env\n");
   print("       --baseline\n");
   print("       --bfjobid\n");
   print("ERROR: These errors can be ENV vars or provided as args\n");
   print("To provide as arg:\n");
   print("deploycreate.pl --app <APP> --env <ENV> --baseline <BL> --bfjobid <BFID>\n");
   exit(1);
}

# Make sure the bf job id is digits.
if($bfjobid !~ /\d*/)
{
   print("ERROR: bfjobid must be digits only\n");
   exit(1);
}


my($address) = "http://iedaau020.txaccess.net:6666/dm/deploy_create/$app/$env/$baseline/$bfjobid";
my($request) = HTTP::Request->new(GET => $address);
my($ua) = LWP::UserAgent->new;
my($response) = $ua->request($request);
print($response);
exit(0);
      


sub load_current_baseline
{  
   my($base_dir, $view_name) = @_;
   my($fullpath) = $base_dir . "/" . $view_name;
   my($blstring) = "cd $fullpath; cleartool lsstream -fmt '%[latest_bls]Xp'";
   print($blstring . "\n");
   my($blstring) = `$blstring`;
   my(@bllist) = split(/\ /, $blstring);
   foreach $blname (@bllist)
   {  
      #$helper->println("cleartool lsbl -fmt %[depends_on]p $blname");
      @dependencies = `cd $fullpath; cleartool lsbl -fmt %[depends_on]p $blname`;
      if(@dependencies > 0)
      {
         print("blname => " . $blname . "\n");
         my($regex) = "(baseline:)(.*)\@\/(.*)";
         $blname =~ /$regex/;
         print("2 = $2\n");
         $bl = $2;
         return($bl);
      }
   }
}

