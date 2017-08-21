package org.cocome.tradingsystem.inventory.data.plant;

import org.cocome.tradingsystem.inventory.data.persistence.AbstractPersistence;
import org.cocome.tradingsystem.inventory.data.persistence.ServiceAdapterHeaders;

import javax.ejb.Local;
import javax.ejb.Stateless;

/**
 * Handles Create, Update, Delete operations for plant instances
 * @author Rudolf Biczok
 */
@Stateless
@Local(IPlantPersistence.class)
public class PlantPersistence extends AbstractPersistence<IPlant> implements IPlantPersistence {
    @Override
    protected String convertToSubmittableContent(IPlant entity) {
        return String.valueOf(entity.getEnterpriseId()) +
                ServiceAdapterHeaders.SEPARATOR +
                encodeString(entity.getName()) +
                ServiceAdapterHeaders.SEPARATOR +
                encodeString(entity.getLocation());
    }

    @Override
    protected String convertToSubmittableContentWithId(IPlant entity) {
        return String.valueOf(entity.getEnterpriseId()) +
                ServiceAdapterHeaders.SEPARATOR +
                entity.getId() +
                ServiceAdapterHeaders.SEPARATOR +
                encodeString(entity.getName()) +
                ServiceAdapterHeaders.SEPARATOR +
                encodeString(entity.getLocation());
    }

    @Override
    protected String getContentHeader() {
        return "TradingEnterpriseId;PlantName;PlantLocation";
    }

    @Override
    protected String getContentHeaderWithId() {
        return "TradingEnterpriseId;PlantId;PlantName;PlantLocation";
    }

    @Override
    protected String getEntityName() {
        return "Plant";
    }
}
