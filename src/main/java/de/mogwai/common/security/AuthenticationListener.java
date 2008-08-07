package de.mogwai.common.security;

import org.springframework.security.event.authentication.AuthenticationFailureBadCredentialsEvent;
import org.springframework.security.event.authentication.AuthenticationFailureLockedEvent;
import org.springframework.security.event.authentication.AuthenticationSuccessEvent;
import org.springframework.security.event.authorization.AuthorizationFailureEvent;

public interface AuthenticationListener {

    void handleAuthenticationSuccessEvent(AuthenticationSuccessEvent aEvent);

    void handleBadCredentialsEvent(AuthenticationFailureBadCredentialsEvent aEvent);

    void handleAuthorizationFailureEvent(AuthorizationFailureEvent aEvent);

    void handleAuthenticationLockedEvent(AuthenticationFailureLockedEvent aEvent);
}
