package Cookbook::Schema::Result::ItemDef;

use strict;
use warnings;

use base 'DBIx::Class';

__PACKAGE__->load_components("InflateColumn::DateTime", "Core");
__PACKAGE__->table("item_def");
__PACKAGE__->add_columns(
  "item_id",
  { data_type => "INT", default_value => undef, is_nullable => 0, size => 11 },
  "item_descr",
  { data_type => "VARCHAR", default_value => "", is_nullable => 0, size => 200 },
);
__PACKAGE__->set_primary_key("item_id");


# Created by DBIx::Class::Schema::Loader v0.04006 @ 2009-07-12 19:21:41
# DO NOT MODIFY THIS OR ANYTHING ABOVE! md5sum:/iNlV4iA84P9BDIfvuAw0A

__PACKAGE__->belongs_to(
                           ingredients => 'Cookbook::Schema::Result::Ingredients',
                           {
                              'foreign.item_id' => 'self.item_id'
                           }
                       );

# You can replace this text with custom content, and it will be preserved on regeneration
1;
