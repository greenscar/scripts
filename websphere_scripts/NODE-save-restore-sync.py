#
# Author: Barry Searle
#
# (C) Copyright IBM Corp. 2004,2006 - All Rights Reserved.
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
# 2.0 (10feb2006) initial Jython version
# 1.1 (17nov2004) initial shipped version
#

def saveAndDisableAutosync ( action, nodesContainedServers ):
        global ScriptLocation
        execfile(ScriptLocation+"/Definitions.py" )
        if (( action == "update") ):
                pass
        else:
                fail("saveAndDisableAutosync: should never be called for action="+action+" (only for 'install')" )
        #endElse

        log(INFO_, "" )
        log(MAJOR_, "saveAndDisableAutosync of affectedNodes begin ..." )
        savedNodesAutosyncs = []
        for nodeContainedServer in nodesContainedServers:
                nodeName = nodeContainedServer[0]
                nodeAgent = AdminConfig.getid("/Node:"+nodeName+"/Server:nodeagent/" )
                syncServ = AdminConfig.list("ConfigSynchronizationService", nodeAgent )
                syncEnabled = AdminConfig.showAttribute(syncServ, "autoSynchEnabled" )
                synchOnServerStartup = AdminConfig.showAttribute(syncServ, "synchOnServerStartup" )
                log(INFO_, "nodeContainedServers: nodeName="+nodeName+" syncEnabled="+syncEnabled+" synchOnServerStartup="+synchOnServerStartup )
                if (syncEnabled):
                        log(MAJOR_, "saveAndDisableAutosync: temporarily setting AutoSyncEnabled FALSE for node="+nodeName )
                        AdminConfig.modify(syncServ, [["autoSynchEnabled", "false"]] )
                #endIf
                if (synchOnServerStartup):
                        log(MAJOR_, "saveAndDisableAutosync: temporarily setting SynchOnServerStartup FALSE for node="+nodeName )
                        AdminConfig.modify(syncServ, [["synchOnServerStartup", "false"]] )
                #endIf

                savedNodeAutosync = [nodeName, syncEnabled, synchOnServerStartup]
                log(DEBUG_, "saveAndDisableAutosync: nodeName="+nodeName+" savedNodeAutosync="+`savedNodeAutosync` )
                savedNodesAutosyncs.append(savedNodeAutosync)
        #endFor
        configSave( )
        log(DEBUG_, "saveAndDisableAutosync: returning node savedNodesAutosyncs="+`savedNodesAutosyncs` )
        highlight(MAJOR_, "saveAndDisableAutosync of affectedNodes DONE." )
        return savedNodesAutosyncs
#endDef

def restoreAutosync ( action, savedNodesAutosyncs ):
        global ScriptLocation
        execfile(ScriptLocation+"/Definitions.py" )
        if (( action == "update") ):
                pass
        else:
                fail("saveAndDisableAutosync: should never be called for action="+action+" (only for 'update')" )
        #endElse
        log(MAJOR_, "restoreAutosync of affectedNodes begin ..." )

        log(DEBUG_, "restoreAutosync: savedNodesAutosyncs="+`savedNodesAutosyncs` )
        for savedNodeAutosync in savedNodesAutosyncs:
                log(VERBOSE_, "restoreAutosync: savedNodeAutosync="+`savedNodeAutosync` )
                nodeName = savedNodeAutosync[0]
                nodeAgent = AdminConfig.getid("/Node:"+nodeName+"/Server:nodeagent/" )
                syncServ = AdminConfig.list("ConfigSynchronizationService", nodeAgent )
                syncEnabled = savedNodeAutosync[1]
                synchOnServerStartup = savedNodeAutosync[2]
                log(INFO_, "restoreAutosync: nodeName="+nodeName+" syncEnabled="+syncEnabled+" synchOnServerStartup="+synchOnServerStartup )
                if (syncEnabled):
                        log(MAJOR_, "saveAndDisableAutosync: restoring AutoSyncEnabled      TRUE for node="+nodeName )
                        AdminConfig.modify(syncServ, [["autoSynchEnabled", "true"]] )
                else:
                        log(WARNING_, "saveAndDisableAutosync: restoring AutoSyncEnabled      FALSE for node="+nodeName )
                #endElse
                if (synchOnServerStartup):
                        log(MAJOR_, "saveAndDisableAutosync: restoring SynchOnServerStartup TRUE for node="+nodeName )
                        AdminConfig.modify(syncServ, [["synchOnServerStartup", "true"]] )
                else:
                        log(WARNING_, "saveAndDisableAutosync: restoring SynchOnServerStartup FALSE for node="+nodeName )
                #endElse
        #endFor
        configSave( )
        highlight(MAJOR_, "restoreAutosync of affectedNodes DONE." )
#endDef

def configSave (  ):
        global ScriptLocation
        execfile(ScriptLocation+"/Definitions.py" )
        log(MAJOR_, "configSave: ..." )
        saved = AdminConfig.save( )
        if (len(saved) > 0):
                log(INFO_, saved )
        #endIf
        log(VERBOSE_, "configSave: DONE." )
#endDef
