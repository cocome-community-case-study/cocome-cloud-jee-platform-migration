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
 **************************************************************************
 */

package org.cocome.tradingsystem.inventory.data.plant.recipe;

import org.cocome.tradingsystem.inventory.data.IIdentifiable;
import org.cocome.tradingsystem.util.exception.NotInDatabaseException;

/**
 * Represents a single {@link IPlantOperationOrder} entry in the database.
 *
 * @author Rudolf Biczok
 */
public interface IPlantOperationOrderEntry extends IIdentifiable {

    /**
     * @return The amount of ordered products
     */
    long getAmount();

    /**
     * @param amount The amount of ordered products
     */
    void setAmount(final long amount);

    /**
     * @return The operation order where the entry belongs to
     */
    IPlantOperationOrder getOrder() throws NotInDatabaseException;

    /**
     * @param productOrder The operation order where the entry belongs to
     */
    void setOrder(final IPlantOperationOrder productOrder);

    /**
     * @return The id of the operation order where the entry belongs to
     */
    long getOrderId();

    /**
     * @param orderId The id of the operation order where the entry belongs to
     */
    void setOrderId(final long orderId);

    /**
     * @return The operation which is ordered
     */
    IPlantOperation getOperation() throws NotInDatabaseException;

    /**
     * @param operation The operation which is ordered
     */
    void setOperation(final IPlantOperation operation);

    /**
     * @return The operation which is ordered
     */
    long getOperationId();

    /**
     * @param operation The operation which is ordered
     */
    void setOperatioId(final long operation);
}
