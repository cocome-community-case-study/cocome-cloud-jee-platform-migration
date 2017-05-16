/***************************************************************************
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
 ***************************************************************************/

package org.cocome.tradingsystem.cashdeskline.cashdesk;

import java.io.Serializable;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.enterprise.event.Event;
import javax.enterprise.inject.spi.BeanManager;
import javax.inject.Inject;

import org.apache.log4j.Logger;
import org.cocome.tradingsystem.cashdeskline.coordinator.ExpressModePolicy;
import org.cocome.tradingsystem.cashdeskline.datatypes.PaymentMode;
import org.cocome.tradingsystem.cashdeskline.events.AccountSaleEvent;
import org.cocome.tradingsystem.cashdeskline.events.CashAmountEnteredEvent;
import org.cocome.tradingsystem.cashdeskline.events.ChangeAmountCalculatedEvent;
import org.cocome.tradingsystem.cashdeskline.events.ExpressModeDisabledEvent;
import org.cocome.tradingsystem.cashdeskline.events.ExpressModeEnabledEvent;
import org.cocome.tradingsystem.cashdeskline.events.InsufficientCashAmountEvent;
import org.cocome.tradingsystem.cashdeskline.events.InvalidCreditCardEvent;
import org.cocome.tradingsystem.cashdeskline.events.InvalidProductBarcodeEvent;
import org.cocome.tradingsystem.cashdeskline.events.PaymentModeRejectedEvent;
import org.cocome.tradingsystem.cashdeskline.events.PaymentModeSelectedEvent;
import org.cocome.tradingsystem.cashdeskline.events.RunningTotalChangedEvent;
import org.cocome.tradingsystem.cashdeskline.events.SaleFinishedEvent;
import org.cocome.tradingsystem.cashdeskline.events.SaleRegisteredEvent;
import org.cocome.tradingsystem.cashdeskline.events.SaleStartedEvent;
import org.cocome.tradingsystem.cashdeskline.events.SaleSuccessEvent;
import org.cocome.tradingsystem.external.DebitResult;
import org.cocome.tradingsystem.external.IBankLocal;
import org.cocome.tradingsystem.external.TransactionID;
import org.cocome.tradingsystem.inventory.application.store.IStoreInventoryLocal;
import org.cocome.tradingsystem.inventory.application.store.NoSuchProductException;
import org.cocome.tradingsystem.inventory.application.store.ProductOutOfStockException;
import org.cocome.tradingsystem.inventory.application.store.ProductWithStockItemTO;
import org.cocome.tradingsystem.inventory.application.store.SaleTO;
import org.cocome.tradingsystem.util.java.Lists;
import org.cocome.tradingsystem.util.java.Sets;
import org.cocome.tradingsystem.util.qualifier.CashDeskRequired;
import org.cocome.tradingsystem.util.scope.CashDeskSessionScoped;
import org.cocome.tradingsystem.util.scope.IContextRegistry;
import org.cocome.tradingsystem.util.scope.RegistryKeys;
import org.cocome.tradingsystem.util.scope.context.INamedSessionContext;

/**
 * Implements the cash desk model. The model provides methods to process a sale,
 * which will be typically triggered by events from other cash desk components.
 * The methods that can be called depend on the state of the cash desk.
 * 
 * @author Yannick Welsch
 * @author Lubomir Bulej
 * @author Tobias Pöppke
 * @author Robert Heinrich
 */

@CashDeskSessionScoped
public class CashDeskModel implements Serializable, ICashDeskModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1990275917445989607L;

	private static final Logger LOG =
			Logger.getLogger(CashDeskModel.class);

	private static final String INVALID_CARD_INFO = "XXXX XXXX XXXX XXXX";

	/**
	 * New sale can be started (and thus current sale aborted) in almost
	 * all cash desk states except when already paid by cash.
	 */
	private static final Set<CashDeskState> START_SALE_STATES = CashDeskModel.setOfStates(
			CashDeskState.EXPECTING_SALE,
			CashDeskState.EXPECTING_ITEMS,
			CashDeskState.EXPECTING_PAYMENT,
			CashDeskState.PAYING_BY_CASH,
			CashDeskState.EXPECTING_CARD_INFO,
			CashDeskState.PAYING_BY_CREDIT_CARD);

	/**
	 * Items can be only added to the sale after it has been started and
	 * has not been indicated by the cashier as finished.
	 */
	private static final Set<CashDeskState> ADD_ITEM_TO_SALE_STATES =
			CashDeskModel.setOfStates(CashDeskState.EXPECTING_ITEMS);

	/**
	 * Sale can be only finished when scanning items.
	 */
	private static final Set<CashDeskState> FINISH_SALES_STATES =
			CashDeskModel.setOfStates(CashDeskState.EXPECTING_ITEMS);

	/**
	 * Payment mode can be selected either when a sale has been finished or
	 * when switching from credit card payment to cash.
	 */
	private static final Set<CashDeskState> SELECT_PAYMENT_MODE_STATES =
			CashDeskModel.setOfStates(CashDeskState.EXPECTING_PAYMENT, CashDeskState.EXPECTING_CARD_INFO, CashDeskState.PAYING_BY_CREDIT_CARD);

	/**
	 * Cash payment can only proceed when the cashier selected the cash
	 * payment mode.
	 */
	private static final Set<CashDeskState> START_CASH_PAYMENT_STATES =
			CashDeskModel.setOfStates(CashDeskState.PAYING_BY_CASH);

	/**
	 * In cash payment mode, sale is finished when it has been paid for
	 * and the cash box has been closed.
	 */
	private static final Set<CashDeskState> FINISH_CASH_PAYMENT_STATES =
			CashDeskModel.setOfStates(CashDeskState.PAID_BY_CASH);

	/**
	 * Credit card payment can be made only when the cashier selected the credit
	 * card payment mode or when rescanning the card.
	 */
	private static final Set<CashDeskState> START_CREADIT_CARD_PAYMENT_STATES =
			CashDeskModel.setOfStates(CashDeskState.EXPECTING_CARD_INFO, CashDeskState.PAYING_BY_CREDIT_CARD);

	/**
	 * In credit card payment mode, sale is finished only when we have the
	 * credit card info.
	 */
	private static final Set<CashDeskState> FINISH_CREDIT_CARD_PAYMENT_STATES =
			CashDeskModel.setOfStates(CashDeskState.PAYING_BY_CREDIT_CARD);

	CashDeskState state = CashDeskState.EXPECTING_SALE;

	private final ExpressModePolicy expressModePolicy = ExpressModePolicy.getInstance();
	
	private String name;
	
	@Inject
	BeanManager manager;

	// Cash desk related information is required here
	@Inject @CashDeskRequired
	IContextRegistry registry;
	
	@Inject
	IBankLocal remoteBank;

	@Inject
	IStoreInventoryLocal inventory;
	
	@Inject 
	Event<InvalidProductBarcodeEvent> invalidProductBarcodeEvents;
	
	@Inject 
	Event<RunningTotalChangedEvent> runningTotalChangedEvents;
	
	@Inject 
	Event<SaleStartedEvent> saleStartedEvents;
	
	@Inject 
	Event<SaleFinishedEvent> saleFinishedEvents;
	
	@Inject 
	Event<PaymentModeSelectedEvent> paymentMethodSelectedEvents;
	
	@Inject 
	Event<PaymentModeRejectedEvent> paymentModeRejectedEvents;
	
	@Inject 
	Event<ChangeAmountCalculatedEvent> changeAmountCalculatedEvents;
	
	@Inject 
	Event<InvalidCreditCardEvent> invalidCreditCardEvents;
	
	@Inject 
	Event<AccountSaleEvent> accountSaleEvents;
	
	@Inject 
	Event<SaleSuccessEvent> saleSuccessEvents;
	
	@Inject 
	Event<SaleRegisteredEvent> saleRegisteredEvents;
	
	@Inject 
	Event<ExpressModeEnabledEvent> expressModeEnabledEvents;
	
	@Inject 
	Event<ExpressModeDisabledEvent> expressModeDisabledEvents;
	
	@Inject 
	Event<InsufficientCashAmountEvent> insufficientCashAmountEvents;
	
	@Inject 
	Event<CashAmountEnteredEvent> cashAmountEnteredEvents;

	//
	// Cash desk state
	//

	boolean expressModeEnabled = false;

	//
	// Sale state
	//



	List<ProductWithStockItemTO> saleProducts;

	private double runningTotal;

	String cardInfo;
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setName(String cashDeskName) {
		this.name = cashDeskName;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getName() {
		return this.name;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public CashDeskState getState() {
		return this.state;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isInExpressMode() {
		return this.expressModeEnabled;
	}

	//
	// State mutator methods
	//

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void startSale() throws IllegalCashDeskStateException  {
		this.ensureStateIsLegal(START_SALE_STATES);
		this.state = CashDeskState.EXPECTING_ITEMS;
		this.resetSale();
		this.sendSaleStartedEvent();
	}

	@PostConstruct
	void initCashDesk() {
		INamedSessionContext context = (INamedSessionContext) manager.getContext(CashDeskSessionScoped.class);
		LOG.debug("SaleStartedObservers: " + manager.resolveObserverMethods(new SaleStartedEvent()));
		this.name = context.getName();
		LOG.debug("New CashDeskModel created with name " + this.name);
		this.resetSale();
	}

	private void resetSale() {
		this.runningTotal = 0.0;
		this.saleProducts = Lists.newArrayList();
		this.cardInfo = INVALID_CARD_INFO;
	}

	private void sendSaleStartedEvent() {
		saleStartedEvents.fire(new SaleStartedEvent());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void addItemToSale(final long barcode) throws IllegalCashDeskStateException, ProductOutOfStockException {
		this.ensureStateIsLegal(ADD_ITEM_TO_SALE_STATES);

		//
		// If we can accept more items into the sale, add the item to the sale,
		// otherwise complain of exceeding the express mode item count limit.
		//
		if (this.canAcceptNextItem()) {

			//
			// Using the barcode, query the store inventory product stock item
			// and add it to the sale. If there is no product with the given
			// barcode, notify the other cash desk components and ignore the
			// event. Any other failures will cause session rolled back.
			//
			long storeID = registry.getLong(RegistryKeys.STORE_ID);
			try {
				final ProductWithStockItemTO productStockItem =
						this.inventory.getProductWithStockItem(storeID, barcode);
				this.addItemToSale(productStockItem);

			} catch (NoSuchProductException nspe) {
				LOG.info("No product/stock item for barcode " + barcode);
				this.sendInvalidProductBarcodeEvent(barcode);

			}

		} else {
			// TODO Consider notifying other components. --LB
			LOG.error(String.format(
					"Cannot process more than %d items in express mode!",
					this.expressModePolicy.getExpressItemLimit()
					));
		}
	}

	private boolean canAcceptNextItem() {
		final boolean expressModeDisabled = !this.expressModeEnabled;
		final boolean itemCountUnderLimit = this.saleProducts.size() < this.expressModePolicy.getExpressItemLimit();

		return expressModeDisabled || itemCountUnderLimit;
	}

	private void sendInvalidProductBarcodeEvent(final long barcode) {
		invalidProductBarcodeEvents.fire(new InvalidProductBarcodeEvent(barcode));
	}

	private void addItemToSale(ProductWithStockItemTO product) throws ProductOutOfStockException {		
		checkSufficientProductStockAmount(product);
		
		//
		// Add the product item to the sale and update the running total.
		//
		this.saleProducts.add(product);

		final double price = product.getStockItemTO().getSalesPrice();
		
		this.runningTotal = this.computeNewRunningTotal(price);

		this.sendRunningTotalChangedEvent(product.getName(), price);
	}

	private void checkSufficientProductStockAmount(
			ProductWithStockItemTO product)
			throws ProductOutOfStockException {
		long currentAmount = product.getStockItemTO().getAmount();
		if (saleProducts.contains(product)) {
			ProductWithStockItemTO referenceProd = saleProducts.get(saleProducts.indexOf(product));
			currentAmount = referenceProd.getStockItemTO().getAmount();
			if (currentAmount > 0) {
				referenceProd.getStockItemTO().setAmount(currentAmount - 1);
			}
		}
		
		if (currentAmount < 1) {
			throw new ProductOutOfStockException("The requested product is not in Stock anymore!");
		}
	}

	private double computeNewRunningTotal(final double price) {
		final double result = this.runningTotal + price;
		return Math.rint(100 * result) / 100;
	}

	private void sendRunningTotalChangedEvent(String productName, 
			double salePrice) {
		runningTotalChangedEvents.fire(new RunningTotalChangedEvent(
				productName, salePrice, this.runningTotal));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void finishSale() throws IllegalCashDeskStateException {
		this.ensureStateIsLegal(FINISH_SALES_STATES);

		//
		// If there actually are any items in the sale, the sale can be finished
		// and the cashier will proceed to payment mode selection.
		//
		if (this.saleProducts.size() > 0) {
			this.state = CashDeskState.EXPECTING_PAYMENT;
			this.sendSaleFinishedEvent();
		}
	}

	private void sendSaleFinishedEvent() {
		saleFinishedEvents.fire(new SaleFinishedEvent());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void selectPaymentMode(final PaymentMode mode) throws IllegalCashDeskStateException {
		this.ensureStateIsLegal(SELECT_PAYMENT_MODE_STATES);
		//
		// Cash payment can be selected whenever payment mode selection is
		// legal (i.e. even in the middle of credit card payment, in case
		// it fails).
		//
		if (mode == PaymentMode.CASH) {
			this.state = CashDeskState.PAYING_BY_CASH;
			this.sendPaymentModeSelectedEvent(mode);

		} else if (mode == PaymentMode.CREDIT_CARD) {
			//
			// Credit card payment can be only selected when the
			// cash desk is in normal (not express) mode.
			//
			if (!this.expressModeEnabled) {
				this.state = CashDeskState.EXPECTING_CARD_INFO;
				this.sendPaymentModeSelectedEvent(mode);

			} else {
				final String message = "Credit cards not accepted in express mode";
				this.sendPaymentModeRejectedEvent(mode, message);
				LOG.info(message);
			}

		} else {
			final String message = "Unknown payment mode: " + mode;
			this.sendPaymentModeRejectedEvent(mode, message);
			LOG.error(message);
		}
	}


	private void sendPaymentModeSelectedEvent(final PaymentMode mode) {
		paymentMethodSelectedEvents.fire(new PaymentModeSelectedEvent(mode));
	}


	private void sendPaymentModeRejectedEvent(
			final PaymentMode mode, final String reason
			) {
		paymentModeRejectedEvents.fire(new PaymentModeRejectedEvent(mode, reason));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void startCashPayment(final double amount) throws IllegalCashDeskStateException {
		this.ensureStateIsLegal(START_CASH_PAYMENT_STATES);
		//
		// Calculate the change to be given back. Negative change amount means
		// that the paid amount was insufficient.
		//
		final double change = this.computeChangeAmount(amount);
		if (Math.signum(change) >= 0) {
			this.state = CashDeskState.PAID_BY_CASH;
			this.sendCashAmountEnteredEvent(amount);
			this.sendChangeAmountCalculatedEvent(change);

		} else {
			LOG.error(String.format(
					"Insufficient cash (%f) for the sale total (%f)!",
					amount, this.runningTotal
					));
			insufficientCashAmountEvents.fire(new InsufficientCashAmountEvent(amount, runningTotal));
		}
	}

	private void sendCashAmountEnteredEvent(final double amount) {
		cashAmountEnteredEvents.fire(new CashAmountEnteredEvent(amount));
	}

	private double computeChangeAmount(final double amount) {
		// Calculate and round change to return.
		final double changeAmount = amount - this.runningTotal;
		return Math.rint(100 * changeAmount) / 100;
	}


	private void sendChangeAmountCalculatedEvent(final double amount) {
		changeAmountCalculatedEvents.fire(new ChangeAmountCalculatedEvent(amount));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void finishCashPayment() throws IllegalCashDeskStateException {
		this.ensureStateIsLegal(FINISH_CASH_PAYMENT_STATES);

		//

		this.state = CashDeskState.EXPECTING_SALE;
		this.makeSale(PaymentMode.CASH);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void startCreditCardPayment(final String cardInfo) throws IllegalCashDeskStateException { // NOCS
		this.ensureStateIsLegal(START_CREADIT_CARD_PAYMENT_STATES);

		this.cardInfo = cardInfo;
		this.state = CashDeskState.PAYING_BY_CREDIT_CARD;
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public void finishCreditCardPayment(int pin) throws IllegalCashDeskStateException {
		this.ensureStateIsLegal(FINISH_CREDIT_CARD_PAYMENT_STATES);
		//
		// Validate the card. If the card is invalid, notify other components
		// about it and ignore the request.
		//
		final TransactionID creditCardTid = remoteBank.validateCard(this.cardInfo, pin);
		if (creditCardTid == null) {
			this.sendInvalidCreditCardEvent(this.cardInfo);
			return;
		}

		//
		// Charge the card.
		//
		final DebitResult debitResult = remoteBank.debitCard(creditCardTid);
		if (debitResult == DebitResult.OK) {
			//
			// If the card has been charged, account the finished sale and
			// prepare for the next sale.
			//
			this.state = CashDeskState.EXPECTING_SALE;
			this.makeSale(PaymentMode.CREDIT_CARD);

		} else if (debitResult == DebitResult.INVALID_TRANSACTION_ID) {
			//
			// If the transaction ID is invalid, notify other components and
			// restart the card payment process.
			//
			// TODO Use different event for invalid transaction id.
			//
			LOG.info("Invalid transaction ID, rescan card.");
			this.sendInvalidCreditCardEvent(this.cardInfo);
			this.state = CashDeskState.EXPECTING_CARD_INFO;

		} else if (debitResult == DebitResult.INSUFFICIENT_BALANCE) {
			//
			// If there are not enough money, notify other components and
			// restart the card payment process.
			//
			// TODO Use different event for insufficient money..
			// TODO Display should show "not enough money"
			//
			LOG.info("Not enough money on the account");
			this.sendInvalidCreditCardEvent(this.cardInfo);
			this.state = CashDeskState.EXPECTING_CARD_INFO;

		} else {
			LOG.error("Unexpected debit result " + debitResult);
		}
	}


	private void sendInvalidCreditCardEvent(final String cardInfo) { // NOCS
		invalidCreditCardEvents.fire(new InvalidCreditCardEvent(cardInfo));
	}

	//

	private void makeSale(final PaymentMode mode) {
		final SaleTO saleTO = this.createSaleTO();

		//
		// Request the store inventory system to account for the sale.
		// This should use JMS so that the notification can be asynchronous
		// and the message persisted.
		//
		this.sendAccountSaleEvent(saleTO);

		//
		// Notify cash desk components that sale has been successful.
		//
		this.sendSaleSuccessEvent();

		//
		// Notify the coordinator about the sale, providing basic statistics.
		//
		this.sendSaleRegisteredEvent(saleTO.getProductTOs().size(), mode);
	}

	private SaleTO createSaleTO() {
		final SaleTO saleTO = new SaleTO();
		saleTO.setDate(new Date());
		saleTO.setProductTOs(this.saleProducts);
		return saleTO;
	}

	private void sendAccountSaleEvent(final SaleTO saleTO)  {
		final long storeID = registry.getLong(RegistryKeys.STORE_ID);
		accountSaleEvents.fire(new AccountSaleEvent(storeID, saleTO));
	}

	private void sendSaleSuccessEvent() {
		saleSuccessEvents.fire(new SaleSuccessEvent());
	}

	private void sendSaleRegisteredEvent(
			final int itemCount, final PaymentMode mode
			) {
		saleRegisteredEvents.fire(new SaleRegisteredEvent(this.name, itemCount, mode));
	}

	//
	// Cash desk can be switched to express mode in all states.
	//
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void enableExpressMode() {
		if (!this.expressModeEnabled) {
			this.expressModeEnabled = true;
			LOG.debug("express mode enabled in model " + this.toString());
			this.sendExpressModeEnabledEvent(this.name);
		}
	}

	private void sendExpressModeEnabledEvent(final String cashDeskName) {
		expressModeEnabledEvents.fire(new ExpressModeEnabledEvent(cashDeskName));
	}

	//
	// The express mode can be disabled in all states.
	//
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void disableExpressMode() {
		if (this.expressModeEnabled) {
			this.expressModeEnabled = false;
			LOG.error("express mode disabled in model " + this.toString());
			this.sendExpressModeDisabledEvent();
		}
	}

	private void sendExpressModeDisabledEvent() {
		expressModeDisabledEvents.fire(new ExpressModeDisabledEvent());
	}

	//

	private void ensureStateIsLegal(final Set<CashDeskState> legalStates) throws IllegalCashDeskStateException {
		final CashDeskState currentState = this.state;
		if (!legalStates.contains(currentState)) {
			throw new IllegalCashDeskStateException(this.state, legalStates);
		}
	}

	/**
	 * State collection factory.
	 * 
	 * @param elements
	 *            number of states
	 * @return return an enum value set
	 */
	@SafeVarargs
	private static <E extends Enum<E>> Set<E> setOfStates(final E... elements) {
		return Collections.unmodifiableSet(Sets.newEnumSet(elements));
	}

}
