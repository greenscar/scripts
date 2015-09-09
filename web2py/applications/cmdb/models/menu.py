# -*- coding: utf-8 -*- 

#########################################################################
## Customize your APP title, subtitle and menus here
#########################################################################

response.title = request.application
response.subtitle = T('app')

##########################################
## this is the main application menu
## add/remove items as required
##########################################

response.menu_envts = [
    [T('Databases'), False, 
     URL(request.application,'dbs','index'), []],
    [T('Servers'), False, 
     URL(request.application,'servers','index'), []]
]
response.menu_apps = [
    [T('Customers'), False, 
     URL(request.application,'customers','index'), []],
    [T('Groups'), False, 
     URL(request.application,'groups','index'), []],
    [T('Processes'), False, 
     URL(request.application,'processes','index'), []],
    [T('Apps'), False, 
     URL(request.application,'apps','index'), []]
]
response.menu_instances = [
    [T('Process Instances'), False, 
     URL(request.application,'process_instances','index'), []],
    [T('App Instances'), False, 
     URL(request.application,'app_instances','index'), []],
    ]
   

##########################################
## this is here to provide shortcuts
## during development. remove in production 
##########################################

response.menu_edit=[
  [T('Edit'), False, URL('admin', 'default', 'design/%s' % request.application),
   [
            [T('Controller'), False, 
             URL('admin', 'default', 'edit/%s/controllers/%s.py' \
                     % (request.application,request.controller=='appadmin' and
                        'default' or request.controller))], 
            [T('View'), False, 
             URL('admin', 'default', 'edit/%s/views/%s' \
                     % (request.application,response.view))],
            [T('Layout'), False, 
             URL('admin', 'default', 'edit/%s/views/layout.html' \
                     % request.application)],
            [T('Stylesheet'), False, 
             URL('admin', 'default', 'edit/%s/static/base.css' \
                     % request.application)],
            [T('DB Model'), False, 
             URL('admin', 'default', 'edit/%s/models/db.py' \
                     % request.application)],
            [T('Menu Model'), False, 
             URL('admin', 'default', 'edit/%s/models/menu.py' \
                     % request.application)],
            [T('Database'), False, 
             URL(request.application, 'appadmin', 'index')],
            ]
   ],
  ]
  
