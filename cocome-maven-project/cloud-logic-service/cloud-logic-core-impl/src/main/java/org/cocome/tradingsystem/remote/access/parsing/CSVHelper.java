package org.cocome.tradingsystem.remote.access.parsing;

import de.kit.ipd.java.utils.framework.table.Column;
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
import org.cocome.tradingsystem.inventory.data.enterprise.parameter.IBooleanCustomProductParameter;
import org.cocome.tradingsystem.inventory.data.enterprise.parameter.ICustomProductParameter;
import org.cocome.tradingsystem.inventory.data.enterprise.parameter.INorminalCustomProductParameter;
import org.cocome.tradingsystem.inventory.data.persistence.ServiceAdapterHeaders;
import org.cocome.tradingsystem.inventory.data.plant.IPlant;
import org.cocome.tradingsystem.inventory.data.plant.IPlantDataFactory;
import org.cocome.tradingsystem.inventory.data.plant.expression.IConditionalExpression;
import org.cocome.tradingsystem.inventory.data.plant.expression.IExpression;
import org.cocome.tradingsystem.inventory.data.plant.parameter.IBooleanPlantOperationParameter;
import org.cocome.tradingsystem.inventory.data.plant.parameter.INorminalPlantOperationParameter;
import org.cocome.tradingsystem.inventory.data.plant.parameter.IPlantOperationParameter;
import org.cocome.tradingsystem.inventory.data.plant.productionunit.IProductionUnit;
import org.cocome.tradingsystem.inventory.data.plant.productionunit.IProductionUnitClass;
import org.cocome.tradingsystem.inventory.data.plant.productionunit.IProductionUnitOperation;
import org.cocome.tradingsystem.inventory.data.plant.recipe.*;
import org.cocome.tradingsystem.inventory.data.store.*;
import org.cocome.tradingsystem.inventory.data.usermanager.ICustomer;
import org.cocome.tradingsystem.inventory.data.usermanager.IUser;
import org.cocome.tradingsystem.inventory.data.usermanager.IUserDataFactory;
import org.cocome.tradingsystem.remote.access.connection.QueryParameterEncoder;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;


@RequestScoped
public class CSVHelper implements IBackendConversionHelper {

    @Inject
    private IEnterpriseDataFactory enterpriseFactory;

    @Inject
    private IStoreDataFactory storeFactory;

    @Inject
    private IPlantDataFactory plantFactory;

    @Inject
    private IUserDataFactory userFactory;

    @Inject
    private Instance<ICredentialFactory> credFactoryInstance;

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

    private IProduct getProductFromRow(Row<String> row) {
        IProduct result = getNewProductInstance(row.getColumns().get(3).getValue());

        result.setBarcode(Long.parseLong(row.getColumns().get(0).getValue()));
        result.setName(decodeString(row.getColumns().get(1).getValue()));
        result.setPurchasePrice(Double.parseDouble(row.getColumns().get(2).getValue()));

        return result;
    }

    private IProduct getNewProductInstance(String value) {
        if (value.contains("CustomProduct")) {
            return enterpriseFactory.getNewCustomProduct();
        }
        if (value.contains("Product")) {
            return enterpriseFactory.getNewProduct();
        }
        throw new IllegalArgumentException("Unknown product type: " + value);
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
        return rowToCollection(input, row -> {
            final IPlant result = enterpriseFactory.getNewPlant();
            result.setEnterpriseId(fetchLong(row.getColumns().get(0)));
            result.setId(fetchLong(row.getColumns().get(1)));
            result.setName(fetchString(row.getColumns().get(2)));
            result.setLocation(fetchString(row.getColumns().get(3)));

            return result;
        });
    }

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
        ICustomer savedCustomer = customers.get(currentCustomer.getId());

        LOG.debug("Got from row: " + currentCustomer + "; " + currentUser);

        currentCustomer.setUser(currentUser);

        if (savedCustomer == null) {
            savedCustomer = currentCustomer;
            customers.put(currentCustomer.getId(), currentCustomer);
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

        result.setId(Long.parseLong(row.getColumns().get(0).getValue()));
        result.setFirstName(decodeString(row.getColumns().get(1).getValue()));
        result.setLastName(decodeString(row.getColumns().get(2).getValue()));
        result.setMailAddress(decodeString(row.getColumns().get(3).getValue()));

        return result;
    }

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

    @Override
    public Collection<IStockItem> getStockItems(String input) {
        return rowToCollection(input, row -> processStockItemRow(row, 0));
    }

    private IStockItem processStockItemRow(Row<String> row, int offset) {
        final IStockItem result = storeFactory.getNewStockItem();

        result.setId(fetchLong(row.getColumns().get(offset)));
        result.setStoreId(fetchLong(row.getColumns().get(1 + offset)));
        result.setProductBarcode(fetchLong(row.getColumns().get(2 + offset)));
        result.setMinStock(fetchLong(row.getColumns().get(3 + offset)));
        result.setMaxStock(fetchLong(row.getColumns().get(4 + offset)));
        result.setIncomingAmount(fetchLong(row.getColumns().get(5 + offset)));
        result.setAmount(fetchLong(row.getColumns().get(6 + offset)));
        result.setSalesPrice(fetchDouble(row.getColumns().get(7 + offset)));

        return result;
    }

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

    @Override
    public Collection<IProductionUnit> getProductionUnit(String unit) {
        return rowToCollection(unit, row -> {
            final IProductionUnit result = plantFactory.getNewProductionUnit();

            result.setId(fetchLong(row.getColumns().get(0)));
            result.setLocation(fetchString(row.getColumns().get(1)));
            result.setInterfaceUrl(fetchString(row.getColumns().get(2)));
            result.setDouble(fetchBoolean(row.getColumns().get(3)));
            result.setPlantId(fetchLong(row.getColumns().get(4)));
            result.setProductionUnitClassId(fetchLong(row.getColumns().get(5)));

            return result;
        });
    }

    @Override
    public Collection<IProductionUnitClass> getProductionUnitClasses(String input) {
        return rowToCollection(input, row -> {
            final IProductionUnitClass result = plantFactory.getNewProductionUnitClass();

            result.setPlantId(fetchLong(row.getColumns().get(0)));
            result.setId(fetchLong(row.getColumns().get(1)));
            result.setName(fetchString(row.getColumns().get(2)));

            return result;
        });
    }

    @Override
    public Collection<IProductionUnitOperation> getProductionUnitOperations(String input) {
        return rowToCollection(input, row -> processPlantUnitOperationRow(row, 0));
    }

    private IProductionUnitOperation processPlantUnitOperationRow(Row<String> row, int offset) {
        final IProductionUnitOperation result = plantFactory.getNewProductionUnitOperation();

        result.setId(fetchLong(row.getColumns().get(offset)));
        result.setName(fetchString(row.getColumns().get(1 + offset)));
        result.setOperationId(fetchString(row.getColumns().get(2 + offset)));
        result.setExecutionDurationInMillis(fetchLong(row.getColumns().get(3 + offset)));
        result.setProductionUnitClassId(fetchLong(row.getColumns().get(4 + offset)));

        return result;
    }

    @Override
    public Collection<IEntryPoint> getEntryPoints(String entryPoint) {
        return rowToCollection(entryPoint, row -> {
            final IEntryPoint result = enterpriseFactory.getNewEntryPoint();

            result.setId(fetchLong(row.getColumns().get(0)));
            result.setName(fetchString(row.getColumns().get(1)));

            return result;
        });
    }

    @Override
    public Collection<IConditionalExpression> getConditionalExpressions(String conditionalExpression) {
        return rowToCollection(conditionalExpression, row -> processConditionalExpressionRow(row, 0));
    }

    private IConditionalExpression processConditionalExpressionRow(Row<String> row, int offset) {
        final IConditionalExpression result = plantFactory.getNewConditionalExpression();

        result.setParameterId(fetchLong(row.getColumns().get(offset)));
        result.setId(fetchLong(row.getColumns().get(1 + offset)));
        result.setParameterValue(fetchString(row.getColumns().get(2 + offset)));
        result.setOnTrueExpressionIds(fetchIds(row.getColumns().get(3 + offset)));
        result.setOnFalseExpressionIds(fetchIds(row.getColumns().get(4 + offset)));

        return result;
    }

    @Override
    public Collection<IBooleanCustomProductParameter> getBooleanCustomProductParameter(String param) {
        return rowToCollection(param, row -> processBooleanCustomProductParameterRow(row, 0));
    }

    @Override
    public Collection<INorminalCustomProductParameter> getNorminalCustomProductParameter(String param) {
        return rowToCollection(param, row -> processNorminalCustomProductParameterRow(row, 0));
    }

    @Override
    public Collection<ICustomProductParameter> getCustomProductParameter(String param) {
        return rowToCollection(param, row -> {
            final String typeName = row.getColumns().get(0).getValue();
            final int offset = Integer.parseInt(row.getColumns().get(1).getValue());
            if (typeName.contains("BooleanCustomProductParameter")) {
                return processBooleanCustomProductParameterRow(row, offset);
            } else if (typeName.contains("NorminalCustomProductParameter")) {
                return processNorminalCustomProductParameterRow(row, offset);
            }
            throw new IllegalArgumentException("Unsupported type: " + typeName);
        });
    }

    @Override
    public Collection<IBooleanPlantOperationParameter> getBooleanPlantOperationParameter(String param) {
        return rowToCollection(param, row -> processBooleanPlantOperationParameterRow(row, 0));
    }

    @Override
    public Collection<INorminalPlantOperationParameter> getNorminalPlantOperationParameter(String param) {
        return rowToCollection(param, row -> processNorminalPlantOperationParameterRow(row, 0));
    }

    @Override
    public Collection<IPlantOperationParameter> getPlantOperationParameters(String param) {
        return rowToCollection(param, row -> {
            final String typeName = row.getColumns().get(0).getValue();
            final int offset = Integer.parseInt(row.getColumns().get(1).getValue());
            if (typeName.contains("BooleanPlantOperationParameter")) {
                return processBooleanPlantOperationParameterRow(row, offset);
            } else if (typeName.contains("NorminalPlantOperationParameter")) {
                return processNorminalPlantOperationParameterRow(row, offset);
            }
            throw new IllegalArgumentException("Unsupported type: " + typeName);
        });
    }

    @Override
    public Collection<IEntryPointInteraction> getEntryPointInteraction(String entryPointInteraction) {
        return rowToCollection(entryPointInteraction, row -> {
            final IEntryPointInteraction result = plantFactory.getNewEntryPointInteraction();

            result.setId(fetchLong(row.getColumns().get(0)));
            result.setToId(fetchLong(row.getColumns().get(1)));
            result.setFromId(fetchLong(row.getColumns().get(2)));

            return result;
        });
    }

    @Override
    public Collection<IParameterInteraction> getParameterInteraction(String parameterInteraction) {
        return rowToCollection(parameterInteraction, row -> {
            final IParameterInteraction result = plantFactory.getNewParameterInteraction();

            result.setId(fetchLong(row.getColumns().get(0)));
            result.setToId(fetchLong(row.getColumns().get(1)));
            result.setFromId(fetchLong(row.getColumns().get(2)));

            return result;
        });
    }

    @Override
    public Collection<IRecipe> getRecipe(String recipe) {
        return rowToCollection(recipe, row -> {
            final IRecipe result = plantFactory.getNewRecipe();

            result.setId(fetchLong(row.getColumns().get(0)));
            result.setCustomProductId(fetchLong(row.getColumns().get(1)));
            result.setOperationIds(fetchIds(row.getColumns().get(2)));
            result.setEntryPointInteractionIds(fetchIds(row.getColumns().get(3)));
            result.setParameterInteractionIds(fetchIds(row.getColumns().get(4)));
            result.setName(fetchString(row.getColumns().get(5)));
            result.setInputEntryPointIds(fetchIds(row.getColumns().get(6)));
            result.setOutputEntryPointIds(fetchIds(row.getColumns().get(7)));

            return result;
        });
    }

    @Override
    public Collection<IExpression> getExpressions(String expression) {
        return rowToCollection(expression, row -> {
            final String typeName = row.getColumns().get(0).getValue();
            final int offset = Integer.parseInt(row.getColumns().get(1).getValue());
            if (typeName.contains("ProductionUnitOperation")) {
                return processPlantUnitOperationRow(row, offset);
            } else if (typeName.contains("ConditionalExpression")) {
                return processConditionalExpressionRow(row, offset);
            }
            throw new IllegalArgumentException("Unsupported type: " + typeName);
        });
    }

    @Override
    public Collection<IPlantOperationOrder> getPlantOperationOrder(String order) {
        return rowToCollection(order, row -> {
            final IPlantOperationOrder result = plantFactory.getNewPlantOperationOrder();

            result.setId(fetchLong(row.getColumns().get(0)));
            result.setOrderingDate(fetchDate(row.getColumns().get(1)));
            result.setDeliveryDate(fetchDate(row.getColumns().get(2)));
            result.setEnterpriseId(fetchLong(row.getColumns().get(3)));

            return result;
        });
    }

    @Override
    public Collection<IPlantOperationOrderEntry> getPlantOperationOrderEntry(String orderEntry) {
        return rowToCollection(orderEntry, row -> {
            final IPlantOperationOrderEntry result = plantFactory.getNewPlantOperationOrderEntry();

            result.setId(fetchLong(row.getColumns().get(0)));
            result.setAmount(fetchLong(row.getColumns().get(1)));

            return result;
        });
    }

    @Override
    public Collection<IPlantOperationParameterValue> getPlantOperationParameterValue(String parameterValue) {
        return rowToCollection(parameterValue, row -> {
            final IPlantOperationParameterValue result = plantFactory.getNewPlantOperationParameterValue();

            result.setId(fetchLong(row.getColumns().get(0)));
            result.setValue(fetchString(row.getColumns().get(1)));
            result.setParameterId(fetchLong(row.getColumns().get(2)));

            return result;
        });
    }

    @Override
    public Collection<IItem> getItem(String itemId) {
        return rowToCollection(itemId, row -> {
            final String typeName = row.getColumns().get(0).getValue();
            final int offset = Integer.parseInt(row.getColumns().get(1).getValue());
            if (typeName.contains("StockItem")) {
                return processStockItemRow(row, offset);
            } else if (typeName.contains("OnDemandItem")) {
                return processOnDemandItemRow(row, offset);
            }
            throw new IllegalArgumentException("Unsupported type: " + typeName);
        });
    }

    @Override
    public Collection<IOnDemandItem> getOnDemandItem(String onDemandItemId) {
        return rowToCollection(onDemandItemId, row -> processOnDemandItemRow(row, 0));
    }

    private IOnDemandItem processOnDemandItemRow(Row<String> row, int offset) {
        final IOnDemandItem result = storeFactory.getNewOnDemandItem();

        result.setId(fetchLong(row.getColumns().get(offset)));
        result.setStoreId(fetchLong(row.getColumns().get(1 + offset)));
        result.setProductBarcode(fetchLong(row.getColumns().get(2 + offset)));
        result.setSalesPrice(fetchDouble(row.getColumns().get(3 + offset)));

        return result;
    }

    @Override
    public Collection<IPlantOperation> getPlantOperation(String plantOperation) {
        return rowToCollection(plantOperation, row -> {
            final IPlantOperation result = plantFactory.getNewPlantOperation();

            result.setId(fetchLong(row.getColumns().get(0)));
            result.setPlantId(fetchLong(row.getColumns().get(1)));
            result.setExpressionIds(fetchIds(row.getColumns().get(2)));
            result.setName(fetchString(row.getColumns().get(3)));
            result.setInputEntryPointIds(fetchIds(row.getColumns().get(4)));
            result.setOutputEntryPointIds(fetchIds(row.getColumns().get(5)));

            return result;
        });
    }

    private INorminalCustomProductParameter processNorminalCustomProductParameterRow(Row<String> row, int offset) {
        final INorminalCustomProductParameter result = enterpriseFactory.getNewNorminalCustomProductParameter();
        result.setCustomProductId(Long.parseLong(row.getColumns().get(offset).getValue()));
        result.setId(Long.parseLong(row.getColumns().get(1 + offset).getValue()));
        result.setName(row.getColumns().get(2 + offset).getValue());
        result.setCategory(row.getColumns().get(3 + offset).getValue());
        result.setOptions(fetchStringSet(row.getColumns().get(4 + offset)));

        return result;
    }

    private IBooleanCustomProductParameter processBooleanCustomProductParameterRow(Row<String> row, int offset) {
        final IBooleanCustomProductParameter result = enterpriseFactory.getNewBooleanCustomProductParameter();
        result.setCustomProductId(Long.parseLong(row.getColumns().get(offset).getValue()));
        result.setId(Long.parseLong(row.getColumns().get(1 + offset).getValue()));
        result.setName(row.getColumns().get(2 + offset).getValue());
        result.setCategory(row.getColumns().get(3 + offset).getValue());

        return result;
    }

    private INorminalPlantOperationParameter processNorminalPlantOperationParameterRow(Row<String> row, int offset) {
        final INorminalPlantOperationParameter result = plantFactory.getNewNorminalPlantOperationParameter();
        result.setId(Long.parseLong(row.getColumns().get(1 + offset).getValue()));
        result.setName(row.getColumns().get(2 + offset).getValue());
        result.setCategory(row.getColumns().get(3 + offset).getValue());
        result.setOptions(fetchStringSet(row.getColumns().get(4 + offset)));

        return result;
    }

    private IBooleanPlantOperationParameter processBooleanPlantOperationParameterRow(Row<String> row, int offset) {
        final IBooleanPlantOperationParameter result = plantFactory.getNewBooleanPlantOperationParameter();
        result.setId(Long.parseLong(row.getColumns().get(1 + offset).getValue()));
        result.setName(row.getColumns().get(2 + offset).getValue());
        result.setCategory(row.getColumns().get(3 + offset).getValue());

        return result;
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

    private <T> Collection<T> rowToCollection(String input, Function<Row<String>, T> rowConverter) {
        CSVParser parser = new CSVParser();
        parser.parse(input);

        return parser.getModel().getRows().stream()
                .map(rowConverter)
                .collect(Collectors.toList());
    }

    private Date fetchDate(Column<String> column) {
        return TimeUtils.convertToDateObject(column.getValue());
    }

    private long fetchLong(Column<String> column) {
        return fetchColVal(
                column,
                Long::parseLong,
                Long.MIN_VALUE);
    }

    private double fetchDouble(Column<String> column) {
        return fetchColVal(
                column,
                Double::parseDouble,
                Double.MIN_VALUE);
    }

    private Set<String> fetchStringSet(Column<String> column) {
        return fetchColVal(
                column,
                str -> Arrays.stream(str.split(ServiceAdapterHeaders.SET_SEPARATOR))
                        .collect(Collectors.toSet()),
                Collections.emptySet());
    }

    private List<Long> fetchIds(Column<String> column) {
        return fetchColVal(
                column,
                str -> Arrays.stream(str.split(ServiceAdapterHeaders.SET_SEPARATOR))
                        .map(Long::valueOf).collect(Collectors.toList()),
                Collections.emptyList());
    }

    private String fetchString(Column<String> column) {
        return fetchColVal(
                column,
                this::decodeString,
                null);
    }

    private boolean fetchBoolean(Column<String> stringColumn) {
        return Boolean.valueOf(stringColumn.getValue());
    }

    private <T> T fetchColVal(Column<String> column, Function<String, T> columnValueConverter,
                              T defaultVal) {
        String strValue = column.getValue();
        return strValue.equals("null") ? defaultVal : columnValueConverter.apply(strValue);
    }

}
