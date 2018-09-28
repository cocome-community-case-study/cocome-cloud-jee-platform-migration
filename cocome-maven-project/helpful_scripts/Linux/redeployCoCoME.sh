#!/bin/bash

#Please Change those paths!!!!!!!!!!!!!!!!!!!!!!!!!


cocome_settings="CoCOME       /settings.xml"
cocome_pom="/COCOME       /pom.xml"
glassfish_home="/GLASSFISH                  /bin/"
glassfish_domain="/GLASSFISH            /domains/"




cd $glassfish_home
./asadmin stop-domain registry ; 
./asadmin stop-domain web ; 
./asadmin stop-domain store ; 
./asadmin stop-domain enterprise ; 
./asadmin stop-domain plant ; 



cd $glassfish_domain

rm -rf enterprise/applications/* ;
rm -rf enterprise/generated/* ;
rm -rf enterprise/osgi-cache/* ;


rm -rf registry/applications/* ;
rm -rf registry/generated/* ;
rm -rf registry/osgi-cache/* ;

rm -rf store/applications/* ;
rm -rf store/generated/* ;
rm -rf store/osgi-cache/* ;


rm -rf web/applications/* ;
rm -rf web/generated/* ;
rm -rf web/osgi-cache/* ;

rm -rf plant/applications/* ;
rm -rf plant/generated/* ;
rm -rf plant/osgi-cache/* ;


cd $glassfish_home
./asadmin start-domain registry ; 
./asadmin start-domain web ; 
./asadmin start-domain store ; 
./asadmin start-domain enterprise ; 
./asadmin start-domain plant ; 

./asadmin undeploy store-logic-ear --port 8148 ;
./asadmin undeploy cloud-registry-service --port 8448 ;
./asadmin undeploy enterprise-logic-ear --port 8348 ;
./asadmin undeploy plant-logic-ear --port 8548 ;
./asadmin undeploy cloud-web-frontend --port 8048 ;

mvn -s $cocome_settings install -f $cocome_pom -DskipTests -U

echo "Redeployment successful if mvn build was successfull!!!!!!"
