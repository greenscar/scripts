import MySQLdb
import time
import re
def index():
   if len(request.args) == 1:
      sort_by = request.args(0)
      if cmp(sort_by, "group_name") == 0:
         processes = db((db.groups.id == db.processes.groups_id) & (db.processes.id == db.apps.processes_id)).select(orderby=db.groups.name|db.processes.name, groupby=db.apps.id)
      elif cmp(sort_by, "service_name") == 0:
         processes = db((db.groups.id == db.processes.groups_id) & (db.processes.id == db.apps.processes_id)).select(orderby=db.processes.name|db.groups.name|db.apps.name, groupby=db.apps.id)
      elif cmp(sort_by, "app_name") == 0:
         processes = db((db.groups.id == db.processes.groups_id) & (db.processes.id == db.apps.processes_id)).select(orderby=db.apps.name|db.groups.name|db.processes.name, groupby=db.apps.id)
      elif cmp(sort_by, "mbean") == 0:
         processes = db((db.groups.id == db.processes.groups_id) & (db.processes.id == db.apps.processes_id)).select(orderby=db.apps.mbean, groupby=db.apps.id)
      elif cmp(sort_by, "alive_check") == 0:
         processes = db((db.groups.id == db.processes.groups_id) & (db.processes.id == db.apps.processes_id)).select(orderby=db.apps.alive_check, groupby=db.apps.id)
   else:
      processes = db((db.groups.id == db.processes.groups_id) & (db.processes.id == db.apps.processes_id)).select(orderby=db.groups.name|db.processes.name, groupby=db.apps.id)
   return dict(processes=processes)
      
def edit():
   print("apps.edit")
   apps = db((db.processes.id == db.apps.processes_id) & (db.apps.id == request.args(0))).select(db.apps.ALL, db.processes.ALL, groupby=db.apps.id)
   try:
      thisapp=apps[0]
   except:
      redirect(URL(r=request, f='index'))
      return(dict())
   print(thisapp)
   if ((len(request.args) == 1) & (request.args(0).isdigit())):
      if(request.vars):
         print("--------------- process form ---------------")
         process_id = request.vars.process_id
         app_name = request.vars.app_name
         mbean_val = request.vars.mbean
         alivecheck = request.vars.alivecheck
         group_options = [OPTION("NONE", _value="NONE")] + [OPTION(group.name, _value=group.id) for group in db().select(db.groups.id, db.groups.name)]
         process_options = [OPTION("NONE", _value="NONE")] + [OPTION(process.name, _value=process.id) for process in db().select(db.processes.id, db.processes.name)]
         form = FORM(TABLE(
                           TR(TH(H2('Modify' + thisapp.apps.name), _colspan=3, _align='center')),
                           #TR('Group: ', SELECT(group_options, value=group_id,_name="group_id")),
                           TR('Process: ', SELECT(process_options, value=process_id,_name="process_id")),
                           TR('App: ', INPUT(_type='text', _name='app_name', value=app_name, _size=50)),
                           TR('MBEAN: ', INPUT(_type='text', _name='mbean', value=mbean_val, _size=50)),
                           TR('Alive Check: ', INPUT(_type='text', _name='alivecheck', value=alivecheck, _size=50)),
                           TR('Delete ', INPUT(_type="checkbox", _name="delete", _onclick="return confirm('Are you sure you want to delete " + thisapp.apps.name + "?');")),
                           TR("",INPUT(_type="submit", _value="SUBMIT"))
                          )
                    )

         # IF DELETE CHECKED, DELETE FROM DATABASE.
         if(request.vars.delete is not None):
            print("DELETE THIS ONE")
            if(has_child_rows(thisapp.apps.id)):
               response.flash = "This app has instances. You must first delete the instances which exist."
               return dict(form=form)
            else:
               db(db.apps.id == thisapp.apps.id).delete()
               print("--------------")
               print(db._lastsql)
               print("--------------")
               redirect(URL(r=request, f='index'))
               return(dict())
         else:
            print("process_id = " + process_id)
            if(not re.search("^\d+$", process_id)):
               response.flash = "Please select a Process."
            elif((not app_name)):
               response.flash = "Please define the App name."
            if(response.flash):
               return dict(form=form)
            else:
               # INSERT IT INTO THE DB
               curtime = MySQLdb.TimestampFromTicks(time.time())
               print("Update app id " + str(thisapp.apps.id) + " in db")
               db(db.apps.id == thisapp.apps.id).update(name = app_name, 
                                                   mbean = mbean_val, 
                                                   alive_check = alivecheck, 
                                                   processes_id = process_id,
                                                   updated_at = curtime)
               print("--------------")
               print(db._lastsql)
               print("--------------")
               print("Service Instance successfully inserted into DB")
               redirect(URL(r=request, f='index'))
               return(dict())
      else:
      
         # Load all groups
         group_options = [OPTION(group.name, _value=group.id) for group in db().select(db.groups.id, db.groups.name)]
         # Load all processes
         process_options = [OPTION(service.name, _value=service.id) for service in db().select(db.processes.id, db.processes.name)]
         # Load details of provided app
         form = FORM(TABLE(
                           TR(TH(H2('Modify ' + thisapp.apps.name), _colspan=3, _align='center')),
                           #TR('Group: ', SELECT(group_options, value=thisapp.groups.id,_name="group_id")),
                           TR('Process: ', SELECT(process_options, value=thisapp.processes.id,_name="process_id")),
                           TR('App: ', INPUT(_type='text', _name='app_name', value=thisapp.apps.name, _size=50)),
                           TR('MBEAN: ', INPUT(_type='text', _name='mbean', value=thisapp.apps.mbean, _size=50)),
                           TR('Alive Check: ', INPUT(_type='text', _name='alivecheck', value=thisapp.apps.alive_check, _size=50)),
                              TR('Delete ', INPUT(_type="checkbox", _name="delete", _onclick="return confirm('Are you sure you want to delete " + thisapp.apps.name + "?');")),
                           TR("",INPUT(_type="submit", _value="SUBMIT"))
                          )
                    )
         return dict(form=form)
         #return dict(groups=group_options, processes=process_options, thisapp=thisapp[0])
   else:
      redirect(URL(r=request, f='index'))
      
      
def add():
   print("apps.add")
   if(request.vars):
      print("process form")
      process_id = request.vars.process_id
      app_name = request.vars.app_name
      mbean_val = request.vars.mbean
      alivecheck = request.vars.alivecheck
      print("process_id = '" + process_id + "'")
      if(not re.search('^\d+$', process_id)):
         response.flash = "Please select a Process."
      elif((not app_name)):
         response.flash = "Please define the App name."
      if(response.flash):
         #group_options = [OPTION("NONE", _value="NONE")] + [OPTION(group.name, _value=group.id) for group in db().select(db.groups.id, db.groups.name)]
         process_options = [OPTION("NONE", _value="NONE")] + [OPTION(process.name, _value=process.id) for process in db().select(db.processes.id, db.processes.name)]
         form = FORM(TABLE(
                           TR(TH(H2('Create an App'), _colspan=3, _align='center')),
                           #TR('Group: ', SELECT(group_options, value=group_id,_name="group_id")),
                           TR('Process: ', SELECT(process_options, value=process_id,_name="process_id")),
                           TR('App: ', INPUT(_type='text', _name='app_name', value=app_name, _size=50)),
                           TR('MBEAN: ', INPUT(_type='text', _name='mbean', value=mbean_val, _size=50)),
                           TR('Alive Check: ', INPUT(_type='text', _name='alivecheck', value=alivecheck, _size=50)),
                           TR("",INPUT(_type="submit", _value="SUBMIT"))
                          )
                    )
         return dict(form=form)
      else:
         # INSERT IT INTO THE DB
         curtime = MySQLdb.TimestampFromTicks(time.time())
         print("insert it into the db")
         db.apps.insert(name = app_name,
                        mbean = mbean_val,
                        alive_check = alivecheck,
                        processes_id = process_id,
                        created_at = curtime,
                        updated_at = curtime)
         print("--------------")
         print(db._lastsql)
         print("--------------")
         print("Service Instance successfully inserted into DB")
         redirect(URL(r=request, f='index'))
         return(dict())
   else:
      #group_options = [OPTION("NONE", _value="NONE")] + [OPTION(group.name, _value=group.id) for group in db().select(db.groups.id, db.groups.name)]
      process_options = [OPTION("NONE", _value="NONE")] + [OPTION(process.name, _value=process.id) for process in db().select(db.processes.id, db.processes.name)]
      form = FORM(TABLE(
                        TR(TH(H2('Create an App'), _colspan=3, _align='center')),
                        #TR('Group: ', SELECT(group_options, _name="group_id")),
                        TR('Process: ', SELECT(process_options, _name="process_id")),
                        TR('App Name: ', INPUT(_type='text', _name='app_name', _size=50)),
                        TR('MBean: ', INPUT(_type='text', _name='mbean', _size=50)),
                        TR('Alive Check: ', INPUT(_type='text', _name='alivecheck')),
                        TR("",INPUT(_type="submit", _value="SUBMIT"))
                       )
                 )
      return dict(form=form)
      
      
      
def has_child_rows(app_id):
   app_instancesrs = db(db.app_instances.apps_id == app_id).select()
   if(app_instancesrs):
      return True
   else:
      return False
         
