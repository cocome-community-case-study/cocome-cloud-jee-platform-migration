/**
 *
 */
package org.cocome.tradingsystem.inventory.data.plant;

import org.cocome.tradingsystem.inventory.application.plant.PlantTO;
import org.cocome.tradingsystem.inventory.application.plant.PlantWithEnterpriseTO;
import org.cocome.tradingsystem.util.exception.NotInDatabaseException;

/**
 * @author Rudolf Bictok
 */
public interface IPlantDataFactory {
    IPlant convertToPlant(PlantTO plantTO);

    PlantTO fillPlantTO(IPlant plant);

    IPlant getNewPlant();

    PlantWithEnterpriseTO fillPlantWithEnterpriseTO(
            IPlant plant) throws NotInDatabaseException;
}
