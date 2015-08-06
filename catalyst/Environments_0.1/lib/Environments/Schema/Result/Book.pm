package Environments::Schema::Result::Book;

use strict;
use warnings;

use base 'DBIx::Class';

__PACKAGE__->load_components("InflateColumn::DateTime", "TimeStamp", "Core");
__PACKAGE__->table("book");
__PACKAGE__->add_columns(
  "id",
  { data_type => "NUMBER", default_value => undef, is_nullable => 0, size => 38 },
  "title",
  {
    data_type => "VARCHAR2",
    default_value => undef,
    is_nullable => 1,
    size => 255,
  },
  "rating",
  { data_type => "NUMBER", default_value => undef, is_nullable => 1, size => 38 },
);
__PACKAGE__->set_primary_key("id");


# Created by DBIx::Class::Schema::Loader v0.04006 @ 2009-07-30 14:17:44
# DO NOT MODIFY THIS OR ANYTHING ABOVE! md5sum:Xw5Y+NWlRGaMPTNTR3yqjw


# You can replace this text with custom content, and it will be preserved on regeneration
1;
