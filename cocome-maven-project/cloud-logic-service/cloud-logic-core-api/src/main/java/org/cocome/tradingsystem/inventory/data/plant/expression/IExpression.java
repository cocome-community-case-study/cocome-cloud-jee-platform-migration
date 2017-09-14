package org.cocome.tradingsystem.inventory.data.plant.expression;

import org.cocome.tradingsystem.inventory.data.IIdentifiable;
import org.cocome.tradingsystem.inventory.data.plant.recipe.IPlantOperation;

/**
 * Represents an expression that can be used for plant-local recipes.
 *
 * @author Rudolf Biczok
 */
public interface IExpression extends IIdentifiable {

    /**
     * @return A unique identifier of this Plant.
     */
    @Override
    long getId();

    /**
     * @param id a unique identifier of this Plant
     */
    void setId(final long id);

    /**
     * @return the associated plant operation
     */
    IPlantOperation getPlantOperation();

    /**
     * @param plantOperation the associated plant operation
     */
    void setPlantOperation(IPlantOperation plantOperation);
}
