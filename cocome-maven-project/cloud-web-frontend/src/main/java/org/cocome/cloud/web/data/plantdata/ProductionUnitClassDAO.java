package org.cocome.cloud.web.data.plantdata;

import org.apache.log4j.Logger;
import org.cocome.cloud.logic.stub.IPlantManager;
import org.cocome.cloud.logic.stub.NotInDatabaseException_Exception;
import org.cocome.cloud.logic.webservice.StreamUtil;
import org.cocome.cloud.web.connector.plantconnector.PlantQuery;
import org.cocome.tradingsystem.inventory.application.plant.productionunit.ProductionUnitClassTO;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Named
@ApplicationScoped
public class ProductionUnitClassDAO {
    private static final Logger LOG = Logger.getLogger(ProductionUnitClassDAO.class);

    @Inject
    private PlantQuery plantQuery;

    public List<ProductionUnitClassViewData> queryPUCs(@NotNull PlantViewData plant)
            throws NotInDatabaseException_Exception {
        LOG.debug("Querying production unit classes");

        final IPlantManager plantManager = plantQuery.lookupPlantManager(plant.getID());
        final List<ProductionUnitClassTO> pucTOs = plantManager.queryProductionUnitClassesByPlantID(plant.getID());
        final List<ProductionUnitClassViewData> pucs = new ArrayList<>(pucTOs.size());
        for (final ProductionUnitClassTO pucTO : pucTOs) {
            final List<ProductionUnitOperationViewData> operations =
                    StreamUtil.ofNullable(plantManager.
                            queryProductionUnitOperationsByProductionUnitClassID(pucTO.getId()))
                            .map(ProductionUnitOperationViewData::new).collect(Collectors.toList());
            pucs.add(new ProductionUnitClassViewData(pucTO, operations));
        }
        return pucs;
    }
}
