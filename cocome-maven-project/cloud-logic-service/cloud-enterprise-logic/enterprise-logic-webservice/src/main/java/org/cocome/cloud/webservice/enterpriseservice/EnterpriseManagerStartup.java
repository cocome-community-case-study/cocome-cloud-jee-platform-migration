package org.cocome.cloud.webservice.enterpriseservice;

import java.net.URISyntaxException;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;

import org.apache.log4j.Logger;
import org.cocome.cloud.logic.registry.client.IApplicationHelper;
import org.cocome.cloud.registry.service.Names;

@Singleton
@Startup
public class EnterpriseManagerStartup {
	private static final Logger LOG = Logger.getLogger(EnterpriseManagerStartup.class);

	@Inject
	IApplicationHelper applicationHelper;
	
	@Inject
	String enterpriseServiceWSDL;
	
	@Inject
	String enterpriseReportingWSDL;
	
	@Inject
	String loginManagerWSDL;
	
	@Inject
	long defaultEnterpriseIndex;
	
	@PostConstruct
	private void registerEnterpriseManagerComponents() {
		try {
			// Register this enterprise server as default if none was defined before
			applicationHelper.registerComponent(Names.getEnterpriseManagerRegistryName(defaultEnterpriseIndex),
					enterpriseServiceWSDL, false);
			
			applicationHelper.registerComponent(Names.getEnterpriseReportingRegistryName(defaultEnterpriseIndex),
					enterpriseReportingWSDL, false);
			
			applicationHelper.registerComponent(Names.getLoginManagerRegistryName(defaultEnterpriseIndex),
					loginManagerWSDL, false);
		} catch (URISyntaxException e) {
			LOG.error("Error registering components: " + e.getMessage());
		}
	}
}
