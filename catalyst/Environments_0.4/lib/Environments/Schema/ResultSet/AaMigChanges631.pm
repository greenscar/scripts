package Environments::Schema::ResultSet::AaMigChanges631;

use strict;
use warnings;
use base 'DBIx::Class::ResultSet';

=head2 changes_not_in

=cut
sub changes_not_in
{
   my($self, $c, $other_schema, $rel) = @_;
   $c->log->debug("other_schema = $other_schema");
   $c->log->debug("rel = $rel");
   my($query) = 'tiers_id not in (select tiers_id from ' . $other_schema . ')';
   if($rel =~ /\d+/)
   {
      $query .= ' and build_num = ' . $rel;
   }
   return($self->search_literal($query));
}

=head2 changes_per_release

=cut
sub changes_per_release
{
   my($self, $c, $rel) = @_;
   $c->log->debug("changes_per_release($self, $c, $rel)");
   return $self->search({
                           'build_num' => { '=' => $rel }
                        });
}
1;
