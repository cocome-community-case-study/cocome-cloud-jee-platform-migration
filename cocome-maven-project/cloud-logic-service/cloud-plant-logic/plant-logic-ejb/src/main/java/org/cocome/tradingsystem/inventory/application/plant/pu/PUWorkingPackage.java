package org.cocome.tradingsystem.inventory.application.plant.pu;

import org.cocome.tradingsystem.inventory.data.plant.productionunit.IProductionUnitClass;

import java.util.List;

/**
 * Encapsulate a set of atomic production unit instructions which are supposed to be executed on
 * a particular {@link IProductionUnitClass}.
 *
 * @author Rudolf Biczok
 */
public class PUWorkingPackage {
    private final IProductionUnitClass puc;
    private final List<String> operations;

    /**
     * Canonical constructor
     *
     * @param puc        the unit class
     * @param operations the operations that have to be executed
     */
    public PUWorkingPackage(final IProductionUnitClass puc, final List<String> operations) {
        this.puc = puc;
        this.operations = operations;
    }

    /**
     * @return the unit class
     */
    public IProductionUnitClass getPUC() {
        return puc;
    }

    /**
     * @return the list of operations
     */
    public List<String> getOperations() {
        return operations;
    }
}
