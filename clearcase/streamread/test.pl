#!/usr/bin/perl

#print "content-type: text/html \n\n";
use CQPerlExt;
use ClearCase;
use Helper;
use Activity;


use File::stat;
use Time::localtime;
use File::Listing;
$file = "C:\\webserver\\htdocs\\buildreports\\LOCAL_2008-03-11_1200.htm";
$earfiledir = "C:/webserver/htdocs/buildreports";
$date_string = ctime(stat($file)->mtime);
print "file $file updated at $date_string\n";
my($thisbuilddate) = 0;
my($priorbuilddate) = 0;
for (parse_dir(`ls -l $earfiledir`))
{
   ($name, $type, $size, $mtime, $mode) = @$_;
   next if $type ne 'f'; # plain file
   if($mtime > $thisbuilddate)
   {
      $priorbuilddate = $thisbuilddate;
      $thisbuilddate = $mtime;
   }
   
   print($name . " => " . $mtime . "\n");
}
print("prior => " . $priorbuilddate . "\n");
print("this => " . $thisbuilddate . "\n");



exit();
$ccadmin = new ClearCase->new();
$helper = new Helper->new();
# Create ClearQuest session
$sessionObj = CQSession::Build();
#$databases = $sessionObj->GetAccessibleDatabases("MASTR", "jsandlin", "2003.06.00"); 
#$count = $databases->Count(); 
#For each accessible database, login as joe with password gh36ak3 
#for($x=0;$x<$count;$x++){ 
#   $db = $databases->Item($x); 
#   $dbName = $db->GetDatabaseName(); 
#   print($dbName . "\n");
#   # Logon to the database 
#   $sessionObj->UserLogon( "jsandlin", "ClearQuest", $dbName, "" ) or print("Could NOT connect to $dbName\n"); 
#   my $loginname = $sessionObj->GetUserLoginName(); 
#   my $authloginname = $sessionObj->GetAuthenticationLoginName(); 
#   print "User login: $authusername , $authloginname , $cqusername, $loginname \n";
#   print("logged into $dbName\n");
#   #... 
#} 
#CQSession::Unbuild($sessionObj);

# Create acts hashtable to be returned.
my(%acts) = ();

# Login to the ClearQuest database
# SessionLogon is for UNIX
$sessionObj->UserLogon("jsandlin", "ClearQuest", "TAA", "7.0.0");
#$sessionObj->SessionLogon("jsandlin", "ClearQuest", "TAA", "2003.06.00") or die("Could not connect");
      $querydef = $sessionObj->BuildQuery("UCMUtilityActivity");
      # Fields to be returned by result set
      $querydef->BuildField("id");
      $querydef->BuildField("Headline");
      $querydef->BuildField("Owner");
      $querydef->BuildField("ucm_stream");
      $querydef->BuildField("history.action_timestamp");
      $querydef->BuildField("record_type");
      
      # Add where clause
      #my(@sourcestream) = ($helper->ucm_stream());
      my(@sourcestream) = ("jsandlin_TIERS_DEV", "jsandlin_TIERS_SIT");
      my(@start_date)="2008-03-08 12:00:00";
      #my($operator) = $querydef->BuildFilterOperator($CQPerlExt::CQ_BOOL_OP_AND);
      #$operator->BuildFilter("history.action_timestamp", $CQPerlExt::CQ_COMP_OP_GTE, \@date);
      my($operator) = $querydef->BuildFilterOperator($CQPerlExt::CQ_BOOL_OP_OR);
      $operator->BuildFilter("ucm_stream", $CQPerlExt::CQ_COMP_OP_EQ, \@sourcestream);
      # Build the resultset
      my($resultset)= $sessionObj->BuildResultSet($querydef);
      # Execute the resultset query
      $resultset->Execute();
      $ct = $resultset->ExecuteAndCountRecords();
      print("count = $ct\n");
      # Go through result set & create appropriate objects
      while ($resultset->MoveNext() == $CQPerlExt::CQ_SUCCESS) {
         # THIS QUERY IS GOING TO RETURN MULTIPLE ROWS FOR ACTIVITIES THAT ARE USED > 1 TIME.
         # WE ONLY CARE ABOUT ONE ENTRY FOR EACH ACTIVITY NUM. THUS, DON'T PROCESS IF
         # ACTIVITY NUM ALREADY EXISTS.
         print($resultset->GetColumnValue(1) . "\n");
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
            $helper->println($act->id() . " - " . $act->timestamp . " - " . $act->act_type());
            $act->parse_headline($helper);
            $acts{$act->id()} = $act;
            $act = undef;
         }
      }
CQSession::Unbuild($sessionObj);

