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

import org.cocome.cloud.logic.webservice.StreamUtil;
import org.cocome.tradingsystem.inventory.application.IIdentifiableTO;
import org.cocome.tradingsystem.inventory.application.store.EnterpriseTO;
import org.cocome.tradingsystem.inventory.data.plant.parameter.IPlantOperationParameter;
import org.cocome.tradingsystem.util.exception.NotInDatabaseException;

import javax.xml.bind.annotation.*;
import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * The class represents an order of a {@link PlantOperationTO} in the database.
 *
 * @author Rudolf Biczok
 */
@XmlType(
        name = "PlantOperationOrderTO",
        namespace = "http://recipe.plant.application.inventory.tradingsystem.cocome.org")
@XmlRootElement(name = "PlantOperationOrderTO")
@XmlAccessorType(XmlAccessType.FIELD)
public class PlantOperationOrderTO implements Serializable, IIdentifiableTO {

    private static final long serialVersionUID = -8340585715760459030L;

    @XmlElement(name = "id", required = true)
    private long id;
    @XmlElement(name = "deliveryDate", required = true)
    private Date deliveryDate;
    @XmlElement(name = "orderingDate", required = true)
    private Date orderingDate;
    @XmlElement(name = "enterprise", required = true)
    private EnterpriseTO enterprise;
    @XmlElement(name = "orderEntries", required = true)
    private Collection<PlantOperationOrderEntryTO> orderEntries;

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
     * @return The enterprise where the order is placed.
     */
    public EnterpriseTO getEnterprise() {
        return this.enterprise;
    }

    /**
     * @param enterprise the enterprise where the order is placed
     */
    public void setEnterprise(final EnterpriseTO enterprise) {
        this.enterprise = enterprise;
    }

    /**
     * @return plant operation entries
     */
    public Collection<PlantOperationOrderEntryTO> getOrderEntries() {
        return orderEntries;
    }

    /**
     * @param orderEntries plant operation entries
     */
    public void setOrderEntries(Collection<PlantOperationOrderEntryTO> orderEntries) {
        this.orderEntries = orderEntries;
    }
}
