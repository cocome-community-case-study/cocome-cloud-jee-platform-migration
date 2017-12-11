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

package org.cocome.tradingsystem.inventory.data.plant;

import org.cocome.tradingsystem.inventory.data.plant.productionunit.IProductionUnit;
import org.cocome.tradingsystem.inventory.data.plant.productionunit.IProductionUnitClass;
import org.cocome.tradingsystem.inventory.data.plant.productionunit.IProductionUnitOperation;
import org.cocome.tradingsystem.inventory.data.plant.recipe.IParameterValue;
import org.cocome.tradingsystem.inventory.data.plant.recipe.IPlantOperationOrder;
import org.cocome.tradingsystem.inventory.data.plant.recipe.IPlantOperationOrderEntry;
import org.cocome.tradingsystem.util.exception.NotInDatabaseException;

import javax.ejb.Local;
import java.util.Collection;

/**
 * This interface provides methods for querying the database.
 *
 * @author Rudolf Biczok
 */
@Local
public interface IPlantQuery {

    /**
     * Retrieves all production unit classes belonging to this plant from the database.
     *
     * @param plantID the unique identifier of a {@link IPlant} entity
     * @return All {@link IProductionUnitClass} found in the given enterprise or an empty collection
     */
    Collection<IProductionUnitClass> queryProductionUnitClassesByPlantId(long plantID);

    /**
     * Retrieves a specific {@link IProductionUnitClass} belonging to this enterprise from the database.
     *
     * @param productionUnitClassID the unique identifier of the {@link IProductionUnitClass} entity
     * @return The {@link IProductionUnitClass} if found
     * @throws NotInDatabaseException if no {@link IProductionUnitClass} could be found in the given enterprise
     */
    IProductionUnitClass queryProductionUnitClass(long productionUnitClassID) throws NotInDatabaseException;

    /**
     * Retrieves all production unit operations belonging to the given unit class from the database.
     *
     * @param productionUnitClassID the unique identifier of a {@link IProductionUnitClass} entity
     * @return All {@link IProductionUnitOperation} found in the given unit class or an empty collection
     */
    Collection<IProductionUnitOperation> queryProductionUnitOperationsByProductionUnitClassId(long productionUnitClassID);

    /**
     * Retrieves a specific {@link IProductionUnitClass} belonging to this enterprise from the database.
     *
     * @param productionUnitOperationId the unique identifier of the {@link IProductionUnitOperation} entity
     * @return The {@link IProductionUnitOperation} if found
     * @throws NotInDatabaseException if no {@link IProductionUnitOperation} could be found in the given enterprise
     */
    IProductionUnitOperation queryProductionUnitOperation(long productionUnitOperationId) throws NotInDatabaseException;

    /**
     * @param orderId the id of the plant operation order
     * @return the plant operation order of the corresponding database id
     * @throws NotInDatabaseException if the order does not exist
     */
    IPlantOperationOrder queryPlantOperationOrderById(long orderId) throws NotInDatabaseException;

    /**
     * @param orderEntryId the id of the order entry
     * @return the order entry of the corresponding database id
     * @throws NotInDatabaseException if the order does not exist
     */
    IPlantOperationOrderEntry queryPlantOperationOrderEntryById(long orderEntryId) throws NotInDatabaseException;

    /**
     * @param paramValueId the id of the parameter value
     * @return the parameter value of the corresponding database id
     * @throws NotInDatabaseException if the parameter value does not exist
     */
    IParameterValue queryParameterValueById(long paramValueId)
            throws NotInDatabaseException;

    /**
     * @param productionUnitId the id of the production unit
     * @return the production unit of the corresponding database id
     * @throws NotInDatabaseException if the production unit does not exist
     */
    IProductionUnit queryProductionUnit(long productionUnitId) throws NotInDatabaseException;

    /**
     * @param plantId the id of the plant
     * @return all production units that belong to the given plant
     * @throws NotInDatabaseException if the plant id does not exist in the database
     */
    Collection<IProductionUnit> queryProductionUnits(long plantId) throws NotInDatabaseException;
}
