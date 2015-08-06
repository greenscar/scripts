package Environments::Schema::Result::R2instances;

use strict;
use warnings;

use base 'DBIx::Class';

__PACKAGE__->load_components("InflateColumn::DateTime", "TimeStamp", "Core");
__PACKAGE__->table("r2instances");
__PACKAGE__->add_columns(
  "id",
  { data_type => "NUMBER", default_value => undef, is_nullable => 0, size => 38 },
  "application",
  { data_type => "NUMBER", default_value => undef, is_nullable => 0, size => 38 },
  "environment",
  { data_type => "NUMBER", default_value => undef, is_nullable => 0, size => 38 },
  "link",
  {
    data_type => "VARCHAR2",
    default_value => undef,
    is_nullable => 1,
    size => 200,
  },
  "link_public",
  { data_type => "NUMBER", default_value => "0 ", is_nullable => 0, size => 38 },
);
__PACKAGE__->set_primary_key("id");
__PACKAGE__->has_many(
  "r2deployments",
  "Environments::Schema::Result::R2deployments",
  { "foreign.instance" => "self.id" },
);
__PACKAGE__->belongs_to(
  "environment",
  "Environments::Schema::Result::R2environments",
  { id => "environment" },
);
__PACKAGE__->belongs_to(
  "application",
  "Environments::Schema::Result::R2applications",
  { id => "application" },
);


# Created by DBIx::Class::Schema::Loader v0.04006 @ 2009-09-15 09:56:18
# DO NOT MODIFY THIS OR ANYTHING ABOVE! md5sum:YtPrO2qofI+mn+EeTOKDKQ


# Need to tell the Result Class that the id column has a sequence.
__PACKAGE__->add_columns(
   "id",
   {
    data_type => "NUMBER",
    default_value => undef,
    is_nullable => 0,
    size => 38,
    sequence => "r2instances_seq"
   }
);

# Set ResultSet Class
__PACKAGE__->resultset_class('Environments::Schema::ResultSet::R2instances');

=head2 set_latest_deployment
=cut
sub set_latest_deployment
{
   my($self, $c) = @_;
   $c->log->debug("env:app => " . $self->environment->name . ":" . $self->application->name);
   $c->log->debug("Deployments:");
   my($latest_deployment);
   foreach($self->r2deployments)
   {
      #$c->log->debug($_->timestamp);
      if($latest_deployment eq '')
      {
         #$c->log->debug("$_ => " . $_);
         $latest_deployment = $_;
         #$c->log->debug("$latest_deployment => " . $latest_deployment);
      }  
      if($_->timestamp > $latest_deployment->timestamp)
      {
         #$c->log->debug($_->timestamp . " => " . $_->baseline);
         $latest_deployment = $_;
      }
   }
   if($latest_deployment ne '')
   {
      $c->log->debug($latest_deployment->timestamp . " => " . $latest_deployment->baseline);
   }
   $c->log->debug("END Deployments:");
}        
=head2 get_latest_deployment
=cut
sub get_latest_deployment
{
   my($self) = @_;
   #$c->log->debug("env:app => " . $self->environment->name . ":" . $self->application->name);
   #$c->log->debug("Deployments:");
   my($latest_deployment);
   foreach($self->r2deployments)
   {
      #$c->log->debug($_->timestamp);
      if($latest_deployment eq '')
      {
         #$c->log->debug("$_ => " . $_);
         $latest_deployment = $_;
         #$c->log->debug("$latest_deployment => " . $latest_deployment);
      }  
      if($_->timestamp > $latest_deployment->timestamp)
      {
         #$c->log->debug($_->timestamp . " => " . $_->baseline);
         $latest_deployment = $_;
      }
   }
   #if($latest_deployment ne '')
   #{
   #   $c->log->debug($latest_deployment->timestamp . " => " . $latest_deployment->baseline);
   #}
   #$c->log->debug("END Deployments:");
   return($latest_deployment);
}

# You can replace this text with custom content, and it will be preserved on regeneration
1;
