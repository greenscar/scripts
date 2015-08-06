################################################################################
# Script:      autobloo.pl
# Version:     3.0
# Author:      James Sandlin <james.g.sandlin@hhsc.state.tx.us>
# Date:        2009-02-05
# Purpose:     This set of scripts is used to automate tasks in the stream 
#                 structure defined as the HHSC EA Standard.
#              Tasks automated by this package include:
#                 1) Baseline creation
#                 2) Stream rebasing
#                 3) Rebase Reporting
# Syntax:      This package is designed to work with IBM BuildForge.
#              In order to use without BuildForge, one must first
#                 define all required environment variables.
#              The driver script to this package is autobloo.pl
################################################################################
################################################################################
# UpdateAction Object
# An UpdateAction is an action taken by ClearCase during a view update
################################################################################
package UpdateAction;

sub new
{
   my $self = {};
   my $action = undef;
   my $filename = undef;
   my $versionnum = undef;
   bless $self;
   return $self;
}

sub action
{    
   my ( $self, $action ) = @_;                         
   $self->{action} = $action if defined($action);
   return $self->{action};
}
sub filename
{    
   my ( $self, $filename ) = @_;                         
   $self->{filename} = $filename if defined($filename);
   return $self->{filename};
}
sub version
{    
   my ( $self, $version ) = @_;                         
   $self->{version} = $version if defined($version);
   return $self->{version};
}

sub get_csv_report
{
   my ($self, $helper) = @_;
   $to_return = "action=";
   $to_return .= $self->action();
   $to_return .= "^";
   $to_return .= "filename=";
   $to_return .= $self->filename();
   $to_return .= "^";
   $to_return .= "version=";
   $to_return .= $self->version();
   return($to_return . "\n");
}
sub get_html_report
{
   my ($self, $helper, $color) = @_;
   my($to_return) = undef;
   $to_return .= "<tr bgcolor=\"#$color\">\n";
   $to_return .= "<td width=\"100\"/>";
   $to_return .= $self->action();
   $to_return .= "</td>\n";
   $to_return .= "<td width=\"500\"/>";
   $to_return .= $self->filename();
   $to_return .= "</td>\n";
   $to_return .= "<td width=\"500\"/>";
   $to_return .= $self->version();
   $to_return .= "&nbsp;" if($self->version() =~ "");
   $to_return .= "</td>\n";
   $to_return .= "</tr>\n";
   return($to_return);
}
1;
