package Environments::Schema::Result::Stage_Merge;

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
__PACKAGE__->resultset_class('Environments::Schema::ResultSet::Stage_Merge');

__PACKAGE__->might_have(631 => 'Environments::Schema::Result::AaMigChanges650', 'tiers_id');
__PACKAGE__->might_have(641 => 'Environments::Schema::Result::AaMigChanges650', 'tiers_id');
__PACKAGE__->might_have(650 => 'Environments::Schema::Result::AaMigChanges650', 'tiers_id');
__PACKAGE__->might_have(700 => 'Environments::Schema::Result::AaMigChanges700', 'tiers_id');
#__PACKAGE__->many_to_many(631 => 'Environments::Schema::Result::AaMigChanges650', 'tiers_id');
#__PACKAGE__->many_to_many(641 => 'Environments::Schema::Result::AaMigChanges650', 'tiers_id');
#__PACKAGE__->many_to_many(650 => 'Environments::Schema::Result::AaMigChanges650', 'tiers_id');
#__PACKAGE__->many_to_many(700 => 'Environments::Schema::Result::AaMigChanges700', 'tiers_id');

#__PACKAGE__->has_many(dbs => 'Environments::Schema::Result::V_Ad_Instances','HOST_ID');
1;
