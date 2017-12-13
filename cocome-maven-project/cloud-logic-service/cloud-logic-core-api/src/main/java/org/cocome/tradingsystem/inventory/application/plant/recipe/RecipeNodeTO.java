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

package org.cocome.tradingsystem.inventory.application.plant.recipe;

import org.cocome.tradingsystem.inventory.application.IIdentifiableTO;

import javax.xml.bind.annotation.*;
import java.io.Serializable;

/**
 * Represents the top-level recipe for producing a custom product.
 *
 * @author Rudolf Biczok
 */
@XmlType(
        name = "RecipeNodeTO",
        namespace = "http://recipe.plant.application.inventory.tradingsystem.cocome.org")
@XmlRootElement(name = "RecipeNodeTO")
@XmlAccessorType(XmlAccessType.FIELD)
public class RecipeNodeTO implements IIdentifiableTO, Serializable {
    private static final long serialVersionUID = 1L;

    @XmlElement(name = "id", required = true)
    private long id;
    @XmlElement(name = "recipe", required = true)
    private RecipeTO recipe;
    @XmlElementRef(name = "operation")
    private RecipeOperationTO operation;

    @Override
    public long getId() {
        return id;
    }

    @Override
    public void setId(long id) {
        this.id = id;
    }

    public RecipeTO getRecipe() {
        return recipe;
    }

    public void setRecipe(RecipeTO recipe) {
        this.recipe = recipe;
    }

    public RecipeOperationTO getOperation() {
        return operation;
    }

    public void setOperation(RecipeOperationTO operation) {
        this.operation = operation;
    }
}
