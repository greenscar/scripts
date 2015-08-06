db.define_table('groups',
   Field('name', 'string'),
   Field('created_at', 'datetime', default=request.now),
   Field('updated_at', 'datetime', default=request.now),
   migrate=False)
db.groups.name.requires = [IS_NOT_EMPTY(), IS_NOT_IN_DB(db, 'groups.name')]
db.groups.created_at.readable = True
db.groups.created_at.writable = False
db.groups.updated_at.readable = True
db.groups.updated_at.writable = False
######################
#
######################
db.define_table('processes',
   Field('name', 'string', length=45),
   Field('name_friendly', 'string', length=45),
   Field('groups_id', db.groups),
   Field('created_at', 'datetime', default=request.now),
   Field('updated_at', 'datetime', default=request.now),
   migrate=False)
db.processes.name.requires = [IS_NOT_EMPTY(), IS_NOT_IN_DB(db, 'processes.name')]
db.processes.groups_id.requires = IS_IN_DB(db, 'groups.id', 'groups.name')
db.processes.created_at.readable = True
db.processes.created_at.writable = False
db.processes.updated_at.readable = True
db.processes.updated_at.writable = False
######################
#
######################
db.define_table('apps',
   Field('name', 'string', length=45),
   Field('mbean', 'string', length=45),
   Field('alive_check', 'string', length=45),
   Field('processes_id', db.processes),
   Field('created_at', 'datetime', default=request.now),
   Field('updated_at', 'datetime', default=request.now),
   migrate=False)
db.apps.name.requires = [IS_NOT_EMPTY(), IS_NOT_IN_DB(db, 'apps.name')]
db.apps.mbean.requires = IS_NOT_EMPTY()
db.apps.processes_id.requires = IS_IN_DB(db, 'processes.id', 'processes.name')
db.apps.created_at.readable = True
db.apps.created_at.writable = False
db.apps.updated_at.readable = True
db.apps.updated_at.writable = False
