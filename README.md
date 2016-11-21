# CoCoME Cloud JEE Platform

This is the cloud variant of CoCoME implementing the evolution scenario
platform migration.

CoCoME uses Maven as built system. It has, therefore, a specific
directory layout and subdivision in subprojects. In the following we
describe the overall structure and point to specific information to
setup the build system, configure the deployment, and trigger deployment
and undeployment.

## Directory Structure

- [`cocome-maven-project`](./cocome-maven-project) project root

- [`cocome-maven-project/cloud-logic-service`](./cocome-maven-project/cloud-logic-service) root of the logic services sub-project

- [`cocome-maven-project/cloud-logic-service/cloud-enterprise-logic`](./cocome-maven-project/cloud-logic-service/cloud-enterprise-logic) enterprise node

- [`cocome-maven-project/cloud-logic-service/cloud-logic-core-api`](./cocome-maven-project/cloud-logic-service/cloud-logic-core-api)
- [`cocome-maven-project/cloud-logic-service/cloud-logic-core-impl`](./cocome-maven-project/cloud-logic-service/cloud-logic-core-impl)
- [`cocome-maven-project/cloud-logic-service/cloud-logic-core-services`](./cocome-maven-project/cloud-logic-service/cloud-logic-core-services)
- [`cocome-maven-project/cloud-logic-service/cloud-registry-client`](./cocome-maven-project/cloud-logic-service/cloud-registry-client)

- [`cocome-maven-project/cloud-logic-service/cloud-registry-service`](./cocome-maven-project/cloud-logic-service/cloud-registry-service)
	registry service

- [`cocome-maven-project/cloud-logic-service/cloud-store-logic`](./cocome-maven-project/cloud-logic-service/cloud-store-logic)
	store node

- [`cocome-maven-project/cloud-logic-service/java-utils`](./cocome-maven-project/cloud-logic-service/java-utils) utility java
	code (this might be moved to a separate project in the future)

- [`cocome-maven-project/cloud-web-frontend`](./cocome-maven-project/cloud-web-frontend) root of the web frontend sub-project

- [`cocome-maven-project/doc`](./cocome-maven-project/doc) documentation

## Important Information
- [`Installation in a Nutshell`](./cocome-maven-project/doc/Installation in a nutshell.md) gives an overview of the installation process
- [`Development Setup`](./cocome-maven-project/doc/Development Setup.md) describes how to
	configure a fresh checkout
- [`Deployment Setup`](./cocome-maven-project/doc/Deployment Setup.md) explains the general
	setup of the CoCoME execution environment and how to deploy
	and undeploy components
- [`Eclipse Setup`](`cocome-maven-project/doc/Eclipse Setup.txt`) contains an collection of
	hints and advices to configure Eclipse to work with CoCoME

