package org.cocome.tradingsystem.cashdeskline.cashdesk;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import javax.enterprise.event.Event;
import javax.enterprise.inject.spi.BeanManager;

import org.cocome.tradingsystem.cashdeskline.datatypes.PaymentMode;
import org.cocome.tradingsystem.cashdeskline.events.*;
import org.cocome.tradingsystem.external.DebitResult;
import org.cocome.tradingsystem.external.IBankLocal;
import org.cocome.tradingsystem.external.TransactionID;
import org.cocome.tradingsystem.inventory.application.enterprise.CustomProductTO;
import org.cocome.tradingsystem.inventory.application.store.*;
import org.cocome.tradingsystem.inventory.data.enterprise.*;
import org.cocome.tradingsystem.inventory.data.store.*;
import org.cocome.tradingsystem.util.scope.CashDeskSessionScoped;
import org.cocome.tradingsystem.util.scope.IContextRegistry;
import org.cocome.tradingsystem.util.scope.RegistryKeys;
import org.cocome.tradingsystem.util.scope.context.INamedSessionContext;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class CashDeskModelTest {
    @Mock
    private static BeanManager manager;

    // Cash desk related information is required here
    @Mock
    private static IContextRegistry registry;

    @Mock
    private static IBankLocal remoteBank;

    @Mock
    private static IStoreInventoryLocal inventory;

    @Mock
    private Event<InvalidProductBarcodeEvent> invalidProductBarcodeEvents;

    @Mock
    private Event<RunningTotalChangedEvent> runningTotalChangedEvents;

    @Mock
    private Event<SaleStartedEvent> saleStartedEvents;

    @Mock
    private Event<SaleFinishedEvent> saleFinishedEvents;

    @Mock
    private Event<PaymentModeSelectedEvent> paymentMethodSelectedEvents;

    @Mock
    private Event<PaymentModeRejectedEvent> paymentModeRejectedEvents;

    @Mock
    private Event<ChangeAmountCalculatedEvent> changeAmountCalculatedEvents;

    @Mock
    private Event<InvalidCreditCardEvent> invalidCreditCardEvents;

    @Mock
    private Event<AccountSaleEvent> accountSaleEvents;

    @Mock
    private Event<SaleSuccessEvent> saleSuccessEvents;

    @Mock
    private Event<SaleRegisteredEvent> saleRegisteredEvents;

    @Mock
    private Event<ExpressModeEnabledEvent> expressModeEnabledEvents;

    @Mock
    private Event<ExpressModeDisabledEvent> expressModeDisabledEvents;

    @Mock
    private Event<InsufficientCashAmountEvent> insufficientCashAmountEvents;

    @Mock
    private Event<CashAmountEnteredEvent> cashAmountEnteredEvents;

    @Mock
    private Event<CustomProductEnteredEvent> customProductEnteredEvents;

    @Mock
    private static INamedSessionContext sessionContext;

    private static IProduct prod;
    private static ICustomProduct customProd;
    private static IStockItem stock;
    private static IOnDemandItem onDemandItem;
    private static TransactionID transaction;

    private static final long STORE_ID = 1;
    private static final double CHANGE_AMOUNT = 5.0;
    private static final String CASHDESK_NAME = "test";
    private static final String CARD_INFO = "1234";
    private static final int CARD_PIN = 7777;
    private static final int TRANSACTION_ID = 1;

    private CashDeskModel cashDeskModel;

    @BeforeClass
    public static void setUpClass() {
        prod = new Product();
        prod.setBarcode(1234);
        prod.setId(1);
        prod.setName("Banana");
        prod.setPurchasePrice(10.0);

        stock = new StockItem();
        stock.setAmount(10);
        stock.setId(1);
        stock.setIncomingAmount(0);
        stock.setMaxStock(40);
        stock.setMinStock(5);
        stock.setProduct(prod);
        stock.setSalesPrice(20.0);

        customProd = new CustomProduct();
        customProd.setBarcode(4567);
        customProd.setId(1);
        customProd.setName("Custom Banana");
        customProd.setPurchasePrice(12.0);

        onDemandItem = new OnDemandItem();
        onDemandItem.setId(1);
        onDemandItem.setProduct(customProd);
        onDemandItem.setStoreId(22);

        transaction = new TransactionID(TRANSACTION_ID);
    }

    @Before
    public void setUp() throws NoSuchProductException {
        setUpModel();

        when(manager.getContext(CashDeskSessionScoped.class)).thenReturn(sessionContext);
        when(sessionContext.getName()).thenReturn(CASHDESK_NAME);
        when(registry.getLong(RegistryKeys.STORE_ID)).thenReturn(STORE_ID);

        when(inventory.getProductWithStockItem(STORE_ID, prod.getBarcode()))
                .thenReturn(fillProductWithItemTO(stock));

        when(inventory.getProductWithStockItem(STORE_ID, customProd.getBarcode()))
                .thenReturn(fillProductWithItemTO(onDemandItem));

        when(remoteBank.validateCard(CARD_INFO, CARD_PIN)).thenReturn(transaction);
        when(remoteBank.debitCard(transaction)).thenReturn(DebitResult.OK);

        cashDeskModel.initCashDesk();
    }

    private void setUpModel() {
        cashDeskModel = new CashDeskModel();
        cashDeskModel.accountSaleEvents = accountSaleEvents;
        cashDeskModel.cashAmountEnteredEvents = cashAmountEnteredEvents;
        cashDeskModel.changeAmountCalculatedEvents = changeAmountCalculatedEvents;
        cashDeskModel.expressModeDisabledEvents = expressModeDisabledEvents;
        cashDeskModel.expressModeEnabledEvents = expressModeEnabledEvents;
        cashDeskModel.insufficientCashAmountEvents = insufficientCashAmountEvents;
        cashDeskModel.invalidCreditCardEvents = invalidCreditCardEvents;
        cashDeskModel.invalidProductBarcodeEvents = invalidProductBarcodeEvents;
        cashDeskModel.customProductEnteredEvents = customProductEnteredEvents;
        cashDeskModel.inventory = inventory;
        cashDeskModel.manager = manager;
        cashDeskModel.paymentMethodSelectedEvents = paymentMethodSelectedEvents;
        cashDeskModel.paymentModeRejectedEvents = paymentModeRejectedEvents;
        cashDeskModel.registry = registry;
        cashDeskModel.remoteBank = remoteBank;
        cashDeskModel.runningTotalChangedEvents = runningTotalChangedEvents;
        cashDeskModel.saleFinishedEvents = saleFinishedEvents;
        cashDeskModel.saleRegisteredEvents = saleRegisteredEvents;
        cashDeskModel.saleStartedEvents = saleStartedEvents;
        cashDeskModel.saleSuccessEvents = saleSuccessEvents;
        cashDeskModel.dataFactory = new EnterpriseDatatypesFactory() {
            @Override
            public ICustomProduct getNewCustomProduct() {
                return new CustomProduct();
            }
        };
    }

    @Test
    public void testStartSale() throws IllegalCashDeskStateException {
        cashDeskModel.state = CashDeskState.EXPECTING_SALE;
        cashDeskModel.startSale();

        verify(saleStartedEvents).fire(any(SaleStartedEvent.class));
        assertEquals(CashDeskState.EXPECTING_ITEMS, cashDeskModel.state);
    }

    @Test
    public void testAddItemToSale() throws IllegalCashDeskStateException, ProductOutOfStockException, NoSuchProductException {
        ArgumentCaptor<RunningTotalChangedEvent> runningTotalChangedCaptor = ArgumentCaptor.forClass(RunningTotalChangedEvent.class);

        cashDeskModel.state = CashDeskState.EXPECTING_ITEMS;

        cashDeskModel.addItemToSale(prod.getBarcode());

        verify(inventory).getProductWithStockItem(STORE_ID, prod.getBarcode());
        verify(runningTotalChangedEvents).fire(runningTotalChangedCaptor.capture());

        assertEquals(prod.getName(), runningTotalChangedCaptor.getValue().getProductName());
        assertEquals(stock.getSalesPrice(), runningTotalChangedCaptor.getValue().getProductPrice(), 0.1);
        assertEquals(CashDeskState.EXPECTING_ITEMS, cashDeskModel.state);
    }

    @Test
    public void testAddOnDemandItemToSale() throws IllegalCashDeskStateException, ProductOutOfStockException, NoSuchProductException {
        ArgumentCaptor<RunningTotalChangedEvent> runningTotalChangedCaptor = ArgumentCaptor.forClass(RunningTotalChangedEvent.class);
        ArgumentCaptor<CustomProductEnteredEvent> customProductEnteredEventArgumentCaptor = ArgumentCaptor.forClass(CustomProductEnteredEvent.class);

        cashDeskModel.state = CashDeskState.EXPECTING_ITEMS;

        cashDeskModel.addItemToSale(customProd.getBarcode());

        verify(inventory).getProductWithStockItem(STORE_ID, customProd.getBarcode());
        verify(runningTotalChangedEvents).fire(runningTotalChangedCaptor.capture());
        verify(customProductEnteredEvents).fire(customProductEnteredEventArgumentCaptor.capture());

        assertEquals(customProd.getName(), runningTotalChangedCaptor.getValue().getProductName());
        assertEquals(onDemandItem.getSalesPrice(), runningTotalChangedCaptor.getValue().getProductPrice(), 0.1);
        assertEquals(CashDeskState.EXPECTING_PARAMETER_VALUES, cashDeskModel.state);
    }

    @Test
    public void testFinishSale() throws IllegalCashDeskStateException {
        cashDeskModel.state = CashDeskState.EXPECTING_ITEMS;
        cashDeskModel.saleProducts.add(new SaleEntryTO(fillProductWithItemTO(stock)));

        cashDeskModel.finishSale();

        verify(saleFinishedEvents).fire(any(SaleFinishedEvent.class));

        assertEquals(CashDeskState.EXPECTING_PAYMENT, cashDeskModel.state);
    }

    @Test
    public void testSelectCashPaymentMode() throws IllegalCashDeskStateException {
        ArgumentCaptor<PaymentModeSelectedEvent> paymentModeSelectedCaptor = ArgumentCaptor.forClass(PaymentModeSelectedEvent.class);
        cashDeskModel.state = CashDeskState.EXPECTING_PAYMENT;

        cashDeskModel.selectPaymentMode(PaymentMode.CASH);

        verify(paymentMethodSelectedEvents).fire(paymentModeSelectedCaptor.capture());

        assertEquals(PaymentMode.CASH, paymentModeSelectedCaptor.getValue().getMode());
        assertEquals(CashDeskState.PAYING_BY_CASH, cashDeskModel.state);
    }

    @Test
    public void testSelectCardPaymentMode() throws IllegalCashDeskStateException {
        ArgumentCaptor<PaymentModeSelectedEvent> paymentModeSelectedCaptor = ArgumentCaptor.forClass(PaymentModeSelectedEvent.class);
        cashDeskModel.state = CashDeskState.EXPECTING_PAYMENT;

        cashDeskModel.selectPaymentMode(PaymentMode.CREDIT_CARD);

        verify(paymentMethodSelectedEvents).fire(paymentModeSelectedCaptor.capture());

        assertEquals(PaymentMode.CREDIT_CARD, paymentModeSelectedCaptor.getValue().getMode());
        assertEquals(CashDeskState.EXPECTING_CARD_INFO, cashDeskModel.state);
    }

    @Test
    public void testStartCashPayment() throws IllegalCashDeskStateException {
        ArgumentCaptor<ChangeAmountCalculatedEvent> changeAmountCalculatedCaptor = ArgumentCaptor.forClass(ChangeAmountCalculatedEvent.class);
        ArgumentCaptor<CashAmountEnteredEvent> cashAmountEnteredCaptor = ArgumentCaptor.forClass(CashAmountEnteredEvent.class);
        cashDeskModel.state = CashDeskState.PAYING_BY_CASH;

        cashDeskModel.startCashPayment(CHANGE_AMOUNT);

        verify(changeAmountCalculatedEvents).fire(changeAmountCalculatedCaptor.capture());
        verify(cashAmountEnteredEvents).fire(cashAmountEnteredCaptor.capture());

        assertEquals(CHANGE_AMOUNT, changeAmountCalculatedCaptor.getValue().getChangeAmount(), 0.1);
        assertEquals(CHANGE_AMOUNT, cashAmountEnteredCaptor.getValue().getCashAmount(), 0.1);
        assertEquals(CashDeskState.PAID_BY_CASH, cashDeskModel.state);
    }

    @Test
    public void testFinishCashPayment() throws IllegalCashDeskStateException {
        ArgumentCaptor<AccountSaleEvent> accountSaleEventCaptor = ArgumentCaptor.forClass(AccountSaleEvent.class);
        ArgumentCaptor<SaleRegisteredEvent> saleRegisteredEventCaptor = ArgumentCaptor.forClass(SaleRegisteredEvent.class);
        cashDeskModel.state = CashDeskState.PAID_BY_CASH;
        cashDeskModel.saleProducts.add(new SaleEntryTO(fillProductWithItemTO(stock)));

        cashDeskModel.finishCashPayment();

        verify(accountSaleEvents).fire(accountSaleEventCaptor.capture());
        verify(saleSuccessEvents).fire(any(SaleSuccessEvent.class));
        verify(saleRegisteredEvents).fire(saleRegisteredEventCaptor.capture());

        assertEquals(1, accountSaleEventCaptor.getValue().getSale().getEntries().size());
        assertEquals(prod.getBarcode(), accountSaleEventCaptor.getValue().getSale().getEntries().get(0).getItemInfo().getProduct().getBarcode());

        assertEquals(1, saleRegisteredEventCaptor.getValue().getItemCount());
        assertEquals(PaymentMode.CASH, saleRegisteredEventCaptor.getValue().getPaymentMode());
        assertEquals(CASHDESK_NAME, saleRegisteredEventCaptor.getValue().getCashDesk());

        assertEquals(CashDeskState.EXPECTING_SALE, cashDeskModel.state);
    }

    @Test
    public void testStartCreditCardPayment() throws IllegalCashDeskStateException {
        cashDeskModel.state = CashDeskState.EXPECTING_CARD_INFO;
        cashDeskModel.saleProducts.add(new SaleEntryTO(fillProductWithItemTO(stock)));

        cashDeskModel.startCreditCardPayment(CARD_INFO);

        assertEquals(CashDeskState.PAYING_BY_CREDIT_CARD, cashDeskModel.state);
    }

    @Test
    public void testFinishCreditCardPayment() throws IllegalCashDeskStateException {
        ArgumentCaptor<AccountSaleEvent> accountSaleEventCaptor = ArgumentCaptor.forClass(AccountSaleEvent.class);
        ArgumentCaptor<SaleRegisteredEvent> saleRegisteredEventCaptor = ArgumentCaptor.forClass(SaleRegisteredEvent.class);
        cashDeskModel.state = CashDeskState.PAYING_BY_CREDIT_CARD;
        cashDeskModel.saleProducts.add(new SaleEntryTO(fillProductWithItemTO(stock)));
        cashDeskModel.cardInfo = CARD_INFO;

        cashDeskModel.finishCreditCardPayment(CARD_PIN);

        verify(accountSaleEvents).fire(accountSaleEventCaptor.capture());
        verify(saleSuccessEvents).fire(any(SaleSuccessEvent.class));
        verify(saleRegisteredEvents).fire(saleRegisteredEventCaptor.capture());

        verify(remoteBank).validateCard(CARD_INFO, CARD_PIN);
        verify(remoteBank).debitCard(transaction);

        assertEquals(1, accountSaleEventCaptor.getValue().getSale().getEntries().size());
        assertEquals(prod.getBarcode(), accountSaleEventCaptor.getValue().getSale().getEntries().get(0).getItemInfo().getProduct().getBarcode());

        assertEquals(1, saleRegisteredEventCaptor.getValue().getItemCount());
        assertEquals(PaymentMode.CREDIT_CARD, saleRegisteredEventCaptor.getValue().getPaymentMode());
        assertEquals(CASHDESK_NAME, saleRegisteredEventCaptor.getValue().getCashDesk());

        assertEquals(CashDeskState.EXPECTING_SALE, cashDeskModel.state);
    }

    @Test
    public void testEnableExpressMode() {
        cashDeskModel.expressModeEnabled = false;
        cashDeskModel.enableExpressMode();

        verify(expressModeEnabledEvents).fire(any(ExpressModeEnabledEvent.class));
        assertEquals(true, cashDeskModel.isInExpressMode());
    }

    @Test
    public void testDisableExpressMode() {
        cashDeskModel.expressModeEnabled = true;
        cashDeskModel.disableExpressMode();

        verify(expressModeDisabledEvents).fire(any(ExpressModeDisabledEvent.class));
        assertEquals(false, cashDeskModel.isInExpressMode());
    }

    private ProductWithItemTO fillProductWithItemTO(IItem item) {
        final ProductWithItemTO result = new ProductWithItemTO();
        final IProduct product = item.getProduct();

        result.setProduct(fillProductTO(product));
        result.setItem(fillItemTO(item));

        return result;
    }

    private ItemTO fillItemTO(IItem item) {
        if (item instanceof IStockItem) {
            return this.fillStockItemTO((IStockItem) item);
        } else if (item instanceof IOnDemandItem) {
            return this.fillOnDemandItemTO((IOnDemandItem) item);
        } else {
            throw new UnsupportedOperationException("Unknown item type: " + item.getClass().getName());
        }
    }

    private ProductTO getProductTOInstance(IProduct product) {
        if (product instanceof ICustomProduct) {
            return new CustomProductTO();
        }
        return new ProductTO();
    }

    private ProductTO fillProductTO(IProduct product) {
        ProductTO productTO = getProductTOInstance(product);
        productTO.setBarcode(product.getBarcode());
        productTO.setId(product.getId());
        productTO.setName(product.getName());
        productTO.setPurchasePrice(product.getPurchasePrice());
        return productTO;
    }

    private StockItemTO fillStockItemTO(IStockItem stockItem) {
        final StockItemTO result = new StockItemTO();
        result.setId(stockItem.getId());
        result.setAmount(stockItem.getAmount());
        result.setMinStock(stockItem.getMinStock());
        result.setMaxStock(stockItem.getMaxStock());
        result.setSalesPrice(stockItem.getSalesPrice());
        result.setIncomingAmount(stockItem.getIncomingAmount());

        return result;
    }

    private OnDemandItemTO fillOnDemandItemTO(IOnDemandItem onDemandItem) {
        final OnDemandItemTO result = new OnDemandItemTO();
        result.setId(onDemandItem.getId());
        result.setSalesPrice(onDemandItem.getSalesPrice());
        return result;
    }

}
