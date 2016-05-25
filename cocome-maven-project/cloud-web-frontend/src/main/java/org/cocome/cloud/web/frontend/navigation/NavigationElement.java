package org.cocome.cloud.web.frontend.navigation;

/**
 * Represents an element inside an {@link INavigationMenu}.
 * 
 * @author Tobias Pöppke
 * @author Robert Heinrich
 */
public class NavigationElement implements INavigationElement {
	private String navOutcome;
	private String displayText;
	private ILabelResolver resolver;
	
	/**
	 * 
	 *
	 * @param navElement
	 * @param resolver
	 */
	public NavigationElement(NavigationElements navElement, ILabelResolver resolver) {
		this(navElement.getNavigationOutcome(), null, resolver);
	}
	
	/**
	 * 
	 * @param navOutcome
	 * @param displayText
	 */
	public NavigationElement(String navOutcome, String displayText, ILabelResolver resolver) {
		this.navOutcome = navOutcome;
		this.displayText = displayText;
		this.resolver = resolver;
	}
	
	/* (non-Javadoc)
	 * @see org.cocome.cloud.web.frontend.navigation.INavigationElement#getNavOutcome()
	 */
	@Override
	public String getNavOutcome() {
		return navOutcome;
	}
	
	/**
	 * 
	 * @param navOutcome
	 */
	public void setNavOutcome(String navOutcome) {
		this.navOutcome = navOutcome;
	}
	
	/* (non-Javadoc)
	 * @see org.cocome.cloud.web.frontend.navigation.INavigationElement#getDisplayText()
	 */
	@Override
	public String getDisplayText() {
		if ((displayText == null || displayText.equals("")) && resolver != null) {
			displayText = resolver.getLabel(navOutcome);
		}
		return displayText;
	}
	
	/**
	 * 
	 * @param displayText
	 */
	public void setDisplayText(String displayText) {
		this.displayText = displayText;
	}
}
