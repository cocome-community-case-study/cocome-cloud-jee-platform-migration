package org.cocome.cloud.web.frontend.store;

import org.cocome.cloud.web.data.storedata.IStorePersistence;
import org.cocome.cloud.web.data.storedata.ProductWrapper;
import org.cocome.cloud.web.frontend.navigation.NavigationElements;
import org.cocome.cloud.web.frontend.util.Messages;
import org.cocome.tradingsystem.inventory.application.store.OnDemandItemTO;
import org.cocome.tradingsystem.inventory.application.store.StockItemTO;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

@ManagedBean
@ViewScoped
public class ShowStockView {

    @Inject
    private IStorePersistence storePersistence;

    @Inject
    private StoreInformation storeInformation;

    public String createNewStockItem(ProductWrapper<StockItemTO> item) {
        if (storePersistence.createItem(storeInformation.getActiveStore(), item)) {
            storeInformation.queryStockItems();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
                    Messages.get("stock.add.success"), null));
            return NavigationElements.SHOW_STOCK.getNavigationOutcome();
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    Messages.get("stock.add.failed"), null));
        }
        return null;
    }

    public String createNewOnDemandItem(ProductWrapper<OnDemandItemTO> item) {
        if (storePersistence.createItem(storeInformation.getActiveStore(), item)) {
            storeInformation.queryOnDemandItems();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
                    Messages.get("message.create.success", "On Demand Item"), null));
            return NavigationElements.SHOW_STOCK.getNavigationOutcome();
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    Messages.get("message.create.success","On Demand Item"), null));
        }
        return null;
    }

    public String updateStockItem(ProductWrapper item) {
        if (storePersistence.updateItem(item.getOriginStore(), item)) {
            item.setEditingEnabled(false);
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, Messages.get("stock.update.success"), null));
        } else {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, Messages.get("stock.update.failed"), null));
        }
        return null;
    }
}
