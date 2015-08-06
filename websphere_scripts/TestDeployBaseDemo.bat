@REM Author: Barry Searle
@REM
@REM (C) Copyright IBM Corp. 2004,2006 - All Rights Reserved.
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
@REM 2.0 (27jan2006) changes to invoked TestDeployBase.bat
@REM 1.2 (14jan2005) initial version
@REM

setlocal

@echo.
@echo ########## DEMO, invoke TestDeployBase install ...
call TestDeployBase install
pause
@echo.
@echo ########## DEMO, invoke TestDeployBase uninstall ...
call TestDeployBase uninstall
@echo.
@echo ########## DEMO, TestDeployBaseDemo DONE.
@echo.

