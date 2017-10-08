package org.cocome.tradingsystem.inventory.data.plant;

import org.cocome.tradingsystem.inventory.application.IIdentifiableTO;
import org.cocome.tradingsystem.inventory.application.plant.expression.ConditionalExpressionTO;
import org.cocome.tradingsystem.inventory.application.plant.expression.ExpressionTO;
import org.cocome.tradingsystem.inventory.application.plant.parameter.BooleanPlantOperationParameterTO;
import org.cocome.tradingsystem.inventory.application.plant.parameter.NorminalPlantOperationParameterTO;
import org.cocome.tradingsystem.inventory.application.plant.productionunit.ProductionUnitClassTO;
import org.cocome.tradingsystem.inventory.application.plant.productionunit.ProductionUnitOperationTO;
import org.cocome.tradingsystem.inventory.application.plant.recipe.PlantOperationTO;
import org.cocome.tradingsystem.inventory.data.enterprise.IEnterpriseDataFactory;
import org.cocome.tradingsystem.inventory.data.plant.expression.ConditionalExpression;
import org.cocome.tradingsystem.inventory.data.plant.expression.IConditionalExpression;
import org.cocome.tradingsystem.inventory.data.plant.expression.IExpression;
import org.cocome.tradingsystem.inventory.data.plant.parameter.IBooleanPlantOperationParameter;
import org.cocome.tradingsystem.inventory.data.plant.parameter.INorminalPlantOperationParameter;
import org.cocome.tradingsystem.inventory.data.plant.productionunit.IProductionUnitClass;
import org.cocome.tradingsystem.inventory.data.plant.productionunit.IProductionUnitOperation;
import org.cocome.tradingsystem.inventory.data.plant.productionunit.ProductionUnitClass;
import org.cocome.tradingsystem.inventory.data.plant.productionunit.ProductionUnitOperation;
import org.cocome.tradingsystem.inventory.data.plant.recipe.IPlantOperation;
import org.cocome.tradingsystem.util.exception.NotInDatabaseException;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.inject.Provider;
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
    private IEnterpriseDataFactory enterpriseDatatypes;

    @Override
    public IProductionUnitClass getNewProductionUnitClass() {
        return productionUnitClassProvider.get();
    }

    @Override
    public IProductionUnitOperation getNewProductionUnitOperation() {
        return productionUnitOperationProvider.get();
    }

    @Override
    public IConditionalExpression getNewConditionalExpression() {
        return conditionalExpressionProvider.get();
    }

    @Override
    public ProductionUnitClassTO fillProductionUnitClassTO(IProductionUnitClass puc)
            throws NotInDatabaseException {
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
    public ConditionalExpressionTO fillConditionalExpressionTO(IConditionalExpression conditionalExpression) {
        //TODO
        return null;
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

    private List<ExpressionTO> fillExpressionTOs(List<IExpression> expressions) {
        //TODO
        return null;
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
    public BooleanPlantOperationParameterTO fillBooleanPlantOperationParameterTO(IBooleanPlantOperationParameter booleanPlantOperationParameter) throws NotInDatabaseException {
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
    public NorminalPlantOperationParameterTO fillNorminalPlantOperationParameterTO(INorminalPlantOperationParameter norminalPlantOperationParameter) throws NotInDatabaseException {
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
}
