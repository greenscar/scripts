use strict;
use warnings;
use Test::More tests => 3;

BEGIN { use_ok 'Catalyst::Test', 'Cookbook' }
BEGIN { use_ok 'Cookbook::Controller::Category' }

ok( request('/category')->is_success, 'Request should succeed' );


