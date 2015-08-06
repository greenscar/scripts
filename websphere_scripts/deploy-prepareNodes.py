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
# 2.0 (10feb2006) initial Jython version
# 1.2 (14jan2005) fixed multiple EARs (start/stop apps on servers using $appsNodesServers per app)
# 1.1 (17nov2004) initial shipped version
#

def calculateAffectedNodes ( action, depType, distDir, wasRoot, appNames ):
        global ScriptLocation
        execfile(ScriptLocation+"/Definitions.py" )
        global nodeServerPairs
        global clusters
        global uniqueNodesContainedServers
        global nodesAutosyncs
        global testURLs
        global testResponses
        global appsNodesServers
        appsNodesServers = []
        nodeServerPairs = []
        uniqueNodesContainedServers = []
        nodesAutosyncs = []
        testURLs = []
        testResponses = []
        log(VERBOSE_, "calculateAffectedNodes appNames="+`appNames`+" ..." )
        highlight(DEBUG_, "calculateAffectedNodes appNames="+`appNames`+" ..." )

        ############### FIND NODES/SERVERS ####################
        if (len(appNames) == 0):
                fail("calculateAffectedNodes: No appNames (*.ear) in distDir "+distDir )
        #endIf
        for appName in appNames:

                ################ READ TARGETS ##############
                appTargets = readTargetsFile(appName, depType, distDir )
                appNodeServerPairs = appTargets[0]
                log(DEBUG_, "appNodeServerPairs="+`appNodeServerPairs`)
                tmpNodeServerPairs = []
                appClusters = appTargets[1]
                appTestURLs = appTargets[2]
                appTestResponses = appTargets[3]
                log(DEBUG_, "calculateAffectedNodes: appNodeServerPairs="+`appNodeServerPairs` )
                log(DEBUG_, "calculateAffectedNodes: appClusters="+`appClusters` )
                if (action != "uninstall"):
                        log(DEBUG_, "calculateAffectedNodes: appTestURLs="+`appTestURLs` )
                        log(DEBUG_, "calculateAffectedNodes: appTestResponses="+`appTestResponses` )
                #endIf

                ################## PRE-VALIDATE CLUSTERS ####################
                for cluster in appClusters:
                        PreValidateCluster(cluster )
                #endFor

                ################## APPEND TOTAL NODES/SERVERS/TESTS ############
                for appNodeServerPair in appNodeServerPairs:
                        nodeServerPairs.append(appNodeServerPair)
                        tmpNodeServerPairs.append(appNodeServerPair)
                #endFor
                clusterNodeServerPairs = getNodeServerPairs(appClusters )
                log(DEBUG_, "clusterNodeServerPairs="+`clusterNodeServerPairs`)
                for clusterNodeServerPair in clusterNodeServerPairs:
                        nodeServerPairs.append(clusterNodeServerPair)
                        tmpNodeServerPairs.append(clusterNodeServerPair)
                #endFor

                clusters.append(appClusters)

                for url in appTestURLs:
                        log(DEBUG_, "calculateAffectedNodes: url="+url )
                        testURLs.append(url)
                #endFor
                for response in appTestResponses:
                        log(DEBUG_, "calculateAffectedNodes: response="+response )
                        testResponses.append(response)
                #endFor

                appsNodesServers.append(appName)
                appsNodesServers.append(tmpNodeServerPairs)
        #endFor
        if (action != "uninstall"):
                log(VERBOSE_, "calculateAffectedNodes: RESULT: testURLs="+`testURLs` )
                log(VERBOSE_, "calculateAffectedNodes: RESULT: testResponses="+`testResponses` )
        #endIf
        log(VERBOSE_, "calculateAffectedNodes: RESULT: appsNodesServers="+`appsNodesServers` )
        log(VERBOSE_, "calculateAffectedNodes: RESULT: nodeServerPairs="+`nodeServerPairs` )

        ################## UNIQUE NODES (AND THEIR UNIQUE SERVERS) ############
        if (len(nodeServerPairs) == 0):
                fail("calculateAffectedNodes: No node/server/cluster (Targets) specified" )
        #endIf
        uniqueNodesContainedServers = determineUniqueNodesContainedServers(nodeServerPairs )
        log(INFO_, "calculateAffectedNodes: RESULT: uniqueNodesContainedServers="+`uniqueNodesContainedServers` )

        highlight(MAJOR_, "calculateAffectedNodes DONE." )
#endDef
