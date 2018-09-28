#!/bin/bash


REM !!!!WINDOWS ONLY!!!!!
REM !!!!!!!!!Change the SET Locations to your destination folders/files!!!!!!!!

SET glassfish_home=C:\Users\path to glassfish             \glassfish5\bin

cd %glassfish_home%


call asadmin stop-domain web & 
call asadmin stop-domain enterprise & 
call asadmin stop-domain registry & 
call asadmin stop-domain store & 
call asadmin stop-domain adapter &
call asadmin stop-domain plant

echo All domains stopped!
pause