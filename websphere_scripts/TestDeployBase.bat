@setlocal
@set DEPLOY_DIR=%~dp0
@set APP_NAME=AdderEAR
@set APP_EAR=%APP_NAME%.ear
@set APP_CONTEXT=AdderWAR/AdderTest.jsp


@rem ########################## SETUP: Admin+Target Servers #####################################

@rem LOCAL Base or Cell server (for wsadmin runtime) - does *NOT* need to be running
@set WSADMIN_BIN=G:\WAS-0608-40\profiles\Base61\bin
 set WSADMIN_BIN=G:\was-0620-14\profiles\Base61\bin

@rem TARGET (either Local or Remote) Base Server - *MUST* be running

@rem ### my Target *local v5* Base Server (local 'WAS50')setup:
@rem set ADMINHOST=searle
@rem set ADMINSOAP=8880
@rem set SERVERCELL=searleCell51
@rem set SERVERNODE=searleNodeWas50
@rem set SERVERNAME=server1
@rem set SERVERHOST=%ADMINHOST%
@rem set SERVERPORT=9080

@rem ### my Target *remote v6* Base Server (Remote 'Base6')setup:
@rem set ADMINHOST=bcsearle2
@rem set ADMINSOAP=8880
@rem set SERVERCELL=bcsearle2CellBase6
@rem set SERVERNODE=bcsearle2Base6
@rem set SERVERNAME=server1
@rem set SERVERHOST=%ADMINHOST%
@rem set SERVERPORT=9080

@rem ### my Target *remote v6* Managed Server (remote 'ND60') setup:
@rem set ADMINHOST=bcsearle2
@rem set ADMINSOAP=8879
@rem set SERVERCELL=bcsearle2Cell01
@rem set SERVERNODE=searleND6
@rem set SERVERNAME=server1
@rem set SERVERHOST=%ADMINHOST%
@rem set SERVERPORT=9084

@rem ### my Target *local v61* Base Server (local 'WAS61')setup:
set ADMINHOST=searle
set ADMINSOAP=8880
set SERVERCELL=searleNode02Cell
set SERVERNODE=searleNode61
set SERVERNAME=server1
set SERVERHOST=%ADMINHOST%
set SERVERPORT=9080



@rem ############################# cd wsadmin ########################################
@echo.
@set PARAM=%1
@if $%PARAM%$==$install$   goto wsadmin
@if $%PARAM%$==$uninstall$ goto wsadmin
@echo "ERROR: missing or invalid commandline parameter, must be: 'install' or 'uninstall'
@goto done

:wsadmin
@if not exist "%WSADMIN_BIN%\wsadmin*" echo ERROR: incorrect WSADMIN_BIN=%WSADMIN_BIN%, edit this TestDeployBase.bat and correct this envar
@if not exist "%WSADMIN_BIN%\wsadmin*" goto done
@cd /D %WSADMIN_BIN%
@copy /y "%DEPLOY_DIR%\dist\%APP_EAR%" %APP_EAR%

@set CONN=-conntype SOAP -host %ADMINHOST% -port %ADMINSOAP%
@if $%2$==$bypass$   goto adminType


@REM ################## Check ADMIN and TARGET Servers #######################
:adminState
@echo.
@echo CHECK SERVER...
@set CMD=$AdminControl getAttribute [$AdminControl queryNames node=[$AdminControl getNode],type=Server,*] state
call wsadmin %CONN% -c "%CMD%" -lang jacl > TestDeployBaseTemp
@sort TestDeployBaseTemp /O TestDeployBaseTemp
@set /P Admin_Server_State=<TestDeployBaseTemp
@echo.
@echo Check: Admin serverState must be "STARTED":
@echo Admin_Server_State: %Admin_Server_State%
@if $%Admin_Server_State%$==$STARTED$   goto adminType
echo ERROR: Admin_Server_State=%Admin_Server_State%
goto done

:adminType
@echo.
@set CMD=$AdminControl getAttribute [$AdminControl queryNames node=[$AdminControl getNode],type=Server,*] processType
call wsadmin %CONN% -c "%CMD%" -lang jacl > TestDeployBaseTemp
@sort TestDeployBaseTemp /O TestDeployBaseTemp
@set /P Admin_Server_Type=<TestDeployBaseTemp
@echo.
@echo Check: Admin processType must be "UnManagedProcess" or "DeploymentManager":
@echo Admin_Server_Type: %Admin_Server_Type%
@if $%Admin_Server_Type%$==$UnManagedProcess$   goto targetState
@if $%Admin_Server_Type%$==$DeploymentManager$  goto targetState
echo ERROR: Admin_Server_type=%Admin_Server_Type%
goto done

:targetState
@if $%2$==$bypass$   goto begin
@echo.
@set CONN=-conntype SOAP -host %ADMINHOST% -port %ADMINSOAP%
@set CMD=$AdminControl getAttribute [$AdminControl queryNames node=%SERVERNODE%,process=%SERVERNAME%,type=Server,*] state
call wsadmin %CONN% -c "%CMD%" -lang jacl > TestDeployBaseTemp
@sort TestDeployBaseTemp /O TestDeployBaseTemp
@set /P Target_Server_State=<TestDeployBaseTemp
@echo.
@if not $%ADMINHOST%$==$%SERVERHOST%$ echo Check: ERROR: Target SERVER must be on ADMIN machine to fetch state...
@echo Check: Target (either Local or Remote) serverState must be "STARTED":
@echo Target_Server_State: %Target_Server_State%
@if $%Target_Server_State%$==$STARTED$   goto targetType
echo ERROR: Target_Server_State=%Target_Server_State%
goto done

:targetType
@echo.
@set CMD=$AdminControl getAttribute [$AdminControl queryNames node=%SERVERNODE%,process=%SERVERNAME%,type=Server,*] processType
call wsadmin %CONN% -c "%CMD%" -lang jacl > TestDeployBaseTemp
@sort TestDeployBaseTemp /O TestDeployBaseTemp
@set /P Target_Server_Type=<TestDeployBaseTemp
@echo.
@echo Check: Target (either Local or Remote) processType must be "UnManagedProcess" or "ManagedProcess":
@echo Target_Server_Type: %Target_Server_Type%
@if $%Target_Server_Type%$==$UnManagedProcess$   goto begin
@if $%Target_Server_Type%$==$ManagedProcess$     goto begin
echo ERROR: Target_Server_type=%Target_Server_Type%
@echo.
goto done


@REM ################## begin #######################
:begin
@echo.
@echo LIST APPs...
@set CMD_OPTIONS={"WebSphere:cell=%SERVERCELL%,node=%SERVERNODE%,server=%SERVERNAME%"}  ####v6.1 only
@set CMD_OPTIONS=
set CMD=$AdminApp list %CMD_OPTIONS%
call wsadmin %CONN% -c "%CMD%" -lang jacl

@echo.
@set PARAM=%1
@if $%PARAM%$==$install$   goto InstallStart
@if $%PARAM%$==$uninstall$ goto StopUninstall
@echo "ERROR: missing or invalid commandline parameter, must be: 'install' or 'uninstall'
@goto done


@REM ################## InstallStart #######################
:InstallStart
@echo.
@echo INSTALL APP...
@set CMD_OPTIONS=-verbose -node %SERVERNODE% -server %SERVERNAME% -distributeApp -nodeployejb
@set CMD=$AdminApp install %APP_EAR% {%CMD_OPTIONS%}
call wsadmin %CONN% -c "%CMD%" -lang jacl

@echo SAVE CONFIG...
@set CMD=$AdminConfig save
call wsadmin %CONN% -c "%CMD%" -lang jacl

@echo LIST APPs...
@set CMD_OPTIONS={"WebSphere:cell=%SERVERCELL%,node=%SERVERNODE%,server=%SERVERNAME%"}  ####v6.1 only
@set CMD_OPTIONS=
@set CMD=$AdminApp list
call wsadmin %CONN% -c "%CMD%" -lang jacl

@REM ###### Non-Base (Managed) Servers need NodeSync to distribute/install application
@echo.
@if $%Admin_Server_Type%$==$UnManagedProcess$   @echo Note: BaseServers do not need Dmgr NodeSync to distribute/install application into server.
@if $%Admin_Server_Type%$==$UnManagedProcess$   @goto StartApp
@echo SYNC NODE...   (Target Server is a Managed Server, *NOT* a Base Server)
@set CMD=$AdminControl invoke [$AdminControl completeObjectName type=NodeSync,node=%SERVERNODE%,*] sync
call wsadmin %CONN% -c "%CMD%" -lang jacl

:StartApp
@echo.
@echo START APP...
@set CMD=$AdminControl invoke [$AdminControl queryNames type=ApplicationManager,node=%SERVERNODE%,process=%SERVERNAME%,*] startApplication %APP_NAME%
call wsadmin %CONN% -c "%CMD%" -lang jacl

@echo.
@echo BROWSE APP...
call EXPLORER.EXE http://%SERVERHOST%:%SERVERPORT%/%APP_CONTEXT%

@goto done


@REM ################## StopUninstall #######################
:StopUninstall
@echo.
@echo STOP APP...
@set CMD=$AdminControl invoke [$AdminControl queryNames type=ApplicationManager,node=%SERVERNODE%,process=%SERVERNAME%,*] stopApplication %APP_NAME%
call wsadmin %CONN% -c "%CMD%" -lang jacl

@echo UNINSTALL APP...
@set CMD=$AdminApp uninstall %APP_NAME%
call wsadmin %CONN% -c "%CMD%" -lang jacl

@echo SAVE CONFIG...
@set CMD=$AdminConfig save
call wsadmin %CONN% -c "%CMD%" -lang jacl

@echo LIST APPs...
@set CMD_OPTIONS={"WebSphere:cell=%SERVERCELL%,node=%SERVERNODE%,server=%SERVERNAME%"}  ####v6.1 only
@set CMD_OPTIONS=
@set CMD=$AdminApp list
call wsadmin %CONN% -c "%CMD%" -lang jacl


@REM ################## Done #######################
:done
@rem if exist TestDeployBaseTemp del TestDeployBaseTemp
@if exist %APP_EAR% del %APP_EAR%
@cd /D "%DEPLOY_DIR%"
@echo.
@echo TestDeployBase DONE.
@echo.
