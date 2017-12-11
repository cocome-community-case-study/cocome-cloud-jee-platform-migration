package org.cocome.tradingsystem.inventory.data.plant.recipe;

import org.cocome.tradingsystem.inventory.data.IIdentifiable;
import org.cocome.tradingsystem.util.exception.NotInDatabaseException;

/**
 * Represents the top-level recipe for producing a custom product.
 *
 * @author Rudolf Biczok
 */
public interface IRecipeNode extends IIdentifiable {

    long getId();

    void setId(final long id);

    IRecipe getRecipe() throws NotInDatabaseException;

    void setRecipe(IRecipe recipe);

    IRecipeOperation getOperation() throws NotInDatabaseException;

    void setOperation(IRecipeOperation operation);

    long getRecipeId();

    void setRecipeId(long recipeId);

    long getOperationId();

    void setOperationId(long operationId);
}