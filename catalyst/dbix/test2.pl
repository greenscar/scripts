use MyDB::Schema;
use Time::localtime;

my $schema = MyDB::Schema->connect('dbi:Oracle:host=165.184.34.104;service_name=DTE2;port=1522', 'ENV_MGT', 'envown123');



my @stage = $schema->resultset('R2instances')->all;
foreach(@stage)
{
   print("--------------------------------------\n");
   print("inst_id = " . $_->id . "\n");
   print("app = " . $_->application->name . "\n");
   #foreach($_->r2deployments)
   #{
   #   print($_->timestamp . "\n");   
   #}
   my($latest_deployment);
   foreach($_->r2deployments)
   {
      $_->timestamp =~ m/(\d+)-(\w+)-(\d+)(\d+)\.(\d+)\.(\d+)\.(\d+)\s(AM|PM)/;
      #$_->timestamp =~ m/^(\d+)/;
      #print($_->timestamp . " => " . localtime($_->timestamp)->year . "\n");  
     
      #$c->log->debug($_->timestamp);
      if($latest_deployment eq '')
      {
         #$c->log->debug("$_ => " . $_);
         $latest_deployment = $_;
         #$c->log->debug("$latest_deployment => " . $latest_deployment);
      }  
      if(localtime($_->timestamp) > localtime($latest_deployment->timestamp))
      {
         #$c->log->debug($_->timestamp . " => " . $_->baseline);
         $latest_deployment = $_;
      }
   }
   

   #foreach (sort { $a->timestamp cmp $b->timestamp } $_->r2deployments) {
   #  print ($_->timestamp . "\n");
   #}

   print("latest => " . $latest_deployment->timestamp . "\n");
   print("--------------------------------------\n");
   #$_->get_latest_deployment;
}
exit(1);

#my @all_books = $schema->resultset('R2instances')->all;


print("\n------------------------------\n");
# WORKS
#my(@rs) = $schema->resultset('R2instances')->search();
#

# WORKS
#my(@rs) = $schema->resultset('R2instances')->search({},
#                           {
#                              select => [
#                                 'env_id',
#                                 ],
#                              distinct => 1,
#                              order_by => 'env_id'
#                           }
#                           );
###while(my $dl = $rs->next)
#foreach(@rs)
#{
#   while (($key, $value) = each(%{$_})){
#     print $key.", ".$value."\n";
#   }
#   print("env_id => " . $_->env_id->env_id . "\n");
#   print("------------------\n");
#}
#
my(@rs) = $schema->resultset('R2instances')->load_envts_with_instances();
print("count => " . @rs . "\n");
foreach(@rs)
{
   while (($key, $value) = each(%{$_})){
     print $key.", ".$value."\n";
   }
   print("env_id => " . $_->environment->id . "\n");
   print("------------------\n");
}
my(@test);
for($i=1; $i<20; $i++)
{
   push(@test, $i);
}
#my(@test) = [1,2,3,4,5,6,7,8,9,41,42,43,44,45];
print("------------------------------------------------------\n");
my(@envrs) = $schema->resultset('R2environments')->search(
   {
      id => { 
         #-not_in => [1, 2,41,42,43]
         -not_in => \@test
      },
      #id => { 'NOT IN' => $envs_with_apps_rs->get_column('id')->as_query }
   }
);
foreach(@envrs)
{
   #while (($key, $value) = each(%{$_})){
   #  print $key.", ".$value."\n";
   #}
   print("env_id => " . $_->id . "\n");
   #print("------------------\n");
}




exit();
my(%relnums) = undef;

foreach(@rs)
{
   my($subnum) = $_->BUILD_NUM;
   $subnum =~ /^(\d\d)(\d)/;
   $subnum = "$1.$2";
   if(!defined($relnums{$subnum}))
   {
      $relnums{$subnum} = $subnum;
      print("relnums{$subnum} = $subnum\n");
   }
}
