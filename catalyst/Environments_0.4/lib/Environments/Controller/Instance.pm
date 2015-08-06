package Environments::Controller::Instance;

use strict;
use warnings;
use parent 'Catalyst::Controller::HTML::FormFu';

=head1 NAME

Environments::Controller::Instance - Catalyst Controller

=head1 DESCRIPTION

Catalyst Controller.

=head1 METHODS

=cut

#############################################################
=head2 index
The default method created
=cut
sub index :Path :Args(0) {
    my ( $self, $c ) = @_;

    $c->response->body('Matched Environments::Controller::Instance in Instance.');
}

=head2 list
Load all instances and pass to instances/list.tt2 to be displayed
=cut 
sub list : Local
{
   my($self, $c) = @_;
   $c->stash->{instances} = [$c->model('DB::R2instances')->all];
   $c->stash->{template} = 'instance/list.tt2';
   
}
#############################################################

=head2 base

=cut
sub base : Chained('/') : PathPart('instance') : CaptureArgs(0)
{
   my($self, $c) = @_;
   # Print a message to the debug log
   $c->log->debug('*** Instance->base START ***');
   
   # Store the ResultSet in stash so it's available for other methods
   $c->stash->{instance_rs} = $c->model('DB::R2instances');
   
   # Print a message to the debug log
   $c->log->debug('*** Instance->base END ***');
}

#############################################################
=head2 object
Fetch the specified Instance object based on provided id & store it in the stash

=cut
sub object : Chained('base') : PathPart('id'): CaptureArgs(1)
{
   my($self, $c, $id) = @_;
   
   $c->log->debug("Instance->object($id)");
   
   $c->stash(instance => $c->stash->{instance_rs}->find($id));
   
   # Ensure lookup was successful. If not, error out.
   # $c->detach('/error_404') if !$c->stash->{object};
   die "Instance id $id not found!" if !c->stash->{instance};
}

#############################################################
=head2 create

=cut 
sub create : Chained('base') : PathPart('create'): Args(0): FormConfig
{
   my($self, $c) = @_;
   my($form) = $c->stash->{form};
   if($form->submitted_and_valid)
   {
      my($envid) = $c->request->params->{env};
      my($appref) = $c->request->params->{apps};
      $c->log->debug("appref = $appref");
      #
      
      my(@apps);
      #$c->log->debug("ref $appref eq 'ARRAY' => " . ref $appref eq 'ARRAY');
      if(ref $appref)
      {
         @apps = @{$appref};
      }
      else
      {
         # It's an integer. Create an array with 1 element
         push(@apps, $appref);
      }
      
      #my(@apps) = @{$c->request->params->{apps}};
      foreach my $app (@apps)
      {
         $c->log->debug("insert $app");
         my($instance) = $c->model('DB::R2instances')->create({
               application => $app,
               environment => $envid
         });
      }
      $c->response->redirect($c->uri_for($c->controller('em')->action_for('instance_report')));
      $c->detach;
   }
   else
   {
      #
      # Load list of applications & add to form
      #
      my(@app_objs) = $c->model('DB::R2applications')->all;
      #foreach(@app_objs)
      #{
      #   print($_->id . "\n");
      #}
      my(@apps);
      foreach(sort{$a->name cmp $b->name} @app_objs)
      {
         #print($_->id . " -> " .  $_->name . "\n");
         push(@apps, [$_->id, $_->name]);
      }
      $c->stash->{apps} = @apps;
      my($select_app) = $form->get_element({name => 'apps'});
      $select_app->options(\@apps);
      
      #
      # Load list of environments & add to form
      #
      my(@env_objs) = $c->model('DB::R2environments')->load_with_no_instances($c);
      #my(@env_objs) = $c->model('DB::R2environments')->all;
      #$c->log->debug(@env_objs);
      my(@envs);
      foreach(sort{$a->name cmp $b->name} @env_objs)
      {
         push(@envs, [$_->id, $_->name]);
      }
      my($select_env) = $form->get_element({name => 'env'});
      $select_env->options(\@envs);
      
      $c->stash->{template} = 'instance/create.tt2';
   }
   
}

#############################################################


=head1 AUTHOR

jsandlin

=head1 LICENSE

This library is free software. You can redistribute it and/or modify
it under the same terms as Perl itself.

=cut

1;
