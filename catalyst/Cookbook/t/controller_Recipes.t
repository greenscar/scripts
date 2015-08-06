use strict;
use warnings;
use Test::More tests => 3;

BEGIN { use_ok 'Catalyst::Test', 'Cookbook' }
BEGIN { use_ok 'Cookbook::Controller::Recipes' }

ok( request('/recipes')->is_success, 'Request should succeed' );


