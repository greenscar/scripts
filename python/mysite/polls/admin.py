from mysite.polls.models import Poll
from mysite.polls.models import Choice
from django.contrib import admin

# DEFAULT ADMIN FORM
#admin.site.register(Poll)


# CHANGE ORDER OF FIELDS ON ADMIN FORM
#class PollAdmin(admin.ModelAdmin):
#   fields = ['pub_date', 'question']
#   
#admin.site.register(Poll, PollAdmin)
# END CHANGE ORDER OF FIELDS ON ADMIN FORM


# SPLIT FORM INTO FIELDSETS
#class PollAdmin(admin.ModelAdmin):
#    fieldsets = [
#        (None,               {'fields': ['question']}),
#        ('Date information', {'fields': ['pub_date']}),
#    ]
#
#admin.site.register(Poll, PollAdmin)
# END SPLIT FORM INTO FIELDSETS


# MAKE THE DATE FIELDS COLLAPSABLE
#class PollAdmin(admin.ModelAdmin):
#    fieldsets = [
#        (None,               {'fields': ['question']}),
#        ('Date information', {'fields': ['pub_date'], 'classes': ['collapse']}),
#    ]
#admin.site.register(Poll, PollAdmin)
# END MAKE THE DATE FIELDS COLLAPSABLE


# DEFAULT CHOICE FORM
# THIS REQUIRES REGISTRATION OF THE POLL FORM ALSO.
#admin.site.register(Choice)


# WE WANT THE CHOICES TO BE PART OF THE POLL FORM, NOT 2 SEPERATE FORMS
# TELL DJANGO THAT CHOICES ARE A PART OF THE POLL ADMIN PAGE
#class ChoiceInline(admin.StackedInline):
# IF YOU REPLACE THE LINE BELOW WITH THE ONE ABOVE, EACH CHOICE GETS ITS OWN SECTION.
class ChoiceInline(admin.TabularInline):
    model = Choice
    extra = 3

class PollAdmin(admin.ModelAdmin):
   fieldsets = [
      (None,               {'fields': ['question']}),
      ('Date information', {'fields': ['pub_date'], 'classes': ['collapse']}),
   ]
   inlines = [ChoiceInline]
   #By default, Django displays the str() of each object. But sometimes it'd be more helpful if we could display individual fields. To do that, use the list_display admin option, which is a tuple of field names to display, as columns, on the change list page for the object:
   list_display = ('question', 'pub_date', 'was_published_today')
   # Add a "Filter" sidebar that lets people filter the change list by the pub_date field:
   list_filter = ['pub_date']
   # Add hierarchical navigation, by date, to the top of the change list page
   date_hierarchy = 'pub_date'
   # Add a search field
   search_fields = ['question']


admin.site.register(Poll, PollAdmin)
# END TELL DJANGO THAT CHOICES ARE A PART OF THE POLL ADMIN PAGE

