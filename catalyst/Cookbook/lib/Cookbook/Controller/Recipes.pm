package Cookbook::Controller::Recipes;

use strict;
use warnings;
use parent 'Catalyst::Controller';

=head1 NAME

Cookbook::Controller::Recipes - Catalyst Controller

=head1 DESCRIPTION

Catalyst Controller.

=head1 METHODS

=cut


=head2 index

=cut

sub index :Path :Args(0) {
    my ( $self, $c ) = @_;

    $c->response->body('Matched Cookbook::Controller::Recipes in Recipes.');
}

=head2 base
    
Can place common logic to start chained dispatch here

=cut

sub base :Chained('/') :PathPart('recipes') :CaptureArgs(0) {
   my ($self, $c) = @_;
   
   # Store the ResultSet in stash so it's available for other methods
   $c->stash->{resultset} = $c->model('DB::Recipes');
   
   # Print a message to the debug log
   $c->log->debug('*** INSIDE BASE METHOD ***');
}
############################################

=head2 list
Load all recipes & pass to recipes/list.tt2 in stash

=cut
sub list : Chained('/') :PathPart('recipes/list')
{
   my ($self, $c, $sortby) = @_;
   $c->log->debug("sortby = $sortby");
   if($sortby)
   {
      $c->log->debug("sortby = $sortby");
      #$c->stash->{recipes} = [$c->model('DB::CategoryDef')->search({cat_descr => { 'like', '%Sides%'}})];
      $c->stash->{recipes} = [$c->model('DB::Recipes')->all];
   }
   else
   {
      $c->stash->{recipes} = [$c->model('DB::Recipes')->all];
   }
   #$c->model('DB::Book')->search({}, {order_by => 'title DESC'});
   #$c->stash('DB::Recipes')->search({}, {order_by => 'name'});
   $c->stash->{template} = 'recipes/list.tt2';
}

############################################

=head1 AUTHOR

jsandlin

=head1 LICENSE

This library is free software. You can redistribute it and/or modify
it under the same terms as Perl itself.

=cut

1;
