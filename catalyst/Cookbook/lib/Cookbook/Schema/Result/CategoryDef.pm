package Cookbook::Schema::Result::CategoryDef;

use strict;
use warnings;

use base 'DBIx::Class';

__PACKAGE__->load_components("InflateColumn::DateTime", "Core");
__PACKAGE__->table("category_def");
__PACKAGE__->add_columns(
  "cat_id",
  { data_type => "INT", default_value => undef, is_nullable => 0, size => 11 },
  "cat_descr",
  { data_type => "VARCHAR", default_value => "", is_nullable => 0, size => 30 },
);
__PACKAGE__->set_primary_key("cat_id");


# Created by DBIx::Class::Schema::Loader v0.04006 @ 2009-07-12 19:21:41
# DO NOT MODIFY THIS OR ANYTHING ABOVE! md5sum:uqK0YM0on4Z6mDhuhOu8nw

__PACKAGE__->belongs_to(
                           recipe => 'Cookbook::Schema::Result::Recipe',
                           {
                              'foreign.cat_id' => 'self.cat_id'
                           }
                       );
# You can replace this text with custom content, and it will be preserved on regeneration
1;
