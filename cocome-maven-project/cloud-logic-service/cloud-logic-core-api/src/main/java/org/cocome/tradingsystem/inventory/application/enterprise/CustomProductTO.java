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

package org.cocome.tradingsystem.inventory.application.enterprise;

import org.cocome.tradingsystem.inventory.application.plant.recipe.RecipeTO;
import org.cocome.tradingsystem.inventory.application.store.ProductTO;

import javax.xml.bind.annotation.*;

/**
 * Transfer object of an customizable product
 *
 * @author Rudolf Biczok
 */
@XmlType(
        name = "CustomProductTO",
        namespace = "http://enterprise.application.inventory.tradingsystem.cocome.org")
@XmlRootElement(name = "CustomProductTO")
@XmlAccessorType(XmlAccessType.FIELD)
public class CustomProductTO extends ProductTO {
    private static final long serialVersionUID = 5315366349773650L;

    @XmlElement(name = "recipe", required = true)
    private RecipeTO recipe;

    /**
     * @return return the production recipe
     */
    public RecipeTO getRecipe() {
        return recipe;
    }

    /**
     * @param recipe the production recipe
     */
    public void setRecipe(RecipeTO recipe) {
        this.recipe = recipe;
    }

}
