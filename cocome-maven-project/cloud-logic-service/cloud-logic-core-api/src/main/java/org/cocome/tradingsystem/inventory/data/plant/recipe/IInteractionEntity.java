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
import org.cocome.tradingsystem.util.exception.NotInDatabaseException;

/**
 * Used as common interface for classes who connect two other entity types with each other
 *
 * @param <T> the type of the interaction element
 * @author Rudolf Biczok
 */
public interface IInteractionEntity<
        T extends INameable>
        extends IIdentifiable {

    T getFrom() throws NotInDatabaseException;

    void setFrom(T from);

    long getFromId();

    void setFromId(long from);

    T getTo() throws NotInDatabaseException;

    void setTo(T to);

    long getToId();

    void setToId(long to);

    IRecipe getRecipe() throws NotInDatabaseException;

    void setRecipe(IRecipe recipe);

    long getRecipeId();

    void setRecipeId(long recipeId);
}
