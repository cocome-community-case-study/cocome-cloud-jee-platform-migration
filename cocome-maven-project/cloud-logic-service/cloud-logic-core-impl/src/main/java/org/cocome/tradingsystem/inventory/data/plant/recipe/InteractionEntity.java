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

import org.cocome.tradingsystem.inventory.data.IIdentifiable;
import org.cocome.tradingsystem.inventory.data.INameable;

import javax.enterprise.context.Dependent;
import java.io.Serializable;

/**
 * Used as common interface for classes who connect two other entity types with each other
 *
 * @param <FromType> the type of the first interaction partner
 * @param <ToType>   the type of the second interaction partner
 * @author Rudolf Biczok
 */
public abstract class InteractionEntity<
        FromType extends INameable,
        ToType extends INameable>
        implements Serializable, IIdentifiable {
    private static final long serialVersionUID = 1L;

    private long id;
    private FromType from;
    private ToType to;

    /**
     * @return the database id
     */
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
     * @return the first / source instance
     */
    public FromType getFrom() {
        return from;
    }

    /**
     * @param from the first / source instance
     */
    public void setFrom(FromType from) {
        this.from = from;
    }

    /**
     * @return the second / destination instance
     */
    public ToType getTo() {
        return to;
    }

    /**
     * @param to the second / destination instance
     */
    public void setTo(ToType to) {
        this.to = to;
    }
}
