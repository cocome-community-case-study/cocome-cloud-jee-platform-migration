/*
 ***************************************************************************
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
 ***************************************************************************
 */

package org.cocome.tradingsystem.inventory.data.store;

import org.cocome.tradingsystem.inventory.data.INameable;
import org.cocome.tradingsystem.inventory.data.enterprise.ITradingEnterprise;
import org.cocome.tradingsystem.util.exception.NotInDatabaseException;

import java.util.Collection;

public interface IStore extends INameable {

    /**
     * @return A unique identifier for Store objects
     */
    long getId();

    /**
     * @param id A unique identifier for Store objects
     */
    void setId(long id);

    /**
     * Returns the name of the store.
     *
     * @return Store name.
     */
    String getName();

    /**
     * @param name the name of the Store
     */
    void setName(String name);

    /**
     * Returns the location of the store.
     *
     * @return Store location.
     */
    String getLocation();

    /**
     * Sets the location of the store.
     *
     * @param location store location
     */
    void setLocation(String location);

    /**
     * @return The enterprise which the Store belongs to
     */
    ITradingEnterprise getEnterprise() throws NotInDatabaseException;

    /**
     * @param enterprise The enterprise which the Store belongs to
     */
    void setEnterprise(ITradingEnterprise enterprise);

    /**
     * @return All product orders of the Store.
     */
    Collection<IProductOrder> getProductOrders();

    /**
     * @param productOrders all product orders of the Store
     */
    void setProductOrders(Collection<IProductOrder> productOrders);

    /**
     * @return A list of StockItem objects. A StockItem represents a concrete
     * product in the store including sales price, ...
     */
    Collection<IStockItem> getStockItems();

    /**
     * @param stockItems A list of StockItem objects. A StockItem represents a concrete
     *                   product in the store including sales price, ...
     */
    void setStockItems(Collection<IStockItem> stockItems);

    String toString();

    int compareTo(IStore o);

    String getEnterpriseName();

    void setEnterpriseName(String enterpriseName);

}