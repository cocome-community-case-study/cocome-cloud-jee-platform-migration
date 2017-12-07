package org.cocome.cloud.web.data.plantdata;

import org.apache.log4j.Logger;
import org.cocome.cloud.logic.stub.IPlantManager;
import org.cocome.cloud.logic.stub.NotInDatabaseException_Exception;
import org.cocome.cloud.logic.webservice.StreamUtil;
import org.cocome.cloud.web.connector.plantconnector.PlantQuery;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.stream.Collectors;

@Named
@ApplicationScoped
public class ProductionUnitClassDAO {
    private static final Logger LOG = Logger.getLogger(ProductionUnitClassDAO.class);

    @Inject
    private PlantQuery plantQuery;

    public List<PUCWrapper> queryAll(@NotNull PlantViewData plant) throws NotInDatabaseException_Exception {
        LOG.debug("Querying production unit classes");

        final IPlantManager plantManager = plantQuery.lookupPlantManager(plant.getID());
        return StreamUtil.ofNullable(plantManager.queryProductionUnitClassesByPlantID(plant.getID()))
                .map(e -> new PUCWrapper(e, plant)).collect(Collectors.toList());
    }
}
