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
# 2.0 (10feb2006) initial Jython version, corrected Start/StopApplicationOnNodesAndServers
# 1.3 (22apr2005) additional socket exception checks, fixed GET HTTP/1.1
# 1.2 (14jan2005) fixed startApp returns if exception, does not checkIfRunning
# 1.2 (14jan2005) fixed testApp index into testURLs and testResponses
# 1.1 (17nov2004) initial shipped version
#

def PreValidateApplicationTargetsAndSettings ( appNames, depType, distDir ):
        global ScriptLocation
        execfile(ScriptLocation+"/Definitions.py" )
        for appName in appNames:
                PreValidateTargetsFile(appName, depType, distDir )
                PreValidateSettingsFile(appName, depType, distDir )
        #endFor
        log(INFO_, "PreValidateApplicationTargetsAndSettingsFilesExist DONE. appNames="+`appNames`+" depType="+depType )
#endDef

def PreValidateApplicationsExist ( appNames ):
        global ScriptLocation
        execfile(ScriptLocation+"/Definitions.py" )
        for appName in appNames:
                appExists = checkIfAppExists(appName )
                if (appExists):
                        log(INFO_, "PreValidateApplicationsExist OK appName="+appName )
                else:
                        fail("PreValidateApplicationsPresent: MISSING application="+appName )
                #endElse
        #endFor
#endDef

def PreValidateApplicationsAbsent ( appNames ):
        global ScriptLocation
        execfile(ScriptLocation+"/Definitions.py" )
        for appName in appNames:
                appExists = checkIfAppExists(appName )
                if (appExists):
                        fail("PreValidateApplicationsAbsent: EXISTING application="+appName )
                else:
                        log(INFO_, "PreValidateApplicationsAbsent: OK appName="+appName )
                #endElse
        #endFor
#endDef

def validateApplication ( appName ):
        global ScriptLocation
        execfile(ScriptLocation+"/Definitions.py" )
        log(INFO_, "validateApplication "+appName+" ..." )
        AdminConfig.validate(AdminConfig.getid("/Deployment:"+appName+"/" ) )
#endDef

def listApplications (  ):
        global ScriptLocation
        execfile(ScriptLocation+"/Definitions.py" )
        log(INFO_, "ListApplications:" )
        apps = AdminApp.list( )
        apps = wsadminToList(apps) # running on windows, target is linux (different SEPARATOR)
        for app in apps:
                log(INFO_, "  "+app )
        #endFor
        log(VERBOSE_, "ListApplications: DONE." )
#endDef

def checkIfAppExists ( appName ):
        global ScriptLocation
        execfile(ScriptLocation+"/Definitions.py" )
        appExists = true
        application = AdminConfig.getid("/Deployment:"+appName+"/" )
        log(DEBUG_, "checkIfAppExists appName="+appName+" installedAppID="+application )
        if (len(application) == 0):
                appExists = false
                log(VERBOSE_, "checkIfAppExists: FALSE for appName="+appName )
        else:
                log(VERBOSE_, "checkIfAppExists: TRUE for appName="+appName )
        #endElse
        return appExists
#endDef

def StartApplication ( appName, nodeName, serverName ):
        global ScriptLocation
        execfile(ScriptLocation+"/Definitions.py" )
        log(INFO_, "StartApplication: appName="+appName+" nodeName="+nodeName+" serverName="+serverName+" ..." )
        appMgrID = AdminControl.queryNames("type=ApplicationManager,node="+nodeName+",process="+serverName+",*" )
        length = len(appMgrID)
        log(DEBUG_, "StartApplication: appMgrID.length="+`length`+" appMgrID="+appMgrID )
        if (length >= 1):
                log(VERBOSE_, "StartApplication: starting "+appName+"  ..." )
                try:
                        _excp_ = 0
                        started = AdminControl.invoke(appMgrID, "startApplication", appName )
                except:
                        _type_, _value_, _tbck_ = sys.exc_info()
                        _excp_ = 1
                #endTry
                temp = _excp_
                if (temp > 0):
                        log(WARNING_, "StartApplication: Exception trying to start "+appName+" "+nodeName+" "+serverName )
                        return
                else:
                        if (len(started) > 0):
                                log(INFO_, started )
                        #endIf
                #endElse
        else:
                log(ERROR_, "StartApplication: appMgr ERROR, NOT ACCESSABLE, cannot start "+appName )
        #endElse
        checkApplicationRunning(nodeName, serverName, appName )
        log(VERBOSE_, "StartApplication: DONE." )
#endDef

def StopApplication ( appName, nodeName, serverName ):
        global ScriptLocation
        execfile(ScriptLocation+"/Definitions.py" )
        log(INFO_, "StopApplication: appName="+appName+" nodeName="+nodeName+" serverName="+serverName+" ..." )
        appMgrID = AdminControl.queryNames("type=ApplicationManager,node="+nodeName+",process="+serverName+",*" )
        length = len(appMgrID)
        log(VERBOSE_, "StopApplication: appMgrID.length="+`length`+" appMgrID="+appMgrID )
        if (length >= 1):
                log(VERBOSE_, "StopApplication: stopping "+appName+"  ..." )
                stopped = ""
                try:
                        _excp_ = 0
                        stopped = AdminControl.invoke(appMgrID, "stopApplication", appName )
                except:
                        _type_, _value_, _tbck_ = sys.exc_info()
                        _excp_ = 1
                #endTry
                temp = _excp_
                if (temp > 0):
                        log(WARNING_, "StopApplication: Exception trying to stop "+appName+" "+nodeName+" "+serverName )
                else:
                        if (len(stopped) > 0):
                                log(VERBOSE_, stopped )
                        #endIf
                #endElse
        else:
                log(ERROR_, "StopApplication: appMgr ERROR, NOT ACCESSABLE, cannot stop "+appName )
        #endElse
        log(VERBOSE_, "StopApplication: DONE." )
#endDef

def testApplication ( testURLs, testResponses ):
        global ScriptLocation
        execfile(ScriptLocation+"/Definitions.py" )
        global WasTclVers
        log(INFO_, "" )
        log(MAJOR_, "testApplication: URLs and Responses begin ..." )
        log(DEBUG_, "testApplication: testResponses="+`testResponses` )
        log(DEBUG_, "testApplication: testURLs="+`testURLs` )
        indx = 0
        length = len(testURLs)
        while (indx < length):
                url = testURLs[indx]
                response = testResponses[indx]
                log(VERBOSE_, "testApplication: testURL="+url+"  testResponse="+response )
                indx = indx+1
                lines = readWebPage(url )
                if (lines==None or len(lines) == 0):
                        if (WasTclVers > 5):
                                log(ERROR_, "testApplication: FAILED CONNECT:  url="+url+"  expectedResponse="+response )
                        else:
                                log(WARNING_, "testApplication: (v51 Tcl/Jacl) FAILED CONNECT: url="+url+"  expectedResponse="+response )
                        #endElse
                        continue
                #endIf
                found = false
                for line in lines:
                        if (line.find(response) >= 0):
                                found = true
                        #endIf
                #endFor
                if (found):
                        log(INFO_, "testApplication: PASSED: url="+url+"  contained="+response )
                else:
                        log(ERROR_, "testApplication: FAILED RESPONSE: url="+url+"  expectedResponse="+response )
                #endElse
        #endWhile
#endDef

def readWebPage ( webpageURL ):
        global ScriptLocation
        execfile(ScriptLocation+"/Definitions.py" )
        global WasTclVers
        webpageURL = webpageURL.strip( )
        log(VERBOSE_, "readWebpage webpageURL="+webpageURL )
        slash = webpageURL.find("http://")
        if (slash < 0):
                log(ERROR_, "missing 'http://' from webpageURL="+webpageURL )
                return
        #endIf
        webpageURL = webpageURL[7:]

        slash = webpageURL.find("/")
        if (slash < 0):
                log(ERROR_, "missing server'/'path' from webpageURL="+webpageURL )
                return
        #endIf
        port = ""
        server = ""
        path = ""
        #if ( not x = regexp("^(http://)?([^:/]+)(:([0-9]+))?(/.*)", webpageURL, sre.IGNORECASE) #?PROBLEM? (jacl 194) SUBS REGXXX_NUMBER_ARGS "protocol" "server" "y" "port" "path"  ):
        #        log(ERROR_, "readWebPage: invalid webpageURL="+webpageURL )
        #        return
        #endIf
        server = server.strip( )
        if (port == ""):
                port = 80
        #endIf
        path = webpageURL[(slash):]
        server = webpageURL[0:(slash-0)]
        colon = server.find(":")
        if (colon > 0):
                port = server[(colon+1):]
                server = server[0:(colon-0)]
        #endIf

        log(VERBOSE_, "readWebPage: server="+server+" port="+port+" contextPath="+path )
        try:
                _excp_ = 0
                import httplib
                conn = httplib.HTTPConnection(server,int(port))  #?PROBLEM? (jacl 212) COMMAND_UNKNOWN?  socket(server, port )
        except:
                _type_, _value_, _tbck_ = sys.exc_info()
                log(DEBUG_, "readWebPage Connection Exception="+`_value_`+`_value_` )
                _excp_ = 1
        #endTry
        e = _excp_
        if (e != 0):
                log(ERROR_, "readWebPage: set socket error="+`e`+" for server="+server+" port="+port )
                if (WasTclVers <= 0):
                        log(WARNING_, "testApplication: TclJava.jar and/or Jacl.jar too old for web/socket reads ?" )
                        log(WARNING_, "testApplication: #### (copy TCLJAVA.jar plus JACL.jar from WAS60-lib into WAS51-lib) ####" )
                        WasTclVers = 5
                #endIf
                return
        else:
                WasTclVers = 6
        #endElse
        log(DEBUG_, "readWebPage: completed set socket server="+server+" port="+port )

        try:
                _excp_ = 0
                conn.request("GET",path)
                #print >>sock, "GET "+path+" HTTP/1.1"
                #print >>sock, "Host: "+server
                #print >>sock, "Accept: */*"
                #print >>sock, "User-Agent: JACL readWebPage"
                #print >>sock, ""
                log(DEBUG_, "readWebPage: completed write request to socket ' '" )
                #sock.flush()
                #log(DEBUG_, "readWebPage: completed flush socket " )
        except:
                _type_, _value_, _tbck_ = sys.exc_info()
                log(DEBUG_, "readWebPage Connection Exception="+`_value_`+`_value_` )
                _excp_ = 1
        #endTry
        e = _excp_
        if (e != 0):
                log(ERROR_, "readWebPage: puts socket write error="+`e`+"  (for server="+server+" port="+port+")" )
                return
        #endIf
        log(INFO_, "readWebPage: completed request writes to socket, will read response" )

        lines = []
        lineCount = 0
        more = true
        #done = eof(sock)  #?PROBLEM? (jacl 247) CMD_EOF: replace with try:...except: EOFError
        #if (done):
        #        more = false
        #endIf
        blankLines = 0
        response = conn.getresponse()
        log(VERBOSE_,"WebPageResponse status="+`response.status`+" reason="+`response.reason` )
        resp = response.read(4096)
        #log(DEBUG_,"WebPageResponse resp="+`resp` )
        respLines = wsadminToList(resp)
        indx = 0
        while (more):
            try:
                lineCount = lineCount+1
                if (lineCount > 100):
                        more = false
                #endIf
                #done = eof(sock)  #?PROBLEM? (jacl 255) CMD_EOF: replace with try:...except: EOFError
                #if (done):
                #        more = false
                #else:
                if(indx>=len(respLines)):
                        more = false
                else:
                        #line = sock.readline().strip()
                        line = respLines[indx]
                        indx += 1
                        line = line.strip( )
                        log(DEBUG_, "readWebPage line="+line )
                        if (( line == "Connection: Close")  or
                          ( line == "HTTP/1.1 505 HTTP Version Not Supported") ):
                                log(ERROR_, "readWebPage: Connection Closed, error="+line+"  (for server="+server+" port="+port+")" )
                                EOF = true
                                more = false
                        #endIf
                        lines.append(line)
                        line = line.upper()
                        if (line == "</BODY>"):
                                more = false
                        #endIf
                #endElse

                if(line==""): blankLines = blankLines +1
                else: blankLines = 0
                if(blankLines>20):  more = false
            except EOFError:
                more = false
            #endTry
        #endWhile
        try:
                _excp_ = 0
                x = conn.close()
        except:
                _type_, _value_, _tbck_ = sys.exc_info()
                _excp_ = 1
        #endTry
        return lines
#endDef

def StopApplicationOnNodesAndServers ( appName, uniqueNodeServerPairs ):
        global ScriptLocation
        execfile(ScriptLocation+"/Definitions.py" )
        log(MAJOR_, "StopApplicationOnNodesAndServers: appName="+appName+" NodeServerPairs="+`uniqueNodeServerPairs`+"..." )
        if (len(uniqueNodeServerPairs) == 0):
                fail(ERROR_, "StopApplicationOnNodesAndServers : No nodes/servers/clusters specified" )
        #endIf
        for nodeServer in uniqueNodeServerPairs:
                nodeName = nodeServer[0]
                serverName = nodeServer[1]
                StopApplication(appName, nodeName, serverName )
        #endFor
        highlight(MAJOR_, "StopApplicationOnNodesAndServers  DONE." )
#endDef

def StartApplicationOnNodesAndServers ( appName, uniqueNodeServerPairs ):
        global ScriptLocation
        execfile(ScriptLocation+"/Definitions.py" )
        log(INFO_, "" )
        log(MAJOR_, "StartApplicationOnNodesAndServers: appName="+appName+" NodeServerPairs="+`uniqueNodeServerPairs`+"..." )
        if (len(uniqueNodeServerPairs) == 0):
                fail(ERROR_, "StartApplicationOnNodesAndServers : No nodes/servers/clusters specified" )
        #endIf
        for nodeServer in uniqueNodeServerPairs:
                nodeName = nodeServer[0]
                serverName = nodeServer[1]
                StartApplication(appName, nodeName, serverName )
        #endFor
        highlight(MAJOR_, "StartApplicationOnNodesAndServers  DONE." )
#endDef

def checkApplicationRunning ( nodeName, serverName, appName ):
        global ScriptLocation
        execfile(ScriptLocation+"/Definitions.py" )
        log(VERBOSE_, "checkApplicationRunning: "+nodeName+" "+serverName+" "+appName )
        appID = ""
        try:
                _excp_ = 0
                appID = AdminControl.completeObjectName("type=Application,node="+nodeName+",Server="+serverName+",name="+appName+",*" )
        except:
                _type_, _value_, _tbck_ = sys.exc_info()
                _excp_ = 1
        #endTry
        temp = _excp_
        if (temp > 0):
                log(WARNING_, "checkApplicationRunning: Exception trying to getID for "+appName+" "+nodeName+" "+serverName )
        #endIf
        length = len(appID)
        log(DEBUG_, "checkApplicationRunning: appID.length="+`length` )

        retries = 0
        while (( retries < 20 )  and ( length == 0 ) ):
                retries = retries+1
                log(INFO_, "checkApplicationRunning: not yet started, "+appName+" "+nodeName+" "+serverName+", retries="+`retries`+" ..." )
                try:
                        _excp_ = 0
                        sleepDelay(10 )
                        appID = AdminControl.completeObjectName("type=Application,node="+nodeName+",Server="+serverName+",name="+appName+",*" )
                        if (len(appID) == 0):
                                appExists = checkIfAppExists(appName )
                                if (appExists):
                                        pass
                                else:
                                        log(ERROR_, "checkApplicationRunning: "+appName+" is NOT INSTALLED." )
                                        return
                                #endElse
                        #endIf
                except:
                        _type_, _value_, _tbck_ = sys.exc_info()
                        _excp_ = 1
                #endTry
                temp = _excp_
                if (temp > 0):
                        log(WARNING_, "checkApplicationRunning: Exception trying to getID for "+appName+" "+nodeName+" "+serverName )
                #endIf
                length = len(appID)
                log(DEBUG_, "checkApplicationRunning: temp="+`temp`+" appID="+appID+" ..." )
        #endWhile
        if (retries > 0):
                log(INFO_, "checkApplicationRunning: "+nodeName+" "+serverName+" "+appName+" had slow start, status retries="+`retries` )
        #endIf

        if (length > 0):
                log(INFO_, "checkApplicationRunning: "+appName+" is STARTED." )
        else:
                log(ERROR_, "checkApplicationRunning: "+appName+" "+nodeName+" "+serverName+" DID NOT START." )
        #endElse
#endDef
