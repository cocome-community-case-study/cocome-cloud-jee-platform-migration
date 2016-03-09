package org.cocome.tradingsystem.remote.access.parsing;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Provider;

import org.apache.log4j.Logger;
import org.cocome.tradingsystem.inventory.application.usermanager.credentials.CredentialFactory;
import org.cocome.tradingsystem.inventory.application.usermanager.credentials.ICredential;
import org.cocome.tradingsystem.inventory.data.enterprise.Product;
import org.cocome.tradingsystem.inventory.data.enterprise.ProductSupplier;
import org.cocome.tradingsystem.inventory.data.enterprise.TradingEnterprise;
import org.cocome.tradingsystem.inventory.data.store.OrderEntry;
import org.cocome.tradingsystem.inventory.data.store.ProductOrder;
import org.cocome.tradingsystem.inventory.data.store.StockItem;
import org.cocome.tradingsystem.inventory.data.store.Store;
import org.cocome.tradingsystem.inventory.data.usermanager.CredentialType;
import org.cocome.tradingsystem.inventory.data.usermanager.Customer;
import org.cocome.tradingsystem.inventory.data.usermanager.ICustomer;
import org.cocome.tradingsystem.inventory.data.usermanager.IUser;
import org.cocome.tradingsystem.inventory.data.usermanager.Role;
import org.cocome.tradingsystem.inventory.data.usermanager.User;

import de.kit.ipd.java.utils.framework.table.Row;
import de.kit.ipd.java.utils.parsing.csv.CSVParser;
import de.kit.ipd.java.utils.time.TimeUtils;



@RequestScoped
public class CSVHelper {
	
	@Inject
	Provider<Store> storeProvider;
	
	@Inject
	Provider<TradingEnterprise> enterpriseProvider;
	
	@Inject
	Provider<StockItem> stockItemProvider;
	
	@Inject
	Provider<Product> productProvider;
	
	@Inject
	Provider<ProductSupplier> supplierProvider;
	
	@Inject
	Provider<ProductOrder> orderProvider;
	
	@Inject
	Provider<OrderEntry> orderEntryProvider;
	
	@Inject
	Provider<Customer> customerProvider;
	
	@Inject
	Provider<User> userProvider;
	
	@Inject
	CredentialFactory credFactory;
	
	private static final Logger LOG = Logger.getLogger(CSVHelper.class);
	
	private Store getStoreFromRow(Row<String> row) {
		return getStoreFromRow(row, 0);
	}
	
	private Store getStoreFromRow(Row<String> row, int offset) {
		if (offset < 0) offset = 0;
		
		Store result = storeProvider.get();
		
		String enterpriseName = row.getColumns().get(0 + offset).getValue();
		result.setEnterpriseName(enterpriseName.equals("null") ? null : enterpriseName);
		
		String id = row.getColumns().get(1 + offset).getValue();
		result.setId(id.equals("null") ? Long.MIN_VALUE : Long.parseLong(id));
		
		String storeName = row.getColumns().get(2 + offset).getValue();
		result.setName(storeName.equals("null") ? null : storeName);
		
		String storeLocation = row.getColumns().get(3 + offset).getValue();
		result.setLocation(storeLocation.equals("null") ? null : storeLocation);
		
		return result;
	}

	private ProductSupplier getProductSupplierFromEnterpriseRow(Row<String> row) {
		if (row.getColumns().get(2).getValue().equals("N/A")) {
			return null;
		}
		
		ProductSupplier result = supplierProvider.get();		
		result.setId(Long.parseLong(row.getColumns().get(2).getValue()));
		result.setName(row.getColumns().get(3).getValue());
		return result;
	}
	
	private TradingEnterprise getEnterpriseFromRow(Row<String> row) {
		TradingEnterprise result = enterpriseProvider.get();
		
		result.setId(Long.parseLong(row.getColumns().get(0).getValue()));
		result.setName(row.getColumns().get(1).getValue());
		
		return result;
	}
	
	private StockItem getStockItemFromRow(Row<String> row) {
		StockItem result = stockItemProvider.get();
		
		result.setStoreName(row.getColumns().get(1).getValue());
		result.setStoreLocation(row.getColumns().get(2).getValue());
		result.setProductBarcode(Long.parseLong(row.getColumns().get(3).getValue()));
		result.setId(Long.parseLong(row.getColumns().get(4).getValue()));
		result.setMinStock(Long.parseLong(row.getColumns().get(5).getValue()));
		result.setMaxStock(Long.parseLong(row.getColumns().get(6).getValue()));
		result.setIncomingAmount(Long.parseLong(row.getColumns().get(7).getValue()));
		result.setAmount(Long.parseLong(row.getColumns().get(8).getValue()));
		result.setSalesPrice(Double.parseDouble(row.getColumns().get(9).getValue()));
		
		return result;
	}
	
	private Product getProductFromRow(Row<String> row) {
		Product result = productProvider.get();
		
		result.setBarcode(Long.parseLong(row.getColumns().get(0).getValue()));
		result.setName(row.getColumns().get(1).getValue());
		result.setPurchasePrice(Double.parseDouble(row.getColumns().get(2).getValue()));
		
		return result;
	}
	
	private ProductSupplier getProductSupplierFromRow(Row<String> row) {
		ProductSupplier result = supplierProvider.get();
		
		result.setId(Long.parseLong(row.getColumns().get(0).getValue()));
		result.setName(row.getColumns().get(1).getValue());
		
		return result;
	}
	
	private ProductOrder getProductOrderFromRow(Row<String> row) {
		ProductOrder result = orderProvider.get();
		
		result.setId(Long.parseLong(row.getColumns().get(0).getValue()));
		result.setStoreName(row.getColumns().get(2).getValue());
		result.setStoreLocation(row.getColumns().get(3).getValue());
		result.setDeliveryDate(TimeUtils.convertToDateObject(row.getColumns().get(5).getValue()));
		result.setOrderingDate(TimeUtils.convertToDateObject(row.getColumns().get(6).getValue()));
		
		return result;
	}
	
	private OrderEntry getOrderEntryFromRow(Row<String> row) {
		OrderEntry result = orderEntryProvider.get();
		
		result.setAmount(Long.parseLong(row.getColumns().get(7).getValue()));
		result.setProductBarcode(Long.parseLong(row.getColumns().get(4).getValue()));
		
		return result;
	}
	
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
	public Collection<Store> getStoresFromCSV(String input) {
		CSVParser parser = new CSVParser();
		parser.parse(input);
		
		LinkedList<Store> stores = new LinkedList<Store>();
 		
		if (parser.getModel().getRows().size() > 0) {
			for (Row<String> row : parser.getModel().getRows()) {
				stores.add(getStoreFromRow(row));
			}
		}
		
		return stores;
	}
	
	public Collection<ICustomer> getCustomersFromCSV(String input) {
		LOG.debug("Parsing customer from input: " + input);
		CSVParser parser = new CSVParser();
		parser.parse(input);
		
		int tableRows =  parser.getModel().getRows().size();
		
		LOG.debug("Parsing customer model: " + parser.getModel().toString() + " with " + tableRows + " rows.");
		
		HashMap<Long, ICustomer> customers = null;
		
		if (tableRows > 0) {
			// Make sure to reserve enough initial space for the hash map
			int initialCapacity = (int) (tableRows / 0.75 + 1); 
			customers = new HashMap<>(initialCapacity);
			
			for (Row<String> row : parser.getModel().getRows()) {
				extractCustomerRow(customers, row);
			}
		}
		
		if (customers == null) {
			LOG.warn("No customers found, returning empty list");
			return new LinkedList<>();
		} else {
			return customers.values();
		}
	}
	
	
	private void extractCustomerRow(HashMap<Long, ICustomer> customers, Row<String> row) {
		LOG.debug("Parsing row: " + row.toString());
		
		Customer currentCustomer = getCustomerFromRow(row);
		User currentUser = getUserFromRow(row, 9);
		String currentCreditInfo = row.getColumns().get(4).getValue();
		Store preferredStore = getStoreFromRow(row, 5);
		ICustomer savedCustomer = customers.get(currentCustomer.getID());
		
		LOG.debug("Got from row: " + currentCustomer + "; " + currentUser);
		
		currentCustomer.setUser(currentUser);
		
		if (savedCustomer == null) {
			savedCustomer = currentCustomer;
			customers.put(currentCustomer.getID(), currentCustomer);
		}
		
		if (currentCreditInfo != null && !currentCreditInfo.equals("null")) {
			Set<String> creditCardInfo = savedCustomer.getCreditCardInfo();
			
			if (creditCardInfo == null) {
				creditCardInfo = new HashSet<>();
				savedCustomer.setCreditCardInfo(creditCardInfo);
			}
			
			savedCustomer.addCreditCardInfo(currentCreditInfo);
		}
		savedCustomer.setPreferredStore(preferredStore);
	}

	private Customer getCustomerFromRow(Row<String> row) {
		Customer result = customerProvider.get();
		
		result.setID(Long.parseLong(row.getColumns().get(0).getValue()));
		result.setFirstName(row.getColumns().get(1).getValue());
		result.setLastName(row.getColumns().get(2).getValue());
		result.setMailAddress(row.getColumns().get(3).getValue());
		
		return result;
	}

	public Collection<IUser> getUsersFromCSV(String input) {
		CSVParser parser = new CSVParser();
		parser.parse(input);
		
		
		int tableRows =  parser.getModel().getRows().size();
		
		LOG.debug("Parsing user model: " + parser.getModel().toString());
		
		HashMap<String, IUser> users = null;
		
		if (tableRows > 0) {
			// Make sure to reserve enough initial space for the hash map
			int initialCapacity = (int) (tableRows / 0.75 + 1); 
			users = new HashMap<>(initialCapacity);
			
			for (Row<String> row : parser.getModel().getRows()) {
				extractUserRow(users, row);
			}
		}
		
		if (users == null) {
			return new LinkedList<IUser>();
		} else {
			return users.values();
		}
	}
	
	private void extractUserRow(HashMap<String, IUser> users, Row<String> row) {
		LOG.debug("Parsing row: " + row.toString());
		
		User currentUser = getUserFromRow(row);
		Role currentRole = getRoleFromRow(row);
		ICredential currentCredential = getCredentialFromRow(row); 
		IUser savedUser = users.get(currentUser.getUsername());
		
		LOG.debug("Got from row: " + currentUser + "; " + currentRole);
		
		if (savedUser == null) {
			savedUser = currentUser;
			users.put(currentUser.getUsername(), currentUser);
		}
		
		if (currentCredential != null) {
			Map<CredentialType, ICredential> cred = savedUser.getCredentials();
			
			if (cred == null) {
				cred = new HashMap<>();
				savedUser.setCredentials(cred);
			}
			
			savedUser.getCredentials().put(currentCredential.getType(), currentCredential);
		}
		
		if (currentRole != null) {
			Set<Role> roles = savedUser.getRoles();
			
			if (roles == null) {
				roles = new HashSet<>();
				savedUser.setRoles(roles);
			}
			
			savedUser.getRoles().add(currentRole);
		}
	}

	private ICredential getCredentialFromRow(Row<String> row) {
		CredentialType type = CredentialType.valueOf(row.getColumns().get(2).getValue().toUpperCase());
		ICredential cred = credFactory.getCredential(type);
		cred.setCredentialString(row.getColumns().get(3).getValue());
		return cred;
	}

	private Role getRoleFromRow(Row<String> row) {
		return Role.valueOf(row.getColumns().get(4).getValue().toUpperCase());
	}

	private User getUserFromRow(Row<String> row) {
		return getUserFromRow(row, 0);
	}
	
	private User getUserFromRow(Row<String> row, int offset) {
		if (offset < 0) offset = 0;
		
		User user = userProvider.get();
		user.setUsername(row.getColumns().get(1 + offset).getValue());
		
		return user;
	}

	public Collection<TradingEnterprise> getEnterprisesFromCSV(String input) {
		CSVParser parser = new CSVParser();
		parser.parse(input);
		
		int tableRows =  parser.getModel().getRows().size();
		
		LOG.debug("Parsing enterprise model: " + parser.getModel().toString());
		
		HashMap<Long, TradingEnterprise> enterprises = null;
 		
		if (tableRows > 0) {
			// Make sure to reserve enough initial space for the hash map
			int initialCapacity = (int) (tableRows / 0.75 + 1); 
			enterprises = new HashMap<Long, TradingEnterprise>(initialCapacity);
			
			for (Row<String> row : parser.getModel().getRows()) {
				extractEnterpriseRow(enterprises, row);
			}
		}
		
		if (enterprises == null) {
			return new LinkedList<TradingEnterprise>();
		} else {
			return enterprises.values();
		}
	}

	private void extractEnterpriseRow(
			HashMap<Long, TradingEnterprise> enterprises, Row<String> row) {
		LOG.debug("Parsing row: " + row.toString());
		
		TradingEnterprise currentEnterprise = getEnterpriseFromRow(row);
		ProductSupplier currentProductSupplier = getProductSupplierFromEnterpriseRow(row);
		TradingEnterprise savedEnterprise = enterprises.get(currentEnterprise.getId());
		
		LOG.debug("Got from row: " + currentEnterprise + "; " + currentProductSupplier);
		
		if (savedEnterprise != null && currentProductSupplier != null) {
			// Just add the product supplier if the containing enterprise was already processed
			LOG.debug("Adding product supplier to saved enterprise: " + currentProductSupplier);
			savedEnterprise.getSuppliers().add(currentProductSupplier);
		} else if (savedEnterprise == null) {
			LinkedList<ProductSupplier> newSuppliers = new LinkedList<ProductSupplier>();
			// Containing order was not processed yet
			if (currentProductSupplier != null) {
				LOG.debug("Adding product supplier to current enterprise: " + currentProductSupplier);
				// A ProductSupplier was present, so save it in the enteprise
				newSuppliers.add(currentProductSupplier);
			}
			LOG.debug("Adding enterprise to map: " + currentEnterprise);
			// Put the enterprise into the map in any case and initialize the suppliers
			currentEnterprise.setSuppliers(newSuppliers);
			enterprises.put(currentEnterprise.getId(), currentEnterprise);
		}
		// If an enterprise was found but the current product supplier is null, just do nothing 
	}
	
	public Collection<StockItem> getStockItemsFromCSV(String input) {
		CSVParser parser = new CSVParser();
		parser.parse(input);
		
		LinkedList<StockItem> stockItems = new LinkedList<StockItem>();
 		
		if (parser.getModel().getRows().size() > 0) {
			for (Row<String> row : parser.getModel().getRows()) {
				stockItems.add(getStockItemFromRow(row));
			}
		}
		
		return stockItems;
	}
	
	public Collection<Product> getProductsFromCSV(String input) {
		CSVParser parser = new CSVParser();
		parser.parse(input);
		
		LinkedList<Product> products = new LinkedList<Product>();
 		
		if (parser.getModel().getRows().size() > 0) {
			for (Row<String> row : parser.getModel().getRows()) {
				products.add(getProductFromRow(row));
			}
		}
		
		return products;
	}

	public Collection<ProductSupplier> getProductSuppliersFromCSV(String input) {
		CSVParser parser = new CSVParser();
		parser.parse(input);
		
		LinkedList<ProductSupplier> productSuppliers = new LinkedList<ProductSupplier>();
 		
		if (parser.getModel().getRows().size() > 0) {
			for (Row<String> row : parser.getModel().getRows()) {
				productSuppliers.add(getProductSupplierFromRow(row));
			}
		}
		
		return productSuppliers;
	}
	
	public Collection<ProductOrder> getProductOrdersFromCSV(String input) {
		CSVParser parser = new CSVParser();
		parser.parse(input);
		
		int tableRows =  parser.getModel().getRows().size();
		
		HashMap<Long, ProductOrder> productOrders = null;
 		
		if (tableRows > 0) {
			// Make sure to reserve enough initial space for the hash map
			int initialCapacity = (int) (tableRows / 0.75 + 1); 
			productOrders = new HashMap<Long, ProductOrder>(initialCapacity);
			
			for (Row<String> row : parser.getModel().getRows()) {		
				extractProductOrderRow(productOrders, row);
			}
		}
		if (productOrders == null) {
			return new LinkedList<ProductOrder>();
		} else {
			return productOrders.values();
		}
	}

	private void extractProductOrderRow(
			HashMap<Long, ProductOrder> productOrders, Row<String> row) {
		ProductOrder currentOrder = getProductOrderFromRow(row);
		OrderEntry currentOrderEntry = getOrderEntryFromRow(row);
		ProductOrder savedOrder = productOrders.get(currentOrder.getId());
		
		if (savedOrder != null && currentOrderEntry != null) {
			// Just add the order entry if the containing order was already processed
			savedOrder.getOrderEntries().add(currentOrderEntry);
		} else if (savedOrder == null) {
			// Containing order was not processed yet
			if (currentOrderEntry != null) {
				// An OrderEntry was present, so save it in the order
				currentOrder.getOrderEntries().add(currentOrderEntry);
			}
			// Put the order into the map in any case
			productOrders.put(currentOrder.getId(), currentOrder);
		}
		// If an order was found but the current order entry is null, just do nothing 
	}
}
