###############################################################################
# ClearCase Object 
#$blname = cleartool lsstream -fmt "%[found_bls]p" -cview
#$bltime = cleartool lsbl -fmt "%d" $blname@/TAA_projects

#bl timestamp = 2007-03-28 11:02:10-05
#act timestamp = 2007-03-28 13:56:29-05
# cleartool lsbl 20070328_TIERS_611081@/TAA_projects
###############################################################################
use Activity;
use Baseline;
package ClearCase;
sub new
{
   my $self = {};
   $self->{id} = undef;
   $self->{date} = undef;
   $self->{act_hash_table} = ();
   bless $self;
   return $self;
}
sub id
{    
   my ( $self, $id ) = @_;                         
   $self->{id} = $id if defined($id);
   return $self->{id};
}
sub date
{    
   my ( $self, $date ) = @_;                         
   $self->{date} = $date if defined($date);
   return $self->{date};
}
sub act_hash_table
{    
   my ( $self, %act_hash_table ) = @_;                         
   $self->{act_hash_table} = %act_hash_table if defined(%act_hash_table);
   return $self->{act_hash_table};
}

sub pull_acts_from_date(%all_activities, @dates_to_load)
{
   while ( ($date, $activity) = each(%all_activities) ) 
   {
      print("date = $date => act = $activity\n");
      foreach(@dates_to_load)
      {
         if($date =~ m/^$_/)
         {
            print("$date -> $activity\n");
            $recent_acts{$date} = $activity;
         }
      }
   }
   return %recent_acts;
}
sub get_all_acts_of_stream
{
	my($stream_name) = @_[1];
   my (%act_hash_table) = ();
   my @rc = `cleartool lsact -view $stream_name -fmt "%d^%[crm_record_id]p^%[headline]p^%u\\n"`;
   foreach (@rc) 
   {
      chop;
      my($line) = $_;
      (my($date), my($record_id), my($title), my($user))=split(/\^/,$line);
      $act = new Activity->new();
      $act->id($record_id);
      $date =~ s/T/\ /g;
      $act->date($date);
      $act->title($title);
      $act->user($user);
      $act_hash_table{$date} = $act;
	}
   #while(($date, $act) = each(%act_hash_table))
   #{
   #   print("$date => " . $act->id . "\n");
   #}
   $self->act_hash_table(%act_hash_table);
   return %act_hash_table;
}

###############################################################################
# END ClearCase Object 
###############################################################################
1;
