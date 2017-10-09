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

import org.cocome.tradingsystem.inventory.data.IIdentifiable;
import org.cocome.tradingsystem.inventory.data.enterprise.ICustomProduct;

import java.util.Collection;

/**
 * Represents the top-level recipe for producing a custom product.
 *
 * @author Rudolf Biczok
 */
public interface IRecipe extends IIdentifiable {

    /**
     * @return the custom product for which this recipe provides the production details
     */
    ICustomProduct getCustomProduct();

    /**
     * @param customProduct the custom product for which this recipe provides the production details
     */
    void setCustomProduct(ICustomProduct customProduct);

    /**
     * @return the custom product id for which this recipe provides the production details
     */
    long getCustomProductId();

    /**
     * @param customProduct the custom product id for which this recipe provides the production details
     */
    void setCustomProductId(long customProduct);

    /**
     * @return the list of plant operations needed to be executed
     */
    Collection<IPlantOperation> getOperations();

    /**
     * @param operations the list of plant operations needed to be executed
     */
    void setOperations(Collection<IPlantOperation> operations);

    /**
     * @return the list of plant operation IDs
     */
    Collection<Long> getOperationIds();

    /**
     * @param operationIds the list of plant operation IDs
     */
    void setOperationIds(Collection<Long> operationIds);

    /**
     * @return the list of interactions
     */
    Collection<IEntryPointInteraction> getEntryPointInteractions();

    /**
     * @param entryPointInteractions the list of interactions
     */
    void setEntryPointInteractions(Collection<IEntryPointInteraction> entryPointInteractions);

    /**
     * @return the list of interaction IDs
     */
    Collection<Long> getEntryPointInteractionIds();

    /**
     * @param entryPointInteractionIds the list of interactions
     */
    void setEntryPointInteractionIds(Collection<Long> entryPointInteractionIds);

    /**
     * @return the list of parameter bindings
     */
    Collection<IParameterInteraction> getParameterInteractions();

    /**
     * @param parameterInteractions the list of parameter bindings
     */
    void setParameterInteractions(Collection<IParameterInteraction> parameterInteractions);

    /**
     * @return the list of parameter bindings
     */
    Collection<Long> getParameterInteractionIds();

    /**
     * @param parameterInteractionIds the list of parameter bindings
     */
    void setParameterInteractionIds(Collection<Long> parameterInteractionIds);
}
