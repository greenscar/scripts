package Environments::Schema::Result::Deployments;

use strict;
use warnings;

use base 'DBIx::Class';

__PACKAGE__->load_components("InflateColumn::DateTime", "TimeStamp", "Core");
__PACKAGE__->table("deployments");
__PACKAGE__->add_columns(
  "baseline",
  {
    data_type => "VARCHAR2",
    default_value => undef,
    is_nullable => 1,
    size => 100,
  },
  "timestamp",
  {
    data_type => "TIMESTAMP(6)",
    default_value => undef,
    is_nullable => 0,
    size => 11,
  },
  "applications_id",
  {
    data_type => "NUMBER",
    default_value => undef,
    is_nullable => 0,
    size => 126,
  },
  "environments_id",
  {
    data_type => "NUMBER",
    default_value => undef,
    is_nullable => 0,
    size => 126,
  },
  "bfjobid",
  { data_type => "NUMBER", default_value => undef, is_nullable => 1, size => 38 },
);
__PACKAGE__->set_primary_key("environments_id", "applications_id", "timestamp");
__PACKAGE__->belongs_to(
  "environments_id",
  "Environments::Schema::Result::Environments",
  { id => "environments_id" },
);
__PACKAGE__->belongs_to(
  "applications_id",
  "Environments::Schema::Result::Applications",
  { id => "applications_id" },
);


# Created by DBIx::Class::Schema::Loader v0.04006 @ 2009-08-24 16:13:59
# DO NOT MODIFY THIS OR ANYTHING ABOVE! md5sum:atgW/vAVUiHY+HWRilUKtA

#__PACKAGE__->add_columns(
#   "timestamp",
#   {
#      data_type => "TIMESTAMP(6)",
#      default_value => undef,
#      is_nullable => 0,
#      size => 11,
#      set_on_create => 1
#   }
#);
      
#
# Set ResultSet Class
#
#__PACKAGE__->resultset_class('Environments::Schema::ResultSet::Depoyments');


# You can replace this text with custom content, and it will be preserved on regeneration
1;
