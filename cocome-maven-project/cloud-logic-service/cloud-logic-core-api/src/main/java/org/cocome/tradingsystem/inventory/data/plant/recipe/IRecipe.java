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
import org.cocome.tradingsystem.inventory.data.enterprise.ITradingEnterprise;
import org.cocome.tradingsystem.util.exception.NotInDatabaseException;

import java.util.Collection;

/**
 * Represents the top-level recipe for producing a custom product.
 *
 * @author Rudolf Biczok
 */
public interface IRecipe extends IRecipeOperation {

    ICustomProduct getCustomProduct() throws NotInDatabaseException;

    void setCustomProduct(ICustomProduct customProduct);

    long getCustomProductId();

    void setCustomProductId(long customProductId);

    ITradingEnterprise getEnterprise() throws NotInDatabaseException;

    void setEnterprise(ITradingEnterprise enterprise);

    long getEnterpriseId();

    void setEnterpriseId(long enterpriseId);

    Collection<IRecipeNode> getNodes() throws NotInDatabaseException;

    void setNodes(Collection<IRecipeNode> nodes);

    Collection<IEntryPointInteraction> getEntryPointInteractions() throws NotInDatabaseException;

    void setEntryPointInteractions(Collection<IEntryPointInteraction> entryPointInteractions);

    Collection<IParameterInteraction> getParameterInteractions() throws NotInDatabaseException;

    void setParameterInteractions(Collection<IParameterInteraction> parameterInteractions);
}
