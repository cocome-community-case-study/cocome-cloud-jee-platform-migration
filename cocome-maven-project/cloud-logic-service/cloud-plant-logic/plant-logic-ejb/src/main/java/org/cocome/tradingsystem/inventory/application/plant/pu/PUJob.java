package org.cocome.tradingsystem.inventory.application.plant.pu;

import java.util.List;

/**
 * Represents the data used to handle o job for a specific production unit
 *
 * @author Rudolf Biczok
 */
public class PUJob {
    private final List<String> operations;

    /**
     * Canonical constructor
     *
     * @param operations the list of operations to execute for this job
     */
    PUJob(List<String> operations) {
        this.operations = operations;
    }

    /**
     * @return the list of operations to execute for this job
     */
    public List<String> getOperations() {
        return operations;
    }
}
