package org.cocome.tradingsystem.inventory.data.plant;

import org.apache.log4j.Logger;
import org.cocome.tradingsystem.remote.access.connection.IBackendQuery;
import org.cocome.tradingsystem.remote.access.connection.QueryParameterEncoder;
import org.cocome.tradingsystem.remote.access.parsing.IBackendConversionHelper;
import org.cocome.tradingsystem.util.exception.NotInDatabaseException;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.List;
import java.util.NoSuchElementException;


/**
 * The objects returned will only have their basic datatype attributes filled.
 *
 * @author Rudolf Biczok
 */
@Stateless
@Local(IPlantQuery.class)
public class EnterprisePlantQueryProvider implements IPlantQuery {

    // TODO either cache the retrieved objects or provide faster queries which
    // return objects with only the simple attribute types set and other queries which
    // query all attributes of the objects

    private static final Logger LOG = Logger.getLogger(EnterprisePlantQueryProvider.class);

    @Inject
    IBackendQuery backendConnection;

    @Inject
    IBackendConversionHelper csvHelper;

    @Override
    public IPlant queryPlant(String name, String location) {
        name = QueryParameterEncoder.encodeQueryString(name);
        location = QueryParameterEncoder.encodeQueryString(location);
        String locationQuery = "*";

        if (!location.equals("")) {
            locationQuery = location;
        }

        List<IPlant> plants = (List<IPlant>) csvHelper.getPlants(
                backendConnection.getEntity("Plant", "name=LIKE%20'" + name + "';Plant.location=LIKE%20'" + locationQuery + "'"));

        if (plants.size() > 1) {
            LOG.warn("More than one plant with name " + name +
                    " and location " + location + " was found!");
        }

        try {
            return plants.get(0);
        } catch (IndexOutOfBoundsException e) {
            return null;
        }
    }

    @Override
    public IPlant queryPlantById(long plantId) throws NotInDatabaseException {
        try {
            return csvHelper.getPlants(
                    backendConnection.getEntity("Plant", "id==" + plantId)).iterator().next();
        } catch (NoSuchElementException e) {
            throw new NotInDatabaseException(
                    "Plant with ID " + plantId + " could not be found!");
        }
    }
}
