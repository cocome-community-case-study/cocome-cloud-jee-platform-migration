package org.cocome.tradingsystem.cashdeskline.events;

import javax.enterprise.util.AnnotationLiteral;

public class ContentChangedQualifier extends AnnotationLiteral<ContentChangedEventType> implements
		ContentChangedEventType {
	
	private static final long serialVersionUID = 1934469491292671697L;
	
	private Class<?> type;
	
	public ContentChangedQualifier(Class<?> type) {
		this.type = type;
	}

	@Override
	public Class<?> type() {
		return this.type;
	}

}
