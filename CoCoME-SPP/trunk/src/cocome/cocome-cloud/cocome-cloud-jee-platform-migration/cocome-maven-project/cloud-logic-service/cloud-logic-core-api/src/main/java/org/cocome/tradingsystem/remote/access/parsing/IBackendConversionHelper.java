package org.cocome.tradingsystem.remote.access.parsing;

import java.util.Collection;

import org.cocome.tradingsystem.inventory.data.enterprise.IProduct;
import org.cocome.tradingsystem.inventory.data.enterprise.IProductSupplier;
import org.cocome.tradingsystem.inventory.data.enterprise.ITradingEnterprise;
import org.cocome.tradingsystem.inventory.data.store.IProductOrder;
import org.cocome.tradingsystem.inventory.data.store.IStockItem;
import org.cocome.tradingsystem.inventory.data.store.IStore;
import org.cocome.tradingsystem.inventory.data.usermanager.ICustomer;
import org.cocome.tradingsystem.inventory.data.usermanager.IUser;

public interface IBackendConversionHelper {

	/**
	 * Converts a CSV response from the backend to a list of Store objects.
	 * The enterprise, suppliers and productItems attributes in the Stores are left as
	 * null and have to be filled separately.
	 * 
	 * @param input
	 * 		the csv string to be parsed
	 * 
	 * @return
	 * 		a collection of stores contained in the string
	 */
	Collection<IStore> getStores(String input);

	Collection<ICustomer> getCustomers(String input);

	Collection<IUser> getUsers(String input);

	Collection<ITradingEnterprise> getEnterprises(String input);

	Collection<IStockItem> getStockItems(String input);

	Collection<IProduct> getProducts(String input);

	Collection<IProductSupplier> getProductSuppliers(String input);

	Collection<IProductOrder> getProductOrders(String input);

}