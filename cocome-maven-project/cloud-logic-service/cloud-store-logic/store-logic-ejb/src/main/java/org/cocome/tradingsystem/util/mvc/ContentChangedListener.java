package org.cocome.tradingsystem.util.mvc;

import java.util.HashSet;
import java.util.Set;

import javax.enterprise.event.Observes;

import org.apache.log4j.Logger;
import org.cocome.tradingsystem.cashdeskline.events.ContentChangedEvent;
import org.cocome.tradingsystem.util.scope.CashDeskSessionScoped;

@CashDeskSessionScoped
public class ContentChangedListener implements IContentChangedListener {
	private Set<Class<?>> changedModels = new HashSet<Class<?>>();
	
	private static final Logger LOG = Logger.getLogger(ContentChangedListener.class);
	
	public ContentChangedListener() {
		LOG.debug("ContentChangedListener constructed: " + this.toString());
	}
	
	@Override
	public void onContentChange(@Observes ContentChangedEvent event) {
		Class<?> modelClass = event.getFiringClass();
		LOG.debug("ContentChangedEvent received...");
		if (modelClass != null) {
			LOG.debug("adding model to changed models " + modelClass.getSimpleName());
			changedModels.add(modelClass);
		}
	}

	@Override
	public Set<Class<?>> getChangedModels() {
		LOG.debug("get changed models called");
		Set<Class<?>> returnChangedModels = changedModels;
		changedModels = new HashSet<Class<?>>();
		return returnChangedModels;
	}

}
