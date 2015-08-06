package Cookbook::Schema::Result::Users;

use strict;
use warnings;

use base 'DBIx::Class';

__PACKAGE__->load_components("InflateColumn::DateTime", "Core");
__PACKAGE__->table("users");
__PACKAGE__->add_columns(
  "id",
  { data_type => "VARCHAR", default_value => "", is_nullable => 0, size => 15 },
  "pwd",
  { data_type => "VARCHAR", default_value => "", is_nullable => 0, size => 15 },
  "first_name",
  { data_type => "VARCHAR", default_value => "", is_nullable => 0, size => 20 },
  "last_name",
  { data_type => "VARCHAR", default_value => "", is_nullable => 0, size => 20 },
  "email",
  { data_type => "VARCHAR", default_value => "", is_nullable => 0, size => 30 },
);
__PACKAGE__->set_primary_key("id");


# Created by DBIx::Class::Schema::Loader v0.04006 @ 2009-07-12 19:21:41
# DO NOT MODIFY THIS OR ANYTHING ABOVE! md5sum:nKd3W+mtiZUObVIsC8A8nA


# You can replace this text with custom content, and it will be preserved on regeneration
1;
