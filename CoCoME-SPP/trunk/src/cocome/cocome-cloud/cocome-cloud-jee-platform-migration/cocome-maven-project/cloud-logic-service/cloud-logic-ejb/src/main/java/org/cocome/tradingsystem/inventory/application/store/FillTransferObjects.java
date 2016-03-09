/***************************************************************************
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
 ***************************************************************************/

package org.cocome.tradingsystem.inventory.application.store;

import java.util.ArrayList;
import java.util.List;

import org.cocome.tradingsystem.inventory.application.usermanager.credentials.CredentialTO;
import org.cocome.tradingsystem.inventory.application.usermanager.credentials.ICredential;
import org.cocome.tradingsystem.inventory.data.enterprise.Product;
import org.cocome.tradingsystem.inventory.data.enterprise.ProductSupplier;
import org.cocome.tradingsystem.inventory.data.enterprise.TradingEnterprise;
import org.cocome.tradingsystem.inventory.data.store.OrderEntry;
import org.cocome.tradingsystem.inventory.data.store.ProductOrder;
import org.cocome.tradingsystem.inventory.data.store.StockItem;
import org.cocome.tradingsystem.inventory.data.store.Store;
import org.cocome.tradingsystem.inventory.data.usermanager.CredentialType;
import org.cocome.tradingsystem.inventory.data.usermanager.ICustomer;
import org.cocome.tradingsystem.inventory.data.usermanager.IUser;
import org.cocome.tradingsystem.inventory.data.usermanager.Role;
import org.cocome.tradingsystem.inventory.data.usermanager.UserTO;
import org.cocome.tradingsystem.util.exception.NotInDatabaseException;
import org.cocome.tradingsystem.util.java.Lists;

/**
 * Helper class to transfer the data from the persistent objects to the transfer
 * objects
 * 
 * @author Yannick Welsch
 * @author Lubomir Bulej
 * @author Tobias Pöppke
 * @author Robert Heinrich
 */
public final class FillTransferObjects {

	private FillTransferObjects() {
		// utility class, not to be instantiated
	}

	//

	public static ComplexOrderEntryTO fillComplexOrderEntry(
			final OrderEntry entry
			) throws NotInDatabaseException {
		final ComplexOrderEntryTO result = new ComplexOrderEntryTO();
		result.setAmount(entry.getAmount());
		result.setProductTO(fillProductWithSupplierTO(entry.getProduct()));

		return result;
	}

	public static ComplexOrderTO fillComplexOrderTO(final ProductOrder order) throws NotInDatabaseException {
		final ComplexOrderTO result = new ComplexOrderTO();
		result.setId(order.getId());
		result.setDeliveryDate(order.getDeliveryDate());
		result.setOrderingDate(order.getOrderingDate());

		final List<ComplexOrderEntryTO> oeto = Lists.newArrayList();
		for (final OrderEntry orderentry : order.getOrderEntries()) {
			oeto.add(fillComplexOrderEntry(orderentry));
		}
		result.setOrderEntryTOs(oeto);

		return result;
	}

	public static EnterpriseTO fillEnterpriseTO(
			final TradingEnterprise enterprise
			) {
		final EnterpriseTO result = new EnterpriseTO();
		result.setId(enterprise.getId());
		result.setName(enterprise.getName());

		return result;
	}

	public static OrderEntryTO fillOrderEntryTO(final OrderEntry entry) {
		final OrderEntryTO result = new OrderEntryTO();
		result.setAmount(entry.getAmount());

		return result;
	}

	public static OrderTO fillOrderTO(final ProductOrder order) {
		final OrderTO result = new OrderTO();
		result.setId(order.getId());
		result.setDeliveryDate(order.getDeliveryDate());
		result.setOrderingDate(order.getOrderingDate());

		return result;
	}

	public static ProductTO fillProductTO(final Product product) {
		final ProductTO result = new ProductTO();
		result.setId(product.getId());
		result.setName(product.getName());
		result.setBarcode(product.getBarcode());
		result.setPurchasePrice(product.getPurchasePrice());

		return result;
	}

	public static ProductWithStockItemTO fillProductWithStockItemTO(
			final StockItem stockItem
			) {
		final ProductWithStockItemTO result = new ProductWithStockItemTO();
		final Product product = stockItem.getProduct();

		result.setId(product.getId());
		result.setName(product.getName());
		result.setBarcode(product.getBarcode());
		result.setPurchasePrice(product.getPurchasePrice());
		result.setStockItemTO(fillStockItemTO(stockItem));

		return result;
	}

	public static ProductWithSupplierAndStockItemTO fillProductWithSupplierAndStockItemTO(
			final StockItem stockItem
			) throws NotInDatabaseException {
		final ProductWithSupplierAndStockItemTO result = new ProductWithSupplierAndStockItemTO();
		final Product product = stockItem.getProduct();

		result.setId(product.getId());
		result.setName(product.getName());
		result.setBarcode(product.getBarcode());
		result.setPurchasePrice(product.getPurchasePrice());
		result.setSupplierTO(fillSupplierTO(product.getSupplier()));
		result.setStockItemTO(fillStockItemTO(stockItem));

		return result;
	}

	public static ProductWithSupplierTO fillProductWithSupplierTO(
			final Product product
			) throws NotInDatabaseException {
		final ProductWithSupplierTO result = new ProductWithSupplierTO();
		result.setId(product.getId());
		result.setBarcode(product.getBarcode());
		result.setName(product.getName());
		result.setPurchasePrice(product.getPurchasePrice());
		result.setSupplierTO(fillSupplierTO(product.getSupplier()));
		
		return result;
	}

	static StockItemTO fillStockItemTO(final StockItem si) {
		final StockItemTO result = new StockItemTO();
		result.setId(si.getId());
		result.setAmount(si.getAmount());
		result.setMinStock(si.getMinStock());
		result.setMaxStock(si.getMaxStock());
		result.setSalesPrice(si.getSalesPrice());
		result.setIncomingAmount(si.getIncomingAmount());

		return result;
	}

	public static StoreTO fillStoreTO(final Store store) {
		final StoreTO result = new StoreTO();
		result.setId(store.getId());
		result.setName(store.getName());
		result.setLocation(store.getLocation());

		return result;
	}

	public static StoreWithEnterpriseTO fillStoreWithEnterpriseTO(
			final Store store
			) throws NotInDatabaseException {
		final StoreWithEnterpriseTO result = new StoreWithEnterpriseTO();
		result.setId(store.getId());
		result.setName(store.getName());
		result.setLocation(store.getLocation());
		result.setEnterpriseTO(fillEnterpriseTO(store.getEnterprise()));

		return result;
	}

	static SupplierTO fillSupplierTO(final ProductSupplier supplier) {
		final SupplierTO result = new SupplierTO();
		if (supplier != null) {
			result.setId(supplier.getId());
			result.setName(supplier.getName());
		} else {
			result.setId(-1);
			result.setName("N/A");
		}

		return result;
	}

	public static CustomerWithStoreTO fillCustomerWithStoreTO(final ICustomer customer) {
		final CustomerWithStoreTO result = new CustomerWithStoreTO();
		
		result.setId(customer.getID());
		result.setUsername(customer.getUser().getUsername());
		result.setRoles(new ArrayList<Role>(customer.getUser().getRoles()));
		result.setCreditCardInfos(new ArrayList<String>(customer.getCreditCardInfo()));
		result.setFirstName(customer.getFirstName());
		result.setLastName(customer.getLastName());
		result.setMailAddress(customer.getMailAddress());
		result.setPreferredStoreTO(
				customer.getPreferredStore() != null ? fillStoreTO(customer.getPreferredStore()) : null);
		
		return result;
	}
	
	public static CredentialTO fillCredentialTO(final ICredential credential) {
		final CredentialTO result = new CredentialTO();
		
		result.setType(credential.getType());
		result.setCredentialChars(credential.getCredentialChars());
		
		return result;
	}
	
	public static UserTO fillUserTO(final IUser user) {
		final UserTO result = new UserTO();
		
		result.setUsername(user.getUsername());
		result.setRoles(new ArrayList<Role>(user.getRoles()));
		
		// Only include authentication token but no other credentials
		ICredential authToken = user.getCredential(CredentialType.AUTH_TOKEN); 
		if (authToken != null) {
			ArrayList<CredentialTO> credentials = new ArrayList<>(1);
			credentials.add(fillCredentialTO(authToken));
			result.setCredentials(credentials);
		}
		
		return result;
	}
}
