package org.cocome.cloud.web.frontend.navigation;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;

import java.io.Serializable;
import java.util.Locale;
import java.util.ResourceBundle;

@SessionScoped
public class NavigationElementLabelResolver implements Serializable, ILabelResolver {
	private static final long serialVersionUID = 7607903884265925992L;
	
	private ResourceBundle strings;
	
	@PostConstruct
	private void initResourceBundle() {
		FacesContext ctx = FacesContext.getCurrentInstance();
	    Locale locale = ctx.getViewRoot().getLocale();

		strings = ResourceBundle.getBundle(
	            "cocome.frontend.Strings", locale);
	}
	
	/* (non-Javadoc)
	 * @see org.cocome.cloud.web.frontend.navigation.ILabelResolver#getLabel(java.lang.String)
	 */
	@Override
	public String getLabel(String navOutcome) {
		StringBuilder sb = new StringBuilder();
		sb.append("navigation.");
		sb.append(navOutcome);
		sb.append(".label");
		return strings.getString(sb.toString());
	}

}
