package MyDB::Schema::Result::V_Ad_Instances;

use strict;
use warnings;

use base 'DBIx::Class';

__PACKAGE__->load_components("InflateColumn::DateTime", "Core");
__PACKAGE__->table("v_ad_instances");
__PACKAGE__->add_columns(
   "host_id",
   {
      data_type => "NUMBER",
      default_value => undef,
      is_nullable => 0,
      size => 38
   },
   "instance_name",
   { 
      data_type => "VARCHAR2",
      default_value => undef,
      is_nullable => 0,
      size => 100
   },
   "purpose",
   { 
      data_type => "VARCHAR2",
      default_value => undef,
      is_nullable => 1,
      size => 100
   },
   "db_version",
   { 
      data_type => "VARCHAR2",
      default_value => undef,
      is_nullable => 1,
      size => 10
   },
   "active_sw",
   {
      data_type => "CHAR",
      default_value => undef,
      is_nullable => 1,
      size => 1
   }
);

__PACKAGE__->set_primary_key("instance_name");

# You can replace this text with custom content, and it will be preserved on regeneration
#
# Set relationships:
#

# belongs_to():
#   args:
#     1) Name of relationship, DBIC will create accessor with this name
#     2) Name of the model class referenced by this relationship
#     3) Column name in *this* table
#__PACKAGE__->belongs_to(book => 'MyApp::Schema::Result::Book', 'book_id');
__PACKAGE__->belongs_to(server => 'MyDB::Schema::Result::V_Ad_Hosts', 'host_id');

__PACKAGE__->has_many(
                        environments => 'MyDB::Schema::Result::V_Environments', 
                        { 'foreign.host_string' => 'self.instance_name' }
                     );
__PACKAGE__->resultset_attributes({where => {active_sw => 'Y'}});
               

1;
