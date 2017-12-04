/*
 **************************************************************************
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
 **************************************************************************
 */

package org.cocome.tradingsystem.inventory.application.store;

import org.apache.log4j.Logger;
import org.cocome.cloud.logic.stub.CreateException_Exception;
import org.cocome.cloud.logic.stub.IEnterpriseManager;
import org.cocome.cloud.logic.stub.NotInDatabaseException_Exception;
import org.cocome.cloud.logic.stub.RecipeException_Exception;
import org.cocome.tradingsystem.inventory.application.plant.recipe.ProductionOrderEntryTO;
import org.cocome.tradingsystem.inventory.application.plant.recipe.ProductionOrderTO;
import org.cocome.tradingsystem.inventory.application.plant.recipe.RecipeTO;
import org.cocome.tradingsystem.inventory.data.enterprise.*;
import org.cocome.tradingsystem.inventory.data.persistence.IPersistenceContext;
import org.cocome.tradingsystem.inventory.data.persistence.UpdateException;
import org.cocome.tradingsystem.inventory.data.plant.IPlantDataFactory;
import org.cocome.tradingsystem.inventory.data.plant.recipe.IRecipe;
import org.cocome.tradingsystem.inventory.data.store.*;
import org.cocome.tradingsystem.util.exception.NotInDatabaseException;
import org.cocome.tradingsystem.util.exception.RecipeException;
import org.cocome.tradingsystem.util.java.Lists;
import org.cocome.tradingsystem.util.java.Maps;
import org.cocome.tradingsystem.util.qualifier.StoreRequired;
import org.cocome.tradingsystem.util.scope.IContextRegistry;
import org.cocome.tradingsystem.util.scope.RegistryKeys;

import javax.annotation.PostConstruct;
import javax.ejb.CreateException;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import java.io.Serializable;
import java.util.*;

/**
 * Implements the server part of the store application.
 *
 * @author Yannick Welsch
 * @author Lubomir Bulej
 * @author Tobias Pöppke
 * @author Robert Heinrich
 */
@Stateless
public class StoreServer implements Serializable, IStoreInventoryManagerLocal, IStoreInventoryLocal {

    private static final long serialVersionUID = -529765757261183369L;

    private static final Logger LOG = Logger.getLogger(StoreServer.class);

    @Inject
    private EnterpriseClientFactory enterpriseClientFactory;

    @Inject
    private IStoreQuery storeQuery;

    @Inject
    private IEnterpriseQuery enterpriseQuery;

    @Inject
    private IPersistenceContext pctx;

    @Inject
    @StoreRequired
    private IContextRegistry context;

    @Inject
    private IPlantDataFactory plantFactory;

    @Inject
    private IStoreDataFactory storeFactory;

    @Inject
    private IEnterpriseDataFactory enterpriseFactory;

    /**
     * Remote reference to the product dispatcher.
     * TODO Change to webservice call
     */
//	@EJB
//	private IProductDispatcherLocal __dispatcher;
    @PostConstruct
    private void __setUpStore() {
        long storeIndex = context.getLong(RegistryKeys.STORE_ID);
        LOG.debug("Setting up store with ID " + storeIndex);
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public ProductWithItemTO changePrice(long storeID, final ProductWithItemTO stockItemTO)
            throws NotInDatabaseException, UpdateException {
        final IItem si = storeQuery.queryItemById(
                stockItemTO.getItem().getId());

        si.setSalesPrice(stockItemTO.getItem().getSalesPrice());
        pctx.updateEntity(si);

        return storeFactory.fillProductWithItemTO(si);
    }

    @Override
    public List<ProductWithSupplierTO> getAllProducts(long storeID) throws NotInDatabaseException {
        final Collection<IProduct> products = storeQuery.queryProducts(
                storeID);

        final List<ProductWithSupplierTO> result = Lists.newArrayList();
        for (final IProduct product : products) {
            result.add(enterpriseFactory.fillProductWithSupplierTO(product));
        }

        return result;
    }

    @Override
    public List<ProductWithSupplierAndStockItemTO> getProductsWithStockItems(long storeID) throws NotInDatabaseException {
        final Collection<IStockItem> stockItems = storeQuery
                .queryAllStockItems(storeID);

        final List<ProductWithSupplierAndStockItemTO> result = Lists
                .newArrayList();
        for (final IStockItem stockItem : stockItems) {
            result.add(storeFactory.fillProductWithSupplierAndStockItemTO(stockItem));
        }

        return result;
    }

    @Override
    public ComplexOrderTO getOrder(long storeID, final long orderId) throws NotInDatabaseException {
        return storeFactory.fillComplexOrderTO(storeQuery
                .queryOrderById(orderId));
    }

    @Override
    public List<ComplexOrderTO> getOutstandingOrders(long storeID) throws NotInDatabaseException {
        final Collection<IProductOrder> orders = storeQuery
                .queryOutstandingOrders(storeID);

        final List<ComplexOrderTO> result = Lists.newArrayList();
        for (final IProductOrder order : orders) {
            result.add(storeFactory.fillComplexOrderTO(order));
        }

        return result;
    }

    @Override
    public List<ProductWithItemTO> getProductsWithLowStock(long storeID) {
        final Collection<IStockItem> stockItems = storeQuery
                .queryLowStockItems(storeID);

        final List<ProductWithItemTO> result = Lists.newArrayList();
        for (final IStockItem si : stockItems) {
            result.add(storeFactory.fillProductWithItemTO(si));
        }

        return result;
    }

    @Override
    public StoreWithEnterpriseTO getStore(long storeID) throws NotInDatabaseException {
        return storeFactory.fillStoreWithEnterpriseTO(storeQuery
                .queryStoreById(storeID));
    }

    @Override
    public List<ComplexOrderTO> orderProducts(long storeID, final ComplexOrderTO complexOrder)
            throws NotInDatabaseException, CreateException, UpdateException {
        final IStoreQuery sq = storeQuery;

        final HashMap<Long, List<IOrderEntry>> ordersBySupplier = Maps
                .newHashMap();

        final LinkedList<IStockItem> updatedStockItems = new LinkedList<>();

        for (final ComplexOrderEntryTO coeto : complexOrder.getOrderEntryTOs()) {
            final IProduct product = sq.queryProductByBarcode(coeto.getProductTO().getProductTO()
                    .getBarcode());

            LOG.debug(String.format("Found product %d", coeto.getProductTO().getProductTO().getBarcode()));

            IOrderEntry oe = storeFactory.getNewOrderEntry();
            oe.setProduct(product);
            oe.setAmount(coeto.getAmount());
            oe.setProductBarcode(product.getBarcode());

            // this is probably not necessary to persist because it gets
            // persisted with the ProductOrder later on
            // pctx.createEntity(oe);

            //

            IProductSupplier supplier = product.getSupplier();
            long supplierId = -1;

            // It is possible that there is no supplier entry for a product...
            // Perhaps throw an exception in this case
            if (supplier != null) {
                supplierId = product.getSupplier().getId();
            }

            List<IOrderEntry> entries = ordersBySupplier
                    .computeIfAbsent(supplierId, k -> Lists.newArrayList());
            entries.add(oe);

            IStockItem item = sq.queryStockItem(storeID, oe.getProductBarcode());
            item.setIncomingAmount(item.getIncomingAmount() + oe.getAmount());
            updatedStockItems.add(item);
        }

        LOG.info(ordersBySupplier);
        final IStore store = sq.queryStoreById(storeID);
        final List<IProductOrder> orders = Lists.newArrayList();
        for (final List<IOrderEntry> orderEntries : ordersBySupplier.values()) {
            IProductOrder po = storeFactory.getNewProductOrder();
            po.setOrderEntries(orderEntries);
            po.setStore(store);
            // set OrderingDate to NOW
            po.setOrderingDate(new Date());
            po.setDeliveryDate(null);

            pctx.createEntity(po);

            orders.add(po);
        }

        for (IStockItem item : updatedStockItems) {
            pctx.updateEntity(item);
        }

        //

        final List<ComplexOrderTO> result = Lists.newArrayList();
        for (final IProductOrder order : orders) {
            result.add(storeFactory.fillComplexOrderTO(order));
        }

        return result;
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public void rollInReceivedOrder(long storeID, final long orderId)
            throws InvalidRollInRequestException, NotInDatabaseException, UpdateException {
        final IProductOrder order = storeQuery.queryOrderById(orderId);

        //
        // Ignore the roll in if the order has been already rolled in.
        //
        if (order.getDeliveryDate() != null && order.getDeliveryDate().after(order.getOrderingDate())) {
            final String message = String.format(
                    "Product order %d already rolled in.", order.getId());

            LOG.warn(message);
            throw new InvalidRollInRequestException(message);
        }

        //
        // Ignore the roll in if the order is for different store.
        //
        if (order.getStore().getId() != storeID) {
            final String message = String.format(
                    "Order in store %d cannot be rolled-in by store %d", order
                            .getStore().getId(), storeID);

            LOG.error(message);
            throw new InvalidRollInRequestException(message);
        }

        // set DeliveryDate to NOW
        order.setDeliveryDate(new Date());

        for (final IOrderEntry oe : order.getOrderEntries()) {
            final IStockItem si = storeQuery.queryStockItem(storeID, oe
                    .getProduct().getBarcode());

            //
            // Create a new stock item for completely new products.
            //
            if (si == null) {
                // TODO Create a new stock item if it does not exist
                throw new IllegalArgumentException("StockItem si == null");
            }

            final IProduct product = si.getProduct();
            final long oldAmount = si.getAmount();
            final long newAmount = oldAmount + oe.getAmount();
            final long newIncoming = si.getIncomingAmount() - oe.getAmount();

            si.setAmount(newAmount);
            if (newIncoming >= 0) {
                si.setIncomingAmount(newIncoming);
            } else {
                si.setIncomingAmount(0);
                LOG.warn(String.format("New incoming amount of %s (%d) was negative (%d).", product.getName(),
                        product.getBarcode(), newIncoming));
            }

            pctx.updateEntity(si);

            LOG.warn(String.format("%s (%d) stock increased from %d to %d.",
                    product.getName(), product.getBarcode(), oldAmount,
                    newAmount));
        }
        pctx.updateEntity(order);
    }

    @Override
    public ProductWithItemTO getProductWithStockItem(long storeID,
                                                     final long productBarCode) throws NoSuchProductException {
        final IStockItem stockItem = storeQuery.queryStockItem(storeID,
                productBarCode);

        if (stockItem == null) {
            throw new NoSuchProductException(
                    "There is no stock item for product with barcode "
                            + productBarCode);
        }

        return storeFactory.fillProductWithItemTO(stockItem);
    }

    @Override
    public long accountSale(long storeID, final SaleTO sale)
            throws ProductOutOfStockException, NotInDatabaseException, UpdateException {
        return __bookSale(storeID, sale);
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    private long __bookSale(long storeID, final SaleTO saleTO)
            throws ProductOutOfStockException, NotInDatabaseException, UpdateException {

        final StoreWithEnterpriseTO store = this.getStore(storeID);

        final ProductionOrderTO productionOrder = new ProductionOrderTO();
        productionOrder.setOrderEntries(new LinkedHashSet<>());
        productionOrder.setStore(store);

        for (final SaleEntryTO entry : saleTO.getEntries()) {
            if (entry.getItemInfo().getItem() instanceof StockItemTO) {


                final IStockItem si = storeQuery.queryStockItemById(entry.getItemInfo().getItem().getId());

                if (si.getAmount() == 0) {
                    // Normally this should not happen...
                    throw new ProductOutOfStockException(
                            "The requested product is not in stock anymore!");
                }

                si.setAmount(si.getAmount() - 1);
                pctx.updateEntity(si);
            } else if (entry.getItemInfo().getItem() instanceof OnDemandItemTO) {
                final IRecipe recipe = enterpriseQuery.queryRecipeByCustomProductID(entry.getItemInfo().getProduct().getId());
                final RecipeTO recipeTO = plantFactory.fillRecipeTO(recipe);
                final ProductionOrderEntryTO productionOrderEntry = new ProductionOrderEntryTO();
                productionOrderEntry.setAmount(1);
                productionOrderEntry.setRecipe(recipeTO);
                productionOrderEntry.setParameterValues(entry.getParameterValues());
                productionOrder.getOrderEntries().add(productionOrderEntry);
            } else {
                throw new UnsupportedOperationException("Unknown item type: " + entry.getItemInfo().getItem().getClass().getName());
            }
        }
        //
        // Check for items running low on stock. Required for UC 8.
        // Alternative (and probably better) design would be to check
        // once in a while from separate thread, not on every sale.
        //
        try {
            __checkForLowRunningGoods(storeID);
        } catch (final Exception e) {
            LOG.warn(String.format("Failed UC8! Could not transport low-stock items from other stores: %s",
                    e.getMessage()));
        }

        //
        // Send production order if required
        //
        if (productionOrder.getOrderEntries().isEmpty()) {
            return 0;
        }
        final IEnterpriseManager enterpriseManager = this.enterpriseClientFactory
                .createClient(store.getEnterpriseTO().getId());

        try {
            return enterpriseManager.submitProductionOrder(productionOrder);
        } catch (CreateException_Exception | NotInDatabaseException_Exception | RecipeException_Exception e) {
            LOG.error("Unable to submit production order", e);
            throw new UpdateException(e.getMessage(), e);
        }
    }

    @Override
    public ComplexOrderEntryTO[] getStockItems(long storeID,
                                               final ProductTO[] requiredProductTOs) throws NotImplementedException {
        // TODO Auto-generated method stub
        throw new NotImplementedException("TODO: SDQ implement");
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public void markProductsUnavailableInStock(long storeID,
                                               final ProductMovementTO movedProducts)
            throws ProductOutOfStockException, UpdateException {
        for (final ProductAmountTO movedProduct : movedProducts
                .getProductAmounts()) {
            final ProductTO productTO = movedProduct.getProduct();
            final long barcode = productTO.getBarcode();
            final IStockItem stockItem = storeQuery.queryStockItem(storeID,
                    barcode);

            if (stockItem == null) {
                throw new ProductOutOfStockException(String.format(
                        "Store %d has no product with barcode %d", storeID,
                        barcode));
            }

            //

            final long availableAmount = stockItem.getAmount();
            final long movedAmount = movedProduct.getAmount();

            if (availableAmount < movedAmount) {
                throw new ProductOutOfStockException(
                        String.format(
                                "Store %d only has %d product(s) with barcode %d, but %d required",
                                storeID, availableAmount, barcode,
                                movedAmount));
            }

            // set new remaining stock amount
            stockItem.setAmount(availableAmount - movedAmount);
            pctx.updateEntity(stockItem);

            //
            // TODO: virtual printout is missing
            // A list of all products that need to be delivered should be
            // printed out.
            //
            final StoreTO originStore = movedProducts.getOriginStore();
            final StoreTO destinationStore = movedProducts
                    .getDestinationStore();
            LOG.info(String.format("[%s at %s] Ship %s, barcode %d to %s at %s, amount %d\n",
                    originStore.getName(), originStore.getLocation(),
                    productTO.getName(), barcode, destinationStore.getName(),
                    destinationStore.getLocation(), movedAmount));
        }
    }

    /**
     * Checks for goods that run low. If there are goods running low they
     * transported from nearby stores in the enterprise.
     * <p>
     * Technically, the operation is performed by the product dispatcher. The
     * store only needs to provide it with products that are low on stock. If
     * there is any problem communicating with the product dispatcher, the
     * operation will not be performed. In case of transient errors, the
     * operation may succeed during next check for low-stock products.
     * <p>
     * Required for UC 8
     */
    private void __checkForLowRunningGoods(long storeID) throws Exception {
        //
        // Determine the products and amounts of items that are
        // actually required, i.e. items that are really low on
        // stock, including their current incoming amount.
        //
        final Collection<ProductAmountTO> requiredProducts = __findRequiredProducts(storeID);

        if (requiredProducts.size() < 1) {
            return;
        }

        //
        // Order required products from stores determined by the
        // product dispatcher.
        //
        final ProductAmountTO[] incomingProducts = __orderRequiredProducts(storeID,
                requiredProducts);

        if (incomingProducts.length < 1) {
            return;
        }

        //
        // Mark the products coming from other stores as incoming.
        //
        __registerIncomingProducts(storeID, incomingProducts);
    }

    private Collection<ProductAmountTO> __findRequiredProducts(long storeID) {
        //
        // Query the store inventory for apparently low stock items,
        // without consider items coming from other stores.
        //
        final Collection<IStockItem> lowStockItems = storeQuery
                .queryLowStockItems(storeID);
        if (lowStockItems.size() < 1) {
            return Collections.emptyList();
        }

        //
        // Filter the low-stock items to determine items that are really
        // low on stock and should be transported from other stores.
        //
        final Collection<IStockItem> itemsToOrder = __selectItemsToOrder(storeID, lowStockItems);
        if (itemsToOrder.size() < 1) {
            return Collections.emptyList();
        }

        //
        // Finally determine the product amounts that need ot be transported
        // from nearby stores.
        //
        return __calculateRequiredAmounts(storeID, itemsToOrder);
    }

    /**
     * Selects and returns stock items that are really low on stock and will be
     * ordered from other stores. Many items can be low on stock, but have more
     * stock incoming that along with the current stock satisfies the minimal
     * stock condition. Such items are filtered out and only those really low on
     * stock are left.
     */
    private Collection<IStockItem> __selectItemsToOrder(long storeID,
                                                        final Collection<IStockItem> stockItems) {
        final Collection<IStockItem> result = new LinkedList<>();
        for (final IStockItem stockItem : stockItems) {
            final IProduct product = stockItem.getProduct();
            LOG.debug(String.format("\t%s, barcode %d, amount %d, incoming %d, min stock %d",
                    product.getName(), product.getBarcode(),
                    stockItem.getAmount(), stockItem.getIncomingAmount(),
                    stockItem.getMinStock()));

            final long virtualAmount = stockItem.getAmount()
                    + stockItem.getIncomingAmount();
            if (virtualAmount >= stockItem.getMinStock()) {
                LOG.debug(String.format("\t\tvirtual stock %d => not low stock", virtualAmount));
                continue;
            }

            result.add(stockItem);
        }

        LOG.debug(String.format("%d really low-stock items in store %d", result.size(), storeID));
        return result;
    }

    /**
     * Orders by default the minimum stock items for each low running
     * product/good.
     * <p>
     * Required for UC 8
     *
     * @param stockItems collection of product stock items that run low
     * @return Collection of Product/Amount tuples for each product, which
     * represents the required amount of each product.
     */
    private Collection<ProductAmountTO> __calculateRequiredAmounts(long storeID,
                                                                   final Collection<IStockItem> stockItems) {
        final Collection<ProductAmountTO> result = Lists.newArrayList();

        //
        // Order at least minimum stock for each item, but do not exceed stock
        // limits. The stock of each item in the collections is guaranteed lower
        // than the minimum (including the incoming amount), so we will never
        // exceed the maximum level.
        //
        for (final IStockItem stockItem : stockItems) {
            long orderAmount = stockItem.getMinStock();
            if (2 * stockItem.getMinStock() >= stockItem.getMaxStock()) {
                orderAmount = stockItem.getMaxStock() - stockItem.getMinStock();
            }

            final ProductAmountTO pa = new ProductAmountTO();
            pa.setProduct(enterpriseFactory.fillProductTO(stockItem
                    .getProduct()));
            pa.setAmount(orderAmount);

            result.add(pa);
        }

        LOG.debug(String.format("%d products to be ordered by store %d", result.size(),
                storeID));
        return result;
    }

    /**
     * Requests the product dispatcher to determine the stores to transfer goods
     * from and to issue the product movement orders. Returns the amounts of
     * items incoming from other stores.
     *
     * @throws NotInDatabaseException if a database error occurred
     */
    private ProductAmountTO[] __orderRequiredProducts(
            long storeID,
            final Collection<ProductAmountTO> requiredProducts) throws NotInDatabaseException {
        //
        // Connect to the product dispatcher and order the required products
        // from other stores in the enterprise. Do nothing if the connection
        // cannot be established.
        final IStore store = storeQuery.queryStoreById(storeID);

//		final ProductAmountTO[] result = __dispatcher
//				.dispatchProductsFromOtherStores(store.getId(),
//						requiredProducts);
        final ProductAmountTO[] result = new ProductAmountTO[0];

        LOG.debug(String.format("%d products incoming to store %d", result.length, storeID));
        return result;
    }

    /**
     * Registers the products coming from other stores by increasing the
     * incoming amount of stock items corresponding to the incoming products.
     *
     * @throws UpdateException if a database error occurred
     */
    private void __registerIncomingProducts(
            long storeID,
            final ProductAmountTO[] incomingProducts) throws UpdateException {
        for (final ProductAmountTO incomingProductTO : incomingProducts) {
            final ProductTO incomingProduct = incomingProductTO.getProduct();
            final IStockItem stockItem = storeQuery.queryStockItem(storeID,
                    incomingProduct.getBarcode());

            final long incomingAmount = incomingProductTO.getAmount();
            stockItem.setIncomingAmount(stockItem.getIncomingAmount()
                    + incomingAmount);
            pctx.updateEntity(stockItem);

            LOG.debug(String.format("\t%s, barcode %d, incoming amount %d",
                    incomingProduct.getName(), incomingProduct.getBarcode(),
                    incomingAmount));
        }
    }

    @Override
    public ProductWithItemTO updateItem(long storeID,
                                        ProductWithItemTO itemTO) throws NotInDatabaseException,
            UpdateException {
        final IItem item = storeFactory.convertToItem(itemTO.getItem());
        final IStore store = storeQuery.queryStoreById(storeID);

        final IProduct product;
        if (itemTO.getProduct().getId() != 0) {
            product = enterpriseQuery.queryProductByID(itemTO.getProduct().getId());
        } else {
            product = enterpriseQuery.queryProductByBarcode(itemTO.getProduct().getBarcode());
        }

        item.setProduct(product);
        item.setProductBarcode(product.getBarcode());
        item.setStore(store);
        item.setStoreId(store.getId());

        pctx.updateEntity(item);
        return storeFactory.fillProductWithItemTO(item);
    }

    @Override
    public long createItem(long storeID, ProductWithItemTO itemTO)
            throws NotInDatabaseException, CreateException {
        final IItem item = storeFactory.convertToItem(itemTO.getItem());
        final IStore store = storeQuery.queryStoreById(storeID);

        final IProduct product;
        if (itemTO.getProduct().getId() != 0) {
            product = enterpriseQuery.queryProductByID(itemTO.getProduct().getId());
        } else {
            product = enterpriseQuery.queryProductByBarcode(itemTO.getProduct().getBarcode());
        }

        item.setProduct(product);
        item.setProductBarcode(product.getBarcode());
        item.setStore(store);
        item.setStoreId(store.getId());

        pctx.createEntity(item);
        return item.getId();
    }

    @Override
    public void deleteItem(long storeID, ProductWithItemTO itemTO)
            throws NotInDatabaseException, UpdateException {
        final IItem item = storeFactory.convertToItem(itemTO.getItem());
        item.setProductBarcode(itemTO.getProduct().getBarcode());
        item.setStoreId(storeID);
        pctx.deleteEntity(item);
    }

}
