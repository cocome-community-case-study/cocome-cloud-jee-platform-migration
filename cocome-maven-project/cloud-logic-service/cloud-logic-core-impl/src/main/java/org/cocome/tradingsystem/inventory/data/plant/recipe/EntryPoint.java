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

package org.cocome.tradingsystem.inventory.data.plant.recipe;

import java.io.Serializable;

/**
 * Used to connection ports between {@link IPlantOperation}
 */
public class EntryPoint implements Serializable, IEntryPoint {
    private static final long serialVersionUID = 1L;

    private long id;
    private String name;

    /**
     * @return the database id
     */
    @Override
    public long getId() {
        return this.id;
    }

    /**
     * @param id the database id
     */
    @Override
    public void setId(final long id) {
        this.id = id;
    }

    /**
     * Returns the name of the Plant.
     *
     * @return Plant name.
     */
    @Override
    public String getName() {
        return this.name;
    }

    /**
     * @param name the name of the Plant
     */
    @Override
    public void setName(final String name) {
        this.name = name;
    }

}
