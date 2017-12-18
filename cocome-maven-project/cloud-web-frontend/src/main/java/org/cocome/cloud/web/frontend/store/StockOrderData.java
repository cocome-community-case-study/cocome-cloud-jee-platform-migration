package org.cocome.cloud.web.frontend.store;

import org.cocome.cloud.web.data.storedata.OrderItem;
import org.cocome.cloud.web.data.storedata.ProductWrapper;
import org.cocome.tradingsystem.inventory.application.store.StockItemTO;

import javax.enterprise.context.ConversationScoped;
import javax.inject.Named;
import java.io.Serializable;
import java.util.Map;

@Named
@ConversationScoped
public class StockOrderData implements Serializable {
    private static final long serialVersionUID = -4500925840688109911L;

    private boolean orderInProgress = false;
    private Map<Long, OrderItem> itemMap;
    private ProductWrapper<StockItemTO> selectedItem;

    boolean isOrderInProgress() {
        return orderInProgress;
    }

    void setOrderInProgress(boolean orderInProgress) {
        this.orderInProgress = orderInProgress;
    }

    Map<Long, OrderItem> getItemMap() {
        return itemMap;
    }

    void setItemMap(Map<Long, OrderItem> itemMap) {
        this.itemMap = itemMap;
    }

    ProductWrapper<StockItemTO> getSelectedItem() {
        return selectedItem;
    }

    void setSelectedItem(ProductWrapper<StockItemTO> selectedItem) {
        this.selectedItem = selectedItem;
    }
}
