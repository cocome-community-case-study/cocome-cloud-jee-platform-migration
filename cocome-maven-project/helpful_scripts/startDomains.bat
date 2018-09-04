#!/bin/bash


REM !!!!WINDOWS ONLY!!!!!
REM !!!!!!!!!Change the SET Locations to your destination folders/files!!!!!!!!

SET glassfish_home=C:\Users\Nikolaus\Desktop\Arbeit\glassfish5\bin

cd %glassfish_home%
call asadmin start-database & 
call asadmin start-domain registry & 
call asadmin start-domain adapter & 
call asadmin start-domain web & 
call asadmin start-domain store  &  
call asadmin start-domain enterprise

echo All domains started!!!
pause