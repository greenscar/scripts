import MySQLdb
import time
import re
# coding: utf8
# try something like
def index(): 
   if len(request.args) == 1:
      sort_by = request.args(0)
      if cmp(sort_by, "group_name") == 0:
         order_by = db.groups.name|db.processes.name
      elif cmp(sort_by, "service_name") == 0:
         order_by=db.processes.name|db.groups.name|db.apps.name
      elif cmp(sort_by, "app_name") == 0:
         order_by=db.apps.name|db.groups.name|db.processes.name
      elif cmp(sort_by, "mbean") == 0:
         order_by=db.apps.mbean|db.groups.name|db.apps.name
      elif cmp(sort_by, "alive_check") == 0:
         order_by=db.apps.alive_check|db.groups.name|db.apps.name
      elif cmp(sort_by, "client") == 0:
         order_by = db.customers.name|db.groups.name|db.apps.name
      else:
         order_by = db.app_instances.id
   else:
      order_by = db.app_instances.id
   instances = db((db.servers.id == db.process_instances.servers_id)
                 &
                 (db.customers.id == db.process_instances.customers_id)
                 &
                 (db.app_instances.apps_id == db.apps.id)
                 &
                 (db.app_instances.process_instances_id == db.process_instances.id)
                 &
                 (db.process_instances.processes_id == db.processes.id)
                 &
                 (db.groups.id == db.processes.groups_id)).select(
                     db.groups.ALL, db.servers.ALL, db.customers.ALL, db.processes.ALL, db.apps.ALL, db.process_instances.ALL, db.app_instances.ALL,
                     #left=(db.app_instances.on(db.process_instances.id == db.app_instances.process_instances_id)),
                     orderby=order_by)
   #
   # LEFT JOIN IS NOT WORKING CORRECTLY IN WEB2PY. THEREFORE, WE MUST LOAD THE DB AS A SECOND QUERY RATHER THAN ONE LONG QUERY.
   #
   print("++++++++++++++++++++++++++++++++++++++")
   for i in instances:
      rs = db(db.app_instances.id == i.app_instances.id).select(db.app_instances.ALL, db.dbs.ALL,
            left=(db.dbs_to_app_instances.on(db.app_instances.id == db.dbs_to_app_instances.app_instances_id),
                db.dbs.on(db.dbs.id == db.dbs_to_app_instances.dbs_id)
                ))[0]
      i.dbs = rs.dbs
   print("++++++++++++++++++++++++++++++++++++++")
   return dict(instances=instances)
   
def edit():
   print("app_instances.edit")
   if(len(request.args) == 1 & request.args(0).isdigit()):
      print("args = " + request.args[0])
      instance = db_load_instance(request.args[0])
      if(not instance):
         # IF PROBLEMS LOADING INSTANCE, RETURN TO INDEX
         redirect(URL(r=request, f='index'))
         return(dict())         
      elif(request.vars):
         # REQUEST VARS EXIST. PROCESS FORM.
         print("-------------- process form ------------------")
         dbid = request.vars.dbs_id
         print(request.vars)
         dbs_to_app_instances_id = request.vars.dbs_to_app_instances_id
         print("dbs_to_app_instances_id = '" + str(dbs_to_app_instances_id) + "'")
         
         # IF DBID == NONE & ORIGINAL VALUE = NONE, DO NOTHING
         # IF DBID == NONE & ORIGINAL VALUE = DEFINED, DELETE
         # IF DBID == VALUE & ORIGINAL VALUE = DEFINED, UPDATE
         # IF DBID == VALUE & ORIGINAL VALUE = NONE, INSERT
         if(dbid == "NONE"):
            if(re.search("^0$", dbs_to_app_instances_id)):
               print("Original DB value was nill. New DB value is nill. DO NOTHING")
            else:
               print("Original DB value was defined. New DB value is nill. DELETE")
               # DELETE
               db(db.dbs_to_app_instances.id == dbs_to_app_instances_id).delete()
         else:
            curtime = MySQLdb.TimestampFromTicks(time.time())
            if(re.search("^0$", dbs_to_app_instances_id)):
               print("Original DB value was nill. New DB value is defined. INSERT")
               db.dbs_to_app_instances.insert(dbs_id = dbid, app_instances_id = request.args(0), created_at = curtime, updated_at = curtime)
            else:
               print("Original DB value was defined. New DB value is defined. UPDATE")
               db(db.dbs_to_app_instances.id == dbs_to_app_instances_id).update(dbs_id = dbid, updated_at = curtime)
         print("App Instance successfully updated in DB")
         redirect(URL(r=request, f='index'))
         return(dict())
      else:
         # REQUEST VARS DO NOT EXIST. CREATE FORM
         db_options = [OPTION("NONE", _value="NONE")] + [OPTION(database.name, _value=database.id) for database in db().select(db.dbs.id, db.dbs.name)]
         print("dbs.id = " + str(instance.dbs.id))
         if instance.dbs.id:
            dbval = instance.dbs.id
         else:
            dbval = "NONE"
         if(instance.dbs_to_app_instances.id):
            dbs_to_app_instances_id = instance.dbs_to_app_instances.id
         else:
            dbs_to_app_instances_id = 0
         form = FORM(
                      INPUT(_type="hidden", _name="dbs_to_app_instances_id", _value=dbs_to_app_instances_id),
                      TABLE(
                           TR(TH(H2('Modify ' + instance.processes.name + " " + instance.apps.name), _colspan=2, _align='center')),
                           TR('Service ', instance.processes.name),
                           TR('App ', instance.apps.name),
                           TR('Server ', instance.servers.name),
                           TR('DB ', SELECT(db_options, value=dbval, _name="dbs_id")),
                           TR("",(INPUT(_type="submit", _value="Submit"), " ", INPUT(_type="button", _value="Cancel", _onclick="parent.location='" + URL(r=request, f='index') + "'")))                           
                          )
                    )         
         return(dict(form=form))
   else:
      redirect(URL(r=request, f='index'))
      return(dict())
      
def db_load_instance(app_instance_id):
   print("db_load_instance(" + app_instance_id + ")")
   instance = db(
                    (db.app_instances.id == app_instance_id)
                  & (db.process_instances.servers_id == db.servers.id)
                  & (db.processes.id == db.process_instances.processes_id)
                  & (db.process_instances.customers_id == db.customers.id)
                  & (db.groups.id == db.processes.groups_id)
                  #& ((db.process_instances.id == db.db_2_process_instances.process_instances_id) & (db.dbs.id == db.db_2_process_instances.dbs_id))
                  & ((db.app_instances.apps_id == db.apps.id) & (db.app_instances.process_instances_id == db.process_instances.id))
                 ).select(
                           db.app_instances.ALL,
                           db.apps.ALL,
                           db.process_instances.ALL,
                           db.processes.ALL,
                           db.servers.ALL, 
                           db.customers.ALL,
                           db.processes.ALL, 
                           #db.dbs.ALL,
                           db.groups.ALL,
                           left=(
                                    db.process_instances.on(db.process_instances.id == db.app_instances.process_instances_id)
                                ), 
                           groupby=db.apps.id
                           )[0]
   #print(instance)
   #print("--------------")
   #print("load db")
   # Load DB
   #print("--------------")
   rs = db(db.app_instances.id == instance.app_instances.id).select(db.app_instances.ALL, db.dbs.ALL, db.dbs_to_app_instances.ALL,
         left=(db.dbs_to_app_instances.on(db.app_instances.id == db.dbs_to_app_instances.app_instances_id),
             db.dbs.on(db.dbs.id == db.dbs_to_app_instances.dbs_id)
             ))[0]
   
   instance.dbs = rs.dbs
   instance.dbs_to_app_instances = rs.dbs_to_app_instances
   print("--------------======================-------------")
   print(rs.dbs_to_app_instances)
   print("--------------======================-------------")
   #print("--------------")
   #
   #print("--------------")
   #print(db._lastsql)
   #print("--------------")
   return instance