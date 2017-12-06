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

import org.cocome.tradingsystem.inventory.data.plant.IPlantQuery;
import org.cocome.tradingsystem.inventory.data.plant.expression.IEvaluationContext;
import org.cocome.tradingsystem.inventory.data.plant.expression.IPUInstruction;
import org.cocome.tradingsystem.inventory.data.plant.expression.PUInstruction;
import org.cocome.tradingsystem.util.exception.NotInDatabaseException;

import javax.annotation.PostConstruct;
import javax.enterprise.context.Dependent;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import java.io.Serializable;
import java.util.Collections;
import java.util.List;

/**
 * Represents an atomic operation on a production unit
 *
 * @author Rudolf Biczok
 */
@Dependent
public class ProductionUnitOperation implements Serializable, IProductionUnitOperation {

    private static final long serialVersionUID = 1L;

    private long id;
    private String name;
    private String operationId;
    private long expectedExecutionTime;
    private long productionUnitClassId;
    private IProductionUnitClass productionUnitClass;

    @Inject
    private Instance<IPlantQuery> plantQueryInstance;

    private IPlantQuery plantQuery;

    @PostConstruct
    public void init() {
        plantQuery = plantQueryInstance.get();
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
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getOperationId() {
        return operationId;
    }

    @Override
    public void setOperationId(String operationId) {
        this.operationId = operationId;
    }

    @Override
    public long setExecutionDurationInMillis() {
        return expectedExecutionTime;
    }

    @Override
    public void setExecutionDurationInMillis(long expectedExecutionTime) {
        this.expectedExecutionTime = expectedExecutionTime;
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
    public List<IPUInstruction> evaluate(final IEvaluationContext context) throws NotInDatabaseException {
        return Collections.singletonList(new PUInstruction(this.getProductionUnitClass(), this.operationId));
    }
}
