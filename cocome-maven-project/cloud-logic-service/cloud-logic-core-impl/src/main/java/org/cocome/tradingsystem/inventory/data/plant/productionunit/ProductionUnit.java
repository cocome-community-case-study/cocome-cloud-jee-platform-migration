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

import org.cocome.tradingsystem.inventory.data.enterprise.IEnterpriseQuery;
import org.cocome.tradingsystem.inventory.data.plant.IPlant;
import org.cocome.tradingsystem.inventory.data.plant.IPlantQuery;
import org.cocome.tradingsystem.util.exception.NotInDatabaseException;

import javax.annotation.PostConstruct;
import javax.enterprise.context.Dependent;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import java.io.Serializable;

/**
 * This class represents a Product in the database
 *
 * @author Rudolf Biczok
 */
@Dependent
public class ProductionUnit implements Serializable, IProductionUnit {

    private static final long serialVersionUID = -2577328715744776645L;

    private long id;
    private String location;
    private String interfaceUrl;
    private boolean doubleFlag;
    private IPlant plant;
    private IProductionUnitClass productionUnitClass;

    private long plantId;
    private long productionUnitClassId;

    @Inject
    private Instance<IEnterpriseQuery> enterpriseQueryInstance;

    private IEnterpriseQuery enterpriseQuery;

    @Inject
    private Instance<IPlantQuery> plantQueryInstance;

    private IPlantQuery plantQuery;

    @PostConstruct
    public void init() {
        enterpriseQuery = enterpriseQueryInstance.get();
        plantQuery = plantQueryInstance.get();
        plant = null;
        productionUnitClass = null;
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
    public String getLocation() {
        return this.location;
    }

    @Override
    public void setLocation(final String location) {
        this.location = location;
    }

    @Override
    public String getInterfaceUrl() {
        return this.interfaceUrl;
    }

    @Override
    public void setInterfaceUrl(final String interfaceUrl) {
        this.interfaceUrl = interfaceUrl;
    }

    @Override
    public boolean isDouble() {
        return doubleFlag;
    }

    @Override
    public void setDouble(boolean doubleFlag) {
        this.doubleFlag = doubleFlag;
    }

    @Override
    public IProductionUnitClass getProductionUnitClass() throws NotInDatabaseException {
        if (productionUnitClass == null) {
            productionUnitClass = plantQuery.queryProductionUnitClass(productionUnitClassId);
        }
        return productionUnitClass;
    }

    @Override
    public void setProductionUnitClass(IProductionUnitClass productionUnitClass) {
        this.productionUnitClass = productionUnitClass;
    }

    @Override
    public long getProductionUnitClassId() {
        return productionUnitClassId;
    }

    @Override
    public void setProductionUnitClassId(long productionUnitClassId) {
        this.productionUnitClassId = productionUnitClassId;
    }

    @Override
    public IPlant getPlant() throws NotInDatabaseException {
        if (plant == null) {
            plant = enterpriseQuery.queryPlant(plantId);
        }
        return plant;
    }

    @Override
    public void setPlant(IPlant plant) {
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
