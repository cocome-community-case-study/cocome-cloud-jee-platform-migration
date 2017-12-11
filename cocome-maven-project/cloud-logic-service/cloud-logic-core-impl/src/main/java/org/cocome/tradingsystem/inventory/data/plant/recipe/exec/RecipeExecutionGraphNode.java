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

package org.cocome.tradingsystem.inventory.data.plant.recipe.exec;

import org.cocome.tradingsystem.inventory.data.plant.recipe.IParameterInteraction;
import org.cocome.tradingsystem.inventory.data.plant.recipe.IRecipeOperation;

import java.util.LinkedList;
import java.util.List;

/**
 * Represents one single processing step in the recipe
 *
 * @author Rudolf Biczok
 */
public class RecipeExecutionGraphNode {

    private IRecipeOperation plantOperation;
    private List<RecipeExecutionGraphEdge> edges = new LinkedList<>();

    private List<IParameterInteraction> parameterInteractions = new LinkedList<>();

    public RecipeExecutionGraphNode(final IRecipeOperation plantOperation) {
        this.plantOperation = plantOperation;
    }

    public IRecipeOperation getOperation() {
        return plantOperation;
    }

    public List<RecipeExecutionGraphEdge> getEdges() {
        return edges;
    }

    public List<IParameterInteraction> getParameterInteractions() {
        return parameterInteractions;
    }
}
