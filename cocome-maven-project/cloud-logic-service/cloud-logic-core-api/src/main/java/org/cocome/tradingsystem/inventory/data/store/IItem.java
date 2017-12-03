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

import org.cocome.tradingsystem.inventory.data.IIdentifiable;
import org.cocome.tradingsystem.inventory.data.enterprise.IProduct;
import org.cocome.tradingsystem.util.exception.NotInDatabaseException;

public interface IItem extends IIdentifiable {

    /**
     * @return The Product of a StockItem.
     */
    IProduct getProduct();

    /**
     * @param product the Product of a StockItem
     */
    void setProduct(IProduct product);

    /**
     * @return The sales price of the StockItem
     */
    double getSalesPrice();

    /**
     * @param salesPrice the sales price of the StockItem
     */
    void setSalesPrice(double salesPrice);

    /**
     * @return The store where the StockItem belongs to
     */
    IStore getStore() throws NotInDatabaseException;

    /**
     * @param store The store where the StockItem belongs to
     */
    void setStore(IStore store);

    long getStoreId();

    void setStoreId(long storeName);

    long getProductBarcode();

    void setProductBarcode(long productBarcode);
}