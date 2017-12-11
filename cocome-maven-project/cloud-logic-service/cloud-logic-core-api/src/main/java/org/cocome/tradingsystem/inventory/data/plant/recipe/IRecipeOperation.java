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
import org.cocome.tradingsystem.inventory.data.plant.parameter.IParameter;
import org.cocome.tradingsystem.util.exception.NotInDatabaseException;

import java.util.Collection;

/**
 * Common interface for nestable recipes or atomic plant operations
 *
 * @author Rudolf Biczok
 */
public interface IRecipeOperation extends INameable {

    /**
     * @return all material classes that are required for operation execution
     */
    Collection<IEntryPoint> getInputEntryPoint() throws NotInDatabaseException;

    /**
     * @param inputMaterial all material classes that are required for operation execution
     */
    void setInputEntryPoint(Collection<IEntryPoint> inputMaterial);

    /**
     * @return all material classes that results after the operation execution
     */
    Collection<IEntryPoint> getOutputEntryPoint() throws NotInDatabaseException;

    /**
     * @param outputMaterial all material classes that results after the operation execution
     */
    void setOutputEntryPoint(Collection<IEntryPoint> outputMaterial);

    /**
     * @return the parameters of this operation
     */
    Collection<IParameter> getParameters() throws NotInDatabaseException;
}
