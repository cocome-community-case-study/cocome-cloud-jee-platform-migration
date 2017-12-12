package org.cocome.cloud.web.data.plantdata;

import org.cocome.cloud.web.data.ViewData;
import org.cocome.tradingsystem.inventory.application.plant.productionunit.ProductionUnitTO;

import java.util.List;

/**
 * UI specific wraper objects for {@link ProductionUnitTO}
 *
 * @author Rudolf Biczok
 */
public class ProductionUnitViewData extends ViewData<ProductionUnitTO> {

    public ProductionUnitViewData(ProductionUnitTO data) {
        super(data);
    }

    @Override
    public long getParentId() {
        return this.data.getPlant().getId();
    }
}
