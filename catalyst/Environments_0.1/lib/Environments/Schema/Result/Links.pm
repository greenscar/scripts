package Environments::Schema::Result::Links;

use strict;
use warnings;

use base 'DBIx::Class';

__PACKAGE__->load_components("InflateColumn::DateTime", "TimeStamp", "Core");
__PACKAGE__->table("links");
__PACKAGE__->add_columns(
  "environments_id",
  {
    data_type => "NUMBER",
    default_value => undef,
    is_nullable => 0,
    size => 126,
  },
  "applications_id",
  {
    data_type => "NUMBER",
    default_value => undef,
    is_nullable => 0,
    size => 126,
  },
  "link",
  {
    data_type => "VARCHAR2",
    default_value => undef,
    is_nullable => 1,
    size => 45,
  },
);
__PACKAGE__->set_primary_key("environments_id", "applications_id");
__PACKAGE__->belongs_to(
  "environments_id",
  "Environments::Schema::Result::Environments",
  { id => "environments_id" },
);
__PACKAGE__->belongs_to(
  "applications_id",
  "Environments::Schema::Result::Applications",
  { id => "applications_id" },
);


# Created by DBIx::Class::Schema::Loader v0.04006 @ 2009-07-30 14:17:44
# DO NOT MODIFY THIS OR ANYTHING ABOVE! md5sum:1t3UxjoeLeOCL4ItHVhxbw


# You can replace this text with custom content, and it will be preserved on regeneration
1;
