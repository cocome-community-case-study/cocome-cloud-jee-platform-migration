package org.cocome.cloud.web.frontend.store;

import org.apache.log4j.Logger;
import org.cocome.cloud.web.data.storedata.OrderItem;
import org.cocome.cloud.web.data.storedata.ProductWrapper;
import org.cocome.cloud.web.data.storedata.StorePersistence;
import org.cocome.cloud.web.data.storedata.StoreViewData;
import org.cocome.cloud.web.frontend.navigation.NavigationElements;
import org.cocome.tradingsystem.inventory.application.store.StockItemTO;

import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.*;

@Named
@ConversationScoped
// TODO resolve messages from localized strings
public class StockOrderView implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final Logger LOG = Logger.getLogger(StockOrderView.class);

    @Inject
    private StockOrderData orderData;

    @Inject
    private StoreInformation storeInformation;

    @Inject
    private StorePersistence storePersistence;

    @Inject
    private Conversation conversation;

    public String selectOrderItem(ProductWrapper<StockItemTO> item) {
        LOG.debug(String.format("Selecting item %s for ordering...", item.getName()));
        if (!isOrderInProgress()) {
            LOG.debug("Starting new order");
            startNewOrder();
        }

        orderData.setSelectedItem(item);

        return NavigationElements.ORDER_PRODUCTS.getNavigationOutcome();
    }

    public String addOrderAmount(int amount) {
        ProductWrapper selectedItem = orderData.getSelectedItem();
        Map<Long, OrderItem> itemMap = orderData.getItemMap();

        if (!isOrderInProgress() || selectedItem == null) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage("You have to select a stock item first!"));
            return NavigationElements.SHOW_STOCK.getNavigationOutcome();
        }

        LOG.debug(String.format("Adding amount %d of %s to order...", amount, selectedItem.getName()));

        itemMap.put(selectedItem.getBarcode(), new OrderItem(selectedItem, amount));
        selectedItem.setInCurrentOrder(true);
        deselectProduct();
        return NavigationElements.ORDER_PRODUCTS.getNavigationOutcome();
    }

    public String removeOrderItem(ProductWrapper item) {
        Map<Long, OrderItem> itemMap = orderData.getItemMap();
        OrderItem removedItem = itemMap.remove(item.getBarcode());
        removedItem.getProduct().setInCurrentOrder(false);
        return NavigationElements.SHOW_STOCK.getNavigationOutcome();
    }

    public String startNewOrder() {
        if (conversation.isTransient()) {
            conversation.begin();
        }

        if (isOrderInProgress()) {
            resetOrder();
        }

        orderData.setItemMap(new LinkedHashMap<>());
        orderData.setOrderInProgress(true);
        return NavigationElements.SHOW_STOCK.getNavigationOutcome();
    }

    public String submitOrder() {
        StoreViewData currentStore = storeInformation.getActiveStore();
        Collection<OrderItem> items = orderData.getItemMap().values();
        if (storePersistence.orderProducts(currentStore, items)) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Order was placed successfully!"));
            resetOrder();
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Error while placing the order!"));
        }

        return NavigationElements.SHOW_STOCK.getNavigationOutcome();
    }

    private void resetOrder() {
        if (!conversation.isTransient()) {
            conversation.end();
        }

        orderData.setOrderInProgress(false);
        orderData.setSelectedItem(null);

        emptyItemMap();

        orderData.setItemMap(null);
    }

    private void emptyItemMap() {
        Map<Long, OrderItem> itemMap = orderData.getItemMap();
        if (itemMap != null) {
            for (OrderItem item : itemMap.values()) {
                item.getProduct().setInCurrentOrder(false);
            }
            itemMap.clear();
        }
    }

    public String cancelOrder() {
        resetOrder();
        return NavigationElements.SHOW_STOCK.getNavigationOutcome();
    }

    public String showOrder() {
        return NavigationElements.ORDER_PRODUCTS.getNavigationOutcome();
    }

    public List<OrderItem> getOrderItems() {
        Map<Long, OrderItem> itemMap = orderData.getItemMap();
        return new LinkedList<>(itemMap.values());
    }

    public boolean hasOrderProducts() {
        Map<Long, OrderItem> itemMap = orderData.getItemMap();
        return itemMap != null && (itemMap.size() > 0);
    }

    public boolean isItemSelected() {
        return orderData.getSelectedItem() != null;
    }

    public ProductWrapper<StockItemTO> getSelectedItem() {
        return orderData.getSelectedItem();
    }

    public String deselectProduct() {
        orderData.setSelectedItem(null);
        return NavigationElements.SHOW_STOCK.getNavigationOutcome();
    }

    private boolean isOrderInProgress() {
        return orderData.isOrderInProgress();
    }
}
