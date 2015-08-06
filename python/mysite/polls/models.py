from django.db import models
import datetime

class Poll(models.Model):
   question = models.CharField(max_length=200)
   pub_date = models.DateTimeField('date published')
   def __unicode__(self):
      return self.question
   # CREATE METHOD TO DETERMINE IF THIS POLL WAS PUBLISHED TODAY
   def was_published_today(self):
      return self.pub_date.date() == datetime.date.today()   
   # CHANGE THE HEADER TO BE DISPLAYED IN THIS COLUMN
   was_published_today.short_description = 'Published Today?'

class Choice(models.Model):
   poll = models.ForeignKey(Poll)
   choice = models.CharField(max_length=200)
   votes = models.IntegerField()
   def __unicode__(self):
      return self.choice

