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

package ClearCase;
#use Mysql;
use CQPerlExt;
use Activity;
use Helper;
#use lib "/net/ngacelerra/csw/BuildForge_Software/RequiredProducts/buildforge/Platform/BuildForge/Template/SQL/Mysql";
#
# CONSTRUCTOR
#
sub new
{
   my $self = {};
   $self->{id} = undef;
   $self->{date} = undef;
   $self->{activities} = ();
   $self->{helper} = Helper->new();
   bless $self;
   return $self;
}
#
# PROPERTY METHODS
#
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
sub activities
{    
   ### WORKS
   my($self) = shift;
   if(@_){%{$self->{activities}} = @_};
   #if(@_){$self->{activities} = %{@_[0]}};
   return $self->{activities};
}
sub helper
{
   my( $self, $helper) = @_;
   $self->{helper} = $helper if defined($helper);
   return $self->{helper};
}
#
# END PROPERTY METHODS
#

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

sub load_acts_since_prior_build_w_contribs
{
   my($self, $helper) = @_;
   $helper->println("get_acts_from_date_w_contribs(helper)");
   $self->load_acts_since_prior_view_update($helper);
   $helper->println("acts since $date in " . $helper->ucm_stream() . " => " . $helper->get_hash_size($self->activities));
   #$helper->println("START sizeof self->activities => " . $helper->get_hash_size($self->activities));
   %final_list = ();
   $self->get_contrib_acts($helper, \%final_list);
   $helper->println("acts after get_acts_from_date_w_contribs(helper) = " . $helper->get_hash_size($self->activities));
   
}

sub insert_acts_into_db
{
   #my($self, $helper) = @_;
   #$host = "EA-81007676.hhsea.txnet.state.tx.us";
   #$database = "rational";
   #$user = "rational";
   #$password = "rational";
   #$db = Mysql->connect($host, $database, $user, $password);
   #$db->selectdb($database);
   #insert into databse
   
   #while(($act_id, $activity) = each(%{$self->activities}))
   #{
   #   $ins_stmt = "INSERT INTO acts_per_build (build_id, act_id, build_time) "
   #             . "VALUES (" . $helper->build_id . ", " . $activity->id . ", " . $helper->timestamp_this_build . ")";
   #   #$query = $db->query($ins_stmt);
   #   $helper->println($ins_stmt);
   #}
   #$db->disconnect();
}
#
# Recursively go through $self->activities.
# 1) Remove those that are REBASE
# 2) Add those that are CHECKIN or UNKNOWN to to_return_list
# 3) Recursively call method for those that are PROMOTION
# When $self->activities is empty, set $self->activities = to_return_list
#
sub get_contrib_acts
{
   my($self) = shift;
   my($helper) = shift;
   my(%final_list) = %{$_[0]};
   #$helper->display_hashtable($self->activities);
   #$helper->println("final_list size => " . $helper->get_hash_size(\%final_list));
   if($helper->get_hash_size($self->activities) > 0)
   {
      #my($x) = 0;
      my(%promolist) = ();
      while(($act_id, $activity) = each(%{$self->activities}))
      {
         my($tempact) = $self->remove_act_from_list($helper, $activity->id);
         #$helper->println("activity " . $activity->id());
         # REMOVE THE ACTIVITY FROM $self->activity_list
         # IF THE ACTIVITY DOES NOT HAVE A TITLE DEFINED, LOAD IT.
         if(!(defined($tempact->headline())))
         {
            #$helper->println($tempact->id() . " headline is undefined.");
            #$tempact->headline_load_from_cc($helper);
            $tempact->headline_load_from_cq($helper);
         }
         #$helper->println($tempact->id() . " act_type => " . $tempact->act_type());
         # IF THE ACTIVITY IS A CHECKIN, ADD IT TO THE FINAL LIST.
         if(($tempact->act_type() =~ "CHECKIN") || ($tempact->act_type() =~ "UNKNOWN"))
         {
            $final_list{$tempact->id()} = $tempact;
            #$helper->println("final list size => " . $helper->get_hash_size(\%final_list));
         }
         # IF THE ACTIVITY IS A PROMOTION, LOAD ALL CHILDREN & ADD THEM TO $self->activity_list
         elsif ($tempact->act_type() =~ "PROMOTION")
         {
            $promolist{$tempact->id} = $tempact;
         }
         #if($x++ > 1) { exit(); }
      }
      if(%promolist)
      {
         #########################################################
         # create cleartool call
         #########################################################
         $actidlist = "";
         foreach $key (keys %promolist) {
            $actidlist .= " $key" . $helper->project();
         }
         if($helper->os_is_windows())
         {
            $query = "cleartool lsact -contrib " . $actidlist;
         }
         else
         {
            $query = "cleartool lsact -contrib " . $actidlist; 
         }
         $helper->println($query);
         #########################################################
         # make cleartool call
         #########################################################
         chdir($dir);
         my(@actnums) = `$query`;
         $helper->println(@actnums);
         #########################################################
         # go through results of call and remove activites 
         #    already in final_list
         #########################################################
         for($counter = 0; $counter < $helper->get_array_size(@actnums); $counter++)
         {
            $val = $actnums[$counter];
            chomp($val);
            $actnums[$counter] = $val;
            if(exists($final_list{$val}))
            {
               #$helper->println("act $val already in list");
               $helper->println("# actnums before delete: " . $helper->get_array_size(@actnums));
               splice(@actnums, $counter,1);
               $helper->println("# actnums after delete: " . $helper->get_array_size(@actnums));
            }
            else
            {
               if($actnums[$counter] =~ /40671/)
               {
                  $helper->println("--- $actidlist CONTRIB ACTS ----------");
                  print($actnums[$counter] . ", ");
                  $helper->println("\n-------- END $actidlist CONTRIB ACTS ----------------------------");
               }
            }
         }
         my(%actlist) = $self->load_headlines_for_acts($helper, @actnums);
         $helper->println("actlist size = " . $helper->get_hash_size(\%actlist));
         $self->add_activities($helper, \%actlist);
      }
      %promolist = undef;
      $self->get_contrib_acts($helper, \%final_list);
   }
   else #if($helper->get_hash_size($self->activities) == 0)
   {
      $self->activities(%final_list);
      $helper->display_hashtable(\%final_list);
      return \%final_list; 
   }
   $helper->println("-----------------------------------------------");
}


sub load_headlines_for_acts
{
   my($self, $helper, @actnums) = @_;
   if(!@actnums)
   {
      exit("ERROR in load_headlines_for_acts(helper, actnums) .... actnums array empty");
   }
   $helper->println("load_headlines_for_acts(helper, @actnums)");
   $helper->println(@actnums);
   
   # Create ClearQuest session
   my($session) = CQSession::Build();   
   # Login to the ClearQuest database
   $session->UserLogon($helper->cqlogin(), $helper->cqpwd(), $helper->cqdbname(), $helper->cqdbset());# or die("Could not connect !_");
   #$session->SessionLogon($helper->cqlogin(), $helper->cqpwd(), $helper->cqdbname(), $helper->cqdbset());
   # Create acts hashtable to be returned.
   my(%acts) = ();
   
   
   # Get all activities of activity type we are interested in.
   my(@acttypes) = $helper->entity_defs();
   foreach (@acttypes)
   {
      #$helper->println("-------------------------------------------------------------");
      #$helper->println("acttype = " . $_);
      #$helper->println("actnum = @actnums");
      # Initiate the query.
      my $querydef = $session->BuildQuery($_);
      # Fields to be returned by result set
      $querydef->BuildField("id");
      $querydef->BuildField("Headline");
      $querydef->BuildField("Owner");
      $querydef->BuildField("ucm_stream");
      $querydef->BuildField("history.action_timestamp");
      $querydef->BuildField("record_type");
      
      # Add where clause
      my(@sourcestream) = ($helper->ucm_stream());
      
      my($operator) = $querydef->BuildFilterOperator($CQPerlExt::CQ_BOOL_OP_OR);
      #$operator->BuildFilter("history.action_timestamp", $CQPerlExt::CQ_COMP_OP_GTE, \@date);
      $operator->BuildFilter("id", $CQPerlExt::CQ_COMP_OP_EQ, \@actnums);
      # Build the resultset
      my($resultset)= $session->BuildResultSet($querydef);
      # Enable record count on resultset
      $resultset->EnableRecordCount();      
      # Execute the resultset query
      $resultset->Execute();
      # Display the recordcount
      #$helper->println("RecordCount = " . $resultset->GetRecordCount());
      # Go thcrough result set & create appropriate objects
      while ($resultset->MoveNext() == $CQPerlExt::CQ_SUCCESS) {
         # THIS QUERY IS GOING TO RETURN MULTIPLE ROWS FOR ACTIVITIES THAT ARE USED > 1 TIME.
         # WE ONLY CARE ABOUT ONE ENTRY FOR EACH ACTIVITY NUM. THUS, DON'T PROCESS IF
         # ACTIVITY NUM ALREADY EXISTS.
         #$helper->println("LOADED FROM DB: ID " . $resultset->GetColumnValue(1)); 
         if(!($acts{$resultset->GetColumnValue(1)}))
         {
            my($act) = new Activity->new();
            $act->id($resultset->GetColumnValue(1));
            $act->headline($resultset->GetColumnValue(2));
            $act->owner($resultset->GetColumnValue(3));
            $act->ucm_stream($resultset->GetColumnValue(4));
            $act->timestamp($resultset->GetColumnValue(5));
            $act->record_type($resultset->GetColumnValue(6));
            $act->set_act_type();
            #$helper->println("LOADED FROM CQ => " . $act->id() . " - " . $act->timestamp . " - " . $act->act_type());
            $act->parse_headline($helper);
            $acts{$act->id()} = $act;
            $act = undef;
         }
      }
      $querydef = undef;
      #$helper->println("-------------------------------------------------------------");
   }
   
   # Close CQ connection
   CQSession::Unbuild($session);
   
   # Return list of activities
   return(%acts);
   
}
#
# METHOD: load_acts_since_prior_view_update
# ARG: Helper Object
#      This Helper holds a single date as the starting point from when to look up activities.
# PURPOSE: This method pulls all activities from DB since provided date and 
#          assigns them into this object's array.
#
sub load_acts_since_prior_view_update
{
   my($self, $helper) = @_;
   $helper->println("load_acts_since_prior_view_update(helper);");
   
   # Create ClearQuest session
   my($session) = CQSession::Build();
   
   # Create acts hashtable to be returned.
   my(%acts) = ();
   
   # Login to the ClearQuest database
   $session->UserLogon($helper->cqlogin(), $helper->cqpwd(), $helper->cqdbname(), $helper->cqdbset());
   
   # Get all activities of activity type we are interested in.
   my(@acttypes) = $helper->entity_defs();
   my($x) = 0;
   foreach (@acttypes)
   {
      $helper->println("---------------------- START ---------------------------");
      $helper->println($_);
      # Initiate the query.
      my $querydef = $session->BuildQuery($_);
      # Fields to be returned by result set
      $querydef->BuildField("id");
      $querydef->BuildField("Headline");
      $querydef->BuildField("Owner");
      $querydef->BuildField("ucm_stream");
      $querydef->BuildField("history.action_timestamp");
      $querydef->BuildField("record_type");
      
      # Add where clause
      my(@sourcestream) = ($helper->ucm_stream());
      $helper->println("sourcestream = @sourcestream");
      my(@priordate) = $helper->datestring_prior_view_update();
      my(@thisdate) = $helper->datestring_this_view_update();
      my(@modify_action_name) = ("Modify");
      $helper->println("priordate = @priordate");
      $helper->println("thisdate = @thisdate");
      my($operator) = $querydef->BuildFilterOperator($CQPerlExt::CQ_BOOL_OP_AND);
      $operator->BuildFilter("history.action_name", $CQPerlExt::CQ_COMP_OP_NEQ, \@modify_action_name);
      $operator->BuildFilter("history.action_timestamp", $CQPerlExt::CQ_COMP_OP_GT, \@priordate);
      $operator->BuildFilter("history.action_timestamp", $CQPerlExt::CQ_COMP_OP_LT, \@thisdate);
      $operator->BuildFilter("ucm_stream", $CQPerlExt::CQ_COMP_OP_EQ, \@sourcestream);
      # Build the resultset
      my($resultset)= $session->BuildResultSet($querydef);
      # Execute the resultset query
      $resultset->Execute();
      
      # Go through result set & create appropriate objects
      while ($resultset->MoveNext() == $CQPerlExt::CQ_SUCCESS) {
         # THIS QUERY IS GOING TO RETURN MULTIPLE ROWS FOR ACTIVITIES THAT ARE USED > 1 TIME.
         # WE ONLY CARE ABOUT ONE ENTRY FOR EACH ACTIVITY NUM. THUS, DON'T PROCESS IF
         # ACTIVITY NUM ALREADY EXISTS.
         if(!($acts{$resultset->GetColumnValue(1)}))
         {
            my($act) = new Activity->new();
            $act->id($resultset->GetColumnValue(1));
            $act->headline($resultset->GetColumnValue(2));
            $act->owner($resultset->GetColumnValue(3));
            $act->ucm_stream($resultset->GetColumnValue(4));
            $act->timestamp($resultset->GetColumnValue(5));
            $act->record_type($resultset->GetColumnValue(6));
            $act->set_act_type();
            #$helper->println($act->id() . " - " . $act->timestamp . " - " . $act->act_type());
            $helper->println($act->ucm_stream . " - " . $act->id() . " - " . $act->timestamp . " - " . $act->record_type() . " - " . $act->headline);
            $act->parse_headline($helper);
            $acts{$act->id()} = $act;
            $act = undef;
         }
      }
      $querydef = undef;
      if($x++ > 10) { CQSession::Unbuild($session); exit(); }
      $helper->println("----------------------- END ----------------------------");
   }
   
   # Close CQ connection
   CQSession::Unbuild($session);
   
   # Assign list of activities to self.
   $self->activities(%acts);
}

sub list_activites_ready_to_deliver
{
   return 1;
}


#
# THE CODE BELOW WAS USING CLEARTOOL. I'VE REWRITTEN TO USE CQ SO THIS IS NO LONGER NEEDED
#
#sub pull_acts_from_date
#{
#   my($self, @dates_to_load) = @_;
#   #my(@dates_to_load) = @{$datearray};
#   my(%recent_acts) = ();
#   #$self->helper->println("size = " . $self->helper->get_hash_size(\%recent_acts));   
#   while ( ($act_id, $activity) = each(%{$self->activities}) ) 
#   {
#      foreach $adate (@dates_to_load)
#      {
#         #$self->helper->println($adate);
#         if($activity->date() =~ m/^$adate/)
#         {
#            #$self->helper->println("recent_acts{$act_id} = $activity");
#            #$self->helper->println("size = " . $self->helper->get_hash_size(\%recent_acts));
#            $recent_acts{$act_id} = $activity;
#         }
#      }
#   }
#   $self->activities(%recent_acts);
#   #return %recent_acts;
#}
#
#sub get_acts_from_date
#{
#   my($self, $stream, @dates_to_load) = @_;
#   $self->get_all_acts_of_stream($stream);
#   #@keys = keys(%{$self->activities});
#   $self->pull_acts_from_date(@dates_to_load);
#}
#sub get_acts_from_date_w_contribs
#{
#   my($self, $helper, @dates_to_load) = @_;
#   $helper->println("get_acts_from_date_w_contribs(" . @dates_to_load . ")");
#   $self->get_all_acts_of_stream($helper);
#   #@keys = keys(%{$self->activities});
#   #$helper->println("START sizeof self->activities => " . $helper->get_hash_size($self->activities));
#   $self->pull_acts_from_date(@dates_to_load);
#   #$helper->println("END sizeof self->activities => " . $helper->get_hash_size($self->activities));
#   %final_list = ();
#   #$helper->display_hashtable(\%final_list);
#   #$helper->println("final list size => " . $helper->get_hash_size(\%final_list));
#   #while(($act_id, $activity) = each(%{$self->activities}))
#   #{
#   #   $helper->println("activity " . $activity->id());
#   #}
#   $self->get_contrib_acts($helper, \%final_list);
#   $helper->println("num get_acts_from_date_w_contribs(@dates_to_load) = " . $helper->get_hash_size($self->activities));
#}



#sub get_all_acts_of_stream
#{
#   my($self, $helper) = @_;
#    print("get_all_acts_of_stream(" . $helper->ucm_stream() . ");\n");
#   # LOAD ALL ACTIVITIES FROM STREAM
#   my($str) = "cleartool lsact -view " . $helper->ucm_stream() . " -fmt \"%d^%[crm_record_id]p^%u^%[crm_record_type]p^%[stream]Xp^%[headline]p\\n\"";
#   $helper->println($str);
#   my(@rc) = `$str`;
#   $self->activities($self->create_acts_from_list($helper, @rc));
#}

#sub create_acts_from_list
#{
#   my($self, $helper, @list) = @_;
#   my(%acts) = ();
#   foreach (@list) 
#   {
#      chop; 
#      my($act) = new Activity->new();
#      $act->parse_description($helper, $_);
#      $acts{$act->id()} = $act;
#	}
#   return %acts;
#}

# REQUIRE: name of stream
sub desc_acts_all
{
   my($ucm_stream) = $_[0];
   my(@act_array) = [];
   my($str) = "cleartool describe -fmt \"%[activities]p\" \"stream:$ucm_stream" . $helper->project() . "\"";
   $self->helper->println($str);
   my(@nums) = split(/ /, `$str`);
   return @nums;
}

sub get_acts_after_date_from_stream
{
   my($self, $date, $ucm_stream) = @_;
   my(@actsall) = $self->desc_acts_all($ucm_stream);
   my(%recent_acts) = {};
   foreach $act_id(@actsall)
   {
      my($descr) = `cleartool desc \"activity:$act_id@\$ucm_stream\"`;
      if(0)
      {
         $recent_acts{$act_id} = $activity;
      }
   }
   return %recent_acts;
}
###############################################################################
# END ClearCase Object 
###############################################################################
1;
