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
# 1.3 (22apr2005) moved proc getEnvar into new getEnvars.jacl
# 1.2 (14jan2005) added proc getEnvar
# 1.1 (17nov2004) initial shipped version
#

import sys
import re
import sre
def regexp(pattern, string, flags=0):
        if(re.compile(pattern, flags).search(string)==None): return 0
        else: return 1
def regexpn(pattern, string, flags=0):
        r = re.compile(pattern, flags).subn("", string)
        return r[1]
def wsadminToList(inStr):
        outList=[]
        if (len(inStr)>0 and inStr[0]=='[' and inStr[-1]==']'):
                tmpList = inStr[1:-1].split(" ")
        else:
                tmpList = inStr.split("\n")  #splits for Windows or Linux
        for item in tmpList:
                item = item.rstrip();        #removes any Windows "\r"
                if (len(item)>0):
                        outList.append(item)
        return outList
#endDef

errors = ""
warnings = ""
DEBUG_ = 5
VERBOSE_ = 4
INFO_ = 3
MAJOR_ = 2
WARNING_ = 1
ERROR_ = 0

def checkErrorsWarnings ( level, message ):
        global ScriptLocation
        execfile(ScriptLocation+"/Definitions.py" )
        global errors
        global warnings

        if (( level == ERROR_ ) ):
                message = "ERROR: "+message
                errors.append(message)
        elif (( level == WARNING_ ) ):
                message = "WARNING: "+message
                warnings.append(message)
        #endIf
        return message
#endDef

def log ( level, message ):
        global ScriptLocation
        execfile(ScriptLocation+"/Definitions.py" )
        global logLevel

        if (( level <= logLevel ) ):
                if (( logLevel != DEBUG_ ) ):
                        if ( level == ERROR_ ):
                                highlight(ERROR_, message )
                        elif ( level == WARNING_ ):
                                lowlight(WARNING_, message )
                        else:
                                print message
                        #endElse
                else:
                        checkErrorsWarnings(level, message )
                        if ( level == ERROR_ ):
                                print ".E ###ERROR### "+message
                        elif ( level == WARNING_ ):
                                print "..W ###WARNING### "+message
                        elif ( level == MAJOR_ ):
                                print "...M "+message
                        elif ( level == INFO_ ):
                                print "....I "+message
                        elif ( level == VERBOSE_ ):
                                print ".....V "+message
                        elif ( level == DEBUG_ ):
                                print "......D "+message
                        else:
                                print "???????? "+message
                        #endElse
                #endElse
        #endIf
#endDef

def fail ( msg ):
        global ScriptLocation
        execfile(ScriptLocation+"/Definitions.py" )
        global failOnError
        global errors
        msg = "FAILURE: "+msg
        debugHighlight(ERROR_, msg )
        if (failOnError):
                sys.exit(msg)
        #endIf
#endDef

def debugHighlight ( level, message ):
        global logLevel
        message = checkErrorsWarnings(level, message )
        if ( level <= logLevel ):
                ##puts ""
                print "#######################################################################"
                print message
                print "#######################################################################"
                ##puts ""
        #endIf
#endDef

def highlight ( level, message ):
        global logLevel
        message = checkErrorsWarnings(level, message )
        if ( level <= logLevel ):
                print "======================================================================="
                print message
                print "======================================================================="
        #endIf
#endDef

def lowlight ( level, message ):
        global logLevel
        message = checkErrorsWarnings(level, message )
        if ( level <= logLevel ):
                print "-----------------------------------------------------------------------"
                print message
                print "-----------------------------------------------------------------------"
        #endIf
#endDef
