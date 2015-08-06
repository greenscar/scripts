#!/usr/bin/perl

my $send_to = "To: james\@h8n.com\n";
my $reply_to = "Reply-to: james\@h8n.com\n"; 
my $from = "From: james\@h8n.com\n";
my $subject = "Subject: This is the subject\n"; 
my $content = "<h1>This is the body</h1>"; 


unless(open (MAIL, "|/usr/sbin/sendmail james\@h8n.com")) {
	print "error.\n";
	warn "Error starting sendmail: $!";
}
else{
	print MAIL $from;
	print MAIL $reply_to;
	print MAIL $subject;
	print MAIL $send_to;
	print MAIL "Content-type: text/plain\n\n";
	print MAIL $content;
	close(MAIL) || warn "Error closing mail: $!";
	print "Mail sent\n";
}
