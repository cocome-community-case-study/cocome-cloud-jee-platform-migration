package org.cocome.tradingsystem.util.exception;

import javax.xml.ws.WebFault;

/**
 * Represents an exception that may occur when dealing with invalid recipes
 *
 * @author Rudolf Biczok
 */
@WebFault
public class RecipeException extends BaseException {
    private static final long serialVersionUID = 1L;

    public RecipeException(String string) {
        super(string);
    }

    public RecipeException(String message, Throwable cause) {
        super(message, cause);
    }
}
