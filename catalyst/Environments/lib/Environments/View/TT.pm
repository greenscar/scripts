package Environments::View::TT;

use strict;
use base 'Catalyst::View::TT';

__PACKAGE__->config(
                     # Set default extension
                     TEMPLATE_EXTENSION => '.tt2',
                     # Set location for TT files
                     INCLUDE_PATH => [
                                       Environments->path_to('root', 'src')
                                     ],
                     # Set to 1 for detailed timer stats in your HTML as comments
                     TIMER => 0,
                     # This is your wrapper template located in root/src
                     WRAPPER => 'wrapper.tt2'
                   );

=head1 NAME

Environments::View::TT - TT View for Environments

=head1 DESCRIPTION

TT View for Environments. 

=head1 SEE ALSO

L<Environments>

=head1 AUTHOR

James Sandlin,James Sandlin,512-873-6637,James.Sandlin@hhsc.state.tx.us,HHSC,CR52960,

=head1 LICENSE

This library is free software, you can redistribute it and/or modify
it under the same terms as Perl itself.

=cut

1;
