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

package org.cocome.tradingsystem.inventory.data.plant;

import org.cocome.tradingsystem.inventory.application.enterprise.CustomProductTO;
import org.cocome.tradingsystem.inventory.application.plant.PlantTO;
import org.cocome.tradingsystem.inventory.application.plant.productionunit.ProductionUnitClassTO;
import org.cocome.tradingsystem.inventory.data.enterprise.ICustomProduct;
import org.cocome.tradingsystem.inventory.data.plant.productionunit.IProductionUnitClass;
import org.cocome.tradingsystem.util.exception.NotInDatabaseException;

/**
 * Data conversion and creation utility for plant management
 * @author Rudolf Bictok
 */
public interface IPlantDataFactory {
    IPlant getNewPlant();

    IProductionUnitClass getNewProductionUnitClass();

    ICustomProduct getNewCustomProduct();

    IPlant convertToPlant(PlantTO plantTO);

    IProductionUnitClass convertToProductionUnitClass(ProductionUnitClassTO puc);

    PlantTO fillPlantTO(IPlant plant) throws NotInDatabaseException;

    ProductionUnitClassTO fillProductionUnitClassTO(IProductionUnitClass puc) throws NotInDatabaseException;

    CustomProductTO fillCustomProductTO(ICustomProduct product);
}
