package org.cocome.tradingsystem.inventory.application.production;

import java.io.Serializable;

/**
 * Event class for finished production orders
 *
 * @author Rudolf Biczok
 */
public class ProductionOrderFinishedEvent implements Serializable {

    private static final long serialVersionUID = -1L;

    private final long productionOrderID;

    /**
     * Canonical constructor
     *
     * @param productionOrderID plant operation order
     */
    public ProductionOrderFinishedEvent(final long productionOrderID) {
        this.productionOrderID = productionOrderID;
    }

    /**
     * @return returns the id of the finished plant operation order
     */
    public long getProductionOrderID() {
        return productionOrderID;
    }
}
