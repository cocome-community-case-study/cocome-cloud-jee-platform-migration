package org.cocome.cloud.web.data.storedata;

import org.apache.log4j.Logger;
import org.cocome.tradingsystem.inventory.application.store.ProductTO;
import org.cocome.tradingsystem.inventory.application.store.ProductWithStockItemTO;
import org.cocome.tradingsystem.inventory.application.store.ProductWithSupplierTO;
import org.cocome.tradingsystem.inventory.application.store.StockItemTO;
import org.cocome.tradingsystem.inventory.application.store.SupplierTO;

/**
 * Wraps the {@link ProductTO} and {@link StockItemTO} objects received from 
 * the backend to better accommodate the needs of the UI.
 * 
 * @author Tobias Pöppke
 * @author Robert Heinrich
 */
public class ProductWrapper {
	private static final Logger LOG = Logger.getLogger(ProductWrapper.class);
	private ProductTO product;
	private StockItemTO stockItem;
	
	// TODO should be included in the database product
	private String description;
	
	private StoreViewData originStore;
	
	private boolean editingEnabled = false;
	
	private double newSalesPrice;
	private long newAmount;
	private long newMinAmount;
	private long newMaxAmount;
	
	
	private boolean inCurrentOrder = false;
	
	public ProductWrapper(ProductTO product) {
		this.product = product;
	}
	
	public ProductWrapper(ProductTO product, StockItemTO stockItem, StoreViewData originStore) {
		this(product);
		this.stockItem = stockItem;
		this.originStore = originStore;
		this.newSalesPrice = stockItem.getSalesPrice();
		this.newAmount = stockItem.getAmount();
		this.newMaxAmount = stockItem.getMaxStock();
		this.newMinAmount = stockItem.getMinStock();
	}
	
	public void setStockItem(StockItemTO stockItem) {
		this.stockItem = stockItem;
	}
	
	public String getName() {
		return product.getName();
	}

	public double getSalesPrice() {
		return stockItem != null ? stockItem.getSalesPrice() : -1;
	}

	public String getDescription() {
		return description;
	}

	public long getAmount() {
		return stockItem != null ? stockItem.getAmount() : -1;
	}

	public long getBarcode() {
		return product.getBarcode();
	}
	
	public long getID() {
		return product.getId();
	}

	public StoreViewData getOriginStore() {
		return originStore;
	}
	
	public void setOriginStore(StoreViewData store) {
		if (store != null) {
			this.originStore = store;
		}
	}
	
	public ProductTO getProductTO() {
		return product;
	}
	
	public StockItemTO getStockItemTO() {
		return stockItem;
	}

	public boolean isEditingEnabled() {
		return editingEnabled;
	}

	public void setEditingEnabled(boolean editingEnabled) {
		LOG.debug(String.format("Editing enabled set to %b for item %s.", editingEnabled, product.getName()));
		this.editingEnabled = editingEnabled;
	}
	
	public void resetEdit() {
		newSalesPrice = stockItem.getSalesPrice();
		newAmount = stockItem.getAmount();
		newMaxAmount = stockItem.getMaxStock();
		newMinAmount = stockItem.getMinStock();
		setEditingEnabled(false);
	}
	
	public void setNewSalesPrice(double newPrice) {
		LOG.debug(String.format("New sales price set to %f", newPrice));
		newSalesPrice = newPrice;
	}
	
	public double getNewSalesPrice() {
		return newSalesPrice;
	}
	
	public void submitEdit() {
		LOG.debug(String.format("Setting sales price of %s to %f", product.getName(), newSalesPrice));
		stockItem.setSalesPrice(newSalesPrice);
		stockItem.setAmount(newAmount);
		stockItem.setMaxStock(newMaxAmount);
		stockItem.setMinStock(newMinAmount);
		setEditingEnabled(false);
	}

	public boolean isInCurrentOrder() {
		return inCurrentOrder;
	}

	public void setInCurrentOrder(boolean inCurrentOrder) {
		this.inCurrentOrder = inCurrentOrder;
	}
	
	private static void fillProductTO(ProductTO productTO, ProductWrapper product) {
		productTO.setBarcode(product.getBarcode());
		productTO.setId(product.getID());
		productTO.setName(product.getName());
		productTO.setPurchasePrice(product.getProductTO().getPurchasePrice());
	}
	
	public static ProductWithSupplierTO convertToProductTO(ProductWrapper product) {
		ProductWithSupplierTO productTO = new ProductWithSupplierTO();
		fillProductTO(productTO, product);
		productTO.setSupplierTO(new SupplierTO());
		return productTO;
	}
	
	public static ProductWithStockItemTO convertToProductWithStockItemTO(ProductWrapper product) {
		ProductWithStockItemTO productTO = new ProductWithStockItemTO();
		fillProductTO(productTO, product);
		
		StockItemTO stockItemTO = product.getStockItemTO();
		
		if (stockItemTO == null) {
			stockItemTO = getNewStockItemTO();
		}
		productTO.setStockItemTO(stockItemTO);
		return productTO;
	}

	private static StockItemTO getNewStockItemTO() {
		StockItemTO stockItemTO;
		stockItemTO = new StockItemTO();
		stockItemTO.setAmount(0);
		stockItemTO.setIncomingAmount(0);
		stockItemTO.setMaxStock(0);
		stockItemTO.setMinStock(0);
		stockItemTO.setSalesPrice(0.0);
		return stockItemTO;
	}

	public long getNewAmount() {
		return newAmount;
	}

	public void setNewAmount(long newAmount) {
		this.newAmount = newAmount;
	}

	public long getNewMinAmount() {
		return newMinAmount;
	}

	public void setNewMinAmount(long newMinAmount) {
		this.newMinAmount = newMinAmount;
	}

	public long getNewMaxAmount() {
		return newMaxAmount;
	}

	public void setNewMaxAmount(long newMaxAmount) {
		this.newMaxAmount = newMaxAmount;
	}
}
