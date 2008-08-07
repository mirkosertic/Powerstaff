package de.mogwai.common.business.service.impl;

import org.springframework.security.AuthenticationException;
import org.springframework.security.AuthenticationManager;
import org.springframework.security.concurrent.SessionIdentifierAware;
import org.springframework.security.context.SecurityContext;
import org.springframework.security.context.SecurityContextHolder;
import org.springframework.security.providers.UsernamePasswordAuthenticationToken;

import de.mogwai.common.business.service.LoginException;
import de.mogwai.common.business.service.LoginService;

/**
 * Implementierung des LoginServices.
 * 
 * @author $Author: mirkosertic $
 * @version $Date: 2008-08-07 18:21:03 $
 */
public class LoginServiceImpl implements LoginService {

    private AuthenticationManager authenticationManager;

    /**
     * Gibt den Wert des Attributs <code>authenticationManager</code> zurück.
     * 
     * @return Wert des Attributs authenticationManager.
     */
    public AuthenticationManager getAuthenticationManager() {
        return authenticationManager;
    }

    /**
     * Setzt den Wert des Attributs <code>authenticationManager</code>.
     * 
     * @param authenticationManager
     *                Wert für das Attribut authenticationManager.
     */
    public void setAuthenticationManager(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    /**
     * {@inheritDoc}
     */
    public void login(String aUsername, String aPassword, SessionIdentifierAware aSessionIdentifier) throws LoginException {

        UsernamePasswordAuthenticationToken theOriginalAuth = new UsernamePasswordAuthenticationToken(aUsername, aPassword);
        theOriginalAuth.setDetails(aSessionIdentifier);

        try {
            SecurityContext theSecurityContext = SecurityContextHolder.getContext();
            theSecurityContext.setAuthentication(authenticationManager.authenticate(theOriginalAuth));
        } catch (AuthenticationException e) {
            throw new LoginException("Fehler beim Login", e);
        }
    }
}
