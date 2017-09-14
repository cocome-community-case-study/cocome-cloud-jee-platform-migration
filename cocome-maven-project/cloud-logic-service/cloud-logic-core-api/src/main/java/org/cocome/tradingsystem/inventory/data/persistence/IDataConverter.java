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

package org.cocome.tradingsystem.inventory.data.persistence;

import org.cocome.tradingsystem.util.exception.NotInDatabaseException;

/**
 * Interface for utility classes that converts data objects to transfer objects in both direction.
 *
 * @param <T1> the data object
 * @param <T2> the transfer object
 * @author Rudolf Bictok
 */
public interface IDataConverter<T1, T2> {
    /**
     * Converts the transfer object to a data object
     *
     * @param transferObj the transfer object
     * @return the converted data object
     */
    T1 convertFromTO(T2 transferObj);

    /**
     * Converts the data object to a transfer object
     *
     * @param obj the data object
     * @return the converted transfer object
     * @throws NotInDatabaseException if the object refers to data that does not exist in the database
     */
    T2 convertToTO(T1 obj) throws NotInDatabaseException;
}
