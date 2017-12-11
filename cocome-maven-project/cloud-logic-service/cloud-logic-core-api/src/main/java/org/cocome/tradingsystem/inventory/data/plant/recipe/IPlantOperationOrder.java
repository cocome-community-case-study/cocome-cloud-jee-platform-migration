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

import org.cocome.tradingsystem.inventory.data.enterprise.ITradingEnterprise;
import org.cocome.tradingsystem.inventory.data.plant.IPlant;
import org.cocome.tradingsystem.inventory.data.plant.parameter.IPlantOperationParameter;
import org.cocome.tradingsystem.util.exception.NotInDatabaseException;

/**
 * The class represents an order of a {@link IPlantOperation} in the database.
 *
 * @author Rudolf Biczok
 */
public interface IPlantOperationOrder extends IRecipeOperationOrder<
        IPlantOperationParameter,
        IPlantOperationParameterValue,
        IPlantOperationOrderEntry> {
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

    IPlant getPlant() throws NotInDatabaseException;

    void setPlant(final IPlant plant);

    long getPlantId();

    void setPlantId(final long plantId);
}
