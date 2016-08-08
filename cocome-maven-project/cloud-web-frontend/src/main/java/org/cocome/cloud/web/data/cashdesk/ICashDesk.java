package org.cocome.cloud.web.data.cashdesk;

public interface ICashDesk {

	String getCashDeskName();

	void setCashDeskName(String cashDeskName);

	boolean isCashDeskNameNeeded();

	void setCashDeskNameNeeded(boolean cashDeskNameNeeded);

	void requestNewCashDesk();
}