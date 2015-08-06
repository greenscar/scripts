#!/usr/bin/python

from ID3 import *
import os
import getopt, string, re, sys




rootdir = '/Users/james.sandlin/Music/iTunes/iTunes Media/Music/Pet Shop Boys/Fundamental'



class MP3:
   def __init__(self, filename):
      self.filename = filename
      
   def getid3info(self):
      id3info = ID3(self.filename)
      return id3info      


for subdir, dirs, files in os.walk(rootdir):
   for file in files:
      x = MP3(rootdir + "/" + file)
      id3info =  x.getid3info()
      print(id3info.title);
      print id3info




