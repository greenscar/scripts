package MyApp::View::TT;

use strict;
use base 'Catalyst::View::TT';

__PACKAGE__->config(        # Change default TT extension
   TEMPLATE_EXTENSION => '.tt2',
   # Set the location for TT files
   INCLUDE_PATH => [
      MyApp->path_to( 'root', 'src' ),
   ],        
   # Set to 1 for detailed timer stats in your HTML as comments
   TIMER              => 0,
   # This is your wrapper template located in the 'root/src'
   WRAPPER => 'wrapper.tt2',
);

=head1 NAME

MyApp::View::TT - TT View for MyApp

=head1 DESCRIPTION

TT View for MyApp. 

=head1 SEE ALSO

L<MyApp>

=head1 AUTHOR

jsandlin

=head1 LICENSE

This library is free software, you can redistribute it and/or modify
it under the same terms as Perl itself.

=cut

1;