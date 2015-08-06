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
  "properties",
  "Environments::Schema::Result::Properties",
  { "foreign.environments_id" => "self.id" },
);


# Created by DBIx::Class::Schema::Loader v0.04006 @ 2009-09-15 09:56:18
# DO NOT MODIFY THIS OR ANYTHING ABOVE! md5sum:mvpt9fiuaG8673sMGcbfww

# Need to tell the result class that the id column has a sequence.
__PACKAGE__->add_columns(
   "id",
   {
    data_type => "NUMBER",
    default_value => undef,
    is_nullable => 0,
    size => 126,
    sequence => "seq_environment_id"
   }
);

#
# Set ResultSet Class
#
__PACKAGE__->resultset_class('Environments::Schema::ResultSet::Environments');

__PACKAGE__->has_many(
  "instances",
  "Environments::Schema::Result::Instances",
  { "foreign.environments_id" => "self.id" },
);

#__PACKAGE__->has_many(
#   "deployments",
#   "Environments::Schema::Result::Deployments",
#  { "foreign.environments_id" => "self.id" },
#);


# You can replace this text with custom content, and it will be preserved on regeneration
1;
