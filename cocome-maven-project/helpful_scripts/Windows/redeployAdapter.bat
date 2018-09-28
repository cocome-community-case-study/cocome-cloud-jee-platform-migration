#!/bin/bash


REM !!!!WINDOWS ONLY!!!!!
REM !!!!!!!!!Change the SET Locations to your destination folders/files!!!!!!!!
SET cocome_adapter_settings=C:\Users\    path to settings   \settings.xml
SET cocome_adapter_pom=C:\Users\    path to pom         \pom.xml
SET glassfish_home=C:\Users\  path to glassfish         glassfish5\bin
SET glassfish_domain=C:\Users\path to glassfish domains              \glassfish\domains

call mvn -s %cocome_adapter_settings% clean post-clean -f %cocome_adapter_pom% &

cd %glassfish_home%

call asadmin stop-domain adapter & 


cd %glassfish_domain%



del /S /Q .\adapter\applications\* &
FOR /D %%p IN (".\adapter\applications\*.*") DO rmdir "%%p" /s /q &
del /S /Q .\adapter\generated\* &
FOR /D %%p IN (".\adapter\generated\*.*") DO rmdir "%%p" /s /q &
del /S /Q .\adapter\osgi-cache\* &
FOR /D %%p IN (".\adapter\osgi-cache\*.*") DO rmdir "%%p" /s /q &

cd %glassfish_home%
 
call asadmin start-domain adapter & 


call asadmin undeploy service-adapter-ear --port 8248 &


call mvn -s %cocome_adapter_settings% install -f %cocome_adapter_pom% -DskipTests

echo Redeployment successful if mvn build was successfull!!!!!!
pause
