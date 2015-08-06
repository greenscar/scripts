package Cookbook::Controller::Cookbook;

use strict;
use warnings;
use parent 'Catalyst::Controller';

=head1 NAME

Cookbook::Controller::Cookbook - Catalyst Controller

=head1 DESCRIPTION

Catalyst Controller.

=head1 METHODS

=cut


=head2 index

=cut

sub index :Path :Args(0) {
    my ( $self, $c ) = @_;

    $c->response->body('Matched Cookbook::Controller::Cookbook in Cookbook.');
}


=head1 AUTHOR

jsandlin

=head1 LICENSE

This library is free software. You can redistribute it and/or modify
it under the same terms as Perl itself.

=cut

1;
