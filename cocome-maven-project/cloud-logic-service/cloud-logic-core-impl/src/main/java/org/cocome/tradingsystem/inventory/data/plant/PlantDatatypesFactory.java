package org.cocome.tradingsystem.inventory.data.plant;

import org.apache.log4j.Logger;
import org.cocome.tradingsystem.inventory.application.plant.PlantTO;
import org.cocome.tradingsystem.inventory.data.enterprise.IEnterpriseDataFactory;
import org.cocome.tradingsystem.util.exception.NotInDatabaseException;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.inject.Provider;

@Dependent
class PlantDatatypesFactory implements IPlantDataFactory {
    private static final Logger LOG = Logger.getLogger(PlantDatatypesFactory.class);

    @Inject
    Provider<Plant> plantProvider;

    @Inject
    IEnterpriseDataFactory enterpriseDatatypes;

    @Override
    public IPlant getNewPlant() {
        return plantProvider.get();
    }

    @Override
    public IPlant convertToPlant(PlantTO storeTO) {
        IPlant store = getNewPlant();
        store.setName(storeTO.getName());
        store.setLocation(storeTO.getLocation());
        store.setId(storeTO.getId());
        return store;
    }

    @Override
    public PlantTO fillPlantTO(IPlant store)
            throws NotInDatabaseException {
        final PlantTO result = new PlantTO();
        result.setId(store.getId());
        result.setName(store.getName());
        result.setLocation(store.getLocation());
        result.setEnterpriseTO(enterpriseDatatypes.fillEnterpriseTO(store.getEnterprise()));

        LOG.debug(String.format("Got plant with id %d, name %s and enterprise %s.", result.getId(), result.getName(), result.getEnterpriseTO().getName()));

        return result;
    }

}
