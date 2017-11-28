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
@XmlSeeAlso({BooleanPlantOperationParameterTO.class, NorminalPlantOperationParameterTO.class})
@XmlRootElement(name = "PlantOperationParameterTO")
@XmlAccessorType(XmlAccessType.FIELD)
public abstract class PlantOperationParameterTO implements IParameterTO {

    private static final long serialVersionUID = -2577328715744776645L;

    @XmlElement(name = "id", required = true)
    private long id;
    @XmlElement(name = "name", required = true)
    private String name;
    @XmlElement(name = "category", required = true)
    private String category;

    @Override
    public long getId() {
        return id;
    }

    @Override
    public void setId(long id) {
        this.id = id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getCategory() {
        return category;
    }

    @Override
    public void setCategory(String category) {
        this.category = category;
    }
}
