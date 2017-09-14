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

package org.cocome.tradingsystem.inventory.application.plant.parameter;

import org.cocome.tradingsystem.inventory.application.enterprise.parameter.IParameterTO;
import org.cocome.tradingsystem.inventory.application.plant.recipe.PlantOperationTO;

import javax.xml.bind.annotation.*;

/**
 * Abstract class of {@link IParameterTO} for {@link PlantOperationTO}
 *
 * @author Rudolf Biczok
 */
@XmlType(
        name = "PlantOperationParameterTO",
        namespace = "http://parameter.plant.application.inventory.tradingsystem.cocome.org")
@XmlRootElement(name = "PlantOperationParameterTO")
@XmlAccessorType(XmlAccessType.FIELD)
public class PlantOperationParameterTO implements IParameterTO {

    private static final long serialVersionUID = -2577328715744776645L;

    @XmlElement(name = "id", required = true)
    private long id;
    @XmlElement(name = "name", required = true)
    private String name;
    @XmlElement(name = "category", required = true)
    private String category;
    @XmlElement(name = "plantOperation", required = true)
    private PlantOperationTO plantOperation;

    /**
     * @return The id.
     */
    @Override
    public long getId() {
        return id;
    }

    /**
     * @param id Identifier value.
     */
    @Override
    public void setId(long id) {
        this.id = id;
    }

    /**
     * @return The parameter name
     */
    @Override
    public String getName() {
        return name;
    }

    /**
     * @param name The parameter name
     */
    @Override
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return The the parameter category
     */
    @Override
    public String getCategory() {
        return category;
    }

    /**
     * @param category The parameter category
     */
    @Override
    public void setCategory(String category) {
        this.category = category;
    }

    /**
     * @return the corresponding plant operation
     */
    public PlantOperationTO getPlantOperation() {
        return plantOperation;
    }

    /**
     * @param plantOperation the corresponding plant operation
     */
    public void setPlantOperation(PlantOperationTO plantOperation) {
        this.plantOperation = plantOperation;
    }
}
