package org.cocome.cloud.web.backend.cashdesk;

import java.io.Serializable;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.validation.constraints.NotNull;
import javax.xml.ws.soap.SOAPFaultException;

import org.apache.log4j.Logger;
import org.cocome.cloud.logic.registry.client.ApplicationHelper;
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
import org.cocome.cloud.logic.stub.IUserDisplay;
import org.cocome.cloud.logic.stub.IUserDisplayService;
import org.cocome.cloud.logic.stub.IllegalCashDeskStateException_Exception;
import org.cocome.cloud.logic.stub.IllegalInputException_Exception;
import org.cocome.cloud.logic.stub.NoSuchProductException_Exception;
import org.cocome.cloud.logic.stub.NotBoundException_Exception;
import org.cocome.cloud.logic.stub.NotInDatabaseException_Exception;
import org.cocome.cloud.logic.stub.ProductOutOfStockException_Exception;
import org.cocome.cloud.logic.stub.UnhandledException_Exception;
import org.cocome.cloud.registry.service.Names;

@SessionScoped
public class CashDeskQuery implements Serializable, ICashDeskQuery {
	private static final long serialVersionUID = 9146286507233255051L;

	private static final Logger LOG = Logger.getLogger(CashDeskQuery.class);

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
	
	private boolean initialized = false;

	private String getSOAPFaultMessage(SOAPFaultException e) {
		return e.getFault() != null ? e.getFault().getFaultString(): e.getMessage();
	}
	
	private void lookupCashDeskComponents(long storeID) throws NotInDatabaseException_Exception {
		if (initialized) {
			// Save the lookup calls if already initialized.
			// This may lead to errors if the store server is 
			// relocated during a session.
			return;
		}
		
		LOG.debug(String.format("Looking up cash desk components for store %d", storeID));
		try {
			initCashDeskComponents(storeID);
			initialized = true;
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
		String cashDeskName = Names.getCashDeskRegistryName(storeID, defaultCashDeskIndex);
		cashDesk = applicationHelper.getComponent(cashDeskName, ICashDeskService.SERVICE, ICashDeskService.class)
				.getICashDeskPort();
		cashBox = applicationHelper.getComponent(Names.getCashDeskComponentRegistryName(cashDeskName, "cashBox"),
				ICashBoxService.SERVICE, ICashBoxService.class).getICashBoxPort();
		barcodeScanner = applicationHelper
				.getComponent(Names.getCashDeskComponentRegistryName(cashDeskName, "barcodeScanner"),
						IBarcodeScannerService.SERVICE, IBarcodeScannerService.class)
				.getIBarcodeScannerPort();
		expressLight = applicationHelper
				.getComponent(Names.getCashDeskComponentRegistryName(cashDeskName, "expressLight"),
						IExpressLightService.SERVICE, IExpressLightService.class)
				.getIExpressLightPort();
		cardReader = applicationHelper.getComponent(Names.getCashDeskComponentRegistryName(cashDeskName, "cardReader"),
				ICardReaderService.SERVICE, ICardReaderService.class).getICardReaderPort();
		userDisplay = applicationHelper
				.getComponent(Names.getCashDeskComponentRegistryName(cashDeskName, "userDisplay"),
						IUserDisplayService.SERVICE, IUserDisplayService.class)
				.getIUserDisplayPort();
		printer = applicationHelper.getComponent(Names.getCashDeskComponentRegistryName(cashDeskName, "printer"),
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
		} catch (SOAPFaultException e) {
			LOG.error(String.format("Exception while retrieving express mode state at cashdesk %s in store %d: %s\n%s",
					cashDeskName, storeID, getSOAPFaultMessage(e), e.getStackTrace()));
			throw new UnhandledException_Exception(getSOAPFaultMessage(e));
		}
	}

	@Override
	public String getDisplayMessage(String cashDeskName, long storeID)
			throws UnhandledException_Exception, NotInDatabaseException_Exception {
		try {
			lookupCashDeskComponents(storeID);
			String message = userDisplay.getMessage(cashDeskName, storeID);
			LOG.error(String.format("Got display message '%s'", message));
			return message;
		} catch (UnhandledException_Exception | NotInDatabaseException_Exception e) {
			LOG.error(String.format("Exception while retrieving diplay message at cashdesk %s in store %d: %s\n%s",
					cashDeskName, storeID, e.getMessage(), e.getStackTrace()));
			throw e;
		} catch (SOAPFaultException e) {
			LOG.error(String.format("Exception while retrieving express mode state at cashdesk %s in store %d: %s\n%s",
					cashDeskName, storeID, getSOAPFaultMessage(e), e.getStackTrace()));
			throw new UnhandledException_Exception(getSOAPFaultMessage(e));
		}
	}

	@Override
	public void enterCashAmount(String cashDeskName, long storeID, double amount) throws UnhandledException_Exception, IllegalCashDeskStateException_Exception,
			NotInDatabaseException_Exception {
		try {
			lookupCashDeskComponents(storeID);
			cashDesk.startCashPayment(cashDeskName, storeID, amount);
			cashBox.close(cashDeskName, storeID);
		} catch (UnhandledException_Exception | IllegalCashDeskStateException_Exception
				| NotInDatabaseException_Exception e) {
			LOG.error(String.format("Exception while entering cash amount at cashdesk %s in store %d: %s\n%s",
					cashDeskName, storeID, e.getMessage(), e.getStackTrace()));
			throw e;
		} catch (SOAPFaultException e) {
			LOG.error(String.format("Exception while retrieving express mode state at cashdesk %s in store %d: %s\n%s",
					cashDeskName, storeID, getSOAPFaultMessage(e), e.getStackTrace()));
			throw new UnhandledException_Exception(getSOAPFaultMessage(e));
		}
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
		} catch (SOAPFaultException e) {
			LOG.error(String.format("Exception while retrieving express mode state at cashdesk %s in store %d: %s\n%s",
					cashDeskName, storeID, getSOAPFaultMessage(e), e.getStackTrace()));
			throw new UnhandledException_Exception(getSOAPFaultMessage(e));
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
		} catch (SOAPFaultException e) {
			LOG.error(String.format("Exception while retrieving express mode state at cashdesk %s in store %d: %s\n%s",
					cashDeskName, storeID, getSOAPFaultMessage(e), e.getStackTrace()));
			throw new UnhandledException_Exception(getSOAPFaultMessage(e));
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
		} catch (SOAPFaultException e) {
			LOG.error(String.format("Exception while retrieving express mode state at cashdesk %s in store %d: %s\n%s",
					cashDeskName, storeID, getSOAPFaultMessage(e), e.getStackTrace()));
			throw new UnhandledException_Exception(getSOAPFaultMessage(e));
		}
	}

	@Override
	public void enterBarcode(String cashDeskName, long storeID, long barcode)
			throws UnhandledException_Exception, IllegalCashDeskStateException_Exception,
			NotInDatabaseException_Exception, NoSuchProductException_Exception, ProductOutOfStockException_Exception {
		try {
			lookupCashDeskComponents(storeID);
			barcodeScanner.sendProductBarcode(cashDeskName, storeID, barcode);
		} catch (UnhandledException_Exception | IllegalCashDeskStateException_Exception
				| NotInDatabaseException_Exception | ProductOutOfStockException_Exception
				| NoSuchProductException_Exception e) {
			LOG.error(String.format("Exception while scanning barcode at cashdesk %s in store %d: %s\n%s", cashDeskName,
					storeID, e.getMessage(), e.getStackTrace()));
			throw e;
		} catch (SOAPFaultException e) {
			LOG.error(String.format("Exception while retrieving express mode state at cashdesk %s in store %d: %s\n%s",
					cashDeskName, storeID, getSOAPFaultMessage(e), e.getStackTrace()));
			throw new UnhandledException_Exception(getSOAPFaultMessage(e));
		}
	}

	@Override
	public String[] getPrinterOutput(String cashDeskName, long storeID)
			throws UnhandledException_Exception, NotInDatabaseException_Exception {
		try {
			lookupCashDeskComponents(storeID);
			return printer.getCurrentPrintout(cashDeskName, storeID).split("\\n");
		} catch (UnhandledException_Exception | NotInDatabaseException_Exception e) {
			LOG.error(String.format("Exception while getting printer output at cashdesk %s in store %d: %s\n%s",
					cashDeskName, storeID, e.getMessage(), e.getStackTrace()));
			throw e;
		} catch (SOAPFaultException e) {
			LOG.error(String.format("Exception while retrieving express mode state at cashdesk %s in store %d: %s\n%s",
					cashDeskName, storeID, getSOAPFaultMessage(e), e.getStackTrace()));
			throw new UnhandledException_Exception(getSOAPFaultMessage(e));
		}
	}
}
