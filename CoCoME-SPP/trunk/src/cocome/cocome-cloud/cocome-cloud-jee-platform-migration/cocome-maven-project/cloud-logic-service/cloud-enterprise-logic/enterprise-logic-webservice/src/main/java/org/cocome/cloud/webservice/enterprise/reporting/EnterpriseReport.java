package org.cocome.cloud.webservice.enterprise.reporting;

import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.jws.WebService;

import org.cocome.cloud.logic.registry.client.IApplicationHelper;
import org.cocome.cloud.webservice.util.EJBExceptionHelper;
import org.cocome.logic.webservice.enterprise.reporting.IEnterpriseReport;
import org.cocome.tradingsystem.inventory.application.reporting.IReportingLocal;
import org.cocome.tradingsystem.inventory.application.reporting.ReportTO;
import org.cocome.tradingsystem.util.exception.BaseException;
import org.cocome.tradingsystem.util.exception.NotInDatabaseException;
import org.cocome.tradingsystem.util.scope.CashDeskRegistry;
import org.cocome.tradingsystem.util.scope.ICashDeskRegistryFactory;
import org.cocome.tradingsystem.util.scope.IContextRegistry;
import org.cocome.tradingsystem.util.scope.RegistryKeys;

/**
 * 
 * 
 * @author Tobias Pöppke
 * @author Robert Heinrich
 * 
 */
@WebService(serviceName = "IEnterpriseReportService", 
			name = "IEnterpriseReport", 
			endpointInterface = "org.cocome.logic.webservice.enterprise.reporting.IEnterpriseReport",
			targetNamespace = "http://reporting.enterprise.webservice.logic.cocome.org/")
@Stateless
public class EnterpriseReport implements IEnterpriseReport {
	
	@EJB
	private IReportingLocal reportingService;
	
	@Inject
	private ICashDeskRegistryFactory registryFact;
	
	@Inject
	IApplicationHelper applicationHelper; 
	
	@Inject
	String enterpriseReportingWSDL;
	
	private void setEnterpriseRegistry(long enterpriseID) {
		IContextRegistry registry = new CashDeskRegistry("enterprise#" + enterpriseID);
		registry.putLong(RegistryKeys.ENTERPRISE_ID, enterpriseID);
		registryFact.setEnterpriseContext(registry);
	}
	
	private void setStoreRegistry(long storeID) {
		IContextRegistry registry = new CashDeskRegistry("store#" + storeID);
		registry.putLong(RegistryKeys.STORE_ID, storeID);
		registryFact.setEnterpriseContext(registry);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public ReportTO getStoreStockReport(long storeId) throws NotInDatabaseException {
		setStoreRegistry(storeId);
		return reportingService.getStoreStockReport(storeId);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ReportTO getEnterpriseStockReport(long enterpriseId)
			 throws NotInDatabaseException {
		setEnterpriseRegistry(enterpriseId);
		try {
			return reportingService.getEnterpriseStockReport(enterpriseId);
		} catch (BaseException | EJBException e) {
			Throwable cause = EJBExceptionHelper.getRootCause(e);
			
			if (cause instanceof NotInDatabaseException) {
				throw (NotInDatabaseException) cause;
			} else {
				throw e;
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ReportTO getEnterpriseDeliveryReport(long enterpriseId)
			 throws NotInDatabaseException {
		setEnterpriseRegistry(enterpriseId);
		try {
			return reportingService.getEnterpriseDeliveryReport(enterpriseId);
		} catch (BaseException | EJBException e) {
			Throwable cause = EJBExceptionHelper.getRootCause(e);
			
			if (cause instanceof NotInDatabaseException) {
				throw (NotInDatabaseException) cause;
			} else {
				throw e;
			}
		}
	}
}
