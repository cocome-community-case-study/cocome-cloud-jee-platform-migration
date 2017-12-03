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

package org.cocome.tradingsystem.inventory.application.plant.recipe;

import org.cocome.tradingsystem.inventory.application.IIdentifiableTO;
import org.cocome.tradingsystem.inventory.application.store.StoreWithEnterpriseTO;

import javax.xml.bind.annotation.*;
import java.io.Serializable;
import java.util.Collection;
import java.util.Date;

/**
 * The class represents an order of a {@link RecipeTO} in the database.
 *
 * @author Rudolf Biczok
 */
@XmlType(
        name = "ProductionOrderTO",
        namespace = "http://recipe.plant.application.inventory.tradingsystem.cocome.org")
@XmlRootElement(name = "ProductionOrderTO")
@XmlAccessorType(XmlAccessType.FIELD)
public class ProductionOrderTO implements Serializable, IIdentifiableTO {

    private static final long serialVersionUID = -8340585715760459030L;

    @XmlElement(name = "id", required = true)
    private long id;
    @XmlElement(name = "deliveryDate", required = true)
    private Date deliveryDate;
    @XmlElement(name = "orderingDate", required = true)
    private Date orderingDate;
    @XmlElement(name = "store", required = true)
    private StoreWithEnterpriseTO store;
    @XmlElement(name = "orderEntries", required = true)
    private Collection<ProductionOrderEntryTO> orderEntries;

    @Override
    public long getId() {
        return this.id;
    }

    @Override
    public void setId(final long id) {
        this.id = id;
    }

    /**
     * @return The date of ordering.
     */
    public Date getOrderingDate() {
        return this.orderingDate;
    }

    /**
     * @param orderingDate the date of ordering
     */
    public void setOrderingDate(final Date orderingDate) {
        this.orderingDate = orderingDate;
    }

    /**
     * The delivery date is used for computing the mean time to delivery
     *
     * @return The date of order fulfillment.
     */
    public Date getDeliveryDate() {
        return this.deliveryDate;
    }

    /**
     * The delivery date is used for computing the mean time to delivery
     *
     * @param deliveryDate the date of order fulfillment
     */
    public void setDeliveryDate(final Date deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    /**
     * @return The store from which the order came from.
     */
    public StoreWithEnterpriseTO getStore() {
        return this.store;
    }

    /**
     * @param store the store from which the order came from
     */
    public void setStore(final StoreWithEnterpriseTO store) {
        this.store = store;
    }

    /**
     * @return plant operation entries
     */
    public Collection<ProductionOrderEntryTO> getOrderEntries() {
        return orderEntries;
    }

    /**
     * @param orderEntries plant operation entries
     */
    public void setOrderEntries(Collection<ProductionOrderEntryTO> orderEntries) {
        this.orderEntries = orderEntries;
    }
}
