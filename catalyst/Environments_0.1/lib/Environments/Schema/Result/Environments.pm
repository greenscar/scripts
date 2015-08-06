package Environments::Schema::Result::Environments;

use strict;
use warnings;

use base 'DBIx::Class';

__PACKAGE__->load_components("InflateColumn::DateTime", "TimeStamp", "Core");
__PACKAGE__->table("environments");
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
);
__PACKAGE__->set_primary_key("id");
__PACKAGE__->has_many(
  "deployments",
  "Environments::Schema::Result::Deployments",
  { "foreign.environments_id" => "self.id" },
);
__PACKAGE__->has_many(
  "links",
  "Environments::Schema::Result::Links",
  { "foreign.environments_id" => "self.id" },
);
__PACKAGE__->has_many(
  "properties",
  "Environments::Schema::Result::Properties",
  { "foreign.environments_id" => "self.id" },
);


# Created by DBIx::Class::Schema::Loader v0.04006 @ 2009-07-30 14:17:44
# DO NOT MODIFY THIS OR ANYTHING ABOVE! md5sum:QTkOTp/OfOZFLuHbsD/3AQ


# You can replace this text with custom content, and it will be preserved on regeneration
1;
