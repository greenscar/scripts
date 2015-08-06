# coding: utf8
# try something like
def index(): return dict(message="hello from test.py")

def recipes():
   records=db(db.recipe.category==request.vars.category).select(orderby=db.recipe.title)
   form=SQLFORM(db.recipe,fields=['category'])
   return dict(form=form, records=records)
   
def show():
   id=request.vars.id
   recipes=db(db.recipe.id==id).select()
   if not len(recipes): redirect(URL(r=request,f='recipes'))
   return dict(recipe=recipes[0])
   
def new_recipe():
   form=SQLFORM(db.recipe,fields=['title', 'description', 'category', 'instructions'])
   if form.accepts(request.vars,session):
      redirect(URL(r=request,f='recipes'))
   return dict(form=form)
