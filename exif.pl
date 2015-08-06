#!/opt/perl/bin/perl
use lib "/usr/bin";
use Image::ExifTool qw(:Public);
use Getopt::Long;


my $exifTool = new Image::ExifTool;


# Parse command-line options and assign to appropriate variables
GetOptions('year=s' => \$year,
           'dir=s' =>\$dir);
# Make sure required args are set.
if(! defined($year) && ! defined($dir))
{
   print("Use one of the following:\n");
   print("ERROR: year is not defined\nFormat: perl exif.pl --year <year>\n");
   print("ERROR: dir is not defined\nFormat: perl exif.pl --dir <full path to dir>\n");
   exit(1);
}

$extlist = "JPG";
#$extlist = "avi";
if(defined($year) && ! defined($dir))
{
   $dir = "/Volumes/memories/photographs/" . $year;
}
println("dir = $dir");
@files = `ls -t '$dir' | awk {'print $1'}`;
foreach(@files)
{
   if($_ =~ /($extlist)$/)
   {
      $file = $dir . "/" . $_;
      #print("$file\n");
      my $info = $exifTool->ImageInfo("$file");
      my($filename_orig) = trim($$info{FileName});
      my($createdate) = trim($$info{DateTimeOriginal});
      #println("createdate -> $createdate");
      $createdate =~ /(\d{4})\:(\d{2})\:(\d{2})\s(\d{2})\:(\d{2})\:(\d{2})/;
      my($yr, $mon, $day, $hr, $min, $sec) = ($1, $2, $3, $4, $5, $6);
      my($filename_new) = "$yr-$mon-$day" . "_-_" . "$hr$min$sec.jpg";
      if(-e "$dir/$filename_new")
      {
         for($i=1;(-e "$dir/$filename_new"); $i++)
         {
            $filename_new = "$yr-$mon-$day" . "_-_" . "$hr$min$sec" . $i . ".jpg";
         }
      #   println("NEW -> $filename_new");
      }
      #println("orig = $dir/$filename_orig");
      #println("new = $dir/$filename_new");
      #println(`ls -l $dir/$filename_new`);
      if((!-e "$dir/$filename_new") && ($filename_new =~ /\d{4}-\d{2}-\d{2}_-_\d+\.jpg/))
      {
         println("rename $dir$filename_orig $dir$filename_new");
         rename("$dir$filename_orig", "$dir$filename_new") or die("Error renaming $dir/$filename_orig");
      }
   }
}



exit(1);
foreach (keys %$info) {
    print "$_ => $$info{$_}\n";
}

println("-----------------------------");

# Get list of tags in the order they were found in the file
#@tagList = $exifTool->GetFoundTags($file);

#println(@tagList);

# The following 3 functions pulled from http://www.somacon.com/p114.php
# Perl trim function to remove whitespace from the start and end of the string
sub trim($)
{
   my $string = shift;
	$string =~ s/^\s+//;
	$string =~ s/\s+$//;
	return $string;
}
sub println {
	my (@msg) = @_;
	foreach (@msg) {
      print("$_\n");
	}
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
