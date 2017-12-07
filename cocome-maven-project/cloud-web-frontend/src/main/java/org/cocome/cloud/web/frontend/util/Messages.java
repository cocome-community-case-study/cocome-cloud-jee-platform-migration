package org.cocome.cloud.web.frontend.util;

import javax.faces.context.FacesContext;
import javax.validation.constraints.NotNull;
import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;

public class Messages {
    public static String get(@NotNull String key, final Object... params) {
        FacesContext ctx = FacesContext.getCurrentInstance();
        Locale locale = ctx.getViewRoot().getLocale();

        ResourceBundle strings = ResourceBundle.getBundle("cocome.frontend.Strings", locale);

        return MessageFormat.format(strings.getString(key), params);
    }
}
