package org.cocome.cloud.web.frontend.navigation;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.inject.Named;
import javax.validation.constraints.NotNull;

import org.cocome.cloud.web.events.ChangeViewEvent;
import org.cocome.cloud.web.events.LoginEvent;
import org.cocome.cloud.web.events.LogoutEvent;

/**
 * Implements the navigation menu for the site.
 * 
 * @author Tobias Pöppke
 * @author Robert Heinrich
 */
@Named
@SessionScoped
public class NavigationMenu implements INavigationMenu, Serializable {
	private static final long serialVersionUID = -6352541874730024270L;
	
	private static Map<NavigationViewStates, List<INavigationElement>> STATE_MAP;
	
	private List<INavigationElement> elements;

	@Inject
	ILabelResolver labelResolver;
	
	private NavigationViewStates navigationState = NavigationViewStates.DEFAULT_VIEW;
	

	@PostConstruct
	private synchronized void initViewLists() {
		if (STATE_MAP != null) {
			return;
		}
		
		List<INavigationElement> enterpriseViewList = populateEnterpriseView();
		
		List<INavigationElement> storeViewList = populateStoreView();
		
		List<INavigationElement> cashpadViewList = populateCashpadView();
		
		List<INavigationElement> defaultViewList = populateDefaultView();
		
		STATE_MAP = new HashMap<>(NavigationViewStates.values().length, 1);
		STATE_MAP.put(NavigationViewStates.ENTERPRISE_VIEW, enterpriseViewList);
		STATE_MAP.put(NavigationViewStates.STORE_VIEW, storeViewList);
		STATE_MAP.put(NavigationViewStates.CASHPAD_VIEW, cashpadViewList);
		STATE_MAP.put(NavigationViewStates.DEFAULT_VIEW, defaultViewList);
		STATE_MAP = Collections.unmodifiableMap(STATE_MAP);
		
	}

	private List<INavigationElement> populateCashpadView() {
		List<INavigationElement> cashpadViewList = new LinkedList<>();
		cashpadViewList.add(new NavigationElement(NavigationElements.MAIN_PAGE, labelResolver));
		cashpadViewList.add(new NavigationElement(NavigationElements.LOGOUT, labelResolver));
		return cashpadViewList;
	}

	private List<INavigationElement> populateStoreView() {
		List<INavigationElement> storeViewList = new LinkedList<>();
		storeViewList.add(new NavigationElement(NavigationElements.START_SALE, labelResolver));
		storeViewList.add(new NavigationElement(NavigationElements.ORDER_PRODUCTS, labelResolver));
		storeViewList.add(new NavigationElement(NavigationElements.SHOW_REPORTS, labelResolver));
		storeViewList.add(new NavigationElement(NavigationElements.CHANGE_PRICE, labelResolver));
		storeViewList.add(new NavigationElement(NavigationElements.RECEIVE_PRODUCTS, labelResolver));
		storeViewList.add(new NavigationElement(NavigationElements.LOGOUT, labelResolver));
		return storeViewList;
	}

	private List<INavigationElement> populateEnterpriseView() {
		List<INavigationElement> enterpriseViewList = new LinkedList<>();
		enterpriseViewList.add(new NavigationElement(NavigationElements.SHOW_ENTERPRISES, labelResolver));
		enterpriseViewList.add(new NavigationElement(NavigationElements.CREATE_ENTERPRISE, labelResolver));
		enterpriseViewList.add(new NavigationElement(NavigationElements.CREATE_PRODUCT, labelResolver));
		enterpriseViewList.add(new NavigationElement(NavigationElements.SHOW_PRODUCTS, labelResolver));
		// TODO test this version of labels, perhaps the label resolver is not needed
		enterpriseViewList.add(new NavigationElement(NavigationElements.LOGOUT, "#{strings['navigation.logout.label']}", labelResolver));
		return enterpriseViewList;
	}
	
	private List<INavigationElement> populateDefaultView() {
		List<INavigationElement> enterpriseViewList = new LinkedList<>();
		enterpriseViewList.add(new NavigationElement(NavigationElements.LOGOUT, labelResolver));
		return enterpriseViewList;
	}
	
	/* (non-Javadoc)
	 * @see org.cocome.cloud.shop.navigation.INavigationMenu#getElements()
	 */
	@Override
	public List<INavigationElement> getElements() {
		if (elements == null || elements.isEmpty()) {
			elements = STATE_MAP.get(NavigationViewStates.DEFAULT_VIEW);
		}
		return elements;
	}

	@Override
	public void changeStateTo(@NotNull NavigationViewStates newState) {
		navigationState = newState;
		elements = STATE_MAP.get(navigationState);
	}

	@Override
	public NavigationViewStates getCurrentState() {
		return navigationState;
	}
	
	public void observeLoginEvent(@Observes LoginEvent loginEvent) {
		changeStateTo(loginEvent.getRequestedView());
	}
	
	public void observeLogoutEvent(@Observes LogoutEvent logoutEvent) {
		changeStateTo(NavigationViewStates.DEFAULT_VIEW);
	}
	
	public void observeChangeViewEvent(@Observes ChangeViewEvent changeEvent) {
		changeStateTo(changeEvent.getNewViewState());
	}
}
