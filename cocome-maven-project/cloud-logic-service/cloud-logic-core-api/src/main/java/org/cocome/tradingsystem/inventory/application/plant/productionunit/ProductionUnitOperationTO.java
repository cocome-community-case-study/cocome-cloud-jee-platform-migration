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

package org.cocome.tradingsystem.inventory.application.plant.productionunit;

import org.cocome.tradingsystem.inventory.application.IIdentifiableTO;

import javax.xml.bind.annotation.*;

/**
 * Represents an atomic operation on a production unit
 *
 * @author Rudolf Biczok
 */
@XmlType(
        name = "ProductionUnitOperationTO",
        namespace = "http://productionunit.plant.application.inventory.tradingsystem.cocome.org")
@XmlRootElement(name = "ProductionUnitOperationTO")
@XmlAccessorType(XmlAccessType.FIELD)
public class ProductionUnitOperationTO implements IIdentifiableTO {

    private static final long serialVersionUID = 1L;

    @XmlElement(name = "enterprise", required = true)
    private long id;
    @XmlElement(name = "enterprise", required = true)
    private String operationId;
    @XmlElement(name = "enterprise", required = true)
    private ProductionUnitClassTO productionUnitClass;

    /**
     * Gets identifier value
     *
     * @return The identifier value.
     */
    @Override
    public long getId() {
        return id;
    }

    /**
     * Sets identifier.
     *
     * @param id Identifier value.
     */
    @Override
    public void setId(long id) {
        this.id = id;
    }

    /**
     * @return The operation id unique to the production plant
     */
    public String getOperationId() {
        return operationId;
    }

    /**
     * @param operationId The operation id unique to the production plant
     */
    public void setOperationId(String operationId) {
        this.operationId = operationId;
    }

    /**
     * @return the associated {@link ProductionUnitClassTO}
     */
    public ProductionUnitClassTO getProductionUnitClass() {
        return productionUnitClass;
    }

    /**
     * @param productionUnitClass the associated production unit class
     */
    public void setProductionUnitClass(ProductionUnitClassTO productionUnitClass) {
        this.productionUnitClass = productionUnitClass;
    }
}
