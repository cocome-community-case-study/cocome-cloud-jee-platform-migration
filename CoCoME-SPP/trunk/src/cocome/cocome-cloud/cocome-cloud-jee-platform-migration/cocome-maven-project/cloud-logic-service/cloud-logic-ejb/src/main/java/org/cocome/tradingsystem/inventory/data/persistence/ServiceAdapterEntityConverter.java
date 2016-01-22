package org.cocome.tradingsystem.inventory.data.persistence;

import org.cocome.tradingsystem.inventory.data.enterprise.Product;
import org.cocome.tradingsystem.inventory.data.enterprise.ProductSupplier;
import org.cocome.tradingsystem.inventory.data.enterprise.TradingEnterprise;
import org.cocome.tradingsystem.inventory.data.store.OrderEntry;
import org.cocome.tradingsystem.inventory.data.store.ProductOrder;
import org.cocome.tradingsystem.inventory.data.store.StockItem;
import org.cocome.tradingsystem.inventory.data.store.Store;

import de.kit.ipd.java.utils.time.TimeUtils;

public class ServiceAdapterEntityConverter {
	public static String getStockItemContent(StockItem stockItem) {
		StringBuilder content = new StringBuilder();
		content.append(stockItem.getStore().getId());
		content.append(ServiceAdapterHeaders.SEPARATOR);
		content.append(stockItem.getProductBarcode());
		content.append(ServiceAdapterHeaders.SEPARATOR);
		content.append(stockItem.getMinStock());
		content.append(ServiceAdapterHeaders.SEPARATOR);
		content.append(stockItem.getMaxStock());
		content.append(ServiceAdapterHeaders.SEPARATOR);
		content.append(stockItem.getIncomingAmount());
		content.append(ServiceAdapterHeaders.SEPARATOR);
		content.append(stockItem.getAmount());
		content.append(ServiceAdapterHeaders.SEPARATOR);
		content.append(stockItem.getSalesPrice());
		return content.toString();
	}
	
	public static String getProductOrderContent(ProductOrder productOrder) {
		StringBuilder content = new StringBuilder();
		for (OrderEntry entry : productOrder.getOrderEntries()) {
			content.append(productOrder.getId());
			content.append(ServiceAdapterHeaders.SEPARATOR);
			content.append(productOrder.getStore().getId());
			content.append(ServiceAdapterHeaders.SEPARATOR);
			content.append(entry.getProductBarcode());
			content.append(ServiceAdapterHeaders.SEPARATOR);
			content.append(productOrder.getDeliveryDate() == null ? "00-00-0000" 
					: TimeUtils.convertToStringDate(productOrder.getDeliveryDate()));
			content.append(ServiceAdapterHeaders.SEPARATOR);
			content.append(TimeUtils.convertToStringDate(productOrder.getOrderingDate()));
			content.append(ServiceAdapterHeaders.SEPARATOR);
			content.append(entry.getAmount());
			content.append("\n");
		}
		return content.toString();
	}
	
	public static String getCreateEnterpriseContent(TradingEnterprise enterprise) {
		StringBuilder content = new StringBuilder();
		content.append(enterprise.getName());
		return content.toString();
	}
	
	public static String getUpdateEnterpriseContent(TradingEnterprise enterprise) {
		StringBuilder content = new StringBuilder();
		content.append(enterprise.getId());
		content.append(ServiceAdapterHeaders.SEPARATOR);
		content.append(enterprise.getName());
		return content.toString();
	}
	
	public static String getCreateStoreContent(Store store) {
		StringBuilder content = new StringBuilder();
		content.append(store.getEnterpriseName());
		content.append(ServiceAdapterHeaders.SEPARATOR);
		content.append(store.getName());
		content.append(ServiceAdapterHeaders.SEPARATOR);
		content.append(store.getLocation());
		return content.toString();
	}

	public static String getUpdateStoreContent(Store store) {
		StringBuilder content = new StringBuilder();
		content.append(store.getId());
		content.append(ServiceAdapterHeaders.SEPARATOR);
		content.append(store.getName());
		content.append(ServiceAdapterHeaders.SEPARATOR);
		content.append(store.getLocation());
		return content.toString();
	}

	public static String getCreateSupplierContent(ProductSupplier supplier) {
		StringBuilder content = new StringBuilder();
		content.append(supplier.getName());
		return content.toString();
	}

	public static String getUpdateSupplierContent(ProductSupplier supplier) {
		StringBuilder content = new StringBuilder();
		content.append(supplier.getId());
		content.append(ServiceAdapterHeaders.SEPARATOR);
		content.append(supplier.getName());
		return content.toString();
	}

	public static String getProductContent(Product product) {
		StringBuilder content = new StringBuilder();
		content.append(product.getBarcode());
		content.append(ServiceAdapterHeaders.SEPARATOR);
		content.append(product.getName());
		content.append(ServiceAdapterHeaders.SEPARATOR);
		content.append(product.getPurchasePrice());
		return content.toString();
	}
}