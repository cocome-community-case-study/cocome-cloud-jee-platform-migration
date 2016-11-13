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
  
  You can do this by using the Eclipse git plugin. Go to 'open persepective' -> select GIT -> clone a git repository -> enter both links mentioned above into URI https://github....
  

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
- Normally the domain name has to be named after your glassfish server, host should be changed to 'localhost',
  'adminPort' should be portbase+48 (eg. portbase is 8400 ->  'adminPort' is 8448)  , 'httpPort' should be portbase+80, 
  'user' should be admin and the 'password' should be blank, if you didn't choose one while creating the glassfish domains 
  (do not delete the password line).
  
- However, in case you use one Glassfish server for multiple
  domains or you have configured you Glassfish servers with different
  admin and http ports, the you have to set these values properly.
  
- In the `service-adapter` project, proceed in a similar way (You also have to add a glassfish domain for this one). Therefore
  read the README in th cocome-cloud-jee-service-adapter project.
  
 ## Building CoCoME
 
 Don't forget to start the glassfish servers. Go to 'servers'- perspective -> Right-click -> Start.
  
 On command line you may build CoCoME as follows:
 - Enter `maven-cocome-project`
   `mvn -s settings.xml clean compile package`
   You may execute these three steps also with separate Maven calls.
 - Switch to the other repository and run Maven
   `mvn -s settings.xml clean compile package`
   
   
 If you want to use the mvn commands with the eclipse plugin create the following setup:
 - right-click on cocome-maven-project -> Run As -> Build... ->     
       goal: clean compile package  (without any symbols in between)
       profiles: leave empty
       user settings:  use the settings.xml from the actual project
     
   
The resulting packages can now been deployed by hand, via
`mvn -s settings.xml install`(in both projects), or via the deployment
script `deployment.sh`. Please read the `Deployment Setup.md`.

  
  
  
  
  




