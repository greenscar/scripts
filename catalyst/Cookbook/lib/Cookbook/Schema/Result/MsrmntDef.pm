package Cookbook::Schema::Result::MsrmntDef;

use strict;
use warnings;

use base 'DBIx::Class';

__PACKAGE__->load_components("InflateColumn::DateTime", "Core");
__PACKAGE__->table("msrmnt_def");
__PACKAGE__->add_columns(
  "msrmnt_id",
  { data_type => "INT", default_value => undef, is_nullable => 0, size => 11 },
  "msrmnt_descr",
  { data_type => "VARCHAR", default_value => "", is_nullable => 0, size => 20 },
);
__PACKAGE__->set_primary_key("msrmnt_id");


# Created by DBIx::Class::Schema::Loader v0.04006 @ 2009-07-12 19:21:41
# DO NOT MODIFY THIS OR ANYTHING ABOVE! md5sum:zNV/9O6aIcoxP/jQaOpQqg

__PACKAGE__->belongs_to(
                           ingredients => 'Cookbook::Schema::Result::Ingredients',
                           {
                              'foreign.msrmnt_id' => 'self.msrmnt_id'
                           }
                       );
# You can replace this text with custom content, and it will be preserved on regeneration
1;
