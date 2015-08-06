package MyDB::Schema::ResultSet::V_Environments;

use strict;
use warnings;
use base 'DBIx::Class::ResultSet';


=head2 app_name
Load schemas of a particular app name

=cut
sub app_name
{
   my($self, $name) =@_;
   #$c->log->debug("ResultSet::V_Environments->app_name($self, $name)");
   return $self->search({
                           app_system => { 'like' => $name }
                        });
}

=head2 release_num
Load schemas of particular release num

=cut
sub release_num
{
   my($self, $c, $rel) = @_;
   $rel = $self->prep_rel_num($rel);
   $c->log->debug("rel = $rel");
   return $self->search({
                           'substr(build_num, 1, 3)' => $rel
                        });
}

=head2 app_release
Load schemas of particular release num

=cut
sub app_release
{
   my($self, $c, $name, $rel) = @_;
   $c->log->debug("name = $name, rel = $rel");
   if($name =~ /^ALL$/ && $rel =~ /^ALL$/)
   {
      return $self->search();
   }
   elsif ($name =~ /^ALL$/)
   {
      $rel = $self->prep_rel_num($rel);
      return $self->search({
                              'substr(build_num, 1, 3)' => $rel
                           });
   }
   elsif ($rel =~ /^ALL$/)
   {
      return $self->search({
                              app_system => { 'like' => $name }
                           });
   }
   else
   {
      $rel = $self->prep_rel_num($rel);
      return $self->search({
                              'substr(build_num, 1, 3)' => $rel,
                              app_system => { 'like' => $name }
                           });
   }
}



=head2 get_rel_num_list

=cut
sub get_rel_num_list
{
   my($self, $c) = @_;
   $c->log->debug("get_rel_num_list");
   my(@nums);
   my(@rs) = $self->search(
                           {},
                           {
                              select => [
                                 'build_num',
                              ],
                              distinct => 1,
                              order_by => { -asc => 'build_num' },
                           }
                           );
   my(%relnums, @relnums) = undef;   
   foreach(@rs)
   {
      my($subnum) = $_->build_num;
      $subnum =~ /^(\d\d)(\d)/;
      $subnum = "$1.$2";
      if(!defined($relnums{$subnum}))
      {
         if($subnum =~ /^\d\d\.\d$/)
         {
            $relnums{$subnum} = $subnum;
            push(@relnums, $subnum);
            $c->log->debug("relnums{$subnum} = $subnum");
         }
      }
   }
   return(\@relnums);
   #return($rs);
}

=head2 get_app_list

=cut
sub get_app_list
{
   my($self, $c) = @_;
   $c->log->debug("get_app_list");
   my(@rs) = $self->search(
                           {},
                           {
                              select => [
                                'app_system'
                              ],
                              distinct => 1,
                              order_by => { -asc => 'app_system' },

                           }
                         );
   my(%apps, @apps) = undef;   
   foreach(@rs)
   {
      my($app) = $_->app_system;
      $app =~ /^(.+)/;
      $app = "$1";
      if(!defined($apps{$app}))
      {
         $apps{$app} = $app;
         push(@apps, $app);
         $c->log->debug("apps{$app} = $app");
      }
   }
   return(\@apps);
}


# Allow user to enter release number as XX or XX.X
# May be 75 or 75.0 or 75.2
sub prep_rel_num
{
   my($self, $rel) = @_;
   if($rel =~ /^(\d\d)\.(\d)$/)
   {
      $rel = $1 . $2;
   }
   elsif($rel =~ /^(\d\d)$/)
   {
      $rel = $1 . "0";
   }
   return($rel);
}






1;