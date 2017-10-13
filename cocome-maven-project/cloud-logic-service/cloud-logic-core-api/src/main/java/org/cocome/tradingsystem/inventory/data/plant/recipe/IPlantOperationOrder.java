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

import org.cocome.tradingsystem.inventory.data.IIdentifiable;
import org.cocome.tradingsystem.inventory.data.enterprise.ITradingEnterprise;
import org.cocome.tradingsystem.util.exception.NotInDatabaseException;

import java.util.Date;

/**
 * The class represents an order of a {@link IPlantOperation} in the database.
 *
 * @author Rudolf Biczok
 */
public interface IPlantOperationOrder extends IIdentifiable {

    /**
     * @return The date of ordering.
     */
    Date getOrderingDate();

    /**
     * @param orderingDate the date of ordering
     */
    void setOrderingDate(final Date orderingDate);

    /**
     * The delivery date is used for computing the mean time to delivery
     *
     * @return The date of order fulfillment.
     */
    Date getDeliveryDate();

    /**
     * The delivery date is used for computing the mean time to delivery
     *
     * @param deliveryDate the date of order fulfillment
     */
    void setDeliveryDate(final Date deliveryDate);

    /**
     * @return The enterprise where the order is placed.
     */
    ITradingEnterprise getEnterprise() throws NotInDatabaseException;

    /**
     * @param enterprise the enterprise where the order is placed
     */
    void setEnterprise(final ITradingEnterprise enterprise);

    /**
     * @return The id of the enterprise where the order is placed.
     */
    long getEnterpriseId();

    /**
     * @param enterprise the id of the enterprise where the order is placed
     */
    void setEnterpriseId(final long enterprise);
}
