package org.cocome.tradingsystem.inventory.data.usermanager;

import org.cocome.tradingsystem.inventory.data.IIdentifiable;
import org.cocome.tradingsystem.inventory.data.store.IStore;

import java.util.Set;

/**
 * Interface for customers of the pickup shop.
 *
 * @author Tobias Pöppke
 * @author Robert Heinrich
 */
public interface ICustomer extends IIdentifiable {

    String getMailAddress();

    void setMailAddress(String mailAddress);

    IStore getPreferredStore();

    void setPreferredStore(IStore preferredStore);

    String getLastName();

    void setLastName(String lastName);

    String getFirstName();

    void setFirstName(String firstName);

    Set<String> getCreditCardInfo();

    void setCreditCardInfo(Set<String> creditCardInfo);

    void addCreditCardInfo(String creditCardInfo);

    void removeCreditCardInfo(String creditCardInfo);

    void setUser(IUser user);

    IUser getUser();
}