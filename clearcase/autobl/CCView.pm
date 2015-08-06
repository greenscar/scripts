#!/opt/rational/clearquest/bin/cqperl

package CCView;
use Helper;
use BFJob;
use Baseline;
sub new
{
   my $self = {};
   my $owner = undef;
   my $view_name = undef;
   my $app_name = undef;
   my $rel_num = undef;
   my $rel_name = undef;
   my $test_level = undef;
   my $base_dir = undef;
   my $current_bl = undef;
   my $prev_bl = undef;
   #my $current_bl_full_name = undef;
   my $rebase_bl = undef;
   #my $rebase_bl_full_name = undef;
   my $view_tag = undef;
   my $pvob = undef;
   bless $self;
   return $self;
}


##########################################################################
# END OBJECT VAR METHODS
##########################################################################
# Check the bls & return the diff count
# This is going to be used to ensure we don't rebase if there are no code differences. 
# The only place this needs to be used is going from TST to DEV (NORMAL structure) 
#     or TST to UAT (EMER structure).

sub owner
{    
   my ( $self, $owner ) = @_;                         
   $self->{owner} = $owner if defined($owner);
   return $self->{owner};
}

sub pvob
{    
   my ( $self, $pvob ) = @_;                         
   $self->{pvob} = $pvob if defined($pvob);
   return $self->{pvob};
}

sub view_name
{    
   my ( $self, $view_name ) = @_;                         
   $self->{view_name} = $view_name if defined($view_name);
   return $self->{view_name};
}

sub view_tag
{
   my($self) = @_;
   return $self->owner() . "_" . $self->view_name();
}

sub app_name
{    
   my ( $self, $app_name ) = @_;                         
   $self->{app_name} = $app_name if defined($app_name);
   return $self->{app_name};
}

sub rel_num
{    
   my ( $self, $rel_num ) = @_;                         
   $self->{rel_num} = $rel_num if defined($rel_num);
   return $self->{rel_num};
}


sub rel_name
{    
   my ( $self, $rel_name ) = @_;                         
   $self->{rel_name} = $rel_name if defined($rel_name);
   return $self->{rel_name};
}

sub get_rel_num_short
{
   my($self) = @_;
   my($short_rel_num) = $self->rel_num();
   $short_rel_num =~ s/\.//g;
   return($short_rel_num);
}

sub test_level
{    
   my ( $self, $test_level ) = @_;                         
   $self->{test_level} = $test_level if defined($test_level);
   return $self->{test_level};
}

sub base_dir
{    
   my ( $self, $base_dir ) = @_;                         
   $self->{base_dir} = $base_dir if defined($base_dir);
   return $self->{base_dir};
}

sub prev_bl
{    
   my ( $self, $prev_bl ) = @_;   
   $self->{prev_bl} = $prev_bl if defined($prev_bl);
   return $self->{prev_bl};
}
sub current_bl
{    
   my ( $self, $current_bl ) = @_;   
   $self->{current_bl} = $current_bl if defined($current_bl);
   return $self->{current_bl};
}

sub rebase_bl
{    
   my ( $self, $rebase_bl ) = @_;  
   $self->{rebase_bl} = $rebase_bl if defined($rebase_bl);
   return $self->{rebase_bl};
}



# Check the bls & return the diff count
# This is going to be used to ensure we don't rebase if there are no code differences. 
# The only place this needs to be used is going from TST to DEV (NORMAL structure) 
#     or TST to UAT (EMER structure).
sub diff_bl_load
{
   my($self, $helper) = @_;
   $helper->println("CCView->diff_bl_load($self, $helper)");
   my($fullpath) = $self->base_dir . "/" . $self->view_name();
   my($diffstring) = "cd $fullpath; cleartool diffbl " . $self->current_bl()->id_full() . " " . $self->rebase_bl()->id_full();
   $helper->println($diffstring);
   @diffbl = `$diffstring`;
   my($diffcount) = 0;
   if(scalar @diffbl)
   {
      foreach $act (@diffbl)
      {
         if($act =~ /^\>\>\s(\w+)@\/(\w+)\s(.+)$/)
         {
            my($a) = new Activity->new();
            $a->id($1);
            $a->project($2);
            $a->headline($3);
            $a->load_details_from_cq($helper);
            $helper->println("Adding " . $a->id() . " to rebase " . $self->rebase_bl()->id());
            $self->rebase_bl()->add_activity($a);
         }
      }
   }
   $helper->println("------diff_bl_load end---------");
   $diffcount = $self->rebase_bl()->get_num_activities();
   $helper->println("diff_bl_load($oldbl, $newbl) returning $diffcount");
   return($diffcount);
}


# Verify that src_stream is in the correct format
# projectName_ww.x.y.z_buildName
# Then pull important values from it.
sub parse_view_name
{
   my($self, $helper) = @_;
   if($self->view_name() =~ /^([a-zA-Z0-9]+)_([\d|x][\d|x]\.[\d|x]\.[\d|x]\.[\d|x])_?(\w*)?_([a-zA-Z0-9]+)$/)
   {
      $helper->println("Stream Name Tell Us:");
      $helper->println("CCView->app_name = $1\nCCView->rel_num = $2\nCCView->rel_name = $3\nCCView->test_level = $4");
      if(defined($4))
      {
         $self->app_name($1);
         $self->rel_num($2);
         $self->rel_name($3);
         $self->test_level($4);
      }
      else
      {
         $self->app_name($1);
         $self->rel_num($2);
         $self->test_level($3);
      }
      $helper->println("2 => $2");
      $helper->println($self->rel_num());
      return(1);
   }
   else
   {
      return(0);
   }
}

sub set_all_props
{
   my($self, $helper, $ccview, $test_lvl) = @_;
   $self->owner($ccview->owner());
   $self->app_name($ccview->app_name());
   $self->rel_num($ccview->rel_num());
   $self->rel_name($ccview->rel_name());
   $self->base_dir($ccview->base_dir());
   $self->pvob($ccview->pvob());
   $self->test_level($test_lvl);
   $self->set_view_name_via_props($helper);
}

sub set_view_name_via_props
{
   my($self, $helper) = @_;
   #$helper->println("CCView->set_view_name_via_props(self, helper)");
   if($self->rel_name())
   {
      $self->view_name($self->app_name() . "_" . $self->rel_num() . "_" . $self->rel_name() . "_" . $self->test_level());
   }
   else
   {
      $self->view_name($self->app_name() . "_" . $self->rel_num() . "_" . $self->test_level());
   }
   #$helper->println("self->view_name = " . $self->view_name());
}

# Verify the src_sream view exists on teh build box.
sub view_exists
{
   my($self, $helper, $bfjob) = @_;
   my($fullpath) = $bfjob->bfroot . "/" . $self->view_name();
   $helper->println("Checking to see if $fullpath exists");
   if(! -d "$fullpath")
   {
      $helper->println("$fullpath does NOT exist.");
      return(0);
   }
   $helper->println("$fullpath DOES exist.");
   return(1);
}



sub get_latest_baselines
{
   my($self, $helper) = @_;
	println("get_latest_baselines($streamdir)");
    $blstring = `cd $streamdir; $CLEARTOOL lsstream -fmt '%[latest_bls]Xp'`;
	#println("cd $streamdir; $CLEARTOOL lsstream -fmt '%[latest_bls]Xp'");
	my(@bl_list) = split(/\ /, $blstring);
	#println("----bl_list-----");
	#println(@bl_list);
	#println("--end bl_list--");
   return(@bl_list);
}

sub load_current_baseline
{  
   my($self, $helper) = @_;
   $helper->println("CCView->load_latest_baseline($self, $helper)");
   my($fullpath) = $self->base_dir . "/" . $self->view_name();
   my($blstring) = "cd $fullpath; cleartool lsstream -fmt '%[latest_bls]Xp'";
   #$helper->println($blstring);
   #`$blstring`;
   #my($blstring) = `cd $streamdir; cleartool lsstream -fmt '%[latest_bls]Xp'`;
   my($blstring) = `$blstring`;
   my(@bllist) = split(/\ /, $blstring);
   foreach $blname (@bllist)
   {  
      #$helper->println("cleartool lsbl -fmt %[depends_on]p $blname");
      @dependencies = `cd $fullpath; cleartool lsbl -fmt %[depends_on]p $blname`;
      if(@dependencies > 0)
      {
         $helper->println($self->test_level() . " blname => " . $blname);
         #$helper->println("pvob - " . $self->pvob);
         my($regex) = "(baseline:)(.*)\@\/(.*)";
         $blname =~ /$regex/;
         $b = new Baseline->new();
         $b->id($2);
         $b->pvob($3);
         $self->current_bl($b);
      }
   }
}

# Recommend the created baseline
sub recommend_rebasebl
{
   my($self, $helper) = @_;
   my($str) = "cleartool chstream -recommended" . $self->rebase_bl()->id_full();
   $helper->println($str);
   my($recommended) = `$str`;
   return($recommended);
   #my($int_stream_view, $rebase_bl_name) = @_;
   #println("$CLEARTOOL chstream -recommended $rebase_bl_name\@/$PVOB > /dev/null 2>&1");
   #my($recommended) = `$CLEARTOOL chstream -recommended $rebase_bl_name\@/$PVOB > /dev/null 2>&1`;
   #return($recommended);
}
sub rebase_stream
{
   my($self, $helper, $bfjob) = @_;
   my($returnstr) = undef;
   if($self->rebase_bl()->id() eq $self->current_bl()->id())
   {
      return("NO REBASE NEEDED - Baselines are the same.");
   }
   
   $self->recommend_rebasebl($helper);
   my($fullpath) = $self->base_dir . "/" . $self->view_name();
   $helper->println("---------------------------------- REBASING ----------------------------------");
   my($rebase_str) = "cd $fullpath; cleartool rebase -view " . $self->view_name() . " -baseline " . $self->rebase_bl()->id() . " -force > " . $bfjob->bfroot() . "/" . $self->rebase_bl()->id() . ".log 2>&1";
   $helper->println($rebase_str);
   my($rc) = system($rebase_str);
   $helper->println("-----------rc-----------\n$rc\n-------------rc-------------");
   
   if($rc)
   {
      my($cancel) = "cd $fullpath; cleartool rebase -view " . $self->view_name() . " -cancel -force >> "  . $bfjob->bfroot() . "/" . $self->rebase_bl()->id() . ".log 2>&1";
      $helper->println($cancel);
      system($cancel);
      return($rc);
   }
   my($commit) = "cd $fullpath; cleartool rebase -view " . $self->view_name() . " -complete -force >> "  . $bfjob->bfroot() . "/" . $self->rebase_bl()->id() . ".log 2>&1";
   $helper->println($commit);
   system($commit);
   $self->prev_bl($self->current_bl());
   $self->current_bl($self->rebase_bl());
   
   $helper->println("-----------rc-----------\n$rc\n-------------rc-------------");
   $helper->println("-------------------------------- END REBASING --------------------------------");
   if($rc > 0)
   {
      return($rc);
   }
   else
   {
      return($self->test_level() . "_OLDBL=$foundation_bl_name\n" . $self->test_level() . "_NEWBL=$rebase_bl_name\n");
   }
}

###############################################################################
# END SUB METHODS
###############################################################################
1;
