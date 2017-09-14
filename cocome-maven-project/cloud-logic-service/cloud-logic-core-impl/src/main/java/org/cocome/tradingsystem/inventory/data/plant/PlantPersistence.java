package org.cocome.tradingsystem.inventory.data.plant;

import org.cocome.tradingsystem.inventory.data.persistence.AbstractPersistence;

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
                AbstractPersistence.SEPARATOR +
                encodeString(entity.getName()) +
                AbstractPersistence.SEPARATOR +
                encodeString(entity.getLocation());
    }

    @Override
    protected String convertToSubmittableContentWithId(IPlant entity) {
        return String.valueOf(entity.getEnterpriseId()) +
                AbstractPersistence.SEPARATOR +
                entity.getId() +
                AbstractPersistence.SEPARATOR +
                encodeString(entity.getName()) +
                AbstractPersistence.SEPARATOR +
                encodeString(entity.getLocation());
    }

    @Override
    protected String getContentHeader(IPlant entity) {
        return "TradingEnterpriseId;PlantName;PlantLocation";
    }

    @Override
    protected String getContentHeaderWithId(IPlant entity) {
        return "TradingEnterpriseId;PlantId;PlantName;PlantLocation";
    }

    @Override
    protected String getEntityName(IPlant entity) {
        return "Plant";
    }
}
