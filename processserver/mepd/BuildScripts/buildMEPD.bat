
@echo off

rem -----------------------------------------------------------------------------------------
rem This file is used to set up the necessary variables to run build automation.
rem Run buildMEPD.bat from the same directory.
rem -----------------------------------------------------------------------------------------

rem ANT Home Dir
set ANT_HOME=C:\IBM\SDP70\runtimes\bi_v61\bin
       
rem Set JAVA_HOME to JDK path
set JAVA_HOME=C:\IBM\SDP70\jdk

rem WebSphere Process Server Home Dir
SET WPS_HOME=C:\IBM\SDP70\runtimes\bi_v61
rem Root directory of the automation scripts and workspaces (usually /Automation)
SET AUTOMATION_HOME=C:\views\vnandrambakkam01_MEPD_74_0_0_0\EA_mepd1\BuildScripts

rem ClearCase view directory 
SET CLEARCASE_VIEW=C:\views\vnandrambakkam01_MEPD_74_0_0_0\EA_mepd1

rem SharedService and SharedUtilProj ClearCase view directory 
SET LIB_CLEARCASE_VIEW=C:\views\vnandrambakkam01_EA_Common_74_0_0_0\EA_comn

rem The list of integration modules to build
SET MODULE_LIST=ApplyLivingArrangementRulesProc,ApplyLTSSSummaryRulesProc,ApplyMedicaidWaiverRulesProc,CopayChangesNotificationProc,CreateTLMTaskProc,EADADSListenerProc,EAMEPDNotificationProc,EATiersInquiryProc,EATiersListenerProc,EligNotificationProc,GetProviderAddressProc,LivingArrangementProc,LTSSSummaryProc,MEPDMQTestHarness,MEPDScaServiceGateway,MergeDadsTiersEligPeriodsProc,TIERSMEPDUpdateProc,UpdateCopayPaymentOverrideProc,UpdateLTSSEligibilityPeriodsProc,UpdateLTSSPaymentsProc,UpdateMedicaidWaiversProc,DadsLivingArrangementProc,DadsUpdateMedicaidWaiversProc,UpdateDADSLtssSummaryProc
rem -----------------------------------------------------------------------------------------
rem
rem The values for the following variables need not be changed.
rem
rem -----------------------------------------------------------------------------------------

rem Date in the format YYYYMMDD
SET TODAYS_DATE=%DATE:~10%%DATE:~4,2%%DATE:~7,2%%TIME:~0,2%%TIME:~3,2%%TIME:~6,2%

rem Directory on local machine where ZIPs will be created 
SET OUTPUT_DIR_ZIP=%AUTOMATION_HOME%\dailybuilds\%TODAYS_DATE%\ZIP

rem Directory on local machine where EARs will be created 
SET OUTPUT_DIR_EAR=%AUTOMATION_HOME%\dailybuilds\%TODAYS_DATE%\EAR

rem Add ANT build tools to PATH
set PATH=%PATH%;%ANT_HOME%\bin

rem Contents from ClearCase view are copied to src directory
SET SOURCE=%AUTOMATION_HOME%\dailybuilds\%TODAYS_DATE%\SRC

rem Working Directory
SET WORK_DIR=%AUTOMATION_HOME%\dailybuilds\%TODAYS_DATE%\WD
"%WPS_HOME%\bin\ws_ant.bat" -f build.xml %1
ant %* 
