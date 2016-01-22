package org.cocome.tradingsystem.inventory.data.persistence;

import javax.ejb.EJB;
import javax.ejb.Stateless;

@Stateless
public class CloudPersistence implements IPersistenceLocal {

	@EJB
	private IPersistenceContextLocal persistenceContext;
	
	@Override
	public IPersistenceContextLocal getPersistenceContext() {
		return persistenceContext;
	}

}
