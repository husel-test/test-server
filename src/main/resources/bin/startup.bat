@echo off


if "%OS%" == "Windows_NT" setlocal

rem FUNSERVER_HOME if not defined
cd ..\
set FUNSERVER_HOME = %CURRENT_DIR%
cd bin
set CURRENT_DIR=%cd%
if not "%FUNSERVER_HOME%" == "" goto gotHome
set FUNSERVER_HOME=%CURRENT_DIR%
if exist "%FUNSERVER_HOME%\bin\catalina.bat" goto okHome
cd ..
set FUNSERVER_HOME=%cd%
cd %CURRENT_DIR%
:gotHome
if exist "%FUNSERVER_HOME%\bin\startup.bat" goto okHome
echo The FUNSERVER_HOME environment variable is not defined correctly
echo This environment variable is needed to run this program
goto end
:okHome

rem Get standard Java environment variables
if exist "%FUNSERVER_HOME%\bin\setclasspath.bat" goto okSetclasspath
echo Cannot find %FUNSERVER_HOME%\bin\setclasspath.bat
echo This file is needed to run this program
goto end
:okSetclasspath
set BASEDIR=%FUNSERVER_HOME%
call "%FUNSERVER_HOME%\bin\setclasspath.bat" %1
if errorlevel 1 goto end

rem set CLASSPATH=%CLASSPATH%;%FUNSERVER_HOME%\bin\startup.jar
set CLASSPATH=%CLASSPATH%;.
set _EXECJAVA=%_RUNJAVA%
set MAINCLASS=org/foscam/container/startup/Startup

rem ----- Execute The Requested Command ---------------------------------------

echo Using FUNSERVER_HOME:   %FUNSERVER_HOME%
echo Using JRE_HOME:        %JRE_HOME%
echo Using _EXECJAVA:        %_EXECJAVA%

set JAVA_OPTS=-server -Dlogback.configurationFile=%FUNSERVER_HOME%/conf/logback.xml -Xloggc:%FUNSERVER_HOME%/log/gc.log -Xms512m -Xmx1024m -XX:PermSize=32M -XX:MaxPermSize=256M -Xmn372m -Xss512k -XX:+UseConcMarkSweepGC -XX:+UseParNewGC -XX:ParallelGCThreads=20 -XX:+CMSClassUnloadingEnabled -XX:+UseCMSCompactAtFullCollection -XX:CMSFullGCsBeforeCompaction=5 -XX:+UseFastAccessorMethods -XX:SurvivorRatio=8 -XX:MaxTenuringThreshold=20 -XX:GCTimeRatio=19 -Xnoclassgc -XX:+DisableExplicitGC -XX:-CMSParallelRemarkEnabled -XX:CMSInitiatingOccupancyFraction=70 -XX:SoftRefLRUPolicyMSPerMB=0 -verbose:gc -XX:+PrintGCDetails -XX:+PrintGCTimeStamps -XX:+PrintHeapAtGC -XX:+PrintGCApplicationStoppedTime -XX:+HeapDumpOnOutOfMemoryError
set SERVER_COMMAND=start

%_EXECJAVA% %JAVA_OPTS% -classpath "%CLASSPATH%" -Dfunserver.home="%FUNSERVER_HOME%" %MAINCLASS% %SERVER_COMMAND%
goto end
:end
