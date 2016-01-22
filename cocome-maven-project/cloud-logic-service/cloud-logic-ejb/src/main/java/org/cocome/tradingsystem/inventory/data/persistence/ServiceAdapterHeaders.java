package org.cocome.tradingsystem.inventory.data.persistence;

public final class ServiceAdapterHeaders {
	public static final String PRODUCTORDER_HEADER = "ProductOrderId;StoreId;ProductBarcode;"
			+ "OrderDeliveryDate;OrderOrderingDate;OrderAmount";
	
	public static final String STOCKITEM_HEADER = "StoreId;ProductBarcode;StockItemMinStock;"
			+ "StockItemMaxStock;StockItemIncomingAmount;StockItemAmount;StockItemSalesPrice";
	
	public static final String ENTERPRISE_CREATE_HEADER = "EnterpriseName";
	
	public static final String ENTERPRISE_UPDATE_HEADER = "EnterpriseId;EnterpriseName";
	
	public static final String STORE_CREATE_HEADER = "EnterpriseName;StoreName;StoreLocation";
	
	public static final String STORE_UPDATE_HEADER = "StoreId;StoreName;StoreLocation";
	
	public static final String PRODUCT_HEADER = "ProductBarcode;ProductName;ProductPurchasePrice";
	
	public static final String PRODUCTSUPPLIER_CREATE_HEADER = "ProductSupplierName";
	
	public static final String PRODUCTSUPPLIER_UPDATE_HEADER = "ProductSupplierId;ProductSupplierName";
	
	public static final String SEPARATOR = ";";
}
