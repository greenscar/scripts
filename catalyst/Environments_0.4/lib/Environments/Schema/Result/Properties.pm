package Environments::Schema::Result::Properties;

use strict;
use warnings;

use base 'DBIx::Class';

__PACKAGE__->load_components("InflateColumn::DateTime", "TimeStamp", "Core");
__PACKAGE__->table("properties");
__PACKAGE__->add_columns(
  "prop_names_id",
  {
    data_type => "NUMBER",
    default_value => undef,
    is_nullable => 0,
    size => 126,
  },
  "files_id",
  {
    data_type => "NUMBER",
    default_value => undef,
    is_nullable => 0,
    size => 126,
  },
  "releases_eid",
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
  "environments_id",
  {
    data_type => "NUMBER",
    default_value => undef,
    is_nullable => 0,
    size => 126,
  },
  "prop_value",
  {
    data_type => "VARCHAR2",
    default_value => undef,
    is_nullable => 1,
    size => 256,
  },
  "date_start",
  {
    data_type => "TIMESTAMP(6)",
    default_value => undef,
    is_nullable => 1,
    size => 11,
  },
);
__PACKAGE__->set_primary_key(
  "prop_names_id",
  "files_id",
  "releases_eid",
  "applications_id",
  "environments_id",
);
__PACKAGE__->belongs_to(
  "releases_eid",
  "Environments::Schema::Result::Releases",
  { id => "releases_eid" },
);
__PACKAGE__->belongs_to(
  "prop_names_id",
  "Environments::Schema::Result::PropNames",
  { id => "prop_names_id" },
);
__PACKAGE__->belongs_to(
  "environments_id",
  "Environments::Schema::Result::Environments",
  { id => "environments_id" },
);
__PACKAGE__->belongs_to(
  "files_id",
  "Environments::Schema::Result::Files",
  { id => "files_id" },
);
__PACKAGE__->belongs_to(
  "applications_id",
  "Environments::Schema::Result::Applications",
  { id => "applications_id" },
);


# Created by DBIx::Class::Schema::Loader v0.04006 @ 2009-09-15 09:56:18
# DO NOT MODIFY THIS OR ANYTHING ABOVE! md5sum:dN7DsuneTfDKJPF76GZTgA


# You can replace this text with custom content, and it will be preserved on regeneration
1;
