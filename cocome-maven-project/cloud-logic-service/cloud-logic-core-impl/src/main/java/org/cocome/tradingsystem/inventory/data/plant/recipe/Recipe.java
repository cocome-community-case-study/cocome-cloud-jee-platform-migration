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
import org.cocome.tradingsystem.inventory.data.enterprise.IEnterpriseQuery;
import org.cocome.tradingsystem.util.exception.NotInDatabaseException;

import javax.annotation.PostConstruct;
import javax.enterprise.context.Dependent;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;

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
    private long customProductId;

    // Represent the vertices of the recipe graph
    private Collection<IPlantOperation> operations;
    private List<Long> operationIds;

    // Represent the edges of the recipe graph
    private Collection<IParameterInteraction> parameterInteractions;
    private Collection<IEntryPointInteraction> entryPointInteractions;
    private List<Long> parameterInteractionIds;
    private List<Long> entryPointInteractionIds;

    @Inject
    private Instance<IEnterpriseQuery> enterpriseQueryInstance;

    private IEnterpriseQuery enterpriseQuery;

    @PostConstruct
    public void initPlant() {
        enterpriseQuery = enterpriseQueryInstance.get();
        customProduct = null;
        operations = null;
        entryPointInteractions = null;
        parameterInteractionIds = null;
    }

    @Override
    public long getId() {
        return this.id;
    }

    @Override
    public void setId(final long id) {
        this.id = id;
    }

    @Override
    public ICustomProduct getCustomProduct() throws NotInDatabaseException {
        if (customProduct == null) {
            customProduct = enterpriseQuery.queryCustomProductByID(customProductId);
        }
        return customProduct;
    }

    @Override
    public void setCustomProduct(final ICustomProduct customProduct) {
        this.customProduct = customProduct;
    }

    @Override
    public long getCustomProductId() {
        return customProductId;
    }

    @Override
    public void setCustomProductId(long customProductId) {
        this.customProductId = customProductId;
    }

    @Override
    public Collection<IPlantOperation> getOperations() throws NotInDatabaseException {
        if (operations == null) {
            operations = enterpriseQuery.queryPlantOperations(operationIds);
        }
        return operations;
    }

    @Override
    public void setOperations(Collection<IPlantOperation> operations) {
        this.operations = operations;
    }

    @Override
    public List<Long> getOperationIds() {
        return operationIds;
    }

    @Override
    public void setOperationIds(List<Long> operationIds) {
        this.operationIds = operationIds;
    }

    @Override
    public Collection<IEntryPointInteraction> getEntryPointInteractions() {
        return entryPointInteractions;
    }

    @Override
    public void setEntryPointInteractions(Collection<IEntryPointInteraction> entryPointInteractions) throws NotInDatabaseException {
        if (entryPointInteractions == null) {
            entryPointInteractions = enterpriseQuery.queryEntryPointInteractions(entryPointInteractionIds);
        }
        this.entryPointInteractions = entryPointInteractions;
    }

    @Override
    public List<Long> getEntryPointInteractionIds() {
        return entryPointInteractionIds;
    }

    @Override
    public void setEntryPointInteractionIds(List<Long> entryPointInteractionIds) {
        this.entryPointInteractionIds = entryPointInteractionIds;
    }

    @Override
    public Collection<IParameterInteraction> getParameterInteractions() throws NotInDatabaseException {
        if (parameterInteractions == null) {
            parameterInteractions = enterpriseQuery.queryParameterInteractions(parameterInteractionIds);
        }
        return parameterInteractions;
    }

    @Override
    public void setParameterInteractions(Collection<IParameterInteraction> parameterInteractions) {
        this.parameterInteractions = parameterInteractions;
    }

    @Override
    public List<Long> getParameterInteractionIds() {
        return parameterInteractionIds;
    }

    @Override
    public void setParameterInteractionIds(List<Long> parameterInteractionIds) {
        this.parameterInteractionIds = parameterInteractionIds;
    }
}
