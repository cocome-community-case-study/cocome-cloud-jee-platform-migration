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
import java.io.IOException;
import java.io.Serializable;
import java.util.Collection;

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

    private EnterpriseViewData activeEnterprise;

    private String newEnterpriseName = null;

    private static final Logger LOG = Logger.getLogger(EnterpriseInformation.class);

    @Inject
    private IEnterpriseDAO enterpriseDAO;

    @Inject
    private IStoreDAO storeDAO;

    @Inject
    private PlantDAO plantDAO;
    private boolean hasActiveEnterprise;

    public Collection<EnterpriseViewData> getEnterprises() throws NotInDatabaseException_Exception {
        return enterpriseDAO.getAllEnterprises();
    }

    public Collection<StoreViewData> getStores() throws NotInDatabaseException_Exception {
        return storeDAO.getStoresInEnterprise(getActiveEnterprise().getId());
    }

    public Collection<PlantViewData> getPlants() throws NotInDatabaseException_Exception {
        return plantDAO.getPlantsInEnterprise(getActiveEnterprise().getId());
    }

    public EnterpriseViewData getActiveEnterprise() throws NotInDatabaseException_Exception {
        if (activeEnterprise == null) {
            LOG.info("No enterprise selected, redirecting to enterprises view");
            try {
                throw new IOException("AAAAA");
                //Faces.redirect("show_enterprises.xhtml");
            } catch (IOException e) {
                LOG.error(e);
                e.printStackTrace();
            }
        }
        return activeEnterprise;
    }

    public String showStoresOf(@NotNull EnterpriseViewData enterprise) {
        this.setActiveEnterprise(enterprise);
        return "show_stores?faces-redirect=true";
    }

    public String showPlantsOf(@NotNull EnterpriseViewData enterprise) {
        this.setActiveEnterprise(enterprise);
        return "show_plants?faces-redirect=true";
    }

    public void setActiveEnterprise(@NotNull EnterpriseViewData enterprise) {
        this.activeEnterprise = enterprise;
    }

    public void setNewEnterpriseName(String name) {
        newEnterpriseName = name;
    }

    public String getNewEnterpriseName() {
        return newEnterpriseName;
    }

    public boolean isHasActiveEnterprise() {
        return this.activeEnterprise != null;
    }
}
