use Getopt::Long;
use Time::localtime;
use LWP;
require HTTP::Request;

################################################################################
# Read in cmd line args
################################################################################
# Parse command-line options and assign to appropriate variables
GetOptions('app=s' => \$app,
           'env=s' => \$env,
           'baseline=s' => \$baseline,
           'bfjobid=s' => \$bfjobid
          );

# Make sure required args are set.
if(! defined($app) || !defined($env) || !defined($baseline) || !defined($bfjobid))
{
   print("ERROR: Required Args:\n");
   print("deploycreate.pl --app <APP> --env <ENV> --baseline <BL> --bfjobid <BFID>\n");
   exit(1);
}

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
      
      
      
      
      
