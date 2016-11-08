# Development Setup

## Prerequisites

- Eclipse Neon
- Maven2Eclipse connector (available via Eclipse Marketplace)
- Check `Eclipse Setup.md` for further details on the setup of Eclipse
- Read/Execute `Glassfish Setup.md` and consult `Deployment Setup.md`

## Checkout Projects

- cocome-maven-project
  `git clone https://github.com/cocome-community-case-study/cocome-cloud-jee-platform-migration.git`
- service-adapter
  `git clone https://github.com/cocome-community-case-study/cocome-cloud-jee-service-adapter.git`

## Import Maven Projects

- Choose 'File' > 'Import' > 'Existing Maven Projects'
- Click 'Next'
- Click 'Browse...' and select the root directory of the 
  `cocome-cloud-jee-platform-migration` repository
- Click 'Ok' in the directory dialog
- Make sure all projects are checked in the displayed list
- Click 'Finish'

- Re-iterate this process with the root directory of the 
  `cocome-cloud-jee-service-adapter` repository.

## Configure Project

- Please consult `Glassfish Setup.md` and `Deployment Setup.md` before
  configuring the two Maven-based CoCoME projects.

- In the main project `cocome-maven-project` you may find a file called
  `settings.xml.template`
- Create a copy of this file and name it `settings.xml`
- Open `settings.xml`
- You find five groups of settings labeled `node deployment configuration`
  prefixed with `registry`, `store`, `enterprise`, `web` and
  `service adapter`
- Define the correct values for each deployment configuration. These are
  specifically `domain`, `host`, `adminPort`, `httpPort`, `protocol`,
  `user`, `password`
  Usually `protocol` is `http`, `adminPort` is `4848` and `httpPort` is
  `8080`. However, in case you use one Glassfish server for multiple
  domains or you have configured you Glassfish servers with different
  admin and http ports, the you have to set these values properly.
  
- In the `service-adapter` project, proceed in a similar way
  
 ## Building CoCoME
  
 On command line you may build CoCoME as follows:
 - Enter `maven-cocome-project`
   `mvn -s settings.xml clean compile package`
   You may execute these three steps also with separate Maven calls.
 - Switch to the other repository and run Maven
   `mvn -s settings.xml clean compile package`
   
The resulting packages can now been deployed by hand, via
`mvn -s settings.xml install`(in both projects), or via the deployment
script `deployment.sh`. Please read the `Deployment Setup.md`.

  
  
  
  
  




