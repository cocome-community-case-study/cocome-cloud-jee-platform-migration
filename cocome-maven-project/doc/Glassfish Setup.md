# Glassfish Setup

The project and the documentation are based on Glassfish 4.x as platform
service. However, you may use other JavaEE platform providers.

## Prerequisits

- Eclipse with Java EE support (installed)
- Glassfish 4.0 or higher (can be obtained from https://glassfish.java.net/)

Quick Facts:
- ${GLASSFISH} path to the Glassfish installation
- ${HOME}      home folder of your user account

## Install Glassfish

Depending on your operating system and personal choice you may have
used an installer or an archive to install Glassfish. Please follow
the instructions related to your choice.

This will usually create a domain named 'domain1'. We assume that you
installed Glassfish in a directory represented by the variable
${GLASSFISH}, which we use in the remaineder of this document to refer 
to the installation directory.

Note: in Unix/Linux path names use / and in Windows \. This may require
escaping in certain places, i.e., \\ for a \.

Glassfish is usually installed with the following layout:
`${GLASSFISH}/bin
${GLASSFISH}/glassfish
${GLASSFISH}/javadb
${GLASSFISH}/mq
${GLASSFISH}/pkg
${GLASSFISH}/README.txt`

## Configure Glassfish Instances

We must create four Glassfish instances for all parts of CoCoME. You may
create even one more. You can realize these instances either by
definining mutliple domains in one Glassfish installation or by creating
four or more separate Glassfish installations on different virtual or
physical machines.

In detail these four or five instances are named:
- web
- store
- adapter
- enterprise
- registry (this is optional and can be installed along side the
  store component)

In case you choose separate Glassfish installations you still may name
the individual domains as listed above or call them simply `cocome`.
Either way you have to manage domains. Therefore, we provide a quick
introduction for domain management.

Each domain requires three ports for administration, http, and https.
The defaults are 4848, 8080, 8181. Howver, if you choose to install
multiple domains on one Glassfish installation, you have to pick
suitable ports for all domains. As Glassfish may require additional
ports for JMX, JMS etc. we recommend to set `--portbase` instead of
`--adminport` and `--instanceport`. In this case the ports are
calculated based on the portbase. The admin port is then portbase + 48,
instance port is portbase + 80, https port is portbase + 81.

See also: https://docs.oracle.com/cd/E19798-01/821-1758/6nmnj7pt9/index.html

Note: You may not be able to select port numbers below 32000, as this
can be prohibited by firewalls. 

### Creating Domains with `asadmin`

- Change to ${GLASSFISH}/glassfish 
- Execute bin/asadmin as follows. The tool will prompt for a password.
  You may leave that empty.

`bin/asadmin create-domain --portbase 8000 web`
`bin/asadmin create-domain --portbase 8100 store`
`bin/asadmin create-domain --portbase 8200 adapter`
`bin/asadmin create-domain --portbase 8300 enterprise`
`bin/asadmin create-domain --portbase 8400 registry` (registry is optional)

Note: It is best to start each domain before creating the next when
using this method. Otherwise both domains may end up using the same
ports, for example for the jmx service, which will then cause a conflict
if both domains are started on the same host.

### Create domain using Eclipse

If you installed the Glassfish Tools from the Oracle Enterprise Pack 
for Eclipse you can create a new domain directly from within Eclipse. 
To do this, open the Servers view in Eclipse and add a new server with 
'Right-Click' -> 'New' -> 'Server''. In the wizard, select Glassfish in
the version you use and enter cocome-logic or cocome-web as the server
name.

In the next step of this wizard click on the "+" signs to the right of 
the domain path field. This opens a new dialogue. In the name field 
insert one of the domain names mentioned above. The domain directory
should already point to ${GLASSFISH}/domains and should not be modified.
The portbase is used to compute the ports for this domain.
Portbase + 80 is the port for accessing the application and portbase 
+ 48 is the port where the admin console will be available.
For this method we suggest the following portbases:

- web 8000
- store 8100
- adapter 8200
- enterprise 8300
- registry 8400 (registry is optional)

