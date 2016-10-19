package org.cocome.cloud.web.frontend.util;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;

public abstract class InputValidator {
	public static void handleFailedValidation(FacesContext context, UIComponent comp) {
		((UIInput) comp).setValid(false);
	}
	
	public static void handleFailedValidationMessage(FacesContext context, UIComponent comp, String message) {
		handleFailedValidation(context, comp);
		FacesMessage wrongInputMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, message, "");
		context.addMessage(comp.getClientId(), wrongInputMessage);
	}
}
