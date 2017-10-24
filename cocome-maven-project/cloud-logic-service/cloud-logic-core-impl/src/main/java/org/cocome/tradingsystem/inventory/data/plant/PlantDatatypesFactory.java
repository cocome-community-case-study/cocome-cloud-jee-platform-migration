package org.cocome.tradingsystem.inventory.data.plant;

import org.cocome.cloud.logic.webservice.ThrowingFunction;
import org.cocome.tradingsystem.inventory.application.IIdentifiableTO;
import org.cocome.tradingsystem.inventory.application.plant.expression.ConditionalExpressionTO;
import org.cocome.tradingsystem.inventory.application.plant.expression.ExpressionTO;
import org.cocome.tradingsystem.inventory.application.plant.parameter.BooleanPlantOperationParameterTO;
import org.cocome.tradingsystem.inventory.application.plant.parameter.NorminalPlantOperationParameterTO;
import org.cocome.tradingsystem.inventory.application.plant.parameter.PlantOperationParameterTO;
import org.cocome.tradingsystem.inventory.application.plant.productionunit.ProductionUnitClassTO;
import org.cocome.tradingsystem.inventory.application.plant.productionunit.ProductionUnitOperationTO;
import org.cocome.tradingsystem.inventory.application.plant.productionunit.ProductionUnitTO;
import org.cocome.tradingsystem.inventory.application.plant.recipe.*;
import org.cocome.tradingsystem.inventory.data.enterprise.IEnterpriseDataFactory;
import org.cocome.tradingsystem.inventory.data.plant.expression.ConditionalExpression;
import org.cocome.tradingsystem.inventory.data.plant.expression.IConditionalExpression;
import org.cocome.tradingsystem.inventory.data.plant.expression.IExpression;
import org.cocome.tradingsystem.inventory.data.plant.parameter.IBooleanPlantOperationParameter;
import org.cocome.tradingsystem.inventory.data.plant.parameter.INorminalPlantOperationParameter;
import org.cocome.tradingsystem.inventory.data.plant.parameter.IPlantOperationParameter;
import org.cocome.tradingsystem.inventory.data.plant.productionunit.*;
import org.cocome.tradingsystem.inventory.data.plant.recipe.*;
import org.cocome.tradingsystem.util.exception.NotInDatabaseException;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.inject.Provider;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Dependent
public class PlantDatatypesFactory implements IPlantDataFactory {

    @Inject
    private Provider<ProductionUnit> productionUnitProvider;

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
    private Provider<IPlantOperationOrder> plantOperationOrderProvider;

    @Inject
    private Provider<IPlantOperationOrderEntry> plantOperationOrderEntryProvider;

    @Inject
    private Provider<IPlantOperationParameterValue> plantOperationParameterValueProvider;

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
        operation.setName(productionUnitOperationTO.getName());
        operation.setProductionUnitClassId(productionUnitOperationTO.getProductionUnitClass().getId());
        operation.getExecutionDurationInMillis(productionUnitOperationTO.getExecutionDurationInMillis());
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
        expression.setParameterId(conditionalExpressionTO.getParameter().getId());
        expression.setParameterValue(conditionalExpressionTO.getParameterValue());
        expression.setOnTrueExpressionIds(extractIdsOfCollection(conditionalExpressionTO.getOnTrueExpressions()));
        expression.setOnFalseExpressionIds(extractIdsOfCollection(conditionalExpressionTO.getOnFalseExpressions()));
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
        result.setName(operation.getName());
        result.setOperationId(operation.getOperationId());
        result.setExecutionDurationInMillis(operation.getExecutionDurationInMillis());
        result.setProductionUnitClass(fillProductionUnitClassTO(operation.getProductionUnitClass()));

        return result;
    }

    @Override
    public IProductionUnit getNewProductionUnit() {
        return this.productionUnitProvider.get();
    }

    @Override
    public IProductionUnit convertToProductionUnit(ProductionUnitTO productionUnitTO) {
        final IProductionUnit result = getNewProductionUnit();
        result.setId(productionUnitTO.getId());
        result.setLocation(productionUnitTO.getLocation());
        result.setInterfaceUrl(productionUnitTO.getInterfaceUrl());
        result.setDouble(productionUnitTO.isDouble());
        result.setPlant(enterpriseDatatypes.convertToPlant(productionUnitTO.getPlant()));
        result.setProductionUnitClass(convertToProductionUnitClass(productionUnitTO.getProductionUnitClass()));

        return result;
    }

    @Override
    public ProductionUnitTO fillProductionUnitTO(IProductionUnit unit) throws NotInDatabaseException {
        final ProductionUnitTO result = new ProductionUnitTO();
        result.setId(unit.getId());
        result.setLocation(unit.getLocation());
        result.setInterfaceUrl(unit.getInterfaceUrl());
        result.setDouble(unit.isDouble());
        result.setPlant(enterpriseDatatypes.fillPlantTO(unit.getPlant()));
        result.setProductionUnitClass(fillProductionUnitClassTO(unit.getProductionUnitClass()));

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
        result.setInputEntryPoint(convertList(operation.getInputEntryPoint(), enterpriseDatatypes::fillEntryPointTO));
        result.setOutputEntryPoint(convertList(operation.getOutputEntryPoint(),
                enterpriseDatatypes::fillEntryPointTO));
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

        return result;
    }

    @Override
    public IBooleanPlantOperationParameter convertToBooleanPlantOperationParameter(BooleanPlantOperationParameterTO booleanPlantOperationParameterTO) {
        final IBooleanPlantOperationParameter result = getNewBooleanPlantOperationParameter();
        result.setId(booleanPlantOperationParameterTO.getId());
        result.setName(booleanPlantOperationParameterTO.getName());
        result.setCategory(booleanPlantOperationParameterTO.getCategory());

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

        return result;
    }

    @Override
    public INorminalPlantOperationParameter convertToNorminalPlantOperationParameter(NorminalPlantOperationParameterTO norminalPlantOperationParameterTO) {
        final INorminalPlantOperationParameter result = getNewNorminalPlantOperationParameter();
        result.setId(norminalPlantOperationParameterTO.getId());
        result.setName(norminalPlantOperationParameterTO.getName());
        result.setCategory(norminalPlantOperationParameterTO.getCategory());
        result.setOptions(norminalPlantOperationParameterTO.getOptions());

        return result;
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
        result.setOperationIds(extractIdsOfCollection(recipeTO.getOperations()));
        result.setEntryPointInteractionIds(extractIdsOfCollection(recipeTO.getEntryPointInteractions()));
        result.setParameterInteractionIds(extractIdsOfCollection(recipeTO.getParameterInteractions()));
        return result;
    }

    @Override
    public IPlantOperationOrder getNewPlantOperationOrder() {
        return plantOperationOrderProvider.get();
    }

    @Override
    public PlantOperationOrderTO fillPlantOperationOrderTO(IPlantOperationOrder plantOperationOrder)
            throws NotInDatabaseException {
        final PlantOperationOrderTO result = new PlantOperationOrderTO();
        result.setId(plantOperationOrder.getId());
        result.setOrderingDate(plantOperationOrder.getOrderingDate());
        result.setDeliveryDate(plantOperationOrder.getDeliveryDate());
        result.setEnterprise(enterpriseDatatypes.fillEnterpriseTO(plantOperationOrder.getEnterprise()));
        result.setOrderEntries(convertList(plantOperationOrder.getOrderEntries(),
                this::fillPlantOperationOrderEntryTO));
        return result;
    }

    @Override
    public IPlantOperationOrder convertToPlantOperationOrder(PlantOperationOrderTO plantOperationOrderTO) {
        final IPlantOperationOrder result = getNewPlantOperationOrder();
        result.setId(plantOperationOrderTO.getId());
        result.setOrderingDate(plantOperationOrderTO.getOrderingDate());
        result.setDeliveryDate(plantOperationOrderTO.getDeliveryDate());
        result.setEnterpriseId(plantOperationOrderTO.getEnterprise().getId());
        result.setOrderEntries(convertList(plantOperationOrderTO.getOrderEntries(),
                this::convertToPlantOperationOrderEntry));
        return result;
    }

    @Override
    public IPlantOperationOrderEntry getNewPlantOperationOrderEntry() {
        return plantOperationOrderEntryProvider.get();
    }

    @Override
    public PlantOperationOrderEntryTO fillPlantOperationOrderEntryTO(IPlantOperationOrderEntry plantOperationOrderEntry) throws NotInDatabaseException {
        final PlantOperationOrderEntryTO result = new PlantOperationOrderEntryTO();
        result.setId(plantOperationOrderEntry.getId());
        result.setAmount(plantOperationOrderEntry.getAmount());
        result.setParameterValues(convertList(plantOperationOrderEntry.getParameterValues(),
                this::fillPlantOperationParameterValueTO));
        return result;
    }

    @Override
    public IPlantOperationOrderEntry convertToPlantOperationOrderEntry(PlantOperationOrderEntryTO plantOperationOrderEntryTO) {
        final IPlantOperationOrderEntry result = getNewPlantOperationOrderEntry();
        result.setId(plantOperationOrderEntryTO.getId());
        result.setAmount(plantOperationOrderEntryTO.getAmount());
        result.setParameterValues(convertList(plantOperationOrderEntryTO.getParameterValues(),
                this::convertToPlantOperationParameterValue));
        return result;
    }

    @Override
    public IPlantOperationParameterValue getNewPlantOperationParameterValue() {
        return plantOperationParameterValueProvider.get();
    }

    @Override
    public PlantOperationParameterValueTO fillPlantOperationParameterValueTO(IPlantOperationParameterValue plantOperationParameterValue)
            throws NotInDatabaseException {
        final PlantOperationParameterValueTO result = new PlantOperationParameterValueTO();
        result.setId(plantOperationParameterValue.getId());
        result.setValue(plantOperationParameterValue.getValue());
        result.setParameter(fillPlantOperationParameterTO(plantOperationParameterValue.getParameter()));
        return result;
    }

    @Override
    public IPlantOperationParameterValue convertToPlantOperationParameterValue(PlantOperationParameterValueTO plantOperationParameterValueTO) {
        final IPlantOperationParameterValue result = getNewPlantOperationParameterValue();
        result.setId(plantOperationParameterValueTO.getId());
        result.setValue(plantOperationParameterValueTO.getValue());
        result.setParameterId(plantOperationParameterValueTO.getParameter().getId());
        return result;
    }

    private List<Long> extractIdsOfCollection(Collection<? extends IIdentifiableTO> collection) {
        if (collection == null) {
            return Collections.emptyList();
        }
        return collection.stream().map(IIdentifiableTO::getId).collect(Collectors.toList());
    }

    private <T1, T2, E extends Throwable> Collection<T1> convertList(Collection<T2> collection,
                                                                     ThrowingFunction<T2, T1, E> converter)
            throws E {
        if (collection == null) {
            return Collections.emptyList();
        }

        final Collection<T1> result = new ArrayList<>(collection.size());
        for (final T2 element : collection) {
            result.add(converter.apply(element));
        }
        return result;
    }
}
