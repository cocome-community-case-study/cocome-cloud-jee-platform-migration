package org.cocome.cloud.web.data.plantdata;

import org.apache.log4j.Logger;
import org.cocome.tradingsystem.inventory.application.plant.productionunit.ProductionUnitClassTO;

import java.util.List;

/**
 * UI specific wraper objects for {@link ProductionUnitClassTO}
 *
 * @author Rudolf Biczok
 */
public class ProductionUnitClassViewData {
    private ProductionUnitClassTO puc;

    private List<ProductionUnitOperationViewData> operations;

    public ProductionUnitClassViewData(final ProductionUnitClassTO puc,
                                       final List<ProductionUnitOperationViewData> operations) {
        this.puc = puc;
        this.operations = operations;
    }

    private boolean editingEnabled = false;

    public List<ProductionUnitOperationViewData> getOperations() {
        return operations;
    }

    public void setOperations(List<ProductionUnitOperationViewData> operations) {
        this.operations = operations;
    }

    public ProductionUnitClassTO getPUC() {
        return puc;
    }

    public void setPUC(final ProductionUnitClassTO puc) {
        this.puc = puc;
    }

    public boolean isEditingEnabled() {
        return editingEnabled;
    }

    public void setEditingEnabled(boolean editingEnabled) {
        this.editingEnabled = editingEnabled;
    }
}
