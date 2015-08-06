package Environments::Controller::DM;

use strict;
use warnings;
use parent 'Catalyst::Controller';

=head1 NAME

Environments::Controller::DM - Catalyst Controller

=head1 DESCRIPTION

Catalyst Controller.

=head1 METHODS

=cut


=head2 base

=cut
sub base :Chained('/') :PathPart('dm') :CaptureArgs(0)
{
   my($self, $c) = @_;
   
   # Store the ResultSet in stash so it's available for other methods
   $c->stash->{resultset} = [$c->model('DB::Deployments')->all];
   
   # Print a message to the debug log
   $c->log->debug('*** INSIDE BASE METHOD ***');
}


=head2 index

=cut

sub index :Path :Args(0) 
{
    my ( $self, $c ) = @_;

    $c->response->body('Matched Environments::Controller::DM in DM.');
}

=head2 listdeployments

=cut
sub listdeployments : Local
{
   my($self, $c) = @_;
   $c->stash->{deployments} = [$c->model('DB::Deployments')->all];
   $c->stash->{template} = 'dm/deploylist.tt2';      
}


=head2 deploy_create

=cut
sub deploy_create :Chained('base') :PathPart('deploy_create') :Args(4) 
{
   my($self, $c, $app, $env, $bl, $bfjobid) = @_;
   # Load or create Application
   my($appobj) = $c->model('DB::Applications')->load_or_create_via_name($app);
   # Load or create Enviornment
   my($envobj) = $c->model('DB::Environments')->load_or_create_via_name($env);
   # Insert deployment into database
   my($deployment) = $c->model('DB::Deployments')->create({
         baseline => $bl,
         timestamp => \'Current_Timestamp',
         application_id => $appobj->id,
         environment_id => $envobj->id,
         bf_job_id => $bfjobid
   });      
   $c->stash->{deployments} = [$c->model('DB::Deployments')->all];
   $c->stash->{template} = 'dm/deploylist.tt2';   
}

=head1 AUTHOR

jsandlin

=head1 LICENSE

This library is free software. You can redistribute it and/or modify
it under the same terms as Perl itself.

=cut

1;
