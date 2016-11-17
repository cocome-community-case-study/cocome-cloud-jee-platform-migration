# Eclipse Setup

CoCoME makes use of a wide range of Maven plug-ins including Glassfish
deployment, code generation, configuration, and others. To be able to
process this information in Eclipse, several plug-ins and connectors 
must be added to your Eclipse installation.

## Installation Details
- Eclipse Mars.1 or later for Java EE Developers (http://www.eclipse.org/downloads/packages/)
- Maven2Eclipse connector for EGIT (available via Eclipse Marketplace)
- EGIT - Git Integration for Eclipse (available via Eclipse Marketplace)
- JBoss Tools - Collection of tools to help with Java EE development (optional but recommended, available via Eclipse Marketplace)
- Glassfish Tools - Glassfish Server Integration for Eclipse (optional, available via Eclipse Marketplace)

## Additional M2E CXF Connector

To add CXF support for m2e install the following connector. This is not strictly necessary, 
but without it, Eclipse will show an error for the `cloud-logic-core-services` project.
However, this error is purely cosmetic and does not influence the Maven build.

- Choose 'Help' > 'Install New Software ...'
- Click on 'Add...' to add a new update site
- Name the new update site `cxf m2e connector`
- Enter `https://github.com/ryansmith4/m2e-cxf-codegen-connector.update/raw/master/org.eclipselabs.m2e.cxf.codegen.connector.update-site`
  as URL
- Select all of `m2e-cxf-codegen-connector`
- Click 'Finish'
- Follow the remaining process to install this component


