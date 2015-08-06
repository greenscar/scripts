import MySQLdb
import time
import re
# coding: utf8
# try something like
def index(): 
   groups = db().select(db.groups.id, db.groups.name)
   return dict(groups=groups)

def edit_CRUD():
   print("groups.edit")
   row = db(db.groups.id == request.args[0]).select()[0]
   if(has_child_rows(row.id)):
      form = SQLFORM(db.groups, row, deletable=False)
   else:
      form = SQLFORM(db.groups, row, deletable=True)
      
   if form.accepts(request.vars, session):
      # PROCESS FORM
      # Check to ensure this value is not already in DB
      # If it is, return to form w/ message
      # If not, insert then return to list
      if group_name_exists(request.vars.group_name):
         response.flash=request.vars.group_name + " group already exists"
         return dict(form=form)
      else:
         curtime = MySQLdb.TimestampFromTicks(time.time())
         db(db.groups.id == request.vars.group_id).update(name=request.vars.group_name, updated_at=curtime)
         redirect(URL(r=request, f='index'))
   elif form.errors:
      # SEND TO FORM WITH ERROR DISPLAY
      response.flash='Form has errors'
   else:
      # FORM NOT FILLED. SHOW EMPTY FORM
      response.flash = 'Please complete the form.'
   return dict(form=form)

def edit():
   print("groups.edit")
   if(len(request.args) == 1 & request.args(0).isdigit()):
      groups = db(db.groups.id == request.args(0)).select(db.groups.ALL)
      try:
         thisgroup = groups[0]
      except:
         response.flash = "Group id " + request.args(0) + " does not exist."
         redirect(URL(r=request, f='index'))
         
      if(request.vars):
         print("--------------- process form -----------------")
         print(request.vars)
         form = FORM(
                     INPUT(_type="hidden", _name="group_id", _value=thisgroup.id),
                     TABLE(
                           TR(TH(H2('Modify ' + thisgroup.name + ' Group'), _colspan=2, _align='center')),
                           TR('Name: ', INPUT(_type='text', _name='group_name', value=request.vars.group_name, _size=50, requires=IS_NOT_EMPTY(error_message=T('Name cannot be blank.')))),
                           TR('Delete: ', INPUT(_type="checkbox", _name="delete", _onclick="return confirm('Are you sure you want to delete " + thisgroup.name + "?');")),
                           TR(INPUT(_type="submit", _value="SUBMIT"))
                          )
                    )
         if(request.vars.delete is not None):
            print("DELETE THIS GROUP")
            if(has_child_rows(thisgroup.id)):
               response.flash = "This group has child processes. You cannot delete a group with child processes."
               return(dict(form=form))
            else:
               db(db.groups.id == thisgroup.id).delete()
               print("--------------")
               print(db._lastsql)
               print("--------------")
               redirect(URL(r=request, f='index'))
               return(dict())
         else:
            if(not request.vars.group_name):
               response.flash = "Please enter a group name"
               return dict(form=form)
            elif(group_name_exists(request.vars.group_name)):
               response.flash = request.vars.group_name + " group already exists"
               return dict(form=form)
            else:
               # All is good. process the form
               curtime = MySQLdb.TimestampFromTicks(time.time())
               db(db.groups.id == request.vars.group_id).update(name=request.vars.group_name, updated_at=curtime)
               redirect(URL(r=request, f='index'))
      else:
         form = FORM(
                     INPUT(_type="hidden", _name="group_id", _value=thisgroup.id),
                     TABLE(
                           TR(TH(H2('Modify ' + thisgroup.name + ' Group'), _colspan=2, _align='center')),
                           TR('Name: ', INPUT(_type='text', _name='group_name', value=thisgroup.name, _size=50, requires=IS_NOT_EMPTY(error_message=T('Name cannot be blank.')))),
                           TR('Delete ', INPUT(_type="checkbox", _name="delete", _onclick="return confirm('Are you sure you want to delete " + thisgroup.name + "?');")),
                           TR(INPUT(_type="submit", _value="SUBMIT"))
                          )
                    )
         return dict(form=form)
         
   else:
      redirect(URL(r=request, f='index'))

def add():
   print("groups.add")
   form = FORM(
               TABLE(
                     TR(TH(H2('Create Group'), _colspan=2, _align='center')),
                     TR('Name: ', INPUT(_type='text', _name='group_name', _size=50, requires=IS_NOT_EMPTY(error_message=T('Name cannot be blank.')))),
                     TR(INPUT(_type='submit', _value='SUBMIT'))
                     )
               )
   if(request.vars):
      if group_name_exists(request.vars.group_name):
         response.flash=request.vars.group_name + " group already exists"
         return dict(form=form)
      else:
         curtime = MySQLdb.TimestampFromTicks(time.time())
         db.groups.insert(name=request.vars.group_name, created_at=curtime, updated_at=curtime)
         print(request.vars.group_name + " successfully inserted")
         redirect(URL(r=request, f='index'))
         return dict()
   else:
      return dict(form=form)
        
def delete():
   print("groups.delete")
   
   
def group_name_exists(name):
   grouprs = db(db.groups.name == name).select(db.groups.id)
   if grouprs:
      return grouprs[0].id
   else:
      return 0

def has_child_rows(groupid):
   processesrs = db(db.processes.groups_id == groupid).select(db.processes.id)
   if(processesrs):
      return True
   else:
      return False
         