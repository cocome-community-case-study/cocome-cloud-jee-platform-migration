package org.cocome.cloud.web.data.enterprisedata;

import org.apache.log4j.Logger;
import org.cocome.cloud.logic.stub.NotInDatabaseException_Exception;
import org.cocome.cloud.web.connector.enterpriseconnector.EnterpriseQuery;

import javax.enterprise.context.RequestScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.inject.Inject;
import javax.inject.Named;

/**
 * Converts an Enterprise from the UI representation into an {@link EnterpriseViewData}
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
    private EnterpriseQuery enterpriseQuery;

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        LOG.debug("Converting String: " + value);

        if (value == null || value.isEmpty()) {
            return null;
        }

        Long enterpriseID = Long.valueOf(value);
        EnterpriseViewData enterprise = null;
        try {
            enterprise = enterpriseQuery.getEnterpriseByID(enterpriseID);
        } catch (NotInDatabaseException_Exception e) {
            //do nothing
        }

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

        if (value instanceof EnterpriseViewData) {
            Long enterpriseID = ((EnterpriseViewData) value).getId();
            return String.valueOf(enterpriseID);
        } else {
            throw new ConverterException("The value is not a valid Enterprise instance: " + value);
        }
    }

}
