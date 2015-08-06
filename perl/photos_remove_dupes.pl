#!/usr/bin/perl
#
# Due to a script I ran when migrating files, I now have duplicates all over the place. Search for tehse files and remove
#
#
use File::stat;
use File::Copy;

if ($#ARGV != 0) {
    print "usage: remove_files_due_to_rename_prog.pl <DIR_NAME>\n";
    exit;
}

# DEFINE DIR BASED ON ARG
$dirname = $ARGV[0];
#chop($dirname);
println("dirname = $dirname");

$special_chars = ("'", "\"", "\(", "\)", ",");
# SEARCH THE DIR & SUBDIRS FOR FILES MATCHING THE NAMING WE DON'T WANT
&search_dir($dirname);


sub search_dir
{
	local($dir) = @_;
	local(@lines) = undef;
	local($line) = undef;
	local($file) = undef;
	local($subdir) = undef;
	
	println("SEARCHING $dir");
	# SEARCH THIS DIR
	$dir =~ s/'/\'/g;
	opendir(IMD, $dir) || die("Cannot open $dir");
	#@lines = `ls -l "$dir"`;
	@lines= readdir(IMD);
	#println("ls -l \"$dir\"");
	foreach $filename (@lines)
	{
		#println($filename);
		my($fullfilename) = $dir . "/" . $filename;
		#println("$fullfilename");
		if((-d $fullfilename) && ($filename !~ /^\./))
		{
			#recursive call
			my($subdir) = "$fullfilename";
			#println("a dir -> " . $subdir);
			&search_dir($subdir);
			#println("$filename => $orig_size IS DIR");
		}
		else
		{
			# SET TIMESTAMP ON ALL JPG FILES NO MATTER THE FILENAME
			if($filename =~ /\.(jpg|JPG)$/)
			{
				#println("jhead -ft \"$fullfilename\"");
				`jhead -ft "$fullfilename"`;
			}
			if (($filename =~ /^\d{4}-\d{2}-\d{2}_-_\d{2}\:\d{2}\:\d{2}\.(jpg|JPG)$/)
				|| ($filename =~ /^\d{4}-\d{2}-\d{2}_-_\d{2}\d{2}\d{2}\.(jpg|JPG)$/))
			{
				$orig_size = stat($fullfilename)->size;
				#`jhead -ft $fullfilename`;
				#println("jhead -n%Y-%m-%d_-_%H%M%S \"$fullfilename\"");
				`jhead -n%Y-%m-%d_-_%H%M%S "$fullfilename"`;
				#$cleaned_name = replace_special_chars($filename);
				$cleaned_name = $filename;
				$cleaned_name =~ s/\.jpg/a\.jpg/g;
				$cleaned_name =~ s/\.JPG/a\.JPG/g;
				#println($cleaned_name);
				$full_cleaned_name = $dir . "/" . $cleaned_name;
				if(-e "$full_cleaned_name")
				{
					$cleaned_size = stat($full_cleaned_name)->size;
					if($orig_size == $cleaned_size)
					{
						println("$orig_size => $fullfilename");
						println("$cleaned_size => $full_cleaned_name");
						#println("move cleaned_name => $cleaned_name");
						#move($full_cleaned_name,"/cifs/memories/photographs/temp/" . $cleaned_name);
						println("-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-");
					}
				}
			}
		}
	}
}

sub replace_special_chars
{
	my($filename) = @_;
	$filename =~ s/\'/_/g;
	$filename =~ s/\"/_/g;
	$filename =~ s/\(//g;
	$filename =~ s/\)//g;
	$filename =~ s/\&/_/g;
	return($filename);
}

sub println
{
	foreach(@_)
	{
		print $_ . "\n";
	}
}

