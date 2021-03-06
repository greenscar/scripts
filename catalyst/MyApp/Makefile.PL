#!/usr/bin/env perl
# IMPORTANT: if you delete this file your app will not work as
# expected.  You have been warned.
use inc::Module::Install;

name 'MyApp';
all_from 'lib/MyApp.pm';

requires 'Catalyst::Runtime' => '5.80004';
requires 'Catalyst::Plugin::ConfigLoader';
requires 'Catalyst::Plugin::Static::Simple';
requires 'Catalyst::Action::RenderView';
requires 'Catalyst::Plugin::StackTrace';
requires (
   'Catalyst::Plugin::Authentication' => '0',
   'Catalyst::Plugin::Session' => '0',
   'Catalyst::Plugin::Session::Store::FastMmap' => '0',
   'Catalyst::Plugin::Session::State::Cookie' => '0',        
   'Catalyst::Plugin::Authorization::Roles' => '0'
);
requires 'parent';
requires 'Config::General'; # This should reflect the config file format you've chosen
                 # See Catalyst::Plugin::ConfigLoader for supported formats
catalyst;

install_script glob('script/*.pl');
auto_install;
WriteAll;
