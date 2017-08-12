package org.cocome.cloud.web.data.plantdata;

import org.cocome.cloud.logic.stub.NotInDatabaseException_Exception;
import org.cocome.cloud.web.data.storedata.StoreViewData;

import javax.validation.constraints.NotNull;

public interface IPlantPersistence {
    String updatePlant(@NotNull PlantViewData store)
            throws NotInDatabaseException_Exception;

    String createPlant(long enterpriseID,
                       @NotNull String name,
                       @NotNull String location)
            throws NotInDatabaseException_Exception;
}
