#!/usr/bin/perl
$IP_FILE = "/home/c8h10n4o2/scripts/ipemail.result";
my $newip = `wget www.whatismyip.com/automation/n09230945.asp -O - -q`;
my $oldip = getoldip();
my $send_to = "To: james\@h8n.com\n";
my $reply_to = "Reply-to: james\@h8n.com\n"; 
my $from = "From: james\@h8n.com\n";
my $subject = "Subject: IP Address: $newip\n"; 
my $content = "<h1>$newip</h1>"; 
if($newip ne $oldip)
{
   writenewip($newip);
   unless(open (MAIL, "|/usr/sbin/sendmail james\@h8n.com")) 
   {
      print "error.\n";
      warn "Error starting sendmail: $!";
   }
   else{
      print MAIL $from;
      print MAIL $reply_to;
      print MAIL $subject;
      print MAIL $send_to;
      print MAIL "Content-type: text/html\n\n";
      print MAIL $content;
      close(MAIL) || warn "Error closing mail: $!";
      print "Mail sent\n";
   }
}

sub writenewip
{
   my($newip) = @_;
   print ("new IP = $newip\n");
   open IPFILE, ">$IP_FILE";
   print IPFILE "$newip\n";
   close IPFILE;
}

sub getoldip
{
   open IPFILE, "$IP_FILE";
   my($line) = 0;
   while ( <IPFILE> )
   {
      chomp;
      $line = $_;
   }
   close IPFILE;
   return($line);
}

