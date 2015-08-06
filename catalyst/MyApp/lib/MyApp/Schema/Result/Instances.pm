package Environments::Schema::Result::Instances;

use strict;
use warnings;

use base 'DBIx::Class';

__PACKAGE__->load_components("InflateColumn::DateTime", "TimeStamp", "Core");
__PACKAGE__->table("instances");
__PACKAGE__->add_columns(
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
  "link",
  {
    data_type => "VARCHAR2",
    default_value => undef,
    is_nullable => 1,
    size => 200,
  },
  "link_public",
  { data_type => "NUMBER", default_value => "0 ", is_nullable => 0, size => 1 },
);


# Created by DBIx::Class::Schema::Loader v0.04006 @ 2009-09-15 09:56:18
# DO NOT MODIFY THIS OR ANYTHING ABOVE! md5sum:cn29iqH9kGUJc1eccsEQ1g

#
# Set ResultSet Class
#
__PACKAGE__->resultset_class('Environments::Schema::ResultSet::Instances');

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
