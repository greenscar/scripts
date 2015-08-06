
#use lib '/net/ngacelerra/csw/BuildForge_Software/RequiredProducts/buildforge/Platform/BuildForge/Template/SQL';
use lib '/usr/local/perl-5.10.0/lib/site_perl/5.10.0/sun4-solaris-thread-multi';
use DBI;
#use BaseTemplate;
#use Mysql;
print(join("\n", @INC, "\n"));



$host = "EA-81007676.hhsea.txnet.state.tx.us";
$db = "rational";
$user = "rational";
$password = "rational";

$connectionInfo="dbi:mysql:$db;$host";

# make connection to database
$dbh = DBI->connect($connectionInfo,$user,$password);


#$db = Mysql->connect($host, $database, $user, $password);
#$db->selectdb($database);
#insert into databse

while(($act_id, $activity) = each(%{$self->activities}))
{
   $ins_stmt = "INSERT INTO acts_per_build (build_id, act_id, build_time) "
             . "VALUES (" . $helper->build_id . ", " . $activity->id . ", " . $helper->timestamp_this_build . ")";
   #$query = $db->query($ins_stmt);
   print($ins_stmt . "\n");
}
#$db->disconnect();


#
#[sandlja@iedaau019]/> find . -type f -name "DBI.pm" 2>/dev/null
#./export/app/oracle/product/oratrbld/10A/client/perl/lib/site_perl/5.8.3/Apache/DBI.pm
#./export/app/oracle/product/oratrbld/10A/client/perl/lib/site_perl/5.8.3/sun4-solaris-thread-multi-64/Bundle/DBI.pm
#./export/app/oracle/product/oratrbld/10A/client/perl/lib/site_perl/5.8.3/sun4-solaris-thread-multi-64/DBI.pm
#
#./usr/local/perl-5.10.0/lib/site_perl/5.10.0/sun4-solaris-thread-multi/Bundle/DBI.pm
#./usr/local/perl-5.10.0/lib/site_perl/5.10.0/sun4-solaris-thread-multi/DBI.pm
#./usr/local/perl-5.8.8/lib/site_perl/5.8.8/sun4-solaris-thread-multi/DBI.pm
#./usr/local/perl-5.8.8/lib/site_perl/5.8.8/sun4-solaris-thread-multi/Bundle/DBI.pm
#
