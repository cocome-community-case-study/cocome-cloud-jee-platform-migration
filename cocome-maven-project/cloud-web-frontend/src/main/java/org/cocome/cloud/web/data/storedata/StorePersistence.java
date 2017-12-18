package org.cocome.cloud.web.data.storedata;

import org.cocome.cloud.logic.stub.NotInDatabaseException_Exception;
import org.cocome.cloud.web.connector.enterpriseconnector.EnterpriseQuery;
import org.cocome.cloud.web.connector.storeconnector.StoreQuery;
import org.cocome.cloud.web.frontend.navigation.NavigationElements;
import org.cocome.cloud.web.frontend.util.Messages;

import javax.enterprise.context.ApplicationScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.validation.constraints.NotNull;
import java.util.Collection;

@Named
@ApplicationScoped
public class StorePersistence {

    @Inject
    private EnterpriseQuery enterpriseQuery;

    @Inject
    private StoreQuery storeQuery;

    public String updateStore(@NotNull StoreViewData store) throws NotInDatabaseException_Exception {
        store.updateStoreInformation();
        if (enterpriseQuery.updateStore(store)) {
            store.setEditingEnabled(false);
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, Messages.get("store.update.success"), null));
            return null;
        }
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_ERROR, Messages.get("store.update.failed"), null));

        return "error";
    }

    public String createStore(long enterpriseID, @NotNull String name, @NotNull String location) throws NotInDatabaseException_Exception {
        if (enterpriseQuery.createStore(enterpriseID, name, location)) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, Messages.get("store.create.success"), null));
        } else {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, Messages.get("store.create.failed"), null));
        }

        return NavigationElements.SHOW_STORES.getNavigationOutcome();
    }

    public boolean createItem(StoreViewData store, ProductWrapper product) {
        return storeQuery.createItem(store, product);
    }

    public boolean orderProducts(StoreViewData store, Collection<OrderItem> items) {
        return storeQuery.orderProducts(store, items);
    }

    public boolean rollInOrder(StoreViewData store, long orderID) {
        return storeQuery.rollInOrder(store, orderID);
    }

    public boolean updateItem(StoreViewData store, ProductWrapper item) {
        return storeQuery.updateItem(store, item);
    }
}
