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

import org.cocome.tradingsystem.inventory.data.enterprise.ITradingEnterprise;

import java.io.Serializable;

/**
 * Represents a class of production unity utilizing a specific set of {@link ProductionUnitOperation}
 *
 * @author Rudolf Biczok
 */
public class ProductionUnitClass implements Serializable, IProductionUnitClass {

    private static final long serialVersionUID = -2577328715744776645L;

    private long id;
    private String name;
    private ITradingEnterprise enterprise;

    /**
     * @return The id.
     */
    @Override
    public long getId() {
        return id;
    }

    /**
     * @param id Identifier value.
     */
    @Override
    public void setId(long id) {
        this.id = id;
    }

    /**
     * @return The name of the product
     */
    @Override
    public String getName() {
        return name;
    }

    /**
     * @param name The name of the product
     */
    @Override
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return The enterprise which the Plant belongs to
     */
    @Override
    public ITradingEnterprise getEnterprise() {
        return this.enterprise;
    }

    /**
     * @param enterprise The enterprise which the Plant belongs to
     */
    @Override
    public void setEnterprise(final ITradingEnterprise enterprise) {
        this.enterprise = enterprise;
    }
}
