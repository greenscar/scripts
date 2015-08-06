use strict;
use warnings;
use Test::More tests => 3;

BEGIN { use_ok 'Catalyst::Test', 'Environments' }
BEGIN { use_ok 'Environments::Controller::EM' }

ok( request('/em')->is_success, 'Request should succeed' );


