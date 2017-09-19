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

import org.cocome.tradingsystem.inventory.data.plant.IPlant;

import javax.enterprise.context.Dependent;
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
    private IPlant plant;
    private IProductionUnitClass productionUnitClass;

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
    public IProductionUnitClass getProductionUnitClass() {
        return productionUnitClass;
    }

    @Override
    public void setProductionUnitClass(IProductionUnitClass productionUnitClass) {
        this.productionUnitClass = productionUnitClass;
    }

    @Override
    public IPlant getPlant() {
        return plant;
    }

    @Override
    public void setPlant(IPlant plant) {
        this.plant = plant;
    }
}
