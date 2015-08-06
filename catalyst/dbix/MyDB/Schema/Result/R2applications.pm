package MyDB::Schema::Result::R2applications;

use strict;
use warnings;

use base 'DBIx::Class';

__PACKAGE__->load_components("InflateColumn::DateTime", "TimeStamp", "Core");
__PACKAGE__->table("r2applications");
__PACKAGE__->add_columns(
  "id",
  { data_type => "NUMBER", default_value => undef, is_nullable => 0, size => 38 },
  "name",
  {
    data_type => "VARCHAR2",
    default_value => undef,
    is_nullable => 0,
    size => 30,
  },
);
__PACKAGE__->set_primary_key("id");
__PACKAGE__->has_many(
  "r2instances",
  "MyDB::Schema::Result::R2instances",
  { "foreign.application" => "self.id" },
);


# Created by DBIx::Class::Schema::Loader v0.04006 @ 2009-09-15 09:56:18
# DO NOT MODIFY THIS OR ANYTHING ABOVE! md5sum:eDWWNMuMBHMSrmpnSn0tSA


# Need to tell the Result Class that the id column has a sequence.
__PACKAGE__->add_columns(
   "id",
   {
    data_type => "NUMBER",
    default_value => undef,
    is_nullable => 0,
    size => 38,
    sequence => "r2applications_seq"
   }
);

# Set ResultSet Class
__PACKAGE__->resultset_class('MyDB::Schema::ResultSet::R2applications');
# You can replace this text with custom content, and it will be preserved on regeneration
1;
