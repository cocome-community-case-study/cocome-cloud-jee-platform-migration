package org.cocome.tradingsystem.inventory.data.usermanager;

import org.cocome.tradingsystem.inventory.application.usermanager.CredentialType;
import org.cocome.tradingsystem.inventory.application.usermanager.CredentialTypeNotFoundException;
import org.cocome.tradingsystem.inventory.application.usermanager.Role;
import org.cocome.tradingsystem.inventory.application.usermanager.UserTO;
import org.cocome.tradingsystem.inventory.application.usermanager.credentials.ICredential;

import java.util.Map;
import java.util.Set;

/**
 * Interface for users of the system. This interface is mainly
 * used for authentication purposes.
 *
 * @author Tobias Pöppke
 * @author Robert Heinrich
 */
public interface IUser {
    void initUser(UserTO user);

    String getUsername();

    void setCredentials(Map<CredentialType, ICredential> credentials);

    Map<CredentialType, ICredential> getCredentials();

    ICredential getCredential(CredentialType credentialType);

    void addCredential(ICredential credential);

    void removeCredential(CredentialType credentialType);

    /**
     * Resets the credential of the given type to a new value.
     * This can be used to handle password reset requests when
     * called with the {@code CredentialType.PLAIN_PASSWORD} or
     * {@code CredentialType.HASHED_PASSWORD}.
     *
     * @param credentialType
     * @return the new credential string, e.g. the new password
     * @throws CredentialTypeNotFoundException if no credential
     *                                         with this type is registered for this user
     */
    String resetCredential(CredentialType credentialType) throws CredentialTypeNotFoundException;

    boolean checkCredential(ICredential credential);

    boolean checkUserCredentials(IUser userToCheck);

    boolean hasRole(Role role);

    void addRole(Role role);

    Set<Role> getRoles();

    void setRoles(Set<Role> roles);

    void withdrawRole(Role role);

    void setUsername(String username);
}
