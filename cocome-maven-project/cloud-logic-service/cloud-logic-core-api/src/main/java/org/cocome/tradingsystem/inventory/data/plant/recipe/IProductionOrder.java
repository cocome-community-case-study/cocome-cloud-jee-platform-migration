/*
 **************************************************************************
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

import org.cocome.tradingsystem.inventory.data.enterprise.parameter.ICustomProductParameterValue;
import org.cocome.tradingsystem.inventory.data.store.IStore;
import org.cocome.tradingsystem.util.exception.NotInDatabaseException;

/**
 * The class represents an order of a {@link IRecipe} in the database.
 *
 * @author Rudolf Biczok
 */
public interface IProductionOrder extends IRecipeOperationOrder<ICustomProductParameterValue,
        IProductionOrderEntry> {

    /**
     * @return the source store from which the order came from
     */
    IStore getStore() throws NotInDatabaseException;

    /**
     * @param store the source store from which the order came from
     */
    void setStore(final IStore store);

    /**
     * @return The id of the source store from which the order came from
     */
    long getStoreId();

    /**
     * @param storeId the id of the source store from which the order came from
     */
    void setStoreId(final long storeId);

}
