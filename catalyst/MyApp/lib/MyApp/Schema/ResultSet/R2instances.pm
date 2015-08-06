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
                              id => {'=' => $envid}
                           });
   }
   elsif ($envid =~ /^ALL$/)
   {
      return $self->search({
                              id => {'=' => $appid}
                           });
   }
   else
   {
      return $self->search({
                              id => {'=' => $appid},
                              id => {'=' => $envid}
                           });
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
}
1;
