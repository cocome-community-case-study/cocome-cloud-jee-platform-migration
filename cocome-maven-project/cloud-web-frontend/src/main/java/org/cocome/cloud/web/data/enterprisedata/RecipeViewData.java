package org.cocome.cloud.web.data.enterprisedata;

import org.cocome.cloud.web.data.ViewData;
import org.cocome.tradingsystem.inventory.application.plant.recipe.RecipeOperationTO;
import org.cocome.tradingsystem.inventory.application.plant.recipe.RecipeTO;

/**
 * UI specific wrapper objects for {@link RecipeTO}
 *
 * @author Rudolf Biczok
 */
public class RecipeViewData extends ViewData<RecipeTO> {

    public RecipeViewData(RecipeTO data) {
        super(data);
    }

    @Override
    public long getParentId() {
        return this.data.getEnterprise().getId();
    }
}
