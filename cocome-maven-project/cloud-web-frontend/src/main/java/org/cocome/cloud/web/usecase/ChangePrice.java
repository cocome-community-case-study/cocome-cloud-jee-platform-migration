package org.cocome.cloud.web.usecase;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIData;
import javax.faces.context.FacesContext;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.ActionEvent;
import javax.faces.event.ActionListener;
import javax.xml.ws.WebServiceRef;
import javax.xml.ws.soap.SOAPFaultException;

import org.apache.log4j.Logger;
import org.cocome.cloud.web.entitywrapper.ProductWSSTOWrapper;
import org.cocome.tradingsystem.inventory.application.store.ProductWithSupplierAndStockItemTO;
import org.cocome.cloud.logic.stub.IStoreManagerService;
import org.cocome.cloud.logic.stub.IStoreManager;
import org.cocome.cloud.logic.stub.NotInDatabaseException_Exception;



@ManagedBean
@SessionScoped
public class ChangePrice implements ActionListener, IUseCase {

	@WebServiceRef(IStoreManagerService.class)
	IStoreManager storeManager;
	
	private static final Logger LOG = Logger.getLogger(ChangePrice.class);
	
	private UIData data = new UIData();
	private String enterpriseName;
	private String storeId;
	private String storeName;
	private String storeLocation;
	private HashMap<edu.kit.ipd.sdq.evaluation.Barcode, ProductWSSTOWrapper> stockItems;
	private String message;
	private boolean showMessage = false;
	private boolean showChangePrice = false;
	
	public UIData getData() {
		return data;
	}

	public void setData(UIData data) {
		this.data = data;
	}

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

	public List<ProductWSSTOWrapper> getStockItems() {
		return new LinkedList<ProductWSSTOWrapper>(stockItems.values());
	}

	public void setStockItems(Collection<ProductWSSTOWrapper> stockItems) {
		stockItems.clear();
		for (ProductWSSTOWrapper item : stockItems) {
			this.stockItems.put(item.getProductTO().getBarcode(), item);
		}
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public boolean isShowMessage() {
		return showMessage;
	}

	public void setShowMessage(boolean showMessage) {
		this.showMessage = showMessage;
	}

	public boolean isShowChangePrice() {
		return showChangePrice;
	}

	public void setShowChangePrice(boolean showChangePrise) {
		this.showChangePrice = showChangePrise;
	}

	@Override
	public String invoke() {
		this.showChangePrice = true;
		stockItems = new HashMap<edu.kit.ipd.sdq.evaluation.Barcode, ProductWSSTOWrapper>();
		try {
			List<ProductWithSupplierAndStockItemTO> items = storeManager.getProductsWithStockItems(Integer.parseInt(storeId));
			for (ProductWithSupplierAndStockItemTO item : items) {
				stockItems.put(item.getBarcode(), new ProductWSSTOWrapper(item));
			}
		} catch (SOAPFaultException ex) {
			LOG.error("SOAPFault while getting products: " + ex.getFault().getDetail());
			this.message = "Connection Error!";
			this.showMessage = true;
		} catch (NumberFormatException e) {
			this.message = "Wrong store id!";
			this.showMessage = true;
		} catch (NotInDatabaseException_Exception e) {
			this.message = "The store with this id was not found in the database!";
			this.showMessage = true;
		}
		
		if (this.stockItems.size() == 0) {
			FacesContext.getCurrentInstance().addMessage("usecase:changePrise", 
					new FacesMessage("Empty", 
							"There are no Stock Item in Store!"));
		}
		return "change_price_doit";
	}

	public String changeSalesPrice(double newPrice) {
		try {
			LOG.debug("Trying to change price...");
			ProductWSSTOWrapper s = (ProductWSSTOWrapper) this.data.getRowData();
				ProductWSSTOWrapper selectedItem = stockItems.get(s.getProductTO()
						.getBarcode());
				if (selectedItem != null) {
					LOG.debug("Changing price on stockItem with id " + selectedItem.getStockItemTO().getId() 
							+ " to " + newPrice);
					selectedItem.getStockItemTO().setSalesPrice(newPrice);
					storeManager.changePrice(Long.parseLong(storeId),
							selectedItem.getStockItemTO());
					selectedItem.setEditingEnabled(false);
					
					this.message = "Price successfully changed!";
					this.showMessage = true;
				}
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage("usecase:newprice",
					new FacesMessage("Error", "Database connection aborted."));
		}

		return "change_price_change";
	}
	
	public String editStockItemPrice() {
		ProductWSSTOWrapper s = (ProductWSSTOWrapper) this.data.getRowData();
		
		ProductWSSTOWrapper selectedItem = stockItems.get(s.getProductTO().getBarcode());
		if (selectedItem != null) {
			selectedItem.setEditingEnabled(true);
		}
		
		return "edit stockItem price";
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

		data = new UIData();
		enterpriseName = "";
		storeId = "";
		storeName = "";
		storeLocation = "";
		stockItems = new HashMap<edu.kit.ipd.sdq.evaluation.Barcode, ProductWSSTOWrapper>();
		message = "";
		showMessage = false;
		showChangePrice = false;

		return "change_price_finish";
	}

	@Override
	public void processAction(ActionEvent arg0) throws AbortProcessingException {
		finish();

	}

}
