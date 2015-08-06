#!/opt/rational/clearquest/bin/cqperl

# Script:      streamread.pl
# Version:     2.0
# Author:      James Sandlin <james.g.sandlin@hhsc.state.tx.us>
# Date:        2008-02-05
# Purpose:     Generates a report of all ClearCase activities in a build of type:
#              "BaseCMActivity", "ChangeActivity", "UCMUtilityActivity", "Defect"
#              A stream name is provided. The script looks at the date of the last 
#              build & the current build. It gets dates from those files and finds
#              All deliveries to the build stream in that time range. It then
#              recursively traces those activites back to their initial contributing
#              activity.

# Syntax:      streamread.pl ARGS
# Arguments:   --stream    n     Name of stream to find initial activities

###############################################################################
# Activity Object 
###############################################################################
package Activity;
use CQPerlExt;
use Helper;
#constructor
sub new
{
   my $self = {};
   $self->{id} = undef;
   $self->{ucm_stream} = undef;
   $self->{timestamp} = undef;
   $self->{project} = undef;
   $self->{date} = undef;
   $self->{owner} = undef;
   $self->{headline} = undef;
   $self->{act_type} = undef;
   $self->{record_type} = undef;
   $self->{contributing_acts} = ();
   $self->{headline_valid} = undef;
   $self->{release} = undef;
   #$self->{itg_nums} = [];
   #$self->{qc_nums} = [];
   $self->{itg_num} = undef;
   $self->{qc_num} = undef;
   $self->{team} = undef;
   $self->{headline_descr} = undef;
   $self->{files} = [];
   $self->{helper} = Helper->new();
   bless $self;
   return $self;
}

####################################################################
# START object variable functions
####################################################################
sub helper
{
   my( $self, $helper) = @_;
   $self->{helper} = $helper if defined($helper);
   return $self->{helper};
}
sub project
{    
   my ( $self, $project ) = @_;                         
   $self->{project} = $project if defined($project);
   return $self->{project};
}
sub id
{    
   my ( $self, $id ) = @_;                         
   $self->{id} = $id if defined($id);
   return $self->{id};
}
sub ucm_stream
{    
   my($self, $ucm_stream) = @_;
   
   if($ucm_stream =~ /^stream/)
   {
      $ucm_stream = substr($ucm_stream, 7);
   }
   if($ucm_stream =~ /@/)
   {
      $at_pos = index($ucm_stream, '@');
      $ucm_stream = substr($ucm_stream, 0, $at_pos);
   }
   $self->{ucm_stream} = $ucm_stream if defined($ucm_stream);
   return $self->{ucm_stream};
   
   #my ( $self, $ucm_stream ) = @_;
   #$self->{ucm_stream} = $ucm_stream if defined($ucm_stream);
   #return $self->{ucm_stream};
}
sub timestamp
{    
   my ( $self, $timestamp ) = @_;                         
   $self->{timestamp} = $timestamp if defined($timestamp);
   return $self->{timestamp};
}
sub date
{    
   my ( $self, $date ) = @_;                         
   $self->{date} = $date if defined($date);
   return $self->{date};
}
sub owner
{    
   my ( $self, $owner ) = @_;                         
   $self->{owner} = $owner if defined($owner);
   return $self->{owner};
}
sub headline
{    
   my ( $self, $headline ) = @_;
   $self->{headline} = $headline if defined($headline);
   return $self->{headline};
}
sub act_type
{    
   my ( $self, $act_type ) = @_;                         
   $self->{act_type} = $act_type if defined($act_type);
   return $self->{act_type};
}
sub record_type
{    
   my ( $self, $record_type ) = @_;                         
   $self->{record_type} = $record_type if defined($record_type);
   $self->set_act_type() if defined($record_type);
   return $self->{record_type};
}
sub contributing_acts
{    
   my ( $self, %contributing_acts ) = @_;                         
   $self->{contributing_acts} = %contributing_acts if defined(%contributing_acts);
   return $self->{contributing_acts};
}
sub headline_valid
{    
   my ( $self, $headline_valid ) = @_;
   $self->{headline_valid} = $headline_valid if defined($headline_valid);
   return $self->{headline_valid};
}
sub release
{    
   my ( $self, $release ) = @_;
   $self->{release} = $release if defined($release);
   return $self->{release};
}
sub team
{    
   my ( $self, $team ) = @_;
   $self->{team} = $team if defined($team);
   return $self->{team};
}
sub headline_descr
{    
   my ( $self, $headline_descr ) = @_;
   $self->{headline_descr} = $headline_descr if defined($headline_descr);
   return $self->{headline_descr};
}

sub qc_num
{    
   my $self = shift;
   if (@_) { @{ $self->{qc_num} } = @_ }
   return $self->{qc_num};
}

sub itg_num
{    
   my $self = shift;
   if (@_) { @{ $self->{itg_num} } = @_ }
   return $self->{itg_num};
}

#sub itg_nums
#{    
#   my $self = shift;
#   if (@_) { @{ $self->{itg_nums} } = @_ }
#   return $self->{itg_nums};
#}
#sub add_itg_num
#{
#   my $self = shift;
#   my($itg_to_add) = @_;
#   push(@{$self->{itg_nums}}, $itg_to_add);
#}
#sub qc_nums
#{   
#   my $self = shift;
#   #my ( $self, @qc_nums ) = @_;
#   if (@_) { @{ $self->{qc_nums} } = @_ }
#   return $self->{qc_nums};
#}
#sub add_qc_num
#{
#   my($qc_to_add) = @_;
#   push(@{$self->{qc_nums}}, $qc_to_add);
#}
sub files
{    
   my $self = shift;
   if(@_) { @{ $self->{files} } = @_ }
   return $self->{files};
}
sub add_file
{
   my $self = shift;
   my($file_to_add) = @_;
   push(@{$self->{files}}, $file_to_add);
}

sub set_act_type
{
   my($self) = shift;
   if($self->record_type =~ /^UCMUtilityActivity$/) 
   {
      if($self->headline =~ /rebase/)
      {
         $self->act_type("REBASE");
      }
      else
      {
         $self->act_type("PROMOTION");
      }
   }
   elsif (($self->record_type =~ /^BaseCMActivity$/) || ($self->record_type =~ /^ChangeActivity$/) || ($self->record_type =~ /^Defect$/))
   {
      $self->act_type("CHECKIN");
   }
   else
   {
      $self->act_type("UNKNOWN");
   }
}


####################################################################
# END object variable functions
####################################################################

sub load_entity_def_names
{
   my($self, $helper) = @_;
   # Create ClearQuest session
   my($session) = CQSession::Build();
   # Login to the ClearQuest database
   $session->UserLogon($helper->cqlogin(), $helper->cqpwd(), $helper->cqdbname(), $helper->cqdbset());
   my($ednames) = $session->GetQueryEntityDefNames();
   foreach $name (@$ednames)
   {
      $helper->println($name);
   }
   # Close CQ connection
   CQSession::Unbuild($session);
}

# Load details from CQ.
sub load_details_from_cq
{
   my($self, $helper) = @_;
   $helper->println("Activity->load_details_from_cq($self, $helper)");
   
   # Create ClearQuest session
   my($session) = CQSession::Build();
   
   # Login to the ClearQuest database
   $session->UserLogon($helper->cqlogin(), $helper->cqpwd(), $helper->cqdbname(), $helper->cqdbset());
   
   # From ClearQuestAPIReference.pdf:
   # The name you specify in the entitydef_name parameter must correspond to an 
   # appropriate record type in the schema. To obtain a list of legal names for 
   # entitydef_name, use the GetQueryEntityDefNames method. 


   # 
   # Must have array to build query.
   # Initiate the query.
   my(@acttypes) = $helper->entity_defs();
   foreach (@acttypes)
   {
      # Initiate the query.
      my $querydef = $session->BuildQuery($_);
      $querydef->BuildField("id");
      $querydef->BuildField("Headline");
      $querydef->BuildField("Owner");
      $querydef->BuildField("ucm_stream");
      $querydef->BuildField("history.action_timestamp");
      $querydef->BuildField("record_type");
      $querydef->BuildField("qc_num");
      $querydef->BuildField("release_num");
      $querydef->BuildField("sr_er_num");
      $querydef->BuildField("team");
      $helper->println("fields built");
      @value = $self->id();
      my $rootoperator = $querydef->BuildFilterOperator($CQPerlExt::CQ_BOOL_OP_AND);
      $rootoperator->BuildFilter("id", $CQPerlExt::CQ_COMP_OP_EQ, \@value);
      # Build the resultset
      my($resultset)= $session->BuildResultSet($querydef);
      # Execute the resultset query
      $resultset->Execute();
      
      # Go through result set & create appropriate objects
      while ($resultset->MoveNext() == $CQPerlExt::CQ_SUCCESS) {
         my($act) = new Activity->new();
         $self->id($resultset->GetColumnValue(1));
         $self->headline($resultset->GetColumnValue(2));
         $self->owner($resultset->GetColumnValue(3));
         $self->ucm_stream($resultset->GetColumnValue(4));
         $self->timestamp($resultset->GetColumnValue(5));
         $self->record_type($resultset->GetColumnValue(6));
         $self->qc_num($resultset->GetColumnValue(7));
         $self->rel_num($resultset->Get_ColumnValue(8));
         $self->itg_num($resultset->Get_ColumnValue(9));
         $self->team($resultset->Get_ColumnValue(10));
      $querydef->BuildField("qc_num");
      $querydef->BuildField("release_num");
      $querydef->BuildField("sr_er_num");
      $querydef->BuildField("team");
      }
      $self->printActivityDetails($helper);
      $helper->println("self->id = " . $self->id());
   }
   # Close CQ connection
   CQSession::Unbuild($session);
   
}
sub printActivityDetails
{
      my($self, $helper) = @_;
      $helper->println("------------------------------------------------------------------");
      $helper->println("ID => " . $self->id);
      $helper->println("DATE => " . $self->date);
      $helper->println("USER => " . $self->owner);
      $helper->println("TITLE => " . $self->headline);
      $helper->println("RECORD TYPE => " . $self->record_type);
      $helper->println("SOURCE STREAM => " . $self->ucm_stream);
      $helper->println("ACT TYPE => " . $self->act_type);
      $helper->println("RELEASE => " . $self->release);
      $helper->println("FXL_AREA => " . $self->team);
      $helper->println("HEADLINE_DESCR => " . $self->headline_descr);
      my(@nums) = @{$self->itg_nums};
      if($helper->get_array_size(@nums) > 0)
      {
         $helper->println("ITG_NUMS\n----------------\n");
         foreach $num(@nums)
         {
            $helper->println($num);
         }
         $helper->println("----------------\n");
      }
      my(@nums) = undef;
      if($helper->get_array_size(@nums) > 0)
      {
         $helper->println("QC_NUMS\n----------------\n");
         my(@nums) = @{$self->qc_nums};
         foreach $num(@nums)
         {
            $helper->println($num);
         }
         $helper->println("----------------\n");
      }
      $helper->println("------------------------------------------------------------------\n");
}
###############################################################################
# END Activity Object 
###############################################################################
1;

