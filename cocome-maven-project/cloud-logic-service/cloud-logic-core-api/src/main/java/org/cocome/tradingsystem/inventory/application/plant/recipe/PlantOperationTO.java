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

import org.cocome.tradingsystem.inventory.application.plant.PlantTO;
import org.cocome.tradingsystem.inventory.application.plant.expression.MarkupInfo;

import javax.xml.bind.annotation.*;
import java.util.Objects;

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

    @XmlElement(name = "markup", required = true)
    private MarkupInfo markup;

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
     * @return rdescribing the internals of the plant operation
     */
    public MarkupInfo getMarkup() {
        return markup;
    }

    /**
     * @param markup the markup describing the internals of the plant operation
     */
    public void setMarkup(MarkupInfo markup) {
        this.markup = markup;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PlantOperationTO that = (PlantOperationTO) o;
        return Objects.equals(plant, that.plant)
                && Objects.equals(getName(), that.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(plant, getName());
    }
}
