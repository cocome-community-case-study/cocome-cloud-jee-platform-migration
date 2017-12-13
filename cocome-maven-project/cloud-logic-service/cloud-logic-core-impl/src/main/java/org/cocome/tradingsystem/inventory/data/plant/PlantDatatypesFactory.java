package org.cocome.tradingsystem.inventory.data.plant;

import org.cocome.cloud.logic.webservice.ThrowingFunction;
import org.cocome.tradingsystem.inventory.application.plant.parameter.BooleanParameterTO;
import org.cocome.tradingsystem.inventory.application.plant.parameter.NominalParameterTO;
import org.cocome.tradingsystem.inventory.application.plant.parameter.ParameterTO;
import org.cocome.tradingsystem.inventory.application.plant.parameter.ParameterValueTO;
import org.cocome.tradingsystem.inventory.application.plant.productionunit.ProductionUnitClassTO;
import org.cocome.tradingsystem.inventory.application.plant.productionunit.ProductionUnitOperationTO;
import org.cocome.tradingsystem.inventory.application.plant.productionunit.ProductionUnitTO;
import org.cocome.tradingsystem.inventory.application.plant.recipe.*;
import org.cocome.tradingsystem.inventory.data.enterprise.IEnterpriseDataFactory;
import org.cocome.tradingsystem.inventory.data.plant.parameter.IBooleanParameter;
import org.cocome.tradingsystem.inventory.data.plant.parameter.INominalParameter;
import org.cocome.tradingsystem.inventory.data.plant.parameter.IParameter;
import org.cocome.tradingsystem.inventory.data.plant.productionunit.*;
import org.cocome.tradingsystem.inventory.data.plant.recipe.*;
import org.cocome.tradingsystem.inventory.data.store.IStoreDataFactory;
import org.cocome.tradingsystem.util.exception.NotInDatabaseException;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.inject.Provider;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

@Dependent
public class PlantDatatypesFactory implements IPlantDataFactory {

    @Inject
    private Provider<ProductionUnit> productionUnitProvider;

    @Inject
    private Provider<ProductionUnitClass> productionUnitClassProvider;

    @Inject
    private Provider<ProductionUnitOperation> productionUnitOperationProvider;

    @Inject
    private Provider<IPlantOperation> plantOperationProvider;

    @Inject
    private Provider<IBooleanParameter> booleanPlantOperationParameterProvider;

    @Inject
    private Provider<INominalParameter> norminalPlantOperationParameterProvider;

    @Inject
    private Provider<IEntryPointInteraction> entryPointInteractionProvider;

    @Inject
    private Provider<IParameterInteraction> parameterInteractionProvider;

    @Inject
    private Provider<IEntryPoint> entryPointProvider;

    @Inject
    private Provider<IRecipe> recipeProvider;

    @Inject
    private Provider<IPlantOperationOrder> plantOperationOrderProvider;

    @Inject
    private Provider<IPlantOperationOrderEntry> plantOperationOrderEntryProvider;

    @Inject
    private Provider<IParameterValue> parameterValueProvider;

    @Inject
    private Provider<IProductionOrder> productionOrderProvider;

    @Inject
    private Provider<IProductionOrderEntry> productionOrderEntryProvider;

    @Inject
    private Provider<IRecipeNode> recipeNodeProvider;

    @Inject
    private IEnterpriseDataFactory enterpriseDatatypes;

    @Inject
    private IStoreDataFactory storeDatatypes;

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
        operation.setExecutionDurationInMillis(productionUnitOperationTO.getExecutionDurationInMillis());
        operation.setOperationId(productionUnitOperationTO.getOperationId());
        return operation;
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
        result.setExecutionDurationInMillis(operation.setExecutionDurationInMillis());
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
        result.setPlantId(productionUnitTO.getPlant().getId());
        result.setProductionUnitClass(convertToProductionUnitClass(productionUnitTO.getProductionUnitClass()));
        result.setProductionUnitClassId(productionUnitTO.getProductionUnitClass().getId());

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
    public IEntryPoint getNewEntryPoint() {
        return entryPointProvider.get();
    }

    @Override
    public EntryPointTO fillEntryPointTO(IEntryPoint entryPoint) throws NotInDatabaseException {
        final EntryPointTO entryPointTO = new EntryPointTO();
        entryPointTO.setId(entryPoint.getId());
        entryPointTO.setName(entryPoint.getName());
        entryPointTO.setOperation(fillRecipeOperationTO(entryPoint.getOperation()));
        entryPointTO.setDirection(EntryPointTO.DirectionTO.valueOf(entryPoint.getDirection().name()));

        return entryPointTO;
    }

    @Override
    public IEntryPoint convertToEntryPoint(EntryPointTO entryPointTO) {
        final IEntryPoint entryPoint = getNewEntryPoint();
        entryPoint.setId(entryPointTO.getId());
        entryPoint.setName(entryPointTO.getName());
        entryPoint.setOperation(convertToRecipeOperation(entryPointTO.getOperation()));
        entryPoint.setOperationId(entryPointTO.getOperation().getId());
        entryPoint.setDirection(IEntryPoint.Direction.valueOf(entryPointTO.getDirection().name()));

        return entryPoint;
    }

    @Override
    public RecipeOperationTO fillRecipeOperationTO(IRecipeOperation operation) throws NotInDatabaseException {
        if (IPlantOperation.class.isAssignableFrom(operation.getClass())) {
            return this.fillPlantOperationTO(
                    (IPlantOperation) operation);
        } else if (IRecipe.class.isAssignableFrom(operation.getClass())) {
            return this.fillRecipeTO(
                    (IRecipe) operation);
        }
        throw new IllegalArgumentException("Unknown class to handle: " + operation.getClass());
    }

    @Override
    public IRecipeOperation convertToRecipeOperation(RecipeOperationTO operationTO) {
        if (PlantOperationTO.class.isAssignableFrom(operationTO.getClass())) {
            return this.convertToPlantOperation((PlantOperationTO) operationTO);
        } else if (RecipeTO.class.isAssignableFrom(operationTO.getClass())) {
            return this.convertToRecipe(
                    (RecipeTO) operationTO);
        }
        throw new IllegalArgumentException("Unknown class to handle: " + operationTO.getClass());
    }

    @Override
    public ParameterTO fillParameterTO(IParameter parameter)
            throws NotInDatabaseException {
        if (IBooleanParameter.class.isAssignableFrom(parameter.getClass())) {
            return this.fillBooleanParameterTO(
                    (IBooleanParameter) parameter);
        } else if (INominalParameter.class.isAssignableFrom(parameter.getClass())) {
            return this.fillNominalParameterTO(
                    (INominalParameter) parameter);
        }
        throw new IllegalArgumentException("Unknown class to handle: " + parameter.getClass());
    }

    @Override
    public IParameter convertToParameter(ParameterTO parameterTO) {
        if (BooleanParameterTO.class.isAssignableFrom(parameterTO.getClass())) {
            return this.convertToBooleanParameter(
                    (BooleanParameterTO) parameterTO);
        } else if (NominalParameterTO.class.isAssignableFrom(parameterTO.getClass())) {
            return this.convertToNominalParameter(
                    (NominalParameterTO) parameterTO);
        }
        throw new IllegalArgumentException("Unknown class to handle: " + parameterTO.getClass());
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
        result.setMarkup(operation.getMarkup());
        return result;
    }

    @Override
    public IPlantOperation convertToPlantOperation(PlantOperationTO plantOperationTO) {
        final IPlantOperation result = getNewPlantOperation();
        result.setId(plantOperationTO.getId());
        result.setName(plantOperationTO.getName());
        result.setPlantId(plantOperationTO.getPlant().getId());
        result.setMarkup(plantOperationTO.getMarkup());

        return result;
    }

    @Override
    public IBooleanParameter getNewBooleanParameter() {
        return booleanPlantOperationParameterProvider.get();
    }

    @Override
    public BooleanParameterTO fillBooleanParameterTO(
            IBooleanParameter booleanParameter)
            throws NotInDatabaseException {
        final BooleanParameterTO result = new BooleanParameterTO();
        result.setId(booleanParameter.getId());
        result.setName(booleanParameter.getName());
        result.setCategory(booleanParameter.getCategory());
        result.setOperation(fillRecipeOperationTO(booleanParameter.getOperation()));

        return result;
    }

    @Override
    public IBooleanParameter convertToBooleanParameter(BooleanParameterTO booleanParameterTO) {
        final IBooleanParameter result = getNewBooleanParameter();
        result.setId(booleanParameterTO.getId());
        result.setName(booleanParameterTO.getName());
        result.setCategory(booleanParameterTO.getCategory());
        result.setOperation(convertToRecipeOperation(booleanParameterTO.getOperation()));
        result.setOperationId(booleanParameterTO.getOperation().getId());

        return result;
    }

    @Override
    public INominalParameter getNewNominalParameter() {
        return norminalPlantOperationParameterProvider.get();
    }

    @Override
    public NominalParameterTO fillNominalParameterTO(
            INominalParameter nominalParameter)
            throws NotInDatabaseException {
        final NominalParameterTO result = new NominalParameterTO();
        result.setId(nominalParameter.getId());
        result.setName(nominalParameter.getName());
        result.setCategory(nominalParameter.getCategory());
        result.setOptions(nominalParameter.getOptions());
        result.setOperation(fillRecipeOperationTO(nominalParameter.getOperation()));

        return result;
    }

    @Override
    public INominalParameter convertToNominalParameter(NominalParameterTO nominalParameterTO) {
        final INominalParameter result = getNewNominalParameter();
        result.setId(nominalParameterTO.getId());
        result.setName(nominalParameterTO.getName());
        result.setCategory(nominalParameterTO.getCategory());
        result.setOptions(nominalParameterTO.getOptions());
        result.setOperation(convertToRecipeOperation(nominalParameterTO.getOperation()));
        result.setOperationId(nominalParameterTO.getOperation().getId());

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
        result.setFrom(fillEntryPointTO(entryPointInteraction.getFrom()));
        result.setTo(fillEntryPointTO(entryPointInteraction.getTo()));
        result.setRecipe(fillRecipeTO(entryPointInteraction.getRecipe()));
        return result;
    }

    @Override
    public IEntryPointInteraction convertToEntryPointInteraction(EntryPointInteractionTO entryPointInteractionTO) {
        final IEntryPointInteraction result = getNewEntryPointInteraction();
        result.setId(entryPointInteractionTO.getId());
        result.setFromId(entryPointInteractionTO.getFrom().getId());
        result.setToId(entryPointInteractionTO.getTo().getId());
        result.setRecipeId(entryPointInteractionTO.getRecipe().getId());
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
        result.setFrom(fillParameterTO(parameterInteraction.getFrom()));
        result.setTo(fillParameterTO(parameterInteraction.getTo()));
        result.setRecipe(fillRecipeTO(parameterInteraction.getRecipe()));
        return result;
    }

    @Override
    public IParameterInteraction convertToParameterInteraction(ParameterInteractionTO parameterInteractionTO) {
        final IParameterInteraction result = getNewParameterInteraction();
        result.setId(parameterInteractionTO.getId());
        result.setFromId(parameterInteractionTO.getFrom().getId());
        result.setToId(parameterInteractionTO.getTo().getId());
        result.setRecipeId(parameterInteractionTO.getRecipe().getId());
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
        result.setName(recipe.getName());
        result.setCustomProduct(enterpriseDatatypes.fillCustomProductTO(recipe.getCustomProduct()));
        result.setEnterprise(enterpriseDatatypes.fillEnterpriseTO(recipe.getEnterprise()));

        return result;
    }

    @Override
    public IRecipe convertToRecipe(RecipeTO recipeTO) {
        final IRecipe result = getNewRecipe();
        result.setId(recipeTO.getId());
        result.setName(recipeTO.getName());
        result.setCustomProductId(recipeTO.getCustomProduct().getId());
        result.setEnterpriseId(recipeTO.getEnterprise().getId());
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
        result.setPlant(enterpriseDatatypes.fillPlantTO(plantOperationOrder.getPlant()));
        result.setOrderEntries(convertList(plantOperationOrder.getOrderEntries(),
                this::fillPlantOperationOrderEntryTO));
        return result;
    }

    @Override
    public IPlantOperationOrder convertToPlantOperationOrder(PlantOperationOrderTO plantOperationOrderTO)
            throws NotInDatabaseException {
        final IPlantOperationOrder result = getNewPlantOperationOrder();
        result.setId(plantOperationOrderTO.getId());
        result.setOrderingDate(plantOperationOrderTO.getOrderingDate());
        result.setDeliveryDate(plantOperationOrderTO.getDeliveryDate());
        result.setEnterprise(enterpriseDatatypes.convertToEnterprise(plantOperationOrderTO.getEnterprise()));
        result.setEnterpriseId(plantOperationOrderTO.getEnterprise().getId());
        result.setPlant(enterpriseDatatypes.convertToPlant(plantOperationOrderTO.getPlant()));
        result.setPlantId(plantOperationOrderTO.getPlant().getId());
        result.setOrderEntries(convertList(plantOperationOrderTO.getOrderEntries(),
                this::convertToPlantOperationOrderEntry));
        return result;
    }

    @Override
    public IPlantOperationOrderEntry getNewPlantOperationOrderEntry() {
        return plantOperationOrderEntryProvider.get();
    }

    @Override
    public PlantOperationOrderEntryTO fillPlantOperationOrderEntryTO(IPlantOperationOrderEntry plantOperationOrderEntry)
            throws NotInDatabaseException {
        final PlantOperationOrderEntryTO result = new PlantOperationOrderEntryTO();
        result.setId(plantOperationOrderEntry.getId());
        result.setPlantOperation(fillPlantOperationTO(plantOperationOrderEntry.getOperation()));
        result.setAmount(plantOperationOrderEntry.getAmount());
        result.setParameterValues(convertList(plantOperationOrderEntry.getParameterValues(),
                this::fillParameterValueTO));
        return result;
    }

    @Override
    public IPlantOperationOrderEntry convertToPlantOperationOrderEntry(
            PlantOperationOrderEntryTO plantOperationOrderEntryTO)
            throws NotInDatabaseException {
        final IPlantOperationOrderEntry result = getNewPlantOperationOrderEntry();
        result.setId(plantOperationOrderEntryTO.getId());
        result.setAmount(plantOperationOrderEntryTO.getAmount());
        result.setOperation(convertToPlantOperation(plantOperationOrderEntryTO.getPlantOperation()));
        result.setOperationId(plantOperationOrderEntryTO.getPlantOperation().getId());
        result.setParameterValues(convertList(plantOperationOrderEntryTO.getParameterValues(),
                this::convertToParameterValue));
        return result;
    }

    @Override
    public IParameterValue getNewParameterValue() {
        return parameterValueProvider.get();
    }

    @Override
    public ParameterValueTO fillParameterValueTO(
            IParameterValue plantOperationParameterValue)
            throws NotInDatabaseException {
        final ParameterValueTO result = new ParameterValueTO();
        result.setId(plantOperationParameterValue.getId());
        result.setValue(plantOperationParameterValue.getValue());
        result.setParameter(fillParameterTO(plantOperationParameterValue.getParameter()));
        return result;
    }

    @Override
    public IParameterValue convertToParameterValue(ParameterValueTO parameterValueTO) {
        final IParameterValue result = getNewParameterValue();
        result.setId(parameterValueTO.getId());
        result.setValue(parameterValueTO.getValue());
        result.setParameter(convertToParameter(parameterValueTO.getParameter()));
        result.setParameterId(parameterValueTO.getParameter().getId());
        return result;
    }

    @Override
    public IProductionOrder getNewProductionOrder() {
        return productionOrderProvider.get();
    }

    @Override
    public ProductionOrderTO fillProductionOrderTO(IProductionOrder productionOrder)
            throws NotInDatabaseException {
        final ProductionOrderTO result = new ProductionOrderTO();
        result.setId(productionOrder.getId());
        result.setOrderingDate(productionOrder.getOrderingDate());
        result.setDeliveryDate(productionOrder.getDeliveryDate());
        result.setStore(storeDatatypes.fillStoreWithEnterpriseTO(productionOrder.getStore()));
        result.setOrderEntries(convertList(productionOrder.getOrderEntries(),
                this::fillProductionOrderEntryTO));
        return result;
    }

    @Override
    public IProductionOrder convertToProductionOrder(ProductionOrderTO productionOrderTO)
            throws NotInDatabaseException {
        final IProductionOrder result = getNewProductionOrder();
        result.setId(productionOrderTO.getId());
        result.setOrderingDate(productionOrderTO.getOrderingDate());
        result.setDeliveryDate(productionOrderTO.getDeliveryDate());
        result.setEnterpriseId(productionOrderTO.getEnterprise().getId());
        result.setStoreId(productionOrderTO.getStore().getId());
        result.setOrderEntries(convertList(productionOrderTO.getOrderEntries(),
                this::convertToProductionOrderEntry));
        return result;
    }

    @Override
    public IProductionOrderEntry getNewProductionOrderEntry() {
        return productionOrderEntryProvider.get();
    }

    @Override
    public ProductionOrderEntryTO fillProductionOrderEntryTO(IProductionOrderEntry productionOrderEntry)
            throws NotInDatabaseException {
        final ProductionOrderEntryTO result = new ProductionOrderEntryTO();
        result.setId(productionOrderEntry.getId());
        result.setRecipe(fillRecipeTO(productionOrderEntry.getOperation()));
        result.setAmount(productionOrderEntry.getAmount());
        result.setParameterValues(convertList(productionOrderEntry.getParameterValues(),
                this::fillParameterValueTO));
        return result;
    }

    @Override
    public IProductionOrderEntry convertToProductionOrderEntry(ProductionOrderEntryTO productionOrderEntryTO)
            throws NotInDatabaseException {
        final IProductionOrderEntry result = getNewProductionOrderEntry();
        result.setId(productionOrderEntryTO.getId());
        result.setAmount(productionOrderEntryTO.getAmount());
        result.setOperation(convertToRecipe(productionOrderEntryTO.getRecipe()));
        result.setParameterValues(convertList(productionOrderEntryTO.getParameterValues(),
                this::convertToParameterValue));
        return result;
    }

    @Override
    public IRecipeNode getNewRecipeNode() {
        return this.recipeNodeProvider.get();
    }

    @Override
    public RecipeNodeTO fillRecipeNodeTO(IRecipeNode recipeNode) throws NotInDatabaseException {
        final RecipeNodeTO result = new RecipeNodeTO();
        result.setId(recipeNode.getId());
        result.setOperation(fillRecipeOperationTO(recipeNode.getOperation()));
        result.setRecipe(fillRecipeTO(recipeNode.getRecipe()));
        return result;
    }

    @Override
    public IRecipeNode convertToRecipeNode(RecipeNodeTO recipeNode) throws NotInDatabaseException {
        final IRecipeNode result = getNewRecipeNode();
        result.setId(recipeNode.getId());
        result.setOperation(convertToRecipeOperation(recipeNode.getOperation()));
        result.setOperationId(recipeNode.getOperation().getId());
        result.setRecipe(convertToRecipe(recipeNode.getRecipe()));
        result.setRecipeId(recipeNode.getRecipe().getId());
        return result;
    }

    private <T1, T2, E extends Throwable> Collection<T1> convertList(Collection<T2> collection,
                                                                     ThrowingFunction<T2, T1, E> converter)
            throws E, NotInDatabaseException {
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
