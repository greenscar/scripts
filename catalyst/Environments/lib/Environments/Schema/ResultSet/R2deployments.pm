package Environments::Schema::ResultSet::R2deployments;

use strict;
use warnings;
use base 'DBIx::Class::ResultSet';


=head2 load_via_app_and_env
=cut
sub load_via_app_and_env
{
   my($self, $c, $appid, $envid) = @_;
   $c->log->debug("R2deployments->load_via_app_and_env($self, $c, $appid, $envid)");
   $c->log->debug("appid = $appid, envid = $envid");
   
   # Load instance list via app / env
   
   if($appid =~ /^ALL$/ && $envid =~ /^ALL$/)
   {
      return $self->search();
   }
   elsif ($appid =~ /^ALL$/)
   {
      # 1) Load list instances in the environment provided
      my(@instancesrs) = $c->model('DB::R2instances')->load_via_app_and_env($c, "ALL", $envid);
      my(@instancelist);
      # 2) Assign that list of instances' ids to an array which can be passed
      #    to a search arg
      foreach(@instancesrs)
      {
         push(@instancelist, $_->id);
      }
      # 3) Search via the instance list & return.
      if(@instancelist)
      {
         return(
            $self->search({
               instance => {
                  -in => \@instancelist
               }
            })
         );
      }
   }
   elsif ($envid =~ /^ALL$/)
   {
      # 1) Load list instances in the app provided
      my(@instancesrs) = $c->model('DB::R2instances')->load_via_app_and_env($c, $appid, "ALL");
      my(@instancelist);
      # 2) Assign that list of instances' ids to an array which can be passed
      #    to a search arg
      foreach(@instancesrs)
      {
         push(@instancelist, $_->id);
      }
      # 3) Search via the instance list & return.
      if(@instancelist)
      {
         return(
            $self->search({
               instance => {
                  -in => \@instancelist
               }
            })
         );
      }
   }
   else
   {
      # 1) Load list instances in the app & env provided
      my(@instancesrs) = $c->model('DB::R2instances')->load_via_app_and_env($c, $appid, $envid);
      my(@instancelist);
      # 2) Assign that list of instances' ids to an array which can be passed
      #    to a search arg
      foreach(@instancesrs)
      {
         push(@instancelist, $_->id);
      }
      # 3) Search via the instance list & return.
      if(@instancelist)
      {
         return(
            $self->search({
               instance => {
                  -in => \@instancelist
               }
            })
         );
      }
   }
}

1;
