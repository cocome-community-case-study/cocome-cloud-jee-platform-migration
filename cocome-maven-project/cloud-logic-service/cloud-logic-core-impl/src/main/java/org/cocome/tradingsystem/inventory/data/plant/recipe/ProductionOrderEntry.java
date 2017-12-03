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

import org.cocome.tradingsystem.inventory.data.enterprise.parameter.ICustomProductParameter;
import org.cocome.tradingsystem.inventory.data.enterprise.parameter.ICustomProductParameterValue;
import org.cocome.tradingsystem.util.exception.NotInDatabaseException;

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
    private Collection<ICustomProductParameterValue> parameterValues;

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
    public Collection<ICustomProductParameter> getParameters() throws NotInDatabaseException {
        return this.getRecipe().getCustomProduct().getParameters();
    }

    @Override
    public Collection<ICustomProductParameterValue> getParameterValues() {
        return parameterValues;
    }

    @Override
    public void setParameterValues(Collection<ICustomProductParameterValue> parameterValues) {
        this.parameterValues = parameterValues;
    }

    @Override
    public IRecipe getRecipe() {
        return recipe;
    }

    @Override
    public void setRecipe(IRecipe operation) {
        this.recipe = operation;
    }

}
