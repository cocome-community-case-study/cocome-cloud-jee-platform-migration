package org.cocome.cloud.web.usecase;

import javax.faces.event.ActionEvent;

public interface IUseCase {
	
	public String invoke() throws Exception;
	
	public void attrListener(ActionEvent event);
	
	public String finish();
	
}
