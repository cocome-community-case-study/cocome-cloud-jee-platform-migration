package org.cocome.cloud.web.usecase;

import java.util.ArrayList;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIData;
import javax.faces.context.FacesContext;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.ActionEvent;
import javax.faces.event.ActionListener;
import javax.xml.datatype.DatatypeConstants;
import javax.xml.ws.WebServiceRef;
import javax.xml.ws.soap.SOAPFaultException;

import org.cocome.cloud.web.entitywrapper.ComplexOrderEntryTOWrapper;
import org.cocome.tradingsystem.inventory.application.store.ComplexOrderEntryTO;
import org.cocome.tradingsystem.inventory.application.store.ComplexOrderTO;
import org.cocome.cloud.logic.stub.IStoreManager;
import org.cocome.cloud.logic.stub.IStoreManagerService;
import org.cocome.cloud.logic.stub.NotInDatabaseException_Exception;
import org.cocome.cloud.logic.stub.InvalidRollInRequestException_Exception;
import org.cocome.cloud.logic.stub.UpdateException_Exception;

@ManagedBean
@SessionScoped
public class ReceiveOrderedProducts implements ActionListener, IUseCase {
	
	@WebServiceRef(IStoreManagerService.class)
	IStoreManager storeManager;
	
	private UIData data = new UIData();
	private String reciveAmount;
	private String enterpriseName;
	private String storeId;
	private String storeName;
	private String storeLocation;
	private List<ComplexOrderEntryTOWrapper> listOfReceProductOrder = new ArrayList<>();
	private String orderDeliveryDate;
	private boolean showList = false;
	private String orderId = "";
	private boolean showReceive = false ;
	private boolean showamounthere = false;
	private String orderMessage;
	private String amountHere;

	

	/**
	 * @return the data
	 */
	public UIData getData() {
		return data;
	}

	/**
	 * @param data the data to set
	 */
	public void setData(UIData data) {
		this.data = data;
	}

	/**
	 * @return the orderId
	 */
	public String getOrderId() {
		return orderId;
	}

	/**
	 * @param orderId the orderId to set
	 */
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	/**
	 * @return the showReceive
	 */
	public boolean isShowReceive() {
		return showReceive;
	}

	/**
	 * @param showReceive the showReceive to set
	 */
	public void setShowReceive(boolean showReceive) {
		this.showReceive = showReceive;
	}

	/**
	 * @return the showamounthere
	 */
	public boolean isShowamounthere() {
		return showamounthere;
	}

	/**
	 * @param showamounthere the showamounthere to set
	 */
	public void setShowamounthere(boolean showamounthere) {
		this.showamounthere = showamounthere;
	}

	/**
	 * @return the orderMessage
	 */
	public String getOrderMessage() {
		return orderMessage;
	}

	/**
	 * @param orderMessage the orderMessage to set
	 */
	public void setOrderMessage(String orderMessage) {
		this.orderMessage = orderMessage;
	}

	/**
	 * @return the reciveAmount
	 */
	public String getReciveAmount() {
		return reciveAmount;
	}

	/**
	 * @param reciveAmount the reciveAmount to set
	 */
	public void setReciveAmount(String reciveAmount) {
		this.reciveAmount = reciveAmount;
	}

	/**
	 * @return the amountHere
	 */
	public String getAmountHere() {
		return amountHere;
	}

	/**
	 * @param amountHere the amountHere to set
	 */
	public void setAmountHere(String amountHere) {
		this.amountHere = amountHere;
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

	/**
	 * @return the orderDeliveryDate
	 */
	public String getOrderDeliveryDate() {
		return orderDeliveryDate;
	}

	/**
	 * @param orderDeliveryDate the orderDeliveryDate to set
	 */
	public void setOrderDeliveryDate(String orderDeliveryDate) {
		this.orderDeliveryDate = orderDeliveryDate;
	}

	/**
	 * @return the listOfProductOrder
	 */
	public List<ComplexOrderEntryTOWrapper> getListOfReceProductOrder() {
		return listOfReceProductOrder;
	}

	/**
	 * @param listOfProductOrder the listOfProductOrder to set
	 */
	public void setListOfReceProductOrder(List<ComplexOrderEntryTOWrapper> listOfReceProductOrder) {
		this.listOfReceProductOrder = listOfReceProductOrder;
	}

	/**
	 * @return the showList
	 */
	public boolean isShowList() {
		return showList;
	}

	/**
	 * @param showList the showList to set
	 */
	public void setShowList(boolean showList) {
		this.showList = showList;
	}

	@Override
	public void processAction(ActionEvent arg0) throws AbortProcessingException {
		finish();
		
	}

	@Override
	public String invoke() {
		this.showReceive = true;
		this.orderMessage = "";
		this.listOfReceProductOrder.clear();
		return null;
	}
	
	public String getAllOrderProduct() {
		listOfReceProductOrder.clear();
		long storeID = Long.parseLong(this.storeId);
		try {
			for (ComplexOrderTO order : storeManager.getOutstandingOrders(storeID)) {
				for (ComplexOrderEntryTO orderEntry : order.getOrderEntryTOs()) {
					listOfReceProductOrder.add(new ComplexOrderEntryTOWrapper(orderEntry, order));
				}
			}
		} catch (NotInDatabaseException_Exception | SOAPFaultException e) {
			FacesContext.getCurrentInstance().addMessage("usecase:orderSearch", 
					new FacesMessage("Error", 
					"The store was not found in the database."));
			listOfReceProductOrder.clear();
		}
		
//		
//		GetXMLFromBackend get = new GetXMLFromBackend();
//		String dataOrderproduct;
//		CSVParser parser = new CSVParser();
//		
//		try {
//			dataOrderproduct = get.getProductOrder("store.id==" + this.storeId);
//			parser.parse(dataOrderproduct);
//		}
//		catch (Exception e) {
//			FacesContext.getCurrentInstance().addMessage("usecase:orderSearch", 
//					new FacesMessage("Error", 
//							"Database connection aborted."));
//		}
//		
//		ProductOrder e = new ProductOrder();
//		
//		DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
//		
//		try {
//		for (int i = 0; i < parser.getModel().getRows().size(); i++) {
//			
//			Date deliveryDate = dateFormat.parse(parser.getModel().getRows().get(i).getColumns().get(5).getValue());
//			Date orderingDate = dateFormat.parse(parser.getModel().getRows().get(i).getColumns().get(6).getValue());
//			
//			if (deliveryDate.before(orderingDate)) {
//				
//				e.setProductOrderID(parser.getModel().getRows().get(i).getColumns().get(0).getValue());
//				e.setEnterpriseName(parser.getModel().getRows().get(i).getColumns().get(1).getValue());
//				e.setStoreID(this.storeId);
//				e.setStoreName(parser.getModel().getRows().get(i).getColumns().get(2).getValue());
//				e.setStoreLocation(parser.getModel().getRows().get(i).getColumns().get(3).getValue());
//				e.setProductBarcode(parser.getModel().getRows().get(i).getColumns().get(4).getValue());
//				e.setOrderDeliveryDate(parser.getModel().getRows().get(i).getColumns().get(5).getValue());
//				e.setOrderOrderingDate(parser.getModel().getRows().get(i).getColumns().get(6).getValue());
//				e.setOrderAmount(parser.getModel().getRows().get(i).getColumns().get(7).getValue());
//				e.setEditable(false);
//				CSVParser parser_Product = new CSVParser();
//				parser_Product.parse(get.getProducts("barcode==" + e.getProductBarcode()));
//				e.setProductName(parser_Product.getModel().getRows().get(0).getColumns()
//						.get(1).getValue());
//				this.listOfReceProductOrder.add(e);
//				e = new ProductOrder();
//				
//			}
//			else  {
//				FacesContext.getCurrentInstance().addMessage("usecase:orderSearch", 
//						new FacesMessage("Empty", 
//								"There are no order to receive."));
//			}
//		}
//		}catch (Exception ex) {
//			FacesContext.getCurrentInstance().addMessage("usecase:orderSearch", 
//					new FacesMessage("Empty", 
//							"Database connection aborted."));
//		}
		return null;
	}
	
	public String searchProductOrder()  {
		listOfReceProductOrder.clear();
		if (this.orderId.isEmpty() || !this.orderId.matches("[0-9]*")) {
			FacesContext.getCurrentInstance().addMessage("db:orderSearch", 
					new FacesMessage("Error", 
					"Please enter a valid order id."));
			return null;
		}
		
		ComplexOrderTO order;
		try {
			order = storeManager.getOrder(Long.parseLong(storeId), Long.parseLong(this.orderId));
		} catch (NumberFormatException | NotInDatabaseException_Exception
				 | SOAPFaultException e) {
			FacesContext.getCurrentInstance().addMessage("db:orderSearch", 
					new FacesMessage("Error", 
					"Could not find the order with the given id."));
			listOfReceProductOrder.clear();
			return null;
		}
		
		for (ComplexOrderEntryTO orderEntry : order.getOrderEntryTOs()) {
			listOfReceProductOrder.add(new ComplexOrderEntryTOWrapper(orderEntry, order));
		}
		
		int comparison = order.getDeliveryDate().compareTo(order.getOrderingDate());
		
		if (comparison == DatatypeConstants.GREATER || comparison == DatatypeConstants.EQUAL) {
			this.orderMessage = "Order has already arrived.";
			this.showamounthere = true;
		} else {
			this.orderMessage = "Order has not yet arrived.";
		}
		
//		try {
//		this.orderMessage = "";
//		if (!this.orderId.isEmpty() && this.orderId.matches("[0-9]*")) {
//			
//			GetXMLFromBackend get = new GetXMLFromBackend();
//			CSVParser parser = new CSVParser();
//			try {
//				parser.parse(get.getProductOrder("id==" + this.orderId + ";ProductOrder.store.id==" + this.storeId));
//			}
//			catch (Exception ex) {
//				FacesContext.getCurrentInstance().addMessage("db:orderSearch", 
//						new FacesMessage("Error", 
//								"Database connection aborted."));
//			}
//			if (parser.getModel().size() > 0) {
//				ProductOrder e = new ProductOrder();
//				this.listOfReceProductOrder = new ArrayList<>();
//				e.setProductOrderID(parser.getModel().getRows().get(0).getColumns().get(0).getValue());
//				e.setEnterpriseName(parser.getModel().getRows().get(0).getColumns().get(1).getValue());
//				e.setStoreID(this.storeId);
//				e.setStoreName(parser.getModel().getRows().get(0).getColumns().get(2).getValue());
//				e.setStoreLocation(parser.getModel().getRows().get(0).getColumns().get(3).getValue());
//				e.setProductBarcode(parser.getModel().getRows().get(0).getColumns().get(4).getValue());
//				e.setOrderDeliveryDate(parser.getModel().getRows().get(0).getColumns().get(5).getValue());
//				e.setOrderOrderingDate(parser.getModel().getRows().get(0).getColumns().get(6).getValue());
//				e.setOrderAmount(parser.getModel().getRows().get(0).getColumns().get(7).getValue());
//				e.setEditable(false);
//				CSVParser parser_Product = new CSVParser();
//				parser_Product.parse(get.getProducts("barcode==" + e.getProductBarcode()));
//				e.setProductName(parser_Product.getModel().getRows().get(0).getColumns()
//						.get(1).getValue());
//				this.listOfReceProductOrder.add(e);
//				
//				SimpleDateFormat sdfToDate = new SimpleDateFormat( "dd-MM-yyyy" );
//				Date arivedate = sdfToDate.parse(parser.getModel().getRows().get(0).getColumns().get(5).getValue());
//				Date orderDate = sdfToDate.parse(parser.getModel().getRows().get(0).getColumns().get(6).getValue());
//				
//				if (arivedate.after(orderDate) || arivedate.compareTo(orderDate) == 0) {
//					this.orderMessage = "The order is here!";
//					this.showamounthere = true;
//				}
//				else {
//					this.orderMessage = "Not Yet here!";
//				}
//			}
//			else {
//				FacesContext.getCurrentInstance().addMessage("usecase:orderSearch", 
//						new FacesMessage("Error", 
//								"The product order does not exist in the database."));
//				this.listOfReceProductOrder = new ArrayList<>();
//			}
//			
//		} else {
//			FacesContext.getCurrentInstance().addMessage("usecase:orderSearch", 
//					new FacesMessage("Error", 
//							"The number must be positive integer."));
//		}
//		}
//		catch (Exception e) {
//			FacesContext.getCurrentInstance().addMessage("usecase:orderSearch", 
//					new FacesMessage("Error", 
//							"Database connection aborted."));
//		}
		return null;
	}

	public String rollInOrder() {
		ComplexOrderEntryTOWrapper order = (ComplexOrderEntryTOWrapper) data.getRowData();
		
		try {
			storeManager.rollInReceivedOrder(Long.parseLong(storeId), order.getContainingOrder().getId());
			order.setContainingOrder(storeManager.getOrder(Long.parseLong(storeId), order.getContainingOrder().getId()));
			FacesContext.getCurrentInstance().addMessage("usecase:orderSearch", 
					new FacesMessage("Success", 
							"Order rolled in successfully."));
			listOfReceProductOrder.clear();
		} catch (NumberFormatException
				| InvalidRollInRequestException_Exception
				| NotInDatabaseException_Exception | UpdateException_Exception
				| SOAPFaultException e) {
			   FacesContext.getCurrentInstance().addMessage("usecase:orderSearch", 
						new FacesMessage("Error", 
								"Could not roll in the order."));
		}
//		
//		if (e.getIncommingAmount().matches("[1-9][0-9]*")) {
//			
//			GetXMLFromBackend get = new GetXMLFromBackend();
//			String orderAmount = "";
//			
//			for(ProductOrder porder: listOfReceProductOrder) {
//				
//				if (porder.getProductOrderID().equals(e.getProductOrderID())) {
//					
//					orderAmount = porder.getOrderAmount();
//					break;
//				}
//			}
//			
//			
//		if (orderAmount.equals(e.getIncommingAmount())) {
//				
//			CSVParser stockItemParser = new CSVParser();
//			
//			try {	
//					
//				stockItemParser.parse(get.getStockItems("store.id==" + this.storeId + ";product.barcode==" + e.getProductBarcode()));
//					  
//				int sumAmount = Integer.parseInt(e.getIncommingAmount()) +
//							  	Integer.parseInt(stockItemParser.getModel().getRows().get(0).getColumns().get(7).getValue());
//					  
//				String sum = sumAmount + "";
//					  
//				StockItem item = new StockItem();
//					  
//				item.setProductBarcode(stockItemParser.getModel().getRows().get(0).getColumns().get(3).getValue());
//				item.setStockItemAmount(sum);
//				item.setStockItemMinStock(stockItemParser.getModel().getRows().get(0).getColumns().get(4).getValue());
//				item.setStockItemMaxStock(stockItemParser.getModel().getRows().get(0).getColumns().get(5).getValue());
//				item.setStockItemIncomingAmount(e.getIncommingAmount());
//				item.setStockItemSalesPrice(stockItemParser.getModel().getRows().get(0).getColumns().get(8).getValue());
//					  
//				List<StockItem> listOfStokItems = new ArrayList<>();
//					  
//				listOfStokItems.add(item);
//					  
//				UpdateStockItem upItems = new UpdateStockItem(storeId, listOfStokItems);
//				upItems.update();
//				
//				ProductOrder order = new ProductOrder();
//					  
//				DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
//				Date date = new Date();
//					  
//				order.setProductOrderID(e.getProductOrderID());
//				order.setProductBarcode(e.getProductBarcode());
//				order.setOrderDeliveryDate(dateFormat.format(date));
//				order.setOrderOrderingDate(e.getOrderOrderingDate());
//				order.setOrderAmount(e.getOrderAmount());
//					  
//				List<ProductOrder> listOfProductOrder = new ArrayList<>();
//				
//				listOfProductOrder.add(order);
//					  
//				UpdateOrderProduct upOrder = new UpdateOrderProduct(storeId, order.getProductOrderID(), listOfProductOrder);
//				upOrder.update();
//				
//				orderAmount = "";
//				listOfReceProductOrder = new ArrayList<>();
//				
//					  
//				} catch (Exception ex) {
//					   FacesContext.getCurrentInstance().addMessage("usecase:orderSearch", 
//								new FacesMessage("Error", 
//										"Database connection aborted."));
//			    }
//				
//			} else {
//				FacesContext.getCurrentInstance().addMessage("usecase:orderSearch", 
//						new FacesMessage("Error", 
//								"Ther product order not Comlpete!"));
//			  }
//			
//		} else {
//			FacesContext.getCurrentInstance().addMessage("usecase:orderSearch", 
//					new FacesMessage("Error", 
//							"The number must be positive integer."));
//		  }
//		
		return "check current amount";
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
		listOfReceProductOrder.clear();
		orderDeliveryDate = "";
		showList = false;
		orderId = "";
		showReceive = false ;
		showamounthere = false;
		orderMessage = "";
		amountHere = "";
		return null;
	}

}
