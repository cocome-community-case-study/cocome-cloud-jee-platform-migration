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
import org.cocome.tradingsystem.inventory.data.enterprise.parameter.IParameterValue;
import org.cocome.tradingsystem.util.exception.NotInDatabaseException;

import java.util.Collection;
import java.util.Date;

/**
 * The class represents order within the CoCoME eco system
 *
 * @author Rudolf Biczok
 */
public interface IRecipeOperationOrder<
        T1 extends IParameterValue,
        T2 extends IRecipeOperationOrderEntry<T1>>
        extends IIdentifiable {

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
     * @return the order entries.
     */
    Collection<T2> getOrderEntries();

    /**
     * @param orderEntries the order entries.
     */
    void setOrderEntries(Collection<T2> orderEntries);
}
