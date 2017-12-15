package org.cocome.cloud.web.data.enterprisedata;

import org.cocome.cloud.web.data.ViewData;
import org.cocome.tradingsystem.inventory.application.store.EnterpriseTO;

import javax.validation.constraints.NotNull;

/**
 * Holds information on an enterprise.
 *
 * @author Tobias Pöppke
 * @author Robert Heinrich
 */
public class EnterpriseViewData extends ViewData<EnterpriseTO> {

    public EnterpriseViewData(final long id, final String name) {
        super(new EnterpriseTO());
        this.data.setId(id);
        this.data.setName(name);
    }

    public EnterpriseViewData(EnterpriseTO data) {
        super(data);
    }

    @Override
    public long getParentId() {
        return 0;
    }

    public void setData(EnterpriseTO data) {
        this.data = data;
    }

    public long getId() {
        return this.data.getId();
    }

    public void setId(long id) {
        this.data.setId(id);
    }

    public String getName() {
        return this.data.getName();
    }

    public void setName(String name) {
        this.setName(name);
    }

    /**
     * Creates a new EnterpriseTO object from an existing Enterprise object.
     *
     * @param enterprise
     * @return
     */
    public static EnterpriseTO createEnterpriseTO(@NotNull EnterpriseViewData enterprise) {
        EnterpriseTO enterpriseTO = new EnterpriseTO();
        enterpriseTO.setId(enterprise.getId());
        enterpriseTO.setName(enterprise.getName());
        return enterpriseTO;
    }
}
