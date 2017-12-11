package org.cocome.cloud.logic.webservice.plantservice;

import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;
import org.apache.cxf.jaxrs.client.JAXRSClientFactory;
import org.apache.log4j.Logger;
import org.cocome.cloud.logic.registry.client.IApplicationHelper;
import org.cocome.cloud.logic.webservice.ThrowingFunction;
import org.cocome.cloud.registry.service.Names;
import org.cocome.logic.webservice.plantservice.IPlantManager;
import org.cocome.tradingsystem.inventory.application.plant.PlantTO;
import org.cocome.tradingsystem.inventory.application.plant.iface.IPUInterface;
import org.cocome.tradingsystem.inventory.application.plant.iface.OperationEntry;
import org.cocome.tradingsystem.inventory.application.plant.productionunit.ProductionUnitClassTO;
import org.cocome.tradingsystem.inventory.application.plant.productionunit.ProductionUnitOperationTO;
import org.cocome.tradingsystem.inventory.application.plant.productionunit.ProductionUnitTO;
import org.cocome.tradingsystem.inventory.application.plant.pu.PUManager;
import org.cocome.tradingsystem.inventory.application.plant.recipe.PlantOperationOrderTO;
import org.cocome.tradingsystem.inventory.data.enterprise.IEnterpriseQuery;
import org.cocome.tradingsystem.inventory.data.persistence.IPersistenceContext;
import org.cocome.tradingsystem.inventory.data.persistence.UpdateException;
import org.cocome.tradingsystem.inventory.data.plant.IPlant;
import org.cocome.tradingsystem.inventory.data.plant.IPlantDataFactory;
import org.cocome.tradingsystem.inventory.data.plant.IPlantQuery;
import org.cocome.tradingsystem.inventory.data.plant.productionunit.IProductionUnit;
import org.cocome.tradingsystem.inventory.data.plant.productionunit.IProductionUnitClass;
import org.cocome.tradingsystem.inventory.data.plant.productionunit.IProductionUnitOperation;
import org.cocome.tradingsystem.inventory.data.plant.recipe.IPlantOperationOrder;
import org.cocome.tradingsystem.inventory.data.plant.recipe.IPlantOperationOrderEntry;
import org.cocome.tradingsystem.inventory.data.plant.recipe.IPlantOperationParameterValue;
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
import java.util.Collections;
import java.util.Date;

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
    private PUManager puManager;

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

    private <T1, T2> Collection<T2> queryCollectionByParentID(final long parentId,
                                                              final ThrowingFunction<Long, Collection<T1>, NotInDatabaseException> queryCommand,
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
    public Collection<ProductionUnitClassTO> queryProductionUnitClassesByPlantID(long plantId)
            throws NotInDatabaseException {
        return this.queryCollectionByParentID(plantId,
                plantQuery::queryProductionUnitClassesByPlantId,
                plantFactory::fillProductionUnitClassTO);
    }

    @Override
    public ProductionUnitClassTO queryProductionUnitClassByID(long productionUnitClassId)
            throws NotInDatabaseException {
        return plantFactory.fillProductionUnitClassTO(
                plantQuery.queryProductionUnitClass(productionUnitClassId));
    }

    @Override
    public long importProductionUnitClass(final String name,
                                          final String interfaceURL,
                                          final PlantTO plantTO) throws CreateException, NotInDatabaseException {
        final IPUInterface iface = JAXRSClientFactory.create(interfaceURL, IPUInterface.class,
                Collections.singletonList(new JacksonJsonProvider()));

        final IProductionUnitClass puc = plantFactory.getNewProductionUnitClass();
        puc.setPlant(enterpriseQuery.queryPlant(plantTO.getId()));
        puc.setPlantId(plantTO.getId());
        puc.setName(name);
        this.persistenceContext.createEntity(puc);

        for (final OperationEntry entry : iface.getOperations()) {
            final IProductionUnitOperation op = plantFactory.getNewProductionUnitOperation();
            op.setProductionUnitClass(puc);
            op.setExecutionDurationInMillis(2);
            op.setProductionUnitClassId(puc.getId());
            op.setOperationId(entry.getOperationId());
            op.setName(entry.getName());
            this.persistenceContext.createEntity(op);
        }

        return puc.getId();
    }

    @Override
    public long createProductionUnitClass(ProductionUnitClassTO productionUnitClassTO)
            throws CreateException {
        final IProductionUnitClass puc = plantFactory.convertToProductionUnitClass(productionUnitClassTO);
        persistenceContext.createEntity(puc);
        return puc.getId();
    }

    @Override
    public void updateProductionUnitClass(ProductionUnitClassTO productionUnitClassTO)
            throws UpdateException, NotInDatabaseException {
        persistenceContext.updateEntity(plantFactory.convertToProductionUnitClass(productionUnitClassTO));
    }

    @Override
    public void deleteProductionUnitClass(ProductionUnitClassTO productionUnitClassTO)
            throws UpdateException, NotInDatabaseException {
        final IProductionUnitClass puc =
                plantQuery.queryProductionUnitClass(productionUnitClassTO.getId());
        persistenceContext.deleteEntity(puc);
    }

    /* CRUD for {@link ProductionUnitOperationTO} **************/

    @Override
    public Collection<ProductionUnitOperationTO> queryProductionUnitOperationsByProductionUnitClassID(final long productionUnitClassId)
            throws NotInDatabaseException {
        return this.queryCollectionByParentID(productionUnitClassId,
                plantQuery::queryProductionUnitOperationsByProductionUnitClassId,
                plantFactory::fillProductionUnitOperationTO);
    }

    @Override
    public ProductionUnitOperationTO queryProductionUnitOperationByID(final long productionUnitOperationId)
            throws NotInDatabaseException {
        return plantFactory.fillProductionUnitOperationTO(
                plantQuery.queryProductionUnitOperation(productionUnitOperationId));
    }

    @Override
    public long createProductionUnitOperation(final ProductionUnitOperationTO productionUnitOperationTO) throws CreateException {
        final IProductionUnitOperation operation = plantFactory.convertToProductionUnitOperation(productionUnitOperationTO);
        persistenceContext.createEntity(operation);
        return operation.getId();
    }

    @Override
    public void updateProductionUnitOperation(final ProductionUnitOperationTO productionUnitOperationTO)
            throws NotInDatabaseException, UpdateException {
        persistenceContext.updateEntity(plantFactory.convertToProductionUnitOperation(productionUnitOperationTO));
    }

    @Override
    public void deleteProductionUnitOperation(final ProductionUnitOperationTO productionUnitOperationTO)
            throws NotInDatabaseException, UpdateException {
        final IProductionUnitOperation puc = plantQuery.queryProductionUnitOperation(productionUnitOperationTO.getId());
        persistenceContext.deleteEntity(puc);
    }

    /* CRUD for {@link ProductionUnitTO} **************/

    @Override
    public ProductionUnitTO queryProductionUnitByID(final long productionUnitId) throws NotInDatabaseException {
        return plantFactory.fillProductionUnitTO(
                plantQuery.queryProductionUnit(productionUnitId));
    }

    @Override
    public Collection<ProductionUnitTO> queryProductionUnitsByPlantID(long plantId) throws NotInDatabaseException {
        return this.queryCollectionByParentID(plantId,
                plantQuery::queryProductionUnits,
                plantFactory::fillProductionUnitTO);
    }


    @Override
    public long createProductionUnit(final ProductionUnitTO productionUnitTO) throws CreateException {
        final IProductionUnit operation = plantFactory.convertToProductionUnit(productionUnitTO);
        persistenceContext.createEntity(operation);
        //TODO throw NotInDatabaseExceution
        try {
            puManager.addPUToWorkerPool(operation);
        } catch (NotInDatabaseException e) {
            throw new CreateException("Unable to create worker");
        }
        return operation.getId();
    }

    @Override
    public void updateProductionUnit(final ProductionUnitTO productionUnitTO)
            throws NotInDatabaseException, UpdateException {
        persistenceContext.updateEntity(plantFactory.convertToProductionUnit(productionUnitTO));
    }

    @Override
    public void deleteProductionUnit(final ProductionUnitTO productionUnitTO)
            throws NotInDatabaseException, UpdateException {
        final IProductionUnit puc = plantQuery.queryProductionUnit(productionUnitTO.getId());
        persistenceContext.deleteEntity(puc);
    }

    /* Business Logic **************/

    @Override
    public PlantOperationOrderTO queryPlantOperationOrderById(final long plantOperationOrderId) throws NotInDatabaseException {
        return plantFactory.fillPlantOperationOrderTO(
                plantQuery.queryPlantOperationOrderById(plantOperationOrderId));
    }

    @Override
    public long orderOperation(final PlantOperationOrderTO plantOperationOrderTO)
            throws NotInDatabaseException, CreateException, UpdateException {
        final IPlantOperationOrder order = plantFactory.convertToPlantOperationOrder(plantOperationOrderTO);
        order.check();
        order.setOrderingDate(new Date());
        persistOrder(order);
        puManager.submitOrder(order);
        return order.getId();
    }

    private void persistOrder(IPlantOperationOrder order) throws CreateException {
        persistenceContext.createEntity(order);
        for (final IPlantOperationOrderEntry entry : order.getOrderEntries()) {
            persistenceContext.createEntity(entry, order);
            for (final IPlantOperationParameterValue values : entry.getParameterValues()) {
                persistenceContext.createEntity(values, entry);
            }
        }
    }
}
