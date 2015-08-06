package MyDB::Schema::ResultSet::R2instances;

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

=head2 load_envts_with_instances
=cut
sub load_envts_with_instances
{
   my($self, $c) = @_;
   #$c->log->debug("R2instances->load_envts_with_instances");
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

=head2 app_env_exists
Check the count in the db for this app / env combination
=cut
sub app_env_exists
{
   my($self, $c, $appid, $envid) = @_;
   my($rs) = $self->search({},
               {
                  application => {'=' => $appid},
                  environment => {'=' => $envid}
               });
   $c->log->debug("app $appid && env $envid count = " . $rs->count);
   return($rs);
}
1;
