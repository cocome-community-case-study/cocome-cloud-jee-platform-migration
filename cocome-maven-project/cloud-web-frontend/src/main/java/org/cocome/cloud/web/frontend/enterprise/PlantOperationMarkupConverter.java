package org.cocome.cloud.web.frontend.enterprise;


import com.fasterxml.jackson.core.JsonProcessingException;
import org.cocome.tradingsystem.inventory.application.plant.expression.MarkupInfo;
import org.cocome.tradingsystem.inventory.parser.plant.MarkupParser;
import org.omnifaces.util.Messages;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;
import java.io.IOException;

@FacesConverter("org.cocome.plantOperationMarkupConverter")
public class PlantOperationMarkupConverter implements Converter {

    private static final MarkupParser parser = new MarkupParser();

    @Override
    public Object getAsObject(FacesContext context, UIComponent component,
                              String value) {
        try {
            return parser.parse(value);
        } catch (IOException e) {
            throw new ConverterException(Messages.createError(component.getClientId(context),
                    "Unexpected parse markup: {0}", e.getMessage()));
        }
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component,
                              Object value) {
        if (value instanceof MarkupInfo) {
            try {
                return parser.toString((MarkupInfo) value);
            } catch (JsonProcessingException e) {
                throw new ConverterException(Messages.createFatal(component.getClientId(context),
                        "Unexpected JSON processing error"), e);
            }
        }
        throw new ConverterException(Messages.createFatal(component.getClientId(context),
                "Wrong object typ (expecting {0}, got {1}", MarkupInfo.class, value.getClass()));
    }
}