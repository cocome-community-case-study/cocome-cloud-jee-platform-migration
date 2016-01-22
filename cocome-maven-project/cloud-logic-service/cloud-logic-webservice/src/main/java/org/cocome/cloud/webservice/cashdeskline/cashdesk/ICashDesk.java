package org.cocome.cloud.webservice.cashdeskline.cashdesk;

import java.util.Set;

import javax.jws.WebParam;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlElement;

import org.cocome.cloud.webservice.exception.UnhandledException;
import org.cocome.tradingsystem.cashdeskline.cashdesk.IllegalCashDeskStateException;
import org.cocome.tradingsystem.cashdeskline.cashdesk.cashbox.IllegalInputException;
import org.cocome.tradingsystem.inventory.application.store.NoSuchProductException;
import org.cocome.tradingsystem.inventory.application.store.ProductOutOfStockException;

/**
 * Interface to the cash desk component of a cash desk.
 * Every method returns a Set of models that have changed 
 * during the invocation. These models have to be queried 
 * separately to obtain the new contents. Some of the methods 
 * on the underlying cash desk model can also be called by events
 * fired from other cash desk components.
 * 
 * @author Tobias Pöppke
 *
 */
@WebService
public interface ICashDesk {

	/**
	 * Checks if this cash desk is in express mode.
	 * 
	 * @param cashDeskName 
	 * 			the name of the cash desk to check
	 * 
	 * @param storeID 
	 * 			the store ID of the store in which the cash desk is located
	 * 
	 * @return 
	 * 			{@code true} if cash desk is in express mode, {@code false} otherwise
	 * @throws Exception 
	 * 
	 * @throws BaseWSException
	 */
	public boolean isInExpressMode(@XmlElement(required = true) @WebParam(name = "cashDeskName") String cashDeskName, 
			@XmlElement(required = true) @WebParam(name = "storeID") long storeID) throws UnhandledException;

	/**
	 * Starts a new sale process. After starting a new sale, 
	 * the cash desk is awaiting products.
	 * 
	 * @throws BaseWSException 
	 * 
	 * @return 
	 * 		Set of model classes with changed content.
	 * @throws UnhandledException 
	 * @throws IllegalCashDeskStateException 
	 * @throws Exception 
	 */
	public Set<Class<?>> startSale(@XmlElement(required = true) @WebParam(name = "cashDeskName") String cashDeskName, 
			@XmlElement(required = true) @WebParam(name = "storeID") long storeID) throws IllegalCashDeskStateException, UnhandledException;

	/**
	 * Finishes the current sale. That means this cash desk is no longer 
	 * awaiting products to be scanned. After this the cash desk awaits 
	 * the selection of the payment mode. This is only valid if {@code startSale}
	 * was called before or a triggering event was fired for it.
	 * 
	 * @return 
	 * 		Set of model classes with changed content.
	 * @throws UnhandledException 
	 * @throws ProductOutOfStockException 
	 * @throws IllegalCashDeskStateException 
	 * @throws Exception 
	 * 
	 * @throws BaseWSException 
	 * 
	 */
	public Set<Class<?>> finishSale(@XmlElement(required = true) @WebParam(name = "cashDeskName") String cashDeskName,
			@XmlElement(required = true) @WebParam(name = "storeID") long storeID) throws IllegalCashDeskStateException, ProductOutOfStockException, UnhandledException;

	/**
	 * Selects the payment mode for this cash desk. This method is 
	 * only valid to call if the {@code finishSale} method was called 
	 * before or a triggering event was fired for it.
 	 * 
	 * @param cashDeskName
	 * 		the name of the cash desk to select payment for
	 * 
	 * @param storeID
	 * 		the storeID of the cash desk's store
	 * 
	 * @param mode
	 * 		the payment mode as specified in {@code PaymentMode}
	 * 
	 * @return
	 * 		Set of model classes with changed content.
	 * @throws UnhandledException 
	 * @throws IllegalCashDeskStateException 
	 * @throws IllegalInputException 
	 * 
	 * @throws BaseWSException
	 */
	public Set<Class<?>> selectPaymentMode(@XmlElement(required = true) @WebParam(name = "cashDeskName") String cashDeskName,
			@XmlElement(required = true) @WebParam(name = "storeID") long storeID,
			@XmlElement(required = true) @WebParam(name = "mode") String mode) throws IllegalCashDeskStateException, UnhandledException, IllegalInputException;

	/**
	 * Starts the cash payment procedure. This is only a valid action if 
	 * the cash payment option was selected before with {@code selectPaymentMode} 
	 * or a triggering event was fired for it.
	 * 
	 * @param amount
	 * 		the cash amount payed by the customer
	 * 
	 * @return 
	 * 		Set of model classes with changed content.
	 * @throws UnhandledException 
	 * @throws IllegalCashDeskStateException 
	 * 
	 * @throws BaseWSException 
	 */
	public Set<Class<?>> startCashPayment(@XmlElement(required = true) @WebParam(name = "cashDeskName") String cashDeskName, 
			@XmlElement(required = true) @WebParam(name = "storeID") long storeID, 
			@XmlElement(required = true) @WebParam(name = "amount") final double amount) throws IllegalCashDeskStateException, UnhandledException;

	/**
	 * Finishes the cash payment process. This is only valid if the cash payment
	 * was started before or a triggering event was fired for it.
	 * 
	 * @param cashDeskName
	 * 			
	 * @param storeID
	 * @return
	 * @throws UnhandledException 
	 * @throws ProductOutOfStockException 
	 * @throws IllegalCashDeskStateException 
	 * @throws BaseWSException
	 */
	public Set<Class<?>> finishCashPayment(@XmlElement(required = true) @WebParam(name = "cashDeskName") String cashDeskName, 
			@XmlElement(required = true) @WebParam(name = "storeID") long storeID) throws IllegalCashDeskStateException, ProductOutOfStockException, UnhandledException;

	/**
	 * 
	 * @param cardInfo
	 * @return 
	 * @throws UnhandledException 
	 * @throws IllegalCashDeskStateException 
	 * @throws BaseWSException 
	 */
	public Set<Class<?>> startCreditCardPayment(@XmlElement(required = true) @WebParam(name = "cashDeskName") String cashDeskName, 
			@XmlElement(required = true) @WebParam(name = "storeID") long storeID,
			@XmlElement(required = true) @WebParam(name = "cardInfo") String cardInfo) throws IllegalCashDeskStateException, UnhandledException;

	/**
	 * 
	 * @param pin
	 * @return 
	 * @throws UnhandledException 
	 * @throws IllegalCashDeskStateException 
	 * @throws BaseWSException 
	 */
	public Set<Class<?>> finishCreditCardPayment(@XmlElement(required = true) @WebParam(name = "cashDeskName") String cashDeskName,
			@XmlElement(required = true) @WebParam(name = "storeID") long storeID,
			@XmlElement(required = true) @WebParam(name = "pin") final int pin) throws IllegalCashDeskStateException, UnhandledException;

	//
	// Cash desk can be switched to express mode in all states.
	//
	public Set<Class<?>> enableExpressMode(@XmlElement(required = true) @WebParam(name = "cashDeskName") String cashDeskName, 
			@XmlElement(required = true) @WebParam(name = "storeID") long storeID) throws UnhandledException;

	//
	// The express mode can be disabled in all states.
	//
	public Set<Class<?>> disableExpressMode(@XmlElement(required = true) @WebParam(name = "cashDeskName") String cashDeskName,
			@XmlElement(required = true) @WebParam(name = "storeID") long storeID) throws UnhandledException;

	
	/**
	 * 
	 * Adds an item with the specified barcode into the running sale and updates
	 * the running total. Does nothing if there is no product with the specified
	 * barcode, or the number of items in an express sale exceeds the allowed
	 * limit.
	 *
	 * 
	 * @param cashDeskName
	 * @param storeID
	 * @param barcode
	 * @return
	 * @throws UnhandledException 
	 * @throws NoSuchProductException 
	 * @throws IllegalCashDeskStateException 
	 * @throws ProductOutOfStockException 
	 * @throws BaseWSException
	 */
	public Set<Class<?>> addItemToSale(@XmlElement(required = true) @WebParam(name = "cashDeskName") String cashDeskName,
			@XmlElement(required = true) @WebParam(name = "storeID") long storeID,
			@XmlElement(required = true) @WebParam(name = "barcode") long barcode) throws IllegalCashDeskStateException, NoSuchProductException, UnhandledException, ProductOutOfStockException;	
}
