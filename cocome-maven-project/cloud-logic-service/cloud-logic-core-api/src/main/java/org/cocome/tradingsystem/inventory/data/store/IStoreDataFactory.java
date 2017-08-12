/**
 *
 */
package org.cocome.tradingsystem.inventory.data.store;

import org.cocome.tradingsystem.inventory.application.store.*;
import org.cocome.tradingsystem.util.exception.NotInDatabaseException;

/**
 * @author Tobias Pöppke
 * @author Robert Heinrich
 */
public interface IStoreDataFactory {
    IOrderEntry getNewOrderEntry();

    IProductOrder getNewProductOrder();

    IStockItem getNewStockItem();

    IStore getNewStore();

    IOrderEntry convertToOrderEntry(ComplexOrderEntryTO entryTO);

    ComplexOrderEntryTO fillComplexOrderEntryTO(IOrderEntry entry) throws NotInDatabaseException;

    IProductOrder convertToProductOrder(ComplexOrderTO orderTO);

    ComplexOrderTO fillComplexOrderTO(IProductOrder order) throws NotInDatabaseException;

    IStockItem convertToStockItem(StockItemTO stockItemTO);

    StockItemTO fillStockItemTO(IStockItem stockItem);

    IStore convertToStore(StoreTO storeTO);

    StoreTO fillStoreTO(IStore store);

    OrderEntryTO fillOrderEntryTO(IOrderEntry entry);

    OrderTO fillOrderTO(IProductOrder order);

    ProductWithStockItemTO fillProductWithStockItemTO(IStockItem stockItem);

    ProductWithSupplierAndStockItemTO fillProductWithSupplierAndStockItemTO(
            IStockItem stockItem) throws NotInDatabaseException;

    StoreWithEnterpriseTO fillStoreWithEnterpriseTO(
            IStore store) throws NotInDatabaseException;
}
