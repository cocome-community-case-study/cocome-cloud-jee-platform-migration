package org.cocome.cloud.web.frontend.cashdesk;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import org.apache.log4j.Logger;
import org.cocome.cloud.logic.stub.IllegalCashDeskStateException_Exception;
import org.cocome.cloud.logic.stub.IllegalInputException_Exception;
import org.cocome.cloud.logic.stub.NoSuchProductException_Exception;
import org.cocome.cloud.logic.stub.NotInDatabaseException_Exception;
import org.cocome.cloud.logic.stub.ProductOutOfStockException_Exception;
import org.cocome.cloud.logic.stub.UnhandledException_Exception;
import org.cocome.cloud.web.backend.cashdesk.ICashDeskQuery;
import org.cocome.cloud.web.data.cashdesk.ICashDesk;
import org.cocome.cloud.web.frontend.navigation.NavigationElements;
import org.cocome.cloud.web.frontend.store.IStoreInformation;
import org.cocome.cloud.web.util.Messages;

@ConversationScoped
@ManagedBean
public class CashDeskView implements Serializable {
	private static final long serialVersionUID = -2512543291563857980L;
	private static final Logger LOG = Logger.getLogger(CashDeskView.class);

	private static final String[] EMPTY_OUTPUT = {};

	@Inject
	IStoreInformation storeInformation;

	@Inject
	ICashDeskQuery cashDeskQuery;

	@Inject
	Conversation conversation;

	@Inject
	CashDeskData cashDeskData;

	@Inject
	ICashDesk cashDesk;

	@PostConstruct
	public void initCashDesk() {
		if (conversation.isTransient()) {
			conversation.begin();
		}
	}

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
		String arguments;
		if (!conversation.isTransient()) {
			arguments = "?cid=" + conversation.getId();
		} else {
			arguments = "";
		}

		String redirectOutcome = NavigationElements.START_SALE.getNavigationOutcome() + arguments;

		return redirectOutcome;
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
		return cashDeskData.isSaleStarted();
	}

	public boolean isInExpressMode() {
		return cashDeskData.isInExpressMode();
	}

	public String getDisplayMessage() {
		return cashDeskData.getDisplayMessage();
	}

	public String[] getPrinterOutput() {
		return cashDeskData.getPrinterOutput();
	}

	public void updateExpressMode() {
		try {
			cashDeskData.setInExpressMode(
					cashDeskQuery.isInExpressMode(cashDesk.getCashDeskName(), storeInformation.getActiveStoreID()));
		} catch (UnhandledException_Exception | NotInDatabaseException_Exception e) {
			addFacesError(Messages.getLocalizedMessage("cashdesk.error.express.retrieve"));
			cashDeskData.setInExpressMode(false);
		}
	}

	private void addFacesError(String errorString) {
		FacesContext context = FacesContext.getCurrentInstance();
		context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "ERROR",
				errorString));
	}

	public void updateDisplayMessage() {
		try {
			cashDeskData.setDisplayMessage(
					cashDeskQuery.getDisplayMessage(cashDesk.getCashDeskName(), storeInformation.getActiveStoreID()));
		} catch (UnhandledException_Exception | NotInDatabaseException_Exception e) {
			addFacesError(Messages.getLocalizedMessage("cashdesk.error.display.retrieve"));
			cashDeskData.setDisplayMessage("");
		}
	}

	public String enterCashAmount(double cashAmount) {
		if (!cashDeskQuery.enterCashAmount(cashDesk.getCashDeskName(), storeInformation.getActiveStoreID(),
				cashAmount)) {
			addFacesError(Messages.getLocalizedMessage("cashdesk.error.cash_pay.failed"));
		} else {
			endConversation();
		}
		updateDisplayAndPrinter();
		updateExpressMode();
		return getSalePageRedirectOutcome();
	}

	private void endConversation() {
		if (!conversation.isTransient()) {
			conversation.end();
		}
	}

	public String enterCardInfo(String cardInfo, int pin) {
		try {
			cashDeskQuery.enterCardInfo(cashDesk.getCashDeskName(), storeInformation.getActiveStoreID(), cardInfo, pin);
			endConversation();
		} catch (UnhandledException_Exception | IllegalCashDeskStateException_Exception
				| NotInDatabaseException_Exception e) {
			addFacesError(String.format(Messages.getLocalizedMessage("cashdesk.error.card_pay.failed"), e.getMessage()));
		}
		updateDisplayAndPrinter();
		updateExpressMode();
		return getSalePageRedirectOutcome();
	}

	public String startCashPayment() {
		try {
			cashDeskQuery.startCashPayment(cashDesk.getCashDeskName(), storeInformation.getActiveStoreID());
		} catch (NotInDatabaseException_Exception | ProductOutOfStockException_Exception | UnhandledException_Exception
				| IllegalCashDeskStateException_Exception | IllegalInputException_Exception e) {
			addFacesError(String
					.format(Messages.getLocalizedMessage("cashdesk.error.start_cash_pay.failed"), e.getMessage()));
		}

		cashDeskData.setCashPayment(true);
		cashDeskData.setCardPayment(false);

		updateDisplayAndPrinter();

		return getSalePageRedirectOutcome();
	}

	public String startCardPayment() {
		try {
			cashDeskQuery.startCreditCardPayment(cashDesk.getCashDeskName(), storeInformation.getActiveStoreID());
		} catch (NotInDatabaseException_Exception | ProductOutOfStockException_Exception | UnhandledException_Exception
				| IllegalCashDeskStateException_Exception | IllegalInputException_Exception e) {
			addFacesError(String
					.format(Messages.getLocalizedMessage("cashdesk.error.start_card_pay.failed"), e.getMessage()));
		}

		cashDeskData.setCardPayment(true);
		cashDeskData.setCashPayment(false);

		updateDisplayAndPrinter();

		return getSalePageRedirectOutcome();
	}

	public String resetSale() {
		try {
			cashDeskQuery.startSale(cashDesk.getCashDeskName(), storeInformation.getActiveStoreID());
		} catch (UnhandledException_Exception | IllegalCashDeskStateException_Exception
				| NotInDatabaseException_Exception e) {
			addFacesError(Messages.getLocalizedMessage("cashdesk.error.illegal_state.start_sale"));
		}

		cashDeskData.setSaleStarted(true);
		cashDeskData.setCashPayment(false);
		cashDeskData.setCardPayment(false);

		updateDisplayAndPrinter();

		return getSalePageRedirectOutcome();
	}

	public boolean isCashPayment() {
		return cashDeskData.isCashPayment();
	}

	public boolean isCardPayment() {
		return cashDeskData.isCardPayment();
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
		return cashDeskData.getBarcode();
	}

	public void setBarcode(String barcode) {
		cashDeskData.setBarcode(barcode);
	}

	private long convertBarcode() throws NumberFormatException {
		long barcode = Long.parseLong(cashDeskData.getBarcode());
		if (barcode < 0) {
			throw new NumberFormatException("Barcode must be positive!");
		}
		return barcode;
	}

	public String scanBarcode() {
		long barcode;
		try {
			barcode = convertBarcode();
		} catch (NumberFormatException e) {
			handleFailedValidationMessage(FacesContext.getCurrentInstance(),
					FacesContext.getCurrentInstance().getViewRoot().findComponent("barcodetext"),
					Messages.getLocalizedMessage("cashdesk.validation.barcode.failed"));
			return getSalePageRedirectOutcome();
		}

		try {
			cashDeskQuery.enterBarcode(cashDesk.getCashDeskName(), storeInformation.getActiveStoreID(), barcode);
			updatePrinterOutput();
		} catch (UnhandledException_Exception | IllegalCashDeskStateException_Exception
				| NotInDatabaseException_Exception | NoSuchProductException_Exception
				| ProductOutOfStockException_Exception e) {
			addFacesError(String.format(Messages.getLocalizedMessage("cashdesk.barcode.scan.failed"), e.getMessage()));
		}
		return getSalePageRedirectOutcome();
	}

	public void addDigitToBarcode(char digit) {
		// TODO Perhaps use a StringBuilder for this
		cashDeskData.setBarcode(cashDeskData.getBarcode() + digit);
	}

	public void clearBarcode() {
		cashDeskData.setBarcode("");
	}

	public void removeLastBarcodeDigit() {
		String barcode = cashDeskData.getBarcode();
		cashDeskData.setBarcode(barcode.substring(0, barcode.length() - 2));
	}

	public void updatePrinterOutput() {
		try {
			cashDeskData.setPrinterOutput(
					cashDeskQuery.getPrinterOutput(cashDesk.getCashDeskName(), storeInformation.getActiveStoreID()));
		} catch (UnhandledException_Exception | NotInDatabaseException_Exception e) {
			addFacesError(String.format(Messages.getLocalizedMessage("cashdesk.error.printer.retrieve"), e.getMessage()));
			cashDeskData.setPrinterOutput(EMPTY_OUTPUT);
		}
	}
}
