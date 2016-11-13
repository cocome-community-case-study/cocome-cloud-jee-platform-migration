# Eclipse Setup

CoCoME makes use of a wide range of Maven plug-ins including glassfish
deployment, code generation, configuration, and others. To be able to
process this information in Eclipse, several plug-ins and connectors 
must be added to your Eclipse installation.

## Prerequisites

- Eclipse Mars.1 or later (other versions my work as well)
- Eclipse JavaEE extensions installed ( http://www.eclipse.org/downloads/packages/eclipse-ide-java-ee-developers/neon1a )
- Maven2Eclipse connector (available via Eclipse Marketplace)
- EGIT - Git Integration for Eclipse

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


