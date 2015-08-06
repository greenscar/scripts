package Cookbook::Schema::Result::Steps;

use strict;
use warnings;

use base 'DBIx::Class';

__PACKAGE__->load_components("InflateColumn::DateTime", "Core");
__PACKAGE__->table("steps");
__PACKAGE__->add_columns(
  "recipe_id",
  { data_type => "INT", default_value => 0, is_nullable => 0, size => 11 },
  "order_num",
  { data_type => "INT", default_value => 0, is_nullable => 0, size => 11 },
  "step_descr",
  {
    data_type => "TEXT",
    default_value => undef,
    is_nullable => 0,
    size => 65535,
  },
);
__PACKAGE__->set_primary_key("recipe_id", "order_num");


# Created by DBIx::Class::Schema::Loader v0.04006 @ 2009-07-12 19:21:41
# DO NOT MODIFY THIS OR ANYTHING ABOVE! md5sum:CH+yhBz6X8vUuZn/UneVnw


# You can replace this text with custom content, and it will be preserved on regeneration
1;
