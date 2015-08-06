package MyDB::Schema::ResultSet::Environments;

use strict;
use warnings;
use base 'DBIx::Class::ResultSet';




=head2 load_via_id
=cut
sub load_via_id
{
   my($self, $id) = @_;
   return $self->search({
                              id => { '=' => $id }
                        });
}

=head2 load_via_name
=cut
sub load_via_name
{
   my($self, $name) = @_;
   return $self->search({
                           name => {'like' => "$name" }
   });   
}

=head2 load_or_create_via_name
Search the table for the name provided.
If found, return ID
If not found, insert then return ID
=cut 
sub load_or_create_via_name
{
   my($self, $name) = @_;
   my($rs) = $self->search({
                           name => {'like' => "$name" }
                           });
   if(!$rs)
   {
      $rs = $self->create({
                           name => $name
                          });
   }
   return($rs);
}



1;
