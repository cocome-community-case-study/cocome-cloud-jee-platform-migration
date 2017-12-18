package org.cocome.cloud.web.frontend.store;

import org.cocome.cloud.web.data.storedata.StoreDAO;
import org.cocome.cloud.web.data.storedata.StorePersistence;
import org.cocome.tradingsystem.inventory.application.store.ComplexOrderTO;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import java.util.LinkedList;
import java.util.List;

@ManagedBean
@ViewScoped
// TODO use localized messages from properties file
public class ReceiveOrderView {
    @Inject
    StoreDAO storeDAO;

    @Inject
    StorePersistence storePersistence;

    @Inject
    StoreInformation storeInformation;

    private List<ComplexOrderTO> orders;

    public void loadAllOrders() {
        orders = storeDAO.getAllOrders(storeInformation.getActiveStore());
    }

    public void loadOrder(long orderId) {
        ComplexOrderTO order = storeDAO.getOrderByID(storeInformation.getActiveStore(), orderId);
        if (order != null) {
            orders = new LinkedList<>();
            orders.add(order);
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Could not find the requested order!"));
        }
    }

    public List<ComplexOrderTO> getOrders() {
        return orders;
    }

    public String rollInOrder(ComplexOrderTO order) {
        if (storePersistence.rollInOrder(storeInformation.getActiveStore(), order.getId())) {
            loadAllOrders();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Order was rolled in successfully!"));
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Error while rolling in the order!"));
        }
        return null;
    }
}
