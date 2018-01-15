package org.cocome.cloud.logic.webservice.storeservice;

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
    private String storeManagerWSDL;

    @Inject
    private String cashDeskWSDL;

    @Inject
    private String barcodeScannerWSDL;

    @Inject
    private String cardReaderWSDL;

    @Inject
    private String cashBoxWSDL;

    @Inject
    private String configuratorWSDL;

    @Inject
    private String expressLightWSDL;

    @Inject
    private String printerWSDL;

    @Inject
    private String userDisplayWSDL;

    @Inject
    private long defaultStoreIndex;

    @Inject
    private int defaultCashDeskIndex;

    @Inject
    private IApplicationHelper applicationHelper;

    @PostConstruct
    private void registerStoreManager() {
        try {
            applicationHelper.registerComponent(Names.getStoreManagerRegistryName(defaultStoreIndex), storeManagerWSDL, false);
            String cashDeskName = Names.getCashDeskRegistryName(defaultStoreIndex, defaultCashDeskIndex);
            applicationHelper.registerComponent(cashDeskName, cashDeskWSDL, false);
            applicationHelper.registerComponent(Names.getCashDeskComponentRegistryName(cashDeskName, "barcodeScanner"), barcodeScannerWSDL, false);
            applicationHelper.registerComponent(Names.getCashDeskComponentRegistryName(cashDeskName, "cardReader"), cardReaderWSDL, false);
            applicationHelper.registerComponent(Names.getCashDeskComponentRegistryName(cashDeskName, "cashBox"), cashBoxWSDL, false);
            applicationHelper.registerComponent(Names.getCashDeskComponentRegistryName(cashDeskName, "configurator"), configuratorWSDL, false);
            applicationHelper.registerComponent(Names.getCashDeskComponentRegistryName(cashDeskName, "expressLight"), expressLightWSDL, false);
            applicationHelper.registerComponent(Names.getCashDeskComponentRegistryName(cashDeskName, "printer"), printerWSDL, false);
            applicationHelper.registerComponent(Names.getCashDeskComponentRegistryName(cashDeskName, "userDisplay"), userDisplayWSDL, false);
        } catch (URISyntaxException e) {
            LOG.error("Error registering component: " + e.getMessage());
        }
    }
}
