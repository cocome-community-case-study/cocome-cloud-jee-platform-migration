package org.cocome.logic.webservice.storeservice;

import java.util.List;

import javax.ejb.CreateException;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlElement;

import org.cocome.tradingsystem.inventory.application.store.ComplexOrderEntryTO;
import org.cocome.tradingsystem.inventory.application.store.ComplexOrderTO;
import org.cocome.tradingsystem.inventory.application.store.InvalidRollInRequestException;
import org.cocome.tradingsystem.inventory.application.store.NoSuchProductException;
import org.cocome.tradingsystem.inventory.application.store.NotImplementedException;
import org.cocome.tradingsystem.inventory.application.store.ProductMovementTO;
import org.cocome.tradingsystem.inventory.application.store.ProductOutOfStockException;
import org.cocome.tradingsystem.inventory.application.store.ProductTO;
import org.cocome.tradingsystem.inventory.application.store.ProductWithStockItemTO;
import org.cocome.tradingsystem.inventory.application.store.ProductWithSupplierAndStockItemTO;
import org.cocome.tradingsystem.inventory.application.store.ProductWithSupplierTO;
import org.cocome.tradingsystem.inventory.application.store.SaleTO;
import org.cocome.tradingsystem.inventory.application.store.StockItemTO;
import org.cocome.tradingsystem.inventory.application.store.StoreWithEnterpriseTO;
import org.cocome.tradingsystem.inventory.data.persistence.UpdateException;
import org.cocome.tradingsystem.util.exception.NotInDatabaseException;

@WebService
public interface IStoreManager {

	@WebMethod
	public void accountSale(
			@XmlElement(required = true) @WebParam(name = "storeID") long storeID,
			@XmlElement(required = true) @WebParam(name = "sale") SaleTO sale) 
					throws ProductOutOfStockException, NotInDatabaseException, UpdateException;

	@WebMethod
	public StoreWithEnterpriseTO getStore(
			@XmlElement(required = true) @WebParam(name = "storeID") long storeID) 
					throws NotInDatabaseException;

	@WebMethod
	public List<ProductWithStockItemTO> getProductsWithLowStock(
			@XmlElement(required = true) @WebParam(name = "storeID") long storeID) 
					throws NotInDatabaseException;

	@WebMethod
	public List<ProductWithSupplierTO> getAllStoreProducts(
			@XmlElement(required = true) @WebParam(name = "storeID") long storeID) 
					throws NotInDatabaseException;

	@WebMethod
	public List<ProductWithSupplierAndStockItemTO> getProductsWithStockItems(
			@XmlElement(required = true) @WebParam(name = "storeID") long storeID)
					throws NotInDatabaseException;

	@WebMethod
	public List<ComplexOrderTO> orderProducts(
			@XmlElement(required = true) @WebParam(name = "storeID") long storeID,
			@XmlElement(required = true) @WebParam(name = "complexOrder") ComplexOrderTO complexOrder)
					throws NotInDatabaseException, CreateException, UpdateException;

	@WebMethod
	public ComplexOrderTO getOrder(
			@XmlElement(required = true) @WebParam(name = "storeID") long storeID,
			@XmlElement(required = true) @WebParam(name = "orderID") long orderId)
					throws NotInDatabaseException;
	
	@WebMethod
	public List<ComplexOrderTO> getOutstandingOrders(
			@XmlElement(required = true) @WebParam(name = "storeID") long storeID)
					throws NotInDatabaseException;

	@WebMethod
	public void rollInReceivedOrder(
			@XmlElement(required = true) @WebParam(name = "storeID") long storeID,
			@XmlElement(required = true) @WebParam(name = "orderID") long orderId)
					throws InvalidRollInRequestException, NotInDatabaseException, UpdateException;

	@WebMethod
	public ProductWithStockItemTO changePrice(
			@XmlElement(required = true) @WebParam(name = "storeID") long storeID,
			@XmlElement(required = true) @WebParam(name = "stockItemTO") StockItemTO stockItemTO)
					throws NotInDatabaseException, UpdateException;
	
	@WebMethod
	public void updateStockItem(
			@XmlElement(required = true) @WebParam(name = "storeID") long storeID,
			@XmlElement(required = true) @WebParam(name = "stockItemTO") StockItemTO stockItemTO) 
					throws NotInDatabaseException, UpdateException;
	
	@WebMethod
	public void createStockItem(
			@XmlElement(required = true) @WebParam(name = "storeID") long storeID,
			@XmlElement(required = true) @WebParam(name = "stockItemTO") ProductWithStockItemTO stockItemTO) 
					throws NotInDatabaseException, CreateException;

	@WebMethod
	public void markProductsUnavailableInStock(
			@XmlElement(required = true) @WebParam(name = "storeID") long storeID,
			@XmlElement(required = true) @WebParam(name = "movedProductAmounts") ProductMovementTO movedProductAmounts)
					throws ProductOutOfStockException, NotInDatabaseException, UpdateException;

	@WebMethod
	public ComplexOrderEntryTO[] getStockItems(
			@XmlElement(required = true) @WebParam(name = "storeID") long storeID,
			@XmlElement(required = true) @WebParam(name = "requiredProductTOs") ProductTO[] requiredProductTOs)
					throws NotInDatabaseException, NotImplementedException;

	@WebMethod
	public ProductWithStockItemTO getProductWithStockItem(
			@XmlElement(required = true) @WebParam(name = "storeID") long storeID,
			@XmlElement(required = true) @WebParam(name = "productBarcode") long productBarcode) 
					throws NoSuchProductException, NotInDatabaseException;

}