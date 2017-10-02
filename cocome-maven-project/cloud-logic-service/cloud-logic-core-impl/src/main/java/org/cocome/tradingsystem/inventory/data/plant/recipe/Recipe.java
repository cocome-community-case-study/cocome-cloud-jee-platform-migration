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

package org.cocome.tradingsystem.inventory.data.plant.recipe;

import org.cocome.tradingsystem.inventory.data.enterprise.ICustomProduct;

import javax.enterprise.context.Dependent;
import java.io.Serializable;
import java.util.Collection;

/**
 * Represents the top-level recipe for producing a custom product.
 *
 * @author Rudolf Biczok
 */
@Dependent
public class Recipe implements Serializable, IRecipe {
    private static final long serialVersionUID = 1L;

    private long id;

    private ICustomProduct customProduct;

    // Represent the vertices of the recipe graph
    private Collection<IPlantOperation> operations;

    // Represent the edges of the recipe graph
    private Collection<IParameterInteraction> parameterInteractions;
    private Collection<IEntryPointInteraction> inputInteractions;
    private Collection<IEntryPointInteraction> outputInteractions;

    @Override
    public long getId() {
        return this.id;
    }

    @Override
    public void setId(final long id) {
        this.id = id;
    }

    @Override
    public ICustomProduct getCustomProduct() {
        return customProduct;
    }

    @Override
    public void setCustomProduct(final ICustomProduct customProduct) {
        this.customProduct = customProduct;
    }

    @Override
    public Collection<IPlantOperation> getOperations() {
        return operations;
    }

    @Override
    public void setOperations(Collection<IPlantOperation> operations) {
        this.operations = operations;
    }

    @Override
    public Collection<IEntryPointInteraction> getInputInteractions() {
        return inputInteractions;
    }

    @Override
    public void setInputInteractions(Collection<IEntryPointInteraction> inputInteractions) {
        this.inputInteractions = inputInteractions;
    }

    @Override
    public Collection<IEntryPointInteraction> getOutputInteractions() {
        return outputInteractions;
    }

    @Override
    public void setOutputInteractions(Collection<IEntryPointInteraction> outputInteractions) {
        this.outputInteractions = outputInteractions;
    }

    @Override
    public Collection<IParameterInteraction> getParameterInteractions() {
        return parameterInteractions;
    }

    @Override
    public void setParameterInteractions(Collection<IParameterInteraction> parameterInteractions) {
        this.parameterInteractions = parameterInteractions;
    }
}
