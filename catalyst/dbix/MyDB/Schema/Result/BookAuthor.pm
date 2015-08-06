package MyDB::Schema::Result::BookAuthor;

use strict;
use warnings;

use base 'DBIx::Class';

__PACKAGE__->load_components("InflateColumn::DateTime", "TimeStamp", "Core");
__PACKAGE__->table("book_author");
__PACKAGE__->add_columns(
  "book_id",
  { data_type => "NUMBER", default_value => undef, is_nullable => 0, size => 38 },
  "author_id",
  { data_type => "NUMBER", default_value => undef, is_nullable => 0, size => 38 },
);
__PACKAGE__->set_primary_key("book_id", "author_id");


# Created by DBIx::Class::Schema::Loader v0.04006 @ 2009-09-15 09:56:18
# DO NOT MODIFY THIS OR ANYTHING ABOVE! md5sum:jX6SBpu7N7uUeBentbv+Hw


# You can replace this text with custom content, and it will be preserved on regeneration
1;
