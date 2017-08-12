package org.cocome.cloud.web.frontend.enterprise;

import org.cocome.cloud.logic.stub.NotInDatabaseException_Exception;
import org.cocome.cloud.web.data.enterprisedata.EnterpriseViewData;
import org.cocome.cloud.web.data.plantdata.PlantViewData;
import org.cocome.cloud.web.data.storedata.StoreViewData;

import javax.validation.constraints.NotNull;
import java.util.Collection;

/**
 * Interface for information regarding the currently active enterprise.
 *
 * @author Tobias Pöppke
 * @author Robert Heinrich
 */
public interface IEnterpriseInformation {
    Collection<EnterpriseViewData> getEnterprises() throws NotInDatabaseException_Exception;

    Collection<StoreViewData> getStores() throws NotInDatabaseException_Exception;

    Collection<PlantViewData> getPlants() throws NotInDatabaseException_Exception;

    long getActiveEnterpriseID();

    void setActiveEnterpriseID(long enterpriseID);

    EnterpriseViewData getActiveEnterprise() throws NotInDatabaseException_Exception;

    void setActiveEnterprise(@NotNull EnterpriseViewData enterprise);

    String submitActiveEnterprise();

    boolean isEnterpriseSubmitted();

    void setEnterpriseSubmitted(boolean submitted);

    boolean isEnterpriseSet();

    String getNewEnterpriseName();

    void setNewEnterpriseName(String name);
}
