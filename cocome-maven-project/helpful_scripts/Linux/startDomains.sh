#!/bin/bash

# Please Change those paths!!!!!!!!!!!!!!!1

glassfish_home="/GLASSFISH                              /bin"

cd $glassfish_home
./asadmin start-domain registry ;
./asadmin start-domain adapter ;
./asadmin start-domain web ;
./asadmin start-domain store  ;  
./asadmin start-domain enterprise ;
./asadmin start-domain plant 

echo "All domains started!!!"
