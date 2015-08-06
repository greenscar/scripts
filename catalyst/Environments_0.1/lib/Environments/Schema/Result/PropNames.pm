package Environments::Schema::Result::PropNames;

use strict;
use warnings;

use base 'DBIx::Class';

__PACKAGE__->load_components("InflateColumn::DateTime", "TimeStamp", "Core");
__PACKAGE__->table("prop_names");
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
    size => 80,
  },
);
__PACKAGE__->set_primary_key("id");
__PACKAGE__->has_many(
  "properties",
  "Environments::Schema::Result::Properties",
  { "foreign.prop_names_id" => "self.id" },
);


# Created by DBIx::Class::Schema::Loader v0.04006 @ 2009-07-30 14:17:44
# DO NOT MODIFY THIS OR ANYTHING ABOVE! md5sum:yM7pwv9wv4QY8jMg9MG8Bw


# You can replace this text with custom content, and it will be preserved on regeneration
1;
