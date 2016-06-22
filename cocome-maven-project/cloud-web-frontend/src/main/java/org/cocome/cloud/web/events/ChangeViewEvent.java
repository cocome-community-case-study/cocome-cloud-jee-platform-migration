package org.cocome.cloud.web.events;

import org.cocome.cloud.web.frontend.navigation.NavigationViewStates;

public class ChangeViewEvent {
	private NavigationViewStates newViewState;

	public ChangeViewEvent(NavigationViewStates newViewState) {
		this.newViewState = newViewState;
	}
	
	public NavigationViewStates getNewViewState() {
		return newViewState;
	}

	public void setNewViewState(NavigationViewStates newViewState) {
		this.newViewState = newViewState;
	}

}
