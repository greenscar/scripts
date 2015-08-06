package MyDB::Schema::ResultSet::R2environments;

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

=head2 load_with_no_instances
When creating a new set of instances, we want the drop down to not include environments already defined.
Thus, load a list of environments who own now instances.
=cut
sub load_with_no_instances
{
   my($self, $c) = @_;
   $c->log->debug("START R2environments->load_with_no_instances");
   #############################
   # WORKS
   my(@envs_with_apps_rs) = $c->model('DB::R2instances')->load_envts_with_instances($c);
   #$c->log->debug("envs_with_apps_rs size = " . @envs_with_apps_rs);
   #$c->log->debug("---------------- ENV WITH APPS ---------------------");
   my(@envlist);
   my($envstr);
   foreach(@envs_with_apps_rs)
   {
      $c->log->debug("id => " . $_->environment->id);
      push(@envlist, $_->environment->id);
      $envstr .= $_->environment->id . ",";
   }
   $envstr = substr($envstr, 0, (length($envstr) - 1));
   #my(@envs_with_apps) = [33];
   #$c->log->debug("envlist = " . @envlist);
   #$c->log->debug("---------------- END ENV WITH APPS ---------------------");
   # END WORKS
   # $c->log->debug(@envlist);
   #############################
   my(@rs);
   #my($envs_with_apps_rs) = $c->model('DB::R2instances')->load_envts_with_instances($c);
   #$c->log->debug("size = " . @envlist);
   #$c->log->debug("-------envlist---------");
   #foreach(@envlist)
   #{
   #   $c->log->debug($_);
   #}
   #$c->log->debug("-------envlist---------");
   if(@envlist > 0)
   {
      @rs = $self->search(
         {
            id => { 
               -not_in => ['1', '2']
            },
            #id => { 'NOT IN' => $envs_with_apps_rs->get_column('id')->as_query }
         }
      );

   }
   #else
   #{
   #   @rs = $self->search({});
   #}
   $c->log->debug("---------------- RESULT ---------------------");
   $c->log->debug(@rs);
   $c->log->debug("---------------- RESULT ---------------------");
   $c->log->debug("END R2environments->load_with_no_instances");
   return(@rs);
}


1;
