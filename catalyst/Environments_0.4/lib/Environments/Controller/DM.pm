package Environments::Controller::DM;

use strict;
use warnings;
use parent 'Catalyst::Controller::HTML::FormFu';

=head1 NAME

Environments::Controller::DM - Catalyst Controller

=head1 DESCRIPTION

Catalyst Controller.

=head1 METHODS

=cut

=head2 index

=cut

sub index :Path :Args(0) 
{
    my ( $self, $c ) = @_;

    $c->response->body('Matched Environments::Controller::DM in DM.');
}

=head2 base

=cut
sub base :Chained('/') :PathPart('dm') :CaptureArgs(0)
{
   my($self, $c) = @_;
   
   # Store the ResultSet in stash so it's available for other methods
   $c->stash->{resultset} = [$c->model('DB::R2deployments')->all];
   
   # Print a message to the debug log
   $c->log->debug('*** INSIDE BASE METHOD ***');
}



#=head2 listall
#
#=cut
#sub listall : Chained('base'): PathPart('listall')
#{
#   my($self, $c) = @_;
#   $c->stash->{deployments} = [$c->model('DB::R2deployments')->all];
#   $c->stash->{template} = 'dm/dep_report.tt2';      
#}

=head2 report
Report all deployments done to the selected environment / app
=cut 
sub dep_report :Chained('base'): PathPart('report'): Args(0): FormConfig
{
   my($self, $c) = @_;
   my($form) = $c->stash->{form};
   if($form->submitted_and_valid)
   {
      my($envid) = $c->request->params->{env};
      my($appid) = $c->request->params->{app};
      $c->log->debug("envid = '$envid' && appid = '$appid'");
      #my(@changes) = $c->model('DB::R2deployments')->load_via_app_and_env($c, $appid, $envid);
      $c->stash->{deployments} = [$c->model('DB::R2deployments')->load_via_app_and_env($c, $appid, $envid)];
      # These vals should be removed as they are in the deployments stash variable.
      $c->stash->{app} = $appid;
      $c->stash->{env} = $envid;
      $c->stash->{template} = 'dm/dep_report.tt2';
   }
   else
   {
      #
      # Load list of applications & add to form
      #
      my(@app_objs) = $c->model('DB::R2applications')->all;
      my(@apps);
      push(@apps, ["ALL", "ALL"]);
      foreach(sort{$a->name cmp $b->name} @app_objs)
      {
         push(@apps, [$_->id, $_->name]);
      }
      my($select_app) = $form->get_element({name => 'app'});
      $select_app->options(\@apps);
      
      #
      # Load list of environments & add to form
      #
      my(@env_objs) = $c->model('DB::R2environments')->all;
      my(@envs);
      push(@envs, ["ALL", "ALL"]);
      foreach(sort{$a->name cmp $b->name} @env_objs)
      {
         push(@envs, [$_->id, $_->name]);
      }
      my($select_env) = $form->get_element({name => 'env'});
      $select_env->options(\@envs);
      
      # Set template
      $c->stash->{template} = 'dm/dep_report.tt2';
   }
}

=head2 deploy_create

=cut
sub deploy_create :Chained('base') :PathPart('deploy_create') :Args(4) 
{
   my($self, $c, $app, $env, $bl, $bfjobid) = @_;
   # Load or create Application
   #my($appobj) = $c->model('DB::R2applications')->load_or_create_via_name($app);
   my($appobj) = $c->model('DB::R2applications')->load_or_create_via_name($app);
   
   # Load or create Enviornment
   #my($envobj) = $c->model('DB::R2environments')->load_or_create_via_name($env);
   my($envobj) = $c->model('DB::R2environments')->load_or_create_via_name($env);
   
   # Load or create instance based on appobj & envobj
   my($instance) = $c->model('DB::R2instances')->load_or_create_via_app_env($c, $appobj->id, $envobj->id);
   
   # Insert deployment into database
   my($deployment) = $c->model('DB::R2deployments')->create({
         baseline => $bl,
         timestamp => \'Current_Timestamp',
         instance => $instance->id,
         bf_job => $bfjobid
   });      
   $c->stash->{deployments} = [$c->model('DB::R2deployments')->all];
   #$c->response->redirect($c->uri_for($c->controller('em')->action_for('instance_report')));
   $c->response->redirect($c->uri_for($self->action_for('dep_report')));
   $c->detach;   
}

=head1 AUTHOR

jsandlin

=head1 LICENSE

This library is free software. You can redistribute it and/or modify
it under the same terms as Perl itself.

=cut

1;
