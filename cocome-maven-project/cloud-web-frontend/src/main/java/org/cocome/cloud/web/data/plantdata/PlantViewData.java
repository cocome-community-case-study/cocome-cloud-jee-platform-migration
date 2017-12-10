package org.cocome.cloud.web.data.plantdata;

import org.cocome.cloud.web.data.ViewData;
import org.cocome.tradingsystem.inventory.application.plant.PlantTO;
import org.cocome.tradingsystem.inventory.application.store.EnterpriseTO;

/**
 * Holds the information about a plant.
 *
 * @author Rudolf Biczok
 */
public class PlantViewData extends ViewData<PlantTO> {

    private boolean editingEnabled;
    private String newName;
    private String newLocation;

    public PlantViewData(long id, EnterpriseTO enterprise, String location, String name) {
        super(new PlantTO());
        this.data.setId(id);
        this.data.setEnterpriseTO(enterprise);
        this.data.setName(name);
        this.data.setLocation(location);
        this.setNewLocation(location);
        this.setNewName(name);
    }

    public long getID() {
        return this.data.getId();
    }

    public void setID(long storeID) {
        this.data.setId(storeID);
    }

    public String getName() {
        return this.data.getName();
    }

    public void setName(String name) {
        this.data.setName(name);
    }

    public String getLocation() {
        return this.data.getLocation();
    }

    public void setLocation(String storeLocation) {
        this.data.setLocation(storeLocation);
    }

    public boolean isEditingEnabled() {
        return editingEnabled;
    }

    public void setEditingEnabled(boolean editingEnabled) {
        if (!editingEnabled) {
            newName = this.data.getName();
            newLocation = this.data.getLocation();
        }
        this.editingEnabled = editingEnabled;
    }

    @Override
    public long getServiceId() {
        return this.data.getEnterpriseTO().getId();
    }

    @Override
    public long getParentId() {
        return this.data.getEnterpriseTO().getId();
    }

    public EnterpriseTO getEnterprise() {
        return this.data.getEnterpriseTO();
    }

    public void setEnterprise(EnterpriseTO enterprise) {
        this.data.setEnterpriseTO(enterprise);
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
        return data;
    }

    void updatePlantInformation() {
        this.data.setName(newName);
        this.data.setLocation(newLocation);
    }
}
