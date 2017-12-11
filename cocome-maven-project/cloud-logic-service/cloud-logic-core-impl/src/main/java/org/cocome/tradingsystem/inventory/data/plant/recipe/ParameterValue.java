package org.cocome.tradingsystem.inventory.data.plant.recipe;

import org.cocome.tradingsystem.inventory.data.enterprise.IEnterpriseQuery;
import org.cocome.tradingsystem.inventory.data.plant.parameter.IParameter;
import org.cocome.tradingsystem.util.exception.NotInDatabaseException;

import javax.annotation.PostConstruct;
import javax.enterprise.context.Dependent;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import java.io.Serializable;

/**
 * Holds a value for a particular {@link IParameter}
 *
 * @author Rudolf Biczok
 */
@Dependent
public class ParameterValue implements Serializable, IParameterValue {

    private static final long serialVersionUID = -2577328715744776645L;

    private long id;
    private String value;
    private IParameter parameter;
    private IRecipeOperationOrderEntry orderEntry;
    private long parameterId;
    private long orderEntryId;

    @Inject
    private Instance<IEnterpriseQuery> enterpriseQueryInstance;

    private IEnterpriseQuery enterpriseQuery;

    @PostConstruct
    public void initPlant() {
        enterpriseQuery = enterpriseQueryInstance.get();
        parameter = null;
    }

    @Override
    public long getId() {
        return id;
    }

    @Override
    public void setId(long id) {
        this.id = id;
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public IRecipeOperationOrderEntry getOrderEntry() throws NotInDatabaseException {
        return this.orderEntry;
    }

    @Override
    public void setOrderEntry(IRecipeOperationOrderEntry orderEntry) {
        this.orderEntry = orderEntry;
    }

    @Override
    public long getOrderEntryId() {
        return orderEntryId;
    }

    @Override
    public void setOrderEntryId(long orderEntryId) {
        this.orderEntryId = orderEntryId;
    }

    @Override
    public IParameter getParameter() throws NotInDatabaseException {
        if (parameter == null) {
            parameter = enterpriseQuery.queryParameterById(parameterId);
        }
        return parameter;
    }

    @Override
    public void setParameter(IParameter parameter) {
        this.parameter = parameter;
    }

    @Override
    public long getParameterId() {
        return parameterId;
    }

    @Override
    public void setParameterId(long parameterId) {
        this.parameterId = parameterId;
    }
}
