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

package org.cocome.tradingsystem.inventory.data.plant.productionunit;

import org.apache.log4j.Logger;
import org.cocome.tradingsystem.inventory.data.enterprise.IEnterpriseQuery;
import org.cocome.tradingsystem.inventory.data.enterprise.ITradingEnterprise;
import org.cocome.tradingsystem.inventory.data.plant.IPlant;
import org.cocome.tradingsystem.util.exception.NotInDatabaseException;

import javax.annotation.PostConstruct;
import javax.enterprise.context.Dependent;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import java.io.Serializable;

/**
 * Represents a class of production unity utilizing a specific set of {@link ProductionUnitOperation}
 *
 * @author Rudolf Biczok
 */
@Dependent
public class ProductionUnitClass implements Serializable, IProductionUnitClass {

    private static final long serialVersionUID = -2577328715744776645L;

    private static final Logger LOG = Logger.getLogger(ProductionUnitClass.class);

    private long id;
    private long plantId;
    private String name;
    private IPlant plant;

    @Inject
    private Instance<IEnterpriseQuery> enterpriseQueryInstance;

    private IEnterpriseQuery enterpriseQuery;

    @PostConstruct
    public void init() {
        enterpriseQuery = enterpriseQueryInstance.get();
        plant = null;
    }

    @Override
    public long getId() {
        return id;
    }

    @Override
    public void setId(long id) {
        this.id = id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public IPlant getPlant() throws NotInDatabaseException {
        if (plant == null) {
            plant = enterpriseQuery.queryPlant(plantId);
            LOG.debug(String.format(
                    "Retrieved plant [%d, %s] for production unit class %s",
                    plant.getId(),
                    plant.getName(),
                    name));
        }
        return plant;
    }

    @Override
    public void setPlant(final IPlant plant) {
        this.plant = plant;
    }

    @Override
    public long getPlantId() {
        return plantId;
    }

    @Override
    public void setPlantId(long plantId) {
        this.plantId = plantId;
    }
}
