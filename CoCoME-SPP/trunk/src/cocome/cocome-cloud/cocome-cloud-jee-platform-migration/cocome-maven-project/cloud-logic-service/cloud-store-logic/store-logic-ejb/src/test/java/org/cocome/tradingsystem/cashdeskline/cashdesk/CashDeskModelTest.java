//package org.cocome.tradingsystem.cashdeskline.cashdesk;
//
//import static org.junit.Assert.*;
//import static org.mockito.Mockito.*;
//
//import javax.enterprise.event.Event;
//import javax.enterprise.inject.spi.BeanManager;
//
//import org.cocome.tradingsystem.cashdeskline.datatypes.PaymentMode;
//import org.cocome.tradingsystem.cashdeskline.events.AccountSaleEvent;
//import org.cocome.tradingsystem.cashdeskline.events.CashAmountEnteredEvent;
//import org.cocome.tradingsystem.cashdeskline.events.ChangeAmountCalculatedEvent;
//import org.cocome.tradingsystem.cashdeskline.events.ExpressModeDisabledEvent;
//import org.cocome.tradingsystem.cashdeskline.events.ExpressModeEnabledEvent;
//import org.cocome.tradingsystem.cashdeskline.events.InsufficientCashAmountEvent;
//import org.cocome.tradingsystem.cashdeskline.events.InvalidCreditCardEvent;
//import org.cocome.tradingsystem.cashdeskline.events.InvalidProductBarcodeEvent;
//import org.cocome.tradingsystem.cashdeskline.events.PaymentModeRejectedEvent;
//import org.cocome.tradingsystem.cashdeskline.events.PaymentModeSelectedEvent;
//import org.cocome.tradingsystem.cashdeskline.events.RunningTotalChangedEvent;
//import org.cocome.tradingsystem.cashdeskline.events.SaleFinishedEvent;
//import org.cocome.tradingsystem.cashdeskline.events.SaleRegisteredEvent;
//import org.cocome.tradingsystem.cashdeskline.events.SaleStartedEvent;
//import org.cocome.tradingsystem.cashdeskline.events.SaleSuccessEvent;
//import org.cocome.tradingsystem.external.DebitResult;
//import org.cocome.tradingsystem.external.IBankLocal;
//import org.cocome.tradingsystem.external.TransactionID;
//import org.cocome.tradingsystem.inventory.application.store.IStoreInventoryLocal;
//import org.cocome.tradingsystem.inventory.application.store.NoSuchProductException;
//import org.cocome.tradingsystem.inventory.application.store.ProductOutOfStockException;
//import org.cocome.tradingsystem.inventory.data.enterprise.IProduct;
//import org.cocome.tradingsystem.inventory.data.store.IStockItem;
//import org.cocome.tradingsystem.util.scope.CashDeskSessionScoped;
//import org.cocome.tradingsystem.util.scope.IContextRegistry;
//import org.cocome.tradingsystem.util.scope.RegistryKeys;
//import org.cocome.tradingsystem.util.scope.context.INamedSessionContext;
//import org.junit.Before;
//import org.junit.BeforeClass;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.mockito.ArgumentCaptor;
//import org.mockito.Mock;
//import org.mockito.runners.MockitoJUnitRunner;
//
//@RunWith(MockitoJUnitRunner.class)
//public class CashDeskModelTest {
//	@Mock
//	private static BeanManager manager;
//
//	// Cash desk related information is required here
//	@Mock
//	private static IContextRegistry registry;
//	
//	@Mock
//	private static IBankLocal remoteBank;
//
//	@Mock
//	private static IStoreInventoryLocal inventory;
//	
//	@Mock 
//	private Event<InvalidProductBarcodeEvent> invalidProductBarcodeEvents;
//	
//	@Mock 
//	private Event<RunningTotalChangedEvent> runningTotalChangedEvents;
//	
//	@Mock 
//	private Event<SaleStartedEvent> saleStartedEvents;
//	
//	@Mock 
//	private Event<SaleFinishedEvent> saleFinishedEvents;
//	
//	@Mock 
//	private Event<PaymentModeSelectedEvent> paymentMethodSelectedEvents;
//	
//	@Mock 
//	private Event<PaymentModeRejectedEvent> paymentModeRejectedEvents;
//	
//	@Mock 
//	private Event<ChangeAmountCalculatedEvent> changeAmountCalculatedEvents;
//	
//	@Mock 
//	private Event<InvalidCreditCardEvent> invalidCreditCardEvents;
//	
//	@Mock 
//	private Event<AccountSaleEvent> accountSaleEvents;
//	
//	@Mock 
//	private Event<SaleSuccessEvent> saleSuccessEvents;
//	
//	@Mock 
//	private Event<SaleRegisteredEvent> saleRegisteredEvents;
//	
//	@Mock 
//	private Event<ExpressModeEnabledEvent> expressModeEnabledEvents;
//	
//	@Mock 
//	private Event<ExpressModeDisabledEvent> expressModeDisabledEvents;
//	
//	@Mock 
//	private Event<InsufficientCashAmountEvent> insufficientCashAmountEvents;
//	
//	@Mock 
//	private Event<CashAmountEnteredEvent> cashAmountEnteredEvents;
//	
//	@Mock
//	private static INamedSessionContext sessionContext;
//	
//	private ArgumentCaptor<RunningTotalChangedEvent> runningTotalChangedCaptor;
//	private ArgumentCaptor<PaymentModeSelectedEvent> paymentModeSelectedCaptor;
//	private ArgumentCaptor<ChangeAmountCalculatedEvent> changeAmountCalculatedCaptor;
//	private ArgumentCaptor<CashAmountEnteredEvent> cashAmountEnteredCaptor;
//	private ArgumentCaptor<AccountSaleEvent> accountSaleEventCaptor;
//	private ArgumentCaptor<SaleRegisteredEvent> saleRegisteredEventCaptor;
//	
//	private static IProduct prod;
//	private static IStockItem stock;
//	private static TransactionID transaction;
//	
//	private static final long STORE_ID = 1;
//	private static final double CHANGE_AMOUNT = 5.0;
//	private static final String CASHDESK_NAME = "test";
//	private static final String CARD_INFO = "1234";
//	private static final int CARD_PIN = 7777;
//	private static final int TRANSACTION_ID = 1;
//	
//	private CashDeskModel cashDeskModel;
//	
//	@BeforeClass
//	public static void setUpClass() throws NoSuchProductException {
//		prod = new Product();
//		prod.setBarcode(1234);
//		prod.setId(1);
//		prod.setName("Banana");
//		prod.setPurchasePrice(10.0);
//		
//		stock = new StockItem();
//		stock.setAmount(10);
//		stock.setId(1);
//		stock.setIncomingAmount(0);
//		stock.setMaxStock(40);
//		stock.setMinStock(5);
//		stock.setProduct(prod);
//		stock.setSalesPrice(20.0);
//		
//		transaction = new TransactionID(TRANSACTION_ID);
//	}
//	
//	@Before
//	public void setUp() throws Exception {
//		setUpModel();
//		
//		when(manager.getContext(CashDeskSessionScoped.class)).thenReturn(sessionContext);
//		when(sessionContext.getName()).thenReturn(CASHDESK_NAME);
//		when(registry.getLong(RegistryKeys.STORE_ID)).thenReturn(STORE_ID);
//		
//		when(inventory.getProductWithStockItem(STORE_ID, prod.getBarcode())).thenReturn(FillTransferObjects.fillProductWithStockItemTO(stock));
//		
//		when(remoteBank.validateCard(CARD_INFO, CARD_PIN)).thenReturn(transaction);
//		when(remoteBank.debitCard(transaction)).thenReturn(DebitResult.OK);
//		
//		cashDeskModel.initCashDesk();
//	}
//
//	private void setUpModel() {
//		cashDeskModel = new CashDeskModel();
//		cashDeskModel.accountSaleEvents = accountSaleEvents;
//		cashDeskModel.cashAmountEnteredEvents = cashAmountEnteredEvents;
//		cashDeskModel.changeAmountCalculatedEvents = changeAmountCalculatedEvents;
//		cashDeskModel.expressModeDisabledEvents = expressModeDisabledEvents;
//		cashDeskModel.expressModeEnabledEvents = expressModeEnabledEvents;
//		cashDeskModel.insufficientCashAmountEvents = insufficientCashAmountEvents;
//		cashDeskModel.invalidCreditCardEvents = invalidCreditCardEvents;
//		cashDeskModel.invalidProductBarcodeEvents = invalidProductBarcodeEvents;
//		cashDeskModel.inventory = inventory;
//		cashDeskModel.manager = manager;
//		cashDeskModel.paymentMethodSelectedEvents = paymentMethodSelectedEvents;
//		cashDeskModel.paymentModeRejectedEvents = paymentModeRejectedEvents;
//		cashDeskModel.registry = registry;
//		cashDeskModel.remoteBank = remoteBank;
//		cashDeskModel.runningTotalChangedEvents = runningTotalChangedEvents;
//		cashDeskModel.saleFinishedEvents = saleFinishedEvents;
//		cashDeskModel.saleRegisteredEvents = saleRegisteredEvents;
//		cashDeskModel.saleStartedEvents = saleStartedEvents;
//		cashDeskModel.saleSuccessEvents = saleSuccessEvents;
//	}
//
//	@Test
//	public void testStartSale() throws IllegalCashDeskStateException {
//		cashDeskModel.state = CashDeskState.EXPECTING_SALE;
//		cashDeskModel.startSale();
//		
//		verify(saleStartedEvents).fire(any(SaleStartedEvent.class));
//		assertEquals(CashDeskState.EXPECTING_ITEMS, cashDeskModel.state);
//	}
//
//	@Test
//	public void testAddItemToSale() throws IllegalCashDeskStateException, ProductOutOfStockException, NoSuchProductException {
//		runningTotalChangedCaptor = ArgumentCaptor.forClass(RunningTotalChangedEvent.class);
//		
//		cashDeskModel.state = CashDeskState.EXPECTING_ITEMS;
//		
//		cashDeskModel.addItemToSale(prod.getBarcode());
//		
//		verify(inventory).getProductWithStockItem(STORE_ID, prod.getBarcode());
//		verify(runningTotalChangedEvents).fire(runningTotalChangedCaptor.capture());
//		
//		assertEquals(prod.getName(), runningTotalChangedCaptor.getValue().getProductName());
//		assertEquals(stock.getSalesPrice(), runningTotalChangedCaptor.getValue().getProductPrice(), 0.1);
//		assertEquals(stock.getSalesPrice(), runningTotalChangedCaptor.getValue().getRunningTotal(), 0.1);
//		assertEquals(CashDeskState.EXPECTING_ITEMS, cashDeskModel.state);
//	}
//
//	@Test
//	public void testFinishSale() throws IllegalCashDeskStateException {
//		cashDeskModel.state = CashDeskState.EXPECTING_ITEMS;
//		cashDeskModel.saleProducts.add(FillTransferObjects.fillProductWithStockItemTO(stock));
//		
//		cashDeskModel.finishSale();
//		
//		verify(saleFinishedEvents).fire(any(SaleFinishedEvent.class));
//		
//		assertEquals(CashDeskState.EXPECTING_PAYMENT, cashDeskModel.state);
//	}
//
//	@Test
//	public void testSelectCashPaymentMode() throws IllegalCashDeskStateException {
//		paymentModeSelectedCaptor = ArgumentCaptor.forClass(PaymentModeSelectedEvent.class);
//		cashDeskModel.state = CashDeskState.EXPECTING_PAYMENT;
//		
//		cashDeskModel.selectPaymentMode(PaymentMode.CASH);
//		
//		verify(paymentMethodSelectedEvents).fire(paymentModeSelectedCaptor.capture());
//		
//		assertEquals(PaymentMode.CASH, paymentModeSelectedCaptor.getValue().getMode());
//		assertEquals(CashDeskState.PAYING_BY_CASH, cashDeskModel.state);
//	}
//	
//	@Test
//	public void testSelectCardPaymentMode() throws IllegalCashDeskStateException {
//		paymentModeSelectedCaptor = ArgumentCaptor.forClass(PaymentModeSelectedEvent.class);
//		cashDeskModel.state = CashDeskState.EXPECTING_PAYMENT;
//		
//		cashDeskModel.selectPaymentMode(PaymentMode.CREDIT_CARD);
//		
//		verify(paymentMethodSelectedEvents).fire(paymentModeSelectedCaptor.capture());
//		
//		assertEquals(PaymentMode.CREDIT_CARD, paymentModeSelectedCaptor.getValue().getMode());
//		assertEquals(CashDeskState.EXPECTING_CARD_INFO, cashDeskModel.state);
//	}
//
//	@Test
//	public void testStartCashPayment() throws IllegalCashDeskStateException {
//		changeAmountCalculatedCaptor = ArgumentCaptor.forClass(ChangeAmountCalculatedEvent.class);
//		cashAmountEnteredCaptor = ArgumentCaptor.forClass(CashAmountEnteredEvent.class);
//		cashDeskModel.state = CashDeskState.PAYING_BY_CASH;
//		
//		cashDeskModel.startCashPayment(CHANGE_AMOUNT);
//		
//		verify(changeAmountCalculatedEvents).fire(changeAmountCalculatedCaptor.capture());
//		verify(cashAmountEnteredEvents).fire(cashAmountEnteredCaptor.capture());
//		
//		assertEquals(CHANGE_AMOUNT, changeAmountCalculatedCaptor.getValue().getChangeAmount(), 0.1);
//		assertEquals(CHANGE_AMOUNT, cashAmountEnteredCaptor.getValue().getCashAmount(), 0.1);
//		assertEquals(CashDeskState.PAID_BY_CASH, cashDeskModel.state);
//	}
//
//	@Test
//	public void testFinishCashPayment() throws IllegalCashDeskStateException {
//		accountSaleEventCaptor = ArgumentCaptor.forClass(AccountSaleEvent.class);
//		saleRegisteredEventCaptor = ArgumentCaptor.forClass(SaleRegisteredEvent.class);
//		cashDeskModel.state = CashDeskState.PAID_BY_CASH;
//		cashDeskModel.saleProducts.add(FillTransferObjects.fillProductWithStockItemTO(stock));
//		
//		cashDeskModel.finishCashPayment();
//		
//		verify(accountSaleEvents).fire(accountSaleEventCaptor.capture());
//		verify(saleSuccessEvents).fire(any(SaleSuccessEvent.class));
//		verify(saleRegisteredEvents).fire(saleRegisteredEventCaptor.capture());
//		
//		assertEquals(1, accountSaleEventCaptor.getValue().getSale().getProductTOs().size());
//		assertEquals(prod.getBarcode(), accountSaleEventCaptor.getValue().getSale().getProductTOs().get(0).getBarcode());
//		
//		assertEquals(1, saleRegisteredEventCaptor.getValue().getItemCount());
//		assertEquals(PaymentMode.CASH, saleRegisteredEventCaptor.getValue().getPaymentMode());
//		assertEquals(CASHDESK_NAME, saleRegisteredEventCaptor.getValue().getCashDesk());
//		
//		assertEquals(CashDeskState.EXPECTING_SALE, cashDeskModel.state);
//	}
//
//	@Test
//	public void testStartCreditCardPayment() throws IllegalCashDeskStateException {
//		cashDeskModel.state = CashDeskState.EXPECTING_CARD_INFO;
//		cashDeskModel.saleProducts.add(FillTransferObjects.fillProductWithStockItemTO(stock));
//		
//		cashDeskModel.startCreditCardPayment(CARD_INFO);
//		
//		assertEquals(CashDeskState.PAYING_BY_CREDIT_CARD, cashDeskModel.state);
//	}
//
//	@Test
//	public void testFinishCreditCardPayment() throws IllegalCashDeskStateException {
//		accountSaleEventCaptor = ArgumentCaptor.forClass(AccountSaleEvent.class);
//		saleRegisteredEventCaptor = ArgumentCaptor.forClass(SaleRegisteredEvent.class);
//		cashDeskModel.state = CashDeskState.PAYING_BY_CREDIT_CARD;
//		cashDeskModel.saleProducts.add(FillTransferObjects.fillProductWithStockItemTO(stock));
//		cashDeskModel.cardInfo = CARD_INFO;
//		
//		cashDeskModel.finishCreditCardPayment(CARD_PIN);
//		
//		verify(accountSaleEvents).fire(accountSaleEventCaptor.capture());
//		verify(saleSuccessEvents).fire(any(SaleSuccessEvent.class));
//		verify(saleRegisteredEvents).fire(saleRegisteredEventCaptor.capture());
//		
//		verify(remoteBank).validateCard(CARD_INFO, CARD_PIN);
//		verify(remoteBank).debitCard(transaction);
//		
//		assertEquals(1, accountSaleEventCaptor.getValue().getSale().getProductTOs().size());
//		assertEquals(prod.getBarcode(), accountSaleEventCaptor.getValue().getSale().getProductTOs().get(0).getBarcode());
//		
//		assertEquals(1, saleRegisteredEventCaptor.getValue().getItemCount());
//		assertEquals(PaymentMode.CREDIT_CARD, saleRegisteredEventCaptor.getValue().getPaymentMode());
//		assertEquals(CASHDESK_NAME, saleRegisteredEventCaptor.getValue().getCashDesk());
//		
//		assertEquals(CashDeskState.EXPECTING_SALE, cashDeskModel.state);
//	}
//
//	@Test
//	public void testEnableExpressMode() {
//		cashDeskModel.expressModeEnabled = false;
//		cashDeskModel.enableExpressMode();
//		
//		verify(expressModeEnabledEvents).fire(any(ExpressModeEnabledEvent.class));
//		assertEquals(true, cashDeskModel.isInExpressMode());
//	}
//
//	@Test
//	public void testDisableExpressMode() {
//		cashDeskModel.expressModeEnabled = true;
//		cashDeskModel.disableExpressMode();
//		
//		verify(expressModeDisabledEvents).fire(any(ExpressModeDisabledEvent.class));
//		assertEquals(false, cashDeskModel.isInExpressMode());
//	}
//
//}
