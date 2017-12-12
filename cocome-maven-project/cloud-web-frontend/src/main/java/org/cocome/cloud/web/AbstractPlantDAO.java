package org.cocome.cloud.web;

import org.cocome.cloud.logic.stub.IPlantManager;
import org.cocome.cloud.logic.stub.NotInDatabaseException_Exception;
import org.cocome.cloud.web.connector.plantconnector.PlantQuery;
import org.cocome.cloud.web.data.AbstractDAO;
import org.cocome.cloud.web.frontend.plant.PlantInformation;
import org.cocome.tradingsystem.inventory.application.IIdentifiableTO;

import javax.inject.Inject;

/**
 * Abstract class for data access objects that rely on a plant service
 *
 * @author Rudolf Biczok
 */
public abstract class AbstractPlantDAO<TTargetContent extends IIdentifiableTO>
        extends AbstractDAO<IPlantManager, TTargetContent> {

    @Inject
    private PlantInformation plantInformation;

    @Inject
    private PlantQuery plantQuery;

    @Override
    public IPlantManager createServiceClient() throws NotInDatabaseException_Exception {
        return this.plantQuery.lookupPlantManager(plantInformation.getActivePlantID());
    }
}
