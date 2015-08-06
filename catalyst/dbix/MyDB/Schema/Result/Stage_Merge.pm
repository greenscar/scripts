package MyDB::Schema::Result::Stage_Merge;

use strict;
use warnings;

use base 'DBIx::Class';


__PACKAGE__->load_components("InflateColumn::DateTime", "Core");
__PACKAGE__->table("stage_merge");
__PACKAGE__->add_columns(
   "tiers_id",
   { 
      data_type => "VARCHAR2",
      default_value => undef,
      is_nullable => 0,
      size => 30
   },
   "build_num",
   {
      data_type => "NUMBER",
      default_value => undef,
      is_nullable => 0,
      size => 38
   },
   "track",
   {
      data_type => "VARCHAR2",
      default_value => undef,
      is_nullable => 1,
      size => 2
   },
   "unix_loc",
   {
      data_type => "VARCHAR2",
      default_value => undef,
      is_nullable => 1,
      size => 100
   }
);
__PACKAGE__->set_primary_key("tiers_id");

#
# Set ResultSet Class
#
__PACKAGE__->resultset_class('MyDB::Schema::ResultSet::Stage_Merge');

__PACKAGE__->might_have(631 => 'MyDB::Schema::Result::AaMigChanges631', 'tiers_id');
__PACKAGE__->might_have(641 => 'MyDB::Schema::Result::AaMigChanges641', 'tiers_id');
__PACKAGE__->might_have(650 => 'MyDB::Schema::Result::AaMigChanges650', 'tiers_id');
__PACKAGE__->might_have(700 => 'MyDB::Schema::Result::AaMigChanges700', 'tiers_id');


=head2 file_name
=cut
sub file_name
{
   my($self) = @_;
   my($loc) = rindex($self->unix_loc, "/");
   return(substr($self->unix_loc, $loc + 1));
   #return $self->unix_loc;
}

1;
