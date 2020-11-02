package de.mogwai.common.security;

import org.springframework.security.Authentication;
import org.springframework.security.event.authentication.AbstractAuthenticationEvent;
import org.springframework.security.event.authentication.AuthenticationFailureBadCredentialsEvent;
import org.springframework.security.event.authentication.AuthenticationFailureLockedEvent;
import org.springframework.security.event.authentication.AuthenticationSuccessEvent;
import org.springframework.security.event.authorization.AuthorizationFailureEvent;
import org.springframework.security.userdetails.UserDetails;


/**
 * Adapter für den AuthenticationListener.
 * 
 * Dies sollte die Basisklasse für alle AuthenticationListener sein !!
 * 
 * @author $Author: mirkosertic $
 * @version $Date: 2008-09-04 18:35:38 $
 */
public class AuthenticationListenerAdapter implements AuthenticationListener {

    /**
     * Ermittlung der aktuellen UserID. 
     * 
     * @param aEvent das Event
     * @return die UserId
     */
    protected String getUserId(AbstractAuthenticationEvent aEvent) {
        Authentication theAuthentication = aEvent.getAuthentication();

        if (theAuthentication != null) {
            Object thePrincipal = theAuthentication.getPrincipal();
            if (thePrincipal instanceof UserDetails) {
                return ((UserDetails) thePrincipal).getUsername();
            }
            return thePrincipal.toString();
        }
        return null;
    }

    /**
     * {@inheritDoc}
     */
    public void handleAuthenticationSuccessEvent(AuthenticationSuccessEvent aEvent) {
    }

    /**
     * {@inheritDoc}
     */
    public void handleBadCredentialsEvent(AuthenticationFailureBadCredentialsEvent aEvent) {
    }

    /**
     * {@inheritDoc}
     */
    public void handleAuthorizationFailureEvent(AuthorizationFailureEvent aEvent) {
    }

    /**
     * {@inheritDoc}
     */
    public void handleAuthenticationLockedEvent(AuthenticationFailureLockedEvent aEvent) {
    }
}
