package org.cocome.cloud.web.data.cashdesk;

import java.io.Serializable;

import javax.enterprise.context.SessionScoped;

@SessionScoped
public class CashDesk implements Serializable, ICashDesk {
	private static final long serialVersionUID = 2531025289417846417L;
	
	private String cashDeskName = "defaultCashDesk";
	private boolean cashDeskNameNeeded = true;
	
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
}
