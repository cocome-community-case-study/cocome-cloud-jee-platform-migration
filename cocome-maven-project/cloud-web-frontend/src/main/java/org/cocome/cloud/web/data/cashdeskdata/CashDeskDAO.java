package org.cocome.cloud.web.data.cashdeskdata;

import org.cocome.cloud.logic.stub.*;
import org.cocome.cloud.web.connector.cashdeskconnector.CashDeskQuery;
import org.cocome.tradingsystem.inventory.application.plant.parameter.ParameterValueTO;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.Collection;
import java.util.List;

@Named
@RequestScoped
public class CashDeskDAO {

    @Inject
    private CashDeskQuery cashDeskQuery;

    public void startSale(String cashDeskName, long storeID) throws UnhandledException_Exception,
            IllegalCashDeskStateException_Exception, NotInDatabaseException_Exception {
        cashDeskQuery.startSale(cashDeskName, storeID);
    }

    public boolean isInExpressMode(String cashDeskName, long storeID)
            throws UnhandledException_Exception, NotInDatabaseException_Exception {
        return cashDeskQuery.isInExpressMode(cashDeskName, storeID);
    }

    public String getDisplayMessage(String cashDeskName, long storeID)
            throws UnhandledException_Exception, NotInDatabaseException_Exception {
        return cashDeskQuery.getDisplayMessage(cashDeskName, storeID);
    }

    public String[] getPrinterOutput(String cashDeskName, long storeID)
            throws UnhandledException_Exception, NotInDatabaseException_Exception {
        return cashDeskQuery.getPrinterOutput(cashDeskName, storeID);
    }

    public void enterCashAmount(String cashDeskName, long storeID, double amount) throws UnhandledException_Exception,
            IllegalCashDeskStateException_Exception, NotInDatabaseException_Exception {
        cashDeskQuery.enterCashAmount(cashDeskName, storeID, amount);
    }

    public void enterCardInfo(String cashDeskName, long storeID, String cardInfo, int pin)
            throws UnhandledException_Exception, IllegalCashDeskStateException_Exception,
            NotInDatabaseException_Exception {
        cashDeskQuery.enterCardInfo(cashDeskName, storeID, cardInfo, pin);
    }

    public void startCashPayment(String cashDeskName, long storeID)
            throws NotInDatabaseException_Exception, ProductOutOfStockException_Exception, UnhandledException_Exception,
            IllegalCashDeskStateException_Exception, IllegalInputException_Exception {
        cashDeskQuery.startCashPayment(cashDeskName, storeID);
    }

    public void startCreditCardPayment(String cashDeskName, long storeID)
            throws NotInDatabaseException_Exception, ProductOutOfStockException_Exception, UnhandledException_Exception,
            IllegalCashDeskStateException_Exception, IllegalInputException_Exception {
        cashDeskQuery.startCreditCardPayment(cashDeskName, storeID);
    }

    public void enterBarcode(String cashDeskName, long storeID, long barcode)
            throws UnhandledException_Exception, IllegalCashDeskStateException_Exception,
            NotInDatabaseException_Exception, NoSuchProductException_Exception, ProductOutOfStockException_Exception {
        cashDeskQuery.enterBarcode(cashDeskName, storeID, barcode);
    }

    public void enterParameterValues(String cashDeskName, long storeID, List<ParameterValueTO> parameterValues)
            throws UnhandledException_Exception, IllegalCashDeskStateException_Exception,
            NotInDatabaseException_Exception, NoSuchProductException_Exception, ProductOutOfStockException_Exception {
        cashDeskQuery.enterParameterValues(cashDeskName, storeID, parameterValues);
    }

}
