package org.cocome.cloud.web;

import org.cocome.cloud.logic.stub.IPlantManager;
import org.cocome.cloud.logic.stub.NotInDatabaseException_Exception;
import org.cocome.cloud.web.connector.plantconnector.PlantQuery;
import org.cocome.cloud.web.data.AbstractDAO;
import org.cocome.tradingsystem.inventory.application.IIdentifiableTO;
import org.cocome.tradingsystem.inventory.application.INameableTO;
import org.cocome.tradingsystem.inventory.data.IIdentifiable;

import javax.inject.Inject;

/**
 * Abstract
 *
 * @author Rudolf Biczok
 */
public abstract class AbstractPlantDAO<
        TTargetContent extends IIdentifiableTO,
        TParentContent extends IIdentifiableTO> extends AbstractDAO<IPlantManager, TTargetContent, TParentContent> {

    @Inject
    private PlantQuery plantQuery;

    @Override
    public IPlantManager createServiceClient(final long target) throws NotInDatabaseException_Exception {
        return this.plantQuery.lookupPlantManager(target);
    }
}
