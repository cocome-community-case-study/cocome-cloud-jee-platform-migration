# Troublshooting you Setup

We encountered different errors, mishaps, and hickups of Glassfish,
Eclipse, and the deployment process. In many cases it is easy to fix
these issues or work around them. Here is a (growing) list of hints
how to solve typical issues:

'Errors with the Target Runtime'

There may be errors regarding the target runtime. They may be resolved
by installing at least the Glassfish Tools from the Oracle Enterprise
Pack for Eclipse (OEPE) from the Eclipse Marketplace. Then add a new
Glassfish server in the Servers view with the Server root directory of
your existing Glassfish installation. After this, change the Targeted
Runtimes of the ear, ejb, webservice and java-utils projects under
Properties -> Targeted Runtimes to the newly created glassfish server.

'Java Errors'

Add the JDK 7 path to your PATH environment variable if you have
problems with running Glassfish through the maven install command.

'Buid Problems'

Disable the Build automatically option in Eclipse if you have trouble 
deploying the `cloud-web-frontend` project or change the output folders
for the Eclipse build to something other than /target. Eclipse auto
build may interfere with the maven build. This may also happen when you
have imported your project not as a Maven project.

'Deployment fails in an erratic way'

We added a script called `deployment.sh` which can be used in Linux and
other environments which support `bash`. It allows to deploy, undeploy,
and check the status of your deployment when this does not work from the
maven build. Therefore, you have to prepare the build as follows:

`mvn -s settings.xml clean compile package`

This must be done for both projects (see [`Development Setup.md`](./Development Setup.md)). 
Furthermore, you have to setup the script to work in your environment.
Please refer to the in script documentation.

In case the deployment still fails multiple times for the same
container, we advice to perform the following maintenance tasks on 
Glassfish (servers need to be running):

1. Try to undeploy the broken component (if it was partially deployed).
2. Therefore, login to the host providing the Glassfish service and undeploy the .jar,.ear...
3. Stop the services, e.g., `asadmin stop-domains` 
4. Go to the `{$GLASSFISH}/glassfish/domains` directory
5. Go to the domain directory for the domain which does not work
6. Go to the applications directory and delete the CONTENT of applications, osgi-cache, generated.
7. Restart the Glassfish service (restart all if you deploy whole CoCoME and not just one service)
8. Execute mvn clean post-clean (It will throw an error message, which can be ignored)
9. Deploy the whole CoCoME (you can try to deploy just the service that caused the problem). Therefore,
   (see [`Deployment Setup.md`](./Deployment Setup.md))

'Errors after a restart of CoCoME'
Sometimes, when you restart CoCoME (after a system shutdown etc) it does not work again. For example, 
Enterprises you once created don't show up. You only get an error message.
Solution: Did you start the database? Did you start the domains in the correct order? (They depend 
on each other). 
This is the right order: start database, start registry, start adapter, start the rest (web, store, enterprise)
