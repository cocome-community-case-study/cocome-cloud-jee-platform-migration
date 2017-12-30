package org.cocome.tradingsystem.inventory.application.production;

import java.io.Serializable;

/**
 * The abstract class for any event time comming from an production unit
 *
 * @author Rudolf Biczok
 */
public class PlantOperationOrderFinishedEvent implements Serializable {

    private static final long serialVersionUID = -1L;

    private final long plantOperationOrderID;

    /**
     * Canonical constructor
     *
     * @param plantOperationOrderID plant operation order
     */
    public PlantOperationOrderFinishedEvent(final long plantOperationOrderID) {
        this.plantOperationOrderID = plantOperationOrderID;
    }

    /**
     * @return returns the id of the finished plant operation order
     */
    public long getPlantOperationOrderID() {
        return plantOperationOrderID;
    }
}
