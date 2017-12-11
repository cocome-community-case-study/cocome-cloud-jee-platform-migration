package org.cocome.tradingsystem.inventory.application.plant.expression;

/**
 * Implementation of {@link PUInstruction}, no equivalent in the database
 *
 * @author Rudolf Biczok
 */
public class PUInstruction {

    private final String pucName;
    private final String operationId;

    /**
     * Canonical constructor
     *
     * @param pucName     the production unit class name
     * @param operationId the id of the operation to execute
     */
    PUInstruction(String pucName, String operationId) {
        this.pucName = pucName;
        this.operationId = operationId;
    }

    /**
     * @return the name of the production unit class
     */
    public String getPUCName() {
        return pucName;
    }

    /**
     * @return returns the operation id
     */
    public String getOperationId() {
        return this.operationId;
    }
}
