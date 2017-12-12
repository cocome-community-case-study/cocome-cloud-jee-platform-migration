package org.cocome.tradingsystem.inventory.data.enterprise;

import org.apache.log4j.Logger;
import org.cocome.cloud.logic.webservice.StreamUtil;
import org.cocome.tradingsystem.inventory.data.plant.IPlant;
import org.cocome.tradingsystem.inventory.data.plant.parameter.IBooleanParameter;
import org.cocome.tradingsystem.inventory.data.plant.parameter.INominalParameter;
import org.cocome.tradingsystem.inventory.data.plant.parameter.IParameter;
import org.cocome.tradingsystem.inventory.data.plant.recipe.*;
import org.cocome.tradingsystem.inventory.data.store.IStore;
import org.cocome.tradingsystem.inventory.data.store.IStoreQuery;
import org.cocome.tradingsystem.remote.access.connection.IBackendQuery;
import org.cocome.tradingsystem.remote.access.connection.QueryParameterEncoder;
import org.cocome.tradingsystem.remote.access.parsing.IBackendConversionHelper;
import org.cocome.tradingsystem.util.exception.NotInDatabaseException;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.function.Function;
import java.util.stream.Collectors;

@Stateless
@Local
public class EnterpriseQueryProvider implements IEnterpriseQuery {

    private static final Logger LOG = Logger.getLogger(EnterpriseQueryProvider.class);

    @Inject
    private IBackendQuery backendConnection;

    @Inject
    private IStoreQuery storeQuery;

    @Inject
    private IBackendConversionHelper csvHelper;

    @Override
    public ITradingEnterprise queryEnterpriseById(long enterpriseID) throws NotInDatabaseException {
        LOG.debug("Trying to retrieve enterprise with id " + enterpriseID);
        try {
            String backendMessage = backendConnection.getEnterprises("id==" + enterpriseID);
            LOG.debug("Backend response: " + backendMessage);
            return csvHelper.getEnterprises(backendMessage).iterator().next();
        } catch (NoSuchElementException e) {
            throw new NotInDatabaseException("Enterprise with ID " + enterpriseID + " could not be found!");
        }
    }

    @Override
    public long getMeanTimeToDelivery(IProductSupplier supplier, ITradingEnterprise enterprise) {
        long mttd = 0;
        try {
            /*
             * mttd = (long) database.query(
			 * "SELECT productorder FROM ProductOrder AS productorder " +
			 * "WHERE productorder.deliveryDate IS NOT NULL " + "AND EXISTS (" +
			 * "SELECT orderentry FROM OrderEntry AS orderentry " +
			 * "WHERE orderentry.order = productorder " + "AND EXISTS (" +
			 * "SELECT product FROM Product AS product " +
			 * "JOIN product.supplier s WHERE s.id = " + supplier.getId() +
			 * " AND orderentry.product = product" + ")" + ") AND EXISTS (" +
			 * "SELECT store FROM Store AS store " +
			 * "JOIN store.enterprise e WHERE productorder.store = store " +
			 * "AND e.id = " + enterprise.getId() + ")").get(0);
			 */
            // TODO compute this here...
            LOG.error("Tried to obtain mean time to delivery, but this is not available at the moment!");

        } catch (NoSuchElementException e) {
            // Means the query returned nothing, so don't crash and return 0 as
            // mttd
        }
        return mttd;
    }

    @Override
    public Collection<ICustomProduct> queryAllCustomProducts() {
        return csvHelper.getProducts(backendConnection.getProducts("name=LIKE%20'*'"))
                .stream()
                .filter(e -> e instanceof ICustomProduct)
                .map(e -> (ICustomProduct) e)
                .collect(Collectors.toList());
    }

    @Override
    public Collection<IProduct> queryAllProducts(long enterpriseID) {
        // Because the backend doesn't allow joins, it is neccessary to first
        // get the stores
        Collection<IStore> stores = queryStoresByEnterpriseId(enterpriseID);
        Collection<IProduct> products = new LinkedList<>();

        for (IStore store : stores) {
            products.addAll(storeQuery.queryProducts(store.getId()));
        }

        return products;
    }

    @Override
    public Collection<IProductSupplier> querySuppliers(long enterpriseID) throws NotInDatabaseException {
        ITradingEnterprise enterprise = queryEnterpriseById(enterpriseID);

        return enterprise.getSuppliers();
    }

    @Override
    public ITradingEnterprise queryEnterpriseByName(String enterpriseName) throws NotInDatabaseException {
        enterpriseName = QueryParameterEncoder.encodeQueryString(enterpriseName);

        try {
            return csvHelper
                    .getEnterprises(backendConnection.getEnterprises("name=LIKE%20'" + enterpriseName + "'")).iterator()
                    .next();
        } catch (NoSuchElementException e) {
            throw new NotInDatabaseException("No enterprise with name '" + enterpriseName + "' was found!");
        }
    }

    @Override
    public IProductSupplier querySupplierForProduct(long enterpriseID, long productBarcode)
            throws NotInDatabaseException {
        ITradingEnterprise enterprise = queryEnterpriseById(enterpriseID);

        // Probably inefficient but not possible otherwise due to serviceadapter
        // limitations
        for (IProductSupplier supplier : enterprise.getSuppliers()) {
            for (IProduct product : supplier.getProducts()) {
                if (product.getBarcode() == productBarcode) {
                    return supplier;
                }
            }
        }
        return null;
    }

    @Override
    public Collection<IProduct> queryProductsBySupplier(long enterpriseID, long productSupplierID)
            throws NotInDatabaseException {
        ITradingEnterprise enterprise = queryEnterpriseById(enterpriseID);

        for (IProductSupplier supplier : enterprise.getSuppliers()) {
            if (supplier.getId() == productSupplierID) {
                return csvHelper.getProducts(backendConnection.getProducts("supplier.id==" + supplier.getId()));
            }
        }

        return Collections.emptyList();
    }

    @Override
    public Collection<IStore> queryStoresByEnterpriseId(long enterpriseID) {
        return csvHelper.getStores(backendConnection.getStores("enterprise.id==" + enterpriseID));
    }

    @Override
    public IStore queryStoreByID(long storeID) throws NotInDatabaseException {
        return getSingleEntity(csvHelper::getStores, "Store", storeID);
    }

    @Override
    public Collection<IPlant> queryPlantsByEnterpriseId(long enterpriseID) {
        return csvHelper.getPlants(backendConnection.getEntity("Plant", "enterprise.id==" + enterpriseID));
    }

    @Override
    public IPlant queryPlant(long plantID) throws NotInDatabaseException {
        return getSingleEntity(csvHelper::getPlants, "Plant", plantID);
    }

    @Override
    public ICustomProduct queryCustomProductByID(long productID) throws NotInDatabaseException {
        final IProduct product = queryProductByID(productID);
        if (product instanceof ICustomProduct) {
            return (ICustomProduct) product;
        }
        throw new NotInDatabaseException("No custom product with ID = " + productID);
    }

    @Override
    public ICustomProduct queryCustomProductByBarcode(long productBarcode) throws NotInDatabaseException {
        final IProduct product = queryProductByBarcode(productBarcode);
        if (product instanceof ICustomProduct) {
            return (ICustomProduct) product;
        }
        throw new NotInDatabaseException("No custom product with barcode = " + productBarcode);
    }

    @Override
    public IEntryPoint queryEntryPointByID(long entryPointId) throws NotInDatabaseException {
        return getSingleEntity(csvHelper::getEntryPoints, "EntryPoint", entryPointId);
    }

    @Override
    public IBooleanParameter queryBooleanParameterByID(long booleanParameterId)
            throws NotInDatabaseException {
        return getSingleEntity(csvHelper::getBooleanParameter,
                "BooleanParameter",
                booleanParameterId);
    }

    @Override
    public INominalParameter queryNominalParameterByID(long nominalParameterId)
            throws NotInDatabaseException {
        return getSingleEntity(csvHelper::getNominalParameter, "NominalParameter", nominalParameterId);
    }

    @Override
    public IParameter queryParameterById(long parameterId) throws NotInDatabaseException {
        return getSingleEntity(csvHelper::getParameters, "Parameter", parameterId);
    }

    @Override
    public IEntryPointInteraction queryEntryPointInteractionByID(long entryPointInteractionId) throws NotInDatabaseException {
        return getSingleEntity(csvHelper::getEntryPointInteraction, "EntryPointInteraction", entryPointInteractionId);

    }

    @Override
    public IParameterInteraction queryParameterInteractionByID(long parameterInteractionId) throws NotInDatabaseException {
        return getSingleEntity(csvHelper::getParameterInteraction, "ParameterInteraction", parameterInteractionId);
    }

    @Override
    public IRecipe queryRecipeByID(long recipeId) throws NotInDatabaseException {
        return getSingleEntity(csvHelper::getRecipe, "Recipe", recipeId);
    }

    @Override
    public IRecipe queryRecipeByCustomProductID(long customProductId) throws NotInDatabaseException {
        return getSingleEntity(csvHelper::getRecipe, "Recipe", "customProduct.id==" + customProductId);
    }

    @Override
    public Collection<IEntryPointInteraction> queryEntryPointInteractionsByRecipeId(long recipeId) throws NotInDatabaseException {
        return csvHelper.getEntryPointInteraction(
                backendConnection.getEntity("EntryPointInteraction", "recipe.id==" + recipeId));
    }

    @Override
    public Collection<IParameterInteraction> queryParameterInteractionsByRecipeId(long recipeId) throws NotInDatabaseException {
        return csvHelper.getParameterInteraction(
                backendConnection.getEntity("ParameterInteraction", "recipe.id==" + recipeId));
    }

    @Override
    public Collection<IRecipeNode> queryRecipeNodesByRecipeId(long recipeId) throws NotInDatabaseException {
        return csvHelper.getRecipeNode(
                backendConnection.getEntity("RecipeNode", "recipe.id==" + recipeId));
    }

    @Override
    public IRecipeOperation queryRecipeOperationById(long operationId) throws NotInDatabaseException {
        return getSingleEntity(csvHelper::getRecipeOperation, "RecipeOperation", operationId);
    }

    @Override
    public Collection<IEntryPoint> queryInputEntryPointsByRecipeOperationId(long operationId) {
        return StreamUtil.ofNullable(queryEntryPointsByRecipeOperationId(operationId))
                .filter(e -> e.getDirection() == IEntryPoint.Direction.INPUT).collect(Collectors.toList());
    }

    @Override
    public Collection<IEntryPoint> queryOutputEntryPointsByRecipeOperationId(long operationId) {
        return StreamUtil.ofNullable(queryEntryPointsByRecipeOperationId(operationId))
                .filter(e -> e.getDirection() == IEntryPoint.Direction.OUTPUT).collect(Collectors.toList());
    }

    @Override
    public Collection<IPlantOperation> queryPlantOperationsByPlantId(long plantId) throws NotInDatabaseException {
        return csvHelper
                .getPlantOperation(backendConnection.getEntity(
                        "PlantOperation",
                        "plant.id==" + plantId));
    }

    private Collection<IEntryPoint> queryEntryPointsByRecipeOperationId(long operationId) {
        return csvHelper
                .getEntryPoints(backendConnection.getEntity(
                        "EntryPoint",
                        "operation.id==" + operationId));
    }

    @Override
    public Collection<IParameter> queryParametersByRecipeOperationID(long PlantOperationId) {
        return csvHelper
                .getParameters(backendConnection.getEntity(
                        "Parameter",
                        "operation.id==" + PlantOperationId));
    }

    @Override
    public IPlantOperation queryPlantOperationByID(long plantOperationId) throws NotInDatabaseException {
        return getSingleEntity(csvHelper::getPlantOperation, "PlantOperation", plantOperationId);
    }

    @Override
    public Collection<IPlant> queryPlantByName(long enterpriseID, String plantName) {
        plantName = QueryParameterEncoder.encodeQueryString(plantName);
        return csvHelper
                .getPlants(backendConnection.getEntity("Plant", "name=LIKE%20'" + plantName
                        + "';Plant.enterprise.id==" + enterpriseID));
    }

    @Override
    public Collection<ITradingEnterprise> queryAllEnterprises() {
        return csvHelper
                .getEnterprises(backendConnection.getEnterprises("name=LIKE%20'*'"));
    }

    @Override
    public IStore queryStoreByEnterprise(long enterpriseID, long storeID) throws NotInDatabaseException {
        try {
            return csvHelper
                    .getStores(backendConnection.getStores("id==" + storeID + ";Store.enterprise.id==" + enterpriseID))
                    .iterator().next();
        } catch (NoSuchElementException e) {
            throw new NotInDatabaseException("No matching store found in database!");
        }
    }

    @Override
    public IProductSupplier querySupplierByID(long supplierID) throws NotInDatabaseException {
        try {
            return csvHelper
                    .getProductSuppliers(backendConnection.getProductSupplier("id==" + supplierID)).iterator().next();
        } catch (NoSuchElementException e) {
            throw new NotInDatabaseException("No matching supplier found in database!");
        }
    }

    @Override
    public IProduct queryProductByID(long productID) throws NotInDatabaseException {
        try {
            IProduct p = csvHelper.getProducts(backendConnection.getProducts("id==" + productID)).iterator()
                    .next();
            p.setId(productID);
            return p;
        } catch (NoSuchElementException e) {
            throw new NotInDatabaseException("No matching product found in database!");
        }
    }

    @Override
    public IProduct queryProductByBarcode(long productBarcode) throws NotInDatabaseException {
        try {
            return csvHelper.getProducts(backendConnection.getProducts("barcode==" + productBarcode))
                    .iterator().next();
        } catch (NoSuchElementException e) {
            throw new NotInDatabaseException("No matching product found in database!");
        }
    }

    @Override
    public Collection<IProduct> queryAllProducts() {
        return csvHelper.getProducts(backendConnection.getProducts("name=LIKE%20'*'"));
    }

    @Override
    public Collection<IStore> queryStoreByName(long enterpriseID, String storeName) {
        storeName = QueryParameterEncoder.encodeQueryString(storeName);
        return csvHelper
                .getStores(backendConnection.getStores("name=LIKE%20'" + storeName
                        + "';Store.enterprise.id==" + enterpriseID));
    }

    private <T> T getSingleEntity(Function<String, Collection<T>> converter,
                                  String entity,
                                  long entityId) throws NotInDatabaseException {
        return this.getSingleEntity(converter, entity, "id==" + entityId);
    }

    private <T> T getSingleEntity(Function<String, Collection<T>> converter,
                                  String entity,
                                  String cond) throws NotInDatabaseException {
        try {
            return converter.apply(backendConnection.getEntity(entity, cond)).iterator().next();
        } catch (NoSuchElementException e) {
            LOG.error(e);
            e.printStackTrace();
            throw new NotInDatabaseException(String.format(
                    "No matching entity of type '%s' found in database! Used condition: %s",
                    entity, cond), e);
        }
    }
}
