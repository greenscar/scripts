AdderDeploy also contains 2 documents: deployment1-publish.pdf and deployment2-publish.pdf

1) you use this Example on a machine with WAS-ND installed (v50 or v51 or v60 or v61)
- since it will run that installation's wsAdmin script processing
- you can target your deployment to whatever WAS-v5x/v6 servers you wish (in your ND cell).
- NOTE: that Jython, althogh shipped with 5.1/6.0, has full required \lib only in WAS-6.1

2) cd into AdderDeploy directory, and edit TestDeploy.bat (or .sh) :
- edit "WASROOT" to set your WAS install location
  = that location must be an ND application server
- On Windows also edit "JACLbaseDir" (no longer used on Linux)
- On Windows edit "SERVERHTTP" if you want to enable an operator browser popup after installations

3) also edit the dist\AdderEAR-pilot.targets   (to specify your target servers/clusters)
- currently installs on one node/server and one cluster - you can install to whatever you want

4) run TestDeploy.bat (or .sh)

5) There is also a TestDeployBase.bat with an example Base (non-managed) AutoDeployment.

NOTE: if you used previous version of AutoDeploy, be sure to read changes-v30.txt
