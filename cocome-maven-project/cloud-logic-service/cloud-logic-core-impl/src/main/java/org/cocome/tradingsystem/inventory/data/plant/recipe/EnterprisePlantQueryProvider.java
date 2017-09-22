package org.cocome.tradingsystem.inventory.data.plant.recipe;

import org.apache.log4j.Logger;
import org.cocome.tradingsystem.inventory.data.plant.IPlantQuery;
import org.cocome.tradingsystem.inventory.data.plant.productionunit.IProductionUnitClass;
import org.cocome.tradingsystem.inventory.data.plant.productionunit.IProductionUnitOperation;
import org.cocome.tradingsystem.remote.access.connection.IBackendQuery;
import org.cocome.tradingsystem.remote.access.parsing.IBackendConversionHelper;
import org.cocome.tradingsystem.util.exception.NotInDatabaseException;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.Collection;
import java.util.NoSuchElementException;
import java.util.function.Function;


/**
 * The objects returned will only have their basic datatype attributes filled.
 *
 * @author Tobias Pöppke
 */
@Stateless
@Local(IPlantQuery.class)
public class EnterprisePlantQueryProvider implements IPlantQuery {

    private static final Logger LOG = Logger.getLogger(EnterprisePlantQueryProvider.class);

    @Inject
    private IBackendQuery backendConnection;

    @Inject
    private IBackendConversionHelper csvHelper;

    @Override
    public Collection<IProductionUnitClass> queryProductionUnitClassesByEnterpriseId(long enterpriseID) {
        return csvHelper.getProductionUnitClasses(backendConnection.getEntity(
                "ProductionUnitClass",
                "enterprise.id==" + enterpriseID));
    }

    @Override
    public IProductionUnitClass queryProductionUnitClass(long productionUnitClassID) throws NotInDatabaseException {
        return getSingleEntity(csvHelper::getProductionUnitClasses, "ProductionUnitClass", productionUnitClassID);
    }

    @Override
    public Collection<IProductionUnitOperation> queryProductionUnitOperationsByEnterpriseId(long enterpriseID) {
        return csvHelper.getProductionUnitOperations(backendConnection.getEntity(
                "ProductionUnitOperation",
                "productionUnitClass.enterprise.id==" + enterpriseID));
    }

    @Override
    public IProductionUnitOperation queryProductionUnitOperation(long productionUnitOperationId) throws NotInDatabaseException {
        return getSingleEntity(
                csvHelper::getProductionUnitOperations,
                "ProductionUnitOperation",
                productionUnitOperationId);
    }

    private <T> T getSingleEntity(Function<String, Collection<T>> converter,
                                  String entity,
                                  long entityId) throws NotInDatabaseException {
        try {
            return converter.apply(backendConnection.getEntity(entity, "id==" + entityId)).iterator().next();
        } catch (NoSuchElementException e) {
            throw new NotInDatabaseException(String.format(
                    "No matching entity of type '%s' and id '%d' found in database!",
                    entity, entityId));
        }
    }
}
