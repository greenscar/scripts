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
    is_nullable => 0,
    size => 100,
  },
  "timestamp",
  {
    data_type => "TIMESTAMP(6)",
    default_value => undef,
    is_nullable => 0,
    size => 11,
  },
  "application_id",
  {
    data_type => "NUMBER",
    default_value => undef,
    is_nullable => 0,
    size => 126,
  },
  "environment_id",
  {
    data_type => "NUMBER",
    default_value => undef,
    is_nullable => 0,
    size => 126,
  },
  "bf_job_id",
  { data_type => "NUMBER", default_value => undef, is_nullable => 1, size => 38 },
);


# Created by DBIx::Class::Schema::Loader v0.04006 @ 2009-08-26 15:49:48
# DO NOT MODIFY THIS OR ANYTHING ABOVE! md5sum:DQZpZiIhLnH0UH8hCYesZQ

#__PACKAGE__->set_primary_key("baseline", "timestamp");
__PACKAGE__->belongs_to(
  "environment",
  "Environments::Schema::Result::Environments",
  { id => "environment_id" },
);
__PACKAGE__->belongs_to(
  "application",
  "Environments::Schema::Result::Applications",
  { id => "application_id" },
);

# You can replace this text with custom content, and it will be preserved on regeneration
1;
