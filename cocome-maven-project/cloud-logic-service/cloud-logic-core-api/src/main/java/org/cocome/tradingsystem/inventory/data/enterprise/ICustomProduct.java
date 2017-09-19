/*
 ****************************************************************************
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
 **************************************************************************
 */

package org.cocome.tradingsystem.inventory.data.enterprise;

import org.cocome.tradingsystem.inventory.data.plant.recipe.IRecipe;

/**
 * Represents a customizable product
 *
 * @author Rudolf Biczok
 */
public interface ICustomProduct extends IAbstractProduct {

    /**
     * @return return the production recipe
     */
    IRecipe getRecipe();

    /**
     * @param recipe the production recipe
     */
    void setRecipe(IRecipe recipe);

    /**
     * @return the id of the corresponding enterprise
     */
    long getRecipeId();

    /**
     * @param enterpriseId the id of the corresponding enterprise
     */
    void setRecipeId(long enterpriseId);

}
