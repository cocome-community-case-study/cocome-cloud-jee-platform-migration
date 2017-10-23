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

import org.cocome.tradingsystem.inventory.application.INameableTO;
import org.cocome.tradingsystem.inventory.application.plant.expression.ExpressionTO;

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
public class ProductionUnitOperationTO extends ExpressionTO implements INameableTO {

    private static final long serialVersionUID = 1L;

    @XmlElement(name = "name", required = true)
    private String name;
    @XmlElement(name = "operationId", required = true)
    private String operationId;
    @XmlElement(name = "executionDurationInMillis", required = true)
    private long executionDurationInMillis;
    @XmlElement(name = "productionUnitClass", required = true)
    private ProductionUnitClassTO productionUnitClass;

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
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
     * @return the expected time needed for this operation to finish (in milliseconds)
     */
    public long getExecutionDurationInMillis() {
        return executionDurationInMillis;
    }

    /**
     * @param executionDurationInMillis the expected time needed for this operation to finish (in milliseconds)
     */
    public void setExecutionDurationInMillis(long executionDurationInMillis) {
        this.executionDurationInMillis = executionDurationInMillis;
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
