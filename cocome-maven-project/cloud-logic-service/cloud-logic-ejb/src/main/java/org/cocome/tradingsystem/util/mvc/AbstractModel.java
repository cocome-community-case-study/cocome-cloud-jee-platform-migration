/***************************************************************************
 * Copyright 2013 DFG SPP 1593 (http://dfg-spp1593.de)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ***************************************************************************/

package org.cocome.tradingsystem.util.mvc;

import javax.enterprise.event.Event;
import javax.inject.Inject;

import org.apache.log4j.Logger;
import org.cocome.tradingsystem.cashdeskline.events.ContentChangedEvent;

/**
 * Represents a base class for implementing named observable models. It supports
 * observer management and change notification.
 * 
 * @author Lubomir Bulej
 * @author Tobias Pöppke
 */
public class AbstractModel<T> {
	
	private static final Logger LOG =
			Logger.getLogger(AbstractModel.class);
	
	@Inject 
	private Event<ContentChangedEvent> event;
	
	//
	
	private void fireEvent(Class<? extends AbstractModel> firingClass){
		LOG.debug("Firing event for class " + firingClass);
        event.fire(new ContentChangedEvent(firingClass));
    }

	protected void changedContent() {
		Class<? extends AbstractModel> eventClass = this.getClass();
		fireEvent(eventClass);
	}

}
