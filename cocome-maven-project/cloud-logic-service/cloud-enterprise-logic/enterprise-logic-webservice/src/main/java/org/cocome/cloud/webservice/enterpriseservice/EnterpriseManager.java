package org.cocome.cloud.webservice.enterpriseservice;

import org.apache.log4j.Logger;
import org.cocome.cloud.logic.registry.client.IApplicationHelper;
import org.cocome.cloud.logic.webservice.DBCreateAction;
import org.cocome.cloud.logic.webservice.DBObjectSupplier;
import org.cocome.cloud.logic.webservice.DBUpdateAction;
import org.cocome.cloud.logic.webservice.ThrowingFunction;
import org.cocome.cloud.registry.service.Names;
import org.cocome.logic.webservice.enterpriseservice.IEnterpriseManager;
import org.cocome.tradingsystem.inventory.application.enterprise.CustomProductTO;
import org.cocome.tradingsystem.inventory.application.plant.PlantTO;
import org.cocome.tradingsystem.inventory.application.plant.parameter.BooleanParameterTO;
import org.cocome.tradingsystem.inventory.application.plant.parameter.NominalParameterTO;
import org.cocome.tradingsystem.inventory.application.plant.parameter.ParameterTO;
import org.cocome.tradingsystem.inventory.application.plant.recipe.*;
import org.cocome.tradingsystem.inventory.application.production.ProductionManager;
import org.cocome.tradingsystem.inventory.application.production.PlantOperationOrderFinishedEvent;
import org.cocome.tradingsystem.inventory.application.store.*;
import org.cocome.tradingsystem.inventory.data.enterprise.*;
import org.cocome.tradingsystem.inventory.data.persistence.IPersistenceContext;
import org.cocome.tradingsystem.inventory.data.persistence.UpdateException;
import org.cocome.tradingsystem.inventory.data.plant.IPlant;
import org.cocome.tradingsystem.inventory.data.plant.IPlantDataFactory;
import org.cocome.tradingsystem.inventory.data.plant.parameter.IBooleanParameter;
import org.cocome.tradingsystem.inventory.data.plant.parameter.INominalParameter;
import org.cocome.tradingsystem.inventory.data.plant.recipe.*;
import org.cocome.tradingsystem.inventory.data.plant.recipe.exec.RecipeExecutionGraph;
import org.cocome.tradingsystem.inventory.data.store.IStore;
import org.cocome.tradingsystem.inventory.data.store.IStoreDataFactory;
import org.cocome.tradingsystem.util.exception.NotInDatabaseException;
import org.cocome.tradingsystem.util.exception.RecipeException;
import org.cocome.tradingsystem.util.scope.CashDeskRegistry;
import org.cocome.tradingsystem.util.scope.ICashDeskRegistryFactory;
import org.cocome.tradingsystem.util.scope.IContextRegistry;
import org.cocome.tradingsystem.util.scope.RegistryKeys;

import javax.ejb.CreateException;
import javax.ejb.Stateless;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.jws.WebService;
import java.net.URISyntaxException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * The enterprise service implementation
 *
 * @author Rudolf Biczok
 * @author Tobias Pöppke
 * @author Robert Heinrich
 */
@WebService(
        serviceName = "IEnterpriseManagerService",
        name = "IEnterpriseManager",
        endpointInterface = "org.cocome.logic.webservice.enterpriseservice.IEnterpriseManager",
        targetNamespace = "http://enterprise.webservice.logic.cocome.org/")
@Stateless
public class EnterpriseManager implements IEnterpriseManager {

    private static final Logger LOG = Logger.getLogger(EnterpriseManager.class);

    @Inject
    private IEnterpriseQuery enterpriseQuery;

    @Inject
    private IPersistenceContext persistenceContext;

    @Inject
    private ICashDeskRegistryFactory registryFact;

    @Inject
    private IApplicationHelper applicationHelper;

    @Inject
    private String enterpriseServiceWSDL;

    @Inject
    private String enterpriseReportingWSDL;

    @Inject
    private String loginManagerWSDL;

    @Inject
    private IEnterpriseDataFactory enterpriseFactory;

    @Inject
    private IStoreDataFactory storeFactory;

    @Inject
    private IPlantDataFactory plantFactory;

    @Inject
    private ProductionManager productionManager;

    @Inject
    private Event<PlantOperationOrderFinishedEvent> orderFinishedEvent;

    private void setContextRegistry(long enterpriseID) throws NotInDatabaseException {
        ITradingEnterprise enterprise;
        try {
            enterprise = enterpriseQuery
                    .queryEnterpriseById(enterpriseID);
        } catch (NotInDatabaseException e) {
            LOG.error("Got NotInDatabaseException for enterprise: " + e);
            e.printStackTrace();
            throw e;
        }

        IContextRegistry registry = new CashDeskRegistry("enterprise#" + enterpriseID);
        registry.putLong(RegistryKeys.ENTERPRISE_ID, enterpriseID);
        registryFact.setEnterpriseContext(registry);

        try {
            registerEnterpriseComponents(enterprise);
        } catch (URISyntaxException e) {
            LOG.error("Error registering components: " + e.getMessage());
        }

    }

    private void registerEnterpriseComponents(ITradingEnterprise enterprise) throws URISyntaxException {
        applicationHelper.registerComponent(Names.getEnterpriseManagerRegistryName(enterprise.getId()),
                enterpriseServiceWSDL, false);
        applicationHelper.registerComponent(Names.getEnterpriseManagerRegistryName(enterprise.getName()),
                enterpriseServiceWSDL, false);
        applicationHelper.registerComponent(Names.getEnterpriseReportingRegistryName(enterprise.getId()),
                enterpriseReportingWSDL, false);
        applicationHelper.registerComponent(Names.getLoginManagerRegistryName(enterprise.getId()),
                loginManagerWSDL, false);
    }

    @Override
    public EnterpriseTO queryEnterpriseById(long enterpriseId) throws NotInDatabaseException {
        setContextRegistry(enterpriseId);
        final ITradingEnterprise enterprise =
                saveFetchFromDB(() -> enterpriseQuery
                        .queryEnterpriseById(enterpriseId));
        return enterpriseFactory.fillEnterpriseTO(enterprise);
    }

    @Override
    public long getMeanTimeToDelivery(SupplierTO supplierTO, EnterpriseTO enterpriseTO) throws NotInDatabaseException {
        setContextRegistry(enterpriseTO.getId());

        IProductSupplier supplier;
        ITradingEnterprise enterprise;
        try {
            supplier = enterpriseQuery.querySupplierByID(supplierTO.getId());
            enterprise = enterpriseQuery.queryEnterpriseById(enterpriseTO.getId());
        } catch (NotInDatabaseException e) {
            LOG.error("Got NotInDatabaseException: " + e);
            e.printStackTrace();
            throw e;
        }

        return enterpriseQuery.getMeanTimeToDelivery(supplier, enterprise);
    }

    @Override
    public Collection<ProductTO> getAllEnterpriseProducts(long enterpriseId) throws NotInDatabaseException {
        setContextRegistry(enterpriseId);

        final Collection<IProduct> products =
                saveFetchFromDB(() -> enterpriseQuery.queryAllProducts(enterpriseId));
        return products.stream()
                .map(enterpriseFactory::fillProductTO)
                .collect(Collectors.toList());
    }

    @Override
    public long createEnterprise(String enterpriseName) throws CreateException {
        ITradingEnterprise enterprise = enterpriseFactory.getNewTradingEnterprise();
        enterprise.setName(enterpriseName);
        try {
            persistenceContext.createEntity(enterprise);
            registerEnterpriseComponents(enterprise);
        } catch (CreateException e) {
            LOG.error("Got CreateException: " + e);
            e.printStackTrace();
            throw e;
        } catch (URISyntaxException e) {
            LOG.error("Got URISyntaxException: " + e.getMessage());
            e.printStackTrace();
            throw new CreateException(e.getMessage());
        }
        return enterprise.getId();
    }

    @Override
    public long createStore(StoreWithEnterpriseTO storeTO) throws CreateException {
        IStore store = storeFactory.getNewStore();
        store.setEnterpriseName(storeTO.getEnterpriseTO().getName());
        store.setLocation(storeTO.getLocation());
        store.setName(storeTO.getName());
        try {
            persistenceContext.createEntity(store);
        } catch (CreateException e) {
            LOG.error("Got CreateException: " + e);
            e.printStackTrace();
            throw e;
        }
        return store.getId();
    }

    @Override
    public long createPlant(PlantTO plantTO) throws CreateException {
        IPlant plant = enterpriseFactory.getNewPlant();
        plant.setEnterpriseId(plantTO.getEnterpriseTO().getId());
        plant.setLocation(plantTO.getLocation());
        plant.setName(plantTO.getName());

        saveDBCreateAction(() -> persistenceContext.createEntity(plant));
        return plant.getId();
    }

    @Override
    public Collection<EnterpriseTO> getEnterprises() {
        Collection<ITradingEnterprise> enterprises = enterpriseQuery.queryAllEnterprises();
        Collection<EnterpriseTO> enterpriseTOs = new ArrayList<>(enterprises.size());
        for (ITradingEnterprise enterprise : enterprises) {
            enterpriseTOs.add(enterpriseFactory.fillEnterpriseTO(enterprise));
        }
        return enterpriseTOs;
    }

    @Override
    public Collection<StoreWithEnterpriseTO> queryStoresByEnterpriseID(long enterpriseId)
            throws NotInDatabaseException {
        setContextRegistry(enterpriseId);
        return queryCollection(enterpriseQuery.queryStoresByEnterpriseId(enterpriseId),
                storeFactory::fillStoreWithEnterpriseTO);
    }

    @Override
    public StoreWithEnterpriseTO queryStoreByID(long storeId) throws NotInDatabaseException {
        return storeFactory.fillStoreWithEnterpriseTO(enterpriseQuery.queryStoreByID(storeId));
    }

    @Override
    public Collection<PlantTO> queryPlantsByEnterpriseID(long enterpriseId)
            throws NotInDatabaseException {
        return this.queryCollection(enterpriseQuery.queryPlantsByEnterpriseId(enterpriseId),
                enterpriseFactory::fillPlantTO);
    }

    @Override
    public void updateStore(StoreWithEnterpriseTO storeTO)
            throws NotInDatabaseException, UpdateException {
        IStore store;
        try {
            store = enterpriseQuery.queryStoreByEnterprise(
                    storeTO.getEnterpriseTO().getId(), storeTO.getId());
        } catch (NotInDatabaseException e) {
            LOG.error("Got NotInDatabaseException: " + e);
            e.printStackTrace();
            throw e;
        }

        ITradingEnterprise enterprise;
        try {
            enterprise = enterpriseQuery.queryEnterpriseById(
                    storeTO.getEnterpriseTO().getId());
        } catch (NotInDatabaseException e) {
            LOG.error("Got NotInDatabaseException: " + e);
            e.printStackTrace();
            throw e;
        }

        store.setEnterprise(enterprise);
        store.setEnterpriseName(enterprise.getName());
        store.setLocation(storeTO.getLocation());
        store.setName(storeTO.getName());

        try {
            persistenceContext.updateEntity(store);
        } catch (UpdateException e) {
            LOG.error("Got UpdateException: " + e);
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public void updatePlant(PlantTO plantTO)
            throws NotInDatabaseException, UpdateException {
        final ITradingEnterprise enterprise = saveFetchFromDB(() ->
                enterpriseQuery.queryEnterpriseById(
                        plantTO.getEnterpriseTO().getId()));

        final IPlant plant = saveFetchFromDB(() -> enterpriseQuery.queryPlant(plantTO.getId()));

        plant.setEnterprise(enterprise);
        plant.setEnterpriseId(enterprise.getId());
        plant.setLocation(plantTO.getLocation());
        plant.setName(plantTO.getName());

        saveDBUpdateAction(() -> persistenceContext.updateEntity(plant));
    }

    @Override
    public long createProduct(ProductTO productTO)
            throws CreateException {
        IProduct product = enterpriseFactory.convertToProduct(productTO);

        saveDBCreateAction(() -> persistenceContext.createEntity(product));

        return product.getId();
    }

    @Override
    public void updateProduct(ProductWithSupplierTO productTO)
            throws NotInDatabaseException, UpdateException {
        final IProduct product = saveFetchFromDB(() -> queryProduct(productTO.getProductTO()));

        product.setName(productTO.getProductTO().getName());
        product.setPurchasePrice(productTO.getProductTO().getPurchasePrice());

        if (productTO.getSupplierTO().getId() != 0) {
            IProductSupplier supplier;
            try {
                supplier = enterpriseQuery.querySupplierByID(productTO.getSupplierTO().getId());
            } catch (NotInDatabaseException e) {
                LOG.error("Got NotInDatabaseException: " + e);
                e.printStackTrace();
                throw e;
            }
            product.setSupplier(supplier);
        }

        saveDBUpdateAction(() -> persistenceContext.updateEntity(product));
    }

    @Override
    public Collection<ProductTO> getAllProducts() {
        Collection<IProduct> products = enterpriseQuery.queryAllProducts();
        Collection<ProductTO> productTOs = new ArrayList<>(products.size());

        for (IProduct product : products) {
            productTOs.add(enterpriseFactory.fillProductTO(product));
        }
        return productTOs;
    }

    @Override
    public Collection<CustomProductTO> getAllCustomProducts() {
        return enterpriseQuery.queryAllCustomProducts()
                .stream()
                .map(enterpriseFactory::fillCustomProductTO)
                .collect(Collectors.toList());
    }

    @Override
    public CustomProductTO queryCustomProductByID(long customProductID) throws NotInDatabaseException {
        ICustomProduct product = enterpriseQuery.queryCustomProductByID(customProductID);
        return enterpriseFactory.fillCustomProductTO(product);
    }

    @Override
    public CustomProductTO queryCustomProductByBarcode(long barcode) throws NotInDatabaseException {
        ICustomProduct product = enterpriseQuery.queryCustomProductByBarcode(barcode);
        return enterpriseFactory.fillCustomProductTO(product);
    }

    @Override
    public ProductTO getProductByID(long productID) throws NotInDatabaseException {
        IProduct product = enterpriseQuery.queryProductByID(productID);
        return enterpriseFactory.fillProductTO(product);
    }

    @Override
    public ProductTO getProductByBarcode(long barcode) throws NotInDatabaseException {
        IProduct product = enterpriseQuery.queryProductByBarcode(barcode);
        return enterpriseFactory.fillProductTO(product);
    }

    @Override
    public EnterpriseTO queryEnterpriseByName(String enterpriseName) throws NotInDatabaseException {
        ITradingEnterprise enterprise;
        try {
            enterprise = enterpriseQuery
                    .queryEnterpriseByName(enterpriseName);
        } catch (NotInDatabaseException e) {
            LOG.error("Got NotInDatabaseException for enterprise: " + e);
            e.printStackTrace();
            throw e;
        }
        setContextRegistry(enterprise.getId());
        EnterpriseTO enterpriseTO = enterpriseFactory.fillEnterpriseTO(enterprise);

        LOG.debug(String.format("Assembled EnterpriseTO: [%d, %s]", enterpriseTO.getId(), enterpriseTO.getName()));

        return enterpriseTO;
    }

    @Override
    public StoreWithEnterpriseTO queryStoreByEnterpriseID(long enterpriseID, long storeID)
            throws NotInDatabaseException {
        return storeFactory.fillStoreWithEnterpriseTO(
                enterpriseQuery.queryStoreByEnterprise(enterpriseID, storeID));
    }

    @Override
    public PlantTO queryPlantByID(long plantId) throws NotInDatabaseException {
        return enterpriseFactory.fillPlantTO(
                enterpriseQuery.queryPlant(plantId));
    }

    @Override
    public Collection<ProductTO> getProductsBySupplier(long enterpriseID, long supplierID)
            throws NotInDatabaseException {
        Collection<IProduct> products = enterpriseQuery.queryProductsBySupplier(enterpriseID, supplierID);
        Collection<ProductTO> productTOs = new ArrayList<>(products.size());

        for (IProduct product : products) {
            productTOs.add(enterpriseFactory.fillProductTO(product));
        }

        return productTOs;
    }

    @Override
    public SupplierTO getSupplierByID(long supplierID) throws NotInDatabaseException {
        return enterpriseFactory.fillSupplierTO(enterpriseQuery.querySupplierByID(supplierID));
    }

    @Override
    public Collection<SupplierTO> querySuppliers(long enterpriseID) throws NotInDatabaseException {
        Collection<IProductSupplier> suppliers = enterpriseQuery.querySuppliers(enterpriseID);
        Collection<SupplierTO> supplierTOs = new ArrayList<>(suppliers.size());

        for (IProductSupplier supplier : suppliers) {
            supplierTOs.add(enterpriseFactory.fillSupplierTO(supplier));
        }
        return supplierTOs;
    }

    @Override
    public SupplierTO querySupplierForProduct(long enterpriseID, long productBarcode) throws NotInDatabaseException {
        IProductSupplier supplier = enterpriseQuery.querySupplierForProduct(enterpriseID, productBarcode);
        return enterpriseFactory.fillSupplierTO(supplier);
    }

    @Override
    public Collection<StoreWithEnterpriseTO> queryStoreByName(long enterpriseID, String storeName) throws NotInDatabaseException {
        Collection<IStore> stores = enterpriseQuery.queryStoreByName(enterpriseID, storeName);
        Collection<StoreWithEnterpriseTO> storeTOs = new ArrayList<>(stores.size());

        for (IStore store : stores) {
            storeTOs.add(storeFactory.fillStoreWithEnterpriseTO(store));
        }

        return storeTOs;
    }

    @Override
    public Collection<PlantTO> queryPlantByName(long enterpriseId, String plantName)
            throws NotInDatabaseException {
        Collection<IPlant> plants = enterpriseQuery.queryPlantByName(enterpriseId, plantName);
        Collection<PlantTO> plantTOs = new ArrayList<>(plants.size());

        for (IPlant store : plants) {
            plantTOs.add(enterpriseFactory.fillPlantTO(store));
        }

        return plantTOs;
    }

    @Override
    public void deleteEnterprise(EnterpriseTO enterpriseTO) throws NotInDatabaseException, UpdateException {
        final ITradingEnterprise enterprise = saveFetchFromDB(() ->
                enterpriseQuery.queryEnterpriseById(enterpriseTO.getId()));
        saveDBUpdateAction(() -> persistenceContext.deleteEntity(enterprise));
    }

    @Override
    public void deletePlant(PlantTO plantTO) throws NotInDatabaseException, UpdateException {
        final IPlant plant = saveFetchFromDB(() -> enterpriseQuery.queryPlant(plantTO.getId()));
        saveDBUpdateAction(() -> persistenceContext.deleteEntity(plant));
    }

    @Override
    public void deleteProduct(ProductTO productTO) throws NotInDatabaseException, UpdateException {
        final IProduct product = saveFetchFromDB(() -> queryProduct(productTO));
        saveDBUpdateAction(() -> persistenceContext.deleteEntity(product));
    }

    @Override
    public EntryPointTO queryEntryPointById(long entryPointId) throws NotInDatabaseException {
        return plantFactory.fillEntryPointTO(enterpriseQuery.queryEntryPointByID(entryPointId));
    }

    @Override
    public Collection<EntryPointTO> queryEntryPointsByRecipeOperationId(long recipeId) throws NotInDatabaseException {
        return queryCollection(enterpriseQuery.queryEntryPointsByRecipeOperationId(recipeId),
                plantFactory::fillEntryPointTO);
    }

    @Override
    public Collection<EntryPointTO> queryInputEntryPointsByRecipeOperationId(long operationId) throws NotInDatabaseException {
        Collection<IEntryPoint> entryPoints = enterpriseQuery.queryInputEntryPointsByRecipeOperationId(operationId);
        Collection<EntryPointTO> entryPointTOs = new ArrayList<>(entryPoints.size());

        for (IEntryPoint store : entryPoints) {
            entryPointTOs.add(plantFactory.fillEntryPointTO(store));
        }

        return entryPointTOs;
    }

    @Override
    public Collection<EntryPointTO> queryOutputEntryPointsByRecipeOperationId(long operationId) throws NotInDatabaseException {
        Collection<IEntryPoint> entryPoints = enterpriseQuery.queryOutputEntryPointsByRecipeOperationId(operationId);
        Collection<EntryPointTO> entryPointTOs = new ArrayList<>(entryPoints.size());

        for (IEntryPoint store : entryPoints) {
            entryPointTOs.add(plantFactory.fillEntryPointTO(store));
        }

        return entryPointTOs;
    }

    @Override
    public long createEntryPoint(EntryPointTO entryPointTO) throws CreateException, NotInDatabaseException {
        final IEntryPoint entryPoint = plantFactory.convertToEntryPoint(entryPointTO);
        saveDBCreateAction(() -> persistenceContext.createEntity(entryPoint));
        return entryPoint.getId();
    }

    @Override
    public void updateEntryPoint(EntryPointTO entryPointTO) throws UpdateException, NotInDatabaseException {
        final IEntryPoint entryPoint = plantFactory.convertToEntryPoint(entryPointTO);
        saveDBUpdateAction(() -> persistenceContext.updateEntity(entryPoint));
    }

    @Override
    public void deleteEntryPoint(EntryPointTO entryPointTO) throws UpdateException, NotInDatabaseException {
        final IEntryPoint entryPoint = saveFetchFromDB(() ->
                enterpriseQuery.queryEntryPointByID(entryPointTO.getId()));
        saveDBUpdateAction(() -> persistenceContext.deleteEntity(entryPoint));
    }

    @Override
    public Collection<PlantOperationTO> queryPlantOperationsByPlantId(long plantId) throws NotInDatabaseException {
        return queryCollection(enterpriseQuery.queryPlantOperationsByPlantId(plantId),
                plantFactory::fillPlantOperationTO);
    }

    @Override
    public Collection<RecipeNodeTO> queryRecipeNodesByRecipeId(long recipeId) throws NotInDatabaseException {
        Collection<IRecipeNode> instances = enterpriseQuery.queryRecipeNodesByRecipeId(recipeId);
        Collection<RecipeNodeTO> toInstances = new ArrayList<>(instances.size());
        for (IRecipeNode instance : instances) {
            try {
                toInstances.add(plantFactory.fillRecipeNodeTO(instance));
            } catch (NotInDatabaseException e) {
                LOG.error("Got NotInDatabaseException: " + e, e);
                throw e;
            }
        }
        return toInstances;
    }

    @Override
    public RecipeNodeTO queryRecipeNodeById(long recipeNodeId) throws NotInDatabaseException {
        return plantFactory.fillRecipeNodeTO(enterpriseQuery.queryRecipeNodeById(recipeNodeId));
    }

    @Override
    public long createRecipeNode(RecipeNodeTO recipeNodeTO) throws CreateException, NotInDatabaseException {
        final IRecipeNode recipeNopde = plantFactory.convertToRecipeNode(recipeNodeTO);
        saveDBCreateAction(() -> persistenceContext.createEntity(recipeNopde));
        return recipeNopde.getId();
    }

    @Override
    public void updateRecipeNode(RecipeNodeTO recipeNodeTO) throws UpdateException, NotInDatabaseException {
        final IRecipeNode param = plantFactory.convertToRecipeNode(recipeNodeTO);
        saveDBUpdateAction(() -> persistenceContext.updateEntity(param));
    }

    @Override
    public void deleteRecipeNode(RecipeNodeTO recipeNodeTO) throws UpdateException, NotInDatabaseException {
        final IRecipeNode param = plantFactory.convertToRecipeNode(recipeNodeTO);
        saveDBUpdateAction(() -> persistenceContext.deleteEntity(param));
    }

    @Override
    public PlantOperationTO queryPlantOperationById(long plantOperationId) throws NotInDatabaseException {
        return plantFactory.fillPlantOperationTO(
                enterpriseQuery.queryPlantOperationByID(plantOperationId));
    }

    @Override
    public long createPlantOperation(PlantOperationTO plantOperationTO) throws CreateException {
        final IPlantOperation param = plantFactory.convertToPlantOperation(plantOperationTO);
        saveDBCreateAction(() -> persistenceContext.createEntity(param));
        return param.getId();
    }

    @Override
    public void updatePlantOperation(PlantOperationTO plantOperationTO) throws UpdateException, NotInDatabaseException {
        final IPlantOperation param = plantFactory.convertToPlantOperation(plantOperationTO);
        saveDBUpdateAction(() -> persistenceContext.updateEntity(param));
    }

    @Override
    public void deletePlantOperation(PlantOperationTO plantOperationTO) throws UpdateException, NotInDatabaseException {
        final IPlantOperation param = saveFetchFromDB(() ->
                enterpriseQuery.queryPlantOperationByID(plantOperationTO.getId()));
        saveDBUpdateAction(() -> persistenceContext.deleteEntity(param));
    }

    @Override
    public Collection<BooleanParameterTO> queryBooleanParametersByRecipeOperationId(long operationId)
            throws NotInDatabaseException {
        return queryCollection(enterpriseQuery.queryBooleanParametersByRecipeOperationId(operationId),
                plantFactory::fillBooleanParameterTO);
    }

    @Override
    public BooleanParameterTO queryBooleanParameterById(long booleanParameterId)
            throws NotInDatabaseException {
        return plantFactory.fillBooleanParameterTO(
                enterpriseQuery.queryBooleanParameterByID(booleanParameterId));
    }

    @Override
    public long createBooleanParameter(BooleanParameterTO booleanParameterTO)
            throws CreateException {
        final IBooleanParameter param = plantFactory.convertToBooleanParameter(
                booleanParameterTO);
        saveDBCreateAction(() -> persistenceContext.createEntity(param));
        return param.getId();
    }

    @Override
    public void updateBooleanParameter(BooleanParameterTO booleanPlantOperationParameterTO)
            throws UpdateException, NotInDatabaseException {
        final IBooleanParameter param = plantFactory.convertToBooleanParameter(booleanPlantOperationParameterTO);
        saveDBUpdateAction(() -> persistenceContext.updateEntity(param));
    }

    @Override
    public void deleteBooleanParameter(BooleanParameterTO booleanPlantOperationParameterTO)
            throws UpdateException, NotInDatabaseException {
        final IBooleanParameter param = saveFetchFromDB(() ->
                enterpriseQuery.queryBooleanParameterByID(booleanPlantOperationParameterTO.getId()));
        saveDBUpdateAction(() -> persistenceContext.deleteEntity(param));
    }

    @Override
    public ParameterTO queryParameterById(long nominalParameterId)
            throws NotInDatabaseException {
        return plantFactory.fillParameterTO(
                enterpriseQuery.queryParameterById(nominalParameterId));
    }

    @Override
    public NominalParameterTO queryNominalParameterById(long nominalParameterId)
            throws NotInDatabaseException {
        return plantFactory.fillNominalParameterTO(
                enterpriseQuery.queryNominalParameterByID(nominalParameterId));
    }

    @Override
    public Collection<ParameterTO> queryParametersByRecipeOperationId(long operationId)
            throws NotInDatabaseException {
        return queryCollection(enterpriseQuery.queryParametersByRecipeOperationId(operationId),
                plantFactory::fillParameterTO);
    }

    @Override
    public Collection<NominalParameterTO> queryNominalParametersByRecipeOperationId(long operationId)
            throws NotInDatabaseException {
        return queryCollection(enterpriseQuery.queryNominalParametersByRecipeOperationId(operationId),
                plantFactory::fillNominalParameterTO);
    }

    @Override
    public long createNominalParameter(NominalParameterTO nominalParameterTO)
            throws CreateException {
        final INominalParameter param = plantFactory.convertToNominalParameter(nominalParameterTO);
        saveDBCreateAction(() -> persistenceContext.createEntity(param));
        return param.getId();
    }

    @Override
    public void updateNominalParameter(NominalParameterTO nominalParameterTO)
            throws UpdateException, NotInDatabaseException {
        final INominalParameter param = plantFactory.convertToNominalParameter(nominalParameterTO);
        saveDBUpdateAction(() -> persistenceContext.updateEntity(param));
    }

    @Override
    public void deleteNominalParameter(NominalParameterTO norminalParameterTO)
            throws UpdateException, NotInDatabaseException {
        final INominalParameter param = saveFetchFromDB(() ->
                enterpriseQuery.queryNominalParameterByID(norminalParameterTO.getId()));
        saveDBUpdateAction(() -> persistenceContext.deleteEntity(param));
    }

    @Override
    public Collection<EntryPointInteractionTO> queryEntryPointInteractionsByRecipeId(long recipeId)
            throws NotInDatabaseException {
        return queryCollection(enterpriseQuery.queryEntryPointInteractionsByRecipeId(recipeId),
                plantFactory::fillEntryPointInteractionTO);
    }

    @Override
    public EntryPointInteractionTO queryEntryPointInteractionById(long entryPointInteractionId)
            throws NotInDatabaseException {
        return plantFactory.fillEntryPointInteractionTO(
                enterpriseQuery.queryEntryPointInteractionByID(entryPointInteractionId));
    }

    @Override
    public long createEntryPointInteraction(EntryPointInteractionTO entryPointInteractionTO) throws CreateException {
        final IEntryPointInteraction param = plantFactory.convertToEntryPointInteraction(entryPointInteractionTO);
        saveDBCreateAction(() -> persistenceContext.createEntity(param));
        return param.getId();
    }

    @Override
    public void updateEntryPointInteraction(EntryPointInteractionTO entryPointInteractionTO)
            throws UpdateException, NotInDatabaseException {
        final IEntryPointInteraction param = plantFactory.convertToEntryPointInteraction(entryPointInteractionTO);
        saveDBUpdateAction(() -> persistenceContext.updateEntity(param));
    }

    @Override
    public void deleteEntryPointInteraction(EntryPointInteractionTO entryPointInteractionTO)
            throws UpdateException, NotInDatabaseException {
        final IEntryPointInteraction param = saveFetchFromDB(() ->
                enterpriseQuery.queryEntryPointInteractionByID(entryPointInteractionTO.getId()));
        saveDBUpdateAction(() -> persistenceContext.deleteEntity(param));
    }

    @Override
    public Collection<ParameterInteractionTO> queryParameterInteractionsByRecipeId(long recipeId) throws NotInDatabaseException {
        return queryCollection(enterpriseQuery.queryParameterInteractionsByRecipeId(recipeId),
                plantFactory::fillParameterInteractionTO);
    }

    @Override
    public ParameterInteractionTO queryParameterInteractionById(long parameterInteractionId) throws NotInDatabaseException {
        return plantFactory.fillParameterInteractionTO(
                enterpriseQuery.queryParameterInteractionByID(parameterInteractionId));
    }

    @Override
    public long createParameterInteraction(ParameterInteractionTO parameterInteractionTO) throws CreateException {
        final IParameterInteraction param =
                plantFactory.convertToParameterInteraction(parameterInteractionTO);
        saveDBCreateAction(() -> persistenceContext.createEntity(param));
        return param.getId();
    }

    @Override
    public void updateParameterInteraction(ParameterInteractionTO parameterInteractionTO)
            throws UpdateException, NotInDatabaseException {
        final IParameterInteraction param = plantFactory.convertToParameterInteraction(parameterInteractionTO);
        saveDBUpdateAction(() -> persistenceContext.updateEntity(param));
    }

    @Override
    public void deleteParameterInteraction(ParameterInteractionTO parameterInteractionTO)
            throws UpdateException, NotInDatabaseException {
        final IParameterInteraction param = saveFetchFromDB(() ->
                enterpriseQuery.queryParameterInteractionByID(parameterInteractionTO.getId()));
        saveDBUpdateAction(() -> persistenceContext.deleteEntity(param));
    }

    @Override
    public Collection<RecipeOperationTO> queryRecipeOperationsByEnterpriseId(long enterpriseId)
            throws NotInDatabaseException {
        final List<RecipeOperationTO> result = new LinkedList<>();
        result.addAll(this.queryRecipesByEnterpriseId(enterpriseId));
        for(final IPlant plant : enterpriseQuery.queryPlantsByEnterpriseId(enterpriseId)) {
            result.addAll(this.queryPlantOperationsByPlantId(plant.getId()));
        }
        return result;
    }

    @Override
    public RecipeOperationTO queryRecipeOperationById(long recipeOperationId) throws NotInDatabaseException {
        return plantFactory.fillRecipeOperationTO(
                enterpriseQuery.queryRecipeOperationById(recipeOperationId));
    }

    @Override
    public void validateRecipe(RecipeTO recipeTO) throws RecipeException, NotInDatabaseException {
        final IRecipe recipe = plantFactory.convertToRecipe(recipeTO);
        new RecipeExecutionGraph(recipe);
    }

    @Override
    public Collection<RecipeTO> queryRecipesByEnterpriseId(long enterpriseId) throws NotInDatabaseException {
        return queryCollection(enterpriseQuery.queryRecipesByEnterpriseId(enterpriseId),
                plantFactory::fillRecipeTO);
    }

    @Override
    public RecipeTO queryRecipeById(long recipeId) throws NotInDatabaseException {
        return plantFactory.fillRecipeTO(
                enterpriseQuery.queryRecipeByID(recipeId));
    }

    @Override
    public RecipeTO queryRecipeByCustomProductBarcode(long customProductBarcode) throws NotInDatabaseException {
        final IRecipe r = enterpriseQuery.queryRecipeByCustomProductBarcode(customProductBarcode);
        return plantFactory.fillRecipeTO(r);
    }

    @Override
    public long createRecipe(RecipeTO recipeTO) throws CreateException {
        final IRecipe param = plantFactory.convertToRecipe(recipeTO);
        saveDBCreateAction(() -> persistenceContext.createEntity(param));
        return param.getId();
    }

    @Override
    public void updateRecipe(RecipeTO recipeTO) throws UpdateException, NotInDatabaseException {
        final IRecipe param = plantFactory.convertToRecipe(recipeTO);
        saveDBUpdateAction(() -> persistenceContext.updateEntity(param));
    }

    @Override
    public void deleteRecipe(RecipeTO recipeTO) throws UpdateException, NotInDatabaseException {
        final IRecipe param = saveFetchFromDB(() ->
                enterpriseQuery.queryRecipeByID(recipeTO.getId()));
        saveDBUpdateAction(() -> persistenceContext.deleteEntity(param));
    }

    @Override
    public void onPlantOperationFinish(long plantOperationOrderEntryId) {
        LOG.info("Finished plant operation for order entry: " + plantOperationOrderEntryId);
    }

    @Override
    public void onPlantOperationOrderEntryFinish(long plantOperationOrderEntryId) {
        LOG.info("Finished plant operation order entry: " + plantOperationOrderEntryId);
    }

    @Override
    public void onPlantOperationOrderFinish(long plantOperationOrderId) {
        LOG.info("Finished plant operation order " + plantOperationOrderId);
        orderFinishedEvent.fire(new PlantOperationOrderFinishedEvent(plantOperationOrderId));
    }

    @Override
    public long submitProductionOrder(ProductionOrderTO productionOrderTO)
            throws NotInDatabaseException, CreateException, RecipeException {
        final IProductionOrder order = plantFactory.convertToProductionOrder(productionOrderTO);
        order.check();
        order.setOrderingDate(new Date());
        persistOrder(order);
        productionManager.submitOrder(order);
        return order.getId();
    }

    private IProduct queryProduct(ProductTO productTO)
            throws NotInDatabaseException {
        if (productTO.getId() != 0) {
            return enterpriseQuery.queryProductByID(productTO.getId());
        }
        return enterpriseQuery.queryProductByBarcode(productTO
                .getBarcode());
    }

    private <T1, T2> Collection<T2> queryCollection(Collection<T1> instances,
                                                    final ThrowingFunction<T1, T2, NotInDatabaseException> conversionCommand)
            throws NotInDatabaseException {
        Collection<T2> toInstances = new ArrayList<>(instances.size());
        for (T1 instance : instances) {
            try {
                toInstances.add(conversionCommand.apply(instance));
            } catch (NotInDatabaseException e) {
                LOG.error("Got NotInDatabaseException: " + e, e);
                throw e;
            }
        }
        return toInstances;
    }

    private <T> T saveFetchFromDB(DBObjectSupplier<T> supplier) throws NotInDatabaseException {
        try {
            return supplier.get();
        } catch (NotInDatabaseException e) {
            LOG.error("Got NotInDatabaseException: " + e, e);
            throw e;
        }
    }

    private void saveDBUpdateAction(DBUpdateAction action) throws UpdateException {
        try {
            action.perform();
        } catch (UpdateException e) {
            LOG.error("Got UpdateException: " + e, e);
            e.printStackTrace();
            throw e;
        }
    }

    private void saveDBCreateAction(DBCreateAction action) throws CreateException {
        try {
            action.perform();
        } catch (CreateException e) {
            LOG.error("Got CreateException: " + e, e);
            e.printStackTrace();
            throw e;
        }
    }

    //TODO code duplication (see PlantManager)
    private void persistOrder(IProductionOrder order) throws CreateException {
        persistenceContext.createEntity(order);
        for (final IProductionOrderEntry entry : order.getOrderEntries()) {
            entry.setOrder(order);
            entry.setOrderId(order.getId());
            persistenceContext.createEntity(entry);
            for (final IParameterValue value : entry.getParameterValues()) {
                value.setOrderEntry(entry);
                value.setOrderEntryId(entry.getId());
                persistenceContext.createEntity(value);
            }
        }
    }
}

