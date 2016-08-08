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
	String cashDeskWSDL;
	
	@Inject
	String barcodeScannerWSDL;
	
	@Inject
	String cardReaderWSDL;
	
	@Inject
	String cashBoxWSDL;
	
	@Inject
	String expressLightWSDL;
	
	@Inject
	String printerWSDL;
	
	@Inject
	String userDisplayWSDL;
	
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
			String cashDeskName = Names.getCashDeskRegistryName(defaultStoreIndex, defaultCashDeskIndex);
			applicationHelper.registerComponent(cashDeskName, cashDeskWSDL, false);
			applicationHelper.registerComponent(Names.getCashDeskComponentRegistryName(cashDeskName, "barcodeScanner"), barcodeScannerWSDL, false);
			applicationHelper.registerComponent(Names.getCashDeskComponentRegistryName(cashDeskName, "cardReader"), cardReaderWSDL, false);
			applicationHelper.registerComponent(Names.getCashDeskComponentRegistryName(cashDeskName, "cashBox"), cashBoxWSDL, false);
			applicationHelper.registerComponent(Names.getCashDeskComponentRegistryName(cashDeskName, "expressLight"), expressLightWSDL, false);
			applicationHelper.registerComponent(Names.getCashDeskComponentRegistryName(cashDeskName, "printer"), printerWSDL, false);
			applicationHelper.registerComponent(Names.getCashDeskComponentRegistryName(cashDeskName, "userDisplay"), userDisplayWSDL, false);
		} catch (URISyntaxException e) {
			LOG.error("Error registering component: " + e.getMessage());
		}
	}
}
