package org.cocome.cloud.web.frontend.store;

import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;
import org.cocome.cloud.web.data.storedata.ProductWrapper;

@ManagedBean
@ViewScoped
public class ShowReportView {
	private long storeId;
	private List<ProductWrapper> stockItems;
	
	@Inject
	StoreInformation storeInformation;

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
