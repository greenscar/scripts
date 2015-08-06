package Environments::Schema::Result::Applications;

use strict;
use warnings;

use base 'DBIx::Class';

__PACKAGE__->load_components("InflateColumn::DateTime", "TimeStamp", "Core");
__PACKAGE__->table("applications");
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
  "links",
  "Environments::Schema::Result::Links",
  { "foreign.applications_id" => "self.id" },
);
__PACKAGE__->has_many(
  "properties",
  "Environments::Schema::Result::Properties",
  { "foreign.applications_id" => "self.id" },
);
__PACKAGE__->has_many(
  "services_provider_app_ids",
  "Environments::Schema::Result::Services",
  { "foreign.provider_app_id" => "self.id" },
);
__PACKAGE__->has_many(
  "services_consumer_app_ids",
  "Environments::Schema::Result::Services",
  { "foreign.consumer_app_id" => "self.id" },
);


# Created by DBIx::Class::Schema::Loader v0.04006 @ 2009-08-26 15:49:48
# DO NOT MODIFY THIS OR ANYTHING ABOVE! md5sum:Pn+1X5UqBquUx2NYEBpuPA


#
# Set ResultSet Class
#
__PACKAGE__->resultset_class('Environments::Schema::ResultSet::Applications');


__PACKAGE__->add_columns(
   "id",
   {
    data_type => "NUMBER",
    default_value => undef,
    is_nullable => 0,
    size => 126,
    sequence => "seq_application_id"
   }
);
# You can replace this text with custom content, and it will be preserved on regeneration
1;
