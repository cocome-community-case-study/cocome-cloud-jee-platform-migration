package org.cocome.cloud.logic.webservice.store;

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
public class StoreManagerStartup {
	private static final Logger LOG = Logger.getLogger(StoreManagerStartup.class);
	
	@Inject
	String storeManagerWSDL;
	
	@Inject
	long defaultStoreIndex;
	
	@Inject
	int defaultCashDeskIndex;
	
	@Inject
	IApplicationHelper applicationHelper;
	
	@PostConstruct
	private void registerStoreManager() {
		try {
			applicationHelper.registerComponent(Names.getStoreManagerRegistryName(defaultStoreIndex), storeManagerWSDL, false);
			applicationHelper.registerComponent(Names.getCashDeskRegistryName(defaultStoreIndex, defaultCashDeskIndex), storeManagerWSDL, false);
		} catch (URISyntaxException e) {
			LOG.error("Error registering component: " + e.getMessage());
		}
	}
}
