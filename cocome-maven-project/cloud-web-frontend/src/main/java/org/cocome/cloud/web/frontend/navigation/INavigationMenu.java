package org.cocome.cloud.web.frontend.navigation;

import java.util.Collection;

/**
 * Interface representing the navigation menu on the site. 
 * 
 * @author Tobias Pöppke
 * @author Robert Heinrich
 */
public interface INavigationMenu {

	Collection<INavigationElement> getElements();
	
	void changeStateTo(NavigationStates newState);
	
	NavigationStates getCurrentState();
}