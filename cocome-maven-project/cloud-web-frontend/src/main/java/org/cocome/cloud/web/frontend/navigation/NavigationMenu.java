package org.cocome.cloud.web.frontend.navigation;

import org.cocome.cloud.web.data.logindata.IUser;
import org.cocome.cloud.web.events.ChangeViewEvent;
import org.cocome.cloud.web.events.LoginEvent;
import org.cocome.cloud.web.events.LogoutEvent;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.inject.Named;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.*;

/**
 * Implements the navigation menu for the site.
 *
 * @author Rudolf Biczok
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
    private ILabelResolver labelResolver;

    private IUser currentUser;

    private NavigationViewStates navigationState = NavigationViewStates.DEFAULT_VIEW;


    @PostConstruct
    private synchronized void initViewLists() {
        if (STATE_MAP != null) {
            return;
        }

        STATE_MAP = new HashMap<>(NavigationViewStates.values().length, 1);
        STATE_MAP.put(NavigationViewStates.ENTERPRISE_VIEW, populateEnterpriseView());
        STATE_MAP.put(NavigationViewStates.STORE_VIEW, populateStoreView());
        STATE_MAP.put(NavigationViewStates.PLANT_VIEW, populatePlantView());
        STATE_MAP.put(NavigationViewStates.CASHPAD_VIEW, populateCashpadView());
        STATE_MAP.put(NavigationViewStates.DEFAULT_VIEW, populateDefaultView());
        STATE_MAP = Collections.unmodifiableMap(STATE_MAP);
    }

    private List<INavigationElement> populateCashpadView() {
        List<INavigationElement> cashpadViewList = new LinkedList<>();
        cashpadViewList.add(new NavigationElement(NavigationElements.ENTERPRISE_MAIN, labelResolver));
        return cashpadViewList;
    }

    private List<INavigationElement> populatePlantView() {
        List<INavigationElement> plantViewList = new LinkedList<>();
        plantViewList.add(new NavigationElement(NavigationElements.PLANT_OPERATION, labelResolver));
        plantViewList.add(new NavigationElement(NavigationElements.PLANT_PU, labelResolver));
        plantViewList.add(new NavigationElement(NavigationElements.PLANT_PUC, labelResolver));
        return plantViewList;
    }

    private List<INavigationElement> populateStoreView() {
        List<INavigationElement> storeViewList = new LinkedList<>();
        storeViewList.add(new NavigationElement(NavigationElements.START_SALE, labelResolver));
        storeViewList.add(new NavigationElement(NavigationElements.SHOW_STOCK, labelResolver));
        storeViewList.add(new NavigationElement(NavigationElements.STOCK_REPORT, labelResolver));
        storeViewList.add(new NavigationElement(NavigationElements.RECEIVE_PRODUCTS, labelResolver));
        return storeViewList;
    }

    private List<INavigationElement> populateEnterpriseView() {
        List<INavigationElement> enterpriseViewList = new LinkedList<>();
        enterpriseViewList.add(new NavigationElement(NavigationElements.SHOW_ENTERPRISES, labelResolver));
        enterpriseViewList.add(new NavigationElement(NavigationElements.SHOW_PRODUCTS, labelResolver));

        return enterpriseViewList;
    }

    private List<INavigationElement> populateDefaultView() {
        return new LinkedList<>();
    }

    @Override
    public List<INavigationElement> getElements() {
        if (elements == null || elements.isEmpty()) {
            elements = new LinkedList<>(STATE_MAP.get(NavigationViewStates.DEFAULT_VIEW));
        }
        return elements;
    }

    @Override
    public String changeStateTo(@NotNull NavigationViewStates newState) {
        navigationState = newState;
        elements = new LinkedList<>(STATE_MAP.get(navigationState));

        Iterator<INavigationElement> iterator = elements.iterator();

        if (currentUser == null) {
            navigationState = NavigationViewStates.DEFAULT_VIEW;
            elements = STATE_MAP.get(NavigationViewStates.DEFAULT_VIEW);
            return NavigationElements.LOGIN.getNavigationOutcome();
        }

        while (iterator.hasNext()) {
            INavigationElement element = iterator.next();
            if (element.getRequiredPermission() != null &&
                    !currentUser.hasPermissionString(element.getRequiredPermission())) {
                iterator.remove();
            }
        }

        switch (newState) {
            case CASHPAD_VIEW:
                return NavigationElements.START_SALE.getNavigationOutcome();
            case STORE_VIEW:
                return NavigationElements.STORE_MAIN.getNavigationOutcome();
            case PLANT_VIEW:
                return NavigationElements.PLANT_MAIN.getNavigationOutcome();
            case ENTERPRISE_VIEW:
                return NavigationElements.SHOW_ENTERPRISES.getNavigationOutcome();
            default:
                return NavigationElements.LOGIN.getNavigationOutcome();
        }
    }

    @Override
    public NavigationViewStates getCurrentState() {
        return navigationState;
    }

    public void observeLoginEvent(@Observes LoginEvent loginEvent) {
        this.currentUser = loginEvent.getUser();
        changeStateTo(loginEvent.getRequestedView());
    }

    public void observeLogoutEvent(@Observes LogoutEvent logoutEvent) {
        this.currentUser = null;
        changeStateTo(NavigationViewStates.DEFAULT_VIEW);
    }

    public void observeChangeViewEvent(@Observes ChangeViewEvent changeEvent) {
        changeStateTo(changeEvent.getNewViewState());
    }
}
