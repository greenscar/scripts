#
# Author: Barry Searle
#
# (C) Copyright IBM Corp. 2006,2007 - All Rights Reserved.
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
# 3.1 (08may2007) initial shipped version
#

global setJvmDisableJIT
def setJvmDisableJIT ( nodeName, serverName, disableJIT ):
        global AdminConfig, DEBUG_, INFO_
        aJvmID = getJvmID(nodeName, serverName)
        aJvmAttrs = [["disableJIT", disableJIT]]
        modifyJvmAttrs(aJvmID, aJvmAttrs)
#endDef

global setJvmDebugMode
def setJvmDebugMode ( nodeName, serverName, debugMode ):
        global AdminConfig, getJvmID, modifyJvmAttrs
        aJvmID = getJvmID(nodeName, serverName)
        aJvmAttrs = [["debugMode", debugMode]]
        modifyJvmAttrs(aJvmID, aJvmAttrs)
#endDef

global setJvmHeapSize
def setJvmHeapSize ( nodeName, serverName, initialHeap, maxHeap ):
        global AdminConfig
        aJvmID = getJvmID(nodeName, serverName)
        aJvmAttrs = [["initialHeapSize", initialHeap], ["maximumHeapSize", maxHeap]]
        modifyJvmAttrs(aJvmID, aJvmAttrs)
#endDef

################### Utility methods ###########################
global getJvmID
def getJvmID ( nodeName, serverName ):
        global AdminConfig
        aServerID = AdminConfig.getid("/Node:"+nodeName+"/Server:"+serverName+"/" )
        aJvmID = AdminConfig.list("JavaVirtualMachine", aServerID )
        return aJvmID
#endDef

global modifyJvmAttrs
def modifyJvmAttrs ( aJvmID, ajvmAttrs ):
        global AdminConfig, DEBUG_, INFO_
        aJvmSettings = AdminConfig.show(aJvmID )
        log(DEBUG_, "\nsetJVM initialJvmSettings: \n"+aJvmSettings )
        
        AdminConfig.modify(aJvmID, ajvmAttrs )
        aJvmSettings = AdminConfig.show(aJvmID )
        log(INFO_, "\nsetJVM changedJvmSettings: \n"+aJvmSettings )
#endDef
