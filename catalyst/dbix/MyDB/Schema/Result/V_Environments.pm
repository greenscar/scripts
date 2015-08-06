package MyDB::Schema::Result::V_Environments;

use strict;
use warnings;

use base 'DBIx::Class';

__PACKAGE__->load_components("InflateColumn::DateTime", "Core");
__PACKAGE__->table("v_environments");
__PACKAGE__->add_columns(
   "env_id",
   {
      data_type => "NUMBER",
      default_value => undef,
      is_nullable => 0,
      size => 38
   },
   "name",
   { 
      data_type => "VARCHAR2",
      default_value => undef,
      is_nullable => 0,
      size => 100
   },
   "host_string",
   { 
      data_type => "VARCHAR2",
      default_value => undef,
      is_nullable => 0,
      size => 100
   },
   "connect_account",
   { 
      data_type => "VARCHAR2",
      default_value => undef,
      is_nullable => 0,
      size => 30
   },
   "display_sw",
   { 
      data_type => "CHAR",
      default_value => undef,
      is_nullable => 1,
      size => 1
   },
   "build_num",
   {
      data_type => "NUMBER",
      default_value => undef,
      is_nullable => 1,
      size => 38
   },
   "app_system",
   { 
      data_type => "VARCHAR2",
      default_value => undef,
      is_nullable => 0,
      size => 10
   },
);
__PACKAGE__->set_primary_key("env_id");


# belongs_to():
#   args:
#     1) Name of relationship, DBIC will create accessor with this name
#     2) Name of the model class referenced by this relationship
#     3) Column name in *this* table
#__PACKAGE__->belongs_to(book => 'MyApp::Schema::Result::Book', 'book_id');
__PACKAGE__->belongs_to(
                           database => 'MyDB::Schema::Result::V_Ad_Instances', 
                           { 'foreign.instance_name' => 'self.host_string'}
                       );
__PACKAGE__->resultset_attributes({where => {display_sw => 'Y'}});
#
# Set ResultSet Class
#
__PACKAGE__->resultset_class('MyDB::Schema::ResultSet::V_Environments');




















1;
