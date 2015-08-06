This application was created by PreGame

*** WARNING *** I AM VERY TIRED AND IT WAS LATE SO I CREATED THIS README QUICKLY!  IT IS NOT COMPLETE AND YOU MAY HAVE TROUBLE UNDERSTANDING IT. ***WARNING ***

Extract the contents of the .tar file exactly.  I have never used untar but the folders should be in place.

Once extracted the application files should be in /usr/palm/applications/com.pregame.app.flashlight and the service should be in /usr/share/dbus-1/system-services make sure the Prelease.jar file is in /usr/lib/luna/java

After the files are extracted you can either 'Reboot' your Pre or you can try and updated the services manually.

----------------------
Install/ Update service
-----------------------
- Place the flashlight.tar at the root / dir
- run tar -xvf flashlight.tar (this will extract the files to the needed directory)

-Run the following command while rooted into the Pre

/usr/bin/luna-helper 'luna://com.palm.vm/launch' '{"serviceName":"com.pregame.prelease","className":"com.pregame.Prelease"}'

This will start the Prelease service.

Then you can either pkill LunaSysMgr or you can do what mdkeiln mentioned in IRC and run this simple command which takes about .5 seconds

luna-send -n 1 palm://com.palm.applicationManager/rescan {}

Once done you should be able to see the Flashlight! app in the appList.  If you do not see it please make sure the install was correct.

If the flashlight does not turn on then you will need to 

chmod 744 /usr/palm/applications/com.pregame.app.flashlight/app/scripts/lighton.sh
and
chmod 744 /usr/palm/applications/com.pregame.app.flashlight/app/scripts/lightoff.sh

This is only a temp release until I can get rid of the .sh script which I have not had time to do lately.  I have the code created for it just haven't had time to test and work out the bugs.

*NOTE*
If it still does not work after reboot remove the /usr/palm/applications/com.pregame.app.flashlight directory and all of it's contents then re-install.
------------
Credits
------------
bpadalino - Work with me for hours to find a dbus that would work.
ShinAli - Created the base and very first rooted service for the Pre.  I used his base code as a model for mine.
#Webos-Internals on Freenode - Great community full of helpful information
PreCentral.net - Decided to make the app because the community wanted one.