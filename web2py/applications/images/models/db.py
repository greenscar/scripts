from gluon.tools import Crud
from gluon.tools import Auth

db = DAL("mysql://web2py:web2py@localhost/web2py")
   
db.define_table('image',
   Field('title'),
   Field('file', 'upload'))

db.define_table('comment',
   Field('image_id', db.image),
   Field('author'),
   Field('email'),
   Field('body', 'text'))

db.image.title.requires = [IS_NOT_EMPTY(),
                         IS_NOT_IN_DB(db, db.image.title)]

db.comment.image_id.requires = IS_IN_DB(db, db.image.id, 'db.image.title')
db.comment.author.requires = IS_NOT_EMPTY()
db.comment.email.requires = IS_EMAIL()
db.comment.body.requires = IS_NOT_EMPTY()

db.comment.image_id.writable = db.comment.image_id.readable = False

# use CRUD
crud = Crud(globals(), db)

# use AUTH
auth = Auth(globals(), db)
auth.define_tables()
