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

package org.cocome.tradingsystem.inventory.data.plant.productionunit;

import org.cocome.tradingsystem.inventory.data.INameable;
import org.cocome.tradingsystem.inventory.data.plant.IPlant;
import org.cocome.tradingsystem.util.exception.NotInDatabaseException;

/**
 * Represents a class of production unity utilizing a specific set of {@link IProductionUnitOperation}
 *
 * @author Rudolf Biczok
 */
public interface IProductionUnitClass extends INameable {

    /**
     * @return The enterprise which the Plant belongs to
     */
    IPlant getPlant() throws NotInDatabaseException;

    /**
     * @param plant the plant where this class of production units belongs to
     */
    void setPlant(final IPlant plant);

    /**
     * @return the id of the corresponding plant
     */
    long getPlantId();

    /**
     * @param plantId the id of the corresponding plant
     */
    void setPlantId(long plantId);

}
