package org.cocome.tradingsystem.inventory.data.plant.recipe;

import org.cocome.tradingsystem.inventory.data.enterprise.IEnterpriseQuery;
import org.cocome.tradingsystem.inventory.data.plant.parameter.IPlantOperationParameter;
import org.cocome.tradingsystem.util.exception.NotInDatabaseException;

import javax.annotation.PostConstruct;
import javax.enterprise.context.Dependent;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import java.io.Serializable;

/**
 * Holds a value for a particular {@link IPlantOperationParameter}
 *
 * @author Rudolf Biczok
 */
@Dependent
public class PlantOperationParameterValue implements Serializable, IPlantOperationParameterValue {

    private static final long serialVersionUID = -2577328715744776645L;

    private long id;
    private String value;
    private IPlantOperationParameter parameter;
    private long parameterId;

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
    public IPlantOperationParameter getParameter() throws NotInDatabaseException {
        if (parameter == null) {
            parameter = enterpriseQuery.queryPlantOperationParameterById(parameterId);
        }
        return parameter;
    }

    @Override
    public void setParameter(IPlantOperationParameter parameter) {
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
