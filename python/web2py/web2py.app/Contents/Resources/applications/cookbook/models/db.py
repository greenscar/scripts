# -*- coding: utf-8 -*- 

#########################################################################
## This scaffolding model makes your app work on Google App Engine too
#########################################################################

if request.env.web2py_runtime_gae:            # if running on Google App Engine
   db = DAL('gae')                           # connect to Google BigTable
   session.connect(request, response, db=db) # and store sessions and tickets there
   ### or use the following lines to store sessions in Memcache
   # from gluon.contrib.memdb import MEMDB
   # from google.appengine.api.memcache import Client
   # session.connect(request, response, db=MEMDB(Client())
else:                                         # else use a normal relational database
   #db = DAL('sqlite://storage.sqlite')       # if not, use SQLite or other DB
   import datetime; now=datetime.date.today()
   db=SQLDB('mysql://web2py:web2py@localhost:3306/web2py')
   db.define_table('category', SQLField('name'))
   
   db.define_table('recipe',
                  SQLField('title'),
                  SQLField('description', length=256),
                  SQLField('category', db.category),
                  SQLField('date', 'date', default=now),
                  SQLField('instructions', 'text'))

   db.category.name.requires=[IS_NOT_EMPTY(), IS_NOT_IN_DB(db, 'category.name')]
   db.recipe.title.requires=[IS_NOT_EMPTY()]
   db.recipe.description.requires=IS_NOT_EMPTY()
   db.recipe.category.requires=IS_IN_DB(db, 'category.id', 'category.name')
   db.recipe.date.requires=IS_DATE()
   
## if no need for session
# session.forget()

#########################################################################
## Here is sample code if you need for 
## - email capabilities
## - authentication (registration, login, logout, ... )
## - authorization (role based authorization)
## - services (xml, csv, json, xmlrpc, jsonrpc, amf, rss)
## - crud actions
## comment/uncomment as needed

from gluon.tools import *
auth=Auth(globals(),db)                      # authentication/authorization
auth.settings.hmac_key='sha512:4bb1d652-006d-42e8-8d37-1eb5306ed957'
auth.define_tables()                         # creates all needed tables
crud=Crud(globals(),db)                      # for CRUD helpers using auth
service=Service(globals())                   # for json, xml, jsonrpc, xmlrpc, amfrpc

# crud.settings.auth=auth                      # enforces authorization on crud
# mail=Mail()                                  # mailer
# mail.settings.server='smtp.gmail.com:587'    # your SMTP server
# mail.settings.sender='you@gmail.com'         # your email
# mail.settings.login='username:password'      # your credentials or None
# auth.settings.mailer=mail                    # for user email verification
# auth.settings.registration_requires_verification = True
# auth.settings.registration_requires_approval = True
# auth.messages.verify_email = \
#  'Click on the link http://.../user/verify_email/%(key)s to verify your email'
## more options discussed in gluon/tools.py
#########################################################################

#########################################################################
## Define your tables below, for example
##
## >>> db.define_table('mytable',Field('myfield','string'))
##
## Fields can be 'string','text','password','integer','double','boolean'
##       'date','time','datetime','blob','upload', 'reference TABLENAME'
## There is an implicit 'id integer autoincrement' field
## Consult manual for more options, validators, etc.
##
## More API examples for controllers:
##
## >>> db.mytable.insert(myfield='value')
## >>> rows=db(db.mytable.myfield=='value').select(db.mytable.ALL)
## >>> for row in rows: print row.id, row.myfield
#########################################################################