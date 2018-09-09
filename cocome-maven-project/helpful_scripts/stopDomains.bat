#!/bin/bash


REM !!!!WINDOWS ONLY!!!!!
REM !!!!!!!!!Change the SET Locations to your destination folders/files!!!!!!!!

SET glassfish_home=C:\Users\Nikolaus\Desktop\Arbeit\glassfish5\bin

cd %glassfish_home%

call asadmin stop-database & 
call asadmin stop-domain web & 
call asadmin stop-domain enterprise & 
call asadmin stop-domain registry & 
call asadmin stop-domain store & 
call asadmin stop-domain adapter 

echo All domains stopped!
pause