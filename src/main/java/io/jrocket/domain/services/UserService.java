package io.jrocket.domain.services;

import io.jrocket.infra.util.ApplicationException;

/**
 * User: OCTO-JBU
 * Date: 06/03/14
 * Time: 09:02
 */
public interface UserService {

    /**
     *
     * @param login The login of the user
     * @param password The password of the user
     * @return The user session token
     */
    String signIn(String login, String password) throws ApplicationException;

    /**
     *
     * @param sessionToken The user session token
     */
    void signOut(String sessionToken) throws ApplicationException;
}
