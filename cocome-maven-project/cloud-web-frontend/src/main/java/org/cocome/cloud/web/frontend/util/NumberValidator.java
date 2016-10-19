package org.cocome.cloud.web.frontend.util;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.inject.Named;

@Named
public class NumberValidator extends InputValidator {

	private static double tempDouble = 0.0;
	
	private static long tempLong = 0;
	
	private static boolean validateDouble(String input) {
		try {
			tempDouble = Double.parseDouble(input);
			return true;
		} catch (NumberFormatException e) {
			tempDouble = 0.0;
			return false;
		}
	}
	
	private static boolean validateLong(String input) {
		try {
			tempLong = Long.parseLong(input);
			return true;
		} catch (NumberFormatException e) {
			tempLong = 0;
			return false;
		}
	}
	
	public static void validateLong(FacesContext context, UIComponent comp, Object value) {
		String input = (String) value;
		
		if (!validateLong(input)) {
			handleFailedValidation(context, comp);
		}
	}
	
	public static void validatePositiveLong(FacesContext context, UIComponent comp, Object value) {
		String input = (String) value;
		
		if (!validateLong(input) || tempLong < 0) {
			handleFailedValidation(context, comp);
		}
		tempLong = 0;
	}
	
	public static void validateDouble(FacesContext context, UIComponent comp, Object value) {
		String input = (String) value;
		
		if (!validateDouble(input)) {
			handleFailedValidation(context, comp);
		}
	}
	
	public static void validatePositiveDouble(FacesContext context, UIComponent comp, Object value) {
		String input = (String) value;
		
		if (!validateDouble(input) || tempDouble < 0) {
			handleFailedValidation(context, comp);
		}
		tempDouble = 0.0;
	}
}
