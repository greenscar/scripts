
use CQPerlExt;
# Create ClearQuest session
$session = CQSession::Build();
# Login to the ClearQuest database
$session->UserLogon("ccbuild", "ClearQuest", "TAA", "7.0.0");
$ednames = $session->GetQueryEntityDefNames();
foreach $name (@$ednames)
{
     print("name -> $name\n");
}
# Close CQ connection
CQSession::Unbuild($session);

