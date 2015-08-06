#!/usr/bin/perl
my($file) = "http://";
my($DLFILE);

open(my $DLFILE, '<', "http://www.maxmind.com/app/download_new?edition_id=139&date=20091101&suffix=zip&license_key=NVdV0VBRL2XY") or die "ERROR";

binmode($DLFILE);
print while <$DLFILE>;
undef($DLFILE);

