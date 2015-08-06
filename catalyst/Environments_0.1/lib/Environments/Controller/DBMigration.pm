package Environments::Controller::DBMigration;

use strict;
use warnings;
use parent 'Catalyst::Controller';

=head1 NAME

Environments::Controller::DBMigration - Catalyst Controller

=head1 DESCRIPTION

Catalyst Controller.

=head1 METHODS

=cut


=head2 base

=cut
sub base :Chained('/') :PathPart('dbmigration') :CaptureArgs(0)
{
   my($self, $c) = @_;
   $c->stash->{mig_scripts} = [$c->model('DB::Stage_Merge')->all];
   $c->stash->{template} = 'dbs/mig_scripts.tt2';
}

=head2 listall
Load all schema objects & pass to dbs/schema_list.tt2 in stash for display

=cut
sub list : Chained('base') :PathPart('list')
{
   my($self, $c) = @_;
   my $rel = $c->request->params->{rel} || "ALL";
   $c->log->debug("rel = $rel");
   $c->stash->{mig_scripts} = [$c->model('DB::Stage_Merge')->release_num($c, $rel)];
   $c->stash->{template} = 'dbs/mig_scripts.tt2';
}

=head2 listrel

=cut
sub listrel :Chained('base') : PathPart('listrel') : Args(1)
{
   my($self, $c, $rel) = @_;
   $c->stash->{rel} = $rel;
   $c->stash->{mig_changes} = [$c->model('DB::Stage_Merge')->release_num($c, $rel)];
   $c->stash->{template} = 'dbs/mig_scripts.tt2';
}

=head2 listenv

=cut
sub listenv :Chained('base') : PathPart('listenv') : Args(1)
{
   my($self, $c, $env) = @_;
   $c->log->debug("env = $env");
   my($changetable) = "DB::AaMigChanges" . $env;
   #$c->log->debug($c->model($changetable)->all);
   #$c->stash->{mig_changes} = [$c->model($changetable)->all];
   #
   # If the DB is being refreshed, this query will fail.
   # TODO: Figure out how to do a try / catch on all queries to mig_changes
   #        views.
   #
   my(@changes) = $c->model($changetable)->all;
   $c->stash->{mig_changes} = \@changes;
   $c->stash->{env} = $env;
   foreach(@changes)
   {
      $c->log->debug($_->tiers_id);
      $c->log->debug($_->start_dt);
      #if(defined $_->file)
      #{
      #   #$c->log->debug($_->file->unix_loc);
      #   $c->log->debug("HAS FILE");
      #}
      #else
      #{
      #   $c->log->debug("HAS NO FILE");
      #}
   }
   $c->stash->{template} = 'dbs/mig_changes.tt2';
}

=head2 diff

=cut
sub diff :Chained('base') : PathPart('diff'): Args(2)
{
   my($self, $c, $env1, $env2) = @_;
   #$c->stash->{diffs} = [$c->model('DB::Stage_Merge')->schema($env1)];
   my($changetable1) = "DB::AaMigChanges" . $env1;
   my($changetable2) = "DB::AaMigChanges" . $env2;
   $c->log->debug("diff($changetable1, $changetable2)");
   my(@rs1) = $c->model($changetable1)->all;
   my(@rs2) = $c->model($changetable2)->all;
   $c->log->debug("rs1 = " . @rs1);
   $c->log->debug("rs2 = " . @rs2);
   
   for(my($loc1)=0; $loc1 < @rs1; $loc1++)
   {
      my($change1) = $rs1[$loc1];
      
      for(my($loc2)=0; $loc2 < @rs2; $loc2++)
      {
         my($change2) = $rs2[$loc2];
         #print($rs1[$loc1] . " = " . $rs2[$loc2] . "\n");
         if($change1->tiers_id eq $change2->tiers_id && $change1->version == $change2->version)
         {
            splice(@rs1, $loc1, 1);
            splice(@rs2, $loc2, 1);
         #   #last;
         }
      }
   }
   $c->log->debug("rs1 = " . @rs1);
   $c->log->debug("rs2 = " . @rs2);
   $c->stash->{in1not2} = \@rs1;
   $c->stash->{in2not1} = \@rs2;
   $c->stash->{table1} = $env1;
   $c->stash->{table2} = $env2;
   $c->stash->{template} = 'dbs/mig_compare.tt2';
   
}

=head2 diff2

=cut
sub diff2 :Chained('base') : PathPart('diff2'): Args(2)
{
   my($self, $c, $env1, $env2) = @_;
   $c->stash->{diffs} = [$c->model('DB::Stage_Merge')->schema($env1)];
   $c->stash->{table1} = $env1;
   $c->stash->{table2} = $env2;
   $c->stash->{template} = 'dbs/mig_compare.tt2';
}
=head1 AUTHOR

James Sandlin,James Sandlin,512-873-6637,James.Sandlin@hhsc.state.tx.us,HHSC,CR52960,

=head1 LICENSE

This library is free software. You can redistribute it and/or modify
it under the same terms as Perl itself.

=cut

1;
