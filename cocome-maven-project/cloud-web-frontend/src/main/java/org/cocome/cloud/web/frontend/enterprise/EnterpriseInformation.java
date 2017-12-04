package org.cocome.cloud.web.frontend.enterprise;

import org.apache.log4j.Logger;
import org.cocome.cloud.logic.stub.NotInDatabaseException_Exception;
import org.cocome.cloud.web.data.enterprisedata.EnterpriseViewData;
import org.cocome.cloud.web.data.enterprisedata.IEnterpriseDAO;
import org.cocome.cloud.web.data.plantdata.PlantDAO;
import org.cocome.cloud.web.data.plantdata.PlantViewData;
import org.cocome.cloud.web.data.storedata.IStoreDAO;
import org.cocome.cloud.web.data.storedata.StoreViewData;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Collection;
import java.util.LinkedList;

/**
 * Holds information about the currently active enterprise.
 *
 * @author Tobias Pöppke
 * @author Robert Heinrich
 */
@Named
@SessionScoped
public class EnterpriseInformation implements Serializable {
    private static final long serialVersionUID = 1L;

    private long activeEnterpriseID = Long.MIN_VALUE;
    private EnterpriseViewData activeEnterprise;

    private String newEnterpriseName = null;

    private static final Logger LOG = Logger.getLogger(EnterpriseInformation.class);

    @Inject
    private IEnterpriseDAO enterpriseDAO;

    @Inject
    private IStoreDAO storeDAO;

    @Inject
    private PlantDAO plantDAO;

    private boolean enterpriseSubmitted = false;

    public Collection<EnterpriseViewData> getEnterprises() throws NotInDatabaseException_Exception {
        return enterpriseDAO.getAllEnterprises();
    }

    public Collection<StoreViewData> getStores() throws NotInDatabaseException_Exception {
        if (activeEnterpriseID != Long.MIN_VALUE) {
            return storeDAO.getStoresInEnterprise(activeEnterpriseID);
        }
        // TODO Throw custom exception to signal error here
        return new LinkedList<>();
    }

    public Collection<PlantViewData> getPlants() throws NotInDatabaseException_Exception {
        if (activeEnterpriseID != Long.MIN_VALUE) {
            return plantDAO.getPlantsInEnterprise(activeEnterpriseID);
        }
        // TODO Throw custom exception to signal error here
        return new LinkedList<>();
    }

    public long getActiveEnterpriseID() {
        return activeEnterpriseID;
    }

    public void setActiveEnterpriseID(@NotNull long enterpriseID) {
        LOG.debug("Active enterprise was set to " + enterpriseID);
        this.activeEnterpriseID = enterpriseID;
    }

    public EnterpriseViewData getActiveEnterprise() throws NotInDatabaseException_Exception {
        if (activeEnterprise == null && activeEnterpriseID != Long.MIN_VALUE) {
            activeEnterprise = enterpriseDAO.getEnterpriseByID(activeEnterpriseID);
        }
        return activeEnterprise;
    }

    public String submitActiveEnterprise() {
        if (isEnterpriseSet()) {
            enterpriseSubmitted = true;
            // return null to only refresh the current form and not create a new view
            return null;
        } else {
            return "error";
        }
    }

    public boolean isEnterpriseSubmitted() {
        return enterpriseSubmitted;
    }

    public boolean isEnterpriseSet() {
        return activeEnterpriseID != Long.MIN_VALUE;
    }

    public void setEnterpriseSubmitted(boolean submitted) {
        this.enterpriseSubmitted = submitted;
    }

    public void setActiveEnterprise(@NotNull EnterpriseViewData enterprise) {
        activeEnterpriseID = enterprise.getId();
        this.activeEnterprise = enterprise;
    }

    public void setNewEnterpriseName(String name) {
        newEnterpriseName = name;
    }

    public String getNewEnterpriseName() {
        return newEnterpriseName;
    }
}
