package org.cocome.cloud.web.connector.enterpriseconnector;

import org.cocome.cloud.logic.stub.NotInDatabaseException_Exception;
import org.cocome.cloud.web.data.enterprisedata.EnterpriseViewData;
import org.cocome.cloud.web.data.plantdata.PlantViewData;
import org.cocome.cloud.web.data.storedata.ProductWrapper;
import org.cocome.cloud.web.data.storedata.StoreViewData;

import javax.validation.constraints.NotNull;
import java.util.Collection;
import java.util.List;

/**
 * Interface for the retrieval of enterprise and store related information from the backend.
 *
 * @author Tobias Pöppke
 * @author Robert Heinrich
 */
public interface IEnterpriseQuery {

    Collection<EnterpriseViewData> getEnterprises() throws NotInDatabaseException_Exception;

    Collection<StoreViewData> getStores(long enterpriseID) throws NotInDatabaseException_Exception;

    Collection<PlantViewData> getPlants(long enterpriseID) throws NotInDatabaseException_Exception;

    void updateEnterpriseInformation() throws NotInDatabaseException_Exception;

    void updateStoreInformation() throws NotInDatabaseException_Exception;

    void updatePlantInformation() throws NotInDatabaseException_Exception;

    EnterpriseViewData getEnterpriseByID(long enterpriseID) throws NotInDatabaseException_Exception;

    StoreViewData getStoreByID(long storeID) throws NotInDatabaseException_Exception;

    PlantViewData getPlantByID(long plantID) throws NotInDatabaseException_Exception;

    List<ProductWrapper> getAllProducts() throws NotInDatabaseException_Exception;

    ProductWrapper getProductByID(long productID) throws NotInDatabaseException_Exception;

    ProductWrapper getProductByBarcode(long barcode) throws NotInDatabaseException_Exception;

    boolean updateStore(@NotNull StoreViewData store) throws NotInDatabaseException_Exception;

    boolean updatePlant(@NotNull PlantViewData plant) throws NotInDatabaseException_Exception;

    boolean createEnterprise(@NotNull String name) throws NotInDatabaseException_Exception;

    boolean createProduct(@NotNull String name, long barcode, double purchasePrice) throws NotInDatabaseException_Exception;

    boolean createCustomProduct(@NotNull String name, long barcode, double purchasePrice) throws NotInDatabaseException_Exception;

    boolean createStore(long enterpriseID, @NotNull String name, @NotNull String location) throws NotInDatabaseException_Exception;

    boolean createPlant(long enterpriseID, @NotNull String name, @NotNull String location) throws NotInDatabaseException_Exception;

}