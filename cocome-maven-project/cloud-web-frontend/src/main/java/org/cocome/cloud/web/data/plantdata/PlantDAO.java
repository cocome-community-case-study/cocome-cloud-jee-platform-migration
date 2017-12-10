package org.cocome.cloud.web.data.plantdata;

import org.cocome.cloud.logic.stub.NotInDatabaseException_Exception;
import org.cocome.cloud.web.connector.enterpriseconnector.EnterpriseQuery;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.Collection;

@Named
@RequestScoped
public class PlantDAO {

    @Inject
    private EnterpriseQuery enterpriseQuery;

    public Collection<PlantViewData> getPlantsInEnterprise(long enterpriseID) throws NotInDatabaseException_Exception {
        return enterpriseQuery.getPlants(enterpriseID);
    }

    public PlantViewData getPlantByID(long plantID) throws NotInDatabaseException_Exception {
        return enterpriseQuery.getPlantByID(plantID);
    }

}
