package org.cocome.cloud.web.data.plantdata;

import org.cocome.tradingsystem.inventory.application.plant.productionunit.ProductionUnitOperationTO;

/**
 * UI specific wraper objects for {@link ProductionUnitOperationTO}
 *
 * @author Rudolf Biczok
 */
public class ProductionUnitOperationViewData {

    private ProductionUnitOperationTO operation;

    public ProductionUnitOperationViewData(final ProductionUnitOperationTO operation) {
        this.operation = operation;
    }

    public ProductionUnitOperationTO getOperation() {
        return operation;
    }

    public void setOperation(final ProductionUnitOperationTO operation) {
        this.operation = operation;
    }

    private boolean editingEnabled = false;

    public boolean isEditingEnabled() {
        return editingEnabled;
    }

    public void setEditingEnabled(boolean editingEnabled) {
        this.editingEnabled = editingEnabled;
    }
}
