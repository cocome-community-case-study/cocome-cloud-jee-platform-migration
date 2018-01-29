/*
 ***************************************************************************
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
 **************************************************************************
 */

package org.cocome.tradingsystem.inventory.data.plant.recipe;

import javax.enterprise.context.Dependent;
import java.io.Serializable;
import java.util.Collection;

/**
 * Represents a single {@link IPlantOperationOrder} entry in the database.
 *
 * @author Rudolf Biczok
 */
@Dependent
public class ProductionOrderEntry implements Serializable, IProductionOrderEntry {

    private static final long serialVersionUID = -7683436740437770058L;

    private long id;
    private long amount;
    private IRecipe recipe;
    private long recipeId;
    private Collection<IParameterValue> parameterValues;
    private IProductionOrder order;
    private long orderId;
    private boolean finished;

    @Override
    public long getId() {
        return this.id;
    }

    @Override
    public void setId(final long id) {
        this.id = id;
    }

    @Override
    public long getAmount() {
        return this.amount;
    }

    @Override
    public void setAmount(final long amount) {
        this.amount = amount;
    }

    @Override
    public Collection<IParameterValue> getParameterValues() {
        return parameterValues;
    }

    @Override
    public void setParameterValues(Collection<IParameterValue> parameterValues) {
        this.parameterValues = parameterValues;
    }

    @Override
    public long getOperationId() {
        return recipeId;
    }

    @Override
    public void setOperationId(long operationId) {
        this.recipeId = operationId;
    }

    @Override
    public long getOrderId() {
        return orderId;
    }

    @Override
    public void setOrderId(long orderId) {
        this.orderId = orderId;
    }

    @Override
    public IRecipe getOperation() {
        return recipe;
    }

    @Override
    public void setOperation(IRecipe operation) {
        this.recipe = operation;
    }

    @Override
    public IProductionOrder getOrder() {
        return order;
    }

    @Override
    public void setOrder(IProductionOrder order) {
        this.order = order;
    }

    @Override
    public boolean isFinished() {
        return finished;
    }

    @Override
    public void setFinished(boolean finished) {
        this.finished = finished;
    }

}
