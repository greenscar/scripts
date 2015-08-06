#!/usr/bin/ksh

echo "delete cache from iepsau001"
#`ssh wasadmin@iepsau001 rm -r /opt/WebSphere/AppSer*/temp/iepsau*/*-cluster*/*`

echo "delete cache from iepsau002"
#`ssh wasadmin@iepsau002 rm -r /opt/WebSphere/AppSer*/temp/iepsau*/*-cluster*/*`



 

#rsync -e ssh -av --rsync-path=/usr/local/bin/rsync /home/ccbuild/james/60.1.1 wasadmin'@'iepsau002:/opt/WebSphere/AppSer*/installedApps/*/stage3tiers.ear/stage3tiersWeb.war/jsp/in

WEBSPHERELOC="/opt/WebSphere"
APPSERVERS="AppServer9 AppServer8 AppServer7 AppServer6 AppServer4 AppServer3 AppServer2 AppServer"
FILELOC="installedApps/iepsau004Network/stage3tiers.ear/stage3tiersWeb.war/jsp/in"
FILE="INFAEPayeeInformation.jsp"
for aLoc in APPSERVERS
do
   echo "rsync /home/ccbuild/james/60.1.1 wasadmin@iepsau002:${WEBSPHERELOC}/${aLoc}/${FILELOC}
done

#ls -d /opt/WebSphere/AppSer*/installedApps/*/stage3tiers.ear/stage3tiersWeb.war/jsp/in/INFAEPayeeInformation.jsp

#[wasadmin@iepsau002]/> ls -d /opt/WebSphere/AppSer*/installedApps/*/stage3tiers.ear/stage3tiersWeb.war/jsp//in/INFAEPayeeInformation.jsp
#-rw-r--r--   1 wasadmin was        22528 Jan 31 20:06 /opt/WebSphere/AppServer9/installedApps/iepsau004Network/stage3tiers.ear/stage3tiersWeb.war/jsp/in/INFAEPayeeInformation.jsp
#-rw-r--r--   1 wasadmin was        22528 Jan 31 20:06 /opt/WebSphere/AppServer8/installedApps/iepsau004Network/stage3tiers.ear/stage3tiersWeb.war/jsp/in/INFAEPayeeInformation.jsp
#-rw-r--r--   1 wasadmin was        22528 Jan 31 20:06 /opt/WebSphere/AppServer7/installedApps/iepsau004Network/stage3tiers.ear/stage3tiersWeb.war/jsp/in/INFAEPayeeInformation.jsp
#-rw-r--r--   1 wasadmin was        22528 Jan 31 20:06 /opt/WebSphere/AppServer6/installedApps/iepsau004Network/stage3tiers.ear/stage3tiersWeb.war/jsp/in/INFAEPayeeInformation.jsp
#-rw-r--r--   1 wasadmin was        22528 Jan 31 20:06 /opt/WebSphere/AppServer4/installedApps/iepsau004Network/stage3tiers.ear/stage3tiersWeb.war/jsp/in/INFAEPayeeInformation.jsp
#-rw-r--r--   1 wasadmin was        22528 Jan 31 20:06 /opt/WebSphere/AppServer3/installedApps/iepsau004Network/stage3tiers.ear/stage3tiersWeb.war/jsp/in/INFAEPayeeInformation.jsp
#-rw-r--r--   1 wasadmin was        22528 Jan 31 20:06 /opt/WebSphere/AppServer2/installedApps/iepsau004Network/stage3tiers.ear/stage3tiersWeb.war/jsp/in/INFAEPayeeInformation.jsp
#-rw-r--r--   1 wasadmin was        22528 Jan 31 20:06 /opt/WebSphere/AppServer/installedApps/iepsau004Network/stage3tiers.ear/stage3tiersWeb.war/jsp/in/INFAEPayeeInformation.jsp
#[wasadmin@iepsau002]/> exit

