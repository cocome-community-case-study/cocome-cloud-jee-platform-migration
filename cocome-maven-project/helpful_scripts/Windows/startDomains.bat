#!/bin/bash


REM !!!!WINDOWS ONLY!!!!!
REM !!!!!!!!!Change the SET Locations to your destination folders/files!!!!!!!!

SET glassfish_home=C:\Users\   path to glassfish         \glassfish5\bin

cd %glassfish_home%
call asadmin start-domain registry & 
call asadmin start-domain adapter & 
call asadmin start-domain web & 
call asadmin start-domain store  &  
call asadmin start-domain enterprise &
call asadmin start-domain plant 

echo All domains started!!!
pause