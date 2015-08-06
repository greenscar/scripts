use strict;
use warnings;
use Win32::Service;
use Win32::NetAdmin;
use Getopt::Long;

# Define vars we will use during this script
my($domain, %services, %status, $pdc, @servers, @DC, @feedsservers, $action);

# Get arg provided & ensure it was defined correctly.
GetOptions('action=s' => \$action);
if(!defined($action) || ($action !~ /^(start|stop|bounce|status)$/))
{
    die("Syntax: perl services.pl --action (start|stop|bounce|status)\n");
}
# Get the domain name
unless ($domain = Win32::DomainName){die "Unable to obtain the domain name";}
# Get the domain controller
unless (Win32::NetAdmin::GetDomainController("", $domain, $pdc)){die "Unable to obtain the PDC name for $domain.";}
# Get a list of all servers on the domain from the domain controller
unless (Win32::NetAdmin::GetServers($pdc, $domain, 0x00000008, \@DC)) {&logit("Unable to read NetBios 0008.");}
unless (Win32::NetAdmin::GetServers($pdc, $domain, 0x00008000, \@servers)) {&logit("Unable to read NetBios 8000.");}
#
# Currently, this script is made to do all boxes with FEED in the name, thus the regexp below.
# If you want to do a differnt set of boxes, change the regexp below.
# Go through the list of servers & create a list of those with FEED in the name
#
for (@servers) 
{
    if(/FEED/)
    {
       push(@feedsservers, $_);
    }
}

my($servicetostop) = "FeedsService";

# Go through this list of FEED servers & perform the action provided as the arg.
foreach (@feedsservers)
{
   # Get the current status of FeedsService on this box.
   my($status) = get_status("$_", $servicetostop);
   println("$_ FeedsService => $status");
   if($action =~ /start/)
   {
       if($status =~ /^STOPPED$/)
       {
           Win32::Service::StartService($_, "$servicetostop");
           println("$_ newstatus -> " . get_status("$_", "$servicetostop"));
       }
       else
       {
           println("ERROR STARTING $_ -> STATUS IS $status");
       }
   }
   elsif($action =~ /stop/)
   {
       if($status =~ /^RUNNING$/)
       {
           Win32::Service::StopService($_, "$servicetostop");
           println("$_ newstatus -> " . get_status("$_", "$servicetostop"));
       }
       else
       {
           println("ERROR STOPPING $_ -> STATUS IS $status");
       }
       
   }
   elsif($action =~ /bounce/)
   {
       if($status =~ /^RUNNING$/)
       {
           Win32::Service::StopService($_, "$servicetostop");
           println("$_ stopped newstatus -> " . get_status("$_", "$servicetostop"));
           Win32::Service::StartService($_, "$servicetostop");
           println("$_ started newstatus -> " . get_status("$_", "$servicetostop"));
       }
       else
       {
           println("ERROR BOUNCING $_ -> STATUS IS $status");
       }
       
   }
   elsif($action =~ /status/)
   {
       #my($status) = get_status("$_", "FeedsService", \%status);
       println("status => $status");
   }
   println("----------------------------------");
}
sleep(10);
println("----------------- STATUS ------------------");
foreach (@feedsservers)
{
   # Get the current status of FeedsService on this box.
   my($status) = get_status("$_", "$servicetostop");
   println(" $_ -> $status");
}
println("--------------- END STATUS ----------------");


sub get_status
{
    my($box, $service) = @_;
    my(%statsref);
    Win32::Service::GetStatus("$box", $service, \%statsref);
    foreach my $k (keys(%statsref))
    {
        if($k =~ /CurrentState/)
        {
            if($statsref{$k} == 1){ return("STOPPED"); }
            elsif($statsref{$k} == 2){ return("STARTING"); }
            elsif($statsref{$k} == 3){ return("STOPPING"); }
            elsif($statsref{$k} == 4){ return("RUNNING"); }
            elsif($statsref{$k} == 5){ return("CONTINUE PENDING"); }
            elsif($statsref{$k} == 6){ return("PAUSE PENDING"); }
            elsif($statsref{$k} == 7){ return("PAUSED"); }
            else { return($statsref{$k}); }
        }
    }
    #display_hashtable(\%statsref);
    
}
# Display the hastable with its keys and values
sub display_hashtable
{
   my(%ht) = %{$_[0]};
   my($hasVars) = 0;
   foreach my $k ( keys(%ht) ) {
      print "$k : $ht{$k}\n";
   }
}  

sub println {
	my (@msg) = @_;
	foreach (@msg) {
	  print($_ . "\n")
	}
}

