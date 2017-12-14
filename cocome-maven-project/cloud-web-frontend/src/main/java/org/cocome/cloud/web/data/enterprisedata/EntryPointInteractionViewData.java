package org.cocome.cloud.web.data.enterprisedata;

import org.cocome.cloud.web.data.ViewData;
import org.cocome.tradingsystem.inventory.application.plant.recipe.EntryPointInteractionTO;

/**
 * UI specific wrapper objects for {@link EntryPointInteractionTO}
 *
 * @author Rudolf Biczok
 */
public class EntryPointInteractionViewData extends ViewData<EntryPointInteractionTO> {

    public EntryPointInteractionViewData(EntryPointInteractionTO data) {
        super(data);
    }

    @Override
    public long getParentId() {
        return this.data.getRecipe().getId();
    }
}
