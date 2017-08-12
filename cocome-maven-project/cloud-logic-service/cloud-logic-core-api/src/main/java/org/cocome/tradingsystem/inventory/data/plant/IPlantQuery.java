/***************************************************************************
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
 ***************************************************************************/

package org.cocome.tradingsystem.inventory.data.plant;

import org.cocome.tradingsystem.util.exception.NotInDatabaseException;

import javax.ejb.Local;

/**
 * This interface provides methods for querying the database.
 *
 * @author Rudolf Biczok
 */
@Local
public interface IPlantQuery {

    /**
     * Queries the database for a {@link IPlant} entity with given name and location.
     * If more than one entity match these criteria, the first match will be returned.
     *
     * @param name     plant name
     * @param location plant location, may contain wildcard characters and may be empty.
     * @return {@link IPlant} entity or {@code null} if there is no plant with
     * the given name and location.
     */
    IPlant queryPlant(String name, String location);

    /**
     * Returns a {@link IPlant} entity corresponding to the given unique identifier.
     *
     * @param storeId unique identifier of a {@link IPlant} entity
     * @return {@link IPlant} entity
     * @throws NotInDatabaseException if a plant with the given id could not be found
     */
    IPlant queryPlantById(long storeId) throws NotInDatabaseException;

}
