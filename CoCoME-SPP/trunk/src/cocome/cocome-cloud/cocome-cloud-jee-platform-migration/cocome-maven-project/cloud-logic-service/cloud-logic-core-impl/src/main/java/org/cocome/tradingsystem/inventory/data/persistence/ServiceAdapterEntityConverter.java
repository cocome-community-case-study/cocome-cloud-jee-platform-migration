package org.cocome.tradingsystem.inventory.data.persistence;

import org.cocome.tradingsystem.inventory.application.usermanager.Role;
import org.cocome.tradingsystem.inventory.application.usermanager.credentials.ICredential;
import org.cocome.tradingsystem.inventory.data.enterprise.IProduct;
import org.cocome.tradingsystem.inventory.data.enterprise.IProductSupplier;
import org.cocome.tradingsystem.inventory.data.enterprise.ITradingEnterprise;
import org.cocome.tradingsystem.inventory.data.store.IOrderEntry;
import org.cocome.tradingsystem.inventory.data.store.IProductOrder;
import org.cocome.tradingsystem.inventory.data.store.IStockItem;
import org.cocome.tradingsystem.inventory.data.store.IStore;
import org.cocome.tradingsystem.inventory.data.usermanager.ICustomer;
import org.cocome.tradingsystem.inventory.data.usermanager.IUser;
import org.cocome.tradingsystem.util.java.DualElement;
import org.cocome.tradingsystem.util.java.DualIterator;

import de.kit.ipd.java.utils.time.TimeUtils;

public class ServiceAdapterEntityConverter {
	public static String getStockItemContent(IStockItem stockItem) {
		StringBuilder content = new StringBuilder();
		content.append(stockItem.getStore().getId());
		content.append(ServiceAdapterHeaders.SEPARATOR);
		content.append(stockItem.getProductBarcode());
		content.append(ServiceAdapterHeaders.SEPARATOR);
		content.append(stockItem.getMinStock());
		content.append(ServiceAdapterHeaders.SEPARATOR);
		content.append(stockItem.getMaxStock());
		content.append(ServiceAdapterHeaders.SEPARATOR);
		content.append(stockItem.getIncomingAmount());
		content.append(ServiceAdapterHeaders.SEPARATOR);
		content.append(stockItem.getAmount());
		content.append(ServiceAdapterHeaders.SEPARATOR);
		content.append(stockItem.getSalesPrice());
		return content.toString();
	}
	
	public static String getProductOrderContent(IProductOrder productOrder) {
		StringBuilder content = new StringBuilder();
		for (IOrderEntry entry : productOrder.getOrderEntries()) {
			content.append(productOrder.getId());
			content.append(ServiceAdapterHeaders.SEPARATOR);
			content.append(productOrder.getStore().getId());
			content.append(ServiceAdapterHeaders.SEPARATOR);
			content.append(entry.getProductBarcode());
			content.append(ServiceAdapterHeaders.SEPARATOR);
			content.append(productOrder.getDeliveryDate() == null ? "00-00-0000" 
					: TimeUtils.convertToStringDate(productOrder.getDeliveryDate()));
			content.append(ServiceAdapterHeaders.SEPARATOR);
			content.append(TimeUtils.convertToStringDate(productOrder.getOrderingDate()));
			content.append(ServiceAdapterHeaders.SEPARATOR);
			content.append(entry.getAmount());
			content.append("\n");
		}
		return content.toString();
	}
	
	public static String getCreateEnterpriseContent(ITradingEnterprise enterprise) {
		StringBuilder content = new StringBuilder();
		content.append(enterprise.getName());
		return content.toString();
	}
	
	public static String getUpdateEnterpriseContent(ITradingEnterprise enterprise) {
		StringBuilder content = new StringBuilder();
		content.append(enterprise.getId());
		content.append(ServiceAdapterHeaders.SEPARATOR);
		content.append(enterprise.getName());
		return content.toString();
	}
	
	public static String getCreateStoreContent(IStore store) {
		StringBuilder content = new StringBuilder();
		content.append(store.getEnterpriseName());
		content.append(ServiceAdapterHeaders.SEPARATOR);
		content.append(store.getName());
		content.append(ServiceAdapterHeaders.SEPARATOR);
		content.append(store.getLocation());
		return content.toString();
	}

	public static String getUpdateStoreContent(IStore store) {
		StringBuilder content = new StringBuilder();
		content.append(store.getId());
		content.append(ServiceAdapterHeaders.SEPARATOR);
		content.append(store.getName());
		content.append(ServiceAdapterHeaders.SEPARATOR);
		content.append(store.getLocation());
		return content.toString();
	}

	public static String getCreateSupplierContent(IProductSupplier supplier) {
		StringBuilder content = new StringBuilder();
		content.append(supplier.getName());
		return content.toString();
	}

	public static String getUpdateSupplierContent(IProductSupplier supplier) {
		StringBuilder content = new StringBuilder();
		content.append(supplier.getId());
		content.append(ServiceAdapterHeaders.SEPARATOR);
		content.append(supplier.getName());
		return content.toString();
	}

	public static String getProductContent(IProduct product) {
		StringBuilder content = new StringBuilder();
		content.append(product.getBarcode());
		content.append(ServiceAdapterHeaders.SEPARATOR);
		content.append(product.getName());
		content.append(ServiceAdapterHeaders.SEPARATOR);
		content.append(product.getPurchasePrice());
		return content.toString();
	}

	public static String getUserContent(IUser user) {
		StringBuilder content = new StringBuilder();
		
		DualIterator<ICredential, Role> iterator = new DualIterator<>(
				user.getCredentials().values(), user.getRoles());
		
		for (DualElement<ICredential, Role> dual : iterator) {
			content.append(user.getUsername());
			content.append(ServiceAdapterHeaders.SEPARATOR);
			content.append(dual.getFirstElement().getType());
			content.append(ServiceAdapterHeaders.SEPARATOR);
			content.append(dual.getFirstElement().getCredentialString());
			content.append(ServiceAdapterHeaders.SEPARATOR);
			content.append(dual.getSecondElement().label().toUpperCase());
		}
		
		return content.toString();
	}
	

	public static String getCustomerContent(ICustomer customer) {
		StringBuilder content = new StringBuilder();
		
		content.append(customer.getFirstName());
		content.append(ServiceAdapterHeaders.SEPARATOR);
		content.append(customer.getLastName());
		content.append(ServiceAdapterHeaders.SEPARATOR);
		content.append(customer.getMailAddress());
		content.append(ServiceAdapterHeaders.SEPARATOR);
		
		if (customer.getPreferredStore() != null) {
			content.append(customer.getPreferredStore().getEnterpriseName());
			content.append(ServiceAdapterHeaders.SEPARATOR);
			content.append(customer.getPreferredStore().getId());
			content.append(ServiceAdapterHeaders.SEPARATOR);
			content.append(customer.getPreferredStore().getName());
			content.append(ServiceAdapterHeaders.SEPARATOR);
			content.append(customer.getPreferredStore().getLocation());
			content.append(ServiceAdapterHeaders.SEPARATOR);
		}
		
		// Mail address is used as username for customers
		content.append(customer.getMailAddress());
		
		return content.toString();
	}
	
	public static String getUpdateCustomerContent(ICustomer customer) {
		StringBuilder content = new StringBuilder();
		
		content.append(customer.getID());
		content.append(ServiceAdapterHeaders.SEPARATOR);
		content.append(customer.getFirstName());
		content.append(ServiceAdapterHeaders.SEPARATOR);
		content.append(customer.getLastName());
		content.append(ServiceAdapterHeaders.SEPARATOR);
		content.append(customer.getMailAddress());
		content.append(ServiceAdapterHeaders.SEPARATOR);
		
		if (customer.getPreferredStore() != null) {
			content.append(customer.getPreferredStore().getEnterpriseName());
			content.append(ServiceAdapterHeaders.SEPARATOR);
			content.append(customer.getPreferredStore().getId());
			content.append(ServiceAdapterHeaders.SEPARATOR);
			content.append(customer.getPreferredStore().getName());
			content.append(ServiceAdapterHeaders.SEPARATOR);
			content.append(customer.getPreferredStore().getLocation());
			content.append(ServiceAdapterHeaders.SEPARATOR);
		}
		
		// Mail address is used as username for customers
		content.append(customer.getMailAddress());
		
		return content.toString();
	}
}