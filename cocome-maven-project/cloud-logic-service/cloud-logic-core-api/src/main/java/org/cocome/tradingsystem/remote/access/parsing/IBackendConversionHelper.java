package org.cocome.tradingsystem.remote.access.parsing;

import org.cocome.tradingsystem.inventory.data.enterprise.ICustomProduct;
import org.cocome.tradingsystem.inventory.data.enterprise.IProduct;
import org.cocome.tradingsystem.inventory.data.enterprise.IProductSupplier;
import org.cocome.tradingsystem.inventory.data.enterprise.ITradingEnterprise;
import org.cocome.tradingsystem.inventory.data.enterprise.parameter.IBooleanCustomProductParameter;
import org.cocome.tradingsystem.inventory.data.enterprise.parameter.ICustomProductParameter;
import org.cocome.tradingsystem.inventory.data.enterprise.parameter.INorminalCustomProductParameter;
import org.cocome.tradingsystem.inventory.data.plant.IPlant;
import org.cocome.tradingsystem.inventory.data.plant.expression.IConditionalExpression;
import org.cocome.tradingsystem.inventory.data.plant.expression.IExpression;
import org.cocome.tradingsystem.inventory.data.plant.parameter.IBooleanPlantOperationParameter;
import org.cocome.tradingsystem.inventory.data.plant.parameter.INorminalPlantOperationParameter;
import org.cocome.tradingsystem.inventory.data.plant.parameter.IPlantOperationParameter;
import org.cocome.tradingsystem.inventory.data.plant.productionunit.IProductionUnitClass;
import org.cocome.tradingsystem.inventory.data.plant.productionunit.IProductionUnitOperation;
import org.cocome.tradingsystem.inventory.data.plant.recipe.*;
import org.cocome.tradingsystem.inventory.data.store.IProductOrder;
import org.cocome.tradingsystem.inventory.data.store.IStockItem;
import org.cocome.tradingsystem.inventory.data.store.IStore;
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

    Collection<IProductionUnitClass> getProductionUnitClasses(String productionUnitClass);

    Collection<IProductionUnitOperation> getProductionUnitOperations(String productionUnitOperation);

    Collection<IEntryPoint> getEntryPoints(String entryPoint);

    Collection<IConditionalExpression> getConditionalExpressions(String conditionalExpression);

    Collection<ICustomProduct> getCustomProducts(String customProduct);

    Collection<IBooleanCustomProductParameter> getBooleanCustomProductParameter(String param);

    Collection<INorminalCustomProductParameter> getNorminalCustomProductParameter(String param);

    Collection<ICustomProductParameter> getCustomProductParameter(String customProductParameter);

    Collection<IPlantOperation> getPlantOperation(String plantOperation);

    Collection<IBooleanPlantOperationParameter> getBooleanPlantOperationParameter(String param);

    Collection<INorminalPlantOperationParameter> getNorminalPlantOperationParameter(String param);

    Collection<IPlantOperationParameter> getPlantOperationParameters(String plantOperationParameter);

    Collection<IEntryPointInteraction> getEntryPointInteraction(String entryPointInteraction);

    Collection<IParameterInteraction> getParameterInteraction(String parameterInteraction);

    Collection<IRecipe> getRecipe(String recipe);

    Collection<IExpression> getExpressions(String expression);

    Collection<IPlantOperationOrder> getPlantOperationOrder(String order);

    Collection<IPlantOperationOrderEntry> getPlantOperationOrderEntry(String orderEntry);

    Collection<IPlantOperationParameterValue> getPlantOperationParameterValue(String parameterValue);
}