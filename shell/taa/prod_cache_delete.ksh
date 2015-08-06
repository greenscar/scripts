#!/usr/bin/ksh

echo "delete cache from iepsau001"
`ssh wasadmin@iepsau001 rm -r /opt/WebSphere/AppSer*/temp/iepsau*/*-cluster*/*`

echo "delete cache from iepsau002"
`ssh wasadmin@iepsau002 rm -r /opt/WebSphere/AppSer*/temp/iepsau*/*-cluster*/*`



# ls -d /opt/WebSphere/AppSer*/installedApps/iepsau004Network/*

#rsync -e ssh -av --rsync-path=/opt/csw/bin/rsync /home/ccbuild/james/60.1.1 wasadmin'@'iepsau002:/opt/WebSphere/AppSer*/installedApps/*/stage3tiers.ear/stage3tiersWeb.war/jsp/in

#WEBSPHERELOC="/opt/WebSphere"
#APPSERVERS="AppServer10 AppServer9 AppServer8 AppServer7 AppServer6 AppServer5 AppServer4 AppServer3 AppServer2 AppServer"
#FILELOC="installedApps/iepsau004Network/stage3tiers.ear/stage3tiersWeb.war/jsp/in"
#FILE="INFAEPayeeInformation.jsp"
#for aLoc in $APPSERVERS
#do
   #echo "rsync /home/ccbuild/james/60.1.1 wasadmin@iepsau002:${WEBSPHERELOC}/${aLoc}/${FILELOC}"
#   echo "rsync --verbose /home/ccbuild/james/60.1.1/${FILE} wasadmin@iepsau002:${WEBSPHERELOC}/${aLoc}/${FILELOC}/${FILE}"
#done

#rsync --verbose /home/ccbuild/james/60.1.1 wasadmin@iepsau002:${WEBSPHERELOC}/${aLoc}/${FILELOC}
      
#ls -d /opt/WebSphere/AppSer*/installedApps/*/stage3tiers.ear/stage3tiersWeb.war/jsp/in/INFAEPayeeInformation.jsp


