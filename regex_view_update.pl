#Updated:                 snbx_batch/Tst_fldr_1/test-r1-a.txt /main/Test_snbx_2_Mainline_Int/SNBX_89.0.0.0_Tst/6 /main/Test_snbx_2_Mainline_Int/SNBX_89.0.0.0_Tst/1
#New:                     snbx_bo/another1/text123 /main/Test_prj1_85.0_Tst/SNBX_89.0.0.0_Tst/2
#UnloadDeleted:           snbx_bo/another1/text123.txt
#if($line =~ /(Updating|Loading|Making\sdir|Unloaded)\s\"(.*)\"/)
#$line = "Updated:                 snbx_batch/Tst_fldr_1/test-r1-a.txt /main/Test_snbx_2_Mainline_Int/SNBX_89.0.0.0_Tst/6 /main/Test_snbx_2_Mainline_Int/SNBX_89.0.0.0_Tst/1";
#$line = "New:                     snbx_bo/another1/text123 /main/Test_prj1_85.0_Tst/SNBX_89.0.0.0_Tst/2";
#$line = "UnloadDeleted:           snbx_bo/another1/text123.txt";
#if($line =~ /^(Updated|New|UnloadDeleted):\s+([\w*\/?\.?]+)/

#<<	The item is present only in baseline-selector1 or stream-selector1.
#>>	The item is present only in baseline-selector2 or stream-selector2.
#<-	The activity is present in both of the items being compared and is newer in baseline-selector1 or stream-selector1.
#->	The activity is present in both of the items being compared and is newer in baseline-selector2 or stream-selector2.
#none	There are no differences between the objects being compared.

$in_view_update = 0;
$current_act_num = 0;
open(DATA, "view_update.log") or die("Couldn't open file");

foreach $line(<DATA>)
{
      #println("$in_view_update line = $line");
      if($line =~ /Actions\staken\sto\supdate\sthe\sview\:/)
      {
         $in_view_update = 1;
      }
      elsif($line =~ /^#$/)
      {
         ($in_view_update == 1) ? ($in_view_update++) : ($in_view_update = 0);
         println("###### line = $line");
         println("in_view_update = $in_view_update");
      }
      if($in_view_update == 2)
      {
         printf($line);
         #if($line =~ /^(Updated|New|UnloadDeleted):\s+(\S+)(\s(\S+)\/(\d))?(\s(\S+))?/)
         if($line =~ /^(\w*):\s+(\S+)(\s(\S+)\/(\d))?(\s(\S+))?/)
         {
            #my($ua) = new UpdateAction->new();
            $action = $1;
            $filename = $2;
            $version = $3;
            #$self->add_updateaction($ua);
            #println("action = " . $action);
            #println("filename = " . $filename);
            #println("version = " . $version);
            printf("$action - $filename - $version\n");
         }
         
      }  
}

close(DATA);


sub println
{
   my(@msg) = @_;
   foreach (@msg) {
      print("$yr$mon$day_$hr$min$sec $_ \n");
	}
}
