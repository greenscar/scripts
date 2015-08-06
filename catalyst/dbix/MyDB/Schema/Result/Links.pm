package MyDB::Schema::Result::Links;

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
  "MyDB::Schema::Result::Environments",
  { id => "environments_id" },
);
__PACKAGE__->belongs_to(
  "applications_id",
  "MyDB::Schema::Result::Applications",
  { id => "applications_id" },
);


# Created by DBIx::Class::Schema::Loader v0.04006 @ 2009-08-26 15:49:48
# DO NOT MODIFY THIS OR ANYTHING ABOVE! md5sum:ZbQ5F/JGjG0QC4lPfC2xlQ


# You can replace this text with custom content, and it will be preserved on regeneration
1;
