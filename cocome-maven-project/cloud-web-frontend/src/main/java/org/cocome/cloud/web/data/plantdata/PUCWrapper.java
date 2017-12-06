package org.cocome.cloud.web.data.plantdata;

import org.apache.log4j.Logger;
import org.cocome.tradingsystem.inventory.application.plant.productionunit.ProductionUnitClassTO;

/**
 * UI specific wraper objects for {@link ProductionUnitClassTO}
 *
 * @author Rudolf Biczok
 */
public class PUCWrapper {
    private static final Logger LOG = Logger.getLogger(PUCWrapper.class);
    private ProductionUnitClassTO puc;

    public PUCWrapper(ProductionUnitClassTO puc, PlantViewData plant) {
        this.puc = puc;
        this.plant = plant;
        this.newName = plant.getName();
    }

    private PlantViewData plant;

    private boolean editingEnabled = false;

    private String newName;

    public ProductionUnitClassTO getPUC() {
        return puc;
    }

    public void setPUC(final ProductionUnitClassTO puc) {
        this.puc = puc;
    }

    public PlantViewData getPlant() {
        return plant;
    }

    public void setPlant(final PlantViewData plant) {
        this.plant = plant;
    }

    public boolean isEditingEnabled() {
        return editingEnabled;
    }

    public void setEditingEnabled(boolean editingEnabled) {
        this.editingEnabled = editingEnabled;
    }

    public void submitEdit()  {
        this.puc.setName(newName);
        this.setEditingEnabled(false);
    }

    public String getNewName() {
        return newName;
    }

    public void setNewName(String newName) {
        this.newName = newName;
    }
}
