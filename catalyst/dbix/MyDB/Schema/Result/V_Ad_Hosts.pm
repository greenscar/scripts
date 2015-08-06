package MyDB::Schema::Result::V_Ad_Hosts;

use strict;
use warnings;

use base 'DBIx::Class';

__PACKAGE__->load_components("InflateColumn::DateTime", "Core");
__PACKAGE__->table("ad_hosts");
__PACKAGE__->add_columns(
   "host_id",
   {
      data_type => "NUMBER",
      default_value => undef,
      is_nullable => 0,
      size => 38
   },
   "host_name",
   { 
      data_type => "VARCHAR2",
      default_value => undef,
      is_nullable => 0,
      size => 50
   },
   "purpose",
   { 
      data_type => "VARCHAR2",
      default_value => undef,
      is_nullable => 1,
      size => 30
   },
   "num_cpu",
   {
      data_type => "NUMBER",
      default_value => undef,
      is_nullable => 1,
      size => 38
   },
   "data_center",
   { 
      data_type => "VARCHAR2",
      default_value => undef,
      is_nullable => 0,
      size => 7
   },
   "host_active_sw",
   {
      data_type => "CHAR",
      default_value => undef,
      is_nullable => 1,
      size => 1
   }
);
__PACKAGE__->set_primary_key("host_id");

__PACKAGE__->has_many(dbs => 'MyDB::Schema::Result::V_Ad_Instances','host_id');
1;
