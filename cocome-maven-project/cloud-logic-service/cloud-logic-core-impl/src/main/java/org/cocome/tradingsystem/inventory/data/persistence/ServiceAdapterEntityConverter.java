package org.cocome.tradingsystem.inventory.data.persistence;

import com.fasterxml.jackson.core.JsonProcessingException;
import de.kit.ipd.java.utils.time.TimeUtils;
import org.apache.log4j.Logger;
import org.cocome.tradingsystem.inventory.application.plant.expression.MarkupInfo;
import org.cocome.tradingsystem.inventory.application.usermanager.Role;
import org.cocome.tradingsystem.inventory.application.usermanager.credentials.ICredential;
import org.cocome.tradingsystem.inventory.data.INameable;
import org.cocome.tradingsystem.inventory.data.enterprise.IProduct;
import org.cocome.tradingsystem.inventory.data.enterprise.IProductSupplier;
import org.cocome.tradingsystem.inventory.data.enterprise.ITradingEnterprise;
import org.cocome.tradingsystem.inventory.data.plant.IPlant;
import org.cocome.tradingsystem.inventory.data.plant.parameter.IBooleanParameter;
import org.cocome.tradingsystem.inventory.data.plant.parameter.INominalParameter;
import org.cocome.tradingsystem.inventory.data.plant.productionunit.IProductionUnit;
import org.cocome.tradingsystem.inventory.data.plant.productionunit.IProductionUnitClass;
import org.cocome.tradingsystem.inventory.data.plant.productionunit.IProductionUnitOperation;
import org.cocome.tradingsystem.inventory.data.plant.recipe.*;
import org.cocome.tradingsystem.inventory.data.store.*;
import org.cocome.tradingsystem.inventory.data.usermanager.ICustomer;
import org.cocome.tradingsystem.inventory.data.usermanager.IUser;
import org.cocome.tradingsystem.inventory.parser.plant.MarkupParser;
import org.cocome.tradingsystem.util.java.DualElement;
import org.cocome.tradingsystem.util.java.DualIterator;

import java.util.Base64;
import java.util.Collection;
import java.util.stream.Collectors;

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

    private static final Logger LOG = Logger.getLogger(ServiceAdapterEntityConverter.class);

    private static String encodeString(String string) {
        // If the string is encoded there are problems with the
        // queries because of the way the service adapter handles
        // them. Leaving this here for future consideration.
        return string;
        // return QueryParameterEncoder.encodeQueryString(string);
    }

    private static final MarkupParser MARKUP_PARSER = new MarkupParser();

    /**
     * Returns a string containing information about the given stock item.
     *
     * @param stockItem the stock item to convert
     * @return a String representation of the stock item
     */
    static String getCreateStockItemContent(IStockItem stockItem) {
        return String.valueOf(stockItem.getStoreId()) +
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
     * Returns a string containing information about the given stock item.
     *
     * @param stockItem the stock item to convert
     * @return a String representation of the stock item
     */
    static String getUpdateStockItemContent(IStockItem stockItem) {
        return String.valueOf(stockItem.getId()) +
                ServiceAdapterHeaders.SEPARATOR +
                String.valueOf(stockItem.getStoreId()) +
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
     * Returns a string containing information about the given on demand item.
     *
     * @param stockItem the stock item to convert
     * @return a String representation of the stock item
     */
    static String getCreateOnDemandItemContent(IOnDemandItem stockItem) {
        return String.valueOf(stockItem.getStoreId()) +
                ServiceAdapterHeaders.SEPARATOR +
                stockItem.getProductBarcode() +
                ServiceAdapterHeaders.SEPARATOR +
                stockItem.getSalesPrice();
    }


    /**
     * Returns a string containing information about the given on demand item.
     *
     * @param stockItem the stock item to convert
     * @return a String representation of the stock item
     */
    static String getUpdateOnDemandItemContent(IOnDemandItem stockItem) {
        return String.valueOf(stockItem.getId()) +
                ServiceAdapterHeaders.SEPARATOR +
                String.valueOf(stockItem.getStoreId()) +
                ServiceAdapterHeaders.SEPARATOR +
                stockItem.getProductBarcode() +
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
            content.append(TimeUtils.convertNullableToStringDate(productOrder.getDeliveryDate()));
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
        //TODO: WARNING, it is pure coincidence that the implementations of
        // IProduct (Product and CustomProduct) have the same class and package names as
        // those classes in the service-adapter subsystem
        return String.valueOf(product.getBarcode()) +
                ServiceAdapterHeaders.SEPARATOR +
                encodeString(product.getName()) +
                ServiceAdapterHeaders.SEPARATOR +
                product.getPurchasePrice() +
                ServiceAdapterHeaders.SEPARATOR +
                product.getClass().getName();
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

    static String getCreateProductionUnitOperationContent(IProductionUnitOperation operation) {
        return String.valueOf(operation.getName()) +
                ServiceAdapterHeaders.SEPARATOR +
                String.valueOf(operation.getOperationId()) +
                ServiceAdapterHeaders.SEPARATOR +
                String.valueOf(operation.setExecutionDurationInMillis()) +
                ServiceAdapterHeaders.SEPARATOR +
                operation.getProductionUnitClassId();
    }

    static String getUpdateProductionUnitOperationContent(IProductionUnitOperation operation) {
        return String.valueOf(operation.getId()) +
                ServiceAdapterHeaders.SEPARATOR +
                String.valueOf(operation.getName()) +
                ServiceAdapterHeaders.SEPARATOR +
                String.valueOf(operation.getOperationId()) +
                ServiceAdapterHeaders.SEPARATOR +
                String.valueOf(operation.setExecutionDurationInMillis()) +
                ServiceAdapterHeaders.SEPARATOR +
                operation.getProductionUnitClassId();
    }


    static String getCreateProductionUnitContent(IProductionUnit unit) {
        return String.valueOf(unit.getLocation()) +
                ServiceAdapterHeaders.SEPARATOR +
                String.valueOf(unit.getInterfaceUrl()) +
                ServiceAdapterHeaders.SEPARATOR +
                String.valueOf(unit.isDouble()) +
                ServiceAdapterHeaders.SEPARATOR +
                unit.getPlantId() +
                ServiceAdapterHeaders.SEPARATOR +
                unit.getProductionUnitClassId();
    }

    static String getUpdateProductionUnitContent(IProductionUnit unit) {
        return String.valueOf(unit.getId()) +
                ServiceAdapterHeaders.SEPARATOR +
                String.valueOf(unit.getLocation()) +
                ServiceAdapterHeaders.SEPARATOR +
                String.valueOf(unit.getInterfaceUrl()) +
                ServiceAdapterHeaders.SEPARATOR +
                String.valueOf(unit.isDouble()) +
                ServiceAdapterHeaders.SEPARATOR +
                unit.getPlantId() +
                ServiceAdapterHeaders.SEPARATOR +
                unit.getProductionUnitClassId();
    }

    static String getCreateEntryPointContent(IEntryPoint entryPoint) {
        return entryPoint.getName() +
                ServiceAdapterHeaders.SEPARATOR +
                entryPoint.getOperationId() +
                ServiceAdapterHeaders.SEPARATOR +
                entryPoint.getDirection();
    }

    static String getUpdateEntryPointContent(IEntryPoint entryPoint) {
        return String.valueOf(entryPoint.getId()) +
                ServiceAdapterHeaders.SEPARATOR +
                entryPoint.getName() +
                ServiceAdapterHeaders.SEPARATOR +
                entryPoint.getOperationId() +
                ServiceAdapterHeaders.SEPARATOR +
                entryPoint.getDirection();
    }

    static String getCreateBooleanParameterContent(IBooleanParameter param) {
        return String.valueOf(param.getOperationId()) +
                ServiceAdapterHeaders.SEPARATOR +
                String.valueOf(param.getName()) +
                ServiceAdapterHeaders.SEPARATOR +
                encodeString(param.getCategory());
    }

    static String getUpdateBooleanParameterContent(IBooleanParameter param) {
        return String.valueOf(param.getOperationId()) +
                ServiceAdapterHeaders.SEPARATOR +
                String.valueOf(param.getId()) +
                ServiceAdapterHeaders.SEPARATOR +
                String.valueOf(param.getName()) +
                ServiceAdapterHeaders.SEPARATOR +
                encodeString(param.getCategory());
    }

    static String getCreateNominalParameterContent(INominalParameter param) {
        return String.valueOf(param.getOperationId()) +
                ServiceAdapterHeaders.SEPARATOR +
                encodeString(String.valueOf(param.getName())) +
                ServiceAdapterHeaders.SEPARATOR +
                encodeString(param.getCategory()) +
                ServiceAdapterHeaders.SEPARATOR +
                joinValues(param.getOptions());
    }

    static String getUpdateNominalParameterContent(INominalParameter param) {
        return String.valueOf(param.getOperationId()) +
                ServiceAdapterHeaders.SEPARATOR +
                String.valueOf(param.getId()) +
                ServiceAdapterHeaders.SEPARATOR +
                encodeString(String.valueOf(param.getName())) +
                ServiceAdapterHeaders.SEPARATOR +
                encodeString(param.getCategory()) +
                ServiceAdapterHeaders.SEPARATOR +
                encodeString(joinValues(param.getOptions()));
    }

    static String getCreatePlantOperationContent(IPlantOperation operation) {
        return String.valueOf(operation.getPlantId()) +
                ServiceAdapterHeaders.SEPARATOR +
                toBase64String(operation.getMarkup()) +
                ServiceAdapterHeaders.SEPARATOR +
                operation.getName();
    }

    private static String toBase64String(MarkupInfo markup) {
        try {
            final String markupString = MARKUP_PARSER.toString(markup);
            return new String(Base64.getEncoder().encode(markupString.getBytes()));
        } catch (JsonProcessingException e) {
            LOG.error("Unable to convert markup to string: " + e.getMessage(), e);
            throw new IllegalArgumentException(e);
        }
    }

    static String getUpdatePlantOperationContent(IPlantOperation operation) {
        return String.valueOf(operation.getId()) +
                ServiceAdapterHeaders.SEPARATOR +
                String.valueOf(operation.getPlantId()) +
                ServiceAdapterHeaders.SEPARATOR +
                toBase64String(operation.getMarkup()) +
                ServiceAdapterHeaders.SEPARATOR +
                operation.getName();
    }

    static <T extends INameable>
    String getCreateInteractionContent(IInteractionEntity<T> interaction) {
        return String.valueOf(interaction.getToId()) +
                ServiceAdapterHeaders.SEPARATOR +
                String.valueOf(interaction.getFromId()) +
                ServiceAdapterHeaders.SEPARATOR +
                String.valueOf(interaction.getRecipeId());
    }

    static <T extends INameable>
    String getUpdateInteractionContent(IInteractionEntity<T> interaction) {
        return String.valueOf(interaction.getId()) +
                ServiceAdapterHeaders.SEPARATOR +
                String.valueOf(interaction.getToId()) +
                ServiceAdapterHeaders.SEPARATOR +
                String.valueOf(interaction.getFromId()) +
                ServiceAdapterHeaders.SEPARATOR +
                String.valueOf(interaction.getRecipeId());
    }

    static String getCreateRecipeNodeContent(IRecipeNode recipeNode) {
        return String.valueOf(recipeNode.getRecipeId()) +
                ServiceAdapterHeaders.SEPARATOR +
                String.valueOf(recipeNode.getOperationId());
    }

    static String getUpdateRecipeNodeContent(IRecipeNode recipeNode) {
        return String.valueOf(recipeNode.getId()) +
                ServiceAdapterHeaders.SEPARATOR +
                String.valueOf(recipeNode.getRecipeId()) +
                ServiceAdapterHeaders.SEPARATOR +
                String.valueOf(recipeNode.getOperationId());
    }

    static String getCreateRecipeContent(IRecipe recipe) {
        return String.valueOf(recipe.getCustomProductBarcode()) +
                ServiceAdapterHeaders.SEPARATOR +
                recipe.getName() +
                ServiceAdapterHeaders.SEPARATOR +
                recipe.getEnterpriseId();
    }

    static String getUpdateRecipeContent(IRecipe recipe) {
        return String.valueOf(recipe.getId()) +
                ServiceAdapterHeaders.SEPARATOR +
                String.valueOf(recipe.getCustomProductBarcode()) +
                ServiceAdapterHeaders.SEPARATOR +
                recipe.getName() +
                ServiceAdapterHeaders.SEPARATOR +
                recipe.getEnterpriseId();
    }

    static String getCreatePlantOperationOrderContent(IPlantOperationOrder order) {
        return TimeUtils.convertNullableToStringDate(order.getDeliveryDate()) +
                ServiceAdapterHeaders.SEPARATOR +
                TimeUtils.convertToStringDate(order.getOrderingDate()) +
                ServiceAdapterHeaders.SEPARATOR +
                String.valueOf(order.isFinished()) +
                ServiceAdapterHeaders.SEPARATOR +
                String.valueOf(order.getEnterpriseId()) +
                ServiceAdapterHeaders.SEPARATOR +
                String.valueOf(order.getPlantId());
    }

    static String getUpdatePlantOperationOrderContent(IPlantOperationOrder order) {
        return String.valueOf(order.getId()) +
                ServiceAdapterHeaders.SEPARATOR +
                TimeUtils.convertNullableToStringDate(order.getDeliveryDate()) +
                ServiceAdapterHeaders.SEPARATOR +
                TimeUtils.convertToStringDate(order.getOrderingDate()) +
                ServiceAdapterHeaders.SEPARATOR +
                String.valueOf(order.isFinished()) +
                ServiceAdapterHeaders.SEPARATOR +
                String.valueOf(order.getEnterpriseId()) +
                ServiceAdapterHeaders.SEPARATOR +
                String.valueOf(order.getPlantId());
    }

    static String getCreateProductionOrderContent(IProductionOrder order) {
        return TimeUtils.convertNullableToStringDate(order.getDeliveryDate()) +
                ServiceAdapterHeaders.SEPARATOR +
                TimeUtils.convertToStringDate(order.getOrderingDate()) +
                ServiceAdapterHeaders.SEPARATOR +
                String.valueOf(order.isFinished()) +
                ServiceAdapterHeaders.SEPARATOR +
                String.valueOf(order.getEnterpriseId()) +
                ServiceAdapterHeaders.SEPARATOR +
                String.valueOf(order.getStoreId());
    }

    static String getUpdateProductionOrderContent(IProductionOrder order) {
        return String.valueOf(order.getId()) +
                ServiceAdapterHeaders.SEPARATOR +
                TimeUtils.convertNullableToStringDate(order.getDeliveryDate()) +
                ServiceAdapterHeaders.SEPARATOR +
                TimeUtils.convertToStringDate(order.getOrderingDate()) +
                ServiceAdapterHeaders.SEPARATOR +
                String.valueOf(order.isFinished()) +
                ServiceAdapterHeaders.SEPARATOR +
                String.valueOf(order.getEnterpriseId()) +
                ServiceAdapterHeaders.SEPARATOR +
                String.valueOf(order.getStoreId());
    }

    static String getCreatePlantOperationOrderEntryContent(IPlantOperationOrderEntry orderEntry) {
        return String.valueOf(orderEntry.getAmount()) +
                ServiceAdapterHeaders.SEPARATOR +
                String.valueOf(orderEntry.isFinished()) +
                ServiceAdapterHeaders.SEPARATOR +
                String.valueOf(orderEntry.getOperation().getId()) +
                ServiceAdapterHeaders.SEPARATOR +
                String.valueOf(orderEntry.getOrderId());
    }

    static String getUpdatePlantOperationOrderEntryContent(IPlantOperationOrderEntry orderEntry) {
        return String.valueOf(orderEntry.getId()) +
                ServiceAdapterHeaders.SEPARATOR +
                String.valueOf(orderEntry.getAmount()) +
                ServiceAdapterHeaders.SEPARATOR +
                String.valueOf(orderEntry.isFinished()) +
                ServiceAdapterHeaders.SEPARATOR +
                String.valueOf(orderEntry.getOperation().getId()) +
                ServiceAdapterHeaders.SEPARATOR +
                String.valueOf(orderEntry.getOrderId());
    }

    static String getCreateParameterValueContent(IParameterValue value) {
        return String.valueOf(value.getValue()) +
                ServiceAdapterHeaders.SEPARATOR +
                String.valueOf(value.getParameterId()) +
                ServiceAdapterHeaders.SEPARATOR +
                String.valueOf(value.getOrderEntryId());
    }

    static String getUpdateParameterValueContent(IParameterValue value) {
        return String.valueOf(value.getId()) +
                ServiceAdapterHeaders.SEPARATOR +
                String.valueOf(value.getValue()) +
                ServiceAdapterHeaders.SEPARATOR +
                String.valueOf(value.getParameterId()) +
                ServiceAdapterHeaders.SEPARATOR +
                String.valueOf(value.getId()) +
                ServiceAdapterHeaders.SEPARATOR +
                String.valueOf(value.getOrderEntryId());
    }

    static String getCreateProductionOrderEntryContent(IProductionOrderEntry orderEntry) {
        return String.valueOf(orderEntry.getAmount()) +
                ServiceAdapterHeaders.SEPARATOR +
                String.valueOf(orderEntry.isFinished()) +
                ServiceAdapterHeaders.SEPARATOR +
                String.valueOf(orderEntry.getOperation().getId()) +
                ServiceAdapterHeaders.SEPARATOR +
                String.valueOf(orderEntry.getOrderId());
    }

    static String getUpdateProductionOrderEntryContent(IProductionOrderEntry orderEntry) {
        return String.valueOf(orderEntry.getId()) +
                ServiceAdapterHeaders.SEPARATOR +
                String.valueOf(orderEntry.getAmount()) +
                ServiceAdapterHeaders.SEPARATOR +
                String.valueOf(orderEntry.isFinished()) +
                ServiceAdapterHeaders.SEPARATOR +
                String.valueOf(orderEntry.getOperation().getId()) +
                ServiceAdapterHeaders.SEPARATOR +
                String.valueOf(orderEntry.getOrderId());
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

    /**
     * @param collection any collection
     * @return a comma-separated textual representation of the given collection.
     */
    private static <T> String joinValues(final Collection<T> collection) {
        if (collection.isEmpty()) {
            return ServiceAdapterHeaders.NULL_VALUE;
        }
        return collection.stream()
                .map(String::valueOf)
                .collect(Collectors.joining(ServiceAdapterHeaders.SET_SEPARATOR));
    }
}