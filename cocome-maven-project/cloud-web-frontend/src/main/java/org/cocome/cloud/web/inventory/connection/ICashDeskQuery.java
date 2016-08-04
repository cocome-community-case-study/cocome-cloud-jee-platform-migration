package org.cocome.cloud.web.inventory.connection;

import javax.validation.constraints.NotNull;

import org.cocome.cloud.logic.stub.CashDeskState;
import org.cocome.cloud.logic.stub.IllegalCashDeskStateException_Exception;
import org.cocome.cloud.logic.stub.IllegalInputException_Exception;
import org.cocome.cloud.logic.stub.NotInDatabaseException_Exception;
import org.cocome.cloud.logic.stub.ProductOutOfStockException_Exception;
import org.cocome.cloud.logic.stub.UnhandledException_Exception;

public interface ICashDeskQuery {
	public void startSale(@NotNull String cashDeskName, long storeID) throws UnhandledException_Exception, IllegalCashDeskStateException_Exception, NotInDatabaseException_Exception;
	
	public boolean isInExpressMode(@NotNull String cashDeskName, long storeID) throws UnhandledException_Exception, NotInDatabaseException_Exception;
	
	public String getDisplayMessage(@NotNull String cashDeskName, long storeID) throws UnhandledException_Exception, NotInDatabaseException_Exception;
	
	public boolean enterCashAmount(@NotNull String cashDeskName, long storeID, double amount);
	
	public void enterCardInfo(@NotNull String cashDeskName, long storeID, String cardInfo, int pin) throws UnhandledException_Exception, IllegalCashDeskStateException_Exception, NotInDatabaseException_Exception;
	
	public void startCashPayment(@NotNull String cashDeskName, long storeID) throws NotInDatabaseException_Exception, ProductOutOfStockException_Exception, UnhandledException_Exception, IllegalCashDeskStateException_Exception, IllegalInputException_Exception;
	
	public void startCreditCardPayment(@NotNull String cashDeskName, long storeID) throws NotInDatabaseException_Exception, ProductOutOfStockException_Exception, UnhandledException_Exception, IllegalCashDeskStateException_Exception, IllegalInputException_Exception;
}
