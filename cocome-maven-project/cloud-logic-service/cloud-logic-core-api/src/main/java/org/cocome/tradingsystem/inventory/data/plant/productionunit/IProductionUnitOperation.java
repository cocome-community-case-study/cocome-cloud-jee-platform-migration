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

import org.cocome.tradingsystem.inventory.data.IIdentifiable;
import org.cocome.tradingsystem.util.exception.NotInDatabaseException;

/**
 * Represents an atomic operation on a production unit
 *
 * @author Rudolf Biczok
 */
public interface IProductionUnitOperation extends IIdentifiable {

    /**
     * @return The operation id unique to the production plant
     */
    String getOperationId();

    /**
     * @param operationId The operation id unique to the production plant
     */
    void setOperationId(String operationId);

    /**
     * @return the associated {@link IProductionUnitClass}
     */
    IProductionUnitClass getProductionUnitClass() throws NotInDatabaseException;

    /**
     * @param productionUnitClass the associated production unit class
     */
    void setProductionUnitClass(IProductionUnitClass productionUnitClass);


    /**
     * @return id of the associated {@link IProductionUnitClass}
     */
    long getProductionUnitClassId();

    /**
     * @param productionUnitClassId id of the associated production unit class
     */
    void setProductionUnitClassId(long productionUnitClassId);
}
