import MySQLdb
import time
import re
def index():
   
   # LOAD ALL DBS FROM THE DATABASE
   if len(request.args) == 1:
      sort_by = request.args(0)
      if cmp(sort_by, "name") == 0:
         sort_by = db.dbs.name
      elif cmp(sort_by, "user") == 0:
         sort_by = db.dbs.user
      elif cmp(sort_by, "port") == 0:
         sort_by = db.dbs.port
      elif cmp(sort_by, "status") == 0:
         sort_by = db.dbs.status
      elif cmp(sort_by, "san") == 0:
         sort_by = db.dbs.san_disk
      elif cmp(sort_by, "server") == 0:
         sort_by = db.servers.name
      else:
         sort_by = db.dbs.name
      dbs = db(db.dbs.servers_id == db.servers.id).select(db.dbs.ALL, db.servers.ALL, orderby=sort_by)
   else:
      dbs = db(db.dbs.servers_id == db.servers.id).select(db.dbs.ALL, db.servers.ALL, orderby=db.dbs.name)
   

   # GO THROUGH LOADED DATABASES & CREATE ARRAYS
   # THIS IS SO WE CAN HAVE THE DROP DOWN SELECT BOXES WHICH ARE CURRENTLY COMMENTED OUT.
   names = {}
   users = {}
   ports = {}
   statuses = {}
   sans = {}
   servers = {}
   
   for x in dbs:
      if not names.has_key(x.dbs.name):
         names[x.dbs.name] = x.dbs.name
      if not users.has_key(x.dbs.user):
         users[x.dbs.user] = x.dbs.user
      if not ports.has_key(x.dbs.port):
         ports[x.dbs.port] = x.dbs.port 
      if not statuses.has_key(x.dbs.status):
         statuses[x.dbs.status] = x.dbs.status
      if not sans.has_key(x.dbs.san_disk):
         sans[x.dbs.san_disk] = x.dbs.san_disk
      if not servers.has_key(x.servers.name):
         servers[x.servers.name] = x.servers.name     
   
   return dict(dbs=dbs, names=names, users=users, ports=ports, statuses=statuses, sans=sans, servers=servers)

def edit():
   print("dbs.edit")
   if(len(request.args) == 1 & request.args(0).isdigit()):
      dbs = db(db.dbs.id == request.args(0)).select(db.dbs.ALL, db.servers.id)
      try:
         thisdb = dbs[0]
      except:
         response.flash = "DB ID " + request.args(0) + " does not exist."
         redirect(URL(r=request, f='index'))
         
      if(request.vars):
         print("--------------- process form -----------------")
         print(request.vars)
         server_options = [OPTION("NONE", _value="NONE")] + [OPTION(server.name, _value=server.id) for server in db().select(db.servers.id, db.servers.name)]
         form = FORM(
                     TABLE(
                           TR(TH(H2('Modify ' + thisdb.dbs.name + ' DB'), _colspan=2, _align='center')),
                           TR('Name: ', INPUT(_type='text', _name='name', value=request.vars.name, _size=50)),
                           TR('User: ', INPUT(_type='text', _name='user', value=request.vars.user, _size=50)),
                           TR('Port: ', INPUT(_type='text', _name='port', value=request.vars.port, _size=50)),
                           TR('Status: ', SELECT(['NONE', 'Planned', 'Active', 'Sorry'], value=request.vars.status ,_name="status")),
                           TR('San Disk: ', SELECT(['NONE', 'EVA1', 'EVA2'], value=request.vars.san_disk, _name="san_disk")),
                           TR('Server ', SELECT(server_options, value=request.vars.server_id, _name="server_id")),
                           TR('Delete: ', INPUT(_type="checkbox", _name="delete", _onclick="return confirm('Are you sure you want to delete " + thisdb.dbs.name + "?');")),
                           TR(INPUT(_type='submit', _value='SUBMIT'))
                           )
                     )
         if(request.vars.delete is not None):
            print("DELETE THIS DB")
            if(has_child_rows(thisdb.dbs.id)):
               response.flash = "This DB is used by App Instances. You cannot delete a DB which is in use."
               return(dict(form=form))
            else:
               db(db.dbs.id == thisdb.dbs.id).delete()
               print("--------------")
               print(db._lastsql)
               print("--------------")
               redirect(URL(r=request, f='index'))
               return(dict())
         else:            
            if(not request.vars.name):
               response.flash = "Please define the DB name."
            elif(not request.vars.user):
               response.flash = "Please define the DB user."
            elif(not request.vars.port):
               response.flash = "Please define the port."
            elif(not re.search("^\d+$", request.vars.port)):
               response.flash = "Port must be only numbers."
            elif(re.search("^NONE$", request.vars.status)):
               response.flash = "Please select a status."
            elif(re.search("^NONE$", request.vars.san_disk)):
               response.flash = "Please select a San Disk."
            elif(re.search("^NONE$", request.vars.server_id)):
               response.flash = "Please select a Server."
            
            if(response.flash):
               #server_options = [OPTION("NONE", _value="NONE")] + [OPTION(server.name, _value=server.id) for server in db().select(db.servers.id, db.servers.name)]
               #form = FORM(
               #            TABLE(
               #                  TR(TH(H2('Modify ' + thisdb.dbs.name + ' DB'), _colspan=2, _align='center')),
               #                  TR('Name: ', INPUT(_type='text', _name='name', value=request.vars.name, _size=50)),
               #                  TR('User: ', INPUT(_type='text', _name='user', value=request.vars.user, _size=50)),
               #                  TR('Port: ', INPUT(_type='text', _name='port', value=request.vars.port, _size=50)),
               #                  TR('Status: ', SELECT(['NONE', 'Planned', 'Active', 'Sorry'], value=request.vars.status ,_name="status")),
               #                  TR('San Disk: ', SELECT(['NONE', 'EVA1', 'EVA2'], value=request.vars.san_disk, _name="san_disk")),
               #                  TR('Server ', SELECT(server_options, value=request.vars.server_id, _name="server_id")),
               #                  TR(INPUT(_type='submit', _value='SUBMIT'))
               #                  )
               #            )
               return dict(form=form)
            else:
               # All is good. process the form
               curtime = MySQLdb.TimestampFromTicks(time.time())
               db(db.dbs.id == thisdb.dbs.id).update(name = request.vars.name,
                       user = request.vars.user,
                       port = request.vars.port,
                       status = request.vars.status,
                       san_disk = request.vars.san_disk,
                       servers_id = request.vars.server_id,
                       updated_at = curtime)
               print(db._lastsql)
               redirect(URL(r=request, f='index'))
      else:
         server_options = [OPTION("NONE", _value="NONE")] + [OPTION(server.name, _value=server.id) for server in db().select(db.servers.id, db.servers.name)]
         form = FORM(
                     TABLE(
                           TR(TH(H2('Modify ' + thisdb.dbs.name + ' DB'), _colspan=2, _align='center')),
                           TR('Name: ', INPUT(_type='text', _name='name', value=thisdb.dbs.name, _size=50)),
                           TR('User: ', INPUT(_type='text', _name='user', value=thisdb.dbs.user, _size=50)),
                           TR('Port: ', INPUT(_type='text', _name='port', value=thisdb.dbs.port, _size=50)),
                           TR('Status: ', SELECT(['NONE', 'Planned', 'Active', 'Sorry'], value=thisdb.dbs.status ,_name="status")),
                           TR('San Disk: ', SELECT(['NONE', 'EVA1', 'EVA2'], value=thisdb.dbs.san_disk, _name="san_disk")),
                           TR('Server ', SELECT(server_options, value=thisdb.dbs.servers_id, _name="server_id")),
                           TR('Delete: ', INPUT(_type="checkbox", _name="delete", _onclick="return confirm('Are you sure you want to delete " + thisdb.dbs.name + "?');")),
                           TR(INPUT(_type='submit', _value='SUBMIT'))
                           )
                     )
         return dict(form=form)
         
   else:
      redirect(URL(r=request, f='index'))
   

def add():
   print("dbs.add")
   if(request.vars):
      print("process form")
      # Verify input
      if(not request.vars.name):
         response.flash = "Please define the DB name."
      elif(not request.vars.user):
         response.flash = "Please define the DB user."
      elif(not request.vars.port):
         response.flash = "Please define the port."
      elif(not re.search("^\d+$", request.vars.port)):
         response.flash = "Port must be only numbers."
      elif(re.search("^NONE$", request.vars.status)):
         response.flash = "Please select a status."
      elif(re.search("^NONE$", request.vars.san_disk)):
         response.flash = "Please select a San Disk."
      elif(re.search("^NONE$", request.vars.server_id)):
         response.flash = "Please select a Server."
      
      if(response.flash):
         server_options = [OPTION("NONE", _value="NONE")] + [OPTION(server.name, _value=server.id) for server in db().select(db.servers.id, db.servers.name)]
         form = FORM(
                     TABLE(
                           TR(TH(H2('Create DB'), _colspan=2, _align='center')),
                           TR('Name: ', INPUT(_type='text', _name='name', value=request.vars.name, _size=50)),
                           TR('User: ', INPUT(_type='text', _name='user', value=request.vars.user, _size=50)),
                           TR('Port: ', INPUT(_type='text', _name='port', value=request.vars.port, _size=50)),
                           TR('Status: ', SELECT(['NONE', 'Planned', 'Active', 'Sorry'], value=request.vars.status ,_name="status")),
                           TR('San Disk: ', SELECT(['NONE', 'EVA1', 'EVA2'], value=request.vars.san_disk, _name="san_disk")),
                           TR('Server ', SELECT(server_options, value=request.vars.server_id, _name="server_id")),
                           TR(INPUT(_type='submit', _value='SUBMIT'))
                           )
                     )
         return dict(form=form)
      else:
         # INSERT INTO DB
         curtime = MySQLdb.TimestampFromTicks(time.time())
         db.dbs.insert(name = request.vars.name,
                       user = request.vars.user,
                       port = request.vars.port,
                       status = request.vars.status,
                       san_disk = request.vars.san_disk,
                       servers_id = request.vars.server_id,
                       created_at = curtime,
                       updated_at = curtime)
         print(db._lastsql)
         redirect(URL(r=request, f='index'))
         return(dict())
   else:            
      server_options = [OPTION("NONE", _value="NONE")] + [OPTION(server.name, _value=server.id) for server in db().select(db.servers.id, db.servers.name)]
      form = FORM(
                  TABLE(
                        TR(TH(H2('Create DB'), _colspan=2, _align='center')),
                        TR('Name: ', INPUT(_type='text', _name='name', _size=50)),
                        TR('User: ', INPUT(_type='text', _name='user', _size=50)),
                        TR('Port: ', INPUT(_type='text', _name='port', _size=50)),
                        TR('Status: ', SELECT(['NONE', 'Planned', 'Active', 'Sorry'],_name="status")),
                        TR('San Disk: ', SELECT(['NONE', 'EVA1', 'EVA2'], _name="sandisk")),
                        TR('Server ', SELECT(server_options, _name="server_id")),
                        TR(INPUT(_type='submit', _value='SUBMIT'))
                        )
                  )
      return dict(form=form)
                        

def has_child_rows(dbid):
   dbsrs = db(db.dbs_to_app_instances.dbs_id == dbid).select(db.dbs_to_app_instances.id)
   if(dbsrs):
      return True
   else:
      return False
      
