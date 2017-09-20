/*
 *************************************************************************
 * Copyright 2013 DFG SPP 1593 (http://dfg-spp1593.de)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *************************************************************************
 */

package org.cocome.cloud.webservice.enterpriseservice;

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
public class EnterpriseManagerStartup {
    private static final Logger LOG = Logger.getLogger(EnterpriseManagerStartup.class);

    @Inject
    private IApplicationHelper applicationHelper;

    @Inject
    private String enterpriseServiceWSDL;

    @Inject
    private String enterpriseReportingWSDL;

    @Inject
    private String loginManagerWSDL;

    @Inject
    private long defaultEnterpriseIndex;

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
