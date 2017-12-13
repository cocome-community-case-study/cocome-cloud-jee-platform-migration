package org.cocome.cloud.web.data.enterprisedata;

import org.cocome.cloud.web.data.ViewData;
import org.cocome.tradingsystem.inventory.application.plant.parameter.BooleanParameterTO;
import org.cocome.tradingsystem.inventory.application.plant.recipe.EntryPointTO;

/**
 * UI specific wrapper objects for {@link EntryPointTO}
 *
 * @author Rudolf Biczok
 */
public class BooleanParameterViewData extends ViewData<BooleanParameterTO> {

    public BooleanParameterViewData(BooleanParameterTO data) {
        super(data);
    }

    @Override
    public long getParentId() {
        return this.data.getOperation().getId();
    }
}
