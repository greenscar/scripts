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
   $self->{date} = undef;
   $self->{owner} = undef;
   $self->{headline} = undef;
   $self->{act_type} = undef;
   $self->{record_type} = undef;
   $self->{contributing_acts} = ();
   $self->{headline_valid} = undef;
   $self->{release} = undef;
   $self->{itg_nums} = [];
   $self->{qc_nums} = [];
   $self->{fxl_area} = undef;
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
   if ($headline)
   {
      $self->{headline} = $headline;
      $self->headline_validate();
   }
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
sub fxl_area
{    
   my ( $self, $fxl_area ) = @_;
   $self->{fxl_area} = $fxl_area if defined($fxl_area);
   return $self->{fxl_area};
}
sub headline_descr
{    
   my ( $self, $headline_descr ) = @_;
   $self->{headline_descr} = $headline_descr if defined($headline_descr);
   return $self->{headline_descr};
}

sub itg_nums
{    
   my $self = shift;
   if (@_) { @{ $self->{itg_nums} } = @_ }
   return $self->{itg_nums};
}
sub add_itg_num
{
   my $self = shift;
   my($itg_to_add) = @_;
   push(@{$self->{itg_nums}}, $itg_to_add);
}
sub qc_nums
{   
   my $self = shift;
   #my ( $self, @qc_nums ) = @_;
   if (@_) { @{ $self->{qc_nums} } = @_ }
   return $self->{qc_nums};
}
sub add_qc_num
{
   my($qc_to_add) = @_;
   push(@{$self->{qc_nums}}, $qc_to_add);
}
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


#
# IF the headline follows the naming convention, parse the headline into appropriate vars
#72.0 - SR91920 - QC8451 - ED - Fixed MC EDG cascade null pointer - on 20080211.104237."
#
sub parse_headline
{
   my($self, $helper) = @_;
   #$helper->println("----####----####----####----####----####----####----####----####----####----####----####----");
   #$helper->println("parse_headline(" . $self->{headline} . ")");
   my(@parts) = split(/-/, $self->headline());
   my($numparts) = $helper->get_array_size(@parts);
   my($counter) = 1;
   #$self->{qc_nums} = [];
   #$helper->println("qcnum count = " . $helper->get_array_size(@{$self->qc_nums()}));
   #$helper->println("itgnum count = " . $helper->get_array_size(@{$self->itg_nums()}));
   if($self->headline_valid())
   {
      $self->release(@parts[0]); 
      foreach(@parts)
      {
         # trim off white space
         $_ = $helper->trim($_);
         #$helper->println($_);
         #
         # Sometimes the activity will be associated with an ER or SR. Sometimes it wont. 
         # Sometimes the activity will be associated with a QC defect. Sometimes it wont.
         # Thus, pull out the rest of the data based on this.
         #
         #if(($counter > 0) && ($counter < 4))
         #{
            #$uc = uc($_);
         #$helper->println("uc = $uc");
         if((uc($_) =~ /^(ER\s|ER\d)/) || (uc($_) =~ /^(SR\s|SR\d)/) && ($helper->get_array_size(@{$self->itg_nums()}) == 0))
         {
            $self->parse_itg_nums($helper, $_);
         }
         elsif((uc($_) =~ /^(QC\s|QC\d)/) && ($helper->get_array_size(@{$self->qc_nums()}) == 0))
         {
            $self->parse_qc_nums($helper, $_);
         }
         #}
         #$helper->println($helper->get_array_size($self->qc_nums()));
         if(
            (
               ($counter == 2) 
               && 
               ((($helper->get_array_size(@{$self->itg_nums}) == 0) || ($helper->get_array_size(@{$self->qc_nums}) == 0))
                  &&
                (($helper->get_array_size(@{$self->itg_nums}) > 0) || ($helper->get_array_size(@{$self->qc_nums}) > 0)))
            )
           ||
            (
               ($counter == 4) 
               && 
               (($helper->get_array_size(@{$self->itg_nums}) > 0) && ($helper->get_array_size(@{$self->qc_nums}) > 0))
            )
           )
            {
               $self->fxl_area(@parts[$counter]);
               #put for loop here saying descr is rest of arrya (iterate through rest of array appending it all together
               while($counter < $numparts)
               {
                  my($descpart) = $helper->trim(@parts[$counter + 1]);
                  $self->{headline_descr} .= $descpart . " ";
                  $counter++;
               }
            }
            $self->headline_descr($helper->trim($self->headline_descr()));
            $counter++;
         }
         if(($helper->get_array_size(@{$self->itg_nums}) > 0) && ($helper->get_array_size(@{$self->qc_nums}) > 0))
         {
         #   $helper->println("set fxl area(" .@parts[3] . ")"); 
            $self->fxl_area(@parts[3]);
         }
         #$helper->println($self->id . " -> " . $self->headline_descr);
         #$helper->println("--ITG NUMS--");
         #$helper->println(@{$self->itg_nums});
         #$helper->println("--QC NUMS--");
         #$helper->println(@{$self->qc_nums});
         #$helper->println("----####----####----####----####----####----####----####----####----####----####----####----");
   }
}


# NO LONGER USED DUE TO PULLING VALUE FROM CQ
#sub parse_description
#{
#   my($self, $helper, $descr) = @_;
#   #$helper->println("activity->parse_description($descr)\n");
#   my($date) = undef;
#   my($record_id) = undef;
#   my($headline) = undef;
#   my($owner) = undef;
#   my($record_type) = undef;
#   ($date, $record_id, $owner, $record_type, $ucm_stream, $headline)=split(/\^/,$descr);
#   #$helper->println($date . " - " . $owner ." - " . $record_id . " => " . $record_type . " - " . $headline);
#   $self->id($record_id);
#   $date =~ s/T/\ /g;
#   $self->date($date);
#   $self->headline($headline);
#   $self->owner($owner);
#   $self->ucm_stream($ucm_stream);
#   $self->record_type($record_type);
#   if($self->headline_valid())
#   {
#      $self->parse_headline($helper);
#   }
   ##$helper->println("END activity->parse_description($descr)\n");
#}

# Pull an array of ITG #'s from the provided string. 
# REQUIRE: The numbers must be seperated by a comma (,)
# ASSUME: The first 2 chars are "SR" or "ER". The conditional that calls this
#         function won't call it unless this condition is true.
# RETURN: An array of #'s.
sub parse_itg_nums
{
   my($self, $helper, $string) = @_;
   my($strlen) = length($string);
   #$helper->println("parse_itg_nums(helper, $string)");
   $string = substr($string,2,($strlen+1));
   $string = $helper->remove_spaces($string);
   @nums = split(/,/, $string);
   #$helper->println(@nums);
   $self->itg_nums(@nums);
}

# Pull an array of QC #'s from the provided string. 
# REQUIRE: The numbers must be seperated by a comma (,)
# ASSUME: The first 2 chars are "QC". The conditional that calls this
#         function won't call it unless this condition is true.
# RETURN: An array of #'s.
sub parse_qc_nums
{
   my($self, $helper, $string) = @_;
   #$helper->println("parse_qc_nums(helper, $string)");
   my($strlen) = length($string);
   $string = substr($string,2,($strlen+1));
   $string = $helper->remove_spaces($string);
   my(@nums) = split(/,/, $string);
   #$helper->println(@nums);
   $self->qc_nums(@nums);
}

# LOAD THE HEADLINE OF THE SELECT ACTIVITY
sub headline_load_from_cq
{
   my($self, $helper) = @_;

   # Create ClearQuest session
   my($session) = CQSession::Build();
   
   # Create acts hashtable to be returned.
   my(%acts) = ();
   
   # Login to the ClearQuest database
   $session->UserLogon($helper->cqlogin(), $helper->cqpwd(), $helper->cqdbname(), $helper->cqdbset());
   my(@acttypes) = $helper->entity_defs();
   foreach (@acttypes)
   {
      # Initiate the query.
      my $querydef = $session->BuildQuery($_);
      # Fields to be returned by result set
      $querydef->BuildField("id");
      $querydef->BuildField("Headline");
      $querydef->BuildField("Owner");
      $querydef->BuildField("ucm_stream");
      $querydef->BuildField("history.action_timestamp");
      $querydef->BuildField("record_type");
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
         $act->id($resultset->GetColumnValue(1));
         $act->headline($resultset->GetColumnValue(2));
         $act->owner($resultset->GetColumnValue(3));
         $act->ucm_stream($resultset->GetColumnValue(4));
         $act->timestamp($resultset->GetColumnValue(5));
         $act->record_type($resultset->GetColumnValue(6));         
         $act->parse_headline($helper);
         $acts{$act->id()} = $act;
      }
   }
   
   # Close CQ connection
   CQSession::Unbuild($session);
   
}
sub headline_validate
{
   my($self) = shift;
   if($self->headline() =~ /^(\d+(\.\d+)\s*\-|(\d)+\s*\-)\s*((SR|ER|QC)\s*(\d+,?)+\s*\-)(\s*(SR|ER|QC)\s*(\d+,?)+\s*\-)?\s*([a-zA-Z]{2,}\s*-)/)
   {
      $self->headline_valid(1);
   }
   else
   {
      $self->headline_valid(0);
   }
   return($self->headline_valid);
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
      $helper->println("FXL_AREA => " . $self->fxl_area);
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

