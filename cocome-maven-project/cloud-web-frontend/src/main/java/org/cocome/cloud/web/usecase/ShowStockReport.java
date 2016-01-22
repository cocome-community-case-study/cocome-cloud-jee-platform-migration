package org.cocome.cloud.web.usecase;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.ActionEvent;
import javax.faces.event.ActionListener;



@ManagedBean
@SessionScoped
public class ShowStockReport implements ActionListener, IUseCase{
	
	private String enterpriseName;
	private String storeId;
	private String storeName;
	private String storeLocation;

	public String getEnterpriseName() {
		return enterpriseName;
	}

	public void setEnterpriseName(String enterpriseName) {
		this.enterpriseName = enterpriseName;
	}

	public String getStoreId() {
		return storeId;
	}

	public void setStoreId(String storeId) {
		this.storeId = storeId;
	}

	public String getStoreName() {
		return storeName;
	}

	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}

	public String getStoreLocation() {
		return storeLocation;
	}

	public void setStoreLocation(String storeLocation) {
		this.storeLocation = storeLocation;
	}

	@Override
	public void processAction(ActionEvent arg0) throws AbortProcessingException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String invoke() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void attrListener(ActionEvent event) {
		this.enterpriseName = (String) event.getComponent().getAttributes()
				.get("enterpriseName");
		this.storeId = (String) event.getComponent().getAttributes()
				.get("storeId");
		this.storeName = (String) event.getComponent().getAttributes()
				.get("storeName");
		this.storeLocation = (String) event.getComponent().getAttributes()
				.get("storeLocation");
		
	}

	@Override
	public String finish() {
		// TODO Auto-generated method stub
		return null;
	}

}
