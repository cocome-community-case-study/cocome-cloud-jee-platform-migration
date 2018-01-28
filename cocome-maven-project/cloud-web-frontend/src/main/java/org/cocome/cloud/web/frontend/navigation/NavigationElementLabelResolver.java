package org.cocome.cloud.web.frontend.navigation;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;

import org.apache.log4j.Logger;

import java.io.Serializable;
import java.util.Locale;
import java.util.ResourceBundle;

@SessionScoped
public class NavigationElementLabelResolver implements Serializable, ILabelResolver {
	private static final long serialVersionUID = 7607903884265925992L;
	private static final Logger LOG = Logger.getLogger(NavigationElementLabelResolver.class);
	
	private ResourceBundle strings;
	
	@PostConstruct
	private void initResourceBundle() {
		FacesContext ctx = FacesContext.getCurrentInstance();
	    Locale locale = ctx.getViewRoot().getLocale();

		strings = ResourceBundle.getBundle(
	            "cocome.frontend.Strings", locale);
	}
	
	@Override
	public String getLabel(String navOutcome) {
		String transformedOutcome = transformOutcomeToRef(navOutcome);

		String sb = "navigation." +
				transformedOutcome +
				".label";
		return strings.getString(sb);
	}

	private String transformOutcomeToRef(String navOutcome) {
		String transformedOutcome = navOutcome;
		LOG.debug(String.format("Transforming %s...", navOutcome));
		
		if (transformedOutcome.startsWith("/")) {
			transformedOutcome = transformedOutcome.substring(1);
			transformedOutcome = transformedOutcome.replaceAll("/+", "\\.");
			LOG.debug(String.format("Transformed string: %s", transformedOutcome));
		}
		return transformedOutcome;
	}

}
