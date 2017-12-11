/*
 **************************************************************************
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
 **************************************************************************
 */

package org.cocome.tradingsystem.inventory.data.enterprise;

import org.apache.log4j.Logger;
import org.cocome.cloud.logic.stub.IEnterpriseManager;
import org.cocome.cloud.logic.stub.NotInDatabaseException_Exception;
import org.cocome.cloud.logic.webservice.ThrowingFunction;
import org.cocome.tradingsystem.inventory.application.plant.PlantTO;
import org.cocome.tradingsystem.inventory.application.plant.parameter.BooleanParameterTO;
import org.cocome.tradingsystem.inventory.application.plant.parameter.NominalParameterTO;
import org.cocome.tradingsystem.inventory.application.plant.parameter.ParameterTO;
import org.cocome.tradingsystem.inventory.application.store.EnterpriseTO;
import org.cocome.tradingsystem.inventory.application.store.ProductTO;
import org.cocome.tradingsystem.inventory.application.store.StoreWithEnterpriseTO;
import org.cocome.tradingsystem.inventory.application.store.SupplierTO;
import org.cocome.tradingsystem.inventory.data.plant.IPlant;
import org.cocome.tradingsystem.inventory.data.plant.IPlantDataFactory;
import org.cocome.tradingsystem.inventory.data.plant.parameter.IBooleanParameter;
import org.cocome.tradingsystem.inventory.data.plant.parameter.INominalParameter;
import org.cocome.tradingsystem.inventory.data.plant.parameter.IParameter;
import org.cocome.tradingsystem.inventory.data.plant.recipe.*;
import org.cocome.tradingsystem.inventory.data.store.IStore;
import org.cocome.tradingsystem.inventory.data.store.IStoreDataFactory;
import org.cocome.tradingsystem.util.exception.NotInDatabaseException;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

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
    private EnterpriseClientFactory enterpriseClientFactory;

    @Inject
    private IEnterpriseDataFactory enterpriseFactory;

    @Inject
    private IPlantDataFactory plantFactory;

    @Inject
    private IStoreDataFactory storeFactory;

    @Override
    public ITradingEnterprise queryEnterpriseById(long enterpriseID) throws NotInDatabaseException {
        IEnterpriseManager enterpriseManager = enterpriseClientFactory.createClient(enterpriseID);

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
            enterpriseManager = enterpriseClientFactory.createClient(enterpriseID);
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
    public IStore queryStoreByID(long storeID) throws NotInDatabaseException {
        IEnterpriseManager enterpriseManager = enterpriseClientFactory.createClient(defaultEnterpriseIndex);
        try {
            return storeFactory.convertToStore(enterpriseManager.queryStoreByID(storeID));
        } catch (NotInDatabaseException_Exception e) {
            throw new NotInDatabaseException(e.getFaultInfo().getMessage());
        }
    }

    @Override
    public IStore queryStoreByEnterprise(long enterpriseID, long storeID) throws NotInDatabaseException {
        IEnterpriseManager enterpriseManager = enterpriseClientFactory.createClient(enterpriseID);
        try {
            return storeFactory.convertToStore(enterpriseManager.queryStoreByEnterpriseID(enterpriseID, storeID));
        } catch (NotInDatabaseException_Exception e) {
            throw new NotInDatabaseException(e.getFaultInfo().getMessage());
        }
    }

    @Override
    public ITradingEnterprise queryEnterpriseByName(String enterpriseName) throws NotInDatabaseException {
        IEnterpriseManager enterpriseManager = enterpriseClientFactory.createClient(enterpriseName);
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
            enterpriseManager = enterpriseClientFactory.createClient(enterprise.getId());
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
        enterpriseManager = enterpriseClientFactory.createClient(defaultEnterpriseIndex);
        try {
            return enterpriseFactory.convertToProduct(enterpriseManager.getProductByID(productID));
        } catch (NotInDatabaseException_Exception e) {
            throw new NotInDatabaseException(e.getFaultInfo().getMessage());
        }
    }

    @Override
    public IProduct queryProductByBarcode(long productBarcode) throws NotInDatabaseException {
        IEnterpriseManager enterpriseManager;
        enterpriseManager = enterpriseClientFactory.createClient(defaultEnterpriseIndex);
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
            enterpriseManager = enterpriseClientFactory.createClient(enterpriseID);
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
        enterpriseManager = enterpriseClientFactory.createClient(defaultEnterpriseIndex);
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
        enterpriseManager = enterpriseClientFactory.createClient(defaultEnterpriseIndex);
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
            enterpriseManager = enterpriseClientFactory.createClient(enterpriseID);
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
            enterpriseManager = enterpriseClientFactory.createClient(defaultEnterpriseIndex);
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
            enterpriseManager = enterpriseClientFactory.createClient(enterpriseID);
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
                plantFactory.convertToEntryPoint(enterpriseManager.queryEntryPointById(entryPointId)));
    }

    @Override
    public IPlantOperation queryPlantOperationByID(long plantOperationId) throws NotInDatabaseException {
        return querySingleEntity(defaultEnterpriseIndex, enterpriseManager ->
                plantFactory.convertToPlantOperation(
                        enterpriseManager.queryPlantOperationById(plantOperationId)));
    }

    @Override
    public Collection<IParameter> queryParametersByRecipeOperationID(long plantOperationId) throws NotInDatabaseException {
        IEnterpriseManager enterpriseManager;
        final List<ParameterTO> toList;
        try {
            enterpriseManager = enterpriseClientFactory.createClient(defaultEnterpriseIndex);
            toList = enterpriseManager.queryParametersByRecipeOperationID(plantOperationId);
        } catch (NotInDatabaseException | NotInDatabaseException_Exception e) {
            LOG.error("Got error while looking up plants by enterprise: " + e.getMessage(), e);
            return Collections.emptyList();
        }

        final List<IParameter> instanceList = new ArrayList<>(toList.size());

        for (final ParameterTO toInstance : toList) {
            if (BooleanParameterTO.class.isAssignableFrom(toInstance.getClass())) {
                instanceList.add(plantFactory.convertToBooleanParameter(
                        (BooleanParameterTO) toInstance));
                continue;
            } else if (NominalParameterTO.class.isAssignableFrom(toInstance.getClass())) {
                instanceList.add(plantFactory.convertToNominalParameter(
                        (NominalParameterTO) toInstance));
                continue;
            }
            throw new IllegalArgumentException("Unknown class to handle: " + toInstance.getClass());
        }
        return instanceList;
    }

    @Override
    public IBooleanParameter queryBooleanParameterByID(long booleanParameterId)
            throws NotInDatabaseException {
        return querySingleEntity(defaultEnterpriseIndex, enterpriseManager ->
                plantFactory.convertToBooleanParameter(
                        enterpriseManager.queryBooleanParameterById(booleanParameterId)));
    }

    @Override
    public INominalParameter queryNominalParameterByID(long nominalParameterId)
            throws NotInDatabaseException {
        return querySingleEntity(defaultEnterpriseIndex, enterpriseManager ->
                plantFactory.convertToNominalParameter(
                        enterpriseManager.queryNominalParameterById(nominalParameterId)));
    }

    @Override
    public IParameter queryParameterById(long parameterId) throws NotInDatabaseException {
        return querySingleEntity(defaultEnterpriseIndex, enterpriseManager ->
                plantFactory.convertToParameter(
                        enterpriseManager.queryParameterById(parameterId)));
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
    public IRecipe queryRecipeByCustomProductID(long customProductId) throws NotInDatabaseException {
        return querySingleEntity(defaultEnterpriseIndex, enterpriseManager ->
                plantFactory.convertToRecipe(enterpriseManager.queryRecipeByCustomProductId(customProductId)));
    }

    @Override
    public Collection<IEntryPointInteraction> queryEntryPointInteractionsByRecipeId(long recipeId) throws NotInDatabaseException {
        return queryCollection(defaultEnterpriseIndex,
                enterpriseManager -> enterpriseManager.queryEntryPointInteractionsByRecipeId(recipeId),
                plantFactory::convertToEntryPointInteraction);
    }

    @Override
    public Collection<IParameterInteraction> queryParameterInteractionsByRecipeId(long recipeId) throws NotInDatabaseException {
        return queryCollection(defaultEnterpriseIndex,
                enterpriseManager -> enterpriseManager.queryParameterInteractionsByRecipeId(recipeId),
                plantFactory::convertToParameterInteraction);
    }

    @Override
    public Collection<IRecipeNode> queryRecipeNodesByRecipeId(long recipeId) throws NotInDatabaseException {
        return queryCollection(defaultEnterpriseIndex,
                enterpriseManager -> enterpriseManager.queryRecipeNodesByRecipeId(recipeId),
                plantFactory::convertToRecipeNode);
    }

    @Override
    public IRecipeOperation queryRecipeOperationById(long operationId) throws NotInDatabaseException {
        return querySingleEntity(defaultEnterpriseIndex, enterpriseManager ->
                plantFactory.convertToRecipeOperation(
                        enterpriseManager.queryRecipeOperationById(operationId)));
    }

    @Override
    public Collection<IEntryPoint> queryInputEntryPointsByRecipeOperationId(long operationId)
            throws NotInDatabaseException {
        return queryCollection(defaultEnterpriseIndex,
                enterpriseManager -> enterpriseManager.queryInputEntryPointsByRecipeId(operationId),
                plantFactory::convertToEntryPoint);
    }

    @Override
    public Collection<IEntryPoint> queryOutputEntryPointsByRecipeOperationId(long operationId)
            throws NotInDatabaseException {
        return queryCollection(defaultEnterpriseIndex,
                enterpriseManager -> enterpriseManager.queryOutputEntryPointsByRecipeId(operationId),
                plantFactory::convertToEntryPoint);
    }

    @Override
    public Collection<IStore> queryStoreByName(long enterpriseID, String storeName) {
        IEnterpriseManager enterpriseManager;
        List<StoreWithEnterpriseTO> storeTOList;

        try {
            enterpriseManager = enterpriseClientFactory.createClient(enterpriseID);
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
        IEnterpriseManager enterpriseManager = enterpriseClientFactory.createClient(enterpriseID);
        try {
            return queryFunction.apply(enterpriseManager);
        } catch (NotInDatabaseException_Exception e) {
            throw new NotInDatabaseException(e.getFaultInfo().getMessage());
        }
    }

    private <T1, T2> Collection<T1> queryCollection(final long enterpriseID,
                                                    final ThrowingFunction<IEnterpriseManager, Collection<T2>, NotInDatabaseException_Exception> supplier,
                                                    final ThrowingFunction<T2, T1, NotInDatabaseException_Exception> converter) {
        IEnterpriseManager enterpriseManager;
        Collection<T2> toList;
        try {
            enterpriseManager = enterpriseClientFactory.createClient(enterpriseID);
            toList = supplier.apply(enterpriseManager);

            Collection<T1> instanceList = new ArrayList<>(toList.size());
            for (T2 plantTO : toList) {
                instanceList.add(converter.apply(plantTO));
            }
            return instanceList;
        } catch (NotInDatabaseException | NotInDatabaseException_Exception e) {
            LOG.error("Got error while looking up plants by enterprise: " + e.getMessage(), e);
            return Collections.emptyList();
        }

    }

}
