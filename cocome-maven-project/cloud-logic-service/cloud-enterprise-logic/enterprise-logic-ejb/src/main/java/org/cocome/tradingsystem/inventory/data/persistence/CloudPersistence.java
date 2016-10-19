package org.cocome.tradingsystem.inventory.data.persistence;

import javax.ejb.EJB;
import javax.ejb.Stateless;

@Stateless
public class CloudPersistence implements IPersistence {

	@EJB
	private IPersistenceContext persistenceContext;
	
	@Override
	public IPersistenceContext getPersistenceContext() {
		return persistenceContext;
	}
}
