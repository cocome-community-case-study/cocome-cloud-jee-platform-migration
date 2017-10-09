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

import org.cocome.tradingsystem.inventory.application.plant.expression.ConditionalExpressionTO;
import org.cocome.tradingsystem.inventory.application.plant.parameter.BooleanPlantOperationParameterTO;
import org.cocome.tradingsystem.inventory.application.plant.parameter.NorminalPlantOperationParameterTO;
import org.cocome.tradingsystem.inventory.application.plant.parameter.PlantOperationParameterTO;
import org.cocome.tradingsystem.inventory.application.plant.productionunit.ProductionUnitClassTO;
import org.cocome.tradingsystem.inventory.application.plant.productionunit.ProductionUnitOperationTO;
import org.cocome.tradingsystem.inventory.application.plant.recipe.EntryPointInteractionTO;
import org.cocome.tradingsystem.inventory.application.plant.recipe.ParameterInteractionTO;
import org.cocome.tradingsystem.inventory.application.plant.recipe.PlantOperationTO;
import org.cocome.tradingsystem.inventory.application.plant.recipe.RecipeTO;
import org.cocome.tradingsystem.inventory.data.plant.expression.IConditionalExpression;
import org.cocome.tradingsystem.inventory.data.plant.parameter.IBooleanPlantOperationParameter;
import org.cocome.tradingsystem.inventory.data.plant.parameter.INorminalPlantOperationParameter;
import org.cocome.tradingsystem.inventory.data.plant.parameter.IPlantOperationParameter;
import org.cocome.tradingsystem.inventory.data.plant.productionunit.IProductionUnitClass;
import org.cocome.tradingsystem.inventory.data.plant.productionunit.IProductionUnitOperation;
import org.cocome.tradingsystem.inventory.data.plant.recipe.IEntryPointInteraction;
import org.cocome.tradingsystem.inventory.data.plant.recipe.IParameterInteraction;
import org.cocome.tradingsystem.inventory.data.plant.recipe.IPlantOperation;
import org.cocome.tradingsystem.inventory.data.plant.recipe.IRecipe;
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

    ProductionUnitOperationTO fillProductionUnitOperationTO(IProductionUnitOperation operation) throws NotInDatabaseException;

    /* Conditional Expression */

    IConditionalExpression getNewConditionalExpression();

    IConditionalExpression convertToConditionalExpression(ConditionalExpressionTO conditionalExpressionTO);

    ConditionalExpressionTO fillConditionalExpressionTO(IConditionalExpression conditionalExpression) throws NotInDatabaseException;

    /* Plant Operation */

    IPlantOperation getNewPlantOperation();

    IPlantOperation convertToPlantOperation(PlantOperationTO plantOperationTO);

    PlantOperationTO fillPlantOperationTO(IPlantOperation iPlantOperation)
            throws NotInDatabaseException;

    /* Plant Operation Parameter */

    PlantOperationParameterTO fillPlantOperationParameterTO(IPlantOperationParameter parameter)
            throws NotInDatabaseException;

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
}
