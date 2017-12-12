package org.cocome.cloud.web.data.enterprisedata;

import org.cocome.cloud.web.data.ViewData;
import org.cocome.tradingsystem.inventory.application.plant.recipe.PlantOperationTO;

/**
 * UI specific wrapper objects for {@link PlantOperationTO}
 *
 * @author Rudolf Biczok
 */
public class PlantOperationViewData extends ViewData<PlantOperationTO> {

    public PlantOperationViewData(PlantOperationTO data) {
        super(data);
    }

    @Override
    public long getParentId() {
        return this.data.getPlant().getId();
    }
}
