package org.cocome.tradingsystem.remote.access.parsing;

import de.kit.ipd.java.utils.framework.table.Row;
import de.kit.ipd.java.utils.parsing.csv.CSVParser;
import de.kit.ipd.java.utils.time.TimeUtils;
import org.apache.log4j.Logger;
import org.cocome.tradingsystem.inventory.application.usermanager.CredentialType;
import org.cocome.tradingsystem.inventory.application.usermanager.Role;
import org.cocome.tradingsystem.inventory.application.usermanager.credentials.ICredential;
import org.cocome.tradingsystem.inventory.application.usermanager.credentials.ICredentialFactory;
import org.cocome.tradingsystem.inventory.data.enterprise.IEnterpriseDataFactory;
import org.cocome.tradingsystem.inventory.data.enterprise.IProduct;
import org.cocome.tradingsystem.inventory.data.enterprise.IProductSupplier;
import org.cocome.tradingsystem.inventory.data.enterprise.ITradingEnterprise;
import org.cocome.tradingsystem.inventory.data.plant.IPlant;
import org.cocome.tradingsystem.inventory.data.plant.IPlantDataFactory;
import org.cocome.tradingsystem.inventory.data.store.*;
import org.cocome.tradingsystem.inventory.data.usermanager.ICustomer;
import org.cocome.tradingsystem.inventory.data.usermanager.IUser;
import org.cocome.tradingsystem.inventory.data.usermanager.IUserDataFactory;
import org.cocome.tradingsystem.remote.access.connection.QueryParameterEncoder;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import java.util.*;


@RequestScoped
public class CSVHelper implements IBackendConversionHelper {

    @Inject
    IEnterpriseDataFactory enterpriseFactory;

    @Inject
    IStoreDataFactory storeFactory;

    @Inject
    IPlantDataFactory plantFactory;

    @Inject
    IUserDataFactory userFactory;

    @Inject
    Instance<ICredentialFactory> credFactoryInstance;

    private ICredentialFactory credFactory;

    private static final Logger LOG = Logger.getLogger(CSVHelper.class);

    private String decodeString(String string) {
        return QueryParameterEncoder.decodeQueryString(string);
    }

    private void initCredFactory() {
        if (credFactory == null) {
            credFactory = credFactoryInstance.get();
        }
    }

    private IStore getStoreFromRow(Row<String> row) {
        return getStoreFromRow(row, 0);
    }

    private IStore getStoreFromRow(Row<String> row, int offset) {
        if (offset < 0) offset = 0;

        IStore result = storeFactory.getNewStore();

        String enterpriseName = row.getColumns().get(offset).getValue();
        result.setEnterpriseName(enterpriseName.equals("null") ? null : decodeString(enterpriseName));

        String id = row.getColumns().get(1 + offset).getValue();
        result.setId(id.equals("null") ? Long.MIN_VALUE : Long.parseLong(id));

        String storeName = row.getColumns().get(2 + offset).getValue();
        result.setName(storeName.equals("null") ? null : decodeString(storeName));

        String storeLocation = row.getColumns().get(3 + offset).getValue();
        result.setLocation(storeLocation.equals("null") ? null : decodeString(storeLocation));

        return result;
    }

    private IPlant getPlantFromRow(Row<String> row) {
        return getPlantFromRow(row, 0);
    }

    private IPlant getPlantFromRow(Row<String> row, int offset) {
        if (offset < 0) offset = 0;

        IPlant result = plantFactory.getNewPlant();

        String enterpriseId = row.getColumns().get(offset).getValue();
        result.setEnterpriseId(enterpriseId.equals("null") ? Long.MIN_VALUE : Long.parseLong(enterpriseId));

        String id = row.getColumns().get(1 + offset).getValue();
        result.setId(id.equals("null") ? Long.MIN_VALUE : Long.parseLong(id));

        String plantName = row.getColumns().get(2 + offset).getValue();
        result.setName(plantName.equals("null") ? null : decodeString(plantName));

        String plantLocation = row.getColumns().get(3 + offset).getValue();
        result.setLocation(plantLocation.equals("null") ? null : decodeString(plantLocation));

        return result;
    }

    private IProductSupplier getProductSupplierFromEnterpriseRow(Row<String> row) {
        if (row.getColumns().get(2).getValue().equals("N/A")) {
            return null;
        }

        IProductSupplier result = enterpriseFactory.getNewProductSupplier();
        result.setId(Long.parseLong(row.getColumns().get(2).getValue()));
        result.setName(decodeString(row.getColumns().get(3).getValue()));
        return result;
    }

    private ITradingEnterprise getEnterpriseFromRow(Row<String> row) {
        ITradingEnterprise result = enterpriseFactory.getNewTradingEnterprise();

        result.setId(Long.parseLong(row.getColumns().get(0).getValue()));
        result.setName(decodeString(row.getColumns().get(1).getValue()));

        return result;
    }

    private IStockItem getStockItemFromRow(Row<String> row) {
        IStockItem result = storeFactory.getNewStockItem();

        result.setStoreName(decodeString(row.getColumns().get(1).getValue()));
        result.setStoreLocation(decodeString(row.getColumns().get(2).getValue()));
        result.setProductBarcode(Long.parseLong(row.getColumns().get(3).getValue()));
        result.setId(Long.parseLong(row.getColumns().get(4).getValue()));
        result.setMinStock(Long.parseLong(row.getColumns().get(5).getValue()));
        result.setMaxStock(Long.parseLong(row.getColumns().get(6).getValue()));
        result.setIncomingAmount(Long.parseLong(row.getColumns().get(7).getValue()));
        result.setAmount(Long.parseLong(row.getColumns().get(8).getValue()));
        result.setSalesPrice(Double.parseDouble(row.getColumns().get(9).getValue()));

        return result;
    }

    private IProduct getProductFromRow(Row<String> row) {
        IProduct result = enterpriseFactory.getNewProduct();

        result.setBarcode(Long.parseLong(row.getColumns().get(0).getValue()));
        result.setName(decodeString(row.getColumns().get(1).getValue()));
        result.setPurchasePrice(Double.parseDouble(row.getColumns().get(2).getValue()));

        return result;
    }

    private IProductSupplier getProductSupplierFromRow(Row<String> row) {
        IProductSupplier result = enterpriseFactory.getNewProductSupplier();

        result.setId(Long.parseLong(row.getColumns().get(0).getValue()));
        result.setName(decodeString(row.getColumns().get(1).getValue()));

        return result;
    }

    private IProductOrder getProductOrderFromRow(Row<String> row) {
        IProductOrder result = storeFactory.getNewProductOrder();

        result.setId(Long.parseLong(row.getColumns().get(0).getValue()));
        result.setStoreName(decodeString(row.getColumns().get(2).getValue()));
        result.setStoreLocation(decodeString(row.getColumns().get(3).getValue()));
        result.setDeliveryDate(TimeUtils.convertToDateObject(row.getColumns().get(5).getValue()));
        result.setOrderingDate(TimeUtils.convertToDateObject(row.getColumns().get(6).getValue()));

        return result;
    }

    private IOrderEntry getOrderEntryFromRow(Row<String> row) {
        IOrderEntry result = storeFactory.getNewOrderEntry();

        result.setAmount(Long.parseLong(row.getColumns().get(7).getValue()));
        result.setProductBarcode(Long.parseLong(row.getColumns().get(4).getValue()));

        return result;
    }

    /* (non-Javadoc)
     * @see org.cocome.tradingsystem.remote.access.parsing.IBackendConversionHelper#getStores(java.lang.String)
     */
    @Override
    public Collection<IStore> getStores(String input) {
        CSVParser parser = new CSVParser();
        parser.parse(input);

        LinkedList<IStore> stores = new LinkedList<>();

        if (parser.getModel().getRows().size() > 0) {
            for (Row<String> row : parser.getModel().getRows()) {
                stores.add(getStoreFromRow(row));
            }
        }

        return stores;
    }

    @Override
    public Collection<IPlant> getPlants(String input) {
        CSVParser parser = new CSVParser();
        parser.parse(input);

        LinkedList<IPlant> stores = new LinkedList<>();

        if (parser.getModel().getRows().size() > 0) {
            for (Row<String> row : parser.getModel().getRows()) {
                stores.add(getPlantFromRow(row));
            }
        }

        return stores;
    }

    /* (non-Javadoc)
     * @see org.cocome.tradingsystem.remote.access.parsing.IBackendConversionHelper#getCustomers(java.lang.String)
     */
    @Override
    public Collection<ICustomer> getCustomers(String input) {
        LOG.debug("Parsing customer from input: " + input);
        CSVParser parser = new CSVParser();
        parser.parse(input);

        int tableRows = parser.getModel().getRows().size();

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

        ICustomer currentCustomer = getCustomerFromRow(row);
        IUser currentUser = getUserFromRow(row, 9);
        String currentCreditInfo = decodeString(row.getColumns().get(4).getValue());
        IStore preferredStore = getStoreFromRow(row, 5);
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

    private ICustomer getCustomerFromRow(Row<String> row) {
        ICustomer result = userFactory.getNewCustomer();

        result.setID(Long.parseLong(row.getColumns().get(0).getValue()));
        result.setFirstName(decodeString(row.getColumns().get(1).getValue()));
        result.setLastName(decodeString(row.getColumns().get(2).getValue()));
        result.setMailAddress(decodeString(row.getColumns().get(3).getValue()));

        return result;
    }

    /* (non-Javadoc)
     * @see org.cocome.tradingsystem.remote.access.parsing.IBackendConversionHelper#getUsers(java.lang.String)
     */
    @Override
    public Collection<IUser> getUsers(String input) {
        CSVParser parser = new CSVParser();
        parser.parse(input);


        int tableRows = parser.getModel().getRows().size();

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
            return new LinkedList<>();
        } else {
            return users.values();
        }
    }

    private void extractUserRow(HashMap<String, IUser> users, Row<String> row) {
        LOG.debug("Parsing row: " + row.toString());

        IUser currentUser = getUserFromRow(row);
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
        initCredFactory();
        CredentialType type = CredentialType.valueOf(decodeString(row.getColumns().get(2).getValue().toUpperCase()));
        ICredential cred = credFactory.getCredential(type);
        cred.setCredentialString(decodeString(row.getColumns().get(3).getValue()));
        return cred;
    }

    private Role getRoleFromRow(Row<String> row) {
        return Role.valueOf(decodeString(row.getColumns().get(4).getValue().toUpperCase()));
    }

    private IUser getUserFromRow(Row<String> row) {
        return getUserFromRow(row, 0);
    }

    private IUser getUserFromRow(Row<String> row, int offset) {
        if (offset < 0) offset = 0;

        IUser user = userFactory.getNewUser();
        user.setUsername(decodeString(row.getColumns().get(1 + offset).getValue()));

        return user;
    }

    /* (non-Javadoc)
     * @see org.cocome.tradingsystem.remote.access.parsing.IBackendConversionHelper#getEnterprises(java.lang.String)
     */
    @Override
    public Collection<ITradingEnterprise> getEnterprises(String input) {
        CSVParser parser = new CSVParser();
        parser.parse(input);

        int tableRows = parser.getModel().getRows().size();

        LOG.debug("Parsing enterprise model: " + parser.getModel().toString());

        HashMap<Long, ITradingEnterprise> enterprises = null;

        if (tableRows > 0) {
            // Make sure to reserve enough initial space for the hash map
            int initialCapacity = (int) (tableRows / 0.75 + 1);
            enterprises = new HashMap<>(initialCapacity);

            for (Row<String> row : parser.getModel().getRows()) {
                extractEnterpriseRow(enterprises, row);
            }
        }

        if (enterprises == null) {
            return new LinkedList<>();
        } else {
            return enterprises.values();
        }
    }

    private void extractEnterpriseRow(
            HashMap<Long, ITradingEnterprise> enterprises, Row<String> row) {
        LOG.debug("Parsing row: " + row.toString());

        ITradingEnterprise currentEnterprise = getEnterpriseFromRow(row);
        IProductSupplier currentProductSupplier = getProductSupplierFromEnterpriseRow(row);
        ITradingEnterprise savedEnterprise = enterprises.get(currentEnterprise.getId());

        LOG.debug("Got from row: " + currentEnterprise + "; " + currentProductSupplier);

        if (savedEnterprise != null && currentProductSupplier != null) {
            // Just add the product supplier if the containing enterprise was already processed
            LOG.debug("Adding product supplier to saved enterprise: " + currentProductSupplier);
            savedEnterprise.getSuppliers().add(currentProductSupplier);
        } else if (savedEnterprise == null) {
            LinkedList<IProductSupplier> newSuppliers = new LinkedList<>();
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

    /* (non-Javadoc)
     * @see org.cocome.tradingsystem.remote.access.parsing.IBackendConversionHelper#getStockItems(java.lang.String)
     */
    @Override
    public Collection<IStockItem> getStockItems(String input) {
        CSVParser parser = new CSVParser();
        parser.parse(input);

        LinkedList<IStockItem> stockItems = new LinkedList<>();

        if (parser.getModel().getRows().size() > 0) {
            for (Row<String> row : parser.getModel().getRows()) {
                stockItems.add(getStockItemFromRow(row));
            }
        }

        return stockItems;
    }

    /* (non-Javadoc)
     * @see org.cocome.tradingsystem.remote.access.parsing.IBackendConversionHelper#getProducts(java.lang.String)
     */
    @Override
    public Collection<IProduct> getProducts(String input) {
        CSVParser parser = new CSVParser();
        parser.parse(input);

        LinkedList<IProduct> products = new LinkedList<>();

        if (parser.getModel().getRows().size() > 0) {
            for (Row<String> row : parser.getModel().getRows()) {
                products.add(getProductFromRow(row));
            }
        }

        return products;
    }

    /* (non-Javadoc)
     * @see org.cocome.tradingsystem.remote.access.parsing.IBackendConversionHelper#getProductSuppliers(java.lang.String)
     */
    @Override
    public Collection<IProductSupplier> getProductSuppliers(String input) {
        CSVParser parser = new CSVParser();
        parser.parse(input);

        LinkedList<IProductSupplier> productSuppliers = new LinkedList<>();

        if (parser.getModel().getRows().size() > 0) {
            for (Row<String> row : parser.getModel().getRows()) {
                productSuppliers.add(getProductSupplierFromRow(row));
            }
        }

        return productSuppliers;
    }

    /* (non-Javadoc)
     * @see org.cocome.tradingsystem.remote.access.parsing.IBackendConversionHelper#getProductOrders(java.lang.String)
     */
    @Override
    public Collection<IProductOrder> getProductOrders(String input) {
        CSVParser parser = new CSVParser();
        parser.parse(input);

        int tableRows = parser.getModel().getRows().size();

        HashMap<Long, IProductOrder> productOrders = null;

        if (tableRows > 0) {
            // Make sure to reserve enough initial space for the hash map
            int initialCapacity = (int) (tableRows / 0.75 + 1);
            productOrders = new HashMap<>(initialCapacity);

            for (Row<String> row : parser.getModel().getRows()) {
                extractProductOrderRow(productOrders, row);
            }
        }
        if (productOrders == null) {
            return new LinkedList<>();
        } else {
            return productOrders.values();
        }
    }

    private void extractProductOrderRow(
            HashMap<Long, IProductOrder> productOrders, Row<String> row) {
        IProductOrder currentOrder = getProductOrderFromRow(row);
        IOrderEntry currentOrderEntry = getOrderEntryFromRow(row);
        IProductOrder savedOrder = productOrders.get(currentOrder.getId());

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
