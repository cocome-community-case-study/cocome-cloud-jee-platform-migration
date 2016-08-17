package org.cocome.logic.webservice.enterpriseservice.reportingservice;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlElement;

import org.cocome.tradingsystem.inventory.application.reporting.ReportTO;
import org.cocome.tradingsystem.util.exception.NotInDatabaseException;


/**
 * 
 * 
 * @author Tobias Pöppke
 * @author Robert Heinrich
 * 
 */
@WebService
public interface IEnterpriseReport {

	/**
	 * Generates report of available stock for all products in the specified
	 * store.
	 * 
	 * @param storeId
	 *            the store entity identifier for which to generate report
	 * @return
	 *         Report transfer object containing store stock information.
	 * @throws NotInDatabaseException
	 *             if the given store does not exist
	 */
	@WebMethod
	public ReportTO getStoreStockReport(@XmlElement(required = true) @WebParam(name = "storeID") long storeId)
			throws NotInDatabaseException;

	/**
	 * Generates report of available stock for all products in all stores
	 * of the specified enterprise.
	 * 
	 * @param enterpriseId
	 *            the enterprise entity identifier for which to generate report
	 * @return
	 *         Report transfer object containing enterprise stock information.
	 * @throws NotInDatabaseException
	 *             if the given enterprise does not exist
	 */
	@WebMethod
	public ReportTO getEnterpriseStockReport(@XmlElement(required = true) @WebParam(name = "enterpriseID") long enterpriseId)
			throws NotInDatabaseException;

	/**
	 * Generates report of mean time to delivery for each supplier of the
	 * specified enterprise.
	 * 
	 * @param enterpriseId
	 *            the enterprise entity identifier for which to generate report
	 * @return
	 *         Report transfer object containing mean time to delivery
	 *         information.
	 * @throws NotInDatabaseException
	 *             if the given enterprise does not exist
	 */
	@WebMethod
	public ReportTO getEnterpriseDeliveryReport(@XmlElement(required = true) @WebParam(name = "enterpriseID") long enterpriseId)
			throws NotInDatabaseException;

}