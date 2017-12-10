package org.cocome.cloud.web.data.plantdata;

import org.cocome.cloud.web.data.ViewData;
import org.cocome.tradingsystem.inventory.application.plant.productionunit.ProductionUnitOperationTO;

/**
 * UI specific wraper objects for {@link ProductionUnitOperationTO}
 *
 * @author Rudolf Biczok
 */
public class ProductionUnitOperationViewData extends ViewData<ProductionUnitOperationTO> {

    public ProductionUnitOperationViewData(ProductionUnitOperationTO data) {
        super(data);
    }

    @Override
    public long getServiceId() {
        return this.data.getProductionUnitClass().getPlant().getId();
    }

    @Override
    public long getParentId() {
        return this.data.getProductionUnitClass().getId();
    }
}
