package Environments::Schema::ResultSet::Stage_Merge;

use strict;
use warnings;
use base 'DBIx::Class::ResultSet';

=head2 release_num
Load migration scripts executed for a particular release

=cut
sub release_num
{
   my($self, $c, $rel) = @_;
   $c->log->debug("Stage_Merge->release_num($self, $c, $rel)");
   if($rel =~ /^ALL$/)
   {
      return $self->search();
   }
   else
   {
      $rel = $self->prep_rel_num($rel);
      return $self->search({
                           BUILD_NUM => { '=' => $rel }
                           });
   }
}

=head2 schema

=cut
sub schema
{
   my($self, $c, $id) = @_;
   return $self->search(
                           undef,
                           {
                              $id => defined
                           }
                         );
}

sub file_name
{
   my($self) = @_;
   return $self->unix_loc;
}
# Allow user to enter release number as XX or XX.X
# May be 75 or 75.0 or 75.2
sub prep_rel_num
{
   my($self, $rel) = @_;
   if($rel =~ /^(\d\d)\.(\d)$/)
   {
      $rel = $1 . $2;
   }
   elsif($rel =~ /^(\d\d)$/)
   {
      $rel = $1 . "0";
   }
   return($rel);
}


=head1 AUTHOR

James Sandlin,James Sandlin,512-873-6637,James.Sandlin@hhsc.state.tx.us,HHSC,CR52960,

=head1 LICENSE

This library is free software. You can redistribute it and/or modify
it under the same terms as Perl itself.

=cut
1;
