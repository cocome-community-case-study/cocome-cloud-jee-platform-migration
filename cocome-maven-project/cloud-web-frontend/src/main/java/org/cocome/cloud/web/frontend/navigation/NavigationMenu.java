package org.cocome.cloud.web.frontend.navigation;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.enterprise.event.Observes;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.management.ImmutableDescriptor;

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
	
	private static Map<NavigationStates, List<NavigationElement>> STATE_MAP;
	
	private LinkedHashMap<NavigationElements, NavigationElement> elements = new LinkedHashMap<>();

	@Inject
	ILabelResolver labelResolver;
	

	@PostConstruct
	private synchronized void initViewLists() {
		if (STATE_MAP != null) {
			return;
		}
		
		List<NavigationElement> enterpriseViewList = populateEnterpriseView();
		
		List<NavigationElement> storeViewList = populateStoreView();
		
		List<NavigationElement> cashpadViewList = populateCashpadView();
		
		STATE_MAP = new HashMap<>(NavigationStates.values().length, 1);
		STATE_MAP.put(NavigationStates.ENTERPRISE_VIEW, enterpriseViewList);
		STATE_MAP.put(NavigationStates.STORE_VIEW, storeViewList);
		STATE_MAP.put(NavigationStates.CASHPAD_VIEW, cashpadViewList);
		STATE_MAP = Collections.unmodifiableMap(STATE_MAP);
		
	}

	private List<NavigationElement> populateCashpadView() {
		List<NavigationElement> cashpadViewList = new LinkedList<>();
		cashpadViewList.add(new NavigationElement(NavigationElements.BACK, labelResolver));
		cashpadViewList.add(new NavigationElement(NavigationElements.LOGOUT, labelResolver));
		return cashpadViewList;
	}

	private List<NavigationElement> populateStoreView() {
		List<NavigationElement> storeViewList = new LinkedList<>();
		storeViewList.add(new NavigationElement(NavigationElements.START_SALE, labelResolver));
		storeViewList.add(new NavigationElement(NavigationElements.ORDER_PRODUCTS, labelResolver));
		storeViewList.add(new NavigationElement(NavigationElements.SHOW_REPORTS, labelResolver));
		storeViewList.add(new NavigationElement(NavigationElements.CHANGE_PRICE, labelResolver));
		storeViewList.add(new NavigationElement(NavigationElements.RECEIVE_PRODUCTS, labelResolver));
		storeViewList.add(new NavigationElement(NavigationElements.LOGOUT, labelResolver));
		return storeViewList;
	}

	private List<NavigationElement> populateEnterpriseView() {
		List<NavigationElement> enterpriseViewList = new LinkedList<>();
		enterpriseViewList.add(new NavigationElement(NavigationElements.SHOW_ENTERPRISES, labelResolver));
		enterpriseViewList.add(new NavigationElement(NavigationElements.CREATE_ENTERPRISE, labelResolver));
		enterpriseViewList.add(new NavigationElement(NavigationElements.CREATE_PRODUCT, labelResolver));
		enterpriseViewList.add(new NavigationElement(NavigationElements.SHOW_PRODUCTS, labelResolver));
		enterpriseViewList.add(new NavigationElement(NavigationElements.LOGOUT, labelResolver));
		return enterpriseViewList;
	}
	
	/* (non-Javadoc)
	 * @see org.cocome.cloud.shop.navigation.INavigationMenu#getElements()
	 */
	@Override
	public List<NavigationElement> getElements() {
		if (elements.isEmpty()) {
			elements.put(NavigationElements.LOGOUT, new NavigationElement(
					NavigationElements.LOGOUT.getNavigationOutcome(), labelResolver.getLabel(NavigationElements.LOGOUT.getNavigationOutcome())));
		}
		return new ArrayList<NavigationElement>(elements.values());
	}
	
	/* (non-Javadoc)
	 * @see org.cocome.cloud.shop.navigation.INavigationMenu#removeElement(java.lang.String)
	 */
	@Override
	public void removeElement(String viewID) {
		elements.remove(viewID);
	}
	
	/* (non-Javadoc)
	 * @see org.cocome.cloud.shop.navigation.INavigationMenu#addElement(java.lang.String, org.cocome.cloud.shop.navigation.NavigationElement)
	 */
	@Override
	public void addElement(String viewID, NavigationElement element) {
		elements.put(viewID, element);
	}

	@Override
	public void updateNavigationMenu(@Observes IUpdateNavigationEvent event) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void changeStateTo(NavigationStates newState) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public NavigationStates getCurrentState() {
		// TODO Auto-generated method stub
		return null;
	}
}
