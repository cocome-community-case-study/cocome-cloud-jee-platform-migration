# Development Setup

## Prerequisites

- Eclipse Mars
- Maven2Eclipse connector (available via Eclipse Marketplace)
- Clone the `cocome-cloud-jee-platform-migration` from the github
  repository
- Read the deployment-setup.txt

## Configuration of Eclipse

Install of additional m2e components.

- Choose 'Help' > 'Install New Software ...'
- Click on 'Add...' to add a new update site
- Name the new update site `cxf m2e connector`
- Enter `https://github.com/ryansmith4/m2e-cxf-codegen-connector.update/raw/master/org.eclipselabs.m2e.cxf.codegen.connector.update-site`
  as URL
- Select all of `m2e-cxf-codegen-connector`
- Click 'Finish'
- Follow the remaining process to install this component

## Import Maven Project

- Choose 'File' > 'Import' > 'Existing Maven Projects'
- Click 'Next'
- Click 'Browse...' and select the root directory of the clone repository
- Click 'Ok' in the directory dialog
- Make sure all projects are checked in the displayed list
  Note: if you have imported service-adapter before, the `java.utils`
  will not be selectable, this is correct, as this sub project is contained
  in both projects.
- Click 'Finish'

## Configure Project

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




