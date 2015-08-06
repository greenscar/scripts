#!/opt/rational/clearquest/bin/cqperl

package Ratladmin;
use CCView;
use File::Listing;
use Time::localtime;
use Switch;
use Baseline;
sub new
{
   my $self = {};
   my $stream_structure = undef;
   my $stream_list = undef;
   my $ccview_TST = undef;
   my $ccview_LOCAL = undef;
   my $ccview_DEV = undef;
   my $ccview_SIT = undef;
   my $ccview_UAT = undef;
   bless $self;
   
   return $self;
}


sub ccview_TST
{    
   my ( $self, $ccview_TST ) = @_;                         
   $self->{ccview_TST} = $ccview_TST if defined($ccview_TST);
   return $self->{ccview_TST};
}

sub ccview_LOCAL
{    
   my ( $self, $ccview_LOCAL ) = @_;                         
   $self->{ccview_LOCAL} = $ccview_LOCAL if defined($ccview_LOCAL);
   return $self->{ccview_LOCAL};
}

sub ccview_DEV
{    
   my ( $self, $ccview_DEV ) = @_;                         
   $self->{ccview_DEV} = $ccview_DEV if defined($ccview_DEV);
   return $self->{ccview_DEV};
}

sub ccview_SIT
{    
   my ( $self, $ccview_SIT ) = @_;                         
   $self->{ccview_SIT} = $ccview_SIT if defined($ccview_SIT);
   return $self->{ccview_SIT};
}

sub ccview_UAT
{    
   my ( $self, $ccview_UAT ) = @_;                         
   $self->{ccview_UAT} = $ccview_UAT if defined($ccview_UAT);
   return $self->{ccview_UAT};
}

sub stream_structure
{    
   my ( $self, $stream_structure ) = @_;                         
   $self->{stream_structure} = $stream_structure if defined($stream_structure);
   return $self->{stream_structure};
}

sub stream_list
{
   my($self, @list) = @_;
   @{$self->{stream_list}} = @list if(!($self->{stream_list}));
   #if(!($self->{stream_list})){ $self->{stream_list} = $^O; }
   return @{$self->{stream_list}};
}

##########################################################################
# END OBJECT VAR METHODS
##########################################################################


# There are 2 stream strucures based on the type of release.
# Determine the structure being used.
# This method is dependent on views already existing. 
# If we don't want to depend on views existing, 
#    replace -d $stream with `cleartool lsview | grep $prev_stream`
sub set_stream_structure
{
   my($self, $helper, $ccview, $bfjob) = @_; #$test_lvl, $src_stream) = @_;
   $helper->println("Ratladmin->get_stream_structure($self, $helper, $ccview, $bfjob)");
   my($int_stream, $dev_stream, $sit_stream, $uat_stream) = ($ccview->view_name(), $ccview->view_name(), $ccview->view_name(), $ccview->view_name());
   my($DEV, $SIT, $UAT, $TST) = ($helper->DEV(), $helper->SIT(), $helper->UAT(), $helper->TST); 
   
   my($test_lvl) = $ccview->test_level();
   $int_stream =~ s/$test_lvl/$TST/g;
   $dev_stream =~ s/$test_lvl/$DEV/g;
   $sit_stream =~ s/$test_lvl/$SIT/g;
   $uat_stream =~ s/$test_lvl/$UAT/g;
   
   my($stream_structure) = undef;
   $helper->println($bfjob->bfroot() . "/$int_stream");
   $helper->println($bfjob->bfroot() . "/$dev_stream");
   $helper->println($bfjob->bfroot() . "/$sit_stream");
   $helper->println($bfjob->bfroot() . "/$uat_stream");
   
   #my($prevccview) = new CCView->new();
   $self->ccview_LOCAL(new CCView->new());
   $self->ccview_LOCAL->set_all_props($helper, $ccview, $helper->LOCAL());
   #$self->ccview_LOCAL->set_all_props($ccview->app_name(), $ccview->rel_num(), $helper->LOCAL(), $ccview->rel_name());
   $self->ccview_UAT(new CCView->new());
   $self->ccview_UAT->set_all_props($helper, $ccview, $helper->UAT());
   $self->ccview_TST(new CCView->new());
   $self->ccview_TST->set_all_props($helper, $ccview, $helper->TST());
      
   if((-d $bfjob->bfroot() . "/$int_stream") && (-d $bfjob->bfroot() . "/$dev_stream") && (-d $bfjob->bfroot() . "/$sit_stream") && (-d $bfjob->bfroot() . "/$uat_stream"))
   {
      $helper->println("structure = " . $helper->STREAM_STRUCTURE_NORM());
      $self->stream_structure($helper->STREAM_STRUCTURE_NORM());
      $self->stream_list($helper->LOCAL(), $helper->DEV(), $helper->SIT(), $helper->UAT());
   
      $self->ccview_DEV(CCView->new());
      $self->ccview_DEV->set_all_props($helper, $ccview, $helper->DEV());
      $self->ccview_SIT(CCView->new());      
      $self->ccview_SIT->set_all_props($helper, $ccview, $helper->SIT());
      
   }
   elsif((-d $bfjob->bfroot() . "/$int_stream") && (-d $bfjob->bfroot() . "/$uat_stream"))
   {
      $helper->println("structure = " . $helper->STREAM_STRUCTURE_EMER());
      $self->stream_structure($helper->STREAM_STRUCTURE_EMER());
      $self->stream_list($helper->LOCAL(), $helper->UAT());
   
   }
   else
   {
      $helper->println("ERROR in Ratladmin->set_stream_structure ... not all views exist");
      return(0);
   }
   $helper->println("stream list => ");
   $helper->println($self->stream_list());
   return(1);
}





# Verify that src_stream is in the correct format
# projectName_ww.x.y.z_buildName
# Then pull important values from it.
sub parse_view_name
{
   my($self, $helper, $view_name) = @_;
   if($view_name =~ /^([a-zA-Z0-9]+)_([\d|x][\d|x]\.[\d|x]\.[\d|x]\.[\d|x])_?(\w*)?_([a-zA-Z0-9]+)$/)
   {
      println("1 = $1\n2 = $2\n3 = $3\n4 = $4");
      if(defined($4))
      {
         ($app_name, $rel_num, $streampurpose, $test_level) = ($1, $2, $3, $4);
      }
      else
      {
         ($app_name, $rel_num, $test_level) = ($1, $2, $3);
      }
      return ($app_name, $rel_num, $streampurpose, $test_level);
   }
   else
   {
      return(0);
   }
}

# Verify the src_sream view exists on teh build box.
sub verify_view_exists
{
   my($self, $helper, $view_name) = @_;
   my($path) = $helper->bf_root . $view_name;
   if(! -d "$path")
   {
      return(0);
   }
   return(1);
}

sub set_current_baseline_of_ccview_from_tst_lvl
{
   my($self, $helper, $ccview) = @_;
   $helper->println("set_current_baseline_of_ccview_from_tst_lvl($self, $helper, $ccview)");
   $helper->println("view_name => " . $ccview->view_name());
   my($lvl) = $ccview->test_level();
   my($view) = "ccview_" . $lvl;
   $helper->println("view - $view");
   my($obj) = $self->$view();
   $helper->println("obj - " . $obj);
   $helper->println("----------------------------------------------------");
   $helper->println($obj->test_level() . " -> " . $obj->current_bl()->id());
   $helper->println($obj->test_level() . " -> " . $obj->current_bl()->id_full());
   $helper->println("----------------------------------------------------");
   #$ccview->current_bl()
}

sub get_prev_ccview
{
   my($self, $helper, $ccview) = @_;
   $helper->println("get_prev_tst_lvl($self, $helper, $ccview)");
   my($prev_tst_lvl) = undef;
   my($tmp) = undef;
   my($prevccview) = new CCView->new();
   switch ($ccview->test_level())
   {
      case($helper->DEV())
      {
         return($self->ccview_LOCAL());
      }
      case($helper->SIT())
      {
         return($self->ccview_DEV());
      }
      case($helper->UAT())
      {
         return($self->ccview_UAT());
      }
      else
      {
         return 0;
      }
   }
}


sub load_current_baselines
{
   my($self, $helper, $ccview) = @_;
   $helper->println("Ratladmin->load_current_baselines($helper)");
   if($self->stream_structure() eq $helper->STREAM_STRUCTURE_NORM())
   {
      if(defined($self->ccview_DEV) && defined($self->ccview_SIT) && defined($self->ccview_UAT))
      {
         $self->ccview_DEV->load_current_baseline($helper);
         $self->ccview_SIT->load_current_baseline($helper);
         $self->ccview_UAT->load_current_baseline($helper);
      }
      else
      {
         return(0);
      }
   }
   elsif($self->stream_structure() eq $helper->STREAM_STRUCTURE_EMER())
   {
      
      if(defined($self->ccview_UAT))
      {
         $self->ccview_UAT->load_latest_baseline($helper);
      }
      else
      {
         return(0);
      }
   }
   else
   {
      return(0);
   }
   # Now that we have loaded all baselines, set ccview baseline var to the correct val.
   my($view) = "ccview_" . $ccview->test_level();
   my($obj) = $self->$view();
   $ccview->current_bl($obj->current_bl());
   return(1);
}
    

sub create_baseline
{
   my($self, $helper, $ccview, $blname) = @_;
   $helper->println("Ratladmin->create_baseline($self, $helper, $ccview, $blname)");
   my($bl_created) = 0;
   my($rc) = undef;
   my($string) = "cd " . $ccview->base_dir() . "/" . $ccview->view_name() ."; cleartool mkbl -all " . $helper->mkbloptions() . " -view " . $self->ccview_TST()->view_tag() . " " . $blname;
   $helper->println($string);
   $rc = `$string`;
   $helper->println($rc);
   if ($rc =~ /Created\ baseline/) {
      $bl_created = 1;
   }
   $helper->println("bl_created = $bl_created");
   $self->ccview_TST->current_bl($blname);
   $ccview->rebase_bl($blname);
   return($bl_created);   
}

sub set_rebase_bl
{
   my($self, $helper, $bfjob, $ccview, $ccview_prev) = @_;
   $helper->println("set_rebase_bl($self, $helper, $ccview, $ccview_prev)");
   
   if(($ccview->test_level() eq $helper->DEV()) || (($ccview->test_level() eq $helper->UAT()) && ($ratladmin->stream_structure() eq $helper->STREAM_STRUCTURE_EMER())))
   {
      # 1) Construct BL Name
      my($yr, $mon, $day) = $helper->get_curr_date();
      my($hr, $min, $sec) = $helper->get_curr_time();
      $new_bl_name = $yr . $mon . $day . $hr . $min . $sec . "_" . $ccview->get_rel_num_short() . "_" . $bfjob->jobid();
      #if((defined $stream_purpose) && ($stream_purpose ne ""))
      #{
      #   $new_bl_name .= "_" . $stream_purpose;
      #}
      $new_bl_name .=  "_" . $bfjob->start_time() . "_" . $bfjob->bftag();
      $helper->println("new_bl_name => $new_bl_name");
      
      # 2) Create new baseline with this name
      # Commented out for debug purposes. No reason to continue creating baselines.
      #if($self->create_baseline($helper, $ccview, $new_bl_name) == 0)
      #{
      #   printf("ERROR Creating Baseline.");
      #   exit 1;
      #}
      $new_bl_name = "20090206151254_88000_12345_2008-01-01_1010_33";
      $newbl = new Baseline->new();
      $newbl->pvob($ccview->pvob());
      $newbl->id($new_bl_name);
      $helper->println("newbl->pvob -> " . $newbl->pvob());
      $ccview->rebase_bl($newbl);
      #$ccview->rebase_bl($new_bl_name);
      $helper->println($ccview->rebase_bl()->id() . " baseline created");
      $helper->println($ccview->rebase_bl()->id() . " set as ccview->rebase_bl()->id()");
      $helper->println($ccview->rebase_bl()->id_full() . " set as ccview->rebase_bl()->id_full()");
      #$ratladmin->create_baseline($helper, $new_bl_name);
   }
   else
   {
      $ccview->rebase_bl($ccview_prev->current_bl());
      $helper->println($ccview->rebase_bl()->id() . " set as ccview->rebase_bl()->id()");
      $helper->println($ccview->rebase_bl()->id_full() . " set as ccview->rebase_bl()->id_full()");
   }
}

###############################################################################
# END SUB METHODS
###############################################################################
1;
