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
import org.cocome.tradingsystem.util.exception.RecipeException;

import javax.annotation.PostConstruct;
import javax.enterprise.context.Dependent;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    // Represent the vertices of the recipe graph
    private Collection<IPlantOperation> operations;
    private List<Long> operationIds;

    // Represent the edges of the recipe graph
    private Collection<IParameterInteraction> parameterInteractions;
    private Collection<IEntryPointInteraction> entryPointInteractions;
    private List<Long> parameterInteractionIds;
    private List<Long> entryPointInteractionIds;

    // Input / Output ports
    private Collection<IEntryPoint> inputEntryPoint;
    private Collection<IEntryPoint> outputEntryPoint;
    private List<Long> inputEntryPointIds;
    private List<Long> outputEntryPointIds;

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
    public Collection<IEntryPointInteraction> getEntryPointInteractions() throws NotInDatabaseException {
        if (entryPointInteractions == null) {
            entryPointInteractions = enterpriseQuery.queryEntryPointInteractions(entryPointInteractionIds);
        }
        return entryPointInteractions;
    }

    @Override
    public void setEntryPointInteractions(Collection<IEntryPointInteraction> entryPointInteractions) {
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

    @Override
    public Collection<IEntryPoint> getInputEntryPoint() throws NotInDatabaseException {
        if (inputEntryPoint == null) {
            inputEntryPoint = enterpriseQuery.queryEntryPoints(inputEntryPointIds);
        }
        return inputEntryPoint;
    }

    @Override
    public void setInputEntryPoint(Collection<IEntryPoint> inputMaterial) {
        this.inputEntryPoint = inputMaterial;
    }

    @Override
    public List<Long> getInputEntryPointIds() {
        return inputEntryPointIds;
    }

    @Override
    public void setInputEntryPointIds(List<Long> inputEntryPointIds) {
        this.inputEntryPointIds = inputEntryPointIds;
    }

    @Override
    public Collection<IEntryPoint> getOutputEntryPoint() throws NotInDatabaseException {
        if (outputEntryPoint == null) {
            outputEntryPoint = enterpriseQuery.queryEntryPoints(outputEntryPointIds);
        }
        return outputEntryPoint;
    }

    @Override
    public void setOutputEntryPoint(Collection<IEntryPoint> outputEntryPoint) {
        this.outputEntryPoint = outputEntryPoint;
    }

    @Override
    public List<Long> getOutputEntryPointIds() {
        return outputEntryPointIds;
    }

    @Override
    public void setOutputEntryPointIds(List<Long> outputEntryPointIds) {
        this.outputEntryPointIds = outputEntryPointIds;
    }

    @Override
    public void validate() throws RecipeException, NotInDatabaseException {
        final Map<String, Boolean> visitedNodes = new HashMap<>();
        for (final IPlantOperation operation : this.getOperations()) {
            visitedNodes.put(operation.getName(), false);
        }

        checkForMissingEdges(this, visitedNodes);
        if (!visitedNodes.entrySet().stream().allMatch(Map.Entry::getValue)) {
            throw new RecipeException("Not fully connected");
        }
        checkForMissingParameterInteractions();
    }

    private void checkForMissingParameterInteractions() throws NotInDatabaseException {
    }

    private void checkForMissingEdges(final IRecipeOperation recipeStep, final Map<String, Boolean> visitedNodes)
            throws NotInDatabaseException, RecipeException {
        for (final IEntryPoint inputEntryPoint : this.getInputEntryPoint()) {
            final IPlantOperation operation = this.getOutgoingNeighbour(recipeStep, inputEntryPoint);
            if (operation == null) {
                throw new RecipeException(String.format("No output port specified for '%s'.'%s'",
                        recipeStep.getName(),
                        inputEntryPoint.getName()));
            }
            if (visitedNodes.get(operation.getName())) {
                throw new RecipeException(String.format("Cyclic reference between '%s' and '%s'",
                        recipeStep.getName(),
                        operation.getName()));
            }
            visitedNodes.put(operation.getName(), true);
            checkForMissingEdges(operation, visitedNodes);
        }
        for (final IEntryPoint outputEntryPoint : this.getOutputEntryPoint()) {
            final IPlantOperation operation = this.getIngoingNeighbour(recipeStep, outputEntryPoint);
            if (operation == null) {
                throw new RecipeException(String.format("No input port specified for '%s'.'%s'",
                        recipeStep.getName(),
                        outputEntryPoint.getName()));
            }
            if (visitedNodes.get(operation.getName())) {
                throw new RecipeException(String.format("Cyclic reference between '%s' and '%s'",
                        recipeStep.getName(),
                        operation.getName()));
            }
            visitedNodes.put(operation.getName(), true);
            checkForMissingEdges(operation, visitedNodes);
        }
    }

    private IPlantOperation getIngoingNeighbour(final IRecipeOperation operation, final IEntryPoint entryPoint)
            throws NotInDatabaseException {
        for (final IEntryPoint inEntry : operation.getInputEntryPoint()) {
            if (inEntry.getId() == entryPoint.getId()) {
                for (final IEntryPointInteraction interaction : this.getEntryPointInteractions()) {
                    if (interaction.getTo().getId() == entryPoint.getId()) {
                        for (final IPlantOperation nextOperation : this.getOperations()) {
                            for (final IEntryPoint outEntry : nextOperation.getInputEntryPoint()) {
                                if (outEntry.getId() == interaction.getFrom().getId()) {
                                    return nextOperation;
                                }
                            }
                        }
                    }
                }
            }
        }
        return null;
    }

    private IPlantOperation getOutgoingNeighbour(final IRecipeOperation operation, final IEntryPoint entryPoint)
            throws NotInDatabaseException {
        for (final IEntryPoint outEntry : operation.getOutputEntryPoint()) {
            if (outEntry.getId() == entryPoint.getId()) {
                for (final IEntryPointInteraction interaction : this.getEntryPointInteractions()) {
                    if (interaction.getFrom().getId() == entryPoint.getId()) {
                        for (final IPlantOperation nextOperation : this.getOperations()) {
                            for (final IEntryPoint inEntry : nextOperation.getInputEntryPoint()) {
                                if (inEntry.getId() == interaction.getTo().getId()) {
                                    return nextOperation;
                                }
                            }
                        }
                    }
                }
            }
        }
        return null;
    }
}
