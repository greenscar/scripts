use strict;
use warnings;
use Test::More tests => 3;

BEGIN { use_ok 'Catalyst::Test', 'Environments' }
BEGIN { use_ok 'Environments::Controller::DM' }

ok( request('/dm')->is_success, 'Request should succeed' );


