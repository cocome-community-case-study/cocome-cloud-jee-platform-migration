/*
 *************************************************************************
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
 *************************************************************************
 */

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
import org.cocome.tradingsystem.inventory.application.store.*;
import org.cocome.tradingsystem.inventory.data.enterprise.*;
import org.cocome.tradingsystem.inventory.data.persistence.IPersistenceContext;
import org.cocome.tradingsystem.inventory.data.persistence.UpdateException;
import org.cocome.tradingsystem.inventory.data.plant.IPlant;
import org.cocome.tradingsystem.inventory.data.plant.IPlantDataFactory;
import org.cocome.tradingsystem.inventory.data.plant.IPlantQuery;
import org.cocome.tradingsystem.inventory.data.store.IStore;
import org.cocome.tradingsystem.inventory.data.store.IStoreDataFactory;
import org.cocome.tradingsystem.util.exception.NotInDatabaseException;
import org.cocome.tradingsystem.util.scope.CashDeskRegistry;
import org.cocome.tradingsystem.util.scope.ICashDeskRegistryFactory;
import org.cocome.tradingsystem.util.scope.IContextRegistry;
import org.cocome.tradingsystem.util.scope.RegistryKeys;

import javax.ejb.CreateException;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.jws.WebService;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
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
    private IPlantQuery plantQuery;

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
    private long defaultEnterpriseIndex;

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
        Collection<IStore> stores = enterpriseQuery.queryStoresByEnterpriseId(enterpriseId);
        Collection<StoreWithEnterpriseTO> storeTOs = new ArrayList<>(stores.size());
        for (IStore store : stores) {
            try {
                storeTOs.add(storeFactory.fillStoreWithEnterpriseTO(store));
            } catch (NotInDatabaseException e) {
                LOG.error("Got NotInDatabaseException: " + e);
                e.printStackTrace();
                throw e;
            }
        }
        return storeTOs;
    }

    @Override
    public Collection<PlantTO> queryPlantsByEnterpriseID(long enterpriseId)
            throws NotInDatabaseException {
        return this.queryCollectionByEnterpriseID(enterpriseId,
                enterpriseQuery::queryPlantsByEnterpriseId,
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
        IProduct product = enterpriseFactory.getNewProduct();
        product.setBarcode(productTO.getBarcode());
        product.setName(productTO.getName());
        product.setPurchasePrice(productTO.getPurchasePrice());

        saveDBCreateAction(() -> persistenceContext.createEntity(product));

        return product.getId();
    }

    @Override
    public long createCustomProduct(CustomProductTO productTO) throws CreateException {
        final ICustomProduct product = enterpriseFactory.getNewCustomProduct();
        product.setBarcode(productTO.getBarcode());
        product.setName(productTO.getName());
        product.setPurchasePrice(productTO.getPurchasePrice());
        product.setRecipeId(productTO.getRecipe().getId());

        saveDBCreateAction(() -> persistenceContext.createEntity(product));

        return product.getId();
    }

    @Override
    public void updateProduct(ProductWithSupplierTO productTO)
            throws NotInDatabaseException, UpdateException {
        IProduct product;
        try {
            if (productTO.getId() != 0) {
                product = enterpriseQuery.queryProductByID(productTO.getId());
                product.setBarcode(productTO.getBarcode());
            } else {
                product = enterpriseQuery.queryProductByBarcode(productTO
                        .getBarcode());
            }
        } catch (NotInDatabaseException e) {
            LOG.error("Got NotInDatabaseException: " + e);
            e.printStackTrace();
            throw e;
        }

        product.setName(productTO.getName());
        product.setPurchasePrice(productTO.getPurchasePrice());

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
    public void updateCustomProduct(CustomProductTO customProductTO) throws UpdateException, NotInDatabaseException {
        ICustomProduct product;
        try {
            if (customProductTO.getId() != 0) {
                product = enterpriseQuery.queryCustomProductByID(customProductTO.getId());
                product.setBarcode(customProductTO.getBarcode());
            } else {
                product = enterpriseQuery.queryCustomProductByBarcode(customProductTO
                        .getBarcode());
            }
        } catch (NotInDatabaseException e) {
            LOG.error("Got NotInDatabaseException: " + e);
            e.printStackTrace();
            throw e;
        }

        product.setName(customProductTO.getName());
        product.setPurchasePrice(customProductTO.getPurchasePrice());

        //TODO
        /*
        if (customProductTO.getSupplierTO().getId() != 0) {
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
        */

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
    public Collection<CustomProductTO> getAllEnterpriseCustomProducts(long enterpriseId) throws NotInDatabaseException {
        final Collection<ICustomProduct> products =
                saveFetchFromDB(() -> enterpriseQuery.queryAllCustomProducts(enterpriseId));
        return products.stream()
                .map(enterpriseFactory::fillCustomProductTO)
                .collect(Collectors.toList());
    }

    @Override
    public Collection<CustomProductTO> getAllCustomProducts() {
        return enterpriseQuery.queryAllCustomProducts()
                .stream()
                .map(enterpriseFactory::fillCustomProductTO)
                .collect(Collectors.toList());
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
    public void deleteEnterprise(EnterpriseTO enterpriseTO) throws NotInDatabaseException, UpdateException, IOException {
        final ITradingEnterprise enterprise = saveFetchFromDB(() ->
                enterpriseQuery.queryEnterpriseById(enterpriseTO.getId()));
        saveDBUpdateAction(() -> persistenceContext.deleteEntity(enterprise));
    }

    @Override
    public void deletePlant(PlantTO plantTO) throws NotInDatabaseException, UpdateException, IOException {
        final IPlant plant = saveFetchFromDB(() -> enterpriseQuery.queryPlant(plantTO.getId()));
        saveDBUpdateAction(() -> persistenceContext.deleteEntity(plant));
    }

    @Override
    public void deleteCustomProduct(CustomProductTO customProductTO) throws NotInDatabaseException, UpdateException, IOException {
        final ICustomProduct customProduct = saveFetchFromDB(() -> queryCustomProduct(customProductTO));
        saveDBUpdateAction(() -> persistenceContext.deleteEntity(customProduct));
    }

    private ICustomProduct queryCustomProduct(CustomProductTO customProductTO) throws NotInDatabaseException {
        if (customProductTO.getId() != 0) {
            return enterpriseQuery.queryCustomProductByID(customProductTO.getId());
        }
        return enterpriseQuery.queryCustomProductByBarcode(customProductTO
                .getBarcode());
    }

    private <T1, T2> Collection<T2> queryCollectionByEnterpriseID(long enterpriseId,
                                                                  final Function<Long, Collection<T1>> queryCommand,
                                                                  final ThrowingFunction<T1, T2, NotInDatabaseException> conversionCommand)
            throws NotInDatabaseException {
        setContextRegistry(enterpriseId);
        Collection<T1> instances = queryCommand.apply(enterpriseId);
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
}
