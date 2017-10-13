package org.cocome.cloud.logic.webservice.plantservice;

import org.apache.log4j.Logger;
import org.cocome.cloud.logic.registry.client.IApplicationHelper;
import org.cocome.cloud.logic.webservice.ThrowingFunction;
import org.cocome.cloud.logic.webservice.ThrowingSupplier;
import org.cocome.cloud.registry.service.Names;
import org.cocome.logic.webservice.plantservice.IPlantManager;
import org.cocome.tradingsystem.inventory.application.plant.expression.ConditionalExpressionTO;
import org.cocome.tradingsystem.inventory.application.plant.productionunit.ProductionUnitClassTO;
import org.cocome.tradingsystem.inventory.application.plant.productionunit.ProductionUnitOperationTO;
import org.cocome.tradingsystem.inventory.application.plant.recipe.PlantOperationOrderTO;
import org.cocome.tradingsystem.inventory.data.enterprise.IEnterpriseQuery;
import org.cocome.tradingsystem.inventory.data.persistence.IPersistenceContext;
import org.cocome.tradingsystem.inventory.data.persistence.UpdateException;
import org.cocome.tradingsystem.inventory.data.plant.IPlant;
import org.cocome.tradingsystem.inventory.data.plant.IPlantDataFactory;
import org.cocome.tradingsystem.inventory.data.plant.IPlantQuery;
import org.cocome.tradingsystem.inventory.data.plant.expression.IConditionalExpression;
import org.cocome.tradingsystem.inventory.data.plant.productionunit.IProductionUnitClass;
import org.cocome.tradingsystem.inventory.data.plant.productionunit.IProductionUnitOperation;
import org.cocome.tradingsystem.util.exception.NotInDatabaseException;
import org.cocome.tradingsystem.util.scope.CashDeskRegistry;
import org.cocome.tradingsystem.util.scope.IContextRegistry;
import org.cocome.tradingsystem.util.scope.RegistryKeys;

import javax.ejb.CreateException;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.jws.WebService;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.function.Function;

@WebService(
        serviceName = "IPlantManagerService",
        name = "IPlantManager",
        endpointInterface = "org.cocome.logic.webservice.plantservice.IPlantManager",
        targetNamespace = "http://plant.webservice.logic.cocome.org/")
@Stateless
public class PlantManager implements IPlantManager {

    @Inject
    private IEnterpriseQuery enterpriseQuery;

    @Inject
    private IPersistenceContext persistenceContext;

    @Inject
    private IApplicationHelper applicationHelper;

    @Inject
    private IPlantDataFactory plantFactory;

    @Inject
    private IPlantQuery plantQuery;

    @Inject
    private String plantManagerWSDL;

    @Inject
    private long defaultPlantIndex;

    private static final Logger LOG = Logger.getLogger(PlantManager.class);

    private void setContextRegistry(long plantID) throws NotInDatabaseException {
        LOG.debug("Setting plant to store with id " + plantID);
        IPlant store = enterpriseQuery.queryPlant(plantID);
        long enterpriseID = store.getEnterprise().getId();

        IContextRegistry registry = new CashDeskRegistry("plant#" + plantID);
        registry.putLong(RegistryKeys.PLANT_ID, plantID);
        registry.putLong(RegistryKeys.ENTERPRISE_ID, enterpriseID);

        try {
            applicationHelper.registerComponent(
                    Names.getPlantManagerRegistryName(defaultPlantIndex),
                    plantManagerWSDL,
                    false);
            applicationHelper.registerComponent(
                    Names.getStoreManagerRegistryName(plantID),
                    plantManagerWSDL,
                    false);
        } catch (URISyntaxException e) {
            LOG.error("Error registering component: " + e.getMessage());
        }
    }

    private <T1, T2> Collection<T2> queryCollectionByID(final ThrowingSupplier<Collection<T1>, NotInDatabaseException> queryCommand,
                                                        final ThrowingFunction<T1, T2, NotInDatabaseException> conversionCommand)
            throws NotInDatabaseException {
        Collection<T1> instances = queryCommand.get();
        Collection<T2> toInstances = new ArrayList<>(instances.size());
        for (T1 instance : instances) {
            toInstances.add(conversionCommand.apply(instance));
        }
        return toInstances;
    }

    private <T1, T2> Collection<T2> queryCollectionByParentID(final long parentId,
                                                              final Function<Long, Collection<T1>> queryCommand,
                                                              final ThrowingFunction<T1, T2, NotInDatabaseException> conversionCommand)
            throws NotInDatabaseException {
        //setContextRegistry(parentId);
        Collection<T1> instances = queryCommand.apply(parentId);
        Collection<T2> toInstances = new ArrayList<>(instances.size());
        for (T1 instance : instances) {
            toInstances.add(conversionCommand.apply(instance));
        }
        return toInstances;
    }

    /* CRUD for {@link ProductionUnitClassTO} **************/

    @Override
    public Collection<ProductionUnitClassTO> queryProductionUnitClassesByPlantID(long plantId) throws NotInDatabaseException {
        return this.queryCollectionByParentID(plantId,
                plantQuery::queryProductionUnitClassesByPlantId,
                plantFactory::fillProductionUnitClassTO);
    }

    @Override
    public ProductionUnitClassTO queryProductionUnitClassByID(long productionUnitClassId) throws NotInDatabaseException {
        return plantFactory.fillProductionUnitClassTO(
                plantQuery.queryProductionUnitClass(productionUnitClassId));
    }

    @Override
    public long createProductionUnitClass(ProductionUnitClassTO productionUnitClassTO) throws CreateException {
        final IProductionUnitClass puc = plantFactory.convertToProductionUnitClass(productionUnitClassTO);
        persistenceContext.createEntity(puc);
        return puc.getId();
    }

    @Override
    public void updateProductionUnitClass(ProductionUnitClassTO productionUnitClassTO) throws UpdateException, NotInDatabaseException {
        persistenceContext.updateEntity(plantFactory.convertToProductionUnitClass(productionUnitClassTO));
    }

    @Override
    public void deleteProductionUnitClass(ProductionUnitClassTO productionUnitClassTO) throws UpdateException, NotInDatabaseException {
        final IProductionUnitClass puc =
                plantQuery.queryProductionUnitClass(productionUnitClassTO.getId());
        persistenceContext.deleteEntity(puc);
    }

    /* CRUD for {@link ProductionUnitOperationTO} **************/

    @Override
    public Collection<ProductionUnitOperationTO> queryProductionUnitOperationsByProductionUnitClassID(long productionUnitClassId)
            throws NotInDatabaseException {
        return this.queryCollectionByParentID(productionUnitClassId,
                plantQuery::queryProductionUnitOperationsByProductionUnitClassId,
                plantFactory::fillProductionUnitOperationTO);
    }

    @Override
    public ProductionUnitOperationTO queryProductionUnitOperationByID(long productionUnitOperationId) throws NotInDatabaseException {
        return plantFactory.fillProductionUnitOperationTO(
                plantQuery.queryProductionUnitOperation(productionUnitOperationId));
    }

    @Override
    public long createProductionUnitOperation(ProductionUnitOperationTO productionUnitOperationTO) throws CreateException {
        final IProductionUnitOperation operation = plantFactory.convertToProductionUnitOperation(productionUnitOperationTO);
        persistenceContext.createEntity(operation);
        return operation.getId();
    }

    @Override
    public void updateProductionUnitOperation(ProductionUnitOperationTO productionUnitOperationTO)
            throws NotInDatabaseException, UpdateException {
        persistenceContext.updateEntity(plantFactory.convertToProductionUnitOperation(productionUnitOperationTO));
    }

    @Override
    public void deleteProductionUnitOperation(ProductionUnitOperationTO productionUnitOperationTO)
            throws NotInDatabaseException, UpdateException {
        final IProductionUnitOperation puc = plantQuery.queryProductionUnitOperation(productionUnitOperationTO.getId());
        persistenceContext.deleteEntity(puc);
    }

    /* CRUD for {@link ConditionalExpressionTO} **************/

    @Override
    public ConditionalExpressionTO queryConditionalExpressionByID(long conditionalExpressionId) throws NotInDatabaseException {
        return plantFactory.fillConditionalExpressionTO(
                plantQuery.queryConditionalExpression(conditionalExpressionId));
    }

    @Override
    public long createConditionalExpression(ConditionalExpressionTO conditionalExpressionTO) throws CreateException {
        final IConditionalExpression expression = plantFactory.convertToConditionalExpression(conditionalExpressionTO);
        persistenceContext.createEntity(expression);
        return expression.getId();
    }

    @Override
    public void updateConditionalExpression(ConditionalExpressionTO conditionalExpressionTO)
            throws NotInDatabaseException, UpdateException {
        persistenceContext.updateEntity(plantFactory.convertToConditionalExpression(conditionalExpressionTO));
    }

    @Override
    public void deleteConditionalExpression(ConditionalExpressionTO conditionalExpressionTO) throws NotInDatabaseException,
            UpdateException {
        final IConditionalExpression expression = plantQuery.queryConditionalExpression(conditionalExpressionTO.getId());
        persistenceContext.deleteEntity(expression);
    }

    @Override
    public PlantOperationOrderTO queryPlantOperationOrderById(long plantOperationOrderId) throws NotInDatabaseException {
        return plantFactory.fillPlantOperationOrderTO(
                plantQuery.queryPlantOperationOrderById(plantOperationOrderId));
    }

    @Override
    public long orderOperation(PlantOperationOrderTO plantOperationOrderTO) throws NotInDatabaseException, CreateException {
        return 0;
    }
}
