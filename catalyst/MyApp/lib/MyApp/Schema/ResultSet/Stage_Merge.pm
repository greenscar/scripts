package Environments::Schema::ResultSet::Stage_Merge;

use strict;
use warnings;
use base 'DBIx::Class::ResultSet';

=head2 release_num
Load migration scripts executed for a particular release

=cut
sub release_num
{
   my($self, $c, $rel) = @_;
   $c->log->debug("Stage_Merge->release_num($self, $c, $rel)");
   $c->log->debug("rel = $rel");
   if($rel =~ /^ALL$/)
   {
      $c->log->debug("inside ALL");
      return $self->search();
   }
   else
   {
      $c->log->debug("inside ELSE");
      $rel = $self->prep_rel_num($rel);
      $c->log->debug("rel = $rel");
      return $self->search({
                           'substr(build_num, 1, 3)' => $rel
                           });
   }
}

=head2 schema

=cut
sub schema
{
   my($self, $c, $id) = @_;
   return $self->search(
                           undef,
                           {
                              $id => defined
                           }
                         );
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

=head2 get_schema_list
=cut
sub get_schema_list
{
   my(@temp) = (631, 641, 650, 700); 
   return(@temp);
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
                              #order_by => { -asc => 'build_num' },
                           }
                           );
   my(%relnums, @relnumsarray) = undef;   
   foreach(@rs)
   {
      my($subnum) = $_->build_num;
      $subnum =~ /^(\d\d)(\d)/;
      $subnum = "$1.$2";
      if(!defined($relnums{$subnum}))
      {
         if($subnum =~ /^\d\d\.\d$/)
         {
            $relnums{$_->build_num} = $subnum;
            #push(@relnums, $_->build_num);
            $c->log->debug("relnums{$subnum} = $subnum");
         }
      }
   }
   return(%relnums);
}

=head1 AUTHOR

James Sandlin,James Sandlin,512-873-6637,James.Sandlin@hhsc.state.tx.us,HHSC,CR52960,

=head1 LICENSE

This library is free software. You can redistribute it and/or modify
it under the same terms as Perl itself.

=cut
1;
