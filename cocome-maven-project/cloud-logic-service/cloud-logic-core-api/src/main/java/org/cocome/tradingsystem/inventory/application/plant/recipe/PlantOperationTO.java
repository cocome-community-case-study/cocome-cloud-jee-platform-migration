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

import org.cocome.tradingsystem.inventory.application.INameableTO;
import org.cocome.tradingsystem.inventory.application.plant.PlantTO;
import org.cocome.tradingsystem.inventory.application.plant.expression.ExpressionTO;

import javax.xml.bind.annotation.*;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * Represents an operation provided by an plant
 *
 * @author Rudolf Biczok
 */
@XmlType(
        name = "PlantOperationTO",
        namespace = "http://recipe.plant.application.inventory.tradingsystem.cocome.org")
@XmlRootElement(name = "PlantOperationTO")
@XmlAccessorType(XmlAccessType.FIELD)
public class PlantOperationTO extends RecipeOperationTO {
    private static final long serialVersionUID = 1L;

    @XmlElement(name = "plant", required = true)
    private PlantTO plant;

    @XmlElement(name = "expressions", required = true)
    private List<ExpressionTO> expressions;

    /**
     * @return the plant that owns this production unit
     */
    public PlantTO getPlant() {
        return plant;
    }

    /**
     * @param plant the plant that owns this production unit
     */
    public void setPlant(PlantTO plant) {
        this.plant = plant;
    }

    /**
     * @return returns the list of expressions used to execute this operation
     */
    public List<ExpressionTO> getExpressions() {
        return expressions;
    }

    /**
     * @param expressions the list of expressions used to execute this operation
     */
    public void setExpressions(List<ExpressionTO> expressions) {
        this.expressions = expressions;
    }

}
