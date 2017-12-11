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

package org.cocome.tradingsystem.inventory.data.plant.parameter;

import org.cocome.tradingsystem.inventory.data.INameable;
import org.cocome.tradingsystem.inventory.data.plant.recipe.IRecipeOperation;
import org.cocome.tradingsystem.util.exception.NotInDatabaseException;

/**
 * Interface for all types of parameters
 *
 * @author Rudolf Biczok
 */
public interface IParameter extends INameable {

    /**
     * @return The the parameter category
     */
    String getCategory();

    /**
     * @param category The parameter category
     */
    void setCategory(String category);

    /**
     * @return the corresponding recipe operation
     */
    IRecipeOperation getOperation() throws NotInDatabaseException;

    /**
     * @param operation the corresponding recipe operation
     */
    void setOperation(IRecipeOperation operation);

    long getOperationId();

    void setOperationId(long operation);

    /**
     * @param value the target parameter value
     * @return <code>true</code> if the given string is a valid value for this parameter
     */
    boolean isValidValue(String value);

}
