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

/**
 * This class handles the conversion of entity classes into query content to
 * pass on to the service adapter. The service adapter requires all data to be
 * passed as Strings in a specific format which is described in its
 * documentation.
 * 
 * @author Tobias Pöppke
 * @author Robert Heinrich
 */
public class ServiceAdapterEntityConverter {
	private static String encodeString(String string) {
		// If the string is encoded there are problems with the
		// queries because of the way the service adapter handles
		// them. Leaving this here for future consideration.
		return string;
		// return QueryParameterEncoder.encodeQueryString(string);
	}

	/**
	 * Returns a string containing information about the given stock item.
	 * 
	 * @param stockItem
	 * @return a String representation of the stock item
	 */
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

	/**
	 * Returns a string containing information about the given product order.
	 * 
	 * @param productOrder
	 * @return a String representation of the product order
	 */
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

	/**
	 * Returns a string containing all needed information to create a new enterprise.
	 * 
	 * @param enterprise
	 * @return a String representation of the enterprise to be created
	 */
	public static String getCreateEnterpriseContent(ITradingEnterprise enterprise) {
		StringBuilder content = new StringBuilder();
		content.append(encodeString(enterprise.getName()));
		return content.toString();
	}

	/**
	 * Returns a string containing all necessary information to update an enterprise.
	 * 
	 * @param enterprise
	 * @return a String representation of the enterprise to be updated
	 */
	public static String getUpdateEnterpriseContent(ITradingEnterprise enterprise) {
		StringBuilder content = new StringBuilder();
		content.append(enterprise.getId());
		content.append(ServiceAdapterHeaders.SEPARATOR);
		content.append(encodeString(enterprise.getName()));
		return content.toString();
	}

	/**
	 * Returns a string containing all needed information to create a new enterprise.
	 * 
	 * @param store
	 * @return a String representation of the store to be created
	 */
	public static String getCreateStoreContent(IStore store) {
		StringBuilder content = new StringBuilder();
		content.append(encodeString(store.getEnterpriseName()));
		content.append(ServiceAdapterHeaders.SEPARATOR);
		content.append(encodeString(store.getName()));
		content.append(ServiceAdapterHeaders.SEPARATOR);
		content.append(encodeString(store.getLocation()));
		return content.toString();
	}

	/**
	 * Returns a string containing all needed information to update a store.
	 * 
	 * @param store
	 * @return a String representation of the updated store
	 */
	public static String getUpdateStoreContent(IStore store) {
		StringBuilder content = new StringBuilder();
		content.append(encodeString(store.getEnterpriseName()));
		content.append(ServiceAdapterHeaders.SEPARATOR);
		content.append(store.getId());
		content.append(ServiceAdapterHeaders.SEPARATOR);
		content.append(encodeString(store.getName()));
		content.append(ServiceAdapterHeaders.SEPARATOR);
		content.append(encodeString(store.getLocation()));
		return content.toString();
	}

	/**
	 * Returns a string containing all needed information to create a new supplier.
	 * 
	 * @param supplier
	 * @return a String representation of the supplier to be created
	 */
	public static String getCreateSupplierContent(IProductSupplier supplier) {
		StringBuilder content = new StringBuilder();
		content.append(encodeString(supplier.getName()));
		return content.toString();
	}

	/**
	 * Returns a string containing all needed information to update a supplier.
	 * 
	 * @param supplier
	 * @return a String representation of the updated store
	 */
	public static String getUpdateSupplierContent(IProductSupplier supplier) {
		StringBuilder content = new StringBuilder();
		content.append(supplier.getId());
		content.append(ServiceAdapterHeaders.SEPARATOR);
		content.append(encodeString(supplier.getName()));
		return content.toString();
	}

	/**
	 * Returns a string containing information about the given product.
	 * 
	 * @param product
	 * @return a String representation of the product
	 */
	public static String getProductContent(IProduct product) {
		StringBuilder content = new StringBuilder();
		content.append(product.getBarcode());
		content.append(ServiceAdapterHeaders.SEPARATOR);
		content.append(encodeString(product.getName()));
		content.append(ServiceAdapterHeaders.SEPARATOR);
		content.append(product.getPurchasePrice());
		return content.toString();
	}

	/**
	 * Returns a string containing information about the given user.
	 * 
	 * @param user
	 * @return a String representation of the user
	 */
	public static String getUserContent(IUser user) {
		StringBuilder content = new StringBuilder();

		DualIterator<ICredential, Role> iterator = new DualIterator<>(user.getCredentials().values(), user.getRoles());

		for (DualElement<ICredential, Role> dual : iterator) {
			content.append(encodeString(user.getUsername()));
			content.append(ServiceAdapterHeaders.SEPARATOR);
			content.append(dual.getFirstElement().getType());
			content.append(ServiceAdapterHeaders.SEPARATOR);
			content.append(encodeString(dual.getFirstElement().getCredentialString()));
			content.append(ServiceAdapterHeaders.SEPARATOR);
			content.append(encodeString(dual.getSecondElement().label().toUpperCase()));
		}

		return content.toString();
	}

	/**
	 * Returns a string containing information about the given customer.
	 * 
	 * @param customer
	 * @return a String representation of the customer
	 */
	public static String getCustomerContent(ICustomer customer) {
		StringBuilder content = new StringBuilder();

		content.append(encodeString(customer.getFirstName()));
		content.append(ServiceAdapterHeaders.SEPARATOR);
		content.append(encodeString(customer.getLastName()));
		content.append(ServiceAdapterHeaders.SEPARATOR);
		content.append(encodeString(customer.getMailAddress()));
		content.append(ServiceAdapterHeaders.SEPARATOR);

		if (customer.getPreferredStore() != null) {
			content.append(encodeString(customer.getPreferredStore().getEnterpriseName()));
			content.append(ServiceAdapterHeaders.SEPARATOR);
			content.append(customer.getPreferredStore().getId());
			content.append(ServiceAdapterHeaders.SEPARATOR);
			content.append(encodeString(customer.getPreferredStore().getName()));
			content.append(ServiceAdapterHeaders.SEPARATOR);
			content.append(encodeString(customer.getPreferredStore().getLocation()));
			content.append(ServiceAdapterHeaders.SEPARATOR);
		}

		// Mail address is used as username for customers
		content.append(encodeString(customer.getMailAddress()));

		return content.toString();
	}

	/**
	 * Returns a string containing all needed information to update a customer.
	 * 
	 * @param customer
	 * @return a String representation of the updated customer
	 */
	public static String getUpdateCustomerContent(ICustomer customer) {
		StringBuilder content = new StringBuilder();

		content.append(customer.getID());
		content.append(ServiceAdapterHeaders.SEPARATOR);
		content.append(encodeString(customer.getFirstName()));
		content.append(ServiceAdapterHeaders.SEPARATOR);
		content.append(encodeString(customer.getLastName()));
		content.append(ServiceAdapterHeaders.SEPARATOR);
		content.append(encodeString(customer.getMailAddress()));
		content.append(ServiceAdapterHeaders.SEPARATOR);

		if (customer.getPreferredStore() != null) {
			content.append(encodeString(customer.getPreferredStore().getEnterpriseName()));
			content.append(ServiceAdapterHeaders.SEPARATOR);
			content.append(customer.getPreferredStore().getId());
			content.append(ServiceAdapterHeaders.SEPARATOR);
			content.append(encodeString(customer.getPreferredStore().getName()));
			content.append(ServiceAdapterHeaders.SEPARATOR);
			content.append(encodeString(customer.getPreferredStore().getLocation()));
			content.append(ServiceAdapterHeaders.SEPARATOR);
		}

		// Mail address is used as username for customers
		content.append(encodeString(customer.getMailAddress()));

		return content.toString();
	}
}