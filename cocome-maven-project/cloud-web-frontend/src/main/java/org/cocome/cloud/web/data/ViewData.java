package org.cocome.cloud.web.data;

import org.cocome.tradingsystem.inventory.application.IIdentifiableTO;

/**
 * Abstract class for any kind of view data
 *
 * @author Rudolf Biczok
 */
public abstract class ViewData<T extends IIdentifiableTO> {

    protected T data;

    public ViewData(final T data) {
        this.data = data;
    }

    public T getData() {
        return data;
    }

    private boolean editingEnabled = false;

    public boolean isEditingEnabled() {
        return editingEnabled;
    }

    public void setEditingEnabled(boolean editingEnabled) {
        this.editingEnabled = editingEnabled;
    }

    public abstract long getServiceId();

    public abstract long getParentId();
}
