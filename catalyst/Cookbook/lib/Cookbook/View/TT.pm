package Cookbook::View::TT;

use strict;
use base 'Catalyst::View::TT';

__PACKAGE__->config(
               # Change Default TT extension
               TEMPLATE_EXTENSION   => '.tt2',
               # Change Location of TT files
               INCLUDE_PATH         => 
                                       [
                                          Cookbook->path_to('root', 'src')
                                       ],
               # Set to 1 for detailed timer stats in HTML as comments
               TIMER                => 1,
               # Set the wrapper template located in 'root/src'
               WRAPPER              => 'wrapper.tt2'
            );

=head1 NAME

Cookbook::View::TT - TT View for Cookbook

=head1 DESCRIPTION

TT View for Cookbook. 

=head1 SEE ALSO

L<Cookbook>

=head1 AUTHOR

jsandlin

=head1 LICENSE

This library is free software, you can redistribute it and/or modify
it under the same terms as Perl itself.

=cut

1;
