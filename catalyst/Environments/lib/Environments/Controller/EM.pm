package Environments::Controller::EM;

use strict;
use warnings;
use parent 'Catalyst::Controller::HTML::FormFu';

=head1 NAME

Environments::Controller::EM - Catalyst Controller

=head1 DESCRIPTION

Catalyst Controller.

=head1 METHODS

=cut

=head2 base

=cut
sub base :Chained('/') :PathPart('em') :CaptureArgs(0)
{
   my($self, $c) = @_;
   
   # Store the ResultSet in stash so it's available for other methods
   #$c->stash->{resultset} = [$c->model('DB::R2deployments')->all];
   $c->stash->{environments} = $c->model('DB::R2environments');
   # Print a message to the debug log
   $c->log->debug('*** INSIDE BASE METHOD ***');
}


=head2 index

=cut

sub index :Path :Args(0) {
    my ( $self, $c ) = @_;
    $c->response->body('Matched Environments::Controller::EM in EM.');
}

=head2 environment
Fetch the specified environment object based on the ID & store it in stash

=cut
sub environment :Chained('base'): PathPart('id'):CaptureArgs(1)
{
   my($self, $c, $id) = @_;
   
   $c->log->debug("EM->environment($id)");
   
   $c->stash(envt => $c->stash->{environments}->find($id));
   
   die "Envt ID $id not found!" if !$c->stash->{envt};
   
}


=head2 instance_report
As an environment has a set of instances, the instance report will be loaded
via the environment manager controller
=cut
sub instance_report :Chained('base'): PathPart('instance_report'): Args(0)#: FormConfig
{
   my($self, $c) = @_;
   $c->log->debug("EM->instance_report");
   # Load a list of all environments with instances.
   my(@envs) = $c->model('DB::R2environments')->load_with_instances($c);
   foreach(@envs)
   {
      #my($env) = @_;
      $c->log->debug("-----------------------------");
      $c->log->debug($_);
      $c->log->debug("env name => " . $_->name); 
      
      foreach($_->r2instances)
      {
         #$c->log->debug("env:app => " . $_->environment->name . ":" . $_->application->name);
         #$c->log->debug("Deployments:");
         #my($maxts);
         #foreach($_->r2deployments)
         #{
         #   if($_->timestamp > $maxts)
         #   {
         #      $c->log->debug($_->timestamp . " => " . $_->baseline);
         #   }
         #}
         #$c->log->debug("END Deployments:");
         $_->set_latest_deployment($c);
      }
      $c->log->debug("-----------------------------");
   }
   $c->stash->{envs} = \@envs;
   $c->log->debug("END EM->instance_report");
   $c->stash->{template} = 'em/instance_report.tt2';
}

=head2 instances_edit
Modify the instances which are in this environment & their links. 
Note: This requires a custom form so formfu will not do the job.
=cut
sub instances_edit : Chained('environment') : PathPart('instances_edit') : Args(0)
{
   my($self, $c) = @_;
   
   # Get the specified environment already saved via the 'environment' method
   my($envt) = $c->stash->{envt};
   
   #Make sure we were able to load an envt via the provided envt id
   unless($envt)
   {
      $c->flash->{error_msg} = "Invalid Environment -- Cannot Edit";
      $c->response->redirect($c->uri_for($self->action_for('instance_report')));
      $c->detach;
   }
   
   my(@app_objs) = $c->model('DB::R2applications')->all();
   $c->stash->{envt} = $envt;
   $c->stash->{apps} = \@app_objs;
   $c->stash->{template} = 'em/instances_edit.tt2';
   
}


=head2 instances_edit_process
#Process the data entered in teh instances_edit form from instances_edit method.
=cut
sub instances_edit_process : Chained('environment') : PathPart('instances_edit_process') : Args(0)
{
   my($self, $c) = @_;
   $c->log->debug("EM->instances_edit_process");
   
   my($envt) = $c->stash->{envt};
   
   #Make sure we were able to load an envt via the provided envt id
   unless($envt)
   {
      $c->flash->{error_msg} = "Invalid Environment -- Cannot Edit";
      $c->response->redirect($c->uri_for($self->action_for('instance_report')));
      $c->detach;
   }
   
   # The form contains a list of app id's followed by _check or _link
   # These app id's are dynamic based on what app ids exist in the DB @ the time
   # the request for the form was submitted. These id's were pulled from teh DB
   # via the instances_edit method.
   # Thus, we need to iterate through all existing applications & see which ones are defined.
   # Load apps from DB
   my(@applist) = $c->model('DB::R2applications')->all();
   # Iterate through this list of apps & check the web form for these values.
   foreach(@applist)
   {
      my($appid) = $_->id;
      my($checkkey) = $appid . "_check";
      #$c->log->debug("checkkey = $checkkey");
      my($checkval) = $c->request->params->{$checkkey};
      #$c->log->debug("'$checkkey' = " . $checkval);
      
      my($linkkey) = $appid . "_link";
      #$c->log->debug("linkkey = $linkkey");
      my($linkval) = $c->request->params->{$linkkey};
      #$c->log->debug("'$linkkey' = " . $linkval);
      
      my($linkpubkey) = $appid . "_linkpub";
      my($linkpub) = $c->request->params->{$linkpubkey};
      if($linkpub =~ /^on$/)
      {
         $linkpub = 1;
      }
      else
      {
         $linkpub = 0;
      }
      my($instancers) = undef; 
      #$c->log->debug($c->model('DB::R2instances')->app_env_exists($c, $appid, $envt->id));
      $instancers = $c->model('DB::R2instances')->load_via_app_and_env($c, $appid, $envt->id);
      #$c->log->debug("count = " . $instancers->count);
      
      # If the checkmark was checked or link was entered, continue.
      if($checkval || ($linkval !~ /^\s*$/))
      {
         # Look @ the instances table @ the envt id and this app id.
         # If the instance exists, update the link
         # If not, insert.
         if($instancers->count > 0)
         {
            # ENVT / APP combo exists. 
            # Thus: Update
            $instancers->update({
                  link => $linkval,
                  link_public => $linkpub
            });
         }
         else
         {
            # ENVT / APP combo does NOT exist. 
            # Thus: Insert
            $c->log->debug("inside else\n");
            $c->model('DB::R2instances')->create({
                  environment => $envt->id,
                  application => $appid,
                  link => $linkval,
                  link_public => $linkpub
            });
         }
      }
      else
      {
         $c->log->debug("Deleting app");
         # If the checkmark was not checked & there was no link entered, ensure
         #  this app / envt combo does not exist in the instances table
         $instancers->delete;
      }
   }
   
   $c->response->redirect($c->uri_for($self->action_for('instance_report')));
   $c->detach;
   #$c->stash->{template} = 'em/instance_report.tt2';
   $c->log->debug("END EM->instances_edit_process");
}

=head1 AUTHOR

jsandlin

=head1 LICENSE

This library is free software. You can redistribute it and/or modify
it under the same terms as Perl itself.

=cut

1;
