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

package org.cocome.tradingsystem.inventory.data.plant.productionunit;

import org.cocome.tradingsystem.inventory.data.IIdentifiable;
import org.cocome.tradingsystem.inventory.data.plant.IPlant;
import org.cocome.tradingsystem.util.exception.NotInDatabaseException;

/**
 * This class represents a Product in the database
 *
 * @author Rudolf Biczok
 */
public interface IProductionUnit extends IIdentifiable {

    /**
     * @return Production unit location.
     */
    String getLocation();

    /**
     * @param location Production unit location
     */
    void setLocation(final String location);

    /**
     * @return The URL location used to communicate with the device
     */
    String getInterfaceUrl();

    /**
     * @param interfaceUrl The URL location used to communicate with the device
     */
    void setInterfaceUrl(final String interfaceUrl);

    /**
     * @return <code>true</code> if this production unit has no real interface,
     * but is supposed to be simulated by a double
     */
    boolean isDouble();

    /**
     * @param doubleFlag the double flag used to determine if this unit is a dummy system
     */
    void setDouble(final boolean doubleFlag);

    /**
     * @return the production unit class
     */
    IProductionUnitClass getProductionUnitClass() throws NotInDatabaseException;

    /**
     * @param productionUnitClass the production unit class
     */
    void setProductionUnitClass(IProductionUnitClass productionUnitClass);

    /**
     * @return the plant that owns this production unit
     */
    IPlant getPlant() throws NotInDatabaseException;

    /**
     * @param plant the plant that owns this production unit
     */
    void setPlant(IPlant plant);

    /**
     * @return the id of the plant
     */
    long getPlantId();

    /**
     * @param plantId the id of the plant
     */
    void setPlantId(long plantId);

    /**
     * @return the id of the production unit class
     */
    long getProductionUnitClassId();

    /**
     * @param productionUnitClassId the id of the production unit class
     */
    void setProductionUnitClassId(long productionUnitClassId);
}
