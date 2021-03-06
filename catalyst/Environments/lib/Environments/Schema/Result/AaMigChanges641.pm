package Environments::Schema::Result::AaMigChanges641;

use strict;
use warnings;

use base 'DBIx::Class';

__PACKAGE__->load_components("InflateColumn::DateTime", "TimeStamp", "Core");
__PACKAGE__->table("aa_mig_changes_641");
__PACKAGE__->add_columns(
  "tiers_id",
  {
    data_type => "VARCHAR2",
    default_value => undef,
    is_nullable => 1,
    size => 20,
  },
  "start_dt",
  { data_type => "DATE", default_value => undef, is_nullable => 1, size => 19 },
  "end_dt",
  { data_type => "DATE", default_value => undef, is_nullable => 1, size => 19 },
  "version",
  {
    data_type => "NUMBER",
    default_value => undef,
    is_nullable => 1,
    size => 126,
  },
  "logfile",
  {
    data_type => "VARCHAR2",
    default_value => undef,
    is_nullable => 1,
    size => 100,
  },
  "build_num",
  {
    data_type => "NUMBER",
    default_value => undef,
    is_nullable => 1,
    size => 126,
  },
);


# Created by DBIx::Class::Schema::Loader v0.04006 @ 2009-09-15 09:56:18
# DO NOT MODIFY THIS OR ANYTHING ABOVE! md5sum:bGtGZdKF0YWCVH2tiZT5Xw

__PACKAGE__->remove_columns(qw/start_dt end_dt/);

__PACKAGE__->add_columns(
                           "start_dt",
                           { data_type => "DATE",  default_value => undef, is_nullable => 1, size => 19, inflate_date => 0 },
                           "end_dt",
                           { data_type => "DATE",  default_value => undef, is_nullable => 1, size => 19, inflate_date => 0 },
                        );


__PACKAGE__->might_have(
                           file => 'Environments::Schema::Result::Stage_Merge',
                           { 'foreign.tiers_id' => 'self.tiers_id' }
                       );
#__PACKAGE__->many_to_many(file => 'Environments::Schema::Result::Stage_Merge','tiers_id');
# You can replace this text with custom content, and it will be preserved on regeneration
1;

