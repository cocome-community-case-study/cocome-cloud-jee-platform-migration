package org.cocome.cloud.web.usecase;

import java.util.Collection;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIData;
import javax.faces.context.FacesContext;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.ActionEvent;
import javax.faces.event.ActionListener;
import javax.inject.Inject;
import javax.xml.ws.WebServiceRef;
import javax.xml.ws.soap.SOAPFaultException;

import org.apache.log4j.Logger;
import org.cocome.cloud.web.entitywrapper.ProductWSSTOWrapper;
import org.cocome.tradingsystem.inventory.application.store.ComplexOrderEntryTO;
import org.cocome.tradingsystem.inventory.application.store.ComplexOrderTO;
import org.cocome.tradingsystem.inventory.application.store.ProductWithSupplierAndStockItemTO;
import org.cocome.cloud.logic.stub.IStoreManager;
import org.cocome.cloud.logic.stub.NotInDatabaseException_Exception;
import org.cocome.cloud.logic.stub.CreateException_Exception;
import org.cocome.cloud.logic.stub.UpdateException_Exception;
import org.cocome.cloud.logic.stub.IStoreManagerService;

@ManagedBean(name = "orderProduct", eager = true)
@SessionScoped
public class OrderProduct implements ActionListener, IUseCase{
	
	private static final Logger LOG = Logger.getLogger(OrderProduct.class);
	
	@Inject
	StockItemListFactory stockItemListFactory;
	
	@WebServiceRef(IStoreManagerService.class)
	IStoreManager storeManager;

	private StoreStockItemList stockItemList;
	private OrderItemList orderList = new OrderItemList();


	private String storeID;

	private String orderAmount = "";
	
	private boolean showMessage = false;
	private String message;

	// //////////////////////
	private UIData data = new UIData();
	private boolean showOrderProductView = false;
	private boolean showOrderList = false;



	public boolean isShowMessage() {
		return showMessage;
	}

	public void setShowMessage(boolean showMessage) {
		this.showMessage = showMessage;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public UIData getData() {
		return data;
	}

	public void setData(UIData data) {
		this.data = data;
	}

	public boolean isShowOrderProductView() {
		return showOrderProductView;
	}

	public void setShowOrderProductView(boolean ordert) {
		this.showOrderProductView = ordert;
	}

	public List<ProductWSSTOWrapper> getStockItemList() {
		return stockItemList.getListOfStockItems();
	}
	public void setStockItemList(List<ProductWSSTOWrapper> stockItemList) {
		this.stockItemList.setListOfStockItems(stockItemList);
	}

	public String getOrderAmount() {
		return orderAmount;
	}

	public void setOrderAmount(String orderAmount) {
		this.orderAmount = orderAmount;
	}

	public List<ComplexOrderEntryTO> getOrderList() {
		return orderList.getOrderItems();
	}

	public void setOrderList(List<ComplexOrderEntryTO> orderList) {
		this.orderList.setOrderEntries(orderList);
	}

	public boolean isShowOrderList() {
		return showOrderList;
	}

	public void setShowOrderList(boolean orderlistshow) {
		this.showOrderList = orderlistshow;
	}
	
	private void deactivateAllViewComponents() {
		setShowOrderList(false);
		setShowMessage(false);
		setShowOrderProductView(false);
	}

	private String getSOAPFaultMessage(SOAPFaultException e) {
		return e.getFault() != null ? e.getFault().getFaultString(): e.getMessage();
	}
	
	public String invoke() throws Exception {
		deactivateAllViewComponents();
		setShowOrderProductView(true);
		
		long storeID = Long.parseLong(this.storeID);
		
		stockItemList = stockItemListFactory.getStockItemList(storeID);
		
		try {
			Collection<ProductWithSupplierAndStockItemTO> products = storeManager
					.getProductsWithStockItems(storeID);
			stockItemList.insertProducts(products);
		} catch (NotInDatabaseException_Exception e) {
			FacesContext.getCurrentInstance().addMessage("usecase:order", 
			new FacesMessage("Error", 
					"Store does not exist."));
		} catch (SOAPFaultException e) {
			FacesContext.getCurrentInstance().addMessage("usecase:order", 
			new FacesMessage("Empty", getSOAPFaultMessage(e)));
		}
		
		return "getStockItem";
	}

	public String order() {
		ProductWSSTOWrapper s = (ProductWSSTOWrapper) this.data.getRowData();
		
		ComplexOrderEntryTO entry = orderList.getOrderByBarcode(s.getBarcode());
		
		if (entry == null) {
			entry = new ComplexOrderEntryTO();
		}
		
		// Take into account previously placed orders to check for ordering too much
		long combinedAmount = entry.getAmount() + s.getStockItemTO().getIncomingAmount() + s.getStockItemTO().getAmount();
		long stockAfterOrder = combinedAmount + s.getOrderAmount();
		long maxStock = s.getStockItemTO().getMaxStock();
		
		if(stockAfterOrder > maxStock) {
			long maxOrderAmount = maxStock - combinedAmount;
			FacesContext.getCurrentInstance().addMessage("usecase:order", 
			new FacesMessage("Error", 
					"Can not order more than " + maxOrderAmount + " items for this stock item."));
			return "order";
		}
		
		entry.setAmount(s.getOrderAmount());
		entry.setProductTO(s.getProductTO());
		
		orderList.setOrderWithBarcode(s.getBarcode(), entry);
		
		LOG.debug("Added order entry");
		
		setShowOrderList(true);
		
		return "order";
	}

	public String save() {
		ComplexOrderTO order = new ComplexOrderTO();
		order.setOrderEntryTOs(orderList.getOrderItems());
		order.setDeliveryDate(null);
		order.setOrderingDate(null);
		
		LOG.debug("Trying to persist orders: " + order.getOrderEntryTOs());
		
		try {
			storeManager.orderProducts(Long.parseLong(storeID), order);
			FacesContext.getCurrentInstance().addMessage("usecase:order", 
					new FacesMessage("Success", 
							"Order created successfully."));
			setShowOrderList(false);
			stockItemList.setReloadNeeded(true);
			orderList.clear();
		} catch (NumberFormatException | NotInDatabaseException_Exception 
				| CreateException_Exception | UpdateException_Exception e) {
			FacesContext.getCurrentInstance().addMessage("usecase:order", 
			new FacesMessage("Error", 
					"Could not create the orders."));
		}
		return "save";
	}
	
	public String reset() {
		orderList.clear();
		return "reset";
	}
	
	public String finish() {
		this.orderAmount = "";
		showOrderList = false;
		orderList.clear();
		showMessage = false;
		message = "";
		data = new UIData();
		showOrderProductView = false;
		
		return "Finish Order";
	}

	@Override
	public void processAction(ActionEvent arg0) throws AbortProcessingException {
		finish();
	}

	public void getSelected(ActionEvent event) {
		this.storeID = (String) event.getComponent().getAttributes()
				.get("storeId");		

	}
	
	public void getAmount(ActionEvent event) {
		this.orderAmount = (String) event.getComponent().getAttributes()
				.get("orderAmount");
	}

	@Override
	public void attrListener(ActionEvent event) {
		// TODO Auto-generated method stub
		
	}

}
