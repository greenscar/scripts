package Environments::Schema::ResultSet::R2instances;

use strict;
use warnings;
use base 'DBIx::Class::ResultSet';


=head2 load_via_app_and_env
=cut
sub load_via_app_and_env
{
   my($self, $c, $appid, $envid) = @_;
   $c->log->debug("appid = $appid, envid = $envid");
   if($appid =~ /^ALL$/ && $envid =~ /^ALL$/)
   {
      return $self->search();
   }
   elsif ($appid =~ /^ALL$/)
   {
      return $self->search({
                              environment => {'=' => $envid}
                           });
   }
   elsif ($envid =~ /^ALL$/)
   {
      return $self->search({
                              application => {'=' => $appid}
                           });
   }
   else
   {
      return $self->search({
                              application => {'=' => $appid},
                              environment => {'=' => $envid}
                           });
   }
}

=head2 load_or_create_via_app_env

=cut
sub load_or_create_via_app_env
{
   my($self, $c, $appid, $envid) = @_;
   my($rs) = $self->load_via_app_and_env($c, $appid, $envid);
   if($rs)
   {
      return($rs);
   }
   else
   {
      $rs = $self->create({
            application => $appid,
            environment => $envid
         });
      return($rs);
   }
}
=head2 load_envts_with_instances
=cut
sub load_envts_with_instances
{
   my($self, $c) = @_;
   $c->log->debug("R2instances->load_envts_with_instances");
   #my(@rs) = $self->search();
   #return $self->search();
   return $self->search({},
         {
               select => [
                  'environment',
               ],
               distinct => 1,
            order_by => 'environment',
            }
         );                   
   #)->r2deployments->search_related()->max();
}

1;
