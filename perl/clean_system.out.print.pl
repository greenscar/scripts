#!ccperl

# Script:   system_out_clean.pl
# Version:  1.0
# Author:   James Sandlin <james.sandlin@hhsc.tx.state.us>
# Date:     2008-08-01
# Purpose:  Search the provided directory for any java or jsp file containing
#           System.out.print* & replace it with null or calls to the actual 
#           logging framework.

# Syntax:
#   ccperl system_out_clean.pl ARGS
#   Required Arguments:
#     --stream  n Name of stream to clean

# Specify Perl modules to include here
use Time::localtime;
use Getopt::Long;

# Initialize some constants
# Constants are all uppercase
$VERSION       = "1.0";
$VERSION_DATE  = "2008-08-01";
$AUTHOR        = "James Sandlin <james.sandlin\@hhsc.tx.state.us>";
$LOGGER_START  = "us.tx.state.hhsc.ie.architecture.logger.LoggerHelper.log(us.tx.state.hhsc.ie.architecture.logger.LoggerHelper.LEVEL_DEBUG, String.valueOf(";
$LOGGER_END    = "));";
$HOME_DIR      = "/home/wasadmin";
#$FILE_TYPE_REG_EXP   =  "(\.java$|\.jsp$)";

# Display program syntax
sub displaySyntax {
	printf("Syntax:  ccperl system_out_clean.pl ARGS\n\n");
	printf("Required Arguments:\n");
	printf("    --stream  n   Name of stream to clean\n");
    printf("    --action  n   Action you would like taken (search | replace)\n");
    printf("                  search  -> search for System.out.print & report\n");
    printf("                  replace -> search for System.out.print & replace\n");
}



sub find_system_outs
{
   my($dir,$badfilelist) = @_;
	$dir =~ s/'/\'/g;
   opendir(IMD, $dir) || die("Cannot open $dir");
	local(@lines)= readdir(IMD);
	foreach $filename (@lines)
	{
		my($fullfilename) = $dir . "/" . $filename;
      # If it is a directory, do a recursive call
      if((-d $fullfilename) && ($filename !~ /^\./))
		{
			#recursive call
			find_system_outs($fullfilename, $badfilelist);
		}
		elsif ($filename =~ /(\.java$|\.jsp$)/)
		{
         if(`grep "System.out.print" $fullfilename`)
         {
            push(@{$badfilelist},$fullfilename);
         }
		}
	}
   return(\@badfilelist);
}

sub parse_file {
    my ($fname) = @_;
    my @cd = split(/\./, $fname);
	
	if ($cd[@cd - 1] eq "html" || $cd[@cd - 1] eq "htm" || $cd[@cd - 1] eq "shtml")  {
	   @new_file = "";

       open(FILE, "<$fname") || die "Can't open the file $fname\n";
       while(<FILE>)  {
          $_=~s/$str1/$str2/;
          push @new_file, $_;
       }
       close(FILE);
       open(FILE,">$fname") || die "Can't open the file $fname\n";
       for $i(0..$#new_file){
          print FILE $new_file[$i];
       }
       close(FILE);
	}    
}
sub replace_system_outs
{
   my(@filelist) = @_;
   foreach($filename (@filelist))
   {
      local(@newfile) = "";
      `cp $filename $filename.bak`;
      open(FILE, "<$filename") || die "Can't open $filename for reading\n";
      while(<FILE>)
      {
          # System\s*\.\s*out\s*\.\s*print.*\(\s*\)\s*;
          $_=~/System\.out\.print\w*\(\s*\)\s*;//;
          # System\s*\.\s*out\s*\.\s*print[^(]*\(\s*(.*?)\s*\)\s*;
          
          #$_=~//System\.out\.print\w*\(\s*\S*\s*\)\s*;//WHAT_DO_I_WANT/;
          # BECAUSE I ALREADY REPLACED EMPTY PRINTS, I CAN ASSUME THE REST ARE MENT TO GO INTO THE LOGS.
          
          if(/System\.out\.print\w+\s*\(\s*\S*\s*\)\s*;/)
          {
              println("old line => $_");
              $_ =~ /System\.out\.print\w*/\us\.tx\.state\.hhsc\.ie\.architecture\.logger\.LoggerHelper\.log\(us\.tx\.state\.hhsc\.ie\.architecture\.logger\.LoggerHelper\.LEVEL_DEBUG,\ String\.valueOf\(/;
              $_ =~ /\);/\)\);/;
              println("new line => $_");
          }
          push(@newfile,$_);
      }
      close(FILE);
      open(FILE), ">$filename") || die "Can't open $filename for writing\n";
      for $i(0..$#newfile)
      {
          print(FILE, $newfile[$i];
      }
      close(FILE);
      println("$filename DONE");
   }
}
            # $fullfilename has System.out.println in it. Clean this up.
            # Open the file
            #open(FILE, $fullfilename) or die "Could not open $fullfilename\n";
            #while(<FILE>)
            #{
               #System.out.print*() -> ""
               #System.out.print*(<anything>) -> us.tx.state.hhsc.ie.architecture.logger.LoggerHelper.log(us.tx.state.hhsc.ie.architecture.logger.LoggerHelper.LEVEL_DEBUG, String.valueOf(<anything>));

               #perl -pi -e 's/old_string/new_string/g' $fullfilename


               # Clean up worthless println
               #\s*\(\s*\n*\s*\)
               #if(/System\.out\.print\w+\s*\(\s*\n\s*\)\s*;/)
               #{
               #   #DO SOMETHING
               #   println($fullfilename . " has blank System.out.print");
               #   s/
               #}
               #elsif(/System\.out\.print\w+\s*\(\s*\S*\s*\)\s*;/)
               #{
                  #DO SOMETHING
               #   println($fullfilename . " has used System.out.print*(_______);");
               #}
               
               # Replace true prints with logging
               
            #}
            #close(FILE);
}

# Return the number of elements in the provided array
sub get_array_size
{
   @intArray = @_;
   $lastIndex = $#intArray; # index of the last element, $lastIndex == 2
   $length = $lastIndex + 1; # size of the array
   return($length);
}
# Print the arg provided along with a new line char.
sub println {
   my (@msg) = @_;
	foreach (@msg) {
      print($_ . "\n");
	}
}



# Parse command-line options and assign to appropriate variables
GetOptions('stream=s' => \$stream,
           'action=s' => \$action);

# Verify that minimum mandatory arguments are present
if ((! defined $stream) || ($action {
	displaySyntax();
	printf("--stream argument is mandatory\n");
    printf("--action argument is mandatory\n");
	exit 1;
}

# Define the dir to search
$source_dir = $HOME_DIR . "/" . $stream;

# Search the dir for all files of the defined type
@badfilelist = ();
&find_system_outs($source_dir, \@badfilelist);

# If action = replace, replace all System.out.prints appropriately
if($action =~ /^replace$/)
{
    replace_system_outs(@badfilelist);    
}




println("size = " . get_array_size(@badfilelist));
println("------------------------BAD FILE LIST-----------------------------");
println(@badfilelist);



#<!-- =========================================================== -->
#<!-- replace System.out statements for improved performance      -->
#<!-- =========================================================== -->
#<target name="replace-system-outs">	
#	<if>
#		<!-- don't replace System.outs unless the replace.system.outs property says to -->
#		<equals arg1="${replace.system.outs}" arg2="true" />
#		<then>
#			<echo message="Replacing System.out calls in all .java files with " />
#			<echo message="calls to the logging framework.  Note that running this " />
#			<echo message="command in an actual ClearCase view will most likely " />
#			<echo message="result in files being hijacked." />		
#			<!-- check for "System.out.print*();" - replace with empty string 
#				as these lines don't add much value -->
#			<replaceregexp match="System\s*\.\s*out\s*\.\s*print.*\(\s*\)\s*;"
#				       replace=""
#				       flags="g">
#				<fileset dir="${view.dir}">
#					<include name="**/*.java" />
#					<exclude name="**/test/**/*.java" />
#				</fileset>
#			</replaceregexp>
#			<!-- check for "System.out.print*(<text>);" - replace with call to logging framework -->
#			<replaceregexp match="System\s*\.\s*out\s*\.\s*print[^(]*\(\s*(.*?)\s*\)\s*;"
#				       replace="us.tx.state.hhsc.ie.architecture.logger.LoggerHelper.log(us.tx.state.hhsc.ie.architecture.logger.LoggerHelper.LEVEL_DEBUG, String.valueOf(\1));"
#				       flags="gs">
#				<fileset dir="${view.dir}">
#					<include name="**/*.java" />
#					<exclude name="**/test/**/*.java" />
#				</fileset>
#			</replaceregexp>
#		</then>
#		<else>
#			<echo message="Will not replace System.out calls since " />
#			<echo message="the replace.system.out property is not true." />
#		</else>
#	</if>
#</target>
