package org.cocome.cloud.web.data.cashdeskdata;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.cocome.cloud.logic.stub.IllegalCashDeskStateException_Exception;
import org.cocome.cloud.logic.stub.IllegalInputException_Exception;
import org.cocome.cloud.logic.stub.NoSuchProductException_Exception;
import org.cocome.cloud.logic.stub.NotInDatabaseException_Exception;
import org.cocome.cloud.logic.stub.ProductOutOfStockException_Exception;
import org.cocome.cloud.logic.stub.UnhandledException_Exception;
import org.cocome.cloud.web.connector.cashdeskconnector.ICashDeskQuery;

@Named
@RequestScoped
public class CashDeskDAO implements ICashDeskDAO {
	
	@Inject
	ICashDeskQuery cashDeskQuery;

	@Override
	public void startSale(String cashDeskName, long storeID) throws UnhandledException_Exception,
			IllegalCashDeskStateException_Exception, NotInDatabaseException_Exception {
		cashDeskQuery.startSale(cashDeskName, storeID);
	}

	@Override
	public boolean isInExpressMode(String cashDeskName, long storeID)
			throws UnhandledException_Exception, NotInDatabaseException_Exception {
		return cashDeskQuery.isInExpressMode(cashDeskName, storeID);
	}

	@Override
	public String getDisplayMessage(String cashDeskName, long storeID)
			throws UnhandledException_Exception, NotInDatabaseException_Exception {
		return cashDeskQuery.getDisplayMessage(cashDeskName, storeID);
	}

	@Override
	public String[] getPrinterOutput(String cashDeskName, long storeID)
			throws UnhandledException_Exception, NotInDatabaseException_Exception {
		return cashDeskQuery.getPrinterOutput(cashDeskName, storeID);
	}

	@Override
	public void enterCashAmount(String cashDeskName, long storeID, double amount) throws UnhandledException_Exception,
			IllegalCashDeskStateException_Exception, NotInDatabaseException_Exception {
		cashDeskQuery.enterCashAmount(cashDeskName, storeID, amount);
	}

	@Override
	public void enterCardInfo(String cashDeskName, long storeID, String cardInfo, int pin)
			throws UnhandledException_Exception, IllegalCashDeskStateException_Exception,
			NotInDatabaseException_Exception {
		cashDeskQuery.enterCardInfo(cashDeskName, storeID, cardInfo, pin);
	}

	@Override
	public void startCashPayment(String cashDeskName, long storeID)
			throws NotInDatabaseException_Exception, ProductOutOfStockException_Exception, UnhandledException_Exception,
			IllegalCashDeskStateException_Exception, IllegalInputException_Exception {
		cashDeskQuery.startCashPayment(cashDeskName, storeID);
	}

	@Override
	public void startCreditCardPayment(String cashDeskName, long storeID)
			throws NotInDatabaseException_Exception, ProductOutOfStockException_Exception, UnhandledException_Exception,
			IllegalCashDeskStateException_Exception, IllegalInputException_Exception {
		cashDeskQuery.startCreditCardPayment(cashDeskName, storeID);
	}

	@Override
	public void enterBarcode(String cashDeskName, long storeID, long barcode)
			throws UnhandledException_Exception, IllegalCashDeskStateException_Exception,
			NotInDatabaseException_Exception, NoSuchProductException_Exception, ProductOutOfStockException_Exception {
		cashDeskQuery.enterBarcode(cashDeskName, storeID, barcode);
	}

}
