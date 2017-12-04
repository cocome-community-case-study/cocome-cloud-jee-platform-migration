package org.cocome.cloud.web.events;

import org.cocome.cloud.web.data.logindata.IUser;
import org.cocome.cloud.web.data.logindata.UserRole;
import org.cocome.cloud.web.frontend.navigation.NavigationViewStates;

import javax.validation.constraints.NotNull;

public class LoginEvent {
    private IUser user;
    private UserRole role;
    private long storeID;

    private long plantID;

    public LoginEvent(@NotNull IUser user, @NotNull UserRole role, long storeID, long plantID) {
        this.user = user;
        this.role = role;
        this.setStoreID(storeID);
        this.setPlantID(plantID);
    }

    public UserRole getRole() {
        return role;
    }

    public void setRole(@NotNull UserRole role) {
        this.role = role;
    }

    public IUser getUser() {
        return user;
    }

    public void setUser(@NotNull IUser user) {
        this.user = user;
    }

    public NavigationViewStates getRequestedView() {
        return role.associatedView();
    }

    public long getStoreID() {
        return storeID;
    }

    public void setStoreID(long storeID) {
        this.storeID = storeID;
    }

    public long getPlantID() {
        return plantID;
    }

    public void setPlantID(long plantID) {
        this.plantID = plantID;
    }

}
