package org.cocome.cloud.web.data.cashdeskdata;

public interface ICashDesk {

	String getCashDeskName();

	void setCashDeskName(String cashDeskName);

	boolean isCashDeskNameNeeded();

	void setCashDeskNameNeeded(boolean cashDeskNameNeeded);

	void requestNewCashDesk();

	boolean isSaleSuccessful();

	void setPrinterOutput(String[] printerOutput);

	String[] getPrinterOutput();

	void setDisplayMessage(String displayMessage);

	String getDisplayMessage();

	void setInExpressMode(boolean inExpressMode);

	boolean isInExpressMode();

	void setBarcode(String barcode);

	String getBarcode();

	void setCardPayment(boolean cardPayment);

	boolean isCardPayment();

	void setCashPayment(boolean cashPayment);

	boolean isCashPayment();

	void setSaleStarted(boolean saleStarted);

	boolean isSaleStarted();

	void setAllItemsRegistered(boolean allItemsRegistered);

	boolean isAllItemsRegistered();
}