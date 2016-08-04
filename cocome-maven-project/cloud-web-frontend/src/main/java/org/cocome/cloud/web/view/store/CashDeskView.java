package org.cocome.cloud.web.view.store;

import java.io.Serializable;

import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.swing.plaf.basic.BasicTreeUI.TreeCancelEditingAction;

import org.cocome.cloud.logic.stub.CashDeskState;
import org.cocome.cloud.logic.stub.IllegalCashDeskStateException_Exception;
import org.cocome.cloud.logic.stub.IllegalInputException_Exception;
import org.cocome.cloud.logic.stub.NotInDatabaseException_Exception;
import org.cocome.cloud.logic.stub.ProductOutOfStockException_Exception;
import org.cocome.cloud.logic.stub.UnhandledException_Exception;
import org.cocome.cloud.web.inventory.connection.ICashDeskQuery;
import org.cocome.cloud.web.inventory.store.IStoreInformation;
import org.cocome.cloud.web.inventory.store.ProductWrapper;
import org.cocome.cloud.web.util.Messages;

@ConversationScoped
@ManagedBean
public class CashDeskView implements Serializable {
	private static final long serialVersionUID = -2512543291563857980L;

	@Inject
	IStoreInformation storeInformation;

	@Inject
	ICashDeskQuery cashDesk;

	@Inject
	Conversation conversation;

	private String cashDeskName = "defaultCashDesk";
	private boolean setCashDeskName = true;
	private boolean saleStarted = false;
	private boolean cashPayment = false;
	private boolean cardPayment = false;
	
	private String barcode;
	private StringBuilder barcodeBuilder;

	public String submitCashDeskName() {
		try {
			cashDesk.startSale(cashDeskName, storeInformation.getActiveStoreID());
		} catch (UnhandledException_Exception | IllegalCashDeskStateException_Exception
				| NotInDatabaseException_Exception e) {
			FacesContext context = FacesContext.getCurrentInstance();
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "ERROR",
					Messages.getLocalizedMessage("cashdesk.error.illegal_state.start_sale")));
			return null;
		}

		setCashDeskName = false;

		if (conversation.isTransient()) {
			conversation.begin();
		}

		return null;
	}

	public void setCashDeskName(String cashDeskName) {
		this.cashDeskName = cashDeskName;
	}

	public String getCashDeskName() {
		return cashDeskName;
	}

	public boolean isSetCashDeskName() {
		return setCashDeskName;
	}

	public boolean isSaleStarted() {
		return saleStarted;
	}

	public boolean isInExpressMode() {
		try {
			return cashDesk.isInExpressMode(cashDeskName, storeInformation.getActiveStoreID());
		} catch (UnhandledException_Exception | NotInDatabaseException_Exception e) {
			FacesContext context = FacesContext.getCurrentInstance();
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "ERROR",
					Messages.getLocalizedMessage("cashdesk.error.express.retrieve")));
			return true;
		}
	}

	public String getDisplayMessage() {
		try {
			return cashDesk.getDisplayMessage(cashDeskName, storeInformation.getActiveStoreID());
		} catch (UnhandledException_Exception | NotInDatabaseException_Exception e) {
			FacesContext context = FacesContext.getCurrentInstance();
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "ERROR",
					Messages.getLocalizedMessage("cashdesk.error.display.retrieve")));
			return "";
		}
	}

	public String enterCashAmount(double cashAmount) {
		if (!cashDesk.enterCashAmount(cashDeskName, storeInformation.getActiveStoreID(), cashAmount)) {
			FacesContext context = FacesContext.getCurrentInstance();
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "ERROR",
					Messages.getLocalizedMessage("cashdesk.error.cash_pay.failed")));
		} else {
			endConversation();
		}
		return null;
	}

	private void endConversation() {
		if (!conversation.isTransient()) {
			conversation.end();
		}
	}

	public String enterCardInfo(String cardInfo, int pin) {
		try {
			cashDesk.enterCardInfo(cashDeskName, storeInformation.getActiveStoreID(), cardInfo, pin);
		} catch (UnhandledException_Exception | IllegalCashDeskStateException_Exception
				| NotInDatabaseException_Exception e) {
			FacesContext context = FacesContext.getCurrentInstance();
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "ERROR",
					String.format(Messages.getLocalizedMessage("cashdesk.error.card_pay.failed"), e.getMessage())));
		}
		return null;
	}

	public String startCashPayment() {
		try {
			cashDesk.startCashPayment(cashDeskName, storeInformation.getActiveStoreID());
		} catch (NotInDatabaseException_Exception | ProductOutOfStockException_Exception | UnhandledException_Exception
				| IllegalCashDeskStateException_Exception | IllegalInputException_Exception e) {
			FacesContext context = FacesContext.getCurrentInstance();
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "ERROR",
					String.format(Messages.getLocalizedMessage("cashdesk.error.start_cash_pay.failed"), e.getMessage())));
		}
		
		cashPayment = true;
		cardPayment = false;
		
		return null;
	}
	
	public String startCardPayment() {
		try {
			cashDesk.startCreditCardPayment(cashDeskName, storeInformation.getActiveStoreID());
		} catch (NotInDatabaseException_Exception | ProductOutOfStockException_Exception | UnhandledException_Exception
				| IllegalCashDeskStateException_Exception | IllegalInputException_Exception e) {
			FacesContext context = FacesContext.getCurrentInstance();
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "ERROR",
					String.format(Messages.getLocalizedMessage("cashdesk.error.start_card_pay.failed"), e.getMessage())));
		}
		
		cardPayment = true;
		cashPayment = false;
		
		return null;
	}
	
	public String resetSale() {
		try {
			cashDesk.startSale(cashDeskName, storeInformation.getActiveStoreID());
		} catch (UnhandledException_Exception | IllegalCashDeskStateException_Exception
				| NotInDatabaseException_Exception e) {
			FacesContext context = FacesContext.getCurrentInstance();
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "ERROR",
					Messages.getLocalizedMessage("cashdesk.error.illegal_state.start_sale")));
		}
		
		saleStarted = true;
		cashPayment = false;
		cardPayment = false;
		
		return null;
	}

	public boolean isCashPayment() {
		return cashPayment;
	}

	public boolean isCardPayment() {
		return cardPayment;
	}

	private void handleFailedValidationMessage(FacesContext context, UIComponent comp, String message) {
		((UIInput) comp).setValid(false);
		FacesMessage wrongInputMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, "ERROR", message);
		context.addMessage(comp.getClientId(), wrongInputMessage);
	}

	public void validateCashAmount(FacesContext context, UIComponent comp, Object value) {
		String input = (String) value;
		double cashAmount;

		try {
			cashAmount = Double.parseDouble(input);
		} catch (NumberFormatException e) {
			handleFailedValidationMessage(context, comp,
					Messages.getLocalizedMessage("cashdesk.validation.amount.failed"));
			return;
		}

		if (cashAmount < 0) {
			handleFailedValidationMessage(context, comp,
					Messages.getLocalizedMessage("cashdesk.validation.amount.failed"));
		}
	}

	public String getBarcode() {
		return barcode;
	}

	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}
}
