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
# 3.1 (08may2007) moved installOptions from getTargets
# 2.0 (10feb2006) initial Jython version,
# 2.0 (10feb2006) API: changed WebModuleName test into ModuleName
# 1.1 (17nov2004) initial shipped version
#

def PreValidateSettingsFile ( appName, depType, distDir ):
        ext = ".settings"
        settingsFileName = distDir+"/"+appName+depType+ext

        import os
        if (os.path.isfile( settingsFileName ) == 0):
                msg = "PreValidateSettingsFile: ERROR: is not a file, settingsFileName="+settingsFileName
                fail(msg )
        #endIf
        if (os.path.exists( settingsFileName ) == 0):
                msg = "PreValidateSettingsFile: ERROR: does not exist, settingsFileName="+settingsFileName
                fail(msg )
        #endIf
        #if (os.access( settingsFileName, os.R_OK ) == 0):
        #        msg = "PreValidateSettingsFile: ERROR: cannot read settingsFileName="+settingsFileName
        #        fail(msg )
        #endIf
#endDef

def processSettingsFileInstallOptions ( appName, depType, distDir  ):
        global ScriptLocation
        execfile(ScriptLocation+"/Definitions.py" )
        installOptions = processSettingsFile( appName, depType, distDir, false )
        return installOptions
#endDef

def processSettingsFile ( appName, depType, distDir, performSet ):
        from org.python.util import PythonInterpreter as P_Interpretor
        global ScriptLocation
        execfile(ScriptLocation+"/Definitions.py" )
        PreValidateSettingsFile(appName, depType, distDir )
        ext = ".settings"
        settingsFileName = distDir+"/"+appName+depType+ext
        log(INFO_, "" )
        if (performSet == true ): log(MAJOR_, "processSettingsFile: settingsFileName="+settingsFileName )
        objectType = "?"
        objectName = "?"
        showSetResult = false
        appName = "?"
        installOptions = ""

        try:
                _excp_ = 0
                fd = open( settingsFileName, "r" )
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
                msg = "ERROR: processSettingsFile: attempting to read settingsFileName="+settingsFileName
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
                        attrib = line[0:(i-0)]
                        attrib = attrib.strip()
                        value = line[(i+1):]
                        if (attrib == "ApplicationName"):
                                objectName = value
                                objectType = "Application"
                                if (performSet == true ): log(VERBOSE_, "processSettingsFile: objectType="+objectType+" objectName="+objectName )
                                appName = value
                        elif (attrib == "ModuleName"):
                                objectName = value
                                objectType = "Module"
                                if (performSet == true ): log(VERBOSE_, "processSettingsFile: objectType="+objectType+" objectName="+objectName )
                        elif (attrib == "showSetResult"):
                                if (value == "true" or value == "TRUE" or value == "1"):
                                        value = 1
                                else:
                                        value = 0
                                #endElse
                                showSetResult = value
                                if (performSet == true ): log(VERBOSE_, "processSettingsFile: showSetResult="+`showSetResult` )
                        elif (attrib == "installOption"):
                                if (performSet != true ): log(VERBOSE_, "processSettingsFile: installOption="+value )
                                installOptions = installOptions +" " +value
                        else:
                                if (performSet == true ): log(VERBOSE_, "processSettingsFile: attribute="+attrib+"  value="+`value`+"  objectType="+objectType+"  objectName="+objectName+"  showSetResult="+`showSetResult` )
                                if (performSet == true ): setAttribute(objectName, objectType, attrib, value, appName, showSetResult )
                        #endElse
                else:
                        if (""== line):
                                pass
                        else:
                                msg = "processSettingsFile: ERROR(MISSING'='):  line="+line+"   propertyfile="+settingsFileName+" "
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
        log(VERBOSE_, "processSettingsFile: DONE." )
        fd.close()
        return installOptions
#endDef
