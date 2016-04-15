/**
 * 
 */
package org.cocome.tradingsystem.inventory.data.store;

import org.cocome.tradingsystem.inventory.application.store.ComplexOrderEntryTO;
import org.cocome.tradingsystem.inventory.application.store.ComplexOrderTO;
import org.cocome.tradingsystem.inventory.application.store.OrderEntryTO;
import org.cocome.tradingsystem.inventory.application.store.OrderTO;
import org.cocome.tradingsystem.inventory.application.store.ProductWithStockItemTO;
import org.cocome.tradingsystem.inventory.application.store.ProductWithSupplierAndStockItemTO;
import org.cocome.tradingsystem.inventory.application.store.StockItemTO;
import org.cocome.tradingsystem.inventory.application.store.StoreTO;
import org.cocome.tradingsystem.inventory.application.store.StoreWithEnterpriseTO;
import org.cocome.tradingsystem.util.exception.NotInDatabaseException;

/**
 * 
 * 
 * @author Tobias Pöppke
 * @author Robert Heinrich
 *
 */
public interface IStoreDataFactory {
	public IOrderEntry getNewOrderEntry();
	
	public IProductOrder getNewProductOrder();
	
	public IStockItem getNewStockItem();
	
	public IStore getNewStore();
	
	public IOrderEntry convertToOrderEntry(ComplexOrderEntryTO entryTO);
	
	public ComplexOrderEntryTO fillComplexOrderEntryTO(IOrderEntry entry) throws NotInDatabaseException;
	
	public IProductOrder convertToProductOrder(ComplexOrderTO orderTO);
	
	public ComplexOrderTO fillComplexOrderTO(IProductOrder order) throws NotInDatabaseException;
	
	public IStockItem convertToStockItem(StockItemTO stockItemTO);
	
	public StockItemTO fillStockItemTO(IStockItem stockItem);
	
	public IStore convertToStore(StoreTO storeTO);
	
	public StoreTO fillStoreTO(IStore store);
	
	public OrderEntryTO fillOrderEntryTO(IOrderEntry entry);
	
	public OrderTO fillOrderTO(IProductOrder order);
	
	public ProductWithStockItemTO fillProductWithStockItemTO(IStockItem stockItem);
	
	public ProductWithSupplierAndStockItemTO fillProductWithSupplierAndStockItemTO(
			IStockItem stockItem) throws NotInDatabaseException;
	
	public StoreWithEnterpriseTO fillStoreWithEnterpriseTO(
			IStore store) throws NotInDatabaseException;
}
