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

import org.cocome.tradingsystem.inventory.data.INameable;

import java.io.Serializable;

/**
 * Used as common interface for classes who connect two other entity types with each other
 *
 * @param <T> the interaction type
 * @author Rudolf Biczok
 */
public abstract class InteractionEntity<
        T extends INameable>
        implements Serializable, IInteractionEntity<T> {
    private static final long serialVersionUID = 1L;

    long id;
    long fromId;
    long toId;
    long recipeId;

    @Override
    public long getId() {
        return this.id;
    }

    @Override
    public void setId(final long id) {
        this.id = id;
    }

    @Override
    public long getFromId() {
        return fromId;
    }

    @Override
    public void setFromId(long fromId) {
        this.fromId = fromId;
    }

    @Override
    public long getToId() {
        return toId;
    }

    @Override
    public void setToId(long toId) {
        this.toId = toId;
    }

    @Override
    public long getRecipeId() {
        return recipeId;
    }

    @Override
    public void setRecipeId(long recipeId) {
        this.recipeId = recipeId;
    }
}
