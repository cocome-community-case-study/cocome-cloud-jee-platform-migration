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

import javax.ejb.CreateException;

/**
 * Interface for classes that handles Create, Update, Delete operations by directly calling the
 * with service adapter module
 *
 * @author Rudolf Biczok
 */
public interface IPersistence<T> {
    /**
     * Stores a new entity to the database.
     *
     * @param entity the entity to create
     * @throws CreateException if creation failed
     */
    void createEntity(T entity) throws CreateException;

    /**
     * Updates an entity with the new values given.
     *
     * @param entity the entity to update
     * @throws UpdateException if update failed
     */
    void updateEntity(T entity) throws UpdateException;

    /**
     * Deletes the entity from the database
     *
     * @param entity the entity to delete
     * @throws UpdateException if deletion failed
     */
    void deleteEntity(T entity) throws UpdateException;

}
