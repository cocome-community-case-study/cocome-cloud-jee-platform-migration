package org.cocome.tradingsystem.inventory.data.persistence;

import java.io.IOException;

import javax.ejb.CreateException;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import org.apache.log4j.Logger;
import org.cocome.tradingsystem.inventory.data.enterprise.Product;
import org.cocome.tradingsystem.inventory.data.enterprise.ProductSupplier;
import org.cocome.tradingsystem.inventory.data.enterprise.TradingEnterprise;
import org.cocome.tradingsystem.inventory.data.store.ProductOrder;
import org.cocome.tradingsystem.inventory.data.store.StockItem;
import org.cocome.tradingsystem.inventory.data.store.Store;
import org.cocome.tradingsystem.inventory.data.usermanager.ICustomer;
import org.cocome.tradingsystem.inventory.data.usermanager.IUser;
import org.cocome.tradingsystem.remote.access.connection.CSVBackendConnection;

@Stateless
public class CloudPersistenceContext implements IPersistenceContextLocal {
	// TODO make these calls asynchronous by pushing them into a JMS queue
	// and implement bean that asynchronously tries to persist the changes
	
	// TODO create query class to hold information about the queries

	private static final Logger LOG = Logger.getLogger(CloudPersistenceContext.class);
	
	@Inject
	CSVBackendConnection postData;
	
	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void updateEntity(ProductOrder productOrder) throws UpdateException {
		String content = ServiceAdapterEntityConverter.getProductOrderContent(productOrder);
		
		try {
			postData.sendUpdateQuery("ProductOrder", ServiceAdapterHeaders.PRODUCTORDER_HEADER, content);
		} catch (IOException e) {
			LOG.error("Could not execute post because of an IOException: " + e.getMessage());
			throw new UpdateException("Could not update entity!", e);
		}
		
		String response = postData.getResponse();
		
		if(response.contains("FAIL") || !response.contains("SUCCESS")) {
			throw new UpdateException("Could not update entity!");
		}
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void createEntity(StockItem stockItem) throws CreateException {
		String content = ServiceAdapterEntityConverter.getStockItemContent(stockItem);
		try {
			postData.sendCreateQuery("StockItem", ServiceAdapterHeaders.STOCKITEM_HEADER, content);
		} catch (IOException e) {
			LOG.error("Could not execute post because of an IOException: " + e.getMessage());
			throw new CreateException("Could not create entity!");
		}
		
		if(!postData.getResponse().contains("SUCCESS")) {
			throw new CreateException("Could not create entity!");
		}
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void updateEntity(StockItem stockItem) throws UpdateException {
		String content = ServiceAdapterEntityConverter.getStockItemContent(stockItem);
		
		try {
			postData.sendUpdateQuery("StockItem", ServiceAdapterHeaders.STOCKITEM_HEADER, content);
		} catch (IOException e) {
			LOG.error("Could not execute post because of an IOException: " + e.getMessage());
			throw new UpdateException("Could not update entity!", e);
		}
		
		if(!postData.getResponse().contains("SUCCESS")) {
			throw new UpdateException("Could not update entity!");
		}
	}

	@Override
	public void createEntity(TradingEnterprise enterprise) throws CreateException {
		String content = ServiceAdapterEntityConverter.getCreateEnterpriseContent(enterprise);
		try {
			postData.sendCreateQuery("enterprise", ServiceAdapterHeaders.ENTERPRISE_CREATE_HEADER, content);
		} catch (IOException e) {
			LOG.error("Could not execute post because of an IOException: " + e.getMessage());
			throw new CreateException("Could not create entity!");
		}
		
		if(!postData.getResponse().contains("SUCCESS")) {
			throw new CreateException("Could not create entity!");
		}
	}

	@Override
	public void createEntity(ProductOrder productOrder) throws CreateException {
		String content = ServiceAdapterEntityConverter.getProductOrderContent(productOrder);
		try {
			postData.sendCreateQuery("ProductOrder", ServiceAdapterHeaders.PRODUCTORDER_HEADER, content);
		} catch (IOException e) {
			LOG.error("Could not execute post because of an IOException: " + e.getMessage());
			throw new CreateException("Could not create entity!");
		}
		
		if(postData.getResponse().contains("FAIL") || !postData.getResponse().contains("SUCCESS")) {
			throw new CreateException("Could not create entity!");
		}
	}

	@Override
	public void updateEntity(TradingEnterprise enterprise) throws UpdateException {
		String content = ServiceAdapterEntityConverter.getUpdateEnterpriseContent(enterprise);
		
		try {
			postData.sendUpdateQuery("Enterprise", ServiceAdapterHeaders.ENTERPRISE_UPDATE_HEADER, content);
		} catch (IOException e) {
			// TODO perhaps throw this exception to caller?
			LOG.error("Could not execute post because of an IOException: " + e.getMessage());
			throw new UpdateException("Could not update entity!", e);
		}
		
		if(!postData.getResponse().contains("SUCCESS")) {
			throw new UpdateException("Could not update entity!");
		}
		
	}

	@Override
	public void createEntity(Store store) throws CreateException {
		String content = ServiceAdapterEntityConverter.getCreateStoreContent(store);
		try {
			postData.sendCreateQuery("Store", ServiceAdapterHeaders.STORE_CREATE_HEADER, content);
		} catch (IOException e) {
			LOG.error("Could not execute post because of an IOException: " + e.getMessage());
			throw new CreateException("Could not create entity!");
		}
		
		if(!postData.getResponse().contains("SUCCESS")) {
			throw new CreateException("Could not create entity!");
		}
	}

	@Override
	public void updateEntity(Store store) throws UpdateException {
		String content = ServiceAdapterEntityConverter.getUpdateStoreContent(store);
		
		try {
			postData.sendUpdateQuery("Store", ServiceAdapterHeaders.STORE_UPDATE_HEADER, content);
		} catch (IOException e) {
			LOG.error("Could not execute post because of an IOException: " + e.getMessage());
			throw new UpdateException("Could not create entity!", e);
		}
		
		if(!postData.getResponse().contains("SUCCESS")) {
			throw new UpdateException("Could not update entity!");
		}
	}

	@Override
	public void createEntity(ProductSupplier supplier) throws CreateException {
		String content = ServiceAdapterEntityConverter.getCreateSupplierContent(supplier);
		try {
			postData.sendCreateQuery("ProductSupplier", ServiceAdapterHeaders.PRODUCTSUPPLIER_CREATE_HEADER, content);
		} catch (IOException e) {
			LOG.error("Could not execute post because of an IOException: " + e.getMessage());
			throw new CreateException("Could not create entity!");
		}
		
		if(!postData.getResponse().contains("SUCCESS")) {
			throw new CreateException("Could not create entity!");
		}
	}

	@Override
	public void updateEntity(ProductSupplier supplier) throws UpdateException {
		String content = ServiceAdapterEntityConverter.getUpdateSupplierContent(supplier);
		
		try {
			postData.sendUpdateQuery("ProductSupplier", ServiceAdapterHeaders.PRODUCTSUPPLIER_UPDATE_HEADER, content);
		} catch (IOException e) {
			LOG.error("Could not execute post because of an IOException: " + e.getMessage());
			throw new UpdateException("Could not update entity!", e);
		}
		
		if(!postData.getResponse().contains("SUCCESS")) {
			throw new UpdateException("Could not update entity!");
		}
	}

	@Override
	public void createEntity(Product product) throws CreateException {
		String content = ServiceAdapterEntityConverter.getProductContent(product);
		try {
			postData.sendCreateQuery("Product", ServiceAdapterHeaders.PRODUCT_HEADER, content);
		} catch (IOException e) {
			LOG.error("Could not execute post because of an IOException: " + e.getMessage());
			throw new CreateException("Could not create entity!");
		}
		
		if(!postData.getResponse().contains("SUCCESS")) {
			throw new CreateException("Could not create entity!");
		}
	}

	@Override
	public void updateEntity(Product product) throws UpdateException {
		String content = ServiceAdapterEntityConverter.getProductContent(product);
		
		try {
			postData.sendUpdateQuery("Product", ServiceAdapterHeaders.PRODUCT_HEADER, content);
		} catch (IOException e) {
			LOG.error("Could not execute post because of an IOException: " + e.getMessage());
			throw new UpdateException("Could not update entity!", e);
		}
		
		if(!postData.getResponse().contains("SUCCESS")) {
			throw new UpdateException("Could not update entity!");
		}
	}

	@Override
	public void createEntity(Object entity) throws CreateException {
		if (entity instanceof TradingEnterprise) {
			createEntity((TradingEnterprise) entity);		
		} else if (entity instanceof Store) {
			createEntity((Store) entity);
		} else if (entity instanceof Product) {
			createEntity((Product) entity);
		} else if (entity instanceof ProductOrder) {
			createEntity((ProductOrder) entity);
		} else if (entity instanceof StockItem) {
			createEntity((StockItem) entity);
		} else if (entity instanceof ProductSupplier) {
			createEntity((ProductSupplier) entity);
		} else {
			throw new CreateException("The entity with class " + entity.getClass() + " is not recognized and can not be created!");
		}
	}

	@Override
	public void updateEntity(Object entity) throws UpdateException {
		if (entity instanceof TradingEnterprise) {
			updateEntity((TradingEnterprise) entity);		
		} else if (entity instanceof Store) {
			updateEntity((Store) entity);
		} else if (entity instanceof Product) {
			updateEntity((Product) entity);
		} else if (entity instanceof ProductOrder) {
			updateEntity((ProductOrder) entity);
		} else if (entity instanceof StockItem) {
			updateEntity((StockItem) entity);
		} else if (entity instanceof ProductSupplier) {
			updateEntity((ProductSupplier) entity);
		} else {
			throw new UpdateException("The entity with class " + entity.getClass() + " is not recognized and can not be updated!");
		}		
	}

	@Override
	public void createEntity(IUser user) throws CreateException {
		String content = ServiceAdapterEntityConverter.getUserContent(user);
		try {
			postData.sendCreateQuery("LoginUser", ServiceAdapterHeaders.USER_CREATE_HEADER, content);
		} catch (IOException e) {
			LOG.error("Could not execute post because of an IOException: " + e.getMessage());
			throw new CreateException("Could not create entity!");
		}
		
		if(!postData.getResponse().contains("SUCCESS")) {
			throw new CreateException("Could not create entity!");
		}
	}

	@Override
	public void updateEntity(IUser user) throws UpdateException {
		String content = ServiceAdapterEntityConverter.getUserContent(user);
		try {
			postData.sendUpdateQuery("LoginUser", ServiceAdapterHeaders.USER_UPDATE_HEADER, content);
		} catch (IOException e) {
			LOG.error("Could not execute post because of an IOException: " + e.getMessage());
			throw new UpdateException("Could not connect to the database!");
		}
		
		if(!postData.getResponse().contains("SUCCESS")) {
			throw new UpdateException("Could not create entity!");
		}
	}

	@Override
	public void createEntity(ICustomer customer) throws CreateException {
		String content = ServiceAdapterEntityConverter.getCustomerContent(customer);
		
		// TODO Transactions would be good here
		createEntity(customer.getUser());
		
		try {
			postData.sendCreateQuery("Customer", 
					customer.getPreferredStore() == null ? 
							ServiceAdapterHeaders.CUSTOMER_CREATE_HEADER 
							: ServiceAdapterHeaders.CUSTOMER_CREATE_HEADER_WITH_STORE,
					content);
		} catch (IOException e) {
			LOG.error("Could not execute post because of an IOException: " + e.getMessage());
			throw new CreateException("Could not create entity!");
		}
		
		if(!postData.getResponse().contains("SUCCESS")) {
			throw new CreateException("Could not create entity!");
		}
	}

	@Override
	public void updateEntity(ICustomer customer) throws UpdateException {
		String content = ServiceAdapterEntityConverter.getUpdateCustomerContent(customer);
		
		try {
			postData.sendUpdateQuery("Customer", 
					customer.getPreferredStore() == null ? 
							ServiceAdapterHeaders.CUSTOMER_UPDATE_HEADER 
							: ServiceAdapterHeaders.CUSTOMER_UPDATE_HEADER_WITH_STORE,
					content);
		} catch (IOException e) {
			LOG.error("Could not execute post because of an IOException: " + e.getMessage());
			throw new UpdateException("Could not connect to the database!");
		}
		
		if(!postData.getResponse().contains("SUCCESS")) {
			throw new UpdateException("Could not create entity!");
		}
	}
	
	
}
