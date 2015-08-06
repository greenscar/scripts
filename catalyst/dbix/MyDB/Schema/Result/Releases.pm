package MyDB::Schema::Result::Releases;

use strict;
use warnings;

use base 'DBIx::Class';

__PACKAGE__->load_components("InflateColumn::DateTime", "TimeStamp", "Core");
__PACKAGE__->table("releases");
__PACKAGE__->add_columns(
  "id",
  {
    data_type => "NUMBER",
    default_value => undef,
    is_nullable => 0,
    size => 126,
  },
  "name",
  {
    data_type => "VARCHAR2",
    default_value => undef,
    is_nullable => 1,
    size => 45,
  },
);
__PACKAGE__->set_primary_key("id");
__PACKAGE__->has_many(
  "properties",
  "MyDB::Schema::Result::Properties",
  { "foreign.releases_eid" => "self.id" },
);


# Created by DBIx::Class::Schema::Loader v0.04006 @ 2009-09-15 09:56:18
# DO NOT MODIFY THIS OR ANYTHING ABOVE! md5sum:rDxwUOAyYSWlPkGgOKzcYg


# You can replace this text with custom content, and it will be preserved on regeneration
1;
