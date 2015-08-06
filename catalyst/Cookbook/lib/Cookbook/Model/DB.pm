package Cookbook::Model::DB;

use strict;
use base 'Catalyst::Model::DBIC::Schema';

__PACKAGE__->config(
    schema_class => 'Cookbook::Schema',
    connect_info => [
        'dbi:mysql:aubri_cookbook',
        'aubri',
        'aubri',
        
    ],
);

=head1 NAME

Cookbook::Model::DB - Catalyst DBIC Schema Model
=head1 SYNOPSIS

See L<Cookbook>

=head1 DESCRIPTION

L<Catalyst::Model::DBIC::Schema> Model using schema L<Cookbook::Schema>

=head1 AUTHOR

jsandlin

=head1 LICENSE

This library is free software, you can redistribute it and/or modify
it under the same terms as Perl itself.

=cut

1;
