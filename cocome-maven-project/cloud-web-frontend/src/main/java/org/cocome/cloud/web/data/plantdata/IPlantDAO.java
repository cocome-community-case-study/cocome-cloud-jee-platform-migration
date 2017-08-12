package org.cocome.cloud.web.data.plantdata;

import org.cocome.cloud.logic.stub.NotInDatabaseException_Exception;

import java.util.Collection;

/**
 * Interface for accessing and manipulating plant objects
 *
 * @author Rudolf Biczok
 */
public interface IPlantDAO {
    Collection<PlantViewData> getPlantsInEnterprise(long enterpriseID) throws NotInDatabaseException_Exception;

    PlantViewData getPlantByID(long plantID) throws NotInDatabaseException_Exception;
}
