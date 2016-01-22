package org.cocome.tradingsystem.util.mvc;

import java.util.Set;

import org.cocome.tradingsystem.cashdeskline.events.ContentChangedEvent;

public interface IContentChangedListener {
	public void onContentChange(ContentChangedEvent event);
	
	public Set<Class<?>> getChangedModels();

}
