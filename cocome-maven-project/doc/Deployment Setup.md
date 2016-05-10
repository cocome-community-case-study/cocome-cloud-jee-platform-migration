# Deployment Setup

The Cloud CoCoME implementation is realized five different deployable
containers, whereas four are contained in 
`cocome-cloud-jee-platform-migration` and one is contained in 
`cocome-cloud-jee-service-adapter`. Furthermore, CoCoME requires a
running database service.

We deploy CoCoME on a set of nodes running Glassfish 4.x and one node
running a Postgres database server. As Glassfish is able to handle
multiple domains with server, all deployable containers can be put on
one server each in its own domain. However in this document, we use
five different nodes with one Glassfish server.

For each containers the following parameter are relevant:
- ip address (and hostname)
- http port (port used by the services in a deployment)
- admin port (port used by the admin console)
- domain (glassfish domain)
- admin user name (name of the admin user)
- admin password (passowrd of the admin user)

## Maven based Delpoyment

As the deployment is now performed with the cargo maven plug-in, the
specification of a password file is no longer an option and a local
Glassfish installation is not necessary. Instead the password field can
be left empty.

## Script based Deployment

In case of trouble (see `Troubleshooting.md`) you may use the
`deployment.sh` script to deploy CoCoME onto various hosts. Note the
script must be adapted to your needs und defaults on the ports 4848 and
8080. Furthermore, it will not work when all containers are installed
on the same host in different domains, and it requires a password file.

