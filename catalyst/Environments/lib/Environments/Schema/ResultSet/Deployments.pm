package Environments::Schema::ResultSet::Deployments;

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
                              environment_id => {'=' => $envid}
                           });
   }
   elsif ($envid =~ /^ALL$/)
   {
      return $self->search({
                              application_id => {'=' => $appid}
                           });
   }
   else
   {
      return $self->search({
                              application_id => {'=' => $appid},
                              environment_id => {'=' => $envid}
                           });
   }
}
1;
