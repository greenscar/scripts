import MySQLdb
import time
import re
def index():  
   print("process_instances.index")
   if(len(request.args) == 1):
      instances = dbload(request.args(0))
   else:
      instances = dbload()
   return dict(instances=instances)
   
def edit():
   """
   Edit a particular process instance
   """
   print("instances.edit")
   if(len(request.args) == 1 & request.args(0).isdigit()):
      # User has selected an instance to edit. Show the form.
      print("args = " + request.args[0])
      process_completed = False
      # Check form for processing. If good, update data in DB. If bad, forward back to form.
      instance = db_load_instance(request.args[0])
      process_options = [OPTION(process.name, _value=process.id) for process in db().select(db.processes.id, db.processes.name)]
      server_options = [OPTION(server.name, _value=server.id) for server in db().select(db.servers.id, db.servers.name)]
      customer_options = [OPTION(customer.name, _value=customer.id) for customer in db().select(db.customers.id, db.customers.name)]
      db_options = [OPTION(database.name, _value=database.id) for database in db().select(db.dbs.id, db.dbs.name)]
      print("loading instance")
      this_instance = db_load_instance(request.args(0))
      print("---------------------------------------")
      #print(this_instance.servers.name)
      print("active = " + str(this_instance.process_instances.active))
      print("---------------------------------------")
      print("instance loaded")
      form = FORM(TABLE(
                        TR(TH(H2('Modify ' + this_instance.processes.name + " @ " + this_instance.servers.name), _colspan=3, _align='center')),
                        TR('Customer ', SELECT(customer_options, value=this_instance.customers.id, _name="customer_id")),
                        TR('Process ', SELECT(process_options, value=this_instance.processes.id, _name="process_id")),
                        TR('Server ', SELECT(server_options, value=this_instance.servers.id, _name="server_id")),
                        TR('JMX Port ', INPUT(_type="text", _name="port_jmx", value=this_instance.process_instances.jmx_port, requires=IS_INT_IN_RANGE(2000,100000))),
                        TR('HTTP Port ', INPUT(_type="text", _name="port_http", value=this_instance.process_instances.http_port, requires=IS_INT_IN_RANGE(2000,100000))),
                        TR('Active ', ('YES ', INPUT(_type="radio", _name="active", _value='True', value=str(this_instance.process_instances.active)), ' NO ', INPUT(_type="radio", _name="active", _value='False', value=str(this_instance.process_instances.active)))),
                        TR('Delete ', INPUT(_type="checkbox", _name="delete", _onclick="return confirm('Are you sure you want to delete " + this_instance.processes.name + " @ " + this_instance.servers.name + "?');")),
                        TR("",(INPUT(_type="submit", _value="Submit"), " ", INPUT(_type="button", _value="Cancel", _onclick="parent.location='" + URL(r=request, f='index') + "'"))),
                       )
                 )
      # If the request vars are defined, the user has submitted the form.
      if(request.vars):
         print "Processing update."
         # IF DELETE CHECKED, DELETE FROM DATABASE.
         if(request.vars.delete is not None):
            print("DELETE THIS ONE")
            # 1) DELETE ALL DB_TO_APP_INSTANCES THESE APP INSTANCES ARE POINTING TO.
            db(db.dbs_to_app_instances.app_instances_id.belongs((db(db.app_instances.process_instances_id == this_instance.process_instances.id).select(db.app_instances.id)))).delete()
            # 1) DELETE ALL APP INSTANCES WHO ARE A PART OF THIS PROCESS INSTANCE.
            db(db.app_instances.process_instances_id == this_instance.process_instances.id).delete()
            # 2) DELETE THE PROCESS INSTANCE ITSELF.
            db(db.process_instances.id == this_instance.process_instances.id).delete()
            redirect(URL(r=request, f='index'))
            return(dict())
         else:
            # ELSE PROCESS FORM FOR UPDATE
            curtime = MySQLdb.TimestampFromTicks(time.time())
            process_id = request.vars.process_id
            customer_id = request.vars.customer_id
            server_id = request.vars.server_id
            db_id = request.vars.db_id
            isactive = request.vars.active
            port_jmx = request.vars.port_jmx
            port_http = request.vars.port_http
            all_digits = "^(\d+)$"
            
            if(customer_id == "NONE"):
               response.flash = "Please select a Customer"  
               return dict(form=form)
            elif(process_id == "NONE"):
               response.flash = "Please select a Service"
               return dict(form=form)
            elif(server_id == "NONE"):
               response.flash = "Please select a Server"
               return dict(form=form)
            elif port_jmx and not re.search(all_digits, port_jmx):
               response.flash = "JMX Port must be a number or empty."
               return dict(form=form)
            elif port_http and not re.search(all_digits, port_http):
               response.flash = "HTTP Port must be a number or empty."
               return dict(form=form)
            else:
               # PROCESS FORM & INSERT INTO DB
               print("Process Form")
               curtime = MySQLdb.TimestampFromTicks(time.time())
               db(db.process_instances.id == this_instance.process_instances.id).update(processes_id = process_id, servers_id = server_id, customers_id = customer_id, jmx_port = port_jmx, http_port = port_http, active = isactive, updated_at = curtime)
               print("--------------")
               print(db._lastsql)
               print("--------------")
               print("Service Instance successfully inserted into DB")
               redirect(URL(r=request, f='index'))
               return(dict())
      else:
         return dict(form=form)
   else:
      redirect(URL(r=request,f='index'))
   
   
def add():
   print("instances.add")
   process_options = [OPTION("NONE", _value="NONE")] + [OPTION(process.name, _value=process.id) for process in db().select(db.processes.id, db.processes.name)]
   server_options = [OPTION("NONE", _value="NONE")] + [OPTION(server.name, _value=server.id) for server in db().select(db.servers.id, db.servers.name)]
   customer_options = [OPTION("NONE", _value="NONE")] + [OPTION(customer.name, _value=customer.id) for customer in db().select(db.customers.id, db.customers.name)]
   db_options = [OPTION("NONE", _value="NONE")] + [OPTION(database.name, _value=database.id) for database in db().select(db.dbs.id, db.dbs.name)]
   
   if(request.vars):
      curtime = MySQLdb.TimestampFromTicks(time.time())
      process_id = request.vars.process_id
      customer_id = request.vars.customer_id
      server_id = request.vars.server_id
      db_id = request.vars.db_id
      isactive = request.vars.active
      port_jmx = request.vars.port_jmx
      port_http = request.vars.port_http
      form = FORM(TABLE(
                     TR(TH(H2('Create Process Instance'), _colspan=3, _align='center')),
                     TR('Customer ', SELECT(customer_options, value=customer_id, _name="customer_id")),
                     TR('Process ', SELECT(process_options, value=process_id, _name="process_id")),
                     TR('Server ', SELECT(server_options, value=server_id, _name="server_id")),
                     TR('JMX Port ', INPUT(_type="text", _name="port_jmx", value=request.vars.port_jmx, requires=IS_INT_IN_RANGE(2000,100000))),
                     TR('HTTP Port ', INPUT(_type="text", _name="port_http", value=request.vars.port_http, requires=IS_INT_IN_RANGE(2000,100000))),
                     TR('Active ', ('YES ', INPUT(_type="radio", _name="active", _value='True', value=isactive), ' NO ', INPUT(_type="radio", _name="active", _value='False', value=isactive))),
                     TR("",(INPUT(_type="submit", _value="Submit"), " ", INPUT(_type="button", _value="Cancel", _onclick="parent.location='" + URL(r=request, f='index') + "'")))
                     #TR(TH(H2('Modify ' + this_instance.processes.name), _colspan=3, _align='center')),
                    )
              )
      all_digits = "^\d+$"
      if(customer_id == "NONE"):
         response.flash = "Please select a Customer"  
      elif(process_id == "NONE"):
         response.flash = "Please select a Service"
      elif(server_id == "NONE"):
         response.flash = "Please select a Server"
      elif port_jmx and not re.search(all_digits, port_jmx):
         response.flash = "JMX Port must be a number or empty."
      elif port_http and not re.search(all_digits, port_http):
         response.flash = "HTTP Port must be a number or empty."
      
      if(response.flash):
         return dict(form=form)
      else:
         # PROCESS FORM & INSERT INTO DB
         print("Process Form")
         curtime = MySQLdb.TimestampFromTicks(time.time())
         print("processes_id = " + process_id)
         print("servers_id = " + server_id)
         print("customers_id = " + customer_id)
         print("active = " + isactive)
         print("jmx_port = " + port_jmx)
         print("http_port = " + port_http)
         
         process_instance_id = db.process_instances.insert(processes_id = process_id,
                                                           servers_id = server_id,
                                                           jmx_port = port_jmx,
                                                           http_port = port_http,
                                                           customers_id = customer_id,
                                                           active = isactive,
                                                           created_at = curtime,
                                                           updated_at = curtime)
         print("--------------")
         print(db._lastsql)
         print("++++++++++++++++++++++++++")
         print(process_instance_id)
         print("++++++++++++++++++++++++++")
         #print("process_instance_id = " + process_instance_id)
         print("--------------")
         print("Service Instance successfully inserted into DB")
         # NOW THAT WE HAVE INSERTED THE PROCESS_INSTANCE, INSERT APP_INSTANCES BASED ON WHAT APPS ARE IN THIS PROCESS
         # 1) FIGURE OUT WHAT APPS WE NEED TO CREATE INSTANCES FOR
         appsrs = db(db.apps.processes_id == process_id).select(db.apps.id)
         # 2) INSERT ROWS INTO APP_INSTANCES FOR EACH APP
         for row in appsrs:
            db.app_instances.insert(apps_id = row.id,
                                    process_instances_id = process_instance_id,
                                    active = isactive,
                                    created_at = curtime,
                                    updated_at = curtime)
            print(db._lastsql)
         redirect(URL(r=request, f='index'))
         return(dict())
   else:
      form = FORM(TABLE(
                     TR(TH(H2('Create Process Instance'), _colspan=3, _align='center')),
                     TR('Customer ', SELECT(customer_options, _name="customer_id")),
                     TR('Process ', SELECT(process_options, _name="process_id")),
                     TR('Server ', SELECT(server_options, _name="server_id")),
                     TR('JMX Port ', INPUT(_type="text", _name="port_jmx", requires=IS_INT_IN_RANGE(2000,100000))),
                     TR('HTTP Port ', INPUT(_type="text", _name="port_http", requires=IS_INT_IN_RANGE(2000,100000))),
                     TR('Active ', ('YES ', INPUT(_type="radio", _name="active", _value='True', value='True'), ' NO ', INPUT(_type="radio", _name="active", _value='False', value='True'))),
                     TR("",(INPUT(_type="submit", _value="Submit"), " ", INPUT(_type="button", _value="Cancel", _onclick="parent.location='" + URL(r=request, f='index') + "'")))
                     #TR(TH(H2('Modify ' + this_instance.processes.name), _colspan=3, _align='center')),
                    )
              )
      return dict(form=form)
   
   
def db_load_instance(process_instance_id):
   print("db_load_instance(" + process_instance_id + ")")
   instances = db(
                  (db.process_instances.id == process_instance_id)
                  & (db.process_instances.servers_id == db.servers.id)
                  & (db.processes.id == db.process_instances.processes_id)
                  & (db.process_instances.customers_id == db.customers.id)
                  & (db.groups.id == db.processes.groups_id)
                 ).select(
                           db.process_instances.ALL,
                           db.servers.ALL, 
                           db.customers.ALL,
                           db.processes.ALL,
                           db.groups.ALL
                         )
   print("--------------")
   print(db._lastsql)
   print("--------------")
   return instances[0]
   
   
   
   
def dbload(sort_by="customer"):
   print("instances.dbload")
   if cmp(sort_by, "customer") == 0:
      order_by = db.customers.name
   elif cmp(sort_by, "group") == 0:
      order_by = db.groups.name
   elif cmp(sort_by, "process") == 0:
      order_by = db.processes.name
   elif cmp(sort_by, "jmx_port") == 0:
      order_by = db.process_instances.jmx_port | db.groups.name | db.processes.name
   elif cmp(sort_by, "http_port") == 0:
      order_by = db.process_instances.http_port | db.groups.name | db.processes.name
   elif cmp(sort_by, "server") == 0:
      order_by = db.servers.name
   elif cmp(sort_by, "db") == 0:
      order_by = db.dbs.name
   else:
      order_by = db.processes.name
   rs = db(
            (db.process_instances.processes_id == db.processes.id)
            &
            (db.process_instances.customers_id == db.customers.id)
            &
            (db.process_instances.servers_id == db.servers.id)
            &
            (db.processes.groups_id == db.groups.id)
          ).select(
                     db.processes.ALL, db.customers.ALL, db.groups.ALL, db.servers.ALL, db.process_instances.ALL,       
                     orderby = order_by
                   )
   return rs