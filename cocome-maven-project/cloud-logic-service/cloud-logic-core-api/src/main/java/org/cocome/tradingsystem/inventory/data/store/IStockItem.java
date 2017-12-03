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

public interface IStockItem extends IItem {

    /**
     * @return The currently available amount of items of a product
     */
    long getAmount();

    /**
     * @param amount the currently available amount of items of a product
     */
    void setAmount(long amount);

    /**
     * This method will be used while computing the low stock item list.
     *
     * @return The maximum capacity of a product in a store.
     */
    long getMaxStock();

    /**
     * This method enables the definition of the maximum capacity of a product
     * in a store.
     *
     * @param maxStock the maximum capacity of a product in a store
     */
    void setMaxStock(long maxStock);

    /**
     * @return The minimum amount of products which has to be available in a
     * store.
     */
    long getMinStock();

    /**
     * @param minStock the minimum amount of products which has to be available in a
     *                 store
     */
    void setMinStock(long minStock);

    /**
     * Required for UC 8
     *
     * @return incomingAmount
     * the amount of products that will be delivered in the near future
     */
    long getIncomingAmount();

    /**
     * Set the amount of products that will be delivered in the near future.
     * <p>
     * Required for UC 8
     *
     * @param incomingAmount the absolute amount (no delta) of incoming products
     */
    void setIncomingAmount(long incomingAmount);
}