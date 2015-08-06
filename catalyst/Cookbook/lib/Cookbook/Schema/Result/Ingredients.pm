package Cookbook::Schema::Result::Ingredients;

use strict;
use warnings;

use base 'DBIx::Class';

__PACKAGE__->load_components("InflateColumn::DateTime", "Core");
__PACKAGE__->table("ingredients");
__PACKAGE__->add_columns(
  "recipe_id",
  { data_type => "INT", default_value => 0, is_nullable => 0, size => 11 },
  "quantity",
  {
    data_type => "VARCHAR",
    default_value => "N/A",
    is_nullable => 0,
    size => 10,
  },
  "msrmnt_id",
  { data_type => "INT", default_value => 0, is_nullable => 1, size => 11 },
  "item_id",
  { data_type => "INT", default_value => 0, is_nullable => 0, size => 11 },
  "order_num",
  { data_type => "INT", default_value => 0, is_nullable => 0, size => 11 },
);
__PACKAGE__->set_primary_key("recipe_id", "order_num");


# Created by DBIx::Class::Schema::Loader v0.04006 @ 2009-07-12 19:21:41
# DO NOT MODIFY THIS OR ANYTHING ABOVE! md5sum:zPd4TY3Wg2yh1Y8dHj3ERw

__PACKAGE__->belongs_to(
                           recipe => 'Cookbook::Schema::Result::Recipes',
                           {
                              'foreign.recipe_id' => 'self.recipe_id'
                           }
                       );
__PACKAGE__->has_one(
                        measurement => 'Cookbook::Schema::Result::MsrmntDef',
                        {
                           'foreign.msrmnt_id' => 'self.msrmnt_id'
                        }
                     );

__PACKAGE__->has_one(
                        item => 'Cookbook::Schema::Result::ItemDef',
                        {
                           'foreign.item_id' => 'self.item_id'
                        }
                     );
# You can replace this text with custom content, and it will be preserved on regeneration
1;
