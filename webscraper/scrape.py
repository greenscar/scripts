import urllib,urllib2
import urlparse
from BeautifulSoup import BeautifulSoup
import os, sys

def getAllImages(url):
    query = urllib2.Request(url)
    user_agent = "Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 5.1; .NET CLR 1.1.4322; .NET CLR 1.0.3705)"
    query.add_header("User-Agent", user_agent)

    page = BeautifulSoup(urllib2.urlopen(query).read())
    for div in page.findAll("div", {"class": "inplug"}):
        print "found thumbnail"
        for img in div.findAll("img"):
            print "found image"
            src = img["src"]
            if src:
                src = absolutize(src, pageurl)
                f = open(src,'wb')
                f.write(urllib.urlopen(src).read())
                f.close()
        for h5 in div.findAll("h5"):
            print "found Headline"
            value = (h5.contents[0])
            print >> headlines.txt, value


print "getAllImages"
getAllImages("http://www.glam0ur.com/sasha-cane")
