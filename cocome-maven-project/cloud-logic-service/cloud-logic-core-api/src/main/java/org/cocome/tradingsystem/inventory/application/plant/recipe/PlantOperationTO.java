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
public class PlantOperationTO implements Serializable, INameableTO {
    private static final long serialVersionUID = 1L;

    @XmlElement(name = "id", required = true)
    private long id;
    @XmlElement(name = "name", required = true)
    private String name;
    @XmlElement(name = "plant", required = true)
    private PlantTO plant;

    @XmlElement(name = "expressions", required = true)
    private List<ExpressionTO> expressions;
    @XmlElement(name = "inputEntryPoint", required = true)
    private Collection<EntryPointTO> inputEntryPoint;
    @XmlElement(name = "outputEntryPoint", required = true)
    private Collection<EntryPointTO> outputEntryPoint;

    /**
     * @return A unique identifier of this Plant.
     */
    @Override
    public long getId() {
        return this.id;
    }

    /**
     * @param id a unique identifier of this Plant
     */
    @Override
    public void setId(final long id) {
        this.id = id;
    }

    /**
     * Returns the name of the Plant.
     *
     * @return Plant name.
     */
    @Override
    public String getName() {
        return this.name;
    }

    /**
     * @param name the name of the Plant
     */
    @Override
    public void setName(final String name) {
        this.name = name;
    }

    /**
     * @return all material classes that are required for operation execution
     */
    public Collection<EntryPointTO> getInputEntryPoint() {
        return inputEntryPoint;
    }

    /**
     * @param inputMaterial all material classes that are required for operation execution
     */
    public void setInputEntryPoint(Collection<EntryPointTO> inputMaterial) {
        this.inputEntryPoint = inputMaterial;
    }

    /**
     * @return all material classes that results after the operation execution
     */
    public Collection<EntryPointTO> getOutputEntryPoint() {
        return outputEntryPoint;
    }

    /**
     * @param outputMaterial all material classes that results after the operation execution
     */
    public void setOutputEntryPoint(Collection<EntryPointTO> outputMaterial) {
        this.outputEntryPoint = outputMaterial;
    }

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
