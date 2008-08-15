package de.mogwai.common.security;

import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.security.event.authentication.AuthenticationFailureBadCredentialsEvent;
import org.springframework.security.event.authentication.AuthenticationFailureLockedEvent;
import org.springframework.security.event.authentication.AuthenticationSuccessEvent;
import org.springframework.security.event.authorization.AuthorizationFailureEvent;


/**
 * ApplicationListener für Authentifizierungsmeldungen
 * 
 * Authentifizierungmeldungen werden hier abgefangen, und an einen
 * AuthenticationListener delegiert.
 * 
 * @author $Author: mirkosertic $
 * @version $Date: 2008-08-15 16:12:23 $
 */
public class AuthenticationApplicationListener implements ApplicationListener {
    
    private AuthenticationListener authenticationListener;
    
    /**
     * Gibt den Wert des Attributs <code>authenticationListener</code> zurück.
     * 
     * @return Wert des Attributs authenticationListener.
     */
    public AuthenticationListener getAuthenticationListener() {
        return authenticationListener;
    }

    /**
     * Setzt den Wert des Attributs <code>authenticationListener</code>.
     * 
     * @param authenticationListener Wert für das Attribut authenticationListener.
     */
    public void setAuthenticationListener(AuthenticationListener authenticationListener) {
        this.authenticationListener = authenticationListener;
    }

    /**
     * {@inheritDoc}
     */
    public void onApplicationEvent(ApplicationEvent aEvent) {
        if (aEvent instanceof AuthorizationFailureEvent) {
            
            // Zugriff verboten
            AuthorizationFailureEvent theEvent = (AuthorizationFailureEvent) aEvent;
            
            authenticationListener.handleAuthorizationFailureEvent(theEvent);
        }
        if (aEvent instanceof AuthenticationFailureBadCredentialsEvent) {

            // Ein fehlerhafter Login Versuch
            AuthenticationFailureBadCredentialsEvent theEvent = (AuthenticationFailureBadCredentialsEvent) aEvent;

            authenticationListener.handleBadCredentialsEvent(theEvent);
        }
        
        if (aEvent instanceof AuthenticationFailureLockedEvent) {

            AuthenticationFailureLockedEvent theEvent = (AuthenticationFailureLockedEvent) aEvent;

            authenticationListener.handleAuthenticationLockedEvent(theEvent);

        }
        
        if (aEvent instanceof AuthenticationSuccessEvent) {

            // Eine erfolgreiche Anmeldung
            AuthenticationSuccessEvent theEvent = (AuthenticationSuccessEvent) aEvent;

            authenticationListener.handleAuthenticationSuccessEvent(theEvent);
        }
        
    }
}
