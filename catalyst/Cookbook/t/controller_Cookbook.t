use strict;
use warnings;
use Test::More tests => 3;

BEGIN { use_ok 'Catalyst::Test', 'Cookbook' }
BEGIN { use_ok 'Cookbook::Controller::Cookbook' }

ok( request('/cookbook')->is_success, 'Request should succeed' );


