#!/opt/rational/clearquest/bin/cqperl

# Script:      streamread.pl
# Version:     2.0
# Author:      James Sandlin <james.g.sandlin@hhsc.state.tx.us>
# Date:        2008-02-05
# Purpose:     Generates a report of all ClearCase activities in a build of type:
#              "BaseCMActivity", "ChangeActivity", "UCMUtilityActivity", "Defect"
#              A stream name is provided. The script looks at the date of the last 
#              build & the current build. It gets dates from those files and finds
#              All deliveries to the build stream in that time range. It then
#              recursively traces those activites back to their initial contributing
#              activity.

# Syntax:      streamread.pl ARGS
# Arguments:   --stream    n     Name of stream to find initial activities

package Stream;
sub new
{
   my $self = {};
   $self->{name} = undef;
   $self->{children} = [];
   bless $self;
   return $self;
}
sub name
{
   my( $self, $name) = @_;
   $self->{name} = $name if defined($name);
   return $self->{name};
}

sub children
{    
   my $self = shift;
   if (@_) { @{ $self->{children} } = @_ }
   return $self->{children};
}
sub add_child
{
   my $self = shift;
   my($stream_to_add) = @_;
   push(@{$self->{children}}, $stream_to_add);
}

1;
