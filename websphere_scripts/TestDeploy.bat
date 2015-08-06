@REM Author: Barry Searle
@REM
@REM (C) Copyright IBM Corp. 2004,2005 - All Rights Reserved.
@REM DISCLAIMER:
@REM The following source code is sample code created by IBM Corporation.
@REM This sample code is not part of any standard IBM product and is provided
@REM to you solely for the purpose of assisting you in the development of your
@REM applications. The code is provided 'AS IS', without warranty or condition
@REM of any kind. IBM shall not be liable for any damages arising out of your
@REM use of the sample code, even if IBM has been advised of the possibility of
@REM such damages.
@REM
@REM Change History:
@REM 2.0 (10feb2006) added Jython invocation
@REM 1.3 (22apr2005) eliminated D_WAS param, D_DIST optional
@REM 1.2 (14jan2005) use automatically determined D_XXXX envars
@REM 1.1 (17nov2004) initial version
@REM


setlocal
@rem ####### ONLY 'WAS_ROOT' MUST BE SET #######
set WAS_ROOT=G:\was60nd\profiles\ND60
set WAS_ROOT=G:\was-0620-14\profiles\ND61
set ADMIN=-host bcsearle2 -port 8879 -username wsguest -password ws61guest

if $%LANG%$==$$ set LANG=jython
set ADMIN=%ADMIN% -lang %LANG%

@rem ####### disable ('@REM') to bypass browser check #######
set SERVERHTTP=bcsearle2:9081
set APP_CONTEXT=AdderWAR/AdderTest.jsp



@rem removes any previous WAS_USER_SCRIPT (affects wsadmin)
@set WAS_USER_SCRIPT=

@set D_ROOT=%~dp0
@set script=%D_ROOT%automatedDeploy.jacl
if %LANG%==jython @set script=%D_ROOT%automatedDeploy.py
@set D_ACTION=install
@set D_STAGE=-pilot
@set D_FAIL=true
@set D_DIST=%~dp0dist

@if $%1$==$ant$ goto ant
@if $%1$==$install$   goto install
@if $%1$==$update$    goto update
@if $%1$==$confirm$   goto confirm
@if $%1$==$uninstall$ goto uninstall



:install
@rem                                                  action     stage   fail   distDir
@echo FIRST install ...
call "%WAS_ROOT%\bin\wsadmin" %ADMIN% -f "%script%"  %D_ACTION% %D_STAGE% %D_FAIL%

:manualCheck
@echo.
@echo SIMULATE OPERATOR MANUAL CHECK (BROWSE APP) ...
@if not $%SERVERHTTP%$==$$ call EXPLORER.EXE http://%SERVERHTTP%/%APP_CONTEXT%
@if not $%SERVERHTTP%$==$$ pause
@if not $%1$==$$ goto done

:update
@echo SECOND update ...
call "%WAS_ROOT%\bin\wsadmin" %ADMIN% -f "%script%"  update    -pilot   false
@if not $%1$==$$ goto done

:confirm
@echo.
@echo THIRD confirm ...
call "%WAS_ROOT%\bin\wsadmin" %ADMIN% -f "%script%"  confirm   -pilot
@if not $%1$==$$ goto done

:uninstall
@echo.
@echo FOURTH uninstall ...
call "%WAS_ROOT%\bin\wsadmin" %ADMIN% -f "%script%"  uninstall
@goto done



:ant
REM ####### edit automatedD_xml  to set WASROOT location #######
call "%WAS_ROOT%\bin\ws_ant" -buildfile "%D_ROOT%/automatedDeploy.xml"

:done
@echo DONE.
