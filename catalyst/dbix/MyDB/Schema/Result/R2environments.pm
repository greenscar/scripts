package MyDB::Schema::Result::R2environments;

use strict;
use warnings;

use base 'DBIx::Class';

__PACKAGE__->load_components("InflateColumn::DateTime", "TimeStamp", "Core");
__PACKAGE__->table("r2environments");
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
  { "foreign.environment" => "self.id" },
);


# Created by DBIx::Class::Schema::Loader v0.04006 @ 2009-09-15 09:56:18
# DO NOT MODIFY THIS OR ANYTHING ABOVE! md5sum:6uLOogGmkJgrRYU0kGgT+w


# Need to tell the Result Class that the id column has a sequence.
__PACKAGE__->add_columns(
   "id",
   {
    data_type => "NUMBER",
    default_value => undef,
    is_nullable => 0,
    size => 38,
    sequence => "r2environments_seq"
   }
);

# Set ResultSet Class
__PACKAGE__->resultset_class('MyDB::Schema::ResultSet::R2environments');

# You can replace this text with custom content, and it will be preserved on regeneration
1;
