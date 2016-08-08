package org.cocome.cloud.web.frontend.cashdesk;

import java.io.Serializable;

import javax.enterprise.context.ConversationScoped;
import javax.inject.Named;

@Named
@ConversationScoped
public class CashDeskData implements Serializable {
	private static final long serialVersionUID = -3795002839712194728L;
	

	private boolean saleStarted = false;
	private boolean cashPayment = false;
	private boolean cardPayment = false;

	private String barcode;
	
	private boolean inExpressMode = false;
	private String displayMessage = "";
	private String[] printerOutput = {};

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

}
