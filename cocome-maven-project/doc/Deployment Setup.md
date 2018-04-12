# Deployment Setup

The Cloud CoCoME implementation is divided into five different deployable
parts, whereas four are contained in 
`cocome-cloud-jee-platform-migration` and one is contained in 
`cocome-cloud-jee-service-adapter`. Furthermore, CoCoME requires a
running database service. The setup of the database is detailed in the 
[README](https://github.com/cocome-community-case-study/cocome-cloud-jee-service-adapter)
of the `cocome-cloud-jee-service-adapter` project.

We deploy CoCoME on a set of nodes running Glassfish 4.x and one node
running a Postgres database server. As Glassfish is able to handle
multiple domains with server, all deployable containers can be put on
one server each in its own domain. However in this document, we use
five different nodes with one Glassfish server.

For each part, the following parameters are relevant and are set in the settings.xml
for `cocome-maven-project` and `cocome-cloud-jee-service-adapter`:
- ip address (and hostname)
- http port (port used by the services in a deployment)
- admin port (port used by the admin console)
- domain (Glassfish domain)
- admin user name (name of the admin user)
- admin password (password of the admin user)

## Important notice
- In each case you need to start the glassfish domains before you can actually deploy 
  something on them. Therefore, start each domain in the following order: start database, 
  1. start registry,
  2. start adapter, 
  3. start the rest (web, plant, store, enterprise).
- Whenever you want to start the domains, do it in this order.


## Maven based Deployment

As the deployment is now performed with the cargo maven plug-in, the
specification of a password file is no longer an option and a local
Glassfish installation is not necessary. Instead the password field in the 
settings.xml can be left empty.

You can either deploy all parts of CoCoME in one go, or each part separately.
To deploy CoCoME as a whole, execute the following commands in the `cocome-maven-project` folder.

To deploy one of the sub-projects, execute the commands in its corresponding folder: 
 - `cloud-web-frontend` for the frontend part
 - `enterprise-logic-ear`, `store-logic-ear` for the enterprise and store logic parts 
 - `cloud-registry-service` for the registry.

### Command Line Deployment
On command line you may deploy CoCoME or one of the above sub-projects as follows:
- Enter the project folder to deploy CoCoME and run
  `mvn -s settings.xml install`
  
- To Undeploy CoCoME or a sub-project enter:
  `mvn -s settings.xml clean post-clean`
   
### Eclipse Deployment
To deploy CoCoME or one of the above sub-projects from within Eclipse, do the following:
- Right-click on cocome-maven-project -> Run As -> Build...
- Set the following values:
 - Goals: install
 - Profiles: Leave empty
 - User Settings:  Use your settings.xml from the actual project
 
To undeploy CoCoME or a sub-project:
- Right-click on cocome-maven-project -> Run As -> Build...
- Set the following values:
 - Goals: clean post-clean
 - Profiles: Leave empty
 - User Settings:  Use your settings.xml from the actual project
 
Once these settings are saved, you can just right-click on the project -> Run As -> Build
and choose the desired build configuration.

## Script based Deployment

In case of trouble (see [`Troubleshooting.md`](./Troubleshooting.md)) you may use the
`deployment.sh` script to deploy CoCoME onto various hosts. Note the
script must be adapted to your needs und defaults on the ports 4848 and
8080. Furthermore, it will not work when all containers are installed
on the same host in different domains, and it requires a password file.

