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

import java.util.Collection;

/**
 * Represents a single {@link IProductionOrder} entry in the database.
 *
 * @param <T> the operation type
 * @author Rudolf Biczok
 */
public interface IRecipeOperationOrderEntry<T extends IRecipeOperation>
        extends IIdentifiable {

    long getAmount();

    void setAmount(final long amount);

    T getOperation();

    void setOperation(T operation);

    Collection<IParameterValue> getParameterValues();

    void setParameterValues(Collection<IParameterValue> parameterValues);

    long getOperationId();

    void setOperationId(long operationId);

    long getOrderId();

    void setOrderId(final long orderId);

    boolean isFinished();

    void setFinished(boolean finished);

}
