package org.cocome.cloud.web.connector.enterpriseconnector;

import java.util.Collection;
import java.util.List;

import javax.validation.constraints.NotNull;

import org.cocome.cloud.logic.stub.NotInDatabaseException_Exception;
import org.cocome.cloud.web.data.enterprisedata.EnterpriseViewData;
import org.cocome.cloud.web.data.storedata.ProductWrapper;
import org.cocome.cloud.web.data.storedata.StoreViewData;

/**
 * Interface for the retrieval of enterprise and store related information from the backend.
 * 
 * @author Tobias Pöppke
 * @author Robert Heinrich
 */
public interface IEnterpriseQuery {

	public Collection<EnterpriseViewData> getEnterprises();

	public Collection<StoreViewData> getStores(long enterpriseID) throws NotInDatabaseException_Exception;

	public void updateEnterpriseInformation();
	
	public void updateStoreInformation() throws NotInDatabaseException_Exception;
	
	public EnterpriseViewData getEnterpriseByID(long enterpriseID);
	
	public StoreViewData getStoreByID(long storeID) throws NotInDatabaseException_Exception;
	
	public List<ProductWrapper> getAllProducts();
	
	public ProductWrapper getProductByID(long productID) throws NotInDatabaseException_Exception;
	
	public ProductWrapper getProductByBarcode(long barcode) throws NotInDatabaseException_Exception;
	
	public boolean updateStore(@NotNull StoreViewData store);
	
	public boolean createEnterprise(@NotNull String name);
	
	public boolean createProduct(@NotNull String name, long barcode, double purchasePrice);
	
	public boolean createStore(long enterpriseID, @NotNull String name, @NotNull String location);
}