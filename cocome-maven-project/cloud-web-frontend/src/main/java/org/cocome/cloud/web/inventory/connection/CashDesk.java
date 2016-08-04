package org.cocome.cloud.web.inventory.connection;

import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.validation.constraints.NotNull;
import javax.xml.ws.WebServiceRef;

import org.apache.log4j.Logger;
import org.cocome.cloud.logic.registry.client.ApplicationHelper;
import org.cocome.cloud.logic.stub.CashDeskState;
import org.cocome.cloud.logic.stub.IBarcodeScanner;
import org.cocome.cloud.logic.stub.IBarcodeScannerService;
import org.cocome.cloud.logic.stub.ICardReader;
import org.cocome.cloud.logic.stub.ICardReaderService;
import org.cocome.cloud.logic.stub.ICashBox;
import org.cocome.cloud.logic.stub.ICashBoxService;
import org.cocome.cloud.logic.stub.ICashDesk;
import org.cocome.cloud.logic.stub.ICashDeskService;
import org.cocome.cloud.logic.stub.IExpressLight;
import org.cocome.cloud.logic.stub.IExpressLightService;
import org.cocome.cloud.logic.stub.IPrinter;
import org.cocome.cloud.logic.stub.IPrinterService;
import org.cocome.cloud.logic.stub.IStoreManager;
import org.cocome.cloud.logic.stub.IStoreManagerService;
import org.cocome.cloud.logic.stub.IUserDisplay;
import org.cocome.cloud.logic.stub.IUserDisplayService;
import org.cocome.cloud.logic.stub.IllegalCashDeskStateException_Exception;
import org.cocome.cloud.logic.stub.IllegalInputException_Exception;
import org.cocome.cloud.logic.stub.NotBoundException_Exception;
import org.cocome.cloud.logic.stub.NotInDatabaseException_Exception;
import org.cocome.cloud.logic.stub.ProductOutOfStockException_Exception;
import org.cocome.cloud.logic.stub.UnhandledException_Exception;
import org.cocome.cloud.registry.service.Names;

@RequestScoped
public class CashDesk implements ICashDeskQuery {
	private static final Logger LOG = Logger.getLogger(CashDesk.class);

	ICashDeskService cashDeskService;

	ICashBox cashBox;

	ICashDesk cashDesk;

	IBarcodeScanner barcodeScanner;

	IExpressLight expressLight;

	ICardReader cardReader;

	IPrinter printer;

	IUserDisplay userDisplay;

	@Inject
	ApplicationHelper applicationHelper;

	@Inject
	int defaultCashDeskIndex;

	@Inject
	long defaultStoreIndex;

	private void lookupCashDeskComponents(long storeID) throws NotInDatabaseException_Exception {
		LOG.debug(String.format("Looking up cash desk components for store %d", storeID));
		try {
			initCashDeskComponents(storeID);
		} catch (NotBoundException_Exception e) {
			if (storeID == defaultStoreIndex) {
				LOG.error("Got exception while retrieving store manager location: " + e.getMessage());
				e.printStackTrace();
				throw new NotInDatabaseException_Exception(e.getMessage());
			} else {
				lookupCashDeskComponents(defaultStoreIndex);
			}
		}
	}

	private void initCashDeskComponents(long storeID) throws NotBoundException_Exception {
		cashDeskService = applicationHelper.getComponent(Names.getCashDeskRegistryName(storeID, defaultCashDeskIndex),
				ICashDeskService.SERVICE, ICashDeskService.class);
		cashDesk = cashDeskService.getICashDeskPort();
		cashBox = applicationHelper.getComponentFromLocation(cashDeskService.getWSDLDocumentLocation(),
				ICashBoxService.SERVICE, ICashBoxService.class).getICashBoxPort();
		barcodeScanner = applicationHelper.getComponentFromLocation(cashDeskService.getWSDLDocumentLocation(),
				IBarcodeScannerService.SERVICE, IBarcodeScannerService.class).getIBarcodeScannerPort();
		expressLight = applicationHelper.getComponentFromLocation(cashDeskService.getWSDLDocumentLocation(),
				IExpressLightService.SERVICE, IExpressLightService.class).getIExpressLightPort();
		cardReader = applicationHelper.getComponentFromLocation(cashDeskService.getWSDLDocumentLocation(),
				ICardReaderService.SERVICE, ICardReaderService.class).getICardReaderPort();
		userDisplay = applicationHelper.getComponentFromLocation(cashDeskService.getWSDLDocumentLocation(),
				IUserDisplayService.SERVICE, IUserDisplayService.class).getIUserDisplayPort();
		printer = applicationHelper.getComponentFromLocation(cashDeskService.getWSDLDocumentLocation(),
				IPrinterService.SERVICE, IPrinterService.class).getIPrinterPort();
	}

	@Override
	public void startSale(@NotNull String cashDeskName, long storeID) throws UnhandledException_Exception,
			IllegalCashDeskStateException_Exception, NotInDatabaseException_Exception {
		try {
			lookupCashDeskComponents(storeID);
			cashDesk.startSale(cashDeskName, storeID);
		} catch (UnhandledException_Exception | IllegalCashDeskStateException_Exception
				| NotInDatabaseException_Exception e) {
			LOG.error(String.format("Exception while starting new sale at cashdesk %s in store %d: %s\n%s",
					cashDeskName, storeID, e.getMessage(), e.getStackTrace()));
			throw e;
		}
	}

	@Override
	public boolean isInExpressMode(String cashDeskName, long storeID)
			throws UnhandledException_Exception, NotInDatabaseException_Exception {
		try {
			lookupCashDeskComponents(storeID);
			return cashDesk.isInExpressMode(cashDeskName, storeID);
		} catch (UnhandledException_Exception | NotInDatabaseException_Exception e) {
			LOG.error(String.format("Exception while retrieving express mode state at cashdesk %s in store %d: %s\n%s",
					cashDeskName, storeID, e.getMessage(), e.getStackTrace()));
			throw e;
		}
	}

	@Override
	public String getDisplayMessage(String cashDeskName, long storeID)
			throws UnhandledException_Exception, NotInDatabaseException_Exception {
		try {
			lookupCashDeskComponents(storeID);
			return userDisplay.getMessage(cashDeskName, storeID);
		} catch (UnhandledException_Exception | NotInDatabaseException_Exception e) {
			LOG.error(String.format("Exception while retrieving diplay message at cashdesk %s in store %d: %s\n%s",
					cashDeskName, storeID, e.getMessage(), e.getStackTrace()));
			throw e;
		}
	}

	@Override
	public boolean enterCashAmount(String cashDeskName, long storeID, double amount) {
		try {
			lookupCashDeskComponents(storeID);
			cashDesk.startCashPayment(cashDeskName, storeID, amount);
			cashBox.close(cashDeskName, storeID);
			return true;
		} catch (UnhandledException_Exception | IllegalCashDeskStateException_Exception
				| NotInDatabaseException_Exception e) {
			LOG.error(String.format("Exception while entering cash amount at cashdesk %s in store %d: %s\n%s",
					cashDeskName, storeID, e.getMessage(), e.getStackTrace()));
		}
		return false;
	}

	@Override
	public void enterCardInfo(String cashDeskName, long storeID, String cardInfo, int pin)
			throws UnhandledException_Exception, IllegalCashDeskStateException_Exception,
			NotInDatabaseException_Exception {
		try {
			lookupCashDeskComponents(storeID);
			cardReader.sendCreditCardInfo(cashDeskName, storeID, cardInfo);
			cardReader.sendCreditCardPin(cashDeskName, storeID, pin);
		} catch (UnhandledException_Exception | IllegalCashDeskStateException_Exception
				| NotInDatabaseException_Exception e) {
			LOG.error(String.format("Exception while entering card info at cashdesk %s in store %d: %s\n%s",
					cashDeskName, storeID, e.getMessage(), e.getStackTrace()));
			throw e;
		}
	}

	@Override
	public void startCashPayment(String cashDeskName, long storeID)
			throws NotInDatabaseException_Exception, ProductOutOfStockException_Exception, UnhandledException_Exception,
			IllegalCashDeskStateException_Exception, IllegalInputException_Exception {
		try {
			lookupCashDeskComponents(storeID);
			cashDesk.finishSale(cashDeskName, storeID);
			cashDesk.selectPaymentMode(cashDeskName, storeID, "CASH");
		} catch (NotInDatabaseException_Exception | ProductOutOfStockException_Exception | UnhandledException_Exception
				| IllegalCashDeskStateException_Exception | IllegalInputException_Exception e) {
			LOG.error(String.format("Exception while starting cash payment at cashdesk %s in store %d: %s\n%s",
					cashDeskName, storeID, e.getMessage(), e.getStackTrace()));
			throw e;
		}

	}

	@Override
	public void startCreditCardPayment(String cashDeskName, long storeID)
			throws NotInDatabaseException_Exception, ProductOutOfStockException_Exception, UnhandledException_Exception,
			IllegalCashDeskStateException_Exception, IllegalInputException_Exception {
		try {
			lookupCashDeskComponents(storeID);
			cashDesk.finishSale(cashDeskName, storeID);
			cashDesk.selectPaymentMode(cashDeskName, storeID, "CREDIT_CARD");
		} catch (UnhandledException_Exception | IllegalCashDeskStateException_Exception
				| IllegalInputException_Exception | NotInDatabaseException_Exception
				| ProductOutOfStockException_Exception e) {
			LOG.error(String.format("Exception while starting cash payment at cashdesk %s in store %d: %s\n%s",
					cashDeskName, storeID, e.getMessage(), e.getStackTrace()));
			throw e;
		}
	}
}
