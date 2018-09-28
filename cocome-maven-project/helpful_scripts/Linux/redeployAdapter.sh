#!/bin/bash

#Change those paths!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!

cocome_adapter_settings="/ADAPTER    /settings.xml"
cocome_adapter_pom="/ADAPTER                 /pom.xml"
glassfish_home="Glassfish            /bin"
glassfish_domain="/Glassfish                  /glassfish/domains"

cd $glassfish_home

./asadmin start-domain adapter ; 


mvn -s $cocome_adapter_settings clean post-clean -f $cocome_adapter_pom ;

cd $glassfish_home

./asadmin stop-domain adapter ; 


cd $glassfish_domain



rm -rf adapter/applications/* ;

rm -rf adapter/generated/* ;

rm -rf adapter/osgi-cache/* ;

cd $glassfish_home
 
./asadmin start-domain adapter ; 


./asadmin undeploy service-adapter-ear --port 8248 ;


mvn -s $cocome_adapter_settings install -f $cocome_adapter_pom -DskipTests

echo "Redeployment successful if mvn build was successfull!!!!!!"

