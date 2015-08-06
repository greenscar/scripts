package MyDB::Schema::Result::Deployments;

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


# Created by DBIx::Class::Schema::Loader v0.04006 @ 2009-09-15 09:56:18
# DO NOT MODIFY THIS OR ANYTHING ABOVE! md5sum:a7J3FY2K3wmw64JBlLQJdg

#
# Set ResultSet Class
#
__PACKAGE__->resultset_class('MyDB::Schema::ResultSet::Deployments');

#__PACKAGE__->set_primary_key("baseline", "timestamp");
__PACKAGE__->belongs_to(
  "environment",
  "MyDB::Schema::Result::Environments",
  { id => "environment_id" },
);
__PACKAGE__->belongs_to(
  "application",
  "MyDB::Schema::Result::Applications",
  { id => "application_id" },
);

# You can replace this text with custom content, and it will be preserved on regeneration
1;
