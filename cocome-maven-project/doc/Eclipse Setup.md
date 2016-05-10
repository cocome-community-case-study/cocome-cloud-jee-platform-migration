# Eclipse Setup

CoCoME makes use of a wide range of Maven plug-ins including glassfish
deployment, code generation, configuration, and others. To be able to
process this information in Eclipse, several plug-ins and connectors 
must be added to your Eclipse installation.

## Prerequisites

- Eclipse Mars.1 or later (other versions my work as well)
- Eclipse JavaEE extensions installed
- Maven2Eclipse connector (available via Eclipse Marketplace)
- m2c connector for cxf 
  Update Site: https://github.com/ryansmith4/m2e-cxf-codegen-connector.update/raw/master/org.eclipselabs.m2e.cxf.codegen.connector.update-site

Please install all the necessary plug-ins and connectors.

## Checkout Projects

- cocome-maven-project
  `git clone https://github.com/cocome-community-case-study/cocome-cloud-jee-platform-migration.git`
- service-adapter
  `git https://github.com/cocome-community-case-study/cocome-cloud-jee-service-adapter.git`

## Import Projects

Use 'File' > 'Import' > 'Existing Maven Project' to import the CoCoME
maven projects.

To work with CoCoME, it might be helpful to create your own branch for
personal modifications. The remaining setup in Eclipse and other
development IDEs is documented in `Development Setup.md`
