package org.cocome.cloud.web.data.storedata;

import org.apache.log4j.Logger;
import org.cocome.tradingsystem.inventory.application.store.*;

/**
 * Wraps the {@link ProductTO} and {@link StockItemTO} objects received from
 * the backend to better accommodate the needs of the UI.
 *
 * @author Tobias Pöppke
 * @author Robert Heinrich
 */
public class ProductWrapper<T extends ItemTO> {
    private static final Logger LOG = Logger.getLogger(ProductWrapper.class);

    private ProductTO product;
    private T item;

    private StoreViewData originStore;

    private boolean editingEnabled = false;

    private boolean inCurrentOrder = false;

    public ProductWrapper(ProductTO product) {
        this.product = product;
    }

    public ProductWrapper(ProductTO product, T item, StoreViewData originStore) {
        this(product);
        this.item = item;
        this.originStore = originStore;
    }

    public void setItem(T item) {
        this.item = item;
    }

    public String getName() {
        return product.getName();
    }

    public long getBarcode() {
        return product.getBarcode();
    }

    public StoreViewData getOriginStore() {
        return originStore;
    }

    public T getItemTO() {
        return item;
    }

    public boolean isEditingEnabled() {
        return editingEnabled;
    }

    public void setEditingEnabled(boolean editingEnabled) {
        LOG.debug(String.format("Editing enabled set to %b for item %s.", editingEnabled, product.getName()));
        this.editingEnabled = editingEnabled;
    }

    public boolean isInCurrentOrder() {
        return inCurrentOrder;
    }

    public void setInCurrentOrder(boolean inCurrentOrder) {
        this.inCurrentOrder = inCurrentOrder;
    }

    public static ProductWithSupplierTO convertToProductTO(ProductWrapper product) {
        ProductWithSupplierTO productTO = new ProductWithSupplierTO();
        productTO.setProductTO(product.product);
        productTO.setSupplierTO(new SupplierTO());
        return productTO;
    }

    public ProductTO getProduct() {
        return product;
    }

    public void setProduct(ProductTO product) {
        this.product = product;
    }
}
