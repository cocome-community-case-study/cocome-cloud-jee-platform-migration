package org.cocome.tradingsystem.cashdeskline.events;

import org.cocome.tradingsystem.inventory.data.enterprise.ICustomProduct;

import java.io.Serializable;

/**
 * This event is fired when a custom product has been added as sale item.
 *
 * @author Rudolf Biczok
 */
public class CustomProductEnteredEvent implements Serializable {

    private static final long serialVersionUID = -1L;

    private final ICustomProduct customProduct;

    public CustomProductEnteredEvent(ICustomProduct customProduct) {
        this.customProduct = customProduct;
    }

    public ICustomProduct getCustomProduct() {
        return customProduct;
    }

}
