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

/**
 * Used as common interface for classes who connect two other entity types with each other
 *
 * @param <FromType> the type of the first interaction partner
 * @param <ToType>   the type of the second interaction partner
 * @author Rudolf Biczok
 */
public interface IInteractionEntity<
        FromType extends INameable,
        ToType extends INameable>
        extends IIdentifiable {

    /**
     * @return the first / source instance
     */
    FromType getFrom();

    /**
     * @param from the first / source instance
     */
    void setFrom(FromType from);

    /**
     * @return the second / destination instance
     */
    ToType getTo();

    /**
     * @param to the second / destination instance
     */
    void setTo(ToType to);
}
