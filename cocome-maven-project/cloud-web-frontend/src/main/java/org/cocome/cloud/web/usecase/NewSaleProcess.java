package org.cocome.cloud.web.usecase;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.ActionEvent;
import javax.faces.event.ActionListener;
import javax.xml.ws.WebServiceRef;
import javax.xml.ws.soap.SOAPFaultException;

import org.cocome.cloud.logic.stub.ICashBoxService;
import org.cocome.cloud.logic.stub.ICashDeskService;
import org.cocome.cloud.logic.stub.IBarcodeScannerService;
import org.cocome.cloud.logic.stub.IExpressLightService;
import org.cocome.cloud.logic.stub.ICardReaderService;
import org.cocome.cloud.logic.stub.IBarcodeScanner;
import org.cocome.cloud.logic.stub.ICardReader;
import org.cocome.cloud.logic.stub.ICashBox;
import org.cocome.cloud.logic.stub.ICashDesk;
import org.cocome.cloud.logic.stub.IExpressLight;
import org.cocome.cloud.logic.stub.IPrinter;
import org.cocome.cloud.logic.stub.IUserDisplay;
import org.cocome.cloud.logic.stub.IPrinterService;
import org.cocome.cloud.logic.stub.IUserDisplayService;
import org.cocome.cloud.logic.stub.IllegalCashDeskStateException_Exception;
import org.cocome.cloud.logic.stub.UnhandledException_Exception;
import org.cocome.cloud.logic.stub.ProductOutOfStockException_Exception;
import org.cocome.cloud.logic.stub.IllegalInputException_Exception;
import org.cocome.cloud.logic.stub.NoSuchProductException_Exception;


@ManagedBean
@SessionScoped
public class NewSaleProcess implements IUseCase {
//	private static final Logger LOG = Logger.getLogger(NewSaleProcess.class);
	
	@WebServiceRef(ICashBoxService.class)
	ICashBox cashBox;
	
	@WebServiceRef(ICashDeskService.class)
	ICashDesk cashDesk;
	
	@WebServiceRef(IBarcodeScannerService.class)
	IBarcodeScanner barcodeScanner;
	
	@WebServiceRef(IExpressLightService.class)
	IExpressLight expressLight;
	
	@WebServiceRef(ICardReaderService.class)
	ICardReader cardReader;
	
	@WebServiceRef(IPrinterService.class)
	IPrinter printer;
	
	@WebServiceRef(IUserDisplayService.class)
	IUserDisplay userDisplay;
	
	private String enterpriseName;
	private String storeId;
	private String storeName;
	private String storeLocation;
	private String cashDeskName = "defaultCashDesk";
	
	private boolean canFinishSale = false;
	
	private boolean canStartPayment = false;
	

	
	private boolean showPrint = false;
	
	private boolean saleStarted = false;
	
	private String message;
	
	private String userDisplayOut = "";
	
	private String[] printerOut = {};
	
	private boolean payByCash = false;
	private boolean payByCard = false;
	
	private String barcode = "";
	
	private boolean cashDeskNameSet = false;

	private boolean isInExpressMode = false;

	public String getEnterpriseName() {
		return enterpriseName;
	}

	public void setEnterpriseName(String enterpriseName) {
		this.enterpriseName = enterpriseName;
	}

	public String getStoreId() {
		return storeId;
	}

	public void setStoreId(String storeId) {
		this.storeId = storeId;
	}

	public boolean isShowPrint() {
		return showPrint;
	}

	public void setShowPrint(boolean showPrint) {
		this.showPrint = showPrint;
	}

	public String getStoreName() {
		return storeName;
	}

	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}

	public String getStoreLocation() {
		return storeLocation;
	}

	public void setStoreLocation(String storeLocation) {
		this.storeLocation = storeLocation;
	}

	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * @param message the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}

	public boolean isInExpressMode() {
		return isInExpressMode ;
	}

	public void setInExpressMode(boolean express) {
		this.isInExpressMode = express;
	}

	public String getBarcode() {
		return barcode;
	}

	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}

	public boolean isPayByCard() {
		return payByCard;
	}

	public void setPayByCard(boolean payed) {
		this.payByCard = payed;
	}

	private void deactivateAllViewElements() {
		setPayByCard(false);
		setPayByCash(false);
		setSaleStarted(false);
		setShowPrint(false);
	}
	
	private void enableSaleStartedViewElements() {
		setSaleStarted(true);
		setShowPrint(true);
	}
	
	private String getSOAPFaultMessage(SOAPFaultException e) {
		return e.getFault() != null ? e.getFault().getFaultString(): e.getMessage();
	}

	public void updateCashDeskName(String newName) {
		deactivateAllViewElements();
		this.setCashDeskName(newName);
		this.setCashDeskNameSet(true);
		startNewSale();
	}

	public void startNewSale() {
		deactivateAllViewElements();
		setCanFinishSale(false);
		setCanStartPayment(false);
		try {
			cashDesk.startSale(cashDeskName, Long.parseLong(storeId));
			setInExpressMode(cashDesk.isInExpressMode(cashDeskName, Long.parseLong(storeId)));
			enableSaleStartedViewElements();
			updateDisplayComponents();
		} catch (NumberFormatException
				| IllegalCashDeskStateException_Exception
				| UnhandledException_Exception e) {
			FacesContext.getCurrentInstance().addMessage("sale:barcodetext", 
					new FacesMessage("Error!", "Could not start the sale: " + e.getMessage()));
		} catch (SOAPFaultException e) {
			FacesContext.getCurrentInstance().addMessage(
					"sale:barcodetext",
					new FacesMessage("Error",
							getSOAPFaultMessage(e)));
		}
	}
	
	@Override
	public String invoke() throws Exception {
		if(cashDeskNameSet) {			
			// Already been there, so start a new sale
			startNewSale();
		} else {
			setSaleStarted(false);
		}
		return "newsale";
	}
	
	public String barpay() {
		if (!isCanFinishSale()) {
			FacesContext.getCurrentInstance().addMessage("sale:pay-bar", 
					new FacesMessage("No products", 
							"There are no products in this sale yet."));
			return null;
		}
		
		try {
			cashDesk.finishSale(cashDeskName, Long.parseLong(storeId));
			cashDesk.selectPaymentMode(cashDeskName, Long.parseLong(storeId),
					"CASH");
			updateDisplayComponents();
		} catch (NumberFormatException
				| IllegalCashDeskStateException_Exception
				| IllegalInputException_Exception
				| UnhandledException_Exception 
				| ProductOutOfStockException_Exception e) {
			FacesContext.getCurrentInstance().addMessage("sale:pay-bar", 
					new FacesMessage("Error", 
							"Could not select the payment mode."));
			return null;
		} catch (SOAPFaultException e) {
			FacesContext.getCurrentInstance().addMessage(
					"sale:pay-bar",
					new FacesMessage("Error",
							getSOAPFaultMessage(e)));
			return "failed";
		}
		setPayByCash(true);
		setCanStartPayment(true);
		return "Bar Pay";
	}

	public String enterCashAmount(double amount) {		
		if (!isCanStartPayment()) {
			FacesContext.getCurrentInstance().addMessage("sale:pay-bar", 
					new FacesMessage("Not possible", "It is not yet possible to start payment."
							+ " Select a payment method first."));
			return "failed";
		}
		
		long storeID = Long.parseLong(storeId);
		
		try {
			cashDesk.startCashPayment(cashDeskName, storeID, amount);
			cashBox.close(cashDeskName, storeID);
			updateDisplayComponents();
		} catch (NumberFormatException
				| IllegalCashDeskStateException_Exception
				| UnhandledException_Exception e) {
			FacesContext.getCurrentInstance().addMessage("sale:pay-bar", 
					new FacesMessage("Error", 
							"Could not complete the payment process."));
			return "failed";
		} catch (SOAPFaultException e) {
			FacesContext.getCurrentInstance().addMessage(
					"sale:pay-bar",
					new FacesMessage("Error",
							getSOAPFaultMessage(e)));
			return "failed";
		}
		return "success";
	}
	
	public String cardpay() {
		if (this.canFinishSale) {
			long storeID = Long.parseLong(storeId);
			try {
				cashDesk.finishSale(cashDeskName, storeID);
				cashDesk.selectPaymentMode(cashDeskName, storeID,
						"CREDIT_CARD");
				updateDisplayComponents();
			} catch (IllegalCashDeskStateException_Exception
					| ProductOutOfStockException_Exception
					| UnhandledException_Exception
					| IllegalInputException_Exception e) {
				FacesContext.getCurrentInstance().addMessage(
						"sale:pay-card",
						new FacesMessage(
								"Error!",
								"There was an error while finishing the purchase. " + e.getMessage()));
				return "Bar Pay";
			} catch (SOAPFaultException e) {
				FacesContext.getCurrentInstance().addMessage("sale:pay-card",
						new FacesMessage("Error", getSOAPFaultMessage(e)));
				return "Bar Pay";
			}

			setPayByCash(false);
			setPayByCard(true);
			setCanStartPayment(true);
			return "Bar Pay";
		} else {
			FacesContext.getCurrentInstance().addMessage("sale:pay-card", 
					new FacesMessage("No products", 
							"There are no products included in this sale yet."));
		}
		return null;
	}

	public String enterCardInfo(String creditInfo, int pin) {
		if(!canStartPayment) {
			FacesContext.getCurrentInstance().addMessage("sale:pay-card",
					new FacesMessage("Error", "You have to select a payment method first!"));
			return "";
		}
		try {
			long storeID = Long.parseLong(storeId);
			cardReader.sendCreditCardInfo(cashDeskName,
					storeID, creditInfo);
			cardReader.sendCreditCardPin(cashDeskName, storeID, pin);
			updateDisplayComponents();
		} catch (NumberFormatException
				| IllegalCashDeskStateException_Exception
				| UnhandledException_Exception e) {
			FacesContext.getCurrentInstance().addMessage(
					"sale:pay-card",
					new FacesMessage(
							"Error!",
							"There was an error while processing your card. " + e.getMessage()));
		} catch (SOAPFaultException e) {
			FacesContext.getCurrentInstance().addMessage("sale:pay-card",
					new FacesMessage("Error", getSOAPFaultMessage(e)));
		}
		return "Card Pay";
	}

	public String scan() {
		if (barcode.isEmpty() || !barcode.matches("[0-9]+")) {
			FacesContext.getCurrentInstance().addMessage(
					"sale:barcodetext",
					new FacesMessage("Error",
							"The barcode must be a positive integer."));
			this.barcode = "";
			return "";
		}
		
		try {
			barcodeScanner.sendProductBarcode(cashDeskName, Long.parseLong(storeId), Long.parseLong(barcode));
			updateDisplayComponents();
			setCanFinishSale(true);
		} catch (NumberFormatException
				| IllegalCashDeskStateException_Exception
				| NoSuchProductException_Exception
				| UnhandledException_Exception
				| ProductOutOfStockException_Exception e) {
			FacesContext.getCurrentInstance().addMessage(
					"sale:barcodetext",
					new FacesMessage("Error",
							"Could not find the product with the given barcode."));
			this.barcode = "";
			return "";
		} catch (SOAPFaultException e) {
			FacesContext.getCurrentInstance().addMessage(
					"sale:barcodetext",
					new FacesMessage("Error",
							getSOAPFaultMessage(e)));
			this.barcode = "";
			return "";
		}		
		return "";

	}

	private void updateDisplayComponents() throws UnhandledException_Exception {
		long storeID = Long.parseLong(storeId);
		setUserDisplayOut(userDisplay.getMessage(cashDeskName, storeID));
		setPrinterOut(printer.getCurrentPrintout(cashDeskName, storeID).split("\\n"));
		setInExpressMode(cashDesk.isInExpressMode(cashDeskName, storeID));
	}
	
	
	public String nil() {
		this.barcode = this.barcode + "0";
		return "0";
	}

	public String one() {
		this.barcode = this.barcode + "1";
		return "0";
	}

	public String two() {
		this.barcode = this.barcode + "2";
		return "0";
	}

	public String three() {
		this.barcode = this.barcode + "3";
		return "0";
	}

	public String four() {
		this.barcode = this.barcode + "4";
		return "0";
	}

	public String five() {
		this.barcode = this.barcode + "5";
		return "0";
	}

	public String six() {
		this.barcode = this.barcode + "6";
		return "0";
	}

	public String seven() {
		this.barcode = this.barcode + "7";
		return "0";
	}

	public String hight() {
		this.barcode = this.barcode + "8";
		return "0";
	}

	public String nine() {
		this.barcode = this.barcode + "9";
		return "0";
	}
	
	public String removelastDigit() {
		  if(!this.barcode.isEmpty())
			this.barcode = this.barcode.substring(0, this.barcode.length() - 1);
		return "0";
	}
	
	public String removeAll() {
		this.barcode = "";
		return "0";
	}

	public ActionListener createActionListener() {
		return new ActionListener() {
			@Override
			public void processAction(ActionEvent arg0)
					throws AbortProcessingException {
				finish();

			}
		};
	}

	public String goHome() {
		finish();
		return "home";
	}

	

	@Override
	public void attrListener(ActionEvent event) {
		this.enterpriseName = (String) event.getComponent().getAttributes()
				.get("enterpriseName");
		this.storeId = (String) event.getComponent().getAttributes()
				.get("storeId");
		this.storeName = (String) event.getComponent().getAttributes()
				.get("storeName");
		this.storeLocation = (String) event.getComponent().getAttributes()
				.get("storeLocation");
		
	}
	
	@Override
	public String finish() {
		deactivateAllViewElements();
		this.barcode = "";
		return "finish";

	}

	public String getCashDeskName() {
		return cashDeskName;
	}

	public void setCashDeskName(String cashDeskName) {
		this.cashDeskName = cashDeskName;
	}

	public boolean isCashDeskNameSet() {
		return cashDeskNameSet;
	}

	public void setCashDeskNameSet(boolean cashDeskNameSet) {
		this.cashDeskNameSet = cashDeskNameSet;
	}

	public boolean isSaleStarted() {
		return saleStarted;
	}

	public void setSaleStarted(boolean saleStarted) {
		this.saleStarted = saleStarted;
	}

	public String getUserDisplayOut() {
		return userDisplayOut;
	}

	public void setUserDisplayOut(String userDisplayOut) {
		this.userDisplayOut = userDisplayOut;
	}

	public String[] getPrinterOut() {
		return printerOut;
	}

	public void setPrinterOut(String[] printerOut) {
		this.printerOut = printerOut;
	}

	public boolean isPayByCash() {
		return payByCash;
	}

	public void setPayByCash(boolean payByCash) {
		this.payByCash = payByCash;
	}

	public boolean isCanFinishSale() {
		return canFinishSale;
	}

	public void setCanFinishSale(boolean canFinishSale) {
		this.canFinishSale = canFinishSale;
	}

	public boolean isCanStartPayment() {
		return canStartPayment;
	}

	public void setCanStartPayment(boolean canStartPayment) {
		this.canStartPayment = canStartPayment;
	}

}
