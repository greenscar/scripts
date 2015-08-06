package Cookbook::Controller::Category;

use strict;
use warnings;
use parent 'Catalyst::Controller';

=head1 NAME

Cookbook::Controller::Category - Catalyst Controller

=head1 DESCRIPTION

Catalyst Controller.

=head1 METHODS

=cut

=head2 base

=cut
sub base : Chained('/') :PathPart('category') :CaptureArgs(0)
{
   my($self, $c) = @_;
   $c->stash->{categories} = [$c->model('DB::Category')->all];
   $c->stash->{template} = 'recipes/categories.tt2';
}

=head2 listall

=cut
sub listall : Chained('base') : PathPart('listall')
{
   my($self, $c) = @_;
   $c->stash->{template} = 'recipes/categories.tt2';
}

=head2 form_create
=cut
sub form_create : Create('base') : PathPart('form_create') : Args(0)
{
   my($self, $c) = @_;
   
   $c->stash->{template} = 'recipes/categories.tt2';
}

=head2 create
Using formfu, create a Category

=cut
sub create : Chained('base') : PathPart('create') : Args(0) : FormConfig
{
   my($self, $c) = @_;
   # Check authentication
   #$c->detach('/error_noperms') unless $book->create_allowed_by($c->user->get_object);
   
   # Get the form that the :FormConfig attribute saved in the stash
   my $form = $c->stash->{form};
   if($form->submitted_and_valid)
   {
      # Create a new Category
      my $category = $c->model('DB::Category')->new_result({});
      # Save the form data for the category
      $form->model->update($category);
      # Set a status message for the user
      $c->flash->{status_msg} = $category->cat_descr . " created";
      # Return to the category list
      $c->response->redirect($c->uri_for($self->action_for('listall')));
      $c->detach;
   }
   else
   {
      
   }
   
   #Set the template
   $c->stash->{template} = 'categories/create.tt2';
}

=head1 AUTHOR

jsandlin

=head1 LICENSE

This library is free software. You can redistribute it and/or modify
it under the same terms as Perl itself.

=cut

1;
