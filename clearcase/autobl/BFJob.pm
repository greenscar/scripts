#!/opt/rational/clearquest/bin/cqperl

package BFJob;

sub new
{
   my $self = {};
   my $cleartool_path = undef;
   my $cleartool = undef;
   my $pvob = undef;
   my $unixid = undef;
   my $bfroot = undef;
   my $start_time = undef;
   my $bftag = undef;
   my $projectname = undef;
   my $outputdir = undef;
   my $jobid = undef;
   bless $self;
   return $self;
}


##########################################################################
# END OBJECT VAR METHODS
##########################################################################

sub projectname
{
   my( $self, $projectname) = @_;
   $self->{projectname} = $projectname if defined($projectname);
   return $self->{projectname};
}

sub jobid
{
   my( $self, $jobid) = @_;
   $self->{jobid} = $jobid if defined($jobid);
   return $self->{jobid};
}

sub outputdir
{
   my( $self, $outputdir) = @_;
   return $self->bfroot() . "/" . $self->projectname();
}

sub bftag
{
   my( $self, $bftag) = @_;
   $self->{bftag} = $bftag if defined($bftag);
   return $self->{bftag};
}

sub start_time
{
   my( $self, $start_time) = @_;
   $self->{start_time} = $start_time if defined($start_time);
   return $self->{start_time};
}

sub unixid
{
   my( $self, $unixid) = @_;
   $self->{unixid} = $unixid if defined($unixid);
   return $self->{unixid};
}

sub bfroot
{
   my( $self, $bfroot) = @_;
   $self->{bfroot} = $bfroot if defined($bfroot);
   return $self->{bfroot};
}

sub pvob
{
   my( $self, $pvob) = @_;
   $self->{pvob} = $pvob if defined($pvob);
   return $self->{pvob};
}

sub cleartool_path
{
   my( $self, $cleartool_path) = @_;
   $self->{cleartool_path} = $cleartool_path if defined($cleartool_path);
   cleartool($cleartool_path . "/cleartool");
   return $self->{cleartool_path};
}

sub cleartool
{
   my( $self, $cleartool) = @_;
   $self->{cleartool} = $cleartool if defined($cleartool);
   return $self->{cleartool};
}


###############################################################################
# END SUB METHODS
###############################################################################
1;
