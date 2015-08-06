#
# Author: Barry Searle
#
# (C) Copyright IBM Corp. 2004,2007 - All Rights Reserved.
# DISCLAIMER:
# The following source code is sample code created by IBM Corporation.
# This sample code is not part of any standard IBM product and is provided
# to you solely for the purpose of assisting you in the development of your
# applications. The code is provided 'AS IS', without warranty or condition
# of any kind. IBM shall not be liable for any damages arising out of your
# use of the sample code, even if IBM has been advised of the possibility of
# such damages.
#
# Change History:
# 3.0 (25apr2007) added support for installOptions
# 2.0 (10feb2006) initial Jython version
# 1.3 (22apr2005) API: added "appPath" as setTargets parameter
# 1.3 (22apr2005) added "update" code into proc "installAndConfigureApps"
# 1.2 (14jan2005) removed duplicated appends of testURLs and testResponses
# 1.1 (17nov2004) initial shipped version
#

def installAndConfigureApps ( action, depType, distDir, wasRoot, appNames ):
        global ScriptLocation
        execfile(ScriptLocation+"/Definitions.py" )
        global testRULs
        global testResponses
        log(INFO_, "" )
        log(MAJOR_, "installAndConfigureApps: appNames="+`appNames`+" ..." )

        listApplications( )

        ################ INSTALL (or UPDATE) AND CONFIGURE ##############
        for appName in appNames:

                ################ READ TARGETS ##############
                appTargets = readTargetsFile(appName, depType, distDir )
                appNodeServerPairs = appTargets[0]
                appClusters = appTargets[1]
                appTestURLs = appTargets[2]
                appTestResponses = appTargets[3]
                log(DEBUG_, "installAndConfigureApps: appNodeServerPairs="+`appNodeServerPairs` )
                log(DEBUG_, "installAndConfigureApps: appClusters="+`appClusters` )
                log(DEBUG_, "installAndConfigureApps: appTestURLs="+`appTestURLs` )
                log(DEBUG_, "installAndConfigureApps: appTestResponses="+`appTestResponses` )

                log(MAJOR_, "installAndConfigureApps: appName="+appName )
                ext = ".ear"
                appPath = distDir+"/"+appName+ext

                ################ VALIDATE EAR ##############
                validateEAR(appPath )

                ################ READ APP INSTALL OPTIONS (from .settings) ##############
                installOptions = processSettingsFileInstallOptions(appName, depType, distDir)
                ################ INSTALL ##############
                nodeName = []
                serverName = []
                if (len(appNodeServerPairs) > 0):
                        appNodeServerPair = appNodeServerPairs[0]
                        nodeName = appNodeServerPair[0]
                        serverName = appNodeServerPair[1]
                #endIf
                clusterName = []
                if (len(appClusters) > 0):
                        clusterName = appClusters[0]
                #endIf
                if (action == "install"):
                        appExists = checkIfAppExists(appName )
                        if (appExists):
                                msg = "application="+appName+" EXISTS, CANNOT install with same name"
                                fail(msg )
                                highlight(ERROR_, "application="+appName+" EXISTS, will process SETTINGS and TARGETS" )
                                #highlight $WARNING_ "application=$appName EXISTS, will uninstall before install"
                                #uninstall $action $depType $distDir $wasRoot
                        else:
                                installEAR(action, appPath, appName, clusterName, nodeName, serverName, installOptions )
                        #endElse
                elif (action == "update"):
                        appExists = checkIfAppExists(appName )
                        if (appExists):
                                installEAR(action, appPath, appName, clusterName, nodeName, serverName, installOptions )
                        else:
                                msg = "application="+appName+" DOES NOT EXIST, will INSTALL instead of UPDATE"
                                log(WARNING_, msg)
                                installEAR("install", appPath, appName, clusterName, nodeName, serverName, installOptions )
                        #endElse
                #endIf

                ################ CONFIG SETTINGS ##############
                processSettingsFile(appName, depType, distDir, true )

                ################ CONFIG TARGETS ##############
                setTargets(appName, appClusters, appNodeServerPairs, appPath )

                ################ VALIDATE INSTALLED APPLICATION ##############
                validateApplication(appName )
        #endFor
        listApplications( )
        highlight(MAJOR_, "installAndConfigureApps DONE. (ready to distribute to nodes/servers)" )
#endDef
