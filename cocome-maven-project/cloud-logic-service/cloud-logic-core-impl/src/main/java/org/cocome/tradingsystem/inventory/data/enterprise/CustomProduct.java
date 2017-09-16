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

import org.cocome.tradingsystem.inventory.application.plant.recipe.RecipeTO;
import org.cocome.tradingsystem.inventory.application.store.ProductTO;
import org.cocome.tradingsystem.inventory.data.plant.recipe.IRecipe;

import javax.enterprise.context.Dependent;
import javax.xml.bind.annotation.*;
import java.io.Serializable;

/**
 * Transfer object of an customizable product
 *
 * @author Rudolf Biczok
 */
@Dependent
public class CustomProduct extends AbstractProduct implements ICustomProduct {
    private static final long serialVersionUID = 1L;

    private IRecipe recipe;

    /**
     * @return return the production recipe
     */
    public IRecipe getRecipe() {
        return recipe;
    }

    /**
     * @param recipe the production recipe
     */
    public void setRecipe(IRecipe recipe) {
        this.recipe = recipe;
    }

}
