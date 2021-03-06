
db = DAL("mysql://config:C0nf1g@localhost/cmdb")

from gluon.tools import *
#auth=Auth(globals(),db)                      # authentication/authorization

#auth.settings.hmac_key='sha512:d396f739-d397-4e07-8e71-c11aba494810'
#auth.define_tables()                         # creates all needed tables
crud=Crud(globals(),db)                      # for CRUD helpers using auth
service=Service(globals())                   # for json, xml, jsonrpc, xmlrpc, amfrpc

#if auth.is_logged_in():
#   user_id = auth.user.id
#else:
#   user_id = None

db.define_table('environments',
   Field('name', 'string', length=45),
   Field('created_at', 'datetime', default=request.now),
   Field('updated_at', 'datetime', default=request.now),
   migrate=False)
db.environments.name.requires = [IS_NOT_EMPTY(), IS_NOT_IN_DB(db, 'environments.name')]
db.environments.created_at.readable = True
db.environments.created_at.writable = False
db.environments.updated_at.readable = True
db.environments.updated_at.writable = False
######################
#
######################
db.define_table('servers', 
   Field('name', 'string', length=45, notnull=True),
   Field('os', 'string', length=24, notnull=True),
   Field('created_at', 'datetime', default=request.now, notnull=True),
   Field('updated_at', 'datetime', default=request.now, notnull=True),
   migrate=False)
db.servers.name.requires = [IS_NOT_EMPTY(), IS_NOT_IN_DB(db, 'servers.name')]
db.servers.created_at.readable = True
db.servers.created_at.writable = False
db.servers.updated_at.readable = True
db.servers.updated_at.writable = False
######################
#
######################
db.define_table('dbs', 
   Field('name', 'string', length=45, notnull=True),
   Field('user', 'string', length=45, notnull=True),
   Field('port', 'integer', notnull=True),
   Field('status', 'string', length=10, notnull=True),
   Field('san_disk', 'string', length=10, notnull=True),
   Field('servers_id', db.servers),
   Field('created_at', 'datetime', default=request.now),
   Field('updated_at', 'datetime', default=request.now),
   migrate=False)
db.dbs.name.requires = [IS_NOT_EMPTY(), IS_NOT_IN_DB(db, 'dbs.name')]
db.dbs.user.requires = IS_NOT_EMPTY()
db.dbs.port.requires = IS_NOT_EMPTY()
db.dbs.status.requires = IS_NOT_EMPTY()
db.dbs.san_disk.requires = IS_NOT_EMPTY()
db.dbs.servers_id.requires = IS_IN_DB(db, 'servers.id', 'servers.name')
db.dbs.servers_id.readable = True
db.dbs.servers_id.writable = False
db.dbs.created_at.readable = True
db.dbs.created_at.writable = False
db.dbs.updated_at.readable = True
db.dbs.updated_at.writable = False
######################
#
######################
db.define_table('env_2_db',
   Field('dbs_id', db.dbs, notnull=True),
   Field('environments_id', db.environments, notnull=True),
   Field('created_at', 'datetime', default=request.now, notnull=True),
   Field('updated_at', 'datetime', default=request.now, notnull=True),
   migrate=False)
db.env_2_db.dbs_id.requires = IS_IN_DB(db, 'dbs.id', 'dbs.name')
db.env_2_db.environments_id.requires = IS_IN_DB(db, 'environments.id', 'environments.name')
db.env_2_db.created_at.readable = True
db.env_2_db.created_at.writable = False
db.env_2_db.updated_at.readable = True
db.env_2_db.updated_at.writable = False
######################
#
######################
db.define_table('env_2_server',
   Field('servers_id', db.servers, notnull=True),
   Field('environments_id', db.environments, notnull=True),
   Field('created_at', 'datetime', default=request.now, notnull=True),
   Field('updated_at', 'datetime', default=request.now, notnull=True),
   migrate=False)
db.env_2_server.servers_id.requires = IS_IN_DB(db, 'servers.id', 'servers.name')
db.env_2_server.environments_id.requires = IS_IN_DB(db, 'environments.id', 'environments.name')
db.env_2_server.created_at.readable = True
db.env_2_server.created_at.writable = False
db.env_2_server.updated_at.readable = True
db.env_2_server.updated_at.writable = False








