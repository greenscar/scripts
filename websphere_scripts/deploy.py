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
# 3.1 (08may2007) added invocation of processScriptsFile
# 2.0 (10feb2006) initial Jython version
# 1.2 (14jan2005) fixed multiple EARs (start/stop apps on servers using $appsNodesServers)
# 1.1 (17nov2004) initial shipped version
#

def deploy ( action, depType, failOnError, distDir, wasRoot ):
        global ScriptLocation
        execfile(ScriptLocation+"/Definitions.py" )
        log(VERBOSE_, "deploy: INPUTS: "+action+" "+depType+" "+distDir+" "+wasRoot )

        action = action.lower()
        if (action == "install" or action == "update"):
                installOrUpdate(action, depType, distDir, wasRoot )
        elif (action == "confirm"):
                confirm(action, depType, distDir, wasRoot )
        elif (action == "uninstall"):
                uninstall(action, depType, distDir, wasRoot )
        else:
                msg = "ERROR: deploy: unknown action="+action+" (must be 'install' or 'update' or 'confirm' or 'uninstall'"
                fail(msg )
        #endElse
#endDef

def installOrUpdate ( action, depType, distDir, wasRoot ):
        global ScriptLocation
        execfile(ScriptLocation+"/Definitions.py" )
        global nodeServerPairs
        global uniqueNodesContainedServers
        global nodesAutosyncs
        global clusters
        global testURLs
        global testResponses
        global appsNodesServers
        log(VERBOSE_, "installOrUpdate: "+action+" "+depType+" "+distDir )
        log(VERBOSE_, "installOrUpdate: "+wasRoot+" ..." )

        ############### FIND APPLICATIONS ####################
        ears = readDistributionDirectory(distDir )
        log(INFO_, "installOrUpdate: Deployment ears="+`ears` )
        appNames = parseApplicationNames(ears )
        log(INFO_, "installOrUpdate: Deployment appNames="+`appNames` )

        ################## PRE-VALIDATE Application TARGETS+SETTINGS files ####################
        PreValidateApplicationTargetsAndSettings(appNames, depType, distDir )

        if (action == "install"):
                PreValidateApplicationsAbsent(appNames )
        else:
                PreValidateApplicationsExist(appNames )
        #endElse

        ############### CALCULATE AFFECTED NODES ####################
        calculateAffectedNodes(action, depType, distDir, wasRoot, appNames )

        ################## PRE-VALIDATE NODES and SERVERS ####################
        PreValidateNodesAndServers(uniqueNodesContainedServers )

        ############### PREPARE AFFECTED NODES ####################
        if (( action == "update") ):
                nodesAutosyncs = saveAndDisableAutosync(action, uniqueNodesContainedServers )
                log(INFO_, "installOrUpdate: RESULT: nodesAutosyncs="+`nodesAutosyncs` )
        #endIf
        configSave( )

        ############### INSTALL APPLICATION AND CONFIGURE ####################
        for appName in appNames:
                processScriptsFile ( appName, depType, distDir, "preInstall" )
        #endFor
        installAndConfigureApps(action, depType, distDir, wasRoot, appNames )
        for appName in appNames:
                processScriptsFile ( appName, depType, distDir, "postInstall" )
        #endFor
        configSave( )

        ################## SYNC NODES (DISTRIBUTE APPS) ####################
        log(INFO_, "" )
        log(MAJOR_, "installOrUpdate: stopSyncStart of affected nodes ..." )
        for nodeContainedServers in uniqueNodesContainedServers:
                nodeName = nodeContainedServers[0]
                servers = nodeContainedServers[1]
                stopSyncStart(action, nodeName, servers )
        #endFor
        log(MAJOR_, "installOrUpdate: stopSyncStart of affected nodes DONE." )

        ################## START INSTALLED APPLICATIONS ####################
        if (( action == "install") ):
                length = len(appsNodesServers)
                for item in appsNodesServers:
                        itm = item[0]
                        length = len(itm)
                        if (length == 1):
                                appName = item
                        else:
                                StartApplicationOnNodesAndServers(appName, item )
                        #endElse
                #endFor
        #endIf

        ############### RESTORE AFFECTED NODES ####################
        if (( action == "update") ):
                log(DEBUG_, "installOrUpdate: nodesAutosyncs="+`nodesAutosyncs` )
                restoreAutosync(action, nodesAutosyncs )
        #endIf
        configSave( )

        ################## TEST ####################
        testApplication(testURLs, testResponses )

        highlight(MAJOR_, "installOrUpdate: DONE." )
#endDef

def confirm ( action, depType, distDir, wasRoot ):
        global ScriptLocation
        execfile(ScriptLocation+"/Definitions.py" )
        global nodeServerPairs
        global uniqueNodesContainedServers
        global nodesAutosyncs
        global clusters
        global testURLs
        global testResponses
        log(VERBOSE_, "confirm: "+action+" "+depType+" "+distDir )
        log(VERBOSE_, "confirm: "+wasRoot+" ..." )

        ############### FIND APPLICATIONS ####################
        ears = readDistributionDirectory(distDir )
        log(INFO_, "confirm: Deployment ears="+`ears` )
        appNames = parseApplicationNames(ears )
        log(INFO_, "confirm: Deployment appNames="+`appNames` )

        ################## PRE-VALIDATE Application TARGETS+SETTINGS files ####################
        PreValidateApplicationTargetsAndSettings(appNames, depType, distDir )

        ################## PRE-VALIDATE APPLICATIONS (exists) ####################
        PreValidateApplicationsExist(appNames )

        ############### CALCULATE AFFECTED NODES ####################
        calculateAffectedNodes(action, depType, distDir, wasRoot, appNames )

        ################## PRE-VALIDATE NODES and SERVERS ####################
        PreValidateNodesAndServers(uniqueNodesContainedServers )

        ################## TEST ####################
        testApplication(testURLs, testResponses )

        highlight(MAJOR_, "confirm: DONE." )
#endDef

def uninstall ( action, depType, distDir, wasRoot ):
        global ScriptLocation
        execfile(ScriptLocation+"/Definitions.py" )
        global nodeServerPairs
        global uniqueNodesContainedServers
        global nodesAutosyncs
        global clusters
        global testURLs
        global testResponses
        global appsNodesServers
        log(VERBOSE_, "uninstall: "+action+" "+depType+" "+distDir )
        log(VERBOSE_, "uninstall: "+wasRoot+" ..." )

        ############### FIND APPLICATIONS ####################
        ears = readDistributionDirectory(distDir )
        log(INFO_, "uninstall: Deployment ears="+`ears` )
        appNames = parseApplicationNames(ears )
        log(INFO_, "uninstall: Deployment appNames="+`appNames` )

        ################## PRE-VALIDATE Application TARGETS+SETTINGS files ####################
        PreValidateApplicationTargetsAndSettings(appNames, depType, distDir )

        ################## PRE-VALIDATE APPLICATIONS (exists) ####################
        PreValidateApplicationsExist(appNames )

        ############### CALCULATE AFFECTED NODES ####################
        calculateAffectedNodes(action, depType, distDir, wasRoot, appNames )

        ################## PRE-VALIDATE NODES and SERVERS ####################
        PreValidateNodesAndServers(uniqueNodesContainedServers )

        log(INFO_, "uninstall: Deployment appNames="+`appNames` )
        listApplications( )

        ################## STOP AND UNINSTALL APPLICATIONS ####################
        for appName in appNames:
                processScriptsFile ( appName, depType, distDir, "preUninstall" )
        #endFor
        for item in appsNodesServers:
                itm = item[0]
                length = len(itm)
                if (length == 1):
                        appName = item
                else:
                        appExists = checkIfAppExists(appName )
                        if (appExists):
                                StopApplicationOnNodesAndServers(appName, item )
                                uninstallEAR(appName )
                        #endIf
                #endElse
        #endFor
        for appName in appNames:
                processScriptsFile ( appName, depType, distDir, "postUninstall" )
        #endFor

        listApplications( )
        configSave( )
        highlight(MAJOR_, "uninstall: DONE. (appNames="+`appNames`+") " )
#endDef

