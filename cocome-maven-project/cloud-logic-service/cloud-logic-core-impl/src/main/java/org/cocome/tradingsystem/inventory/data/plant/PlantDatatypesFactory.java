package org.cocome.tradingsystem.inventory.data.plant;

import org.cocome.tradingsystem.inventory.application.IIdentifiableTO;
import org.cocome.tradingsystem.inventory.application.plant.expression.ConditionalExpressionTO;
import org.cocome.tradingsystem.inventory.application.plant.expression.ExpressionTO;
import org.cocome.tradingsystem.inventory.application.plant.parameter.BooleanPlantOperationParameterTO;
import org.cocome.tradingsystem.inventory.application.plant.parameter.NorminalPlantOperationParameterTO;
import org.cocome.tradingsystem.inventory.application.plant.parameter.PlantOperationParameterTO;
import org.cocome.tradingsystem.inventory.application.plant.productionunit.ProductionUnitClassTO;
import org.cocome.tradingsystem.inventory.application.plant.productionunit.ProductionUnitOperationTO;
import org.cocome.tradingsystem.inventory.application.plant.recipe.EntryPointInteractionTO;
import org.cocome.tradingsystem.inventory.application.plant.recipe.ParameterInteractionTO;
import org.cocome.tradingsystem.inventory.application.plant.recipe.PlantOperationTO;
import org.cocome.tradingsystem.inventory.application.plant.recipe.RecipeTO;
import org.cocome.tradingsystem.inventory.data.enterprise.IEnterpriseDataFactory;
import org.cocome.tradingsystem.inventory.data.plant.expression.ConditionalExpression;
import org.cocome.tradingsystem.inventory.data.plant.expression.IConditionalExpression;
import org.cocome.tradingsystem.inventory.data.plant.expression.IExpression;
import org.cocome.tradingsystem.inventory.data.plant.parameter.IBooleanPlantOperationParameter;
import org.cocome.tradingsystem.inventory.data.plant.parameter.INorminalPlantOperationParameter;
import org.cocome.tradingsystem.inventory.data.plant.parameter.IPlantOperationParameter;
import org.cocome.tradingsystem.inventory.data.plant.productionunit.IProductionUnitClass;
import org.cocome.tradingsystem.inventory.data.plant.productionunit.IProductionUnitOperation;
import org.cocome.tradingsystem.inventory.data.plant.productionunit.ProductionUnitClass;
import org.cocome.tradingsystem.inventory.data.plant.productionunit.ProductionUnitOperation;
import org.cocome.tradingsystem.inventory.data.plant.recipe.IEntryPointInteraction;
import org.cocome.tradingsystem.inventory.data.plant.recipe.IParameterInteraction;
import org.cocome.tradingsystem.inventory.data.plant.recipe.IPlantOperation;
import org.cocome.tradingsystem.inventory.data.plant.recipe.IRecipe;
import org.cocome.tradingsystem.util.exception.NotInDatabaseException;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.inject.Provider;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Dependent
public class PlantDatatypesFactory implements IPlantDataFactory {

    @Inject
    private Provider<ProductionUnitClass> productionUnitClassProvider;

    @Inject
    private Provider<ProductionUnitOperation> productionUnitOperationProvider;

    @Inject
    private Provider<ConditionalExpression> conditionalExpressionProvider;

    @Inject
    private Provider<IPlantOperation> plantOperationProvider;

    @Inject
    private Provider<IBooleanPlantOperationParameter> booleanPlantOperationParameterProvider;

    @Inject
    private Provider<INorminalPlantOperationParameter> norminalPlantOperationParameterProvider;

    @Inject
    private Provider<IEntryPointInteraction> entryPointInteractionProvider;

    @Inject
    private Provider<IParameterInteraction> parameterInteractionProvider;

    @Inject
    private Provider<IRecipe> recipeProvider;

    @Inject
    private IEnterpriseDataFactory enterpriseDatatypes;

    @Override
    public IProductionUnitClass getNewProductionUnitClass() {
        return productionUnitClassProvider.get();
    }

    @Override
    public IProductionUnitClass convertToProductionUnitClass(ProductionUnitClassTO productionUnitClassTO) {
        final IProductionUnitClass puc = getNewProductionUnitClass();
        puc.setId(productionUnitClassTO.getId());
        puc.setPlantId(productionUnitClassTO.getPlant().getId());
        puc.setName(productionUnitClassTO.getName());
        return puc;
    }

    @Override
    public IProductionUnitOperation getNewProductionUnitOperation() {
        return productionUnitOperationProvider.get();
    }

    @Override
    public IProductionUnitOperation convertToProductionUnitOperation(ProductionUnitOperationTO productionUnitOperationTO) {
        final IProductionUnitOperation operation = new ProductionUnitOperation();
        operation.setId(productionUnitOperationTO.getId());
        operation.setProductionUnitClassId(productionUnitOperationTO.getProductionUnitClass().getId());
        operation.setOperationId(productionUnitOperationTO.getOperationId());
        return operation;
    }

    @Override
    public IConditionalExpression getNewConditionalExpression() {
        return conditionalExpressionProvider.get();
    }

    @Override
    public IConditionalExpression convertToConditionalExpression(ConditionalExpressionTO conditionalExpressionTO) {
        final IConditionalExpression expression = getNewConditionalExpression();
        expression.setId(conditionalExpressionTO.getId());
        expression.setParameterId(conditionalExpressionTO.getId());
        expression.setParameterValue(conditionalExpressionTO.getParameterValue());
        expression.setOnTrueExpressionIds(conditionalExpressionTO.getOnTrueExpressions().stream()
                .map(ExpressionTO::getId).collect(Collectors.toList()));
        expression.setOnFalseExpressionIds(conditionalExpressionTO.getOnFalseExpressions().stream()
                .map(ExpressionTO::getId).collect(Collectors.toList()));
        return expression;
    }

    @Override
    public ProductionUnitClassTO fillProductionUnitClassTO(IProductionUnitClass puc) throws NotInDatabaseException {
        final ProductionUnitClassTO result = new ProductionUnitClassTO();
        result.setId(puc.getId());
        result.setName(puc.getName());
        result.setPlant(enterpriseDatatypes.fillPlantTO(puc.getPlant()));

        return result;
    }

    @Override
    public ProductionUnitOperationTO fillProductionUnitOperationTO(IProductionUnitOperation operation)
            throws NotInDatabaseException {
        final ProductionUnitOperationTO result = new ProductionUnitOperationTO();
        result.setId(operation.getId());
        result.setOperationId(operation.getOperationId());
        result.setProductionUnitClass(fillProductionUnitClassTO(operation.getProductionUnitClass()));

        return result;
    }

    @Override
    public ConditionalExpressionTO fillConditionalExpressionTO(IConditionalExpression expressionTO)
            throws NotInDatabaseException {
        final ConditionalExpressionTO result = new ConditionalExpressionTO();
        result.setId(expressionTO.getId());
        result.setParameter(fillPlantOperationParameterTO(expressionTO.getParameter()));
        result.setParameterValue(expressionTO.getParameterValue());
        result.setOnTrueExpressions(fillExpressionTOs(expressionTO.getOnTrueExpressions()));
        result.setOnFalseExpressions(fillExpressionTOs(expressionTO.getOnFalseExpressions()));
        return result;
    }

    @Override
    public PlantOperationParameterTO fillPlantOperationParameterTO(IPlantOperationParameter parameter)
            throws NotInDatabaseException {
        if (IBooleanPlantOperationParameter.class.isAssignableFrom(parameter.getClass())) {
            return this.fillBooleanPlantOperationParameterTO(
                    (IBooleanPlantOperationParameter) parameter);
        } else if (INorminalPlantOperationParameter.class.isAssignableFrom(parameter.getClass())) {
            return this.fillNorminalPlantOperationParameterTO(
                    (INorminalPlantOperationParameter) parameter);
        }
        throw new IllegalArgumentException("Unknown class to handle: " + parameter.getClass());
    }

    @Override
    public IPlantOperationParameter convertToPlantOperationParameter(PlantOperationParameterTO plantOperationParameterTO) {
        if (BooleanPlantOperationParameterTO.class.isAssignableFrom(plantOperationParameterTO.getClass())) {
            return this.convertToBooleanPlantOperationParameter(
                    (BooleanPlantOperationParameterTO) plantOperationParameterTO);
        } else if (NorminalPlantOperationParameterTO.class.isAssignableFrom(plantOperationParameterTO.getClass())) {
            return this.convertToNorminalPlantOperationParameter(
                    (NorminalPlantOperationParameterTO) plantOperationParameterTO);
        }
        throw new IllegalArgumentException("Unknown class to handle: " + plantOperationParameterTO.getClass());
    }

    @Override
    public IPlantOperation getNewPlantOperation() {
        return plantOperationProvider.get();
    }

    @Override
    public PlantOperationTO fillPlantOperationTO(IPlantOperation operation) throws NotInDatabaseException {
        final PlantOperationTO result = new PlantOperationTO();
        result.setId(operation.getId());
        result.setName(operation.getName());
        result.setPlant(enterpriseDatatypes.fillPlantTO(operation.getPlant()));
        result.setExpressions(fillExpressionTOs(operation.getExpressions()));
        result.setInputEntryPoint(operation.getInputEntryPoint().stream()
                .map(enterpriseDatatypes::fillEntryPointTO)
                .collect(Collectors.toList()));
        result.setOutputEntryPoint(operation.getOutputEntryPoint().stream()
                .map(enterpriseDatatypes::fillEntryPointTO)
                .collect(Collectors.toList()));
        return result;
    }

    private List<ExpressionTO> fillExpressionTOs(List<IExpression> expressions)
            throws NotInDatabaseException {
        final List<ExpressionTO> expressionTOs = new ArrayList<>(expressions.size());
        for (final IExpression expression : expressions) {
            expressionTOs.add(fillExpressionTO(expression));
        }
        return expressionTOs;
    }


    private ExpressionTO fillExpressionTO(IExpression expressions) throws NotInDatabaseException {
        if (IProductionUnitOperation.class.isAssignableFrom(expressions.getClass())) {
            return this.fillProductionUnitOperationTO(
                    (IProductionUnitOperation) expressions);
        } else if (IConditionalExpression.class.isAssignableFrom(expressions.getClass())) {
            return this.fillConditionalExpressionTO(
                    (IConditionalExpression) expressions);
        }
        throw new IllegalArgumentException("Unknown class to handle: " + expressions.getClass());
    }

    @Override
    public IPlantOperation convertToPlantOperation(PlantOperationTO plantOperationTO) {
        final IPlantOperation result = getNewPlantOperation();
        result.setId(plantOperationTO.getId());
        result.setName(plantOperationTO.getName());
        result.setPlantId(plantOperationTO.getPlant().getId());
        result.setExpressionIds(extractIdsOfCollection(plantOperationTO.getExpressions()));
        result.setInputEntryPointIds(extractIdsOfCollection(plantOperationTO.getInputEntryPoint()));
        result.setOutputEntryPointIds(extractIdsOfCollection(plantOperationTO.getOutputEntryPoint()));

        return result;
    }

    @Override
    public IBooleanPlantOperationParameter getNewBooleanPlantOperationParameter() {
        return booleanPlantOperationParameterProvider.get();
    }

    @Override
    public BooleanPlantOperationParameterTO fillBooleanPlantOperationParameterTO(
            IBooleanPlantOperationParameter booleanPlantOperationParameter)
            throws NotInDatabaseException {
        final BooleanPlantOperationParameterTO result = new BooleanPlantOperationParameterTO();
        result.setId(booleanPlantOperationParameter.getId());
        result.setName(booleanPlantOperationParameter.getName());
        result.setCategory(booleanPlantOperationParameter.getCategory());
        result.setPlantOperation(fillPlantOperationTO(booleanPlantOperationParameter.getPlantOperation()));

        return result;
    }

    @Override
    public IBooleanPlantOperationParameter convertToBooleanPlantOperationParameter(BooleanPlantOperationParameterTO booleanPlantOperationParameterTO) {
        final IBooleanPlantOperationParameter result = getNewBooleanPlantOperationParameter();
        result.setId(booleanPlantOperationParameterTO.getId());
        result.setName(booleanPlantOperationParameterTO.getName());
        result.setCategory(booleanPlantOperationParameterTO.getCategory());
        result.setPlantOperationId(booleanPlantOperationParameterTO.getPlantOperation().getId());

        return result;
    }

    @Override
    public INorminalPlantOperationParameter getNewNorminalPlantOperationParameter() {
        return norminalPlantOperationParameterProvider.get();
    }

    @Override
    public NorminalPlantOperationParameterTO fillNorminalPlantOperationParameterTO(
            INorminalPlantOperationParameter norminalPlantOperationParameter)
            throws NotInDatabaseException {
        final NorminalPlantOperationParameterTO result = new NorminalPlantOperationParameterTO();
        result.setId(norminalPlantOperationParameter.getId());
        result.setName(norminalPlantOperationParameter.getName());
        result.setCategory(norminalPlantOperationParameter.getCategory());
        result.setOptions(norminalPlantOperationParameter.getOptions());
        result.setPlantOperation(fillPlantOperationTO(norminalPlantOperationParameter.getPlantOperation()));

        return result;
    }

    @Override
    public INorminalPlantOperationParameter convertToNorminalPlantOperationParameter(NorminalPlantOperationParameterTO norminalPlantOperationParameterTO) {
        final INorminalPlantOperationParameter result = getNewNorminalPlantOperationParameter();
        result.setId(norminalPlantOperationParameterTO.getId());
        result.setName(norminalPlantOperationParameterTO.getName());
        result.setCategory(norminalPlantOperationParameterTO.getCategory());
        result.setOptions(norminalPlantOperationParameterTO.getOptions());
        result.setPlantOperationId(norminalPlantOperationParameterTO.getPlantOperation().getId());

        return result;
    }

    private List<Long> extractIdsOfCollection(Collection<? extends IIdentifiableTO> collection) {
        return collection.stream().map(IIdentifiableTO::getId).collect(Collectors.toList());
    }

    @Override
    public IEntryPointInteraction getNewEntryPointInteraction() {
        return entryPointInteractionProvider.get();
    }

    @Override
    public EntryPointInteractionTO fillEntryPointInteractionTO(
            IEntryPointInteraction entryPointInteraction)
            throws NotInDatabaseException {
        final EntryPointInteractionTO result = new EntryPointInteractionTO();
        result.setId(entryPointInteraction.getId());
        result.setFrom(enterpriseDatatypes.fillEntryPointTO(entryPointInteraction.getFrom()));
        result.setTo(enterpriseDatatypes.fillEntryPointTO(entryPointInteraction.getTo()));
        return result;
    }

    @Override
    public IEntryPointInteraction convertToEntryPointInteraction(EntryPointInteractionTO entryPointInteractionTO) {
        final IEntryPointInteraction result = getNewEntryPointInteraction();
        result.setId(entryPointInteractionTO.getId());
        result.setFromId(entryPointInteractionTO.getFrom().getId());
        result.setToId(entryPointInteractionTO.getTo().getId());
        return result;
    }

    @Override
    public IParameterInteraction getNewParameterInteraction() {
        return parameterInteractionProvider.get();
    }

    @Override
    public ParameterInteractionTO fillParameterInteractionTO(
            IParameterInteraction parameterInteraction)
            throws NotInDatabaseException {
        final ParameterInteractionTO result = new ParameterInteractionTO();
        result.setId(parameterInteraction.getId());
        result.setFrom(enterpriseDatatypes.fillCustomProductParameterTO(parameterInteraction.getFrom()));
        result.setTo(fillPlantOperationParameterTO(parameterInteraction.getTo()));
        return result;
    }

    @Override
    public IParameterInteraction convertToParameterInteraction(ParameterInteractionTO parameterInteractionTO) {
        final IParameterInteraction result = getNewParameterInteraction();
        result.setId(parameterInteractionTO.getId());
        result.setFromId(parameterInteractionTO.getFrom().getId());
        result.setToId(parameterInteractionTO.getTo().getId());
        return result;
    }

    @Override
    public IRecipe getNewRecipe() {
        return recipeProvider.get();
    }

    @Override
    public RecipeTO fillRecipeTO(
            IRecipe recipe)
            throws NotInDatabaseException {
        final RecipeTO result = new RecipeTO();
        result.setId(recipe.getId());
        result.setCustomProduct(enterpriseDatatypes.fillCustomProductTO(recipe.getCustomProduct()));
        result.setOperations(new ArrayList<>());
        for (final IPlantOperation operation : recipe.getOperations()) {
            result.getOperations().add(fillPlantOperationTO(operation));
        }
        result.setParameterInteractions(new ArrayList<>());
        for (final IParameterInteraction paramInteraction : recipe.getParameterInteractions()) {
            result.getParameterInteractions().add(fillParameterInteractionTO(paramInteraction));
        }
        result.setEntryPointInteractions(new ArrayList<>());
        for (final IEntryPointInteraction entryPointInteraction : recipe.getEntryPointInteractions()) {
            result.getEntryPointInteractions().add(fillEntryPointInteractionTO(entryPointInteraction));
        }
        return result;
    }

    @Override
    public IRecipe convertToRecipe(RecipeTO recipeTO) {
        final IRecipe result = getNewRecipe();
        result.setCustomProductId(recipeTO.getCustomProduct().getId());
        result.setOperationIds(recipeTO.getOperations()
                .stream().map(PlantOperationTO::getId).collect(Collectors.toList()));
        result.setEntryPointInteractionIds(recipeTO.getEntryPointInteractions()
                .stream().map(EntryPointInteractionTO::getId).collect(Collectors.toList()));
        result.setParameterInteractionIds(recipeTO.getParameterInteractions()
                .stream().map(ParameterInteractionTO::getId).collect(Collectors.toList()));
        return result;
    }
}
