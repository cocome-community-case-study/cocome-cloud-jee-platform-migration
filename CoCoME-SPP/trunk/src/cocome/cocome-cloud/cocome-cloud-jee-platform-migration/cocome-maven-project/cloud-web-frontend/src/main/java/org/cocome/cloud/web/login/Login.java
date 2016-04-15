package org.cocome.cloud.web.login;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIData;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import javax.inject.Inject;
import javax.xml.ws.WebServiceRef;
import javax.xml.ws.soap.SOAPFaultException;

import org.apache.log4j.Logger;
import org.cocome.cloud.logic.stub.IStoreManager;
import org.cocome.cloud.logic.stub.IStoreManagerService;
import org.cocome.cloud.logic.stub.NotInDatabaseException_Exception;
import org.cocome.tradingsystem.inventory.application.store.StoreWithEnterpriseTO;


@ManagedBean
@SessionScoped
public class Login {

	@Inject
	IAuthenticator authenticator;
	
	@WebServiceRef(IStoreManagerService.class)
	IStoreManager storeManager;
	
	private UIData data = new UIData();
	private String userName = "Username or Email";
	private String password = "Password";
	private String manager = "Store";
	private String enterpriseName = "Enterprise Name";
	private String storeId = "Store ID";
	private String storeName = "Store Name";
	private String storeLocation = "Store Location";
	private boolean setUser = false;
	private boolean ifStore = true;
	private String per;
	private boolean invalid = false;
	
	private static final Logger LOG = Logger.getLogger(Login.class);

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getManager() {
		return manager;
	}

	public void setManager(String manager) {
		this.manager = manager;
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

	public boolean getIfStore() {
		return ifStore;
	}

	public void setIfStore(boolean ifStore) {
		this.ifStore = ifStore;
	}

	/**
	 * @return the setUser
	 */
	public boolean isSetUser() {
		return setUser;
	}

	/**
	 * @param setUser the setUser to set
	 */
	public void setSetUser(boolean setUser) {
		this.setUser = setUser;
	}

	public Login() {
		this.per = "nothing";

	}

	public String setUserPer() {
		if (manager != null && userName != null && password != null) {
			// TODO don't instantiate this here, better call the authenticator for it
			IUser user = new DummyUser(userName, password);
			IPermission permission = new DummyPermission(manager);
			
			LOG.info("Trying to authenticate user: " + user.getUsername());
			
			if (authenticator.checkCredentials(user) && authenticator.checkHasPermission(user, permission)) {
				LOG.info("User authenticated successfully");
				this.setUser = true;
				this.ifStore = true;
				this.invalid = false;
				this.per = user.getUsername();
				if (manager.equals("Database Manager")) {
					return "success_enterprise";
				} else if (checkStoreAndFillInfo()){
					return "success_store";
				}
			}
		}
		this.invalid = true;
		return "invalid";

	}

	public String getPer() {
		return this.per;
	}

	public boolean isInvalid() {
		return invalid;
	}

	public void setInvalid(boolean invaild) {
		this.invalid = invaild;
	}

	public UIData getData() {
		return data;
	}

	public void setData(UIData data) {
		this.data = data;
	}

	public String logout() {
		this.invalid = false;
		this.setUser = false;
		this.userName = "Username or Email";
		this.per = "nothing";
		FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
		return "logout";
	}

	public void selectItem(ValueChangeEvent event) {
		
		manager = event.getNewValue().toString();
		if (manager.equals("Database Manager")) {
			this.ifStore = false;
		} else {
			this.ifStore = true;
		}
	}
	
	private boolean checkStoreAndFillInfo() {
		StoreWithEnterpriseTO storeTO;
		try {
			storeTO = storeManager.getStore(Integer.parseInt(storeId));
		} catch (NotInDatabaseException_Exception e) {
			LOG.error(e.getFaultInfo().getMessage());
			return false;
		} catch (SOAPFaultException e) {
			LOG.error("SoapFault during invocation: " + e.toString() + "\nContent: " + e.getFault());
			return false;
		}
		
		fillStoreInformation(storeTO);
		
		return true;
	}

	public void fillStoreInformation(StoreWithEnterpriseTO storeTO) {
		LOG.debug(String.format(
				"Filling new store information for user %s to [%s, %d, %s, %s].",
				this.userName, storeTO.getEnterpriseTO().getName(),
				storeTO.getId(), storeTO.getName(), storeTO.getLocation()));
		this.enterpriseName = storeTO.getEnterpriseTO().getName();
		this.storeId = String.valueOf(storeTO.getId());
		this.storeName = storeTO.getName();
		this.storeLocation = storeTO.getLocation();
		this.ifStore = true;
	}

}
