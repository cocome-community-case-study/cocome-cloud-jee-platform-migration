package org.cocome.cloud.web.usecase;

import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.ActionEvent;
import javax.faces.event.ActionListener;
import javax.inject.Inject;
import javax.xml.ws.soap.SOAPFaultException;

import org.cocome.cloud.web.entitywrapper.ProductWSSTOWrapper;
import org.cocome.cloud.logic.stub.NotInDatabaseException_Exception;


@ManagedBean
@SessionScoped
public class ShowReportOfProduct implements ActionListener, IUseCase{
	
	@Inject
	StockItemListFactory stockItemListFactory;
	
	private String storeId;
	private String searchID;
	private String message;
	private boolean showSearchContinent = false;
	private String storeLocation;
	
	private boolean per = false;
	private StoreStockItemList stockItemList;
	private boolean showStockreport = false;
	
	
	
	public String getStoreLocation() {
		return storeLocation;
	}
	public void setStoreLocation(String storeLocation) {
		this.storeLocation = storeLocation;
	}
	/**
	 * @return the searchID
	 */
	public String getSearchID() {
		return searchID;
	}
	/**
	 * @param searchID the searchID to set
	 */
	public void setSearchID(String searchID) {
		this.searchID = searchID;
	}
	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}
	/**
	 * @param message the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}
	/**
	 * @return the showSearchContinent
	 */
	public boolean isShowSearchContinent() {
		return showSearchContinent;
	}
	/**
	 * @param showSearchContinent the showSearchContinent to set
	 */
	public void setShowSearchContinent(boolean showSearchContinent) {
		this.showSearchContinent = showSearchContinent;
	}
	public boolean isPer() {
		return per;
	}
	public void setPer(boolean per) {
		this.per = per;
	}
	public boolean isShowStockreport() {
		return showStockreport;
	}
	public void setShowStockreport(boolean showStockreport) {
		this.showStockreport = showStockreport;
	}
	
	
	public List<ProductWSSTOWrapper> getStockItemList() {
		return stockItemList.getListOfStockItems();
	}
	public void setStockItemList(List<ProductWSSTOWrapper> stockItemList) {
		this.stockItemList.setListOfStockItems(stockItemList);
	}
	
	public String invoke() {
		this.showSearchContinent = true;
		return "faild";
	}
	
	public String searchStockItemsOfStore()  {
		setShowStockreport(true);
		
		try {
			stockItemList = stockItemListFactory.getStockItemList(Long.parseLong(storeId));
		} catch (NumberFormatException | NotInDatabaseException_Exception 
				| SOAPFaultException e) {
			FacesContext.getCurrentInstance().addMessage("usecase:searchID", 
					new FacesMessage("Not Found", 
					"The stock items for this store could not be retrieved!"));
		}
		return "";
	}
		
	@Override
	public void processAction(ActionEvent arg0) throws AbortProcessingException {
		this.showStockreport = false;
		this.showSearchContinent =false;
	}
	
	public void getSelected(ActionEvent event) {
		this.storeId = (String) event.getComponent().getAttributes()
				.get("storeId");
		this.storeLocation = (String) event.getComponent().getAttributes()
				.get("storeLocation");

	}
	@Override
	public void attrListener(ActionEvent event) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public String finish() {
		// TODO Auto-generated method stub
		return null;
	}
	

}
