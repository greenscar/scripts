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
	
	#println("SEARCHING $dir");
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
		elsif ($filename =~ /(__|\,|'|\"|\(|\)|\&)+/)
		{
			$orig_size = stat($fullfilename)->size;
			$cleaned_name = replace_special_chars($filename);
			#println($dir);
			$full_cleaned_name = $dir . "/" . $cleaned_name;
			if($filename =~ /Hunger/)
			{
				println($filename);
				println("cleaned => " . $full_cleaned_name);
			}
			if(-e "$full_cleaned_name")
			{
				$cleaned_size = stat($full_cleaned_name)->size;
				if($orig_size == $cleaned_size)
				{
					#println("$orig_size => $fullfilename");
					#println("$cleaned_size => $full_cleaned_name");
					println("cleaned_name => $cleaned_name");
					move($full_cleaned_name,"/cifs/server/music/temp/" . $cleaned_name);
					println("-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-");
				}
			}
		}
	}
}
	
sub split_file_info
{
	my(@fileinfo) = @_;
	println("split_file_info(@fileinfo)");
	my(@parts) = split;
	#println(@parts);
	my($numparts) = $#parts + 1;
	my($permissions) = @parts[0];
	my($directories) = @parts[1];
	my($owner) = @parts[2];
	my($group) = @parts[3];
	my($size) = @parts[4];
	my($date) = @parts[5];
	my($time) = @parts[6];
	my($filename) = "";
	#println("filename =  $filename");
	#println("part7 = ". @parts[7]);
	for($al=7;$al<$numparts;$al++)
	{
		$filename .= @parts[$al];
		if($numparts > $al+1)
		{
			$filename .= "\ ";
		}
	}
	#println("returning $filename");
	#undef(@parts);
	#undef(@fileinfo);
	#undef(@_);
	return($numparts,$permissions,$directories,$owner,$group,$size,$date,$time,$filename);
}

sub replace_special_chars
{
	my($filename) = @_;
	$filename =~ s/\,/_/g;
	$filename =~ s/\'/_/g;
	$filename =~ s/\"/_/g;
	$filename =~ s/_\(/_/g;
	$filename =~ s/\)/_/g;
	$filename =~ s/\&/_/g;
	$filename =~ s/__/_/g;
	return($filename);
}

sub println
{
	foreach(@_)
	{
		print $_ . "\n";
	}
}

