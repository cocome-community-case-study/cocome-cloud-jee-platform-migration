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

import javax.xml.bind.annotation.*;

/**
 * A transfer object class for exchanging basic stock item information between
 * client and the service-oriented application layer. It contains either copies
 * of persisted data which is transferred to the client, or data which is
 * transferred from the client to the application layer to be processed and
 * persisted.
 *
 * @author Sebastian Herold
 * @author Lubomir Bulej
 * @author Rudolf Biczok
 * @see IStockItem
 */
@XmlType(name = "StockItemTO", namespace = "http://store.application.inventory.tradingsystem.cocome.org/")
@XmlRootElement(name = "StockItemTO")
@XmlAccessorType(XmlAccessType.FIELD)
public class StockItemTO extends ItemTO {

    private static final long serialVersionUID = 5874806761123366899L;

    @XmlElement(name = "amount")
    private long __amount;

    @XmlElement(name = "minStock")
    private long __minStock;

    @XmlElement(name = "maxStock")
    private long __maxStock;

    @XmlElement(name = "incomingAmount")
    private long __incomingAmount;

    /**
     * Returns the stock amount.
     *
     * @return Stock amount.
     */
    public long getAmount() {
        return __amount;
    }

    /**
     * Sets the stock amount.
     *
     * @param amount new stock amount
     */
    public void setAmount(final long amount) {
        __amount = amount;
    }

    /**
     * Returns the minimal stock amount for this stock item.
     *
     * @return Minimum stock amount.
     */
    public long getMinStock() {
        return __minStock;
    }

    /**
     * Sets the minimal stock amount for this stock item.
     *
     * @param minStock new minimal stock amount
     */
    public void setMinStock(final long minStock) {
        __minStock = minStock;
    }

    /**
     * Returns the maximal stock amount for this stock item.
     *
     * @return Maximum stock amount.
     */
    public long getMaxStock() {
        return __maxStock;
    }

    /**
     * Sets the maximal stock amount for this stock item.
     *
     * @param maxStock new maximal stock amount
     */
    public void setMaxStock(final long maxStock) {
        __maxStock = maxStock;
    }

    /**
     * Returns the incoming amount of a stock item product.
     *
     * @return Stock item product incoming amount.
     */
    public long getIncomingAmount() {
        return __incomingAmount;
    }

    /**
     * Sets the incoming amount of a stock item product.
     *
     * @param incomingAmount new stock item product incoming amount
     */
    public void setIncomingAmount(long incomingAmount) {
        this.__incomingAmount = incomingAmount;
    }

}
