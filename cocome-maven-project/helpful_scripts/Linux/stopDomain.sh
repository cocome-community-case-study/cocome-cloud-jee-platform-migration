#!/bin/bash

# Please Change those paths!!!!!!!!!!!!!!!1

glassfish_home="/GLASSFISH                              /bin"

cd $glassfish_home
./asadmin stop-domain registry ;
./asadmin stop-domain adapter ;
./asadmin stop-domain web ;
./asadmin stop-domain store  ;  
./asadmin stop-domain enterprise ;
./asadmin stop-domain plant 

echo "All domains stopped!!!"
