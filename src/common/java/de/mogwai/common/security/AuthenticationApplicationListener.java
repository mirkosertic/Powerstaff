package de.mogwai.common.security;

import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.security.event.authentication.AuthenticationFailureBadCredentialsEvent;
import org.springframework.security.event.authentication.AuthenticationFailureLockedEvent;
import org.springframework.security.event.authentication.AuthenticationSuccessEvent;
import org.springframework.security.event.authorization.AuthorizationFailureEvent;


/**
 * ApplicationListener für Authentifizierungsmeldungen
 * <p/>
 * Authentifizierungmeldungen werden hier abgefangen, und an einen
 * AuthenticationListener delegiert.
 *
 * @author $Author: mirkosertic $
 * @version $Date: 2008-09-04 18:35:39 $
 */
public class AuthenticationApplicationListener implements ApplicationListener {

    private AuthenticationListener authenticationListener;

    public void setAuthenticationListener(AuthenticationListener authenticationListener) {
        this.authenticationListener = authenticationListener;
    }

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
