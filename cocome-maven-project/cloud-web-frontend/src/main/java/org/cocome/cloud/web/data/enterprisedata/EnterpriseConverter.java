package org.cocome.cloud.web.data.enterprisedata;

import javax.enterprise.context.RequestScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.log4j.Logger;
import org.cocome.cloud.web.connector.enterpriseconnector.IEnterpriseQuery;

/**
 * Converts an Enterprise from the UI representation into an {@link Enterprise} 
 * object or vice versa. 
 * 
 * @author Tobias Pöppke
 * @author Robert Heinrich
 */
@Named
@RequestScoped
public class EnterpriseConverter implements Converter {
	private static final Logger LOG = Logger.getLogger(EnterpriseConverter.class);

	@Inject
	IEnterpriseQuery enterpriseQuery;
	
	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		LOG.debug("Converting String: " + value);
		
		if (value == null || value.isEmpty()) {
            return null;
        }
		
        Long enterpriseID = Long.valueOf(value);
        Enterprise enterprise = null;
		enterprise = enterpriseQuery.getEnterpriseByID(enterpriseID); 
        
        if (enterprise == null) {
        	throw new ConverterException("The value is not a valid Enterprise ID: " + value);
        }
        
        return enterprise;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) {
		LOG.debug("Converting object: " + value);
		if (value == null) {
            return "";
        }

        if (value instanceof Enterprise) {
            Long enterpriseID = ((Enterprise) value).getId();
            return (enterpriseID != null) ? String.valueOf(enterpriseID) : null;
        } else {
            throw new ConverterException("The value is not a valid Enterprise instance: " + value);
        }
	}

}
