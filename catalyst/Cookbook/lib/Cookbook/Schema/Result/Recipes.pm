package Cookbook::Schema::Result::Recipes;

use strict;
use warnings;

use base 'DBIx::Class';

__PACKAGE__->load_components("InflateColumn::DateTime", "Core");
__PACKAGE__->table("recipes");
__PACKAGE__->add_columns(
  "recipe_id",
  { data_type => "INT", default_value => undef, is_nullable => 0, size => 11 },
  "name",
  { data_type => "VARCHAR", default_value => "", is_nullable => 0, size => 150 },
  "cat_id",
  { data_type => "INT", default_value => 0, is_nullable => 0, size => 11 },
  "tried",
  { data_type => "ENUM", default_value => 0, is_nullable => 0, size => 1 },
  "time_to_complete",
  {
    data_type => "VARCHAR",
    default_value => undef,
    is_nullable => 1,
    size => 30,
  },
  "num_servings",
  {
    data_type => "VARCHAR",
    default_value => undef,
    is_nullable => 1,
    size => 30,
  },
  "num_views",
  { data_type => "INT", default_value => 0, is_nullable => 0, size => 11 },
);
__PACKAGE__->set_primary_key("recipe_id");


# Created by DBIx::Class::Schema::Loader v0.04006 @ 2009-07-12 19:21:41
# DO NOT MODIFY THIS OR ANYTHING ABOVE! md5sum:dlrdCZ6bGENomoatFNyeqw

#My::DBIC::Schema::Recipes->belongs_to
#(
#   category => 'Cookbook::Schema::Result::CategoryDef', 
#   {'foreign.cat_id' => 'self.cat_id'}
#);
   
__PACKAGE__->has_many(steps => 'Cookbook::Schema::Result::Steps', 'recipe_id');
__PACKAGE__->has_many(ingredients => 'Cookbook::Schema::Result::Ingredients', 'recipe_id');
__PACKAGE__->belongs_to(category => 'Cookbook::Schema::Result::CategoryDef', 'cat_id');

# You can replace this text with custom content, and it will be preserved on regeneration
1;
