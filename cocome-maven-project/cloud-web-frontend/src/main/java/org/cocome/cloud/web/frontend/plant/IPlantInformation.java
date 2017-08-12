package org.cocome.cloud.web.frontend.plant;

import org.cocome.cloud.web.data.plantdata.PlantViewData;
import org.cocome.cloud.web.data.storedata.StoreViewData;

import javax.inject.Named;
import javax.validation.constraints.NotNull;

/**
 * Interface to retrieve information about the currently active store.
 *
 * @author Tobias Pöppke
 * @author Robert Heinrich
 */
@Named
public interface IPlantInformation {
    long PLANT_ID_NOT_SET = Long.MIN_VALUE;

    void setActivePlantID(long storeID);

    long getActivePlantID();

    PlantViewData getActivePlant();

    String submitPlant();

    boolean isStoreSet();

    String switchToPlant(@NotNull PlantViewData store, String destination);
}
