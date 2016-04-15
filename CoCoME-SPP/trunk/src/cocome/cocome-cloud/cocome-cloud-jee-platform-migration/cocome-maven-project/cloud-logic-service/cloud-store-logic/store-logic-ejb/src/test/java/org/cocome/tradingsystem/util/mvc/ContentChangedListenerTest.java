package org.cocome.tradingsystem.util.mvc;

import static org.junit.Assert.*;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.cocome.tradingsystem.cashdeskline.events.ContentChangedEvent;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ContentChangedListenerTest {
	
	private ContentChangedListener listener;
	
	private ContentChangedEvent event;
	
	private Set<Class<?>> shouldChangeModels;
	
	@Before
	public void setUp() {
		listener = new ContentChangedListener();
		event = new ContentChangedEvent(AbstractModel.class);
		shouldChangeModels = new HashSet<Class<?>>();
		shouldChangeModels.add(AbstractModel.class);
	}
	
	@After
	public void tearDown() {
		shouldChangeModels.clear();
	}
	

	@Test
	public void testOnContentChange() {
		listener.onContentChange(event);
		assertArrayEquals(shouldChangeModels.toArray(), listener.getChangedModels().toArray());
	}

	@Test
	public void testGetChangedModels() {
		/*
		 * getChangedModels should reset the models returned after every call
		 */
		
		Set<Class<?>> actualChanged = listener.getChangedModels();
		assertArrayEquals(new Class<?>[]{}, actualChanged.toArray());
		
		listener.onContentChange(event);
		
		actualChanged = listener.getChangedModels();
		assertArrayEquals(shouldChangeModels.toArray(), actualChanged.toArray());
		
		actualChanged = listener.getChangedModels();
		assertArrayEquals(new Class<?>[]{}, actualChanged.toArray());
	}

}
