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

import org.cocome.tradingsystem.inventory.application.IIdentifiableTO;
import org.cocome.tradingsystem.inventory.application.plant.PlantTO;

import javax.xml.bind.annotation.*;

/**
 * This class represents a Product in the database
 *
 * @author Rudolf Biczok
 */
@XmlType(
        name = "ProductionUnitTO",
        namespace = "http://productionunit.plant.application.inventory.tradingsystem.cocome.org")
@XmlRootElement(name = "ProductionUnitTO")
@XmlAccessorType(XmlAccessType.FIELD)
public class ProductionUnitTO implements IIdentifiableTO {

    private static final long serialVersionUID = -2577328715744776645L;

    @XmlElement(name = "id", required = true)
    private long id;
    @XmlElement(name = "location", required = true)
    private String location;
    @XmlElement(name = "doubleFlag", required = true)
    private boolean doubleFlag;
    @XmlElement(name = "interfaceUrl", required = true)
    private String interfaceUrl;
    @XmlElement(name = "plant", required = true)
    private PlantTO plant;
    @XmlElement(name = "productionUnitClass", required = true)
    private ProductionUnitClassTO productionUnitClass;

    /**
     * Gets identifier value
     *
     * @return The id.
     */
    @Override
    public long getId() {
        return id;
    }

    /**
     * Sets identifier.
     *
     * @param id Identifier value.
     */
    @Override
    public void setId(long id) {
        this.id = id;
    }

    /**
     * @return Production unit location.
     */
    public String getLocation() {
        return this.location;
    }

    /**
     * @param location Production unit location
     */
    public void setLocation(final String location) {
        this.location = location;
    }

    /**
     * @return The URL location used to communicate with the device
     */
    public String getInterfaceUrl() {
        return this.interfaceUrl;
    }

    /**
     * @param interfaceUrl The URL location used to communicate with the device
     */
    public void setInterfaceUrl(final String interfaceUrl) {
        this.interfaceUrl = interfaceUrl;
    }

    /**
     * @return <code>true</code> if this production unit has no real interface,
     * but is supposed to be simulated by a double
     */
    public boolean isDouble() {
        return doubleFlag;
    }

    /**
     * @param doubleFlag the double flag used to determine if this unit is a dummy system
     */
    public void setDouble(final boolean doubleFlag) {
        this.doubleFlag = doubleFlag;
    }

    /**
     * @return the production unit class
     */
    public ProductionUnitClassTO getProductionUnitClass() {
        return productionUnitClass;
    }

    /**
     * @param productionUnitClass the production unit class
     */
    public void setProductionUnitClass(ProductionUnitClassTO productionUnitClass) {
        this.productionUnitClass = productionUnitClass;
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
}
