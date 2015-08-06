###############################################################################
# Baseline Object 
###############################################################################
package Baseline;
sub new
{
   my $self = {};
   $self->{id} = undef;
   $self->{date} = undef;
   bless $self;
   return $self;
}
sub id
{    
   my ( $self, $id ) = @_;                         
   $self->{id} = $id if defined($id);
   return $self->{id};
}
sub date
{    
   my ( $self, $date ) = @_;                         
   $self->{date} = $date if defined($date);
   return $self->{date};
}
###############################################################################
# END Baseline Object 
###############################################################################
1;
