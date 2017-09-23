package org.cocome.cloud.logic.webservice.plantservice;

import org.apache.log4j.Logger;
import org.cocome.cloud.logic.registry.client.IApplicationHelper;
import org.cocome.cloud.registry.service.Names;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;
import java.net.URISyntaxException;

@Singleton
@Startup
public class PlantManagerStartup {
    private static final Logger LOG = Logger.getLogger(PlantManagerStartup.class);

    @Inject
    private String plantManagerWSDL;

    @Inject
    private long defaultPlantIndex;

    @Inject
    private IApplicationHelper applicationHelper;

    @PostConstruct
    private void registerPlantManager() {
        try {
            applicationHelper.registerComponent(
                    Names.getPlantManagerRegistryName(defaultPlantIndex), plantManagerWSDL, false);
        } catch (URISyntaxException e) {
            LOG.error("Error registering component: " + e.getMessage());
        }
    }
}
