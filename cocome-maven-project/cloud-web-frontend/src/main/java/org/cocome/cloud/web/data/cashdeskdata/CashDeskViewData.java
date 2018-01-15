package org.cocome.cloud.web.data.cashdeskdata;

import org.cocome.cloud.web.frontend.cashdesk.ConfiguratorViewData;

import javax.enterprise.context.SessionScoped;
import java.io.Serializable;

@SessionScoped
public class CashDeskViewData implements Serializable {
    private static final long serialVersionUID = 2531025289417846417L;

    private String cashDeskName = "defaultCashDesk";
    private boolean cashDeskNameNeeded = true;

    private boolean saleStarted = false;

    private boolean parameterInputMode = false;
    private boolean cashPayment = false;
    private boolean cardPayment = false;
    private boolean allItemsRegistered = false;

    private String barcode;

    private boolean inExpressMode = false;
    private String displayMessage = "";
    private String[] printerOutput = {};

    private ConfiguratorViewData configuratorViewData;

    public boolean isSaleStarted() {
        return saleStarted;
    }

    public void setSaleStarted(boolean saleStarted) {
        this.saleStarted = saleStarted;
    }

    public boolean isCashPayment() {
        return cashPayment;
    }

    public void setCashPayment(boolean cashPayment) {
        this.cashPayment = cashPayment;
    }

    public boolean isCardPayment() {
        return cardPayment;
    }

    public void setCardPayment(boolean cardPayment) {
        this.cardPayment = cardPayment;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public boolean isInExpressMode() {
        return inExpressMode;
    }

    public void setInExpressMode(boolean inExpressMode) {
        this.inExpressMode = inExpressMode;
    }

    public String getDisplayMessage() {
        return displayMessage;
    }

    public void setDisplayMessage(String displayMessage) {
        this.displayMessage = displayMessage;
    }

    public String[] getPrinterOutput() {
        return printerOutput;
    }

    public void setPrinterOutput(String[] printerOutput) {
        this.printerOutput = printerOutput;
    }

    public boolean isSaleSuccessful() {
        return displayMessage.contains("Thank you for shopping!");
    }

    public String getCashDeskName() {
        return cashDeskName;
    }

    public void setCashDeskName(String cashDeskName) {
        this.cashDeskName = cashDeskName;
    }

    public boolean isCashDeskNameNeeded() {
        return cashDeskNameNeeded;
    }

    public void setCashDeskNameNeeded(boolean cashDeskNameNeeded) {
        this.cashDeskNameNeeded = cashDeskNameNeeded;
    }

    public void requestNewCashDesk() {
        cashDeskNameNeeded = true;
    }

    public boolean isAllItemsRegistered() {
        return allItemsRegistered;
    }

    public void setAllItemsRegistered(boolean allItemsRegistered) {
        this.allItemsRegistered = allItemsRegistered;
    }

    public boolean isParameterInputMode() {
        return parameterInputMode;
    }

    public void setParameterInputMode(boolean parameterInputMode) {
        this.parameterInputMode = parameterInputMode;
    }

    public void setConfiguratorViewData(ConfiguratorViewData configuratorViewData) {
        this.configuratorViewData = configuratorViewData;
    }

    public ConfiguratorViewData getConfiguratorViewData() {
        return this.configuratorViewData;
    }
}
