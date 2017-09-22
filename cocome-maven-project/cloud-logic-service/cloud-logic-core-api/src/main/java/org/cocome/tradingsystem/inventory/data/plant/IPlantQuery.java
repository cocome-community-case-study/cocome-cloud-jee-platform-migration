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

import org.cocome.tradingsystem.inventory.data.plant.productionunit.IProductionUnitClass;
import org.cocome.tradingsystem.inventory.data.plant.productionunit.IProductionUnitOperation;
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
     * Retrieves all production unit classes belonging to this enterprise from the database.
     *
     * @param enterpriseID the unique identifier of a TradingEnterprise entity
     * @return All {@link IProductionUnitClass} found in the given enterprise or an empty collection
     */
    Collection<IProductionUnitClass> queryProductionUnitClassesByEnterpriseId(long enterpriseID);

    /**
     * Retrieves a specific {@link IProductionUnitClass} belonging to this enterprise from the database.
     *
     * @param productionUnitClassID the unique identifier of the {@link IProductionUnitClass} entity
     * @return The {@link IProductionUnitClass} if found
     * @throws NotInDatabaseException if no {@link IProductionUnitClass} could be found in the given enterprise
     */
    IProductionUnitClass queryProductionUnitClass(long productionUnitClassID) throws NotInDatabaseException;

    /**
     * Retrieves all production unit operations belonging to this enterprise from the database.
     *
     * @param enterpriseID the unique identifier of a TradingEnterprise entity
     * @return All {@link IProductionUnitOperation} found in the given enterprise or an empty collection
     */
    Collection<IProductionUnitOperation> queryProductionUnitOperationsByEnterpriseId(long enterpriseID);

    /**
     * Retrieves a specific {@link IProductionUnitClass} belonging to this enterprise from the database.
     *
     * @param productionUnitOperationId the unique identifier of the {@link IProductionUnitOperation} entity
     * @return The {@link IProductionUnitOperation} if found
     * @throws NotInDatabaseException if no {@link IProductionUnitOperation} could be found in the given enterprise
     */
    IProductionUnitOperation queryProductionUnitOperation(long productionUnitOperationId) throws NotInDatabaseException;

}
