package Environments::Schema::ResultSet::R2environments;

use strict;
use warnings;
use base 'DBIx::Class::ResultSet';




=head2 load_via_id
=cut
sub load_via_id
{
   my($self, $id) = @_;
   return $self->search({
                              id => { '=' => $id }
                        });
}

=head2 load_via_name
=cut
sub load_via_name
{
   my($self, $name) = @_;
   return $self->search({
                           name => {'like' => "$name" }
   });   
}

=head2 load_or_create_via_name
Search the table for the name provided.
If found, return ID
If not found, insert then return ID
=cut 
sub load_or_create_via_name
{
   my($self, $name) = @_;
   my($rs) = $self->search({
                           name => {'like' => "$name" }
                           });
   if(!$rs)
   {
      $rs = $self->create({
                           name => $name
                          });
   }
   return($rs);
}

=head2 load_with_instances
Load all environments with instances for the global report
=cut
sub load_with_instances
{
   my($self, $c) = @_;
   $c->log->debug("START R2environments->load_with_instances");
   my(@envs_with_apps_rs) = $c->model('DB::R2instances')->load_envts_with_instances($c);
   my(@envlist);
   my($envstr);
   foreach(@envs_with_apps_rs)
   {
   #   $c->log->debug("id => " . $_->environment->id);
      push(@envlist, $_->environment->id);
      $envstr .= $_->environment->id . ",";
   }
   $envstr = substr($envstr, 0, (length($envstr) - 1));
   #$c->log->debug("envstr = '$envstr'");
   my(@rs);
   #my($envs_with_apps_rs) = $c->model('DB::R2instances')->load_envts_with_instances($c);
   #$c->log->debug("size = " . @envlist);
   $c->log->debug("-------envlist---------");
   foreach(@envlist)
   {
      $c->log->debug($_);
   }
   $c->log->debug("-------envlist---------");
   if(@envlist)
   {
      @rs = $self->search(
         {
            id => { 
               -in => \@envlist
            },
         }
      );
   }
   else
   {
      @rs = $self->search({});
   }
   $c->log->debug("END R2environments->load_with_instances");
   return(@rs);
}


=head2 load_with_no_instances
When creating a new set of instances, we want the drop down to not include environments already defined.
Thus, load a list of environments who own now instances.
=cut
sub load_with_no_instances
{
   my($self, $c) = @_;
   $c->log->debug("START R2environments->load_with_no_instances");
   my(@envs_with_apps_rs) = $c->model('DB::R2instances')->load_envts_with_instances($c);
   my(@envlist);
   my($envstr);
   foreach(@envs_with_apps_rs)
   {
      $c->log->debug("id => " . $_->environment->id);
      push(@envlist, $_->environment->id);
      $envstr .= $_->environment->id . ",";
   }
   $envstr = substr($envstr, 0, (length($envstr) - 1));
   $c->log->debug("envstr = '$envstr'");
   my(@rs);
   #my($envs_with_apps_rs) = $c->model('DB::R2instances')->load_envts_with_instances($c);
   #$c->log->debug("size = " . @envlist);
   #$c->log->debug("-------envlist---------");
   #foreach(@envlist)
   #{
   #   $c->log->debug($_);
   #}
   #$c->log->debug("-------envlist---------");
   if(@envlist)
   {
      $c->log->debug("envlist => " . $envstr);
      @rs = $self->search(
         {
            id => { 
               -not_in => \@envlist
            },
         }
      );
   }
   else
   {
      @rs = $self->search({});
   }
   foreach(@rs)
   {
      $c->log->debug($_->id . " -> " . $_->name);
   }
   #$c->log->debug("---------------- RESULT ---------------------");
   #$c->log->debug(@rs);
   #$c->log->debug("---------------- RESULT ---------------------");
   $c->log->debug("END R2environments->load_with_no_instances");
   return(@rs);
}


1;
