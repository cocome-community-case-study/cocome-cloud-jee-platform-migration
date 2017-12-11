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

import org.cocome.tradingsystem.inventory.application.plant.expression.MarkupInfo;
import org.cocome.tradingsystem.inventory.data.plant.IPlant;
import org.cocome.tradingsystem.inventory.data.plant.parameter.IParameter;
import org.cocome.tradingsystem.util.exception.NotInDatabaseException;

import java.util.Collection;

/**
 * Represents an operation provided by an plant
 *
 * @author Rudolf Biczok
 */
public interface IPlantOperation extends IRecipeOperation {

    /**
     * @return the plant that owns this production unit
     */
    IPlant getPlant() throws NotInDatabaseException;

    /**
     * @param plant the plant that owns this production unit
     */
    void setPlant(IPlant plant);

    /**
     * @return the id of the associated plant
     */
    long getPlantId();

    /**
     * @param plantId the id of the associated plant
     */
    void setPlantId(long plantId);

    /**
     * @return the markup describing the plant operation
     */
    MarkupInfo getMarkup();

    /**
     * @param markup the markup describing the plant operation
     */
    void setMarkup(MarkupInfo markup);

    Collection<IParameter> getParameters() throws NotInDatabaseException;

    /**
     * @param parameters the list of parameters needed for this plant operation
     */
    void setParameters(final Collection<IParameter> parameters);
}
