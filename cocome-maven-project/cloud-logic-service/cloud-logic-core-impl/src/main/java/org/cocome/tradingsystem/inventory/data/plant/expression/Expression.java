package org.cocome.tradingsystem.inventory.data.plant.expression;

import java.io.Serializable;

/**
 * Represents an expression that can be used for plant-local recipes.
 *
 * @author Rudolf Biczok
 */
public abstract class Expression implements Serializable, IExpression {
    private static final long serialVersionUID = 1L;

    private long id;

    @Override
    public long getId() {
        return this.id;
    }

    @Override
    public void setId(final long id) {
        this.id = id;
    }
}
