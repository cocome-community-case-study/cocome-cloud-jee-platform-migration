/*
 **************************************************************************
 * Copyright 2013 DFG SPP 1593 (http://dfg-spp1593.de)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 **************************************************************************
 */

package org.cocome.cloud.web.frontend.cashdesk;

import java.io.Serializable;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

import org.cocome.cloud.web.data.enterprisedata.ParameterQuery;
import org.cocome.cloud.web.data.enterprisedata.RecipeDAO;
import org.cocome.cloud.web.data.enterprisedata.RecipeOperationQuery;
import org.cocome.tradingsystem.inventory.application.enterprise.CustomProductTO;
import org.cocome.cloud.logic.stub.*;
import org.cocome.cloud.web.connector.enterpriseconnector.EnterpriseQuery;
import org.cocome.cloud.web.data.cashdeskdata.CashDeskViewData;
import org.cocome.cloud.web.data.cashdeskdata.CashDeskDAO;
import org.cocome.cloud.web.data.storedata.ProductWrapper;
import org.cocome.cloud.web.frontend.navigation.NavigationElements;
import org.cocome.cloud.web.frontend.store.StoreInformation;
import org.cocome.cloud.web.frontend.util.InputValidator;
import org.cocome.cloud.web.frontend.util.Messages;
import org.cocome.tradingsystem.inventory.application.plant.recipe.RecipeTO;

@ViewScoped
@ManagedBean
public class CashDeskView implements Serializable {
    private static final long serialVersionUID = -2512543291563857980L;

    private static final String[] EMPTY_OUTPUT = {};

    @Inject
    private StoreInformation storeInformation;

    @Inject
    private CashDeskDAO cashDeskDAO;

    @Inject
    private CashDeskViewData cashDesk;

    @Inject
    private EnterpriseQuery enterpriseQuery;

    @Inject
    private RecipeDAO recipeDAO;

    @Inject
    private ParameterQuery parameterQuery;

    private ConfiguratorViewData configuratorViewData;

    public String submitCashDeskName() {
        cashDesk.setCashDeskNameNeeded(false);
        updateExpressMode();
        return resetSale();
    }

    private void updateDisplayAndPrinter() {
        updateDisplayMessage();
        updatePrinterOutput();
    }

    private String getSalePageRedirectOutcome() {
        return NavigationElements.START_SALE.getNavigationOutcome();
    }

    public void setCashDeskName(String cashDeskName) {
        cashDesk.setCashDeskName(cashDeskName);
    }

    public String getCashDeskName() {
        return cashDesk.getCashDeskName();
    }

    public boolean isCashDeskNameNeeded() {
        return cashDesk.isCashDeskNameNeeded();
    }

    public boolean isSaleStarted() {
        return cashDesk.isSaleStarted();
    }

    public boolean isParameterInputMode() {
        return cashDesk.isParameterInputMode();
    }

    public boolean isInExpressMode() {
        return cashDesk.isInExpressMode();
    }

    public String getDisplayMessage() {
        return cashDesk.getDisplayMessage();
    }

    public String[] getPrinterOutput() {
        return cashDesk.getPrinterOutput();
    }

    public ConfiguratorViewData getConfiguratorViewData() {
        return configuratorViewData;
    }

    private void updateExpressMode() {
        String cashDeskName = cashDesk.getCashDeskName();
        long storeID = storeInformation.getActiveStoreID();

        boolean expressMode = false;

        try {
            expressMode = cashDeskDAO.isInExpressMode(cashDeskName, storeID);
        } catch (UnhandledException_Exception | NotInDatabaseException_Exception e) {
            addFacesError(Messages.get("cashdesk.error.express.retrieve"));
        }

        cashDesk.setInExpressMode(expressMode);
    }

    private void addFacesError(String errorString) {
        FacesContext context = FacesContext.getCurrentInstance();
        context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "ERROR", errorString));
    }

    private void updateDisplayMessage() {
        String cashDeskName = cashDesk.getCashDeskName();
        long storeID = storeInformation.getActiveStoreID();

        String displayMessage = "";

        try {
            displayMessage = cashDeskDAO.getDisplayMessage(cashDeskName, storeID);
        } catch (UnhandledException_Exception | NotInDatabaseException_Exception e) {
            addFacesError(Messages.get("cashdesk.error.display.retrieve"));
        }

        cashDesk.setDisplayMessage(displayMessage);
    }

    public String enterCashAmount(double cashAmount) {
        String cashDeskName = cashDesk.getCashDeskName();
        long storeID = storeInformation.getActiveStoreID();

        try {
            cashDeskDAO.enterCashAmount(cashDeskName, storeID, cashAmount);
        } catch (UnhandledException_Exception | NotInDatabaseException_Exception | IllegalCashDeskStateException_Exception e) {
            addFacesError(String.format(Messages.get("cashdesk.error.cash_pay.failed"), e.getMessage()));
        }

        updateDisplayAndPrinter();
        updateExpressMode();
        return getSalePageRedirectOutcome();
    }

    public String enterCardInfo(String cardInfo, int pin) {
        String cashDeskName = cashDesk.getCashDeskName();
        long storeID = storeInformation.getActiveStoreID();

        try {
            cashDeskDAO.enterCardInfo(cashDeskName, storeID, cardInfo, pin);
        } catch (UnhandledException_Exception | IllegalCashDeskStateException_Exception
                | NotInDatabaseException_Exception e) {
            addFacesError(
                    String.format(Messages.get("cashdesk.error.card_pay.failed"), e.getMessage()));
        }
        updateDisplayAndPrinter();
        updateExpressMode();
        return getSalePageRedirectOutcome();
    }

    public String startCashPayment() {
        String cashDeskName = cashDesk.getCashDeskName();
        long storeID = storeInformation.getActiveStoreID();

        try {
            cashDeskDAO.startCashPayment(cashDeskName, storeID);
            cashDesk.setAllItemsRegistered(true);
            cashDesk.setCashPayment(true);
            cashDesk.setCardPayment(false);
        } catch (NotInDatabaseException_Exception | ProductOutOfStockException_Exception | UnhandledException_Exception
                | IllegalCashDeskStateException_Exception | IllegalInputException_Exception e) {
            addFacesError(String.format(Messages.get("cashdesk.error.start_cash_pay.failed"),
                    e.getMessage()));
        }

        updateDisplayAndPrinter();

        return getSalePageRedirectOutcome();
    }

    public String startCardPayment() {
        String cashDeskName = cashDesk.getCashDeskName();
        long storeID = storeInformation.getActiveStoreID();

        try {
            cashDeskDAO.startCreditCardPayment(cashDeskName, storeID);
            cashDesk.setAllItemsRegistered(true);
            cashDesk.setCardPayment(true);
            cashDesk.setCashPayment(false);
        } catch (NotInDatabaseException_Exception | ProductOutOfStockException_Exception | UnhandledException_Exception
                | IllegalCashDeskStateException_Exception | IllegalInputException_Exception e) {
            addFacesError(String.format(Messages.get("cashdesk.error.start_card_pay.failed"),
                    e.getMessage()));
        }

        updateDisplayAndPrinter();

        return getSalePageRedirectOutcome();
    }

    public String resetSale() {

        String cashDeskName = cashDesk.getCashDeskName();
        long storeID = storeInformation.getActiveStoreID();

        try {
            cashDeskDAO.startSale(cashDeskName, storeID);
            cashDesk.setSaleStarted(true);
            cashDesk.setCashPayment(false);
            cashDesk.setCardPayment(false);
            cashDesk.setParameterInputMode(false);
            clearBarcode();
        } catch (UnhandledException_Exception | IllegalCashDeskStateException_Exception
                | NotInDatabaseException_Exception e) {
            addFacesError(Messages.get("cashdesk.error.illegal_state.start_sale"));
        }


        updateDisplayAndPrinter();

        return getSalePageRedirectOutcome();
    }

    public boolean isCashPayment() {
        return cashDesk.isCashPayment();
    }

    public boolean isCardPayment() {
        return cashDesk.isCardPayment();
    }

    public String getBarcode() {
        return cashDesk.getBarcode();
    }

    public void setBarcode(String barcode) {
        cashDesk.setBarcode(barcode);
    }

    private long convertBarcode() throws NumberFormatException {
        long barcode = Long.parseLong(cashDesk.getBarcode());
        if (barcode < 0) {
            throw new NumberFormatException("Barcode must be positive!");
        }
        return barcode;
    }

    public String enterParameterValues() {
        String cashDeskName = cashDesk.getCashDeskName();
        long storeID = storeInformation.getActiveStoreID();
        try {
            cashDeskDAO.enterParameterValues(cashDeskName, storeID, this.configuratorViewData.getParameterValues());
        } catch (UnhandledException_Exception | IllegalCashDeskStateException_Exception
                | NotInDatabaseException_Exception | NoSuchProductException_Exception
                | ProductOutOfStockException_Exception e) {
            addFacesError(String.format(Messages.get("cashdesk.parameter.value.enter.failed"), e.getMessage()));
        }

        updateDisplayAndPrinter();
        return getSalePageRedirectOutcome();
    }

    public String scanBarcode() {
        long barcode;
        try {
            barcode = convertBarcode();
        } catch (NumberFormatException e) {
            InputValidator.handleFailedValidationMessage(FacesContext.getCurrentInstance(),
                    FacesContext.getCurrentInstance().getViewRoot().findComponent("barcodetext"),
                    Messages.get("cashdesk.validation.barcode.failed"));
            return getSalePageRedirectOutcome();
        }

        String cashDeskName = cashDesk.getCashDeskName();
        long storeID = storeInformation.getActiveStoreID();

        try {
            cashDeskDAO.enterBarcode(cashDeskName, storeID, barcode);
            final ProductWrapper product = enterpriseQuery.getProductByBarcode(barcode);
            storeInformation.queryCustomProducts();
            if(product.getProduct() instanceof CustomProductTO) {
                final RecipeTO recipe = this.recipeDAO.queryRecipe(product.getProduct().getBarcode());
                this.configuratorViewData = new ConfiguratorViewData(parameterQuery.getAllByParent(recipe));
                this.cashDesk.setParameterInputMode(true);
            }
        } catch (UnhandledException_Exception | IllegalCashDeskStateException_Exception
                | NotInDatabaseException_Exception | NoSuchProductException_Exception
                | ProductOutOfStockException_Exception e) {
            addFacesError(String.format(Messages.get("cashdesk.barcode.scan.failed"), e.getMessage()));
        }

        updateDisplayAndPrinter();
        return getSalePageRedirectOutcome();
    }

    public void addDigitToBarcode(char digit) {
        // TODO Perhaps use a StringBuilder for this
        cashDesk.setBarcode(cashDesk.getBarcode() + digit);
    }

    public void clearBarcode() {
        cashDesk.setBarcode("");
    }

    public void removeLastBarcodeDigit() {
        String barcode = cashDesk.getBarcode();
        cashDesk.setBarcode(barcode.substring(0, barcode.length() - 2));
    }

    private void updatePrinterOutput() {
        String cashDeskName = cashDesk.getCashDeskName();
        long storeID = storeInformation.getActiveStoreID();

        String[] printerOutput;

        try {
            printerOutput = cashDeskDAO.getPrinterOutput(cashDeskName, storeID);
        } catch (UnhandledException_Exception | NotInDatabaseException_Exception e) {
            addFacesError(
                    String.format(Messages.get("cashdesk.error.printer.retrieve"), e.getMessage()));
            printerOutput = EMPTY_OUTPUT;
        }
        cashDesk.setPrinterOutput(printerOutput);
    }
}
