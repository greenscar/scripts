package Environments::Schema::Result::Services;

use strict;
use warnings;

use base 'DBIx::Class';

__PACKAGE__->load_components("InflateColumn::DateTime", "TimeStamp", "Core");
__PACKAGE__->table("services");
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
  "sub_url",
  {
    data_type => "VARCHAR2",
    default_value => undef,
    is_nullable => 1,
    size => 45,
  },
  "provider_app_id",
  {
    data_type => "NUMBER",
    default_value => undef,
    is_nullable => 1,
    size => 126,
  },
  "consumer_app_id",
  {
    data_type => "NUMBER",
    default_value => undef,
    is_nullable => 1,
    size => 126,
  },
);
__PACKAGE__->set_primary_key("id");
__PACKAGE__->belongs_to(
  "provider_app_id",
  "Environments::Schema::Result::Applications",
  { id => "provider_app_id" },
);
__PACKAGE__->belongs_to(
  "consumer_app_id",
  "Environments::Schema::Result::Applications",
  { id => "consumer_app_id" },
);


# Created by DBIx::Class::Schema::Loader v0.04006 @ 2009-09-15 09:56:18
# DO NOT MODIFY THIS OR ANYTHING ABOVE! md5sum:gt6SLqV5UWTPXdE58iASqg


# You can replace this text with custom content, and it will be preserved on regeneration
1;
