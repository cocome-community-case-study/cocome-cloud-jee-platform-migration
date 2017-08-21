package org.cocome.tradingsystem.inventory.data.plant;

import org.cocome.tradingsystem.inventory.data.persistence.IPersistence;

import javax.ejb.Local;

/**
 * Persistence class for {@link IPlant} instances
 * @author Rudolf Biczok
 */
@Local
public interface IPlantPersistence extends IPersistence<IPlant> {
}
