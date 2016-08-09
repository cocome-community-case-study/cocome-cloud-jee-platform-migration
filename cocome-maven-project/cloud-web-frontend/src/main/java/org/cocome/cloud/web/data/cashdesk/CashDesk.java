package org.cocome.cloud.web.data.cashdesk;

import java.io.Serializable;

import javax.enterprise.context.SessionScoped;

@SessionScoped
public class CashDesk implements Serializable, ICashDesk {
	private static final long serialVersionUID = 2531025289417846417L;
	
	private String cashDeskName = "defaultCashDesk";
	private boolean cashDeskNameNeeded = true;
	
	private boolean saleStarted = false;
	private boolean cashPayment = false;
	private boolean cardPayment = false;
	private boolean allItemsRegistered = false;
	
	private String barcode;
	
	private boolean inExpressMode = false;
	private String displayMessage = "";
	private String[] printerOutput = {};

	@Override
	public boolean isSaleStarted() {
		return saleStarted;
	}

	@Override
	public void setSaleStarted(boolean saleStarted) {
		this.saleStarted = saleStarted;
	}

	@Override
	public boolean isCashPayment() {
		return cashPayment;
	}

	@Override
	public void setCashPayment(boolean cashPayment) {
		this.cashPayment = cashPayment;
	}

	@Override
	public boolean isCardPayment() {
		return cardPayment;
	}

	@Override
	public void setCardPayment(boolean cardPayment) {
		this.cardPayment = cardPayment;
	}

	@Override
	public String getBarcode() {
		return barcode;
	}

	@Override
	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}

	@Override
	public boolean isInExpressMode() {
		return inExpressMode;
	}

	@Override
	public void setInExpressMode(boolean inExpressMode) {
		this.inExpressMode = inExpressMode;
	}

	@Override
	public String getDisplayMessage() {
		return displayMessage;
	}

	@Override
	public void setDisplayMessage(String displayMessage) {
		this.displayMessage = displayMessage;
	}

	@Override
	public String[] getPrinterOutput() {
		return printerOutput;
	}

	@Override
	public void setPrinterOutput(String[] printerOutput) {
		this.printerOutput = printerOutput;
	}

	@Override
	public boolean isSaleSuccessful() {
		return displayMessage.contains("Thank you for shopping!");
	}
	
	/* (non-Javadoc)
	 * @see org.cocome.cloud.web.data.cashdesk.ICashDesk#getCashDeskName()
	 */
	@Override
	public String getCashDeskName() {
		return cashDeskName;
	}
	
	/* (non-Javadoc)
	 * @see org.cocome.cloud.web.data.cashdesk.ICashDesk#setCashDeskName(java.lang.String)
	 */
	@Override
	public void setCashDeskName(String cashDeskName) {
		this.cashDeskName = cashDeskName;
	}
	
	/* (non-Javadoc)
	 * @see org.cocome.cloud.web.data.cashdesk.ICashDesk#isCashDeskNameNeeded()
	 */
	@Override
	public boolean isCashDeskNameNeeded() {
		return cashDeskNameNeeded;
	}
	
	/* (non-Javadoc)
	 * @see org.cocome.cloud.web.data.cashdesk.ICashDesk#setCashDeskNameNeeded(boolean)
	 */
	@Override
	public void setCashDeskNameNeeded(boolean cashDeskNameNeeded) {
		this.cashDeskNameNeeded = cashDeskNameNeeded;
	}

	@Override
	public void requestNewCashDesk() {
		cashDeskNameNeeded = true;
	}

	@Override
	public boolean isAllItemsRegistered() {
		return allItemsRegistered;
	}

	@Override
	public void setAllItemsRegistered(boolean allItemsRegistered) {
		this.allItemsRegistered = allItemsRegistered;
	}
}
