package Environments::Controller::DBMigration;

use strict;
use warnings;
use parent 'Catalyst::Controller::HTML::FormFu';

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
Load all migration scripts executed for a particular release.

=cut
sub listrel :Chained('base') : PathPart('listrel') : Args(1)
{
   my($self, $c, $rel) = @_;
   $c->stash->{rel} = $rel;
   $c->stash->{mig_scripts} = [$c->model('DB::Stage_Merge')->release_num($c, $rel)];
   $c->stash->{template} = 'dbs/mig_scripts.tt2';
}

=head2 listenv
Load a list of all migration scripts executed in a particular environment

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
   }
   $c->stash->{template} = 'dbs/mig_changes.tt2';
}

=head2 diff
2 envts will be provided
For each envt, load a list of migration scripts executed not in that envt 
but executed in the other.

=cut
sub diff :Chained('base') : PathPart('diff3')
{
   my($self, $c) = @_;
   my $env1 = $c->request->params->{env1} || "700";
   my $env2 = $c->request->params->{env2} || "700";
   my $rel = $c->request->params->{rel} || "ALL";
   my($changetable1) = "DB::AaMigChanges" . $env1;
   my($changetable2) = "DB::AaMigChanges" . $env2;
   $c->log->debug("diff($changetable1, $changetable2)");
   my(@rs1) = $c->model($changetable1)->changes_not_in($c, "aa_mig_changes_" . $env2, $rel);
   my(@rs2) = $c->model($changetable2)->changes_not_in($c, "aa_mig_changes_" . $env1, $rel);
   $c->stash->{in1not2} = \@rs1;
   $c->stash->{in2not1} = \@rs2;
   $c->stash->{table1} = $env1;
   $c->stash->{table2} = $env2;
   $c->stash->{rel} = $rel;
   $c->stash->{template} = 'dbs/mig_compare.tt2';
}

=head2 mig_compare_formfu

=cut 
sub mig_compare_formfu : Chained('base') :PathPart('mig_compare_formfu') :Args(0) : FormConfig
{
   my($self, $c) = @_;
   
   my($form) = $c->stash->{form};
   
   if($form->submitted_and_valid)
   {
      my $env1 = $c->request->params->{env1}   || "631";
      my $env2 = $c->request->params->{env2}   || "631";
      my $rel = $c->request->params->{release}   || "ALL";
      $c->response->redirect($c->uri_for($self->action_for('diff'), {env1 => $env1, env2 => $env2, rel => $rel}));
   }
   else
   {
      my(@envtemp) = $c->model("DB::Stage_Merge")->get_schema_list();
      $c->log->debug("-----------------------------");
      $c->log->debug(@envtemp);
      $c->log->debug("-----------------------------");
      # Get the select added to the config file
      my(@envs) = $form->get_element({type => 'Select'});
      my(@envlist);
      foreach(@envtemp)
      {
         push(@envlist, [$_, $_]);
      }
      
      my($select_env1) = $form->get_element({name => 'env1'});
      # Add the envs to the select list
      $select_env1->options(\@envlist);
      
      my($select_env2) = $form->get_element({name => 'env2'});
      $select_env2->options(\@envlist);
      
      my(@sorted) = $self->convert_rellist_for_display($c);
      
      my($select_rel) = $form->get_element({name => 'release'});
      $select_rel->options(\@sorted);
      
   }
   $c->stash->{template} = 'dbs/mig_compare_formfu.tt2';
}

#
# Load all release numbers from the DB version field.
# As these numbers are in the format used by the DB team, parse
#  the numbers converting them to display format
# Sort the list
# Return the sorted list
#
sub convert_rellist_for_display
{
   my($self, $c) = @_;
   my(%rellisttemp) = $c->model("DB::Stage_Merge")->get_rel_num_list($c);
   my(@rellist);
   my(@sorted);
   my(%relhash);
   while ( my ($key, $value) = each(%rellisttemp) ) {
      $c->log->debug($key . " = " . $value);
      push(@rellist, [$key, $value]);
   }
   $c->log->debug("-----------------------------");
   foreach(@rellist)
   {
      $c->log->debug(@{$_}[0] . " = " . @{$_}[1]);
   }
   $c->log->debug("-----------------------------");
   foreach(sort { @{$a}[0] cmp @{$b}[0]} @rellist)
   {
      push(@sorted, [@{$_}[0], @{$_}[1]]) if(@{$_}[0] =~ /\d+/);
   }
   #@rellist = sort(@rellist);
   unshift(@sorted, ["ALL", "ALL"]);
   $c->log->debug("-----------------------------");
   foreach(@sorted)
   {
      $c->log->debug(@{$_}[0] . " = " . @{$_}[1]);
   }
   $c->log->debug("-----------------------------");
        
   return(@sorted);
}

=head2 mig_report
=cut
sub mig_report :Chained('base') : PathPart('report') :Args(0) : FormConfig
{
   my($self, $c) = @_;
   
   my($form) = $c->stash->{form};
   
   if($form->submitted_and_valid)
   {
      my ($env) = $c->request->params->{env} || "700";
      my ($rel) = $c->request->params->{rel} || "ALL";
      
      my($changetable) = "DB::AaMigChanges" . $env;
      my(@changes);
      
      $c->log->debug("env = $env");
      $c->log->debug("rel = $rel");
         
      if($rel =~ /ALL/)
      {
         $c->log->debug("inside rel == ALL");
         @changes = $c->model($changetable)->all;
      }
      else
      {
         $c->log->debug("inside rel != ALL");
         @changes = $c->model($changetable)->changes_per_release($c, $rel);
      }
      
      $c->stash->{mig_changes} = \@changes;
      $c->stash->{rel} = $rel;
      $c->stash->{env} = $env;
      $c->stash->{template} = 'dbs/mig_changes.tt2';
   }
   else
   {
      my(@envtemp) = $c->model("DB::Stage_Merge")->get_schema_list();
      #$c->log->debug("-----------------------------");
      #$c->log->debug(@envtemp);
      #$c->log->debug("-----------------------------");
      # Get the select added to the config file
      my(@envs) = $form->get_element({type => 'Select'});
      my(@envlist);
      foreach(@envtemp)
      {
         push(@envlist, [$_, $_]);
      }
      my($select_env) = $form->get_element({name => 'env'});
      # Add the envs to the select list
      $select_env->options(\@envlist);
            
      my(@sorted) = $self->convert_rellist_for_display($c);
      
      my($select_rel) = $form->get_element({name => 'rel'});
      $select_rel->options(\@sorted);
      $c->stash->{template} = 'dbs/mig_changes.tt2';
      
   }
   
}


=head1 AUTHOR

James Sandlin,James Sandlin,512-873-6637,James.Sandlin@hhsc.state.tx.us,HHSC,CR52960,

=head1 LICENSE

This library is free software. You can redistribute it and/or modify
it under the same terms as Perl itself.

=cut


sub delete_elem
{
   my($id, $version, @array) = @_;
   for(my($x) = 0; $x < @array; $x++)
   {
      my($elem) = $array[$x];
      if ($elem->tiers_id =~ /$id/ && $elem->version =~ /$version/)
      {
         splice(@array, $x, 1);
         $x--;
      }
   }
   return(@array);
}

1;
