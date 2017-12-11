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
import org.cocome.tradingsystem.inventory.data.enterprise.ITradingEnterprise;
import org.cocome.tradingsystem.inventory.data.plant.parameter.IParameter;
import org.cocome.tradingsystem.util.exception.NotInDatabaseException;

import javax.annotation.PostConstruct;
import javax.enterprise.context.Dependent;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
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
    private String name;
    private ICustomProduct customProduct;
    private long customProductId;
    private ITradingEnterprise enterprise;
    private long enterpriseId;

    // Represent the vertices of the recipe graph
    private Collection<IRecipeNode> nodes;

    // Represent the edges of the recipe graph
    private Collection<IParameterInteraction> parameterInteractions;
    private Collection<IEntryPointInteraction> entryPointInteractions;

    // Input / Output ports
    private Collection<IEntryPoint> inputEntryPoint;
    private Collection<IEntryPoint> outputEntryPoint;

    @Inject
    private Instance<IEnterpriseQuery> enterpriseQueryInstance;

    private IEnterpriseQuery enterpriseQuery;

    @PostConstruct
    public void initPlant() {
        enterpriseQuery = enterpriseQueryInstance.get();
        customProduct = null;
        enterprise = null;
        nodes = null;
        entryPointInteractions = null;
        inputEntryPoint = null;
        outputEntryPoint = null;
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
    public String getName() {
        return this.name;
    }

    @Override
    public void setName(final String name) {
        this.name = name;
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
        return this.customProductId;
    }

    @Override
    public void setCustomProductId(long customProductId) {
        this.customProductId = customProductId;
    }

    @Override
    public ITradingEnterprise getEnterprise() throws NotInDatabaseException {
        if (enterprise == null) {
            enterprise = enterpriseQuery.queryEnterpriseById(enterpriseId);
        }
        return enterprise;
    }

    @Override
    public void setEnterprise(ITradingEnterprise enterprise) {
        this.enterprise = enterprise;
    }

    @Override
    public long getEnterpriseId() {
        return enterpriseId;
    }

    @Override
    public void setEnterpriseId(long enterpriseId) {
        this.enterpriseId = enterpriseId;
    }

    @Override
    public Collection<IRecipeNode> getNodes() throws NotInDatabaseException {
        if (nodes == null) {
            nodes = enterpriseQuery.queryRecipeNodesByRecipeId(this.id);
        }
        return nodes;
    }

    @Override
    public void setNodes(Collection<IRecipeNode> nodes) {
        this.nodes = nodes;
    }

    @Override
    public Collection<IEntryPointInteraction> getEntryPointInteractions() throws NotInDatabaseException {
        if (entryPointInteractions == null) {
            entryPointInteractions = enterpriseQuery.queryEntryPointInteractionsByRecipeId(this.id);
        }
        return entryPointInteractions;
    }

    @Override
    public void setEntryPointInteractions(Collection<IEntryPointInteraction> entryPointInteractions) {
        this.entryPointInteractions = entryPointInteractions;
    }

    @Override
    public Collection<IParameterInteraction> getParameterInteractions() throws NotInDatabaseException {
        if (parameterInteractions == null) {
            parameterInteractions = enterpriseQuery.queryParameterInteractionsByRecipeId(this.id);
        }
        return parameterInteractions;
    }

    @Override
    public void setParameterInteractions(Collection<IParameterInteraction> parameterInteractions) {
        this.parameterInteractions = parameterInteractions;
    }

    @Override
    public Collection<IEntryPoint> getInputEntryPoint() throws NotInDatabaseException {
        if (inputEntryPoint == null) {
            inputEntryPoint = enterpriseQuery.queryInputEntryPointsByRecipeOperationId(id);
        }
        return inputEntryPoint;
    }

    @Override
    public void setInputEntryPoint(Collection<IEntryPoint> inputMaterial) {
        this.inputEntryPoint = inputMaterial;
    }

    @Override
    public Collection<IEntryPoint> getOutputEntryPoint() throws NotInDatabaseException {
        if (outputEntryPoint == null) {
            outputEntryPoint = enterpriseQuery.queryOutputEntryPointsByRecipeOperationId(id);
        }
        return outputEntryPoint;
    }

    @Override
    public void setOutputEntryPoint(Collection<IEntryPoint> outputEntryPoint) {
        this.outputEntryPoint = outputEntryPoint;
    }

    @Override
    public Collection<IParameter> getParameters() {
        return null;
    }
}
