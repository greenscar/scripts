#!/usr/bin/perl
use Getopt::Long;
use File::Copy;
my $artist;
my $dir = "/mnt/music/sorted";
$result = GetOptions("artist=s" => \$artist);
$dir = $dir . "/" . $artist;
opendir(my $dh, $dir) or die "Can't open $dir: $!";

my @files = sort grep { -f "$dir/$_" } readdir($dh);
foreach(@files)
{
   if($_ =~ /$artist_-_(.*)?_-_(\d*)_-_(.*)\.mp3/)
   {
      $_ =~ /$artist_-_(.*)?_-_(\d*)_-_(.*)\.mp3/;
      my $album = $1;
      my $tracknum = $2;
      my $title = $3;
      #print $album . " - " . $tracknum . " - " . $title . "\n";
      #print ($_ . "\n");
      my $albumdir = $dir . "/" . $album;
      if(! -d $albumdir)
      {
         mkdir $albumdir;
      }
      my $new_file_name = $tracknum . "_-_" . $title . ".mp3";
      #print("mv $dir/$_ $albumdir/$new_file_name\n");
      move("$dir/$_", "$albumdir/$new_file_name");
      #chdir($albumdir);
      #my $cmd = "rename -n s/^" . $artist . "_-_" . $album . "_-_// *";
      #print(`$cmd` . "\n");
      #print($cmd . "\n");
      #chdir($dir);
   }
   else
   {
      print($_ . " DOES NOT MATCH\n");
   }
}

