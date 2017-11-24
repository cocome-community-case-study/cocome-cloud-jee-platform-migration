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

import org.cocome.tradingsystem.inventory.data.INameable;
import org.cocome.tradingsystem.inventory.data.plant.parameter.IPlantOperationParameter;
import org.cocome.tradingsystem.inventory.data.plant.recipe.*;
import org.cocome.tradingsystem.util.exception.NotInDatabaseException;
import org.cocome.tradingsystem.util.exception.RecipeException;

import java.util.*;
import java.util.stream.Collectors;

/**
 * This class is used for recipe validation and processing
 *
 * @author Rudolf Biczok
 */
public class RecipeExecutionGraph {

    private static class EdgeMatch {
        private EdgeMatch(final IPlantOperation prev, IEntryPointInteraction interaction) {
            this.prev = prev;
            this.interaction = interaction;
        }

        private IPlantOperation prev;
        private IEntryPointInteraction interaction;
    }

    private final IRecipe recipe;

    private final List<RecipeExecutionGraphNode> startNodes = new LinkedList<>();

    /**
     * Constructs a new execution graph based on the given recipe.
     * <p>
     * During construction checks for:
     * <ol>
     * <li>If all nodes and entry points are used / connected with each other</li>
     * <li>If the recipe is directed acyclic graph</li>
     * <li>If all plant operation parameters are assigned to their corresponding custom product parameters</li>
     * </ol>
     *
     * @param recipe the recipe extracted from the database
     * @throws NotInDatabaseException if some elements in the recipe appear to be invalid database references
     * @throws RecipeException        if the recipe has semantic errors
     */
    public RecipeExecutionGraph(IRecipe recipe)
            throws NotInDatabaseException, RecipeException {
        Objects.requireNonNull(recipe);

        if (recipe.getInputEntryPoint() != null && !recipe.getInputEntryPoint().isEmpty()) {
            throw new RecipeException("Input entry points are currently not supported for recipes");
        }

        this.recipe = recipe;

        final Map<String, RecipeExecutionGraphNode> nodesMapping = this.recipe.getOperations().stream()
                .collect(Collectors.toMap(INameable::getName, RecipeExecutionGraphNode::new));
        final Map<String, Boolean> visited = this.recipe.getOperations().stream()
                .collect(Collectors.toMap(INameable::getName, e -> false));
        final Map<String, Boolean> finished = this.recipe.getOperations().stream()
                .collect(Collectors.toMap(INameable::getName, e -> false));
        final Map<Long, Boolean> entryPointMapping = new HashMap<>();
        for (final IPlantOperation operation : this.recipe.getOperations()) {
            for (final IEntryPoint entryPoint : operation.getInputEntryPoint()) {
                entryPointMapping.putIfAbsent(entryPoint.getId(), false);
            }
            for (final IEntryPoint entryPoint : operation.getOutputEntryPoint()) {
                entryPointMapping.putIfAbsent(entryPoint.getId(), false);
            }
        }

        for (final IEntryPoint e : this.recipe.getOutputEntryPoint()) {
            final EdgeMatch neighbor = followEntryPointInteraction(this.recipe, e);
            traverse(neighbor, nodesMapping, visited, finished, entryPointMapping);
        }

        for (final Map.Entry<String, Boolean> visitedNode : visited.entrySet()) {
            if (!visitedNode.getValue()) {
                throw new RecipeException("Operation not properly included into recipe: " +
                        visitedNode.getKey());
            }
        }
        for (final Map.Entry<Long, Boolean> visitedEntryPoint : entryPointMapping.entrySet()) {
            if (!visitedEntryPoint.getValue()) {
                throw new RecipeException("Operation not properly included into entry point: " +
                        visitedEntryPoint.getKey());
            }
        }
    }

    public IRecipe getRecipe() {
        return recipe;
    }

    public List<RecipeExecutionGraphNode> getStartNodes() {
        return startNodes;
    }

    private void traverse(final EdgeMatch neighbor,
                          final Map<String, RecipeExecutionGraphNode> nodesMapping,
                          final Map<String, Boolean> visited,
                          final Map<String, Boolean> finished,
                          final Map<Long, Boolean> entryPointsVisited) throws NotInDatabaseException, RecipeException {
        entryPointsVisited.put(neighbor.interaction.getFrom().getId(), true);
        entryPointsVisited.put(neighbor.interaction.getTo().getId(), true);

        if (finished.get(neighbor.prev.getName())) {
            return;
        }
        if (visited.get(neighbor.prev.getName())) {
            throw new RecipeException(String.format("Cycle detected on node: '%s'", neighbor.prev.getName()));
        }
        visited.put(neighbor.prev.getName(), true);

        for (final IEntryPoint entryPoint : neighbor.prev.getInputEntryPoint()) {
            final EdgeMatch nextNeighbor = followEntryPointInteraction(neighbor.prev, entryPoint);

            final RecipeExecutionGraphNode from = nodesMapping.get(nextNeighbor.prev.getName());
            final RecipeExecutionGraphNode to = nodesMapping.get(neighbor.prev.getName());

            from.getEdges().add(new RecipeExecutionGraphEdge(nextNeighbor.interaction, to));
            traverse(nextNeighbor, nodesMapping, visited, finished, entryPointsVisited);
        }

        for (final IPlantOperationParameter param : neighbor.prev.getParameters()) {
            nodesMapping.get(neighbor.prev.getName()).getParameterInteractions()
                    .add(getInteractionOf(neighbor.prev, param));
        }

        if (neighbor.prev.getInputEntryPoint().isEmpty()) {
            this.startNodes.add(nodesMapping.get(neighbor.prev.getName()));
        }
        finished.put(neighbor.prev.getName(), true);
    }

    private IParameterInteraction getInteractionOf(final IPlantOperation operation,
                                                   final IPlantOperationParameter param)
            throws NotInDatabaseException, RecipeException {
        for (final IParameterInteraction paramInteraction : this.recipe.getParameterInteractions()) {
            if (paramInteraction.getTo().getId() == param.getId()) {
                return paramInteraction;
            }
        }
        throw new RecipeException(
                String.format("Missing parameter mapping for '%s'.'%s'",
                        operation.getName(),
                        param.getName()));
    }

    private EdgeMatch followEntryPointInteraction(final IRecipeOperation source, final IEntryPoint entryPoint)
            throws NotInDatabaseException, RecipeException {
        for (final IEntryPointInteraction interaction : this.recipe.getEntryPointInteractions()) {
            if (interaction.getTo().getId() == entryPoint.getId()) {
                for (final IPlantOperation operation : this.recipe.getOperations()) {
                    for (final IEntryPoint nextEP : operation.getOutputEntryPoint()) {
                        if (nextEP.getId() == interaction.getFrom().getId()) {
                            return new EdgeMatch(operation, interaction);
                        }
                    }
                }
            }
        }
        throw new RecipeException(
                String.format("Missing edge for: '%s'.'%s'", source.getName(), entryPoint.getName()));
    }
}
