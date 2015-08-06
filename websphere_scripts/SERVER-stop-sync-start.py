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

def stopSyncStart ( action, nodeName, containedServerNames ):
        global ScriptLocation
        execfile(ScriptLocation+"/Definitions.py" )
        containedAppNames = ""
        log(MAJOR_, "stopSyncStart: nodename="+nodeName+" containedServernames="+`containedServerNames`+" ..." )
        if (action == "update"):
                log(MAJOR_, "stopSyncStart: FUTURE: plugin-cfg.xml QUIESCE node="+nodeName+" servers="+`containedServerNames` )
                for serverName in containedServerNames:
                        stopNDServer(nodeName, serverName )
                #endFor
        #endIf
        syncNDNode(nodeName )
        if (action == "update"):
                for serverName in containedServerNames:
                        startNDServer(nodeName, serverName )
                #endFor
        elif (action == "install"):
                log(INFO_, "syncNDNode: TEMPORARY: do nothing during install?" )
                for serverName in containedServerNames:
                        for appName in containedAppNames:
                                log(INFO_, "?????????????????? stopSyncStart: QUESTION: start what Apps on what Servers  on this node="+nodeName )
                                ### StartApplication appName nodeName serverName
                        #endFor
                #endFor
        elif (action == "uninstall"):
                log(INFO_, "syncNDNode: TEMPORARY: do nothing during uninstall?" )
                for serverName in containedServerNames:
                        for appName in containedAppNames:
                                pass
                                ### StartApplication appName nodeName serverName
                        #endFor
                #endFor
        else:
                log(ERROR_, "stopSyncStart: unknown action="+action )
        #endElse
        if (action == "update"):
                log(MAJOR_, "stopSyncStart: FUTURE: plugin-cfg.xml RE-ACTIVATE node="+nodeName+" servers="+`containedServerNames` )
        #endIf
        log(MAJOR_, "stopSyncStart: DONE. (nodeName="+nodeName+")" )
#endDef

def syncNDNode ( nodeName ):
        global ScriptLocation
        execfile(ScriptLocation+"/Definitions.py" )
        EarExpandDelay = 10
        log(MAJOR_, "syncNDNode: ReSync of ND Node="+nodeName+" (actual application distribution to Server(s) ) ..." )
        ndSync = AdminControl.completeObjectName("type=NodeSync,node="+nodeName+",*" )
        if (ndSync == ""):
                fail("cannot syncNDNode (stopped?) nodeName="+nodeName )
        #endIf
        sync = AdminControl.invoke(ndSync, "sync" )
        log(INFO_, "syncNDNode: "+nodeName+" invoked sync="+sync+"  DONE." )
        log(INFO_, "syncNDNode: FUTURE: check for nodeSync EAR expansion complete (for now just delay "+`EarExpandDelay`+" secs)" )
        sleepDelay(EarExpandDelay )
#endDef

def startNDServer ( nodeName, serverName ):
        global ScriptLocation
        execfile(ScriptLocation+"/Definitions.py" )
        log(MAJOR_, "startNDServer: nodeName="+nodeName+" serverName="+serverName+" ..." )
        log(VERBOSE_, "startNDServer: FUTURE: replace wsAdmin.startserver with NodeAgent.launchProcess+Wait" )
        started = ""
        try:
                _excp_ = 0
                started = AdminControl.startServer(serverName, nodeName, 100000 )
        except:
                _type_, _value_, _tbck_ = sys.exc_info()
                _excp_ = 1
        #endTry
        temp = _excp_
        log(DEBUG_, "startNDServer: errorcode="+`temp`+" started="+started+" ..." )
        retries = 0
        while ((temp > 0) and (retries < 5) ):
                retries = retries+1
                log(ERROR_, "startNDServer: start failed exception="+`temp`+" for "+nodeName+" "+serverName+", retries="+`retries`+" ..." )
                try:
                        _excp_ = 0
                        started = AdminControl.startServer(serverName, nodeName, 100000 )
                except:
                        _type_, _value_, _tbck_ = sys.exc_info()
                        _excp_ = 1
                #endTry
                temp = _excp_
                log(DEBUG_, "startNDServer: temp="+temp+" started="+started+" ..." )
        #endWhile
        checkServerStarted(nodeName, serverName )
        log(VERBOSE_, "startNDServer: DONE." )
#endDef

def checkServerStarted ( nodeName, serverName ):
        global ScriptLocation
        execfile(ScriptLocation+"/Definitions.py" )
        log(INFO_, "checkServerStarted: nodeName="+nodeName+" serverName="+serverName+" ..." )
        serverID = ""
        retries = -1
        while ((len(serverID) == 0) and (retries < 300) ):
                retries = retries+1
                log(VERBOSE_, "checkServerStarted: server not yet started, retries="+`retries`+" ..." )
                sleepDelay(10 )
                try:
                        _excp_ = 0
                        serverID = AdminControl.completeObjectName("node="+nodeName+",name="+serverName+",type=Server,*" )
                except:
                        _type_, _value_, _tbck_ = sys.exc_info()
                        _excp_ = 1
                #endTry
                temp = _excp_
                if (temp != 0):
                        log(WARNING_, "checkServerStarted AdminControl exception="+`temp`+" serverID="+serverID )
                #endIf
        #endWhile
        if (retries > 0):
                log(INFO_, "checkServerStarted: "+nodeName+" "+serverName+" had slow start, status retries="+`retries` )
        #endIf
        if (serverID == ""):
                log(ERROR_, "checkServerStarted: "+nodeName+" "+serverName+" server FAILED TO START." )
                return
        #endIf
        state = AdminControl.getAttribute(serverID, "state" )
        state = state.upper()
        if (state == "STARTED"):
                log(INFO_, "checkServerStarted: "+nodeName+" "+serverName+" state="+state )
        else:
                log(ERROR_, "checkServerStarted: "+nodeName+" "+serverName+" INCORRECT state="+state )
        #endElse
        log(VERBOSE_, "checkServerStarted: DONE." )
#endDef

def stopNDServer ( nodeName, serverName ):
        global ScriptLocation
        execfile(ScriptLocation+"/Definitions.py" )
        log(DEBUG_, "stopNDServer: nodeName="+nodeName+" serverName="+serverName )
        serverID = AdminControl.completeObjectName("node="+nodeName+",name="+serverName+",type=Server,*" )
        if (len(serverID) == 0):
                msg = "stopNDServer: cannot access node="+nodeName+" server="+serverName+" state=STOPPED?"
                log(WARNING_, msg )
                return
        #endIf
        state = AdminControl.getAttribute(serverID, "state" )
        state = state.upper()
        log(INFO_, "stopNDServer: nodeName="+nodeName+" serverName="+serverName+" state="+state )
        if (state== "STOPPED"):
                log(INFO_, nodeName+" "+serverName+" state=STOPPED" )
        else:
                stopped = AdminControl.stopServer(serverName, nodeName, "immediate" )
                if (len(stopped) != 0):
                        log(VERBOSE_, "stopNDServer: stopServer response="+stopped )
                #endIf
                checkServerStopped(nodeName, serverName )
        #endElse
        log(VERBOSE_, "stopNDServer: DONE." )
#endDef

def checkServerStopped ( nodeName, serverName ):
        global ScriptLocation
        execfile(ScriptLocation+"/Definitions.py" )
        log(DEBUG_, "checkServerStopped: nodeName="+nodeName+" serverName="+serverName )
        desiredState = "STOPPED"
        serverID = ""
        try:
                _excp_ = 0
                serverID = AdminControl.completeObjectName("node="+nodeName+",name="+serverName+",type=Server,*" )
        except:
                _type_, _value_, _tbck_ = sys.exc_info()
                _excp_ = 1
        #endTry
        temp = _excp_
        if (temp != 0):
                log(WARNING_, "checkServerStopped: exception="+`temp`+" trying to access "+nodeName+" "+serverName )
        #endIf
        if (len(serverID) == 0):
                log(VERBOSE_, "checkServerStopped: cannot access node="+nodeName+" server="+serverName+" (STOPPED?)" )
                actualState = desiredState
        else:
                actualState = AdminControl.getAttribute(serverID, "state" )
        #endElse
        actualState = actualState.upper()
        log(INFO_, "checkServerStopped: "+nodeName+" "+serverName+" actualState="+actualState+" desiredState="+desiredState )
        if (actualState != desiredState):
                msg = "ERROR: checkServerStopped: "+nodeName+" "+serverName+" actualState="+actualState+" instead of desiredState="+desiredState
                fail(msg )
        #endIf
#endDef

def sleepDelay ( secs ):
        global ScriptLocation
        execfile(ScriptLocation+"/Definitions.py" )
        import time
        tstart = time.strftime( "%H:%M:%S", time.localtime() )
        #IGNORE: package require java
        try:
                _excp_ = 0
                java.lang.Thread.sleep( (secs * 1000) )
        except:
                _type_, _value_, _tbck_ = sys.exc_info()
                _excp_ = 1
        #endTry
        temp = _excp_
        tdone = time.strftime( "%H:%M:%S", time.localtime() )
        log(VERBOSE_, "sleepDelay seconds="+`secs`+" start="+tstart+" done="+tdone )
#endDef

