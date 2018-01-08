package org.cocome.cloud.web.data.enterprisedata;

import org.cocome.cloud.web.data.ViewData;
import org.cocome.tradingsystem.inventory.application.plant.parameter.BooleanParameterTO;
import org.cocome.tradingsystem.inventory.application.plant.parameter.ParameterTO;
import org.cocome.tradingsystem.inventory.application.plant.recipe.EntryPointTO;

/**
 * UI specific wrapper objects for {@link ParameterTO}
 *
 * @author Rudolf Biczok
 */
public class ParameterViewData extends ViewData<ParameterTO> {

    public ParameterViewData(ParameterTO data) {
        super(data);
    }

    @Override
    public long getParentId() {
        return this.data.getOperation().getId();
    }
}
