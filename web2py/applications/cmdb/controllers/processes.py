import MySQLdb
import time
import re
# coding: utf8
# try something like
def index(): 
   if len(request.args) == 1:
      sort_by = request.args(0)
      if cmp(sort_by, "friendly") == 0:
            sort_by = db.processes.name_friendly
      elif cmp(sort_by, "process") == 0:
         sort_by = db.processes.name
      elif cmp(sort_by, "group") == 0:
         sort_by = db.groups.name
      else:
         sort_by = db.processes.name
   else:
      sort_by = db.groups.name
   processes = db((db.processes.groups_id == db.groups.id)).select(db.processes.ALL, db.groups.ALL, orderby=sort_by)
   return dict(processes=processes)
   
def edit_CRUD():
   print("processes.edit")
   if(request.args):
      row = db(db.processes.id == request.args[0]).select()[0]
      print(str(row))
      if(has_child_rows(row.id)):
         form = SQLFORM(db.processes, row, deletable=False)
      else:
         form = SQLFORM(db.processes, row, deletable=True)
         
      if form.accepts(request.vars, session):
         if process_name_exists(request.vars.process_name):
            response.flash=request.vars.process_name + " process already exists"
            return dict(form=form)
         else:
            curtime = MySQLdb.TimestampFromTicks(time.time())
            db(db.processes.id == request.vars.process_id).update(name=request.vars.process_name, updated_at=curtime)
            redirect(URL(r=request, f='index'))
      elif form.errors:
         response.flash='Errors in your form.'
      else:
         response.flash='Please complete the form'
      print(str(form))
      return dict(form=form)
   else:
      redirect(URL(r=request, f='index'))
      
def edit():
   print("processes.edit")
   if(len(request.args) == 1 & request.args(0).isdigit()):
      # A NUMBER HAS BEEN PROVIDED IN PATH WITH REGARDS TO PROCESS
      print(request.args(0))
      try:
         processes = db(db.processes.id == request.args(0)).select(db.processes.ALL)
         thisprocess = processes[0]
      except:
         redirect(URL(r=request, f='index'))
      group_options = [OPTION("NONE", _value="NONE")] + [OPTION(group.name, _value=group.id) for group in db().select(db.groups.id, db.groups.name)]
      if(request.vars):
         # THE FORM HAS BEEN FILLED OUT & SUBMITTED. PROCESS
         form = FORM(
                     TABLE(
                           TR(TH(H2('Modify ' + thisprocess.name + ' process'), _colspan=2, _align='center')),
                           TR('Name:', INPUT(_type='text', _name='process_name', value=request.vars.process_name, _size=50, requires=IS_NOT_EMPTY(error_message=T('Name cannot be blank.')))),
                           TR('Name Friendly:', INPUT(_type='text', _name='process_name_friendly', value=request.vars.process_name_friendly, _size=50, requires=IS_NOT_EMPTY(error_message=T('Name Friendly cannot be blank.')))),
                           TR('Group:', SELECT(group_options, value=request.vars.group_id, _name="group_id")),
                           TR('Delete ', INPUT(_type="checkbox", _name="delete", _onclick="return confirm('Are you sure you want to delete " + thisprocess.name + "?');")),
                           TR(INPUT(_type='submit', _value='SUBMIT'))
                          )
                     )
         if(request.vars.delete is not None):
            # USER HAS DECIDED TO DELETE.
            if(has_child_rows(thisprocess.id)):
               response.flash = "This process has child apps or process instances. You cannot delete a process with child apps or process instances."
               return(dict(form=form))
            else:
               db(db.processes.id == thisprocess.id).delete()
               redirect(URL(r=request,f='index'))
               return(dict())
         # USER DOES NOT WANT TO DELETE. PROCESS MODIFICATION
         if(not request.vars.process_name):
            response.flash = "Please define a Name"
         elif(process_name_exists(request.vars.process_name, thisprocess.id)):
            response.flash = "The process name you entered already exists. Please enter a new name."      
         elif(not request.vars.process_name_friendly):
            response.flash = "Please define a friendly name"
         elif(not re.search('^\d+$', request.vars.group_id)):
            response.flash = "Please select a group"
         if(response.flash):
            # THERE WAS SOMETHING WRONG WITH THE FORM. SEND USER BACK.
            return dict(form=form)
         else:
            # THE FORM WAS GOOD. PROCESS MODIFICATION
            curtime = MySQLdb.TimestampFromTicks(time.time())
            db(db.processes.id == thisprocess.id).update(name = request.vars.process_name,
                                                         name_friendly = request.vars.process_name_friendly,
                                                         groups_id = request.vars.group_id,
                                                         updated_at = curtime)
            redirect(URL(r=request, f='index'))
      else:
         form = FORM(
                     TABLE(
                           TR(TH(H2('Modify ' + thisprocess.name + ' process'), _colspan=2, _align='center')),
                           TR('Name:', INPUT(_type='text', _name='process_name', value=thisprocess.name, _size=50, requires=IS_NOT_EMPTY(error_message=T('Name cannot be blank.')))),
                           TR('Name Friendly:', INPUT(_type='text', _name='process_name_friendly', value=thisprocess.name_friendly, _size=50, requires=IS_NOT_EMPTY(error_message=T('Name Friendly cannot be blank.')))),
                           TR('Group:', SELECT(group_options, value=thisprocess.groups_id, _name="group_id")),
                           TR('Delete ', INPUT(_type="checkbox", _name="delete", _onclick="return confirm('Are you sure you want to delete " + thisprocess.name + "?');")),
                           TR(INPUT(_type='submit', _value='SUBMIT'))
                          )
                     )
         return dict(form=form)
   else:
      redirect(URL(r=request, f='index'))
      
def add():
   print("processes.add")
   # Load all groups
   group_options = [OPTION(group.name, _value=group.id) for group in db().select(db.groups.id, db.groups.name)]
   form = FORM(
               TABLE(
                     TR(TH(H2('Create Service'), _colspan=2, _align='center')),
                     TR('Name:', INPUT(_type='text', _name='process_name', _size=50, requires=IS_NOT_EMPTY(error_message=T('Name cannot be blank.')))),
                     TR('Name Friendly:', INPUT(_type='text', _name='process_name_friendly', _size=50, requires=IS_NOT_EMPTY(error_message=T('Name Friendly cannot be blank.')))),
                     TR('Group:', SELECT(group_options, _name="group_id"), "Add Group Link"),                     
                     TR(INPUT(_type='submit', _value='SUBMIT'))
                    )
               )
   if(request.vars):
      if process_name_exists(request.vars.process_name):
         print(request.vars.process_name + " process already exists")
         response.flash=request.vars.process_name + " process already exists"
         return dict(form=form)
      else:
         curtime = MySQLdb.TimestampFromTicks(time.time())
         db.processes.insert(name=request.vars.process_name, name_friendly=request.vars.process_name_friendly, groups_id=request.vars.group_id, created_at=curtime, updated_at=curtime)
         print(request.vars.process_name + " successfully inserted")
         redirect(URL(r=request, f='index'))
         return dict()
   else:
      return dict(form=form)
   
def process_name_exists(name, thisid):
   processrs = db((db.processes.name == name) & (db.processes.id != thisid)).select(db.processes.id)
   if processrs:
      return processrs[0].id
   else:
      return 0

def has_child_rows(process_id):
   process_instancesrs = db(db.process_instances.processes_id == process_id).select(db.process_instances.id)
   appsrs = db(db.apps.processes_id == process_id).select(db.apps.id)
   if(appsrs or process_instancesrs):
      return True
   else:
      return False
         
