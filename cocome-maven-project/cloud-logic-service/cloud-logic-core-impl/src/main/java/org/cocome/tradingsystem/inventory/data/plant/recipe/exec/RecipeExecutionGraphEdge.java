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

import org.cocome.tradingsystem.inventory.data.plant.recipe.IEntryPointInteraction;

/**
 * Contains the entryPoint interaction informations and
 *
 * @author Rudolf Biczok
 */
public class RecipeExecutionGraphEdge {

    private final IEntryPointInteraction entryPointInteraction;

    private final RecipeExecutionGraphNode next;

    public RecipeExecutionGraphEdge(IEntryPointInteraction entryPointInteraction,
                                    RecipeExecutionGraphNode next) {
        this.entryPointInteraction = entryPointInteraction;
        this.next = next;
    }

    public IEntryPointInteraction getEntryPointInteraction() {
        return entryPointInteraction;
    }

    public RecipeExecutionGraphNode getNext() {
        return next;
    }

}
