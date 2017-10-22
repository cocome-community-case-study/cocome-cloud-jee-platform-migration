package org.cocome.tradingsystem.inventory.data.plant.expression;

import org.cocome.tradingsystem.inventory.data.plant.productionunit.IProductionUnitClass;

/**
 * This class is used to encapsulate the necessary information needed to execute one single instruction on a particular
 * production unit class
 *
 * @author Rudolf Biczok
 */
public interface IPUInstruction {

    /**
     * @return returns the production unit class
     */
    IProductionUnitClass getPUC();

    /**
     * @return returns the id of the operation to execute (not database id)
     */
    String getOperationId();

}
