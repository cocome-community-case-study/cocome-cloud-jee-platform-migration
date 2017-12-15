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

package org.cocome.tradingsystem.inventory.application.plant.productionunit;

import org.cocome.tradingsystem.inventory.application.INameableTO;
import org.cocome.tradingsystem.inventory.application.plant.PlantTO;

import javax.xml.bind.annotation.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * Represents a class of production unity utilizing a specific set of {@link ProductionUnitOperationTO}
 *
 * @author Rudolf Biczok
 */
@XmlType(
        name = "ProductionUnitClassTO",
        namespace = "http://productionunit.plant.application.inventory.tradingsystem.cocome.org")
@XmlRootElement(name = "ProductionUnitClassTO")
@XmlAccessorType(XmlAccessType.FIELD)
public class ProductionUnitClassTO implements Serializable, INameableTO {

    private static final long serialVersionUID = -2577328715744776645L;

    @XmlElement(name = "id", required = true)
    private long id;
    @XmlElement(name = "name", required = true)
    private String name;
    @XmlElement(name = "plant", required = true)
    private PlantTO plant;

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
    public void setId(long id) {
        this.id = id;
    }

    /**
     * @return The name of the product
     */
    public String getName() {
        return name;
    }

    /**
     * @param name The name of the product
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return The plant where this class of production units belongs to
     */
    public PlantTO getPlant() {
        return this.plant;
    }

    /**
     * @param plant The plant where this class of production units belongs to
     */
    public void setPlant(final PlantTO plant) {
        this.plant = plant;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProductionUnitClassTO that = (ProductionUnitClassTO) o;
        return Objects.equals(name, that.name) &&
                Objects.equals(plant, that.plant);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, plant);
    }
}
