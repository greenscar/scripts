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
# Baseline Object 
###############################################################################
package Baseline;
use Activity;

sub new
{
   my $self = {};
   $self->{id} = undef;
   $self->{id_full} = undef;
   $self->{date} = undef;
   $self->{pvob} = undef;
   $self->{activities} = ();
   bless $self;
   return $self;
}
sub id
{    
   my ( $self, $id ) = @_;              
   $self->{id} = $id if defined($id);
   $self->id_full("baseline:" . $id . "@/" . $self->pvob()) if defined($id);
   return $self->{id};
}

sub activities
{    
   my ( $self, %activities ) = @_;                         
   $self->{activities} = %activities if defined(%activities);
   return $self->{activities};
}

sub date
{    
   my ( $self, $date ) = @_;                         
   $self->{date} = $date if defined($date);
   return $self->{date};
}

sub pvob
{    
   my ( $self, $pvob ) = @_;                         
   $self->{pvob} = $pvob if defined($pvob);
   $self->id_full("baseline:" . $self->id() . "@/" . $pvob) if defined($pvob);
   return $self->{pvob};
}
sub id_full
{    
   my ( $self, $id_full ) = @_;                         
   $self->{id_full} = $id_full if defined($id_full);
   return $self->{id_full};
}


sub get_act_from_list_via_id
{
   my($self, $id) = @_;
   return @{$self->activities}{$id};
}

sub add_activity
{
   my($self, $activity) = @_;
   #$self->helper->println($activity->id());
   my($id) = $activity->id();
   @{$self->activities}{$id} = $activity;
}
sub add_activities
{
   my($self) = shift;
   my($helper) = shift;
   my(%act_ht) = %{$_[0]};
   #$helper->println("add_activities");
   #$helper->display_hashtable(%act_ht);
   foreach my $key ( keys(%act_ht) ) {
      my($tempact) = $act_ht{$key};
      #$helper->println("adding " . $tempact->id()); 
      @{$self->activities}{$tempact->id()} = $tempact;
   }
   
}
# Remove the act with the provided id from the list & return it
sub remove_act_from_list
{
   my($self, $helper, $id_to_del) = @_;
   return delete(@{$self->activities}{$id_to_del});
}
sub get_num_activities
{
   my($self) = @_;;
   my(%ht) = %{$self->activities()};
   return keys(%ht);
}
###############################################################################
# END Baseline Object 
###############################################################################
1;
