package Environments::Controller::Database;

use strict;
use warnings;
use parent 'Catalyst::Controller';

=head1 NAME

Environments::Controller::Database - Catalyst Controller

=head1 DESCRIPTION

Catalyst Controller.

=head1 METHODS

=cut

=head2 base
Place common logic to start chained dispatch

=cut
sub base :Chained('/') :PathPart('database') :CaptureArgs(0)
{
   my ($self, $c) = @_;
   
   # Store the ResultSet in stash so it's available for other methods
   $c->stash->{db_envts_rs} = $c->model('DB::V_Environments');
   $c->stash->{rel_num_list} = $c->model('DB::V_Environments')->get_rel_num_list($c);
   $c->stash->{app_list} = $c->model('DB::V_Environments')->get_app_list($c);
   
   # Print debug statement
   $c->log->debug('*** INSIDE BASE METHOD ***');
}


=head2 list

=cut
sub list : Chained('base') : PathPart('list')
{
   my($self, $c) = @_;
   my $app = $c->request->params->{app}   || "ALL";
   my $rel = $c->request->params->{rel}   || "ALL";
   $c->stash->{schemas} = [$c->model('DB::V_Environments')->app_release($c, $app, $rel)];
   $c->stash->{app_cur} = $app;
   $c->stash->{rel_cur} = $rel;
   $c->stash->{template} = 'dbs/schema_list.tt2';
   
}

=head2 listall
Load all schema objects & pass to dbs/schema_list.tt2 in stash for display

=cut
sub listall : Chained('base') :PathPart('listall')
{
   my($self, $c) = @_;
   $c->stash->{schemas} = [$c->model('DB::V_Environments')->search()];
   $c->stash->{template} = 'dbs/schema_list.tt2';
}
 
=head2 listapp
List only schemas of provided app

=cut
sub listapp :Chained('base') : PathPart('listapp') : Args(1)  
{   
   my($self, $c, $app) = @_;
   $c->stash->{schemas} = [$c->model('DB::V_Environments')->app_name($app)];
   $c->stash->{template} = 'dbs/schema_list.tt2';
}

=head2 listrel
List only schemas of provided app

=cut
sub listrel :Chained('base') : PathPart('listrel') : Args(1)  
{   
   my($self, $c, $rel) = @_;
   $c->log->debug("rel = $rel");
   $c->stash->{schemas} = [$c->model('DB::V_Environments')->release_num($c, $rel)];
   $c->stash->{template} = 'dbs/schema_list.tt2';
}

=head2 listrelapp

=cut
sub listrelapp  :Chained('base') : PathPart('listrelapp') : Args(2)  
{   
   my($self, $c, $name, $rel) = @_;
   $c->stash->{schemas} = [$c->model('DB::V_Environments')->app_release($c, $name, $rel)];
   $c->stash->{template} = 'dbs/schema_list.tt2';
}
=head1 AUTHOR

James Sandlin,James Sandlin,512-873-6637,James.Sandlin@hhsc.state.tx.us,HHSC,CR52960,

=head1 LICENSE

This library is free software. You can redistribute it and/or modify
it under the same terms as Perl itself.

=cut

1;
