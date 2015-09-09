# -*- coding: utf-8 -*- 

#########################################################################
## This is a samples controller
## - index is the default action of any application
## - user is required for authentication and authorization
## - download is for downloading files uploaded in the db (does streaming)
## - call exposes all registered processes (none by default)
#########################################################################  


 # At the beginning of my default.py

def index():
    """
    example action using the internationalization operator T and flash
    rendered by views/default/index.html or views/generic.html
    """
    response.flash = T('Welcome to web2py')
    return dict(message=T('app CMDB'))


def user():
    """
    exposes:
    http://..../[app]/default/user/login 
    http://..../[app]/default/user/logout
    http://..../[app]/default/user/register
    http://..../[app]/default/user/profile
    http://..../[app]/default/user/retrieve_password
    http://..../[app]/default/user/change_password
    use @auth.requires_login()
        @auth.requires_membership('group name')
        @auth.requires_permission('read','table name',record_id)
    to decorate functions that need access control
    """
    return dict(form=auth())


def download():
    """
    allows downloading of uploaded files
    http://..../[app]/default/download/[filename]
    """
    return response.download(request,db)


def call():
    """
    exposes processes. for example:
    http://..../[app]/default/call/jsonrpc
    decorate with @processes.jsonrpc the functions to expose
    supports xml, json, xmlrpc, jsonrpc, amfrpc, rss, csv
    """
    session.forget()
    return service()
    
def display_form():
   form=FORM('Your name:',
             INPUT(_name='name', requires=IS_NOT_EMPTY()),
             INPUT(_type='submit'))
   if form.accepts(request.vars, session):
      response.flash = 'form accepted'
   elif form.errors:
      response.flash = 'form has errors'
   else:
      response.flash = 'please fill the form'
   return dict(form=form)
