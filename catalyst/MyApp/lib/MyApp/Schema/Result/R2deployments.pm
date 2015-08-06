package Environments::Schema::Result::R2deployments;

use strict;
use warnings;

use base 'DBIx::Class';

__PACKAGE__->load_components("InflateColumn::DateTime", "TimeStamp", "Core");
__PACKAGE__->table("r2deployments");
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
  "instance",
  { data_type => "NUMBER", default_value => undef, is_nullable => 0, size => 38 },
  "bf_job",
  { data_type => "NUMBER", default_value => undef, is_nullable => 1, size => 38 },
);
__PACKAGE__->belongs_to(
  "instance",
  "Environments::Schema::Result::R2instances",
  { id => "instance" },
);


# Created by DBIx::Class::Schema::Loader v0.04006 @ 2009-09-15 09:56:18
# DO NOT MODIFY THIS OR ANYTHING ABOVE! md5sum:JqHZCg/7cn+BhAy8h8Fk6w


# You can replace this text with custom content, and it will be preserved on regeneration
1;
