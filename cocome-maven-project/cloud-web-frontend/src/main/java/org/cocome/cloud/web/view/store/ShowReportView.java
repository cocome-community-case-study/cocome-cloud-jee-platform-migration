package org.cocome.cloud.web.view.store;

import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;

import org.cocome.cloud.web.inventory.connection.IStoreQuery;
import org.cocome.cloud.web.inventory.store.IStoreInformation;
import org.cocome.cloud.web.inventory.store.ProductWrapper;

@ManagedBean
@ViewScoped
public class ShowReportView {
	private long storeId;
	private List<ProductWrapper> stockItems;
	
	@Inject
	IStoreInformation storeInformation;

	public long getStoreId() {
		return storeId;
	}

	public void setStoreId(long storeId) {
		this.storeId = storeId;
	}
	
	public void loadReport(long storeId) {
		stockItems = storeInformation.getStockReport(storeId);
	}

	public List<ProductWrapper> getStockItems() {
		return stockItems;
	}
}
