package org.cocome.cloud.web.data.logindata;

import javax.ejb.Local;

@Local
public interface IAuthenticator {

    /**
     * @param user
     * @return
     */
    boolean checkCredentials(IUser user);

    /**
     * @param user
     * @param permission
     * @return
     */
    boolean checkHasPermission(IUser user, IPermission permission);

    /**
     * Checks the given user credentials and returns the user as a result if the
     * credentials matched. Otherwise null is returned.
     *
     * @param username
     * @param credential
     * @return the user object or null if check failed
     */
    IUser checkCredential(String username, ICredential credential);

}
