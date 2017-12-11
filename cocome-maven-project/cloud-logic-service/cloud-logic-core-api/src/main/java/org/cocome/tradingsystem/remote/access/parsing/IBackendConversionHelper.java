package org.cocome.tradingsystem.remote.access.parsing;

import org.cocome.tradingsystem.inventory.data.enterprise.IProduct;
import org.cocome.tradingsystem.inventory.data.enterprise.IProductSupplier;
import org.cocome.tradingsystem.inventory.data.enterprise.ITradingEnterprise;
import org.cocome.tradingsystem.inventory.data.plant.IPlant;
import org.cocome.tradingsystem.inventory.data.plant.parameter.IBooleanParameter;
import org.cocome.tradingsystem.inventory.data.plant.parameter.INominalParameter;
import org.cocome.tradingsystem.inventory.data.plant.parameter.IParameter;
import org.cocome.tradingsystem.inventory.data.plant.productionunit.IProductionUnit;
import org.cocome.tradingsystem.inventory.data.plant.productionunit.IProductionUnitClass;
import org.cocome.tradingsystem.inventory.data.plant.productionunit.IProductionUnitOperation;
import org.cocome.tradingsystem.inventory.data.plant.recipe.*;
import org.cocome.tradingsystem.inventory.data.store.*;
import org.cocome.tradingsystem.inventory.data.usermanager.ICustomer;
import org.cocome.tradingsystem.inventory.data.usermanager.IUser;

import java.util.Collection;

public interface IBackendConversionHelper {
    Collection<IStore> getStores(String input);

    Collection<IPlant> getPlants(String input);

    Collection<ICustomer> getCustomers(String input);

    Collection<IUser> getUsers(String input);

    Collection<ITradingEnterprise> getEnterprises(String input);

    Collection<IStockItem> getStockItems(String input);

    Collection<IProduct> getProducts(String input);

    Collection<IProductSupplier> getProductSuppliers(String input);

    Collection<IProductOrder> getProductOrders(String input);

    Collection<IProductionUnit> getProductionUnit(String unit);

    Collection<IProductionUnitClass> getProductionUnitClasses(String productionUnitClass);

    Collection<IProductionUnitOperation> getProductionUnitOperations(String productionUnitOperation);

    Collection<IEntryPoint> getEntryPoints(String entryPoint);

    Collection<IPlantOperation> getPlantOperation(String plantOperation);

    Collection<IBooleanParameter> getBooleanParameter(String param);

    Collection<INominalParameter> getNominalParameter(String param);

    Collection<IParameter> getParameters(String plantOperationParameter);

    Collection<IEntryPointInteraction> getEntryPointInteraction(String entryPointInteraction);

    Collection<IParameterInteraction> getParameterInteraction(String parameterInteraction);

    Collection<IRecipe> getRecipe(String recipe);

    Collection<IPlantOperationOrder> getPlantOperationOrder(String order);

    Collection<IPlantOperationOrderEntry> getPlantOperationOrderEntry(String orderEntry);

    Collection<IParameterValue> getParameterValue(String parameterValue);

    Collection<IItem> getItem(String item);

    Collection<IOnDemandItem> getOnDemandItem(String onDemandItem);

    Collection<IRecipeOperation> getRecipeOperation(String recipeOperation);

    Collection<IRecipeNode> getRecipeNode(String recipeNode);
}