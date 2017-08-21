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
     * Retrieves a specific plant belonging to this enterprise from the database.
     *
     * @param enterpriseID the unique identifier of a TradingEnterprise entity
     * @param plantName    the name of the target plant
     * @return A list of plants with the given name or an empty list, if none was found.
     */
    Collection<IPlant> queryPlantByName(
            long enterpriseID, String plantName);

    /**
     * Retrieves a specific plant belonging to this enterprise from the database.
     *
     * @param plantID the unique identifier of a Plant entity
     * @return A list of plants with the given name or an empty list, if none was found.
     * @throws NotInDatabaseException if no Plant could be found in the given enterprise
     */
    IPlant queryPlantById(long plantID) throws NotInDatabaseException;

    /**
     * Retrieves all plants belonging to this enterprise from the database.
     *
     * @param enterpriseID the unique identifier of a TradingEnterprise entity
     * @return All plants found in the given enterprise or an empty collection
     */
    Collection<IPlant> queryPlantsByEnterpriseId(
            long enterpriseID);

    /**
     * Retrieves a specific plant belonging to this enterprise from the database.
     *
     * @param enterpriseID the unique identifier of a TradingEnterprise entity
     * @param plantID      the unique identifier of the Store entity
     * @return The Plant if found
     * @throws NotInDatabaseException if no Plant could be found in the given enterprise
     */
    IPlant queryPlantByEnterprise(
            long enterpriseID, long plantID) throws NotInDatabaseException;
}
