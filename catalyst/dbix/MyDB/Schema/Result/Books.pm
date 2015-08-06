package MyDB::Schema::Result::Books;
use base qw/DBIx::Class/;

__PACKAGE__->load_components(qw/Core/);
__PACKAGE__->table('BOOK');
__PACKAGE__->add_columns(qw/ ID TITLE RATING/);
__PACKAGE__->set_primary_key('ID');
1;
