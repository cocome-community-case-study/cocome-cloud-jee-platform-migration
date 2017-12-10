package org.cocome.cloud.web.data.plantdata;

import org.cocome.cloud.web.data.ViewData;
import org.cocome.tradingsystem.inventory.application.plant.productionunit.ProductionUnitClassTO;

import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

/**
 * UI specific wraper objects for {@link ProductionUnitClassTO}
 *
 * @author Rudolf Biczok
 */
public class ProductionUnitClassViewData extends ViewData<ProductionUnitClassTO> {

    public ProductionUnitClassViewData(ProductionUnitClassTO data) {
        super(data);
    }

    @Override
    public long getServiceId() {
        return this.data.getPlant().getId();
    }

    @Override
    public long getParentId() {
        return this.getServiceId();
    }
}
