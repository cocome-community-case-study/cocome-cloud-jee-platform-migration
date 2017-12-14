package org.cocome.cloud.web.data.enterprisedata;

import org.cocome.cloud.web.data.ViewData;
import org.cocome.tradingsystem.inventory.application.plant.recipe.RecipeNodeTO;

/**
 * UI specific wrapper objects for {@link RecipeNodeTO}
 *
 * @author Rudolf Biczok
 */
public class RecipeNodeViewData extends ViewData<RecipeNodeTO> {

    public RecipeNodeViewData(RecipeNodeTO data) {
        super(data);
    }

    @Override
    public long getParentId() {
        return this.data.getRecipe().getId();
    }
}
