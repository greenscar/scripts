package Environments::Schema::Result::Author;

use strict;
use warnings;

use base 'DBIx::Class';

__PACKAGE__->load_components("InflateColumn::DateTime", "TimeStamp", "Core");
__PACKAGE__->table("author");
__PACKAGE__->add_columns(
  "id",
  { data_type => "NUMBER", default_value => undef, is_nullable => 0, size => 38 },
  "first_name",
  {
    data_type => "VARCHAR2",
    default_value => undef,
    is_nullable => 1,
    size => 80,
  },
  "last_name",
  {
    data_type => "VARCHAR2",
    default_value => undef,
    is_nullable => 1,
    size => 80,
  },
);
__PACKAGE__->set_primary_key("id");


# Created by DBIx::Class::Schema::Loader v0.04006 @ 2009-09-15 09:56:18
# DO NOT MODIFY THIS OR ANYTHING ABOVE! md5sum:B8a/wmu/YxVDPPIhlxrNwg


# You can replace this text with custom content, and it will be preserved on regeneration
1;
