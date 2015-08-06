###############################################################################
# Activity Object 
###############################################################################
package Activity;
#constructor
sub new
{
   my $self = {};
   $self->{id} = undef;
   $self->{date} = undef;
   $self->{user} = undef;
   $self->{title} = undef;
   $self->{files} = [];
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
sub user
{    
   my ( $self, $user ) = @_;                         
   $self->{user} = $user if defined($user);
   return $self->{user};
}
sub title
{    
   my ( $self, $title ) = @_;
   $self->{title} = $title if defined($title);
   return $self->{title};
}
sub files
{    
   my ( $self, @files ) = @_;
   $self->{files} = @files if defined(@files);
   return $self->{files};
}
sub add_file
{
   my($file_to_add) = @_;
   #@array = @{$self->{files}};
   #$new_loc = $#array;
   push(@{$self->{files}}, $file_to_add);
}
###############################################################################
# END Activity Object 
###############################################################################
1;

