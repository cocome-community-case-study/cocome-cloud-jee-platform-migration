package org.cocome.cloud.registry.service;

import java.net.URI;

public class RegistryEntry {
	private String name;
	
	private URI location;
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public URI getLocation() {
		return location;
	}
	
	public void setLocation(URI location) {
		this.location = location;
	}
}
