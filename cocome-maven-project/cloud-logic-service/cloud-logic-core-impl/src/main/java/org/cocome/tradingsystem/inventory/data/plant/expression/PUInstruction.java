package org.cocome.tradingsystem.inventory.data.plant.expression;

import org.cocome.tradingsystem.inventory.data.plant.productionunit.IProductionUnitClass;

/**
 * Implementation of {@link IPUInstruction}, no equivalent in the database
 *
 * @author Rudolf Biczok
 */
public class PUInstruction implements IPUInstruction {

    private final IProductionUnitClass puc;
    private final String operationId;

    /**
     * Canonical constructor
     *
     * @param puc         the production unit class
     * @param operationId the id of the operation to execute
     */
    public PUInstruction(IProductionUnitClass puc, String operationId) {
        this.puc = puc;
        this.operationId = operationId;
    }

    @Override
    public IProductionUnitClass getPUC() {
        return puc;
    }

    @Override
    public String getOperationId() {
        return this.operationId;
    }
}
