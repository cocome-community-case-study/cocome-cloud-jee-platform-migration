package org.cocome.cloud.web.data.plantdata;

import org.cocome.tradingsystem.inventory.application.plant.PlantTO;
import org.cocome.tradingsystem.inventory.application.store.EnterpriseTO;

/**
 * Holds the information about a plant.
 *
 * @author Rudolf Biczok
 */
public class PlantViewData {

    private PlantTO plant = new PlantTO();
    private boolean editingEnabled;
    private String newName;
    private String newLocation;

    public PlantViewData(long id, EnterpriseTO enterprise, String location, String name) {
        this.plant.setId(id);
        this.plant.setEnterpriseTO(enterprise);
        this.plant.setName(name);
        this.plant.setLocation(location);
        this.setNewLocation(location);
        this.setNewName(name);
    }

    public long getID() {
        return this.plant.getId();
    }

    public void setID(long storeID) {
        this.plant.setId(storeID);
    }

    public String getName() {
        return this.plant.getName();
    }

    public void setName(String name) {
        this.plant.setName(name);
    }

    public String getLocation() {
        return this.plant.getLocation();
    }

    public void setLocation(String storeLocation) {
        this.plant.setLocation(storeLocation);
    }

    public boolean isEditingEnabled() {
        return editingEnabled;
    }

    public void setEditingEnabled(boolean editingEnabled) {
        if (!editingEnabled) {
            newName = this.plant.getName();
            newLocation = this.plant.getLocation();
        }
        this.editingEnabled = editingEnabled;
    }

    public EnterpriseTO getEnterprise() {
        return this.plant.getEnterpriseTO();
    }

    public void setEnterprise(EnterpriseTO enterprise) {
        this.plant.setEnterpriseTO(enterprise);
    }

    public String getNewName() {
        return newName;
    }

    public void setNewName(String newName) {
        this.newName = newName;
    }

    public String getNewLocation() {
        return newLocation;
    }

    public void setNewLocation(String newLocation) {
        this.newLocation = newLocation;
    }

    public PlantTO getPlantTO() {
        return plant;
    }

    void updatePlantInformation() {
        this.plant.setName(newName);
        this.plant.setLocation(newLocation);
    }
}
