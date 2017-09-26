package org.cocome.tradingsystem.inventory.data.persistence;

import de.kit.ipd.java.utils.time.TimeUtils;
import org.cocome.tradingsystem.inventory.application.usermanager.Role;
import org.cocome.tradingsystem.inventory.application.usermanager.credentials.ICredential;
import org.cocome.tradingsystem.inventory.data.enterprise.ICustomProduct;
import org.cocome.tradingsystem.inventory.data.enterprise.IProduct;
import org.cocome.tradingsystem.inventory.data.enterprise.IProductSupplier;
import org.cocome.tradingsystem.inventory.data.enterprise.ITradingEnterprise;
import org.cocome.tradingsystem.inventory.data.plant.IPlant;
import org.cocome.tradingsystem.inventory.data.plant.productionunit.IProductionUnitClass;
import org.cocome.tradingsystem.inventory.data.plant.productionunit.IProductionUnitOperation;
import org.cocome.tradingsystem.inventory.data.store.IOrderEntry;
import org.cocome.tradingsystem.inventory.data.store.IProductOrder;
import org.cocome.tradingsystem.inventory.data.store.IStockItem;
import org.cocome.tradingsystem.inventory.data.store.IStore;
import org.cocome.tradingsystem.inventory.data.usermanager.ICustomer;
import org.cocome.tradingsystem.inventory.data.usermanager.IUser;
import org.cocome.tradingsystem.util.java.DualElement;
import org.cocome.tradingsystem.util.java.DualIterator;

/**
 * This class handles the conversion of entity classes into query content to
 * pass on to the service adapter. The service adapter requires all data to be
 * passed as Strings in a specific format which is described in its
 * documentation.
 *
 * @author Tobias Pöppke
 * @author Robert Heinrich
 */
class ServiceAdapterEntityConverter {
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
     * @param stockItem the stock item to convert
     * @return a String representation of the stock item
     */
    static String getStockItemContent(IStockItem stockItem) {
        return String.valueOf(stockItem.getStore().getId()) +
                ServiceAdapterHeaders.SEPARATOR +
                stockItem.getProductBarcode() +
                ServiceAdapterHeaders.SEPARATOR +
                stockItem.getMinStock() +
                ServiceAdapterHeaders.SEPARATOR +
                stockItem.getMaxStock() +
                ServiceAdapterHeaders.SEPARATOR +
                stockItem.getIncomingAmount() +
                ServiceAdapterHeaders.SEPARATOR +
                stockItem.getAmount() +
                ServiceAdapterHeaders.SEPARATOR +
                stockItem.getSalesPrice();
    }

    /**
     * Returns a string containing information about the given product order.
     *
     * @param productOrder the product order to convert
     * @return a String representation of the product order
     */
    static String getProductOrderContent(IProductOrder productOrder) {
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

    static String getCreatePlantContent(IPlant entity) {
        return String.valueOf(entity.getEnterpriseId()) +
                ServiceAdapterHeaders.SEPARATOR +
                encodeString(entity.getName()) +
                ServiceAdapterHeaders.SEPARATOR +
                encodeString(entity.getLocation());
    }

    static String getUpdatePlantContent(IPlant entity) {
        return String.valueOf(entity.getEnterpriseId()) +
                ServiceAdapterHeaders.SEPARATOR +
                entity.getId() +
                ServiceAdapterHeaders.SEPARATOR +
                encodeString(entity.getName()) +
                ServiceAdapterHeaders.SEPARATOR +
                encodeString(entity.getLocation());
    }

    /**
     * Returns a string containing all needed information to create a new enterprise.
     *
     * @param enterprise the enterprise to convert
     * @return a String representation of the enterprise to be created
     */
    static String getCreateEnterpriseContent(ITradingEnterprise enterprise) {
        return encodeString(enterprise.getName());
    }

    /**
     * Returns a string containing all necessary information to update an enterprise.
     *
     * @param enterprise the enterprise to convert
     * @return a String representation of the enterprise to be updated
     */
    static String getUpdateEnterpriseContent(ITradingEnterprise enterprise) {
        return String.valueOf(enterprise.getId()) +
                ServiceAdapterHeaders.SEPARATOR +
                encodeString(enterprise.getName());
    }

    /**
     * Returns a string containing all needed information to create a new enterprise.
     *
     * @param store the store to convert
     * @return a String representation of the store to be created
     */
    static String getCreateStoreContent(IStore store) {
        return encodeString(store.getEnterpriseName()) +
                ServiceAdapterHeaders.SEPARATOR +
                encodeString(store.getName()) +
                ServiceAdapterHeaders.SEPARATOR +
                encodeString(store.getLocation());
    }


    /**
     * Returns a string containing all needed information to update a store.
     *
     * @param store the store to convert
     * @return a String representation of the updated store
     */
    static String getUpdateStoreContent(IStore store) {
        return encodeString(store.getEnterpriseName()) +
                ServiceAdapterHeaders.SEPARATOR +
                store.getId() +
                ServiceAdapterHeaders.SEPARATOR +
                encodeString(store.getName()) +
                ServiceAdapterHeaders.SEPARATOR +
                encodeString(store.getLocation());
    }

    /**
     * Returns a string containing all needed information to create a new supplier.
     *
     * @param supplier the supplier to convert
     * @return a String representation of the supplier to be created
     */
    static String getCreateSupplierContent(IProductSupplier supplier) {
        return encodeString(supplier.getName());
    }

    /**
     * Returns a string containing all needed information to update a supplier.
     *
     * @param supplier the supplier to convert
     * @return a String representation of the updated store
     */
    static String getUpdateSupplierContent(IProductSupplier supplier) {
        return String.valueOf(supplier.getId()) +
                ServiceAdapterHeaders.SEPARATOR +
                encodeString(supplier.getName());
    }

    /**
     * Returns a string containing information about the given product.
     *
     * @param product the product to convert
     * @return a String representation of the product
     */
    static String getProductContent(IProduct product) {
        return String.valueOf(product.getBarcode()) +
                ServiceAdapterHeaders.SEPARATOR +
                encodeString(product.getName()) +
                ServiceAdapterHeaders.SEPARATOR +
                product.getPurchasePrice();
    }

    /**
     * Returns a string containing information about the given user.
     *
     * @param user the user to convert
     * @return a String representation of the user
     */
    static String getUserContent(IUser user) {
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
     * @param customer the user to convert
     * @return a String representation of the customer
     */
    static String getCustomerContent(ICustomer customer) {
        StringBuilder content = new StringBuilder();

        content.append(encodeString(customer.getFirstName()));
        content.append(ServiceAdapterHeaders.SEPARATOR);
        content.append(encodeString(customer.getLastName()));
        content.append(ServiceAdapterHeaders.SEPARATOR);
        content.append(encodeString(customer.getMailAddress()));
        content.append(ServiceAdapterHeaders.SEPARATOR);

        appendPrefferedStore(customer, content);

        // Mail address is used as username for customers
        content.append(encodeString(customer.getMailAddress()));

        return content.toString();
    }

    /**
     * Returns a string containing all needed information to update a customer.
     *
     * @param customer the customer to convert
     * @return a String representation of the updated customer
     */
    static String getUpdateCustomerContent(ICustomer customer) {
        StringBuilder content = new StringBuilder();

        content.append(customer.getId());
        content.append(ServiceAdapterHeaders.SEPARATOR);
        content.append(encodeString(customer.getFirstName()));
        content.append(ServiceAdapterHeaders.SEPARATOR);
        content.append(encodeString(customer.getLastName()));
        content.append(ServiceAdapterHeaders.SEPARATOR);
        content.append(encodeString(customer.getMailAddress()));
        content.append(ServiceAdapterHeaders.SEPARATOR);

        appendPrefferedStore(customer, content);

        // Mail address is used as username for customers
        content.append(encodeString(customer.getMailAddress()));

        return content.toString();
    }

    static String getUpdateProductionUnitClassContent(IProductionUnitClass puc) {
        return String.valueOf(puc.getPlantId()) +
                ServiceAdapterHeaders.SEPARATOR +
                puc.getId() +
                ServiceAdapterHeaders.SEPARATOR +
                encodeString(puc.getName());
    }

    static String getCreateProductionUnitClassContent(IProductionUnitClass puc) {
        return String.valueOf(puc.getPlantId()) +
                ServiceAdapterHeaders.SEPARATOR +
                encodeString(puc.getName());
    }

    static String getCreateCustomProductContent(ICustomProduct customProduct) {
        return String.valueOf(customProduct.getBarcode()) +
                ServiceAdapterHeaders.SEPARATOR +
                encodeString(customProduct.getName()) +
                ServiceAdapterHeaders.SEPARATOR +
                customProduct.getPurchasePrice() +
                ServiceAdapterHeaders.SEPARATOR +
                customProduct.getRecipeId();
    }

    static String getUpdateCustomProductContent(ICustomProduct customProduct) {
        return String.valueOf(customProduct.getId()) +
                ServiceAdapterHeaders.SEPARATOR +
                String.valueOf(customProduct.getBarcode()) +
                ServiceAdapterHeaders.SEPARATOR +
                encodeString(customProduct.getName()) +
                ServiceAdapterHeaders.SEPARATOR +
                customProduct.getPurchasePrice() +
                ServiceAdapterHeaders.SEPARATOR +
                customProduct.getRecipeId();
    }

    static String getCreateProductionUnitOperationContent(IProductionUnitOperation operation) {
        return String.valueOf(operation.getOperationId()) +
                ServiceAdapterHeaders.SEPARATOR +
                operation.getProductionUnitClassId();
    }

    static String getUpdateProductionUnitOperationContent(IProductionUnitOperation operation) {
        return String.valueOf(operation.getId()) +
                ServiceAdapterHeaders.SEPARATOR +
                String.valueOf(operation.getOperationId()) +
                ServiceAdapterHeaders.SEPARATOR +
                operation.getProductionUnitClassId();
    }

    private static void appendPrefferedStore(ICustomer customer, StringBuilder content) {
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
    }
}