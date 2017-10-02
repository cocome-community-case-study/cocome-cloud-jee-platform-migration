package org.cocome.tradingsystem.inventory.data.plant;

import org.cocome.tradingsystem.inventory.application.plant.expression.ConditionalExpressionTO;
import org.cocome.tradingsystem.inventory.application.plant.productionunit.ProductionUnitClassTO;
import org.cocome.tradingsystem.inventory.application.plant.productionunit.ProductionUnitOperationTO;
import org.cocome.tradingsystem.inventory.data.enterprise.IEnterpriseDataFactory;
import org.cocome.tradingsystem.inventory.data.plant.expression.ConditionalExpression;
import org.cocome.tradingsystem.inventory.data.plant.expression.IConditionalExpression;
import org.cocome.tradingsystem.inventory.data.plant.productionunit.IProductionUnitClass;
import org.cocome.tradingsystem.inventory.data.plant.productionunit.IProductionUnitOperation;
import org.cocome.tradingsystem.inventory.data.plant.productionunit.ProductionUnitClass;
import org.cocome.tradingsystem.inventory.data.plant.productionunit.ProductionUnitOperation;
import org.cocome.tradingsystem.util.exception.NotInDatabaseException;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.inject.Provider;

@Dependent
class PlantDatatypesFactory implements IPlantDataFactory {

    @Inject
    private Provider<ProductionUnitClass> productionUnitClassProvider;

    @Inject
    private Provider<ProductionUnitOperation> productionUnitOperationProvider;

    @Inject
    private Provider<ConditionalExpression> conditionalExpressionProvider;

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
}
