package org.cocome.tradingsystem.cashdeskline.events;

import java.io.Serializable;

import org.cocome.tradingsystem.util.mvc.AbstractModel;


/**
 * Event to inform observers that the content of a model has changed. 
 * This event is fired from the {@code AbstractModel} implementation 
 * and is typed with the corresponding class.
 * 
 * To observe events from a specific model use:
 * 
 * {@code @Observes @ContentChangedEventType(ExpressLightModel.class)}
 * 
 * 
 * @author Tobias Pöppke
 * 
 */
public class ContentChangedEvent implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4867745548618994744L;
	
	private final Class<? extends AbstractModel> firingClass;
	
	/**
	 * Creates a new event with the specified class as the firing class.
	 * 
	 * @param firingClass
	 * 		the firing class of this event
	 */
	public ContentChangedEvent(Class<? extends AbstractModel> firingClass) {
		this.firingClass = firingClass;
	}
	
	/**
	 * @return the class given in the event as the firing class
	 */
	public Class<? extends AbstractModel> getFiringClass() {
		return firingClass;
	}
}
