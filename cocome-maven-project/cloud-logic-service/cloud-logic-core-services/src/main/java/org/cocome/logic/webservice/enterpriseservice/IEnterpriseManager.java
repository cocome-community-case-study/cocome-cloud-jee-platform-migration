package org.cocome.logic.webservice.enterpriseservice;

import java.io.IOException;
import java.util.Collection;

import javax.ejb.CreateException;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchema;

import org.cocome.tradingsystem.inventory.application.store.EnterpriseTO;
import org.cocome.tradingsystem.inventory.application.store.ProductTO;
import org.cocome.tradingsystem.inventory.application.store.ProductWithSupplierTO;
import org.cocome.tradingsystem.inventory.application.store.StoreWithEnterpriseTO;
import org.cocome.tradingsystem.inventory.application.store.SupplierTO;
import org.cocome.tradingsystem.inventory.data.enterprise.IProductSupplier;
import org.cocome.tradingsystem.inventory.data.persistence.UpdateException;
import org.cocome.tradingsystem.util.exception.NotInDatabaseException;
/**
 * 
 * 
 * @author Tobias Pöppke
 * @author Robert Heinrich
 * 
 */
@WebService(targetNamespace = "http://enterprise.webservice.logic.cocome.org/")
public interface IEnterpriseManager {
	/**
	 * @param enterpriseId
	 *            the unique identifier of a TradingEnterprise entity
	 *            
	 * @return A EnterpriseTO object with the specified id.
	 * 
	 * @throws NotInDatabaseException
	 *             if a trading enterprise with the given id could not be found
	 */
	@WebMethod
	public EnterpriseTO queryEnterpriseById(
			@XmlElement(required = true) @WebParam(name = "enterpriseID") long enterpriseID) throws NotInDatabaseException;
	
	/**
	 * @param enterpriseName
	 *            the unique identifier of a TradingEnterprise entity
	 *            
	 * @return A EnterpriseTO object containing the enterprise with the specified name.
	 * 
	 * @throws NotInDatabaseException
	 *             if a trading enterprise with the given name could not be found
	 */
	@WebMethod
	public EnterpriseTO queryEnterpriseByName(
			@XmlElement(required = true) @WebParam(name = "enterpriseName") String enterpriseName) throws NotInDatabaseException;
	
	/**
	 * @param enterpriseId
	 *            the unique identifier of a TradingEnterprise entity
	 *            
	 * @return A collection of StoreWithEntepriseTO objects belonging to the given enterprise.
	 * 
	 * @throws NotInDatabaseException
	 *             if a trading enterprise with the given id could not be found
	 */
	@WebMethod
	public Collection<StoreWithEnterpriseTO> queryStoresByEnterpriseID(
			@XmlElement(required = true) @WebParam(name = "enterpriseID") long enterpriseID) throws NotInDatabaseException;
	
	/**
	 * @param enterpriseId
	 *            the unique identifier of a TradingEnterprise entity
	 * @param storeId
	 *            the unique identifier of a Store entity
	 *            
	 * @return A StoreWithEntepriseTO object with the given store identifier and 
	 * 			  belonging to the given enterprise.
	 * 
	 * @throws NotInDatabaseException
	 *             if a trading enterprise with the given id could not be found
	 */
	@WebMethod
	public StoreWithEnterpriseTO queryStoreByEnterpriseID(
			@XmlElement(required = true) @WebParam(name = "enterpriseID") long enterpriseID,
			@XmlElement(required = true) @WebParam(name = "storeID") long storeID) throws NotInDatabaseException;

	/**
	 * @param supplier
	 *            The supplier which delivers the products
	 * @param enterpriseId
	 *            The enterprise for which the products are delivered
	 * @return The mean time to delivery in milliseconds
	 * @throws NotInDatabaseException 
	 */
	@WebMethod
	public long getMeanTimeToDelivery(
			@XmlElement(required = true) @WebParam(name = "productSupplier") SupplierTO supplier,
			@XmlElement(required = true) @WebParam(name = "enterprise") EnterpriseTO enterprise) throws NotInDatabaseException;
	
	/**
	 * Retrieves all products that are sold in this enterprise.
	 * Note that there is no information included about the stores in which 
	 * this product is available.
	 * 
	 * @param enterpriseId 
	 * 				The enterprise for which all products should be retrieved  
	 * @return All {@code ProductTO}s available in the given enterprise 
	 * @throws NotInDatabaseException 
	 */
	@WebMethod
	public Collection<ProductTO> getAllEnterpriseProducts(
			@XmlElement(required = true) @WebParam(name = "enterpriseID") long enterpriseID) 
			throws NotInDatabaseException;
	
	/**
	 * Retrieves all products that are registered in the database.
	 * Note that there is no information included about the stores in which 
	 * these products are available.
	 * 
	 * @return All {@code ProductTO}s available or an empty collection if there are none
	 */
	@WebMethod
	public Collection<ProductTO> getAllProducts();
	
	/**
	 * Retrieves the product with the given ID if it is stored in the database.
	 * The product does not have to be available in any store.
	 * 
	 * @return The {@code ProductTO} with the given ID or null if it was not found
	 * @throws NotInDatabaseException 
	 */
	@WebMethod
	public ProductTO getProductByID(
			@XmlElement(required = true) @WebParam(name = "productID") long productID) throws NotInDatabaseException;
	
	/**
	 * Retrieves the product with the given ID if it is stored in the database.
	 * The product does not have to be available in any store.
	 * 
	 * @param enterpriseID the enterprise to search
	 * 
	 * @param supplierID the supplier ID
	 * 
	 * @return The {@code ProductTO} with the given supplier ID or null if it was not found
	 * @throws NotInDatabaseException 
	 */
	@WebMethod
	public Collection<ProductTO> getProductsBySupplier(
			@XmlElement(required = true) @WebParam(name = "enterpriseID") long enterpriseID, 
			@XmlElement(required = true) @WebParam(name = "supplierID")long supplierID) throws NotInDatabaseException;
	
	/**
	 * Retrieves the supplier with the given ID if it is stored in the database.
	 * 
	 * @param supplierID the supplier ID
	 * 
	 * @return The {@code SupplierTO} with the given supplier ID or null if it was not found
	 * @throws NotInDatabaseException 
	 */
	@WebMethod
	public SupplierTO getSupplierByID(
			@XmlElement(required = true) @WebParam(name = "supplierID") long supplierID) throws NotInDatabaseException;
	
	/**
	 * Retrieves all suppliers registered in the given enterprise.
	 * 
	 * @param enterpriseID the enterprise to search
	 * 
	 * @return The {@code SupplierTO} with the given enterprise ID
	 * @throws NotInDatabaseException 
	 */
	@WebMethod
	public Collection<SupplierTO> querySuppliers(
			@XmlElement(required = true) @WebParam(name = "enterpriseID") long enterpriseID) throws NotInDatabaseException;
	
	/**
	 * Retrieves the suppliers registered in this enterprise for the given product.
	 * 
	 * @param enterpriseID
	 * 			the id of the enterprise for which to retrieve the suppliers
	 * 
	 * @param productBarcode
	 * 			the barcode of the product to look for
	 * 
	 * @return
	 * 			the first supplier found and null if none is found
	 * @throws NotInDatabaseException 
	 */
	@WebMethod
	public SupplierTO querySupplierForProduct(
			@XmlElement(required = true) @WebParam(name = "enterpriseID") long enterpriseID, 
			@XmlElement(required = true) @WebParam(name = "productBarcode")long productBarcode) throws NotInDatabaseException;
	
	/**
	 * Retrieves the product with the given barcode if it is stored in the database.
	 * The product does not have to be available in any store.
	 * 
	 * @return The {@code ProductTO} with the given ID or null if it was not found
	 * @throws NotInDatabaseException 
	 */
	@WebMethod
	public ProductTO getProductByBarcode(
			@XmlElement(required = true) @WebParam(name = "barcode") long barcode) throws NotInDatabaseException;
	
	@WebMethod
	public void createEnterprise(
			@XmlElement(required = true) @WebParam(name = "enterpriseName") String enterpriseName) 
			throws CreateException;
	
	@WebMethod
	public void createStore(
			@XmlElement(required = true) @WebParam(name = "storeTO") StoreWithEnterpriseTO storeTO) 
			throws CreateException;
	
	/**
	 * Updates the store object. This method requires the EnterpriseTO to be present and to have 
	 * at least the id attribute set. 
	 * 
	 * @param storeTO
	 * @throws UpdateException
	 * @throws NotInDatabaseException
	 */
	@WebMethod
	public void updateStore(
			@XmlElement(required = true) @WebParam(name = "storeTO") StoreWithEnterpriseTO storeTO) 
			throws UpdateException, NotInDatabaseException;
	
	@WebMethod
	public void createProduct(
			@XmlElement(required = true) @WebParam(name = "productTO") ProductTO productTO) 
			throws CreateException;
	
	@WebMethod
	public void updateProduct(
			@XmlElement(required = true) @WebParam(name = "productTO") ProductWithSupplierTO productTO) 
			throws UpdateException, NotInDatabaseException;
	
	@WebMethod
	public Collection<EnterpriseTO> getEnterprises();
	
	@WebMethod
	public void fillTestDatabase(int storeCount, int cashDesksPerStore, String dirString) 
			throws NotInDatabaseException, CreateException, IOException;
	
	@WebMethod
	public void fillItemizedDatabase(int stockItemCount, int cashDesksPerStore, String dirString) 
			throws NotInDatabaseException, CreateException, IOException;
	
	@WebMethod
	public void fillStorizedDatabase(int storeCount, int cashDesksPerStore, String dirString) 
			throws NotInDatabaseException, CreateException, IOException;
	
}
