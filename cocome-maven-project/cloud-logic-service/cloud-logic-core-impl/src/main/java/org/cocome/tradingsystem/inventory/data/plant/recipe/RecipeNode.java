package org.cocome.tradingsystem.inventory.data.plant.recipe;

import org.cocome.tradingsystem.inventory.data.enterprise.IEnterpriseQuery;
import org.cocome.tradingsystem.util.exception.NotInDatabaseException;

import javax.annotation.PostConstruct;
import javax.enterprise.context.Dependent;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import java.io.Serializable;

@Dependent
public class RecipeNode implements IRecipeNode, Serializable {
    private static final long serialVersionUID = 1L;

    private long id;
    private IRecipe recipe;
    private IRecipeOperation operation;

    private long recipeId;
    private long operationId;

    @Inject
    private Instance<IEnterpriseQuery> enterpriseQueryInstance;

    private IEnterpriseQuery enterpriseQuery;

    @PostConstruct
    public void initPlant() {
        enterpriseQuery = enterpriseQueryInstance.get();
        recipe = null;
        operation = null;
    }

    @Override
    public long getId() {
        return this.id;
    }

    @Override
    public void setId(final long id) {
        this.id = id;
    }

    @Override
    public IRecipe getRecipe() throws NotInDatabaseException {
        if (recipe == null) {
            recipe = enterpriseQuery.queryRecipeByID(recipeId);
        }
        return recipe;
    }

    @Override
    public void setRecipe(IRecipe recipe) {
        this.recipe = recipe;
    }
    @Override
    public long getRecipeId() {
        return recipeId;
    }

    @Override
    public void setRecipeId(long recipeId) {
        this.recipeId = recipeId;
    }


    @Override
    public IRecipeOperation getOperation() throws NotInDatabaseException {
        if (operation == null) {
            operation = enterpriseQuery.queryRecipeOperationById(operationId);
        }
        return operation;
    }

    @Override
    public void setOperation(IRecipeOperation operation) {
        this.operation = operation;
    }

    @Override
    public long getOperationId() {
        return operationId;
    }

    @Override
    public void setOperationId(long operationId) {
        this.operationId = operationId;
    }
}