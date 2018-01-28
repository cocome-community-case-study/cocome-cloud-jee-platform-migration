package org.cocome.cloud.web.data.enterprisedata;

import org.cocome.cloud.web.data.ViewData;
import org.cocome.tradingsystem.inventory.application.plant.recipe.ProductionOrderTO;
import org.cocome.tradingsystem.inventory.application.plant.recipe.RecipeNodeTO;

/**
 * UI specific wrapper objects for {@link ProductionOrderTO}
 *
 * @author Rudolf Biczok
 */
public class ProductionOrderViewData extends ViewData<ProductionOrderTO> {

    public ProductionOrderViewData(ProductionOrderTO data) {
        super(data);
    }

    @Override
    public long getParentId() {
        return this.data.getEnterprise().getId();
    }
}
