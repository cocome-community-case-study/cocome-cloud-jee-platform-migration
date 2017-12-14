package org.cocome.cloud.web.data.enterprisedata;

import org.cocome.cloud.web.data.ViewData;
import org.cocome.tradingsystem.inventory.application.plant.recipe.ParameterInteractionTO;

/**
 * UI specific wrapper objects for {@link ParameterInteractionTO}
 *
 * @author Rudolf Biczok
 */
public class ParameterInteractionViewData extends ViewData<ParameterInteractionTO> {

    public ParameterInteractionViewData(ParameterInteractionTO data) {
        super(data);
    }

    @Override
    public long getParentId() {
        return this.data.getRecipe().getId();
    }
}
