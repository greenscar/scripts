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
# 3.1 (08may2007) initial version
#

def PreValidateScriptsFile ( appName, depType, distDir ):
        ext = ".scripts"
        scriptsFileName = distDir+"/"+appName+depType+ext

        import os
        if (os.path.isfile( scriptsFileName ) == 0):
                msg = "PreValidateScriptsFile: ERROR: is not a file, scriptsFileName="+scriptsFileName
                fail(msg )
        #endIf
        if (os.path.exists( scriptsFileName ) == 0):
                msg = "PreValidateScriptsFile: ERROR: does not exist, scriptsFileName="+scriptsFileName
                fail(msg )
        #endIf
        #if (os.access( scriptsFileName, os.R_OK ) == 0):
        #        msg = "PreValidateScriptsFile: ERROR: cannot read scriptsFileName="+scriptsFileName
        #        fail(msg )
        #endIf
#endDef

def processScriptsFile ( appName, depType, distDir, phase ):
        global ScriptLocation
        execfile(ScriptLocation+"/Definitions.py" )
        PreValidateScriptsFile(appName, depType, distDir )
        ext = ".scripts"
        scriptsFileName = distDir+"/"+appName+depType+ext
        log(INFO_, "" )
        log(MAJOR_, "processScriptsFile: scriptsFileName="+scriptsFileName )

        try:
                _excp_ = 0
                fd = open( scriptsFileName, "r" )
        except:
                _type_, _value_, _tbck_ = sys.exc_info()
                fd = `_value_`
                _excp_ = 1
        #endTry
        temp = _excp_
        if (temp == 0):
                more = true
        else:
                more = false
                msg = "ERROR: processScriptsFile: attempting to read scriptsFileName="+scriptsFileName
                fail(msg )
                sys.exit(-1)
        #endElse
        blankLines = 0
        while (more):
            try:
                #done = eof(fd)  #?PROBLEM? (jacl 59) CMD_EOF: replace with try:...except: EOFError
                #if (done ):
                #        more = false
                #endIf
                line = fd.readline().strip()
                line = line.strip( )
                comment = line.find("#")
                if (comment == 0):
                        line = ""
                #endIf
                i = line.find("=")
                if (i > 0):
                        param1 = line[0:(i-0)]
                        param1 = param1.strip()
                        param2 = line[(i+1):]
                        if (param1 == "scriptLoad"):
                                param2 = param2+".py"
                                log(INFO_, "processScriptsFile: "+phase+" script="+param2 )
                                execfile(ScriptLocation+"dist/"+param2 )
                        elif (param1 == phase):
                                log(INFO_, "processScriptsFile: "+phase+" Call="+param2 )
                                try:
                                        _excp_ = 0
                                        exec param2
                                        _excp_ = 0 #reset (in case of nested exceptions)
                                except:
                                        _type_, _value_, _tbck_ = sys.exc_info()
                                        _excp_ = 1
                                        log(INFO_, "processScriptsFile: "+phase+" scriptCall Exception _type_ "+`_type_`)
                        		value = `_value_`
                        		WSEX = "com.ibm.ws.scripting.ScriptingException: "
                        		if(value.startswith(WSEX)):
			                        value = value[len(WSEX):]
                        		#endIf
                        		sd = `_tbck_.dumpStack()`
                        		sd = sd.replace("\\\\","/")
                        		i = sd.rfind("  File ")
                        		j = sd.rfind(", line ")
                        		k = sd.rfind(", in ")
                        		locn = ""		
                        		if(i>0 and j>0 and k>0):
                        			file = sd[i+7:j]
			                        line = sd[j+7:k]
                        			func = sd[k+4:-3]
			                        locn = "Function="+func+"  Line="+line+"  File="+file
                        		#endIf
                                        log(INFO_, "processScriptsFile: "+phase+" scriptCall Exception locn "+`locn`)
                                        msg = "processScriptsFile: "+phase+" scriptCall Exception "+`_value_`
                                        fail(msg )
                                #endTry 
                                temp = _excp_
                        else:
                                if (param1!="preInstall" and param1!="postInstall" and param1!="preUninstall" and param1!="postUninstall"):
                                        msg = "processScriptsFile: ERROR unknown: "+param1+"  "+`param2`
                                        log(ERROR_, msg )
                                        fail(msg )
                                #endIf
                        #endElse
                else:
                        if (""== line):
                                pass
                        else:
                                msg = "processScriptsFile: ERROR(MISSING'='):  line="+line+"   scriptsfile="+scriptsFileName+" "
                                log(ERROR_, msg )
                                fail(msg )
                        #endElse
                #endElse

                if(line==""): blankLines = blankLines +1
                else: blankLines = 0
                if(blankLines>30):  more = false
            except EOFError:
                more = false
            #endTry
        #endWhile
        log(VERBOSE_, "processScriptsFile: DONE." )
        fd.close()
#endDef
