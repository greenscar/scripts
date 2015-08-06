use MyDB::Schema;
my $schema = MyDB::Schema->connect('dbi:Oracle:ENV_MGT_TOOLS', 'ENV_MGT', 'envown123');



my @stage = $schema->resultset('Stage_Merge')->all;
foreach(@stage)
{
   print("TIERS_ID = " . $_->TIERS_ID . "\n");
}

exit();

my @all_books = $schema->resultset('Books')->all;


print("\n------------------------------\n");
my(@rs) = $schema->resultset('V_Environments')->search({},
                           {
                              select => [
                                 #'BUILD_NUM',
                                 'substr(BUILD_NUM, 1, 3)'
                              ],
                              as    => [qw/ subnum /],
                              distinct => 1,
                              order_by => { -asc => 'BUILD_NUM' },
                           },
                           #{
                           #   columns => [qw/BUILD_NUM/],
                           #   distinct => 1,
                           #   order_by => 'BUILD_NUM'
                           #}
                           );
#while(my $dl = $rs->next)
foreach(@rs)
{
   print("subnum => " . $_->get_column('subnum') . "\n");
}


print("count => " . @rs . "\n");
exit();
my(%relnums) = undef;

foreach(@rs)
{
   my($subnum) = $_->BUILD_NUM;
   $subnum =~ /^(\d\d)(\d)/;
   $subnum = "$1.$2";
   if(!defined($relnums{$subnum}))
   {
      $relnums{$subnum} = $subnum;
      print("relnums{$subnum} = $subnum\n");
   }
}
