/*
 ****************************************************************************
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

package org.cocome.tradingsystem.inventory.data.enterprise;

import java.io.Serializable;

/**
 * This class represents a Product in the database
 *
 * @author Yannick Welsch
 * @author Tobias Pöppke
 */

abstract class AbstractProduct implements Serializable, IAbstractProduct {
    private static final long serialVersionUID = -2577328715744776645L;

    private long id;

    private long barcode;

    private double purchasePrice;

    private String name;

    /* (non-Javadoc)
     * @see org.cocome.tradingsystem.inventory.data.enterprise.IProduct#getId()
     */
    @Override
    public long getId() {
        return id;
    }

    /* (non-Javadoc)
     * @see org.cocome.tradingsystem.inventory.data.enterprise.IProduct#setId(long)
     */
    @Override
    public void setId(long id) {
        this.id = id;
    }

    /* (non-Javadoc)
     * @see org.cocome.tradingsystem.inventory.data.enterprise.IProduct#getBarcode()
     */
    @Override
    public long getBarcode() {
        return barcode;
    }

    /* (non-Javadoc)
     * @see org.cocome.tradingsystem.inventory.data.enterprise.IProduct#setBarcode(long)
     */
    @Override
    public void setBarcode(long barcode) {
        this.barcode = barcode;
    }

    /* (non-Javadoc)
     * @see org.cocome.tradingsystem.inventory.data.enterprise.IProduct#getName()
     */
    @Override
    public String getName() {
        return name;
    }

    /* (non-Javadoc)
     * @see org.cocome.tradingsystem.inventory.data.enterprise.IProduct#setName(java.lang.String)
     */
    @Override
    public void setName(String name) {
        this.name = name;
    }

    /* (non-Javadoc)
     * @see org.cocome.tradingsystem.inventory.data.enterprise.IProduct#getPurchasePrice()
     */
    @Override
    public double getPurchasePrice() {
        return purchasePrice;
    }

    /* (non-Javadoc)
     * @see org.cocome.tradingsystem.inventory.data.enterprise.IProduct#setPurchasePrice(double)
     */
    @Override
    public void setPurchasePrice(double purchasePrice) {
        this.purchasePrice = purchasePrice;
    }

}
