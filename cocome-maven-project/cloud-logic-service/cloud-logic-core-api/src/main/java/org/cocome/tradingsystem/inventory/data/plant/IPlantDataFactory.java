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

import org.cocome.tradingsystem.inventory.application.enterprise.parameter.CustomProductParameterValueTO;
import org.cocome.tradingsystem.inventory.application.plant.parameter.BooleanPlantOperationParameterTO;
import org.cocome.tradingsystem.inventory.application.plant.parameter.NorminalPlantOperationParameterTO;
import org.cocome.tradingsystem.inventory.application.plant.parameter.PlantOperationParameterTO;
import org.cocome.tradingsystem.inventory.application.plant.productionunit.ProductionUnitClassTO;
import org.cocome.tradingsystem.inventory.application.plant.productionunit.ProductionUnitOperationTO;
import org.cocome.tradingsystem.inventory.application.plant.productionunit.ProductionUnitTO;
import org.cocome.tradingsystem.inventory.application.plant.recipe.*;
import org.cocome.tradingsystem.inventory.data.enterprise.parameter.ICustomProductParameterValue;
import org.cocome.tradingsystem.inventory.data.plant.parameter.IBooleanPlantOperationParameter;
import org.cocome.tradingsystem.inventory.data.plant.parameter.INorminalPlantOperationParameter;
import org.cocome.tradingsystem.inventory.data.plant.parameter.IPlantOperationParameter;
import org.cocome.tradingsystem.inventory.data.plant.productionunit.IProductionUnit;
import org.cocome.tradingsystem.inventory.data.plant.productionunit.IProductionUnitClass;
import org.cocome.tradingsystem.inventory.data.plant.productionunit.IProductionUnitOperation;
import org.cocome.tradingsystem.inventory.data.plant.recipe.*;
import org.cocome.tradingsystem.util.exception.NotInDatabaseException;

/**
 * Data conversion and creation utility for plant management
 *
 * @author Rudolf Bictok
 */
public interface IPlantDataFactory {
    /* Production Unit Operation */

    IProductionUnitClass getNewProductionUnitClass();

    IProductionUnitClass convertToProductionUnitClass(ProductionUnitClassTO productionUnitClassTO);

    ProductionUnitClassTO fillProductionUnitClassTO(IProductionUnitClass puc) throws NotInDatabaseException;

    /* Production Unit Operation */

    IProductionUnitOperation getNewProductionUnitOperation();

    IProductionUnitOperation convertToProductionUnitOperation(ProductionUnitOperationTO productionUnitOperationTO);

    ProductionUnitOperationTO fillProductionUnitOperationTO(IProductionUnitOperation operation)
            throws NotInDatabaseException;

    /* Production Unit */

    IProductionUnit getNewProductionUnit();

    IProductionUnit convertToProductionUnit(ProductionUnitTO productionUnitTO);

    ProductionUnitTO fillProductionUnitTO(IProductionUnit operation)
            throws NotInDatabaseException;

    /* Plant Operation */

    IPlantOperation getNewPlantOperation();

    IPlantOperation convertToPlantOperation(PlantOperationTO plantOperationTO);

    PlantOperationTO fillPlantOperationTO(IPlantOperation iPlantOperation)
            throws NotInDatabaseException;

    /* Plant Operation Parameter */

    PlantOperationParameterTO fillPlantOperationParameterTO(IPlantOperationParameter parameter)
            throws NotInDatabaseException;

    IPlantOperationParameter convertToPlantOperationParameter(PlantOperationParameterTO plantOperationParameterTO);

    /* Boolean Plant Operation Parameter */

    IBooleanPlantOperationParameter getNewBooleanPlantOperationParameter();

    BooleanPlantOperationParameterTO fillBooleanPlantOperationParameterTO(
            IBooleanPlantOperationParameter booleanPlantOperationParameter)
            throws NotInDatabaseException;

    IBooleanPlantOperationParameter convertToBooleanPlantOperationParameter(
            BooleanPlantOperationParameterTO booleanPlantOperationParameterTO);

    /* Norminal Plant Operation Parameter */

    INorminalPlantOperationParameter getNewNorminalPlantOperationParameter();

    NorminalPlantOperationParameterTO fillNorminalPlantOperationParameterTO(
            INorminalPlantOperationParameter norminalPlantOperationParameter)
            throws NotInDatabaseException;

    INorminalPlantOperationParameter convertToNorminalPlantOperationParameter(
            NorminalPlantOperationParameterTO norminalPlantOperationParameterTO);

    /* Entry Point Interaction */

    IEntryPointInteraction getNewEntryPointInteraction();

    EntryPointInteractionTO fillEntryPointInteractionTO(
            IEntryPointInteraction entryPointInteraction)
            throws NotInDatabaseException;

    IEntryPointInteraction convertToEntryPointInteraction(
            EntryPointInteractionTO entryPointInteractionTO);

    /* Parameter Interaction */

    IParameterInteraction getNewParameterInteraction();

    ParameterInteractionTO fillParameterInteractionTO(IParameterInteraction parameterInteraction)
            throws NotInDatabaseException;

    IParameterInteraction convertToParameterInteraction(ParameterInteractionTO parameterInteractionTO);

    /* Recipe */

    IRecipe getNewRecipe();

    RecipeTO fillRecipeTO(IRecipe recipe) throws NotInDatabaseException;

    IRecipe convertToRecipe(RecipeTO recipeTO);

    /* Plant Operation Order */

    IPlantOperationOrder getNewPlantOperationOrder();

    PlantOperationOrderTO fillPlantOperationOrderTO(IPlantOperationOrder plantOperationOrder) throws NotInDatabaseException;

    IPlantOperationOrder convertToPlantOperationOrder(PlantOperationOrderTO plantOperationOrderTO) throws NotInDatabaseException;

    /* Plant Operation Order Entry */

    IPlantOperationOrderEntry getNewPlantOperationOrderEntry();

    PlantOperationOrderEntryTO fillPlantOperationOrderEntryTO(IPlantOperationOrderEntry plantOperationOrderEntry) throws NotInDatabaseException;

    IPlantOperationOrderEntry convertToPlantOperationOrderEntry(PlantOperationOrderEntryTO plantOperationOrderEntryTO) throws NotInDatabaseException;

    /* Plant Operation Order Parameter Value */

    IPlantOperationParameterValue getNewPlantOperationParameterValue();

    PlantOperationParameterValueTO fillPlantOperationParameterValueTO(IPlantOperationParameterValue plantOperationParameterValue)
            throws NotInDatabaseException;

    IPlantOperationParameterValue convertToPlantOperationParameterValue(PlantOperationParameterValueTO plantOperationParameterValueTO);

        /* Production Order */

    IProductionOrder getNewProductionOrder();

    ProductionOrderTO fillProductionOrderTO(IProductionOrder productionOrder) throws NotInDatabaseException;

    IProductionOrder convertToProductionOrder(ProductionOrderTO productionOrderTO) throws NotInDatabaseException;

    /* Production Order Entry */

    IProductionOrderEntry getNewProductionOrderEntry();

    ProductionOrderEntryTO fillProductionOrderEntryTO(IProductionOrderEntry productionOrderEntry) throws NotInDatabaseException;

    IProductionOrderEntry convertToProductionOrderEntry(ProductionOrderEntryTO productionOrderEntryTO) throws NotInDatabaseException;

    /* Plant Operation Order Parameter Value */

    ICustomProductParameterValue getNewCustomProductParameterValue();

    CustomProductParameterValueTO fillCustomProductParameterValueTO(ICustomProductParameterValue customProductParameterValue)
            throws NotInDatabaseException;

    ICustomProductParameterValue convertToCustomProductParameterValue(CustomProductParameterValueTO customProductParameterValueTO);
}
