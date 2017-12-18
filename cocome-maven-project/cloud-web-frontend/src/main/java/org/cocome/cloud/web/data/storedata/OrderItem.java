package org.cocome.cloud.web.data.storedata;

import org.cocome.tradingsystem.inventory.application.store.ComplexOrderEntryTO;
import org.cocome.tradingsystem.inventory.application.store.StockItemTO;

public class OrderItem {
    private ProductWrapper<StockItemTO> product;
    private int amount;

    public OrderItem(ProductWrapper<StockItemTO> product, int amount) {
        this.setProduct(product);
        this.setAmount(amount);
    }

    public ProductWrapper<StockItemTO> getProduct() {
        return product;
    }

    public void setProduct(ProductWrapper<StockItemTO> product) {
        this.product = product;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public static ComplexOrderEntryTO convertToOrderEntryTO(OrderItem item) {
        ComplexOrderEntryTO orderTO = new ComplexOrderEntryTO();
        orderTO.setAmount(item.amount);
        orderTO.setProductTO(ProductWrapper.convertToProductTO(item.product));
        return orderTO;
    }
}
