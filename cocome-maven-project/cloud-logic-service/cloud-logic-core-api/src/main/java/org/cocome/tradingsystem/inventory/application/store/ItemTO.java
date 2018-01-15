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

package org.cocome.tradingsystem.inventory.application.store;

import org.cocome.tradingsystem.inventory.data.store.IStockItem;
import org.cocome.tradingsystem.inventory.data.store.IStore;

import javax.xml.bind.annotation.*;
import java.io.Serializable;

/**
 * Base type for any item in a {@link IStore}.
 *
 * @author Rudolf Biczok
 */
@XmlType(name = "ItemTO", namespace = "http://store.application.inventory.tradingsystem.cocome.org/")
@XmlRootElement(name = "ItemTO")
@XmlSeeAlso({StockItemTO.class, OnDemandItemTO.class})
@XmlAccessorType(XmlAccessType.FIELD)
public abstract class ItemTO implements Serializable {

    private static final long serialVersionUID = 5874806761123366899L;

    //
    @XmlElement(name = "id", required = true)
    private long __id;

    @XmlElement(name = "salesPrice")
    private double __salesPrice;

    public ItemTO() {
    }

    public ItemTO(double __salesPrice) {
        this.__salesPrice = __salesPrice;
    }

    /**
     * Returns the unique identifier of the {@link IStockItem} entity.
     *
     * @return {@link IStockItem} entity identifier.
     */
    public long getId() {
        return __id;
    }

    /**
     * Sets the unique identifier of the {@link IStockItem} entity.
     *
     * @param id new {@link IStockItem} entity identifier
     */
    public void setId(final long id) {
        __id = id;
    }

    /**
     * Returns the sales price of a stock item product.
     *
     * @return Stock item product sales price.
     */
    public double getSalesPrice() {
        return __salesPrice;
    }

    /**
     * Sets the sales price of a stock item product.
     *
     * @param salesPrice new stock item product sales price
     */
    public void setSalesPrice(final double salesPrice) {
        __salesPrice = salesPrice;
    }
}
