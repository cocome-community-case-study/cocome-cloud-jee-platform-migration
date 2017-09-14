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

package org.cocome.tradingsystem.inventory.data.enterprise;

import org.cocome.tradingsystem.inventory.data.INameable;
import org.cocome.tradingsystem.inventory.data.store.IStore;

import java.util.Collection;

public interface ITradingEnterprise extends INameable {

    /**
     * @return id a unique identifier of this TradingEnterprise
     */
    @Override
    long getId();

    /**
     * @param id a unique identifier of this TradingEnterprise
     */
    @Override
    void setId(long id);

    /**
     * @return Name of this TradingEnterprise
     */
    @Override
    String getName();

    /**
     * @param name Name of this TradingEnterprise
     */
    @Override
    void setName(String name);

    /**
     * @return Collection of Stores related to the TradingEnterprise
     */
    Collection<IStore> getStores();

    /**
     * @param stores Collection of Stores related to the TradingEnterprise
     */
    void setStores(Collection<IStore> stores);

    /**
     * @return Collection of Suppliers related to the TradingEnterprise
     */
    Collection<IProductSupplier> getSuppliers();

    /**
     * @param suppliers Collection of Suppliers related to the TradingEnterprise
     */
    void setSuppliers(Collection<IProductSupplier> suppliers);

}