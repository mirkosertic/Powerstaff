package de.mogwai.common.business.service;

public class LoginException extends Exception {

    public LoginException(String aMessage, Exception e) {
        super(aMessage, e);
    }
}
