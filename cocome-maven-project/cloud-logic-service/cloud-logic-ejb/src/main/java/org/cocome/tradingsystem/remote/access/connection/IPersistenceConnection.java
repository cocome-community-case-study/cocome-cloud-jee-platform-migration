package org.cocome.tradingsystem.remote.access.connection;

import java.io.IOException;


/**
 * Interface for sending data manipulation queries to the backend. 
 * 
 * @author Tobias Pöppke
 */
public interface IPersistenceConnection {
	
	/**
	 * 
	 * @param entity
	 * @param header
	 * @param content
	 * @throws IOException
	 */
	public void sendUpdateQuery(String entity, String header, String content) throws IOException;
	
	/**
	 * 
	 * @param entity
	 * @param header
	 * @param content
	 * @throws IOException
	 */
	public void sendCreateQuery(String entity, String header, String content) throws IOException;
	
	/**
	 * 
	 * @return
	 */
	public String getResponse();
	
}
