import os
import re
import pyexiv2
#import jpeg
path="/mnt/memories/photographs/2011"  
dirList=os.listdir(path)
#dirList=["2012-03-29_-_145038990808296.jpg"]
#print pyexiv2.libpyexiv2.Image.tagDetails__dict__.keys()
for filename in dirList:
   if re.search("(jpg|JPG)$", filename):
      try:
         exif = {}
         image = pyexiv2.Image(os.path.join(path, filename))
         image.readMetadata()
         #print image.exifKeys()
         exif_timestamp = str(image['Exif.Image.DateTime'])
         exif_rs = re.search(r"(\d\d\d\d)-(\d\d)-(\d\d)\s(\d\d):(\d\d):(\d\d)", exif_timestamp)
         exif["year"] = exif_rs.group(1)
         exif["month"] = exif_rs.group(2)
         exif["day"] = exif_rs.group(3)
         exif["hour"] = exif_rs.group(4)
         exif["minute"] = exif_rs.group(5)
         exif["second"] = exif_rs.group(6)
         
         filename_rs = re.search(r"(\d\d\d\d)-(\d\d)-(\d\d)_-_(\d\d)(\d\d)(\d\d)(\d\d+)\.(jpg|JPG)", filename)
         print filename + " => " + filename_rs.group(7)
         if filename_rs.group(7) is not None:
            newfilename = exif["year"] + "-" + exif["month"] + "-" + exif["day"] + "_-_" + exif["hour"] + exif["minute"] + exif["second"]
            print ("----------------")
            #print filename
            newfilenamea = newfilename + ".jpg"
            if not os.path.isfile(os.path.join(path, newfilenamea)):
               print "rename " + filename + " to " + os.path.join(path, newfilenamea)
               os.rename(os.path.join(path, filename), os.path.join(path, newfilenamea))
            else:
               newfilename1 = newfilename + "1" + ".jpg"
               if not os.path.isfile(os.path.join(path, newfilename1)):
                  print "rename " + os.path.join(path, filename) + " to " + os.path.join(path, newfilename1)
                  os.rename(os.path.join(path, filename), os.path.join(path, newfilename1))
               else:
                  newfilename2 = newfilename + "2" + ".jpg"
                  if not os.path.isfile(os.path.join(path, newfilename2)):
                     print "rename " + os.path.join(path, filename) + " to " + os.path.join(path, newfilename2)
                     os.rename(os.path.join(path, filename), os.path.join(path, newfilename2))
                  else:
                     newfilename3 = newfilename + "3" + ".jpg"
                     if not os.path.isfile(os.path.join(path, newfilename3)):
                        print "rename " + os.path.join(path, filename) + " to " + os.path.join(path, newfilename3)
                        os.rename(os.path.join(path, filename), os.path.join(path, newfilename3))
                     else:
                        newfilename4 = newfilename + "4" + ".jpg"
                        if not os.path.isfile(os.path.join(path, newfilename4)):
                           print "rename " + os.path.join(path, filename) + " to " + os.path.join(path, newfilename4)
                           os.rename(os.path.join(path, filename), os.path.join(path, newfilename4))
                        else:
                           print "CANNOT RENAME " + os.path.join(path, filename)
            print ("----------------")
      except Exception, e:
         continue   
   #metadata = pyexiv2.metadata.ImageMetadata(os.path.join(path, filename))
   #metadata.read()
#   print filename + " => "
   #print filename + " => " + metadata['Exif.Image.DateTime']
   
#str(os.stat(os.path.join(path, filename)))
