package org.cocome.tradingsystem.cashdeskline.cashdesk;

import javax.ejb.Local;

import org.cocome.tradingsystem.cashdeskline.datatypes.PaymentMode;
import org.cocome.tradingsystem.inventory.application.store.ProductOutOfStockException;

/**
 * Defines the actions available on the cash desk model that can 
 * be triggered from outside.
 * 
 * @author Tobias Pöppke
 * @author Robert Heinrich
 *
 */
@Local
public interface ICashDeskModelLocal {
	
	/**
	 * Sets the name on this cash desk.
	 * 
	 * @param cashDeskName
	 * 		the new name
	 */
	public void setName(String cashDeskName);

	/**
	 * 
	 * @return the name of this cash desk
	 */
	public String getName();

	/**
	 * 
	 * @return the express mode status of this cash desk.
	 */
	public boolean isInExpressMode();

	/**
	 * Signals the start of a new sale to the cash desk and triggers 
	 * all necessary actions. New sale can be started in nearly all states.
	 * 
	 * @throws IllegalCashDeskStateException if it is not possible to 
	 * 		start a sale in the current cash desk state
	 */
	public void startSale() throws IllegalCashDeskStateException;

	/**
	 * Adds an item with the specified barcode into the running sale and updates
	 * the running total. Does nothing if there is no product with the specified
	 * barcode, or the number of items in an express sale exceeds the allowed
	 * limit.
	 * 
	 * @param barcode
	 * 		the barcode of the item
	 * @throws ProductOutOfStockException 
	 */
	public void addItemToSale(edu.kit.ipd.sdq.evaluation.Barcode barcode) throws IllegalCashDeskStateException, ProductOutOfStockException;

	/**
	 * Finishes the current sale and sets the cash desk up to receive payment.
	 * 
	 * @throws IllegalCashDeskStateException if the current cash desk state 
	 * 		does not permit this action
	 */
	public void finishSale() throws IllegalCashDeskStateException;

	/**
	 * Selects the payment mode for the current sale. 
	 * 
	 * @param mode
	 * 		the selected payment mode
	 * 
	 * @throws IllegalCashDeskStateException if the current cash desk state 
	 * 		does not permit this action
	 */
	public void selectPaymentMode(PaymentMode mode) throws IllegalCashDeskStateException;

	/**
	 * Starts the cash payment process with the received amount of money.
	 * 
	 * @param amount
	 * 		the amount of money received
	 * 
	 * @throws IllegalCashDeskStateException if the current cash desk state 
	 * 		does not permit this action 
	 */
	public void startCashPayment(double amount) throws IllegalCashDeskStateException;

	/**
	 * Finishes the cash payment and takes all necessary actions to 
	 * complete the sale.
	 * 
	 * @throws IllegalCashDeskStateException if the current cash desk state 
	 * 		does not permit this action
	 */
	public void finishCashPayment() throws IllegalCashDeskStateException;

	/**
	 * Starts the credit card payment process with the entered credit card info.
	 * 
	 * @param cardInfo
	 * 		the given credit card info
	 * 
	 * @throws IllegalCashDeskStateException if the current cash desk state 
	 * 		does not permit this action
	 */
	public void startCreditCardPayment(String cardInfo) throws IllegalCashDeskStateException;

	/**
	 * Finishes the credit card payment and takes all necessary actions to 
	 * complete the sale. To complete the sale the credit card pin is needed
	 * and has to be valid.
	 * 
	 * @param pin
	 * 		the credit card pin
	 * 
	 * @throws IllegalCashDeskStateException if the current cash desk state 
	 * 		does not permit this action
	 */
	public void finishCreditCardPayment(int pin) throws IllegalCashDeskStateException;

	/**
	* Enables the express mode on this cash desk. The cash desk can be 
	* switched to express mode in all states.
	*/
	public void enableExpressMode();

	/**
	* Disables the express mode on this cash desk. The cash desk can be 
	* switched from express mode to normal mode in all states.
	*/
	public void disableExpressMode();
	
	/**
	 * 
	 * @return the current state of this CashDesk
	 */
	public CashDeskState getState();

}