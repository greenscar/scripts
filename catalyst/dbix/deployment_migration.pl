use MyDB::Schema;
my $schema = MyDB::Schema->connect('dbi:Oracle:host=165.184.34.104;service_name=DTE2;port=1522', 'ENV_MGT', 'envown123');

# Go through list of deployments in old deployment table.  

my(@deployments) = $schema->resultset('Deployments')->all();
foreach(@deployments)
{
   #Get vars
   my($app) = $_->application;
   my($env) = $_->environment;
   my($bl) = $_->baseline;
   my($time) = $_->timestamp;
   my($bfjob) = $_->bf_job_id;
   
   
   # Check the R2instances table to see if this app / envt exists
   my($instance) = $schema->resultset('R2instances')->search({
                                          application => {'=' => $app->id},
                                          environment => {'=' => $env->id}
                                       });
   # If this app / envt combo does not exist in the R2instances table, insert it.
   if(!$instance)
   {
      $instance = $schema->resultset('R2instances')->create({
            application => $app->id,
            environment => $env->id
      });
   }
   
   # Make sure it was inserted.
   if(!$instance)
   {
      print("ERROR!!! Could not insert your instance or find it!!!!!!\n");
      exit(0);
   }
   
   #Now that we have our instance, we need to insert the deployment into R2deployments
   
   # 1) as this script will be run multiple times, lets make sure this row doesn't already exist.
   my($deployment) = $schema->resultset('R2deployments')->search({
         baseline => { 'like' => $bl},
         timestamp => { '=' => $time},
         instance => { '=' => $instance->id},
         bf_job => { '=' => $bfjob}
   });
   
   if(!$deployment)
   {
      $deployment = $schema->resultset('R2deployments')->create({
            baseline => $bl,
            timestamp => $time,
            instance => $instance->id,
            bf_job => $bfjob
      });
   }
      
   
   
   #print($app->id . ":" . $app->name . " -> " . $env->id . ":" . $env->name . "\n");
   
}
