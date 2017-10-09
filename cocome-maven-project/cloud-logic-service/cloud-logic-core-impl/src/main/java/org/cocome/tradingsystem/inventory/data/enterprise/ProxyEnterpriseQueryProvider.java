package org.cocome.tradingsystem.inventory.data.enterprise;

import org.apache.log4j.Logger;
import org.cocome.cloud.logic.registry.client.IApplicationHelper;
import org.cocome.cloud.logic.stub.IEnterpriseManager;
import org.cocome.cloud.logic.stub.IEnterpriseManagerService;
import org.cocome.cloud.logic.stub.NotBoundException_Exception;
import org.cocome.cloud.logic.stub.NotInDatabaseException_Exception;
import org.cocome.cloud.logic.webservice.ThrowingFunction;
import org.cocome.cloud.registry.service.Names;
import org.cocome.tradingsystem.inventory.application.enterprise.parameter.BooleanCustomProductParameterTO;
import org.cocome.tradingsystem.inventory.application.enterprise.parameter.CustomProductParameterTO;
import org.cocome.tradingsystem.inventory.application.enterprise.parameter.NorminalCustomProductParameterTO;
import org.cocome.tradingsystem.inventory.application.plant.PlantTO;
import org.cocome.tradingsystem.inventory.application.plant.parameter.BooleanPlantOperationParameterTO;
import org.cocome.tradingsystem.inventory.application.plant.parameter.NorminalPlantOperationParameterTO;
import org.cocome.tradingsystem.inventory.application.plant.parameter.PlantOperationParameterTO;
import org.cocome.tradingsystem.inventory.application.store.EnterpriseTO;
import org.cocome.tradingsystem.inventory.application.store.ProductTO;
import org.cocome.tradingsystem.inventory.application.store.StoreWithEnterpriseTO;
import org.cocome.tradingsystem.inventory.application.store.SupplierTO;
import org.cocome.tradingsystem.inventory.data.enterprise.parameter.IBooleanCustomProductParameter;
import org.cocome.tradingsystem.inventory.data.enterprise.parameter.ICustomProductParameter;
import org.cocome.tradingsystem.inventory.data.enterprise.parameter.INorminalCustomProductParameter;
import org.cocome.tradingsystem.inventory.data.plant.IPlant;
import org.cocome.tradingsystem.inventory.data.plant.IPlantDataFactory;
import org.cocome.tradingsystem.inventory.data.plant.parameter.IBooleanPlantOperationParameter;
import org.cocome.tradingsystem.inventory.data.plant.parameter.INorminalPlantOperationParameter;
import org.cocome.tradingsystem.inventory.data.plant.parameter.IPlantOperationParameter;
import org.cocome.tradingsystem.inventory.data.plant.recipe.*;
import org.cocome.tradingsystem.inventory.data.store.IStore;
import org.cocome.tradingsystem.inventory.data.store.IStoreDataFactory;
import org.cocome.tradingsystem.util.exception.NotInDatabaseException;

import javax.inject.Inject;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

/**
 * Abstract class for enterprise query provider implementations
 * who send their queries to {@link IEnterpriseManager} firs.
 *
 * @author Rudolf Biczok
 */
public abstract class ProxyEnterpriseQueryProvider implements IEnterpriseQuery {
    private static final Logger LOG = Logger.getLogger(ProxyEnterpriseQueryProvider.class);

    @Inject
    private long defaultEnterpriseIndex;

    @Inject
    private IApplicationHelper applicationHelper;

    @Inject
    private IEnterpriseDataFactory enterpriseFactory;

    @Inject
    private IPlantDataFactory plantFactory;

    @Inject
    private IStoreDataFactory storeFactory;

    private IEnterpriseManager lookupEnterpriseManager(long enterpriseID) throws NotInDatabaseException {
        IEnterpriseManagerService enterpriseService;
        try {
            enterpriseService = applicationHelper.getComponent(Names.getEnterpriseManagerRegistryName(enterpriseID),
                    IEnterpriseManagerService.SERVICE, IEnterpriseManagerService.class);
        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
                | MalformedURLException | NoSuchMethodException | SecurityException | NotBoundException_Exception e) {
            if (enterpriseID == defaultEnterpriseIndex) {
                throw new NotInDatabaseException(
                        "Exception occured while retrieving the enterprise service: " + e.getMessage());
            } else {
                LOG.info("Looking up default enterprise server because there was none registered for id "
                        + enterpriseID);
                return lookupEnterpriseManager(defaultEnterpriseIndex);
            }
        }
        return enterpriseService.getIEnterpriseManagerPort();
    }

    private IEnterpriseManager lookupEnterpriseManager(String enterpriseName) throws NotInDatabaseException {
        IEnterpriseManagerService enterpriseService;
        try {
            enterpriseService = applicationHelper.getComponent(Names.getEnterpriseManagerRegistryName(enterpriseName),
                    IEnterpriseManagerService.SERVICE, IEnterpriseManagerService.class);
        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
                | MalformedURLException | NoSuchMethodException | SecurityException | NotBoundException_Exception e) {
            LOG.info("Looking up default enterprise server because there was none registered for name "
                    + enterpriseName);
            return lookupEnterpriseManager(defaultEnterpriseIndex);
        }
        return enterpriseService.getIEnterpriseManagerPort();
    }

    @Override
    public ITradingEnterprise queryEnterpriseById(long enterpriseID) throws NotInDatabaseException {
        IEnterpriseManager enterpriseManager = lookupEnterpriseManager(enterpriseID);

        try {
            return enterpriseFactory.convertToEnterprise(enterpriseManager.queryEnterpriseById(enterpriseID));
        } catch (NotInDatabaseException_Exception e) {
            throw new NotInDatabaseException(e.getFaultInfo().getMessage());
        }
    }

    @Override
    public Collection<IStore> queryStoresByEnterpriseId(long enterpriseID) {
        IEnterpriseManager enterpriseManager;
        List<StoreWithEnterpriseTO> storeTOList;
        try {
            enterpriseManager = lookupEnterpriseManager(enterpriseID);
            storeTOList = enterpriseManager.queryStoresByEnterpriseID(enterpriseID);
        } catch (NotInDatabaseException | NotInDatabaseException_Exception e) {
            LOG.error("Got error while looking up stores by enterprise: " + e.getMessage());
            return Collections.emptyList();
        }

        List<IStore> storeList = new ArrayList<>(storeTOList.size());

        for (StoreWithEnterpriseTO storeTO : storeTOList) {
            storeList.add(storeFactory.convertToStore(storeTO));
        }
        return storeList;
    }

    @Override
    public IStore queryStoreByEnterprise(long enterpriseID, long storeID) throws NotInDatabaseException {
        IEnterpriseManager enterpriseManager = lookupEnterpriseManager(enterpriseID);
        try {
            return storeFactory.convertToStore(enterpriseManager.queryStoreByEnterpriseID(enterpriseID, storeID));
        } catch (NotInDatabaseException_Exception e) {
            throw new NotInDatabaseException(e.getFaultInfo().getMessage());
        }
    }

    @Override
    public ITradingEnterprise queryEnterpriseByName(String enterpriseName) throws NotInDatabaseException {
        IEnterpriseManager enterpriseManager = lookupEnterpriseManager(enterpriseName);
        try {
            EnterpriseTO enterprise = enterpriseManager.queryEnterpriseByName(enterpriseName);
            return enterpriseFactory.convertToEnterprise(enterprise);
        } catch (NotInDatabaseException_Exception e) {
            throw new NotInDatabaseException(e.getFaultInfo().getMessage());
        }
    }

    @Override
    public long getMeanTimeToDelivery(IProductSupplier supplier, ITradingEnterprise enterprise) {
        IEnterpriseManager enterpriseManager;
        try {
            enterpriseManager = lookupEnterpriseManager(enterprise.getId());
        } catch (NotInDatabaseException e) {
            LOG.error("Error retrieving enterprise manager: " + e.getMessage());
            return 0;
        }
        SupplierTO supplierTO = enterpriseFactory.fillSupplierTO(supplier);
        EnterpriseTO enterpriseTO = enterpriseFactory.fillEnterpriseTO(enterprise);
        try {
            return enterpriseManager.getMeanTimeToDelivery(supplierTO, enterpriseTO);
        } catch (NotInDatabaseException_Exception e) {
            LOG.error("Error querying mean time to delivery: " + e.getFaultInfo().getMessage());
            return 0;
        }
    }

    @Override
    public Collection<ICustomProduct> queryAllCustomProducts() {
        return queryCollection(defaultEnterpriseIndex,
                IEnterpriseManager::getAllCustomProducts,
                enterpriseFactory::convertToCustomProduct);
    }

    @Override
    public Collection<IProduct> queryAllProducts(long enterpriseID) throws NotInDatabaseException {
        return queryCollection(enterpriseID,
                enterpriseManager -> enterpriseManager.getAllEnterpriseProducts(enterpriseID),
                enterpriseFactory::convertToProduct);
    }

    @Override
    public Collection<IProduct> queryAllProducts() {
        return queryCollection(defaultEnterpriseIndex,
                IEnterpriseManager::getAllProducts,
                enterpriseFactory::convertToProduct);
    }

    @Override
    public IProduct queryProductByID(long productID) throws NotInDatabaseException {
        IEnterpriseManager enterpriseManager;
        enterpriseManager = lookupEnterpriseManager(defaultEnterpriseIndex);
        try {
            return enterpriseFactory.convertToProduct(enterpriseManager.getProductByID(productID));
        } catch (NotInDatabaseException_Exception e) {
            throw new NotInDatabaseException(e.getFaultInfo().getMessage());
        }
    }

    @Override
    public IProduct queryProductByBarcode(long productBarcode) throws NotInDatabaseException {
        IEnterpriseManager enterpriseManager;
        enterpriseManager = lookupEnterpriseManager(defaultEnterpriseIndex);
        try {
            return enterpriseFactory.convertToProduct(enterpriseManager.getProductByBarcode(productBarcode));
        } catch (NotInDatabaseException_Exception e) {
            throw new NotInDatabaseException(e.getFaultInfo().getMessage());
        }
    }

    @Override
    public Collection<IProductSupplier> querySuppliers(long enterpriseID) throws NotInDatabaseException {
        IEnterpriseManager enterpriseManager;
        List<SupplierTO> supplierTOList;
        try {
            enterpriseManager = lookupEnterpriseManager(enterpriseID);
            supplierTOList = enterpriseManager.querySuppliers(enterpriseID);
        } catch (NotInDatabaseException | NotInDatabaseException_Exception e) {
            LOG.error("Got error while looking up stores by enterprise: " + e.getMessage());
            return Collections.emptyList();
        }

        List<IProductSupplier> supplierList = new ArrayList<>(supplierTOList.size());

        for (SupplierTO supplierTO : supplierTOList) {
            supplierList.add(enterpriseFactory.convertToSupplier(supplierTO));
        }
        return supplierList;
    }

    @Override
    public IProductSupplier querySupplierByID(long supplierID) throws NotInDatabaseException {
        IEnterpriseManager enterpriseManager;
        enterpriseManager = lookupEnterpriseManager(defaultEnterpriseIndex);
        try {
            return enterpriseFactory.convertToSupplier(enterpriseManager.getSupplierByID(supplierID));
        } catch (NotInDatabaseException_Exception e) {
            throw new NotInDatabaseException(e.getFaultInfo().getMessage());
        }
    }

    @Override
    public IProductSupplier querySupplierForProduct(long enterpriseID, long productBarcode)
            throws NotInDatabaseException {
        IEnterpriseManager enterpriseManager;
        enterpriseManager = lookupEnterpriseManager(defaultEnterpriseIndex);
        try {
            return enterpriseFactory
                    .convertToSupplier(enterpriseManager.querySupplierForProduct(enterpriseID, productBarcode));
        } catch (NotInDatabaseException_Exception e) {
            throw new NotInDatabaseException(e.getFaultInfo().getMessage());
        }
    }

    @Override
    public Collection<IProduct> queryProductsBySupplier(long enterpriseID, long productSupplierID)
            throws NotInDatabaseException {
        IEnterpriseManager enterpriseManager;
        List<ProductTO> productTOList;
        try {
            enterpriseManager = lookupEnterpriseManager(enterpriseID);
            productTOList = enterpriseManager.getProductsBySupplier(enterpriseID, productSupplierID);
        } catch (NotInDatabaseException | NotInDatabaseException_Exception e) {
            LOG.error("Got error while looking up stores by enterprise: " + e.getMessage());
            return Collections.emptyList();
        }

        List<IProduct> productList = new ArrayList<>(productTOList.size());

        for (ProductTO productTO : productTOList) {
            productList.add(enterpriseFactory.convertToProduct(productTO));
        }
        return productList;
    }

    @Override
    public Collection<ITradingEnterprise> queryAllEnterprises() {
        IEnterpriseManager enterpriseManager;
        List<EnterpriseTO> enterpriseTOList;
        try {
            enterpriseManager = lookupEnterpriseManager(defaultEnterpriseIndex);
            enterpriseTOList = enterpriseManager.getEnterprises();
        } catch (NotInDatabaseException e) {
            LOG.error("Got error while looking up stores by enterprise: " + e.getMessage());
            return Collections.emptyList();
        }

        List<ITradingEnterprise> enterpriseList = new ArrayList<>(enterpriseTOList.size());

        for (EnterpriseTO enterpriseTO : enterpriseTOList) {
            enterpriseList.add(enterpriseFactory.convertToEnterprise(enterpriseTO));
        }
        return enterpriseList;
    }

    @Override
    public Collection<IPlant> queryPlantByName(long enterpriseID, String plantName) {
        IEnterpriseManager enterpriseManager;
        List<PlantTO> plantTOList;

        try {
            enterpriseManager = lookupEnterpriseManager(enterpriseID);
            plantTOList = enterpriseManager.queryPlantByName(enterpriseID, plantName);
        } catch (NotInDatabaseException | NotInDatabaseException_Exception e) {
            LOG.error("Got error while looking up plants by name: " + e.getMessage());
            return Collections.emptyList();
        }

        List<IPlant> plantList = new ArrayList<>(plantTOList.size());

        for (PlantTO plantTO : plantTOList) {
            plantList.add(enterpriseFactory.convertToPlant(plantTO));
        }
        return plantList;
    }

    @Override
    public Collection<IPlant> queryPlantsByEnterpriseId(long enterpriseID) {
        return queryCollection(enterpriseID,
                enterpriseManager -> enterpriseManager.queryPlantsByEnterpriseID(enterpriseID),
                enterpriseFactory::convertToPlant);
    }

    @Override
    public IPlant queryPlant(long plantID) throws NotInDatabaseException {
        return querySingleEntity(defaultEnterpriseIndex, enterpriseManager ->
                enterpriseFactory.convertToPlant(enterpriseManager.queryPlantByID(plantID)));
    }

    @Override
    public ICustomProduct queryCustomProductByID(long customProductID) throws NotInDatabaseException {
        return querySingleEntity(defaultEnterpriseIndex, enterpriseManager ->
                enterpriseFactory.convertToCustomProduct(
                        enterpriseManager.queryCustomProductByID(customProductID)));
    }

    @Override
    public ICustomProduct queryCustomProductByBarcode(long customProductBarcode) throws NotInDatabaseException {
        return querySingleEntity(defaultEnterpriseIndex, enterpriseManager ->
                enterpriseFactory.convertToCustomProduct(
                        enterpriseManager.queryCustomProductByBarcode(customProductBarcode)));
    }

    @Override
    public IEntryPoint queryEntryPointByID(long entryPointId) throws NotInDatabaseException {
        return querySingleEntity(defaultEnterpriseIndex, enterpriseManager ->
                enterpriseFactory.convertToEntryPoint(enterpriseManager.queryEntryPointById(entryPointId)));
    }

    @Override
    public Collection<IEntryPoint> queryEntryPoints(List<Long> entryPointIds) throws NotInDatabaseException {
        return queryCollection(defaultEnterpriseIndex,
                enterpriseManager -> enterpriseManager.queryEntryPoints(entryPointIds),
                enterpriseFactory::convertToEntryPoint);
    }

    @Override
    public IBooleanCustomProductParameter queryBooleanCustomProductParameterByID(long booleanCustomProductParameterId)
            throws NotInDatabaseException {
        return querySingleEntity(defaultEnterpriseIndex, enterpriseManager ->
                enterpriseFactory.convertToBooleanCustomProductParameter(
                        enterpriseManager.queryBooleanCustomProductParameterById(booleanCustomProductParameterId)));
    }

    @Override
    public INorminalCustomProductParameter queryNorminalCustomProductParameterByID(long norminalCustomProductParameterId)
            throws NotInDatabaseException {
        return querySingleEntity(defaultEnterpriseIndex, enterpriseManager ->
                enterpriseFactory.convertToNorminalCustomProductParameter(
                        enterpriseManager.queryNorminalCustomProductParameterById(norminalCustomProductParameterId)));
    }

    @Override
    public Collection<ICustomProductParameter> queryParametersByCustomProductID(long customProductId)
            throws NotInDatabaseException {
        IEnterpriseManager enterpriseManager;
        final List<CustomProductParameterTO> toList;
        try {
            enterpriseManager = lookupEnterpriseManager(defaultEnterpriseIndex);
            toList = enterpriseManager.queryParametersByCustomProductID(customProductId);
        } catch (NotInDatabaseException | NotInDatabaseException_Exception e) {
            LOG.error("Got error while looking up plants by enterprise: " + e.getMessage(), e);
            return Collections.emptyList();
        }

        final List<ICustomProductParameter> instanceList = new ArrayList<>(toList.size());

        for (final CustomProductParameterTO toInstance : toList) {
            if (BooleanCustomProductParameterTO.class.isAssignableFrom(toInstance.getClass())) {
                instanceList.add(enterpriseFactory.convertToBooleanCustomProductParameter(
                        (BooleanCustomProductParameterTO) toInstance));
                continue;
            } else if (NorminalCustomProductParameterTO.class.isAssignableFrom(toInstance.getClass())) {
                instanceList.add(enterpriseFactory.convertToNorminalCustomProductParameter(
                        (NorminalCustomProductParameterTO) toInstance));
                continue;
            }
            throw new IllegalArgumentException("Unknown class to handle: " + toInstance.getClass());
        }
        return instanceList;
    }

    @Override
    public IPlantOperation queryPlantOperationByID(long plantOperationId) throws NotInDatabaseException {
        return querySingleEntity(defaultEnterpriseIndex, enterpriseManager ->
                plantFactory.convertToPlantOperation(
                        enterpriseManager.queryPlantOperationById(plantOperationId)));
    }

    @Override
    public Collection<IPlantOperationParameter> queryParametersByPlantOperationID(long plantOperationId) throws NotInDatabaseException {
        IEnterpriseManager enterpriseManager;
        final List<PlantOperationParameterTO> toList;
        try {
            enterpriseManager = lookupEnterpriseManager(defaultEnterpriseIndex);
            toList = enterpriseManager.queryParametersByPlantOperationID(plantOperationId);
        } catch (NotInDatabaseException | NotInDatabaseException_Exception e) {
            LOG.error("Got error while looking up plants by enterprise: " + e.getMessage(), e);
            return Collections.emptyList();
        }

        final List<IPlantOperationParameter> instanceList = new ArrayList<>(toList.size());

        for (final PlantOperationParameterTO toInstance : toList) {
            if (BooleanPlantOperationParameterTO.class.isAssignableFrom(toInstance.getClass())) {
                instanceList.add(plantFactory.convertToBooleanPlantOperationParameter(
                        (BooleanPlantOperationParameterTO) toInstance));
                continue;
            } else if (NorminalPlantOperationParameterTO.class.isAssignableFrom(toInstance.getClass())) {
                instanceList.add(plantFactory.convertToNorminalPlantOperationParameter(
                        (NorminalPlantOperationParameterTO) toInstance));
                continue;
            }
            throw new IllegalArgumentException("Unknown class to handle: " + toInstance.getClass());
        }
        return instanceList;
    }

    @Override
    public IBooleanPlantOperationParameter queryBooleanPlantOperationParameterByID(long booleanPlantOperationParameterId)
            throws NotInDatabaseException {
        return querySingleEntity(defaultEnterpriseIndex, enterpriseManager ->
                plantFactory.convertToBooleanPlantOperationParameter(
                        enterpriseManager.queryBooleanPlantOperationParameterById(booleanPlantOperationParameterId)));
    }

    @Override
    public INorminalPlantOperationParameter queryNorminalPlantOperationParameterByID(long norminalPlantOperationParameterId)
            throws NotInDatabaseException {
        return querySingleEntity(defaultEnterpriseIndex, enterpriseManager ->
                plantFactory.convertToNorminalPlantOperationParameter(
                        enterpriseManager.queryNorminalPlantOperationParameterById(norminalPlantOperationParameterId)));
    }

    @Override
    public IPlantOperationParameter queryPlantOperationParameterById(long parameterId) throws NotInDatabaseException {
        return querySingleEntity(defaultEnterpriseIndex, enterpriseManager ->
                plantFactory.convertToPlantOperationParameter(
                        enterpriseManager.queryPlantOperationParameterById(parameterId)));
    }

    @Override
    public IEntryPointInteraction queryEntryPointInteractionByID(long entryPointInteractionId) throws NotInDatabaseException {
        return querySingleEntity(defaultEnterpriseIndex, enterpriseManager ->
                plantFactory.convertToEntryPointInteraction(
                        enterpriseManager.queryEntryPointInteractionById(entryPointInteractionId)));
    }

    @Override
    public IParameterInteraction queryParameterInteractionByID(long parameterInteractionId) throws NotInDatabaseException {
        return querySingleEntity(defaultEnterpriseIndex, enterpriseManager ->
                plantFactory.convertToParameterInteraction(
                        enterpriseManager.queryParameterInteractionById(parameterInteractionId)));
    }

    @Override
    public IRecipe queryRecipeByID(long recipeId) throws NotInDatabaseException {
        return querySingleEntity(defaultEnterpriseIndex, enterpriseManager ->
                plantFactory.convertToRecipe(enterpriseManager.queryRecipeById(recipeId)));
    }

    @Override
    public Collection<IEntryPointInteraction> queryEntryPointInteractions(List<Long> entryPointInteractionIds) throws NotInDatabaseException {
        return queryCollection(defaultEnterpriseIndex,
                enterpriseManager -> enterpriseManager.queryEntryPointInteractions(entryPointInteractionIds),
                plantFactory::convertToEntryPointInteraction);
    }

    @Override
    public Collection<IParameterInteraction> queryParameterInteractions(List<Long> parameterInteractionIds) throws NotInDatabaseException {
        return queryCollection(defaultEnterpriseIndex,
                enterpriseManager -> enterpriseManager.queryParameterInteractions(parameterInteractionIds),
                plantFactory::convertToParameterInteraction);
    }

    @Override
    public Collection<IPlantOperation> queryPlantOperations(List<Long> operationIds) throws NotInDatabaseException {
        return queryCollection(defaultEnterpriseIndex,
                enterpriseManager -> enterpriseManager.queryPlantOperations(operationIds),
                plantFactory::convertToPlantOperation);
    }

    @Override
    public ICustomProductParameter queryCustomProductParameterByID(long customProductParameterId) throws NotInDatabaseException {
        return querySingleEntity(defaultEnterpriseIndex, enterpriseManager ->
                enterpriseFactory.convertToCustomProductParameter(
                        enterpriseManager.queryCustomProductParameterByID(customProductParameterId)));
    }

    @Override
    public Collection<IStore> queryStoreByName(long enterpriseID, String storeName) {
        IEnterpriseManager enterpriseManager;
        List<StoreWithEnterpriseTO> storeTOList;

        try {
            enterpriseManager = lookupEnterpriseManager(enterpriseID);
            storeTOList = enterpriseManager.queryStoreByName(enterpriseID, storeName);
        } catch (NotInDatabaseException | NotInDatabaseException_Exception e) {
            LOG.error("Got error while looking up stores by name: " + e.getMessage());
            return Collections.emptyList();
        }

        List<IStore> storeList = new ArrayList<>(storeTOList.size());

        for (StoreWithEnterpriseTO storeTO : storeTOList) {
            storeList.add(storeFactory.convertToStore(storeTO));
        }
        return storeList;
    }

    private <T> T querySingleEntity(final long enterpriseID,
                                    final ThrowingFunction<
                                            IEnterpriseManager, T, NotInDatabaseException_Exception> queryFunction)
            throws NotInDatabaseException {
        IEnterpriseManager enterpriseManager = lookupEnterpriseManager(enterpriseID);
        try {
            return queryFunction.apply(enterpriseManager);
        } catch (NotInDatabaseException_Exception e) {
            throw new NotInDatabaseException(e.getFaultInfo().getMessage());
        }
    }

    private <T1, T2> Collection<T1> queryCollection(final long enterpriseID,
                                                    final ThrowingFunction<IEnterpriseManager, Collection<T2>, NotInDatabaseException_Exception> supplier,
                                                    final Function<T2, T1> converter) {
        IEnterpriseManager enterpriseManager;
        Collection<T2> toList;
        try {
            enterpriseManager = lookupEnterpriseManager(enterpriseID);
            toList = supplier.apply(enterpriseManager);
        } catch (NotInDatabaseException | NotInDatabaseException_Exception e) {
            LOG.error("Got error while looking up plants by enterprise: " + e.getMessage(), e);
            return Collections.emptyList();
        }

        Collection<T1> instanceList = new ArrayList<>(toList.size());

        for (T2 plantTO : toList) {
            instanceList.add(converter.apply(plantTO));
        }
        return instanceList;
    }

}
