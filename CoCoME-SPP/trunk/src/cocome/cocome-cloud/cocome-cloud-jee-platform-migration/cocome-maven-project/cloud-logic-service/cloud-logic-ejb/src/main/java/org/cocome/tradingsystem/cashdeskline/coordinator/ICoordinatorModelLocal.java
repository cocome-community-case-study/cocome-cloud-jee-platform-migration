package org.cocome.tradingsystem.cashdeskline.coordinator;

import javax.ejb.Local;

/**
* Interface for the cash desk line coordinator model. The coordinator is
* responsible for collecting sales information from all cash desks and
* deciding in which mode (normal or express) they should operate.
* 
* @author Tobias Pöppke
*/

@Local
public interface ICoordinatorModelLocal {

	/**
	 * Updates the statistics on the coordinator and decides whether the
	 * cash desk should be switched to express mode or not.
	 * 
	 * @param cashDeskName
	 * 		the name of the issuing cash desk
	 * @param sale
	 * 		the sale item representing the sale made
	 */
	public void updateStatistics(String cashDeskName, Sale sale);

}