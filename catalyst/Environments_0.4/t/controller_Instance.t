use strict;
use warnings;
use Test::More tests => 3;

BEGIN { use_ok 'Catalyst::Test', 'Environments' }
BEGIN { use_ok 'Environments::Controller::Instance' }

ok( request('/instance')->is_success, 'Request should succeed' );


