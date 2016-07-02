package org.cocome.cloud.web.usecase;

import java.util.ArrayList;
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
import javax.inject.Inject;
import javax.xml.ws.WebServiceRef;
import javax.xml.ws.soap.SOAPFaultException;

import org.apache.log4j.Logger;
import org.cocome.cloud.web.entitywrapper.EnterpriseTOWrapper;
import org.cocome.cloud.web.entitywrapper.ProductTOWrapper;
import org.cocome.cloud.web.entitywrapper.ProductWSSTOWrapper;
import org.cocome.cloud.web.entitywrapper.StoreTOWrapper;
import org.cocome.cloud.web.login.Login;
import org.cocome.tradingsystem.inventory.application.store.EnterpriseTO;
import org.cocome.tradingsystem.inventory.application.store.ProductTO;
import org.cocome.tradingsystem.inventory.application.store.ProductWithStockItemTO;
import org.cocome.tradingsystem.inventory.application.store.StoreWithEnterpriseTO;
import org.cocome.cloud.logic.stub.IEnterpriseManager;
import org.cocome.cloud.logic.stub.IStoreManager;
import org.cocome.cloud.logic.stub.IStoreManagerService;
import org.cocome.cloud.logic.stub.IEnterpriseManagerService;
import org.cocome.cloud.logic.stub.NotInDatabaseException_Exception;
import org.cocome.cloud.logic.stub.UpdateException_Exception;
import org.cocome.cloud.logic.stub.CreateException_Exception;



/**
 * 
 * @author Anas
 *
 */
@ManagedBean
@SessionScoped
public class CreateEnterprise implements ActionListener, IUseCase {
	
	@Inject
	StockItemListFactory stockItemListFactory;
	
	@WebServiceRef(IEnterpriseManagerService.class)
	IEnterpriseManager enterpriseManager;
	
	@WebServiceRef(IStoreManagerService.class)
	IStoreManager storeManager;
	
	private static Logger LOG = Logger.getLogger(CreateEnterprise.class);

	private UIData data = new UIData();
	private UIData data_stockItem = new UIData();
	private String enterpriseName;
	private String enterpriseID;
	private StoreTOWrapper store = new StoreTOWrapper();
	private boolean viewStores = false;
	private boolean showCreateStore = false;
	private boolean showCreateEnterprise = false;
	///////////////////////////
	private ProductWSSTOWrapper product;
	private String productName;
	private String productBarcode;
	private String purchasePrice;
	private List<ProductTOWrapper> listOfProduct = new ArrayList<ProductTOWrapper>(); 
	private boolean showListofProduct = false;
	private boolean showCreateProduct = false;
	///////////////////////////
	private ProductWSSTOWrapper stockItem;
	private boolean showCreateStockItem = false;
	private boolean showListOfStockItem =false;
	private boolean showListOfNewStockItems = false;
	private StoreStockItemList listOfStockItem;
	private List<ProductWSSTOWrapper> listOfNewStockItems = new LinkedList<ProductWSSTOWrapper>();
	//////////////////////////////
	private UIData data_stores = new UIData();
	private String newStoreId;
	private String newStoreName;
	private String newStoreLocation;
	private boolean canCreateStore = false;
	private boolean editStore = false;
	// ////////////////////////
	private boolean showMessage = false;
	private List<String> messages = new ArrayList<String>();
	private StoreTOWrapper updateStore_data;
	private List<EnterpriseTOWrapper> listOfEnterprise = new ArrayList<EnterpriseTOWrapper>();
	private HashMap<Long, StoreTOWrapper> listOfStores = new HashMap<Long, StoreTOWrapper>();
	private boolean viewData = false;

	/**
	 * @return the data_stockItem
	 */
	public UIData getData_stockItem() {
		return data_stockItem;
	}

	/**
	 * @param data_stockItem the data_stockItem to set
	 */
	public void setData_stockItem(UIData data_stockItem) {
		this.data_stockItem = data_stockItem;
	}

	/**
	 * @return the showListOfStockItem
	 */
	public boolean isShowListOfStockItem() {
		return showListOfStockItem;
	}

	/**
	 * @param showListOfStockItem the showListOfStockItem to set
	 */
	public void setShowListOfStockItem(boolean showListOfStockItem) {
		this.showListOfStockItem = showListOfStockItem;
	}

	public boolean isShowCreateStore() {
		return showCreateStore;
	}

	public void setShowCreateStore(boolean showCreateStore) {
		this.showCreateStore = showCreateStore;
	}

	public boolean isShowCreateEnterprise() {
		return showCreateEnterprise;
	}

	public void setShowCreateEnterprise(boolean showCreateEnterprise) {
		this.showCreateEnterprise = showCreateEnterprise;
	}

	/**
	 * @return the showCreateStockItem
	 */
	public boolean isShowCreateStockItem() {
		return showCreateStockItem;
	}

	/**
	 * @param showCreateStockItem the showCreateStockItem to set
	 */
	public void setShowCreateStockItem(boolean showCreateStockItem) {
		this.showCreateStockItem = showCreateStockItem;
	}

	public ProductWSSTOWrapper getProduct() {
		return product;
	}

	public void setProduct(ProductWSSTOWrapper product) {
		this.product = product;
	}

	public boolean isShowCreateProduct() {
		return showCreateProduct;
	}

	public void setShowCreateProduct(boolean showCreateProduct) {
		this.showCreateProduct = showCreateProduct;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getProductBarcode() {
		return productBarcode;
	}

	public void setProductBarcode(String productBarcode) {
		this.productBarcode = productBarcode;
	}

	public List<ProductTOWrapper> getListOfProduct() {
		return listOfProduct;
	}

	public void setListOfProduct(List<ProductTOWrapper> listOfProduct) {
		this.listOfProduct = listOfProduct;
	}

	public String getPurchasePrice() {
		return purchasePrice;
	}

	public void setPurchasePrice(String purchasePrice) {
		this.purchasePrice = purchasePrice;
	}

	public String getEnterpriseName() {
		return enterpriseName;
	}

	public void setEnterpriseName(String enterpriseName) {
		this.enterpriseName = enterpriseName;
	}

	public String getEnterpriseID() {
		return enterpriseID;
	}

	public void setEnterpriseID(String enterpriseID) {
		this.enterpriseID = enterpriseID;
	}

	public String getStoreId() {
		return String.valueOf(store.getStoreTO().getId());
	}

	public void setStoreId(String storeId) {
		try {
			store.getStoreTO().setId(Long.parseLong(storeId));
		} catch (NumberFormatException e) {
			store.getStoreTO().setId(0);
		}
	}

	public String getStoreName() {
		LOG.debug("Get called for store name: " + store.getStoreTO().getName());
		return store.getStoreTO().getName();
	}

	public void setStoreName(String storeName) {
		store.getStoreTO().setName(storeName);
	}

	public String getStoreLocation() {
		LOG.debug("Get called for store location: " + store.getStoreTO().getLocation());
		return store.getStoreTO().getLocation();
	}

	public void setStoreLocation(String storeLocation) {
		store.getStoreTO().setLocation(storeLocation);
	}

	public String getNewStoreId() {
		return newStoreId;
	}

	public void setNewStoreId(String newStoreId) {
		this.newStoreId = newStoreId;
	}

	public String getNewStoreName() {
		return newStoreName;
	}

	public void setNewStoreName(String newStoreName) {
		this.newStoreName = newStoreName;
	}

	public String getNewStoreLocation() {
		return newStoreLocation;
	}

	public void setNewStoreLocation(String newStoreLocation) {
		this.newStoreLocation = newStoreLocation;
	}

	public StoreTOWrapper getUpdateStore_data() {
		return updateStore_data;
	}

	public void setUpdateStore_data(StoreTOWrapper updateStore) {
		this.updateStore_data = updateStore;
	}

	public List<String> getMessages() {
		return messages;
	}

	public void setMessages(List<String> messages) {
		this.messages = messages;
	}

	public boolean isShowMessage() {
		return showMessage;
	}

	public void setShowMessage(boolean showMessage) {
		this.showMessage = showMessage;
	}

	public boolean isShowListofProduct() {
		return showListofProduct;
	}

	public void setShowListofProduct(boolean showListofProduct) {
		this.showListofProduct = showListofProduct;
	}

	public List<EnterpriseTOWrapper> getListOfEnterprise() {
		return listOfEnterprise;
	}

	public void setListOfEnterprise(List<EnterpriseTOWrapper> listOfEnterprise) {
		this.listOfEnterprise = listOfEnterprise;
	}

	public List<StoreTOWrapper> getListOfStores() {
		return new ArrayList<StoreTOWrapper>(listOfStores.values());
	}

	public void setListOfStores(List<StoreTOWrapper> listOfStores) {
		this.listOfStores.clear();
		for (StoreTOWrapper store : listOfStores) {
			this.listOfStores.put(store.getStoreTO().getId(), store);
		}
	}

	public UIData getData() {
		return data;
	}

	public void setData(UIData data) {
		this.data = data;
	}

	public UIData getData_stores() {
		return data_stores;
	}

	public void setData_stores(UIData data_stores) {
		this.data_stores = data_stores;
	}

	public boolean isCanCreateStore() {
		return canCreateStore;
	}

	public void setCanCreateStore(boolean canCreateStore) {
		this.canCreateStore = canCreateStore;
	}

	public boolean isViewData() {
		return viewData;
	}

	public void setViewData(boolean viewData) {
		this.viewData = viewData;
	}

	public List<ProductWSSTOWrapper> getListOfNewStockItems() {
		return listOfNewStockItems;
	}

	public void setListOfNewStockItems(List<ProductWSSTOWrapper> listOfNewStockItems) {
		this.listOfNewStockItems = listOfNewStockItems;
	}

	public ProductWSSTOWrapper getStockItem() {
		return stockItem;
	}

	public void setStockItem(ProductWSSTOWrapper stockItem) {
		this.stockItem = stockItem;
	}

	/**
	 * @return the listOfStockItem
	 */
	public List<ProductWSSTOWrapper> getListOfStockItem() {
		return listOfStockItem.getListOfStockItems();
	}

	/**
	 * @param listOfStockItem the listOfStockItem to set
	 */
	public void setListOfStockItem(List<ProductWSSTOWrapper> listOfStockItem) {
		this.listOfStockItem.setListOfStockItems(listOfStockItem);
	}

	public boolean isViewStores() {
		return viewStores;
	}

	public void setViewStores(boolean viewStores) {
		this.viewStores = viewStores;
	}

	public boolean isEditStore() {
		return editStore;
	}

	public void setEditStore(boolean editStore) {
		this.editStore = editStore;
	}
	
	public String setCreateEnterprise () {
		finish();
		this.showCreateEnterprise = true;
		return "set_create_enterprise";
	}
	
	private void disableAllViewElements() {
		this.editStore = false;
		this.showCreateEnterprise = false;
		this.showCreateProduct = false;
		this.showCreateStockItem = false;
		this.showCreateStore = false;
		this.showListOfNewStockItems = false;
		this.showListofProduct = false;
		this.showListOfStockItem = false;
		this.showMessage = false;
		this.viewData = false;
		this.viewStores = false;
		this.canCreateStore = false;
		this.messages.clear();
	}
	
	@Override
	public String invoke() throws Exception {
		this.showCreateStore = false;

		try {
			enterpriseManager.createEnterprise(enterpriseName);
		} catch (CreateException_Exception | SOAPFaultException e) {
			this.messages.add("Could not create the enterprise. Reason: " + e.getMessage());
			this.showMessage = true;
		}
		
		return "Get ALL";
	}

	public String getAllFromDB(){
		disableAllViewElements();
		
		String pos = "init";
		try {
			List<EnterpriseTO> enterprises = enterpriseManager.getEnterprises();
			pos = "list " + enterprises.size();
			this.listOfEnterprise = new ArrayList<EnterpriseTOWrapper>(
					enterprises.size());
			pos = "stored list " + enterprises.size();
			for (EnterpriseTO enterprise : enterprises) {
				listOfEnterprise.add(new EnterpriseTOWrapper(enterprise));
			}
			pos = "looped";
			this.viewData = true;
		} catch (SOAPFaultException e) {
			this.messages.add("Could not connect to database! " + e.getMessage() + " " 
					+ pos
			);
			this.showMessage = true;
		}
		return "Get ALL";
	}
	
	public String getAllProduct() {
		disableAllViewElements();
		
		try {
			fillListOfProducts();
			this.showListofProduct = true;
		} catch (SOAPFaultException e) {
			this.messages.add("Error connecting to the database!");
			this.showMessage = true;
			this.showListofProduct = false;
		}

		return "get_all_Product";
	}

	private void fillListOfProducts() {
		List<ProductTO> products = 
				enterpriseManager.getAllProducts();
		this.listOfProduct = new ArrayList<ProductTOWrapper>(products.size());
		for (ProductTO product : products) {
			listOfProduct.add(new ProductTOWrapper(product));
		}
	}
	
	public String setToGetAllStockItem() {
		StoreTOWrapper store = (StoreTOWrapper) data_stores.getRowData();
		setStore(store);
		disableAllViewElements();
		try {
			listOfStockItem = stockItemListFactory.getStockItemList(store.getStoreTO().getId());
		} catch (SOAPFaultException | NotInDatabaseException_Exception exc) {
			this.messages.add("Could not find the store in the database!");
			this.showMessage = true;
		}
		this.showListOfStockItem = true;
		viewStores = true;
		return null;
	}
	
	public String getAllStockItem() {
		setToGetAllStockItem();
		
		return "get_all_StockItem";
	}
	
	public String setEditStockItemAction() {
		fillListOfProducts();
		showListofProduct = true;
		ProductWSSTOWrapper s = (ProductWSSTOWrapper) this.data_stockItem.getRowData();
		
		ProductWSSTOWrapper selectedEntry = listOfStockItem.getStockItemWithID(s.getStockItemTO().getId());
		if(selectedEntry != null) {
			selectedEntry.setEditingEnabled(true);
			this.stockItem = selectedEntry;
		}
		
		return "";
	}
	
	public String saveStockItemAction() {		
		try {
			storeManager.updateStockItem(Long.parseLong(getStoreId()), stockItem.getStockItemTO());
			stockItemListFactory.setReloadNeeded(Long.parseLong(getStoreId()), true);
		} catch (NumberFormatException | NotInDatabaseException_Exception 
				| UpdateException_Exception e) {
			LOG.error("Got exception while updating stock item: " + e);
			this.messages.add("Could not update the stock item!");
			this.showMessage = true;
		}

		return "save_StockItem_Action";
	}
	public String getStores() {
		disableAllViewElements();
		
		EnterpriseTOWrapper enterprise = (EnterpriseTOWrapper) this.data.getRowData();
		
		try {
			Collection<StoreWithEnterpriseTO> storeTOs = 
					enterpriseManager.queryStoresByEnterpriseID(enterprise.getEnterpriseTO().getId());
			
			int initialCapacity = (int) ((storeTOs.size() / 0.75) + 1);
			this.listOfStores = new HashMap<Long, StoreTOWrapper>(initialCapacity);
			
			for (StoreWithEnterpriseTO storeTO : storeTOs) {
				listOfStores.put(storeTO.getId(), new StoreTOWrapper(storeTO));
			}
			viewStores = true;
		} catch (NotInDatabaseException_Exception | SOAPFaultException e) {
			LOG.error("Got exception while getting stores: " + e);
			this.messages.add("Could not find the enterprise with this id in the database!");
			this.showMessage = true;
		}
		return "getStores";
	}

	public String goToStore(StoreTOWrapper store) {
		setEnterpriseName(store.getEnterpriseTO().getName());
		setStore(store);
		return "success_store";
	}

	public String saveStoreAction() {
		try {
			enterpriseManager.updateStore(this.store.getStoreTO());
		} catch (NotInDatabaseException_Exception e) {
			LOG.error("Got exception while updating store: " + e);
			this.messages.add("Could not find the enterprise with this id in the database!");
			this.showMessage = true;
		} catch (UpdateException_Exception | SOAPFaultException e) {
			LOG.error("Got exception while updating store: " + e);
			this.messages.add("Could not update the store!");
			this.showMessage = true;
		}

		return "getStores";
	}
	
	public String editStoreAction() {
		StoreTOWrapper s = (StoreTOWrapper) this.data_stores.getRowData();
		StoreTOWrapper selectedStore = listOfStores.get(s.getStoreTO().getId());
		
		if (selectedStore != null) {
			selectedStore.setEditingEnabled(true);
		}
		return "getStores";
	}

	public void setEditStoreMethode() {
		disableAllViewElements();
		viewStores = true;
		editStore = true;
	}

	public String editStores() {
		StoreTOWrapper e = (StoreTOWrapper) data_stores.getRowData();
		this.updateStore_data.getStoreTO().setId((e.getStoreTO().getId()));
		
		return "edit";
	}
	
	public String setCreateStore() {
		EnterpriseTOWrapper e = (EnterpriseTOWrapper) data.getRowData();
		this.updateStore_data = new StoreTOWrapper();
		this.updateStore_data.getEnterpriseTO().setName(e.getEnterpriseTO().getName());
		disableAllViewElements();
		this.showCreateStore = true;
		return "set create store";
	}
	
	public String createStore()  {
		if (!newStoreName.trim().matches("\\p{Lu}[\\p{L}+\\p{Zs}?\\p{L}*]+")) {
			FacesContext
					.getCurrentInstance()
					.addMessage(
							"db:createstore",
							new FacesMessage("Error",
									"The new store name must start with [A-Z] and end with [a-z]*."));
			return "create Store";
		}

		if (!newStoreLocation.trim().matches("\\p{Lu}[\\p{L}+\\p{Zs}?\\p{L}*]+")) {
			FacesContext
					.getCurrentInstance()
					.addMessage(
							"db:createstore",
							new FacesMessage("Error",
									"The new store location must start with [A-Z] and end with [a-z]*."));
			return "create Store";
		}

		try {
			StoreWithEnterpriseTO newStore = new StoreWithEnterpriseTO();
			newStore.setEnterpriseTO(updateStore_data.getEnterpriseTO());
			newStore.setId(0);
			newStore.setLocation(newStoreLocation);
			newStore.setName(newStoreName);
			
			enterpriseManager.createStore(newStore);
			FacesContext.getCurrentInstance()
			.addMessage(
					"db:createstore",
					new FacesMessage("Success",
							"Store created successfully."));
		} catch (CreateException_Exception | SOAPFaultException e) {
			FacesContext.getCurrentInstance()
					.addMessage(
							"db:createstore",
							new FacesMessage("Error",
									"Could not create store. Reason: "
											+ e.getMessage()));
		}
		return "create Store";
	}
	
	@Override
	public void attrListener(ActionEvent event) {
		this.enterpriseName = (String) event.getComponent().getAttributes()
				.get("enterpriseName");
		setStoreId(String.valueOf(event.getComponent().getAttributes()
				.get("storeId")));
		setStoreName((String) event.getComponent().getAttributes()
				.get("storeName"));
		setStoreLocation((String) event.getComponent().getAttributes()
				.get("storeLocation"));

	}
	
	public String setCreateProduct() {
		disableAllViewElements();
		showCreateProduct = true;
		return "set_create_product";
	}
	
	public String createProduct() {
		
		if (this.productName.isEmpty() || this.productBarcode.isEmpty() || this.purchasePrice.isEmpty()) {
			FacesContext.getCurrentInstance().addMessage("db:createproduct",
					new FacesMessage("Error", "Barcode, purchase price and name must not be empty!"));
			return "create_Product";
		}
		
		ProductTO product = new ProductTO();
		try {
			product.setBarcode(edu.kit.ipd.sdq.evaluation.Barcode.parseInput(productBarcode));
			product.setName(productName);
			product.setPurchasePrice(Double.parseDouble(purchasePrice));
		} catch (NumberFormatException e) {
			FacesContext.getCurrentInstance().addMessage("db:createproduct",
					new FacesMessage("Error", "Barcode and purchase price have to be numbers!"));
			return "create_Product";
		}

		try {
			enterpriseManager.createProduct(product);
			messages.add("Product created successfully!");
			showMessage = true;
		} catch (CreateException_Exception | SOAPFaultException e) {
			FacesContext.getCurrentInstance().addMessage("db:createproduct",
					new FacesMessage("Error", "Product could not be created!"));
			LOG.error("Create exception while creating product: " + e);
			return "create_Product";
		}
		
		return "get_all_Product";
	}
	
	public String setCreateStockItem() {	
		disableAllViewElements();
		fillListOfProducts();
		this.showCreateStockItem = true;
		this.showListofProduct = true;
		
		StoreTOWrapper e  = (StoreTOWrapper) this.data_stores.getRowData();
		setStoreId(String.valueOf(e.getStoreTO().getId()));
		this.listOfNewStockItems.clear();
		addNewStockItem();
		setShowListOfNewStockItems(true);
		
		return "Set_Create_StockItem";
	}
	
	public String addNewStockItem() {
		this.stockItem = new ProductWSSTOWrapper();
		this.listOfNewStockItems.add(stockItem);
		return "add_New_Stock_Item";
	}
	
	public String createStockItem()  {
		messages.add("Creation of stock items:");
		
		List<ProductWSSTOWrapper> sucessfullyCreated = new LinkedList<ProductWSSTOWrapper>();
		
		for (ProductWSSTOWrapper item : listOfNewStockItems) {
			ProductWithStockItemTO product = new ProductWithStockItemTO();
			product.setStockItemTO(item.getStockItemTO());
			product.setBarcode(item.getBarcode());
			product.setId(item.getId());
			product.setName(item.getName());
			product.setPurchasePrice(item.getPurchasePrice());
			try {
				storeManager.createStockItem(Long.parseLong(getStoreId()), product);
				stockItemListFactory.setReloadNeeded(Long.parseLong(getStoreId()), true);
				messages.add("Stock item with barcode " + product.getBarcode() + ": Successfully created!");
				sucessfullyCreated.add(item);
			} catch (NumberFormatException e) {
				messages.add(product.getBarcode() + ": Error! " + e.getMessage());
			} catch (SOAPFaultException e) {
				messages.add(product.getBarcode() + ": Error! " + 
						(e.getFault() == null ? "" :e.getFault().getDetail().toString()));
			} catch (NotInDatabaseException_Exception | CreateException_Exception e) {
				messages.add(product.getBarcode() + ": Error! " + e.getMessage());
			}
		}
		
		listOfNewStockItems.removeAll(sucessfullyCreated);
		
		this.showMessage = true;
		for (String message : messages) {
			LOG.debug("Message: " + message);
		}
		
		return null;
		
	}
	
	@Override
	public String finish() {
		disableAllViewElements();
		data = new UIData();
		enterpriseName = "";
		store = new StoreTOWrapper();
		///////////////////////////
		product = null;
		productName = "";
		productBarcode = "";
		purchasePrice = "";
		///////////////////////////
		stockItem = new ProductWSSTOWrapper();
		listOfNewStockItems.clear();
		// ////////////////////////
		data_stores = new UIData();
		newStoreId = "";
		newStoreName = "";
		newStoreLocation = "";
		// ////////////////////////
		messages.clear();
		updateStore_data = new StoreTOWrapper();
		listOfEnterprise.clear();
		listOfStores.clear();
		return null;
	}

	@Override
	public void processAction(ActionEvent arg0) throws AbortProcessingException {
		finish();
		
	}

	public StoreTOWrapper getStore() {
		return store;
	}

	public void setStore(StoreTOWrapper store) {
		LOG.debug("Setting new store in create enterprise: [ID: " + store.getStoreTO().getId() 
				+ ", Name: " + store.getStoreTO().getName() + ", Location: " + store.getStoreTO().getLocation());
		this.store = store;
	}

	public boolean isShowListOfNewStockItems() {
		return showListOfNewStockItems;
	}

	public void setShowListOfNewStockItems(boolean showListOfNewStockItems) {
		this.showListOfNewStockItems = showListOfNewStockItems;
	}
	
	
}
